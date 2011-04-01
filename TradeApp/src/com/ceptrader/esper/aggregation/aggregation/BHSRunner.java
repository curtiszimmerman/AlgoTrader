package com.ceptrader.esper.aggregation.aggregation;

import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;

import com.ceptrader.scripting.beanshell.ScriptUtils;
import com.espertech.esper.epl.agg.AggregationSupport;
import com.espertech.esper.epl.agg.AggregationValidationContext;

public class BHSRunner extends AggregationSupport {
	private Object	value;
	
	@Override
	public void clear() {
		value = 0;
	}
	
	@Override
	public void enter(final Object arg0) {
		final Object[] o = (Object[]) arg0;
		
		try {
			if (paramStart == 3) {
				value = ScriptUtils.evaluateRowRemoveVar((Interpreter) o[2],
				        o[0].toString(),
				        Arrays.copyOfRange(o, paramStart, o.length));
			} else {
				value = ScriptUtils.evaluateRowRemoveVar(o[0].toString(),
				        Arrays.copyOfRange(o, paramStart, o.length));
			}
		} catch (final EvalError e) {
			throw new RuntimeException("Error in evaluating BeanShell script",
			        e);
		}
	}
	
	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public Class<Object> getValueType() {
		return Object.class;
	}
	
	@Override
	public void leave(final Object arg0) {
		final Object[] o = (Object[]) arg0;
		
		try {
			if (paramStart == 3) {
				ScriptUtils.evaluateRowRemoveVar((Interpreter) o[2],
				        o[1].toString(),
				        Arrays.copyOfRange(o, paramStart, o.length));
			} else {
				ScriptUtils.evaluateRowRemoveVar(o[1].toString(),
				        Arrays.copyOfRange(o, paramStart, o.length));
			}
		} catch (final EvalError e) {
			throw new RuntimeException("Error in evaluating BeanShell script",
			        e);
		}
	}
	
	private int	paramLen	= 0;
	private int	paramStart	= 2;
	
	@Override
	public void validate(final AggregationValidationContext arg0) {
		final Class<?>[] paramTypes = arg0.getParameterTypes();
		final Object[] constVals = arg0.getConstantValues();
		final boolean[] isConst = arg0.getIsConstantValue();
		
		paramLen = paramTypes.length;
		
		if (paramLen <= 2) { throw new IllegalArgumentException(
		        "Requir at least 2 BeanShell scripts"); }
		
		if (paramTypes[1] != String.class) {
			if (!isConst[1] || isConst[1] && constVals[1] != null) { throw new IllegalArgumentException(
			                "Expecting BeanShell Script"); }
		}
		
		if (paramTypes[0] != String.class) {
			if (!isConst[0] || isConst[0] && constVals[0] != null) { throw new IllegalArgumentException(
			                "Expecting BeanShell Script"); }
		}
		
		if (paramTypes[2] == Interpreter.class) {
			paramStart = 3;
		}
	}
	
}
