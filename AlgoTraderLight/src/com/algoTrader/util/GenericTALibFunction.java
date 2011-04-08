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
 * <pre>
 * &lt;plugin-aggregation-function name="talib" function-class="com.algoTrader.util.GenericTALibFunction"/&gt;
 * </pre>
 * The AggregationFunction can be used in an esper statement like this:
 * <pre>
 * select talib("stochF", high, low, close, 3, 2, "Sma")
 * from OHLCBar;
 * </pre>
 * If the TA-Lib Function returns just one value, the value is directly exposed by the AggregationFunction.
 * If the TA-Lib Function returns multiple-values, they will be exposed by a Map
 * </p>
 * The AggregationFunction needs the following libraries:
 * </p>
 * <li><a href="http://commons.apache.org/collections/">Apache Commons Collection</a></li>
 * <li><a href="http://larvalabs.com/collections/">Commons Generics</a></li>
 * <li><a href="http://ta-lib.org/">TA-Lib</a></li>
 * </p>
 * @author Andy Flury
 *
 */
public class GenericTALibFunction extends AggregationSupport {

	private CoreAnnotated core;
	private String functionName;
	private Method function;
	private List<CircularFifoBuffer<Number>> inputParams;
	private List<Object> optInputParams;
	private Map<String, Object> outputParams;

	public GenericTALibFunction() {
		super();
		this.core = new CoreAnnotated();
		this.inputParams = new ArrayList<CircularFifoBuffer<Number>>();
		this.optInputParams = new ArrayList<Object>();
		this.outputParams = new HashMap<String, Object>();
	}

	public void validate(AggregationValidationContext validationContext) {

		boolean[] isConstant = validationContext.getIsConstantValue();
		Object[] constants = validationContext.getConstantValues();
		Class<?>[] paramTypes = validationContext.getParameterTypes();

		// get the functionname
		if (isConstant[0] && paramTypes[0].equals(String.class)) {
			this.functionName = (String) constants[0];
		} else {
			throw new IllegalArgumentException("param 0 has to be a constant and of type String");
		}

		// get the method by iterating over all core-methods
		// we have to do it this way, since we don't have the exact parameters
		for (Method method : this.core.getClass().getDeclaredMethods()) {
			if (method.getName().equals(this.functionName)) {
				this.function = method;
				break;
			}
		}

		// check that we have a function now
		if (this.function == null) {
			throw new IllegalArgumentException("function " + this.functionName + " was not found");
		}

		// get the parameters
		int paramCounter = 1;
		int inputParamCount = 0;
		for (Annotation[] annotations : this.function.getParameterAnnotations()) {
			for (Annotation annotation : annotations) {

				// got through all inputParameters and count them
				if (annotation instanceof InputParameterInfo) {
					InputParameterInfo inputParameterInfo = (InputParameterInfo) annotation;
					if (inputParameterInfo.type().equals(InputParameterType.TA_Input_Real)) {
						if (paramTypes[paramCounter].equals(double.class)) {
							inputParamCount++;
							paramCounter++;
						} else {
							throw new IllegalArgumentException("param number " + paramCounter + " needs must be of type double");
						}
					} else if (inputParameterInfo.type().equals(InputParameterType.TA_Input_Integer)) {
						if (paramTypes[paramCounter].equals(int.class)) {
							inputParamCount++;
							paramCounter++;
						} else {
							throw new IllegalArgumentException("param number " + paramCounter + " needs must be of type int");
						}
					} else if (inputParameterInfo.type().equals(InputParameterType.TA_Input_Price)) {

						// the flags define the number of parameters in use by a bitwise or
						int priceParamSize = numberOfSetBits(inputParameterInfo.flags());
						for (int i = 0; i < priceParamSize; i++) {
							if (paramTypes[paramCounter].equals(double.class)) {
								inputParamCount++;
								paramCounter++;
							} else {
								throw new IllegalArgumentException("param number " + paramCounter + " needs must be of type double");
							}
						}
					}

					// got through all optInputParameters and store them for later
				} else if (annotation instanceof OptInputParameterInfo) {
					OptInputParameterInfo optInputParameterInfo = (OptInputParameterInfo) annotation;
					if (optInputParameterInfo.type().equals(OptInputParameterType.TA_OptInput_IntegerRange)) {
						if (isConstant[paramCounter] && paramTypes[paramCounter].equals(Integer.class)) {
							this.optInputParams.add(constants[paramCounter]);
						} else {
							throw new IllegalArgumentException("param number " + paramCounter + " needs to be a constant of type int");
						}
					} else if (optInputParameterInfo.type().equals(OptInputParameterType.TA_OptInput_RealRange)) {
						if (isConstant[paramCounter] && paramTypes[paramCounter].equals(Double.class)) {
							this.optInputParams.add(constants[paramCounter]);
						} else {
							throw new IllegalArgumentException("param number " + paramCounter + " needs to be a constant of type double");
						}
					} else if (optInputParameterInfo.type().equals(OptInputParameterType.TA_OptInput_IntegerList)) {
						if (isConstant[paramCounter] && paramTypes[paramCounter].equals(String.class)) {
							String value = (String) constants[paramCounter];

							// get the MAType Enum from the value
							MAType type = MAType.valueOf(value);
							this.optInputParams.add(type);
						} else {
							throw new IllegalArgumentException("param number " + paramCounter + " needs to be a constant of type String");
						}
					}
					paramCounter++;

					// to through all outputParameters and store them
				} else if (annotation instanceof OutputParameterInfo) {
					OutputParameterInfo outputParameterInfo = (OutputParameterInfo) annotation;
					if (outputParameterInfo.type().equals(OutputParameterType.TA_Output_Real)) {
						this.outputParams.put(outputParameterInfo.paramName(), new double[1]);
					} else if (outputParameterInfo.type().equals(OutputParameterType.TA_Output_Integer)) {
						this.outputParams.put(outputParameterInfo.paramName(), new int[1]);
					}
				}
			}
		}

		// get the lookback size
		try {
			Object[] args = new Object[this.optInputParams.size()];
			Class<?>[] argTypes = new Class[this.optInputParams.size()];

			// supply all optInputParams
			int argCount = 0;
			for (Object object : this.optInputParams) {
				args[argCount] = object;
				Class<?> clazz = object.getClass();
				Class<?> primitiveClass = ClassUtils.wrapperToPrimitive(clazz);
				if (primitiveClass != null) {
					argTypes[argCount] = primitiveClass;
				} else {
					argTypes[argCount] = clazz;
				}
				argCount++;
			}

			// get and invoke the lookback method
			Method lookback = this.core.getClass().getMethod(this.functionName + "Lookback", argTypes);
			int lookbackPeriod = (Integer) lookback.invoke(this.core, args) + 1;

			// create the fixed size Buffers
			for (int i = 0; i < inputParamCount; i++) {
				this.inputParams.add(new CircularFifoBuffer<Number>(lookbackPeriod));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void enter(Object obj) {

		Object[] params = (Object[]) obj;

		// add all inputs to the correct buffers
		int paramCount = 1;
		for (CircularFifoBuffer<Number> buffer : this.inputParams) {
			Number value = (Number) params[paramCount];
			buffer.add(value);
			paramCount++;
		}
	}

	public void leave(Object obj) {

		// Remove the last element of each buffer
		int paramCount = 1;
		for (CircularFifoBuffer<Number> buffer : this.inputParams) {
			buffer.remove();
			paramCount++;
		}
	}

	public Class<?> getValueType() {

		// if we only have one outPutParam return that value
		// otherwise return a Map
		if (this.outputParams.size() == 1) {
			return this.outputParams.values().iterator().next().getClass();
		} else {
			return Map.class;
		}
	}

	public Object getValue() {

		try {
			// get the total number of parameters
			int numberOfArgs = 2 + this.inputParams.size() + this.optInputParams.size() + 2 + this.outputParams.size();
			Object[] args = new Object[numberOfArgs];

			// get the size of the first input buffer
			int elements = this.inputParams.iterator().next().size();

			args[0] = elements - 1; // startIdx
			args[1] = elements - 1; // endIdx

			// inputParams
			int argCount = 2;
			for (CircularFifoBuffer<Number> buffer : this.inputParams) {

				// look at the first element of the buffer to determine the type
				Object firstElement = buffer.iterator().next();
				if (firstElement instanceof Double) {
					args[argCount] = ArrayUtils.toPrimitive(buffer.toArray(new Double[0]));
				} else if (firstElement instanceof Integer) {
					args[argCount] = ArrayUtils.toPrimitive(buffer.toArray(new Integer[0]));
				} else {
					throw new IllegalArgumentException("unsupported type " + firstElement.getClass());
				}
				argCount++;
			}

			// optInputParams
			for (Object object : this.optInputParams) {
				args[argCount] = object;
				argCount++;
			}

			// begin
			MInteger begin = new MInteger();
			args[argCount] = begin;
			argCount++;

			// length
			MInteger length = new MInteger();
			args[argCount] = length;
			argCount++;

			// OutputParams
			for (Map.Entry<String, Object> entry : this.outputParams.entrySet()) {
				args[argCount++] = entry.getValue();
			}

			// invoke the function
			RetCode retCode = (RetCode) this.function.invoke(this.core, args);

			if (retCode == RetCode.Success) {
				if (length.value == 0)
					return null;

				// if we only have one outPutParam return that value
				// otherwise return a Map
				if (this.outputParams.size() == 1) {
					Object value = this.outputParams.values().iterator().next();
					return getNumberFromNumberArray(value);
				} else {
					Map<String, Object> returnMap = new HashMap<String, Object>();
					for (Map.Entry<String, Object> entry : this.outputParams.entrySet()) {
						Number number = getNumberFromNumberArray(entry.getValue());
						String name = entry.getKey().substring(3);
						returnMap.put(name, number);
					}
					return returnMap;
				}
			} else {
				throw new RuntimeException(retCode.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void clear() {
		inputParams.clear();
		outputParams.clear();
	}

	private Number getNumberFromNumberArray(Object value) {

		if (value instanceof double[]) {
			return ((double[]) value)[0];
		} else if (value instanceof int[]) {
			return ((int[]) value)[0];
		} else {
			throw new IllegalArgumentException(value.getClass() + " not supported");
		}
	}

	private int numberOfSetBits(int i) {
		i = i - ((i >> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
		return ((i + (i >> 4) & 0xF0F0F0F) * 0x1010101) >> 24;
	}
}