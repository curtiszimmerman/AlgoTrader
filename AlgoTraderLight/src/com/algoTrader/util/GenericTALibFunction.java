package com.algoTrader.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.buffer.CircularFifoBuffer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

import com.espertech.esper.epl.agg.AggregationSupport;
import com.espertech.esper.epl.agg.AggregationValidationContext;
import com.tictactec.ta.lib.CoreAnnotated;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import com.tictactec.ta.lib.meta.annotation.InputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.InputParameterType;
import com.tictactec.ta.lib.meta.annotation.OptInputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.OptInputParameterType;
import com.tictactec.ta.lib.meta.annotation.OutputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.OutputParameterType;

/**
 * 
 * Generic AggregateFunction to support all TA-Lib operations
 * <p/>
 * To use the AggregateFunction add the following to the esper configuration
 * 
 * <pre>
 * &lt;plugin-aggregation-function name="talib" function-class="com.algoTrader.util.GenericTALibFunction"/&gt;
 * </pre>
 * 
 * The AggregationFunction can be used in an esper statement like this:
 * 
 * <pre>
 * select talib("stochF", high, low, close, 3, 2, "Sma")
 * from OHLCBar;
 * </pre>
 * 
 * If the TA-Lib Function returns just one value, the value is directly exposed
 * by the AggregationFunction. If the TA-Lib Function returns multiple-values,
 * they will be exposed by a Map
 * </p>
 * The AggregationFunction needs the following libraries:
 * </p> <li><a href="http://commons.apache.org/collections/">Apache Commons
 * Collection</a></li> <li><a href="http://larvalabs.com/collections/">Commons
 * Generics</a></li> <li><a href="http://ta-lib.org/">TA-Lib</a></li> </p>
 * 
 * @author Andy Flury
 * 
 */
public class GenericTALibFunction extends AggregationSupport {
	
	private final CoreAnnotated	                   core;
	private String	                               functionName;
	private Method	                               function;
	private final List<CircularFifoBuffer<Number>>	inputParams;
	private final List<Object>	                   optInputParams;
	private final Map<String, Object>	           outputParams;
	
	public GenericTALibFunction() {
		super();
		core = new CoreAnnotated();
		inputParams = new ArrayList<CircularFifoBuffer<Number>>();
		optInputParams = new ArrayList<Object>();
		outputParams = new HashMap<String, Object>();
	}
	
	@Override
	public void validate(final AggregationValidationContext validationContext) {
		
		final boolean[] isConstant = validationContext.getIsConstantValue();
		final Object[] constants = validationContext.getConstantValues();
		final Class<?>[] paramTypes = validationContext.getParameterTypes();
		
		// get the functionname
		if (isConstant[0] && paramTypes[0].equals(String.class)) {
			functionName = (String) constants[0];
		} else {
			throw new IllegalArgumentException(
			        "param 0 has to be a constant and of type String");
		}
		
		// get the method by iterating over all core-methods
		// we have to do it this way, since we don't have the exact parameters
		for (final Method method : core.getClass().getDeclaredMethods()) {
			if (method.getName().equals(functionName)) {
				function = method;
				break;
			}
		}
		
		// check that we have a function now
		if (function == null) { throw new IllegalArgumentException("function " +
		        functionName + " was not found"); }
		
		// get the parameters
		int paramCounter = 1;
		int inputParamCount = 0;
		for (final Annotation[] annotations : function
		        .getParameterAnnotations()) {
			for (final Annotation annotation : annotations) {
				
				// got through all inputParameters and count them
				if (annotation instanceof InputParameterInfo) {
					final InputParameterInfo inputParameterInfo = (InputParameterInfo) annotation;
					if (inputParameterInfo.type().equals(
					        InputParameterType.TA_Input_Real)) {
						if (paramTypes[paramCounter].equals(double.class)) {
							inputParamCount++;
							paramCounter++;
						} else {
							throw new IllegalArgumentException("param number " +
							        paramCounter +
							        " needs must be of type double");
						}
					} else if (inputParameterInfo.type().equals(
					        InputParameterType.TA_Input_Integer)) {
						if (paramTypes[paramCounter].equals(int.class)) {
							inputParamCount++;
							paramCounter++;
						} else {
							throw new IllegalArgumentException("param number " +
							        paramCounter + " needs must be of type int");
						}
					} else if (inputParameterInfo.type().equals(
					        InputParameterType.TA_Input_Price)) {
						
						// the flags define the number of parameters in use by a
						// bitwise or
						final int priceParamSize = numberOfSetBits(inputParameterInfo
						        .flags());
						for (int i = 0; i < priceParamSize; i++) {
							if (paramTypes[paramCounter].equals(double.class)) {
								inputParamCount++;
								paramCounter++;
							} else {
								throw new IllegalArgumentException(
								        "param number " + paramCounter +
								                " needs must be of type double");
							}
						}
					}
					
					// got through all optInputParameters and store them for
					// later
				} else if (annotation instanceof OptInputParameterInfo) {
					final OptInputParameterInfo optInputParameterInfo = (OptInputParameterInfo) annotation;
					if (optInputParameterInfo.type().equals(
					        OptInputParameterType.TA_OptInput_IntegerRange)) {
						if (isConstant[paramCounter] &&
						        paramTypes[paramCounter].equals(Integer.class)) {
							optInputParams.add(constants[paramCounter]);
						} else {
							throw new IllegalArgumentException("param number " +
							        paramCounter +
							        " needs to be a constant of type int");
						}
					} else if (optInputParameterInfo.type().equals(
					        OptInputParameterType.TA_OptInput_RealRange)) {
						if (isConstant[paramCounter] &&
						        paramTypes[paramCounter].equals(Double.class)) {
							optInputParams.add(constants[paramCounter]);
						} else {
							throw new IllegalArgumentException("param number " +
							        paramCounter +
							        " needs to be a constant of type double");
						}
					} else if (optInputParameterInfo.type().equals(
					        OptInputParameterType.TA_OptInput_IntegerList)) {
						if (isConstant[paramCounter] &&
						        paramTypes[paramCounter].equals(String.class)) {
							final String value = (String) constants[paramCounter];
							
							// get the MAType Enum from the value
							final MAType type = MAType.valueOf(value);
							optInputParams.add(type);
						} else {
							throw new IllegalArgumentException("param number " +
							        paramCounter +
							        " needs to be a constant of type String");
						}
					}
					paramCounter++;
					
					// to through all outputParameters and store them
				} else if (annotation instanceof OutputParameterInfo) {
					final OutputParameterInfo outputParameterInfo = (OutputParameterInfo) annotation;
					if (outputParameterInfo.type().equals(
					        OutputParameterType.TA_Output_Real)) {
						outputParams.put(outputParameterInfo.paramName(),
						        new double[1]);
					} else if (outputParameterInfo.type().equals(
					        OutputParameterType.TA_Output_Integer)) {
						outputParams.put(outputParameterInfo.paramName(),
						        new int[1]);
					}
				}
			}
		}
		
		// get the lookback size
		try {
			final Object[] args = new Object[optInputParams.size()];
			final Class<?>[] argTypes = new Class[optInputParams.size()];
			
			// supply all optInputParams
			int argCount = 0;
			for (final Object object : optInputParams) {
				args[argCount] = object;
				final Class<?> clazz = object.getClass();
				final Class<?> primitiveClass = ClassUtils
				        .wrapperToPrimitive(clazz);
				if (primitiveClass != null) {
					argTypes[argCount] = primitiveClass;
				} else {
					argTypes[argCount] = clazz;
				}
				argCount++;
			}
			
			// get and invoke the lookback method
			final Method lookback = core.getClass().getMethod(
			        functionName + "Lookback", argTypes);
			final int lookbackPeriod = (Integer) lookback.invoke(core, args) + 1;
			
			// create the fixed size Buffers
			for (int i = 0; i < inputParamCount; i++) {
				inputParams.add(new CircularFifoBuffer<Number>(lookbackPeriod));
			}
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void enter(final Object obj) {
		
		final Object[] params = (Object[]) obj;
		
		// add all inputs to the correct buffers
		int paramCount = 1;
		for (final CircularFifoBuffer<Number> buffer : inputParams) {
			final Number value = (Number) params[paramCount];
			buffer.add(value);
			paramCount++;
		}
	}
	
	@Override
	public void leave(final Object obj) {
		
		// Remove the last element of each buffer
		int paramCount = 1;
		for (final CircularFifoBuffer<Number> buffer : inputParams) {
			buffer.remove();
			paramCount++;
		}
	}
	
	@Override
	public Class<?> getValueType() {
		
		// if we only have one outPutParam return that value
		// otherwise return a Map
		if (outputParams.size() == 1) {
			return outputParams.values().iterator().next().getClass();
		} else {
			return Map.class;
		}
	}
	
	@Override
	public Object getValue() {
		
		try {
			// get the total number of parameters
			final int numberOfArgs = 2 + inputParams.size() +
			        optInputParams.size() + 2 + outputParams.size();
			final Object[] args = new Object[numberOfArgs];
			
			// get the size of the first input buffer
			final int elements = inputParams.iterator().next().size();
			
			args[0] = elements - 1; // startIdx
			args[1] = elements - 1; // endIdx
			
			// inputParams
			int argCount = 2;
			for (final CircularFifoBuffer<Number> buffer : inputParams) {
				
				// look at the first element of the buffer to determine the type
				final Object firstElement = buffer.iterator().next();
				if (firstElement instanceof Double) {
					args[argCount] = ArrayUtils.toPrimitive(buffer
					        .toArray(new Double[0]));
				} else if (firstElement instanceof Integer) {
					args[argCount] = ArrayUtils.toPrimitive(buffer
					        .toArray(new Integer[0]));
				} else {
					throw new IllegalArgumentException("unsupported type " +
					        firstElement.getClass());
				}
				argCount++;
			}
			
			// optInputParams
			for (final Object object : optInputParams) {
				args[argCount] = object;
				argCount++;
			}
			
			// begin
			final MInteger begin = new MInteger();
			args[argCount] = begin;
			argCount++;
			
			// length
			final MInteger length = new MInteger();
			args[argCount] = length;
			argCount++;
			
			// OutputParams
			for (final Map.Entry<String, Object> entry : outputParams
			        .entrySet()) {
				args[argCount++] = entry.getValue();
			}
			
			// invoke the function
			final RetCode retCode = (RetCode) function.invoke(core, args);
			
			if (retCode == RetCode.Success) {
				if (length.value == 0) { return null; }
				
				// if we only have one outPutParam return that value
				// otherwise return a Map
				if (outputParams.size() == 1) {
					final Object value = outputParams.values().iterator()
					        .next();
					return getNumberFromNumberArray(value);
				} else {
					final Map<String, Object> returnMap = new HashMap<String, Object>();
					for (final Map.Entry<String, Object> entry : outputParams
					        .entrySet()) {
						final Number number = getNumberFromNumberArray(entry
						        .getValue());
						final String name = entry.getKey().substring(3);
						returnMap.put(name, number);
					}
					return returnMap;
				}
			} else {
				throw new RuntimeException(retCode.toString());
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void clear() {
		inputParams.clear();
		outputParams.clear();
	}
	
	private Number getNumberFromNumberArray(final Object value) {
		
		if (value instanceof double[]) {
			return ((double[]) value)[0];
		} else if (value instanceof int[]) {
			return ((int[]) value)[0];
		} else {
			throw new IllegalArgumentException(value.getClass() +
			        " not supported");
		}
	}
	
	private int numberOfSetBits(int i) {
		i = i - (i >> 1 & 0x55555555);
		i = (i & 0x33333333) + (i >> 2 & 0x33333333);
		return (i + (i >> 4) & 0xF0F0F0F) * 0x1010101 >> 24;
	}
}