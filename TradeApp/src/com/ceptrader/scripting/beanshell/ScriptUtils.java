package com.ceptrader.scripting.beanshell;

import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

import com.ceptrader.tradeapp.util.Logger;

public class ScriptUtils {
	private static Interpreter	bshIntpr	= new Interpreter();
	
	public static Interpreter getBShInterpreter() {
		return ScriptUtils.bshIntpr;
	}
	
	public static Interpreter getNewBShInterpreter() {
		return new Interpreter();
	}
	
	public static synchronized Object runBShScript(
	        final String cmd)
	        throws EvalError {
		return ScriptUtils.runBShScript(ScriptUtils.bshIntpr, cmd);
	}
	
	public static synchronized Object runBShScript(final Interpreter bshIntpr,
	        final String cmd)
	        throws EvalError {
		return bshIntpr.eval(cmd);
	}
	
	public static synchronized ScriptUtils
	        regVar(final Map<String, Object> vars)
	                throws EvalError {
		return ScriptUtils.regVar(ScriptUtils.bshIntpr, vars);
	}
	
	public static synchronized ScriptUtils
	        regVar(final Interpreter bshIntpr, final Map<String, Object> vars)
	                throws EvalError {
		for (final Map.Entry<String, Object> i : vars
		        .entrySet()) {
			bshIntpr.set(i.getKey(), i.getValue());
		}
		
		return new ScriptUtils();
	}
	
	public static synchronized ScriptUtils unregVarFromArray(
	        final String[] names,
	        final Object[] vars)
	        throws EvalError {
		return ScriptUtils.unregVarFromArray(ScriptUtils.bshIntpr, names, vars);
	}
	
	public static synchronized ScriptUtils unregVarFromArray(
	        final Interpreter bshIntpr,
	        final String[] names,
	        final Object[] vars)
	        throws EvalError {
		final int j = 0;
		for (final String i : names) {
			bshIntpr.unset(i);
		}
		
		return new ScriptUtils();
	}
	
	public static synchronized ScriptUtils
	        unregVar(final Map<String, Object> vars)
	                throws EvalError {
		return ScriptUtils.unregVar(ScriptUtils.bshIntpr, vars);
	}
	
	public static synchronized
	        ScriptUtils
	        unregVar(final Interpreter bshIntpr, final Map<String, Object> vars)
	                throws EvalError {
		for (final Map.Entry<String, Object> i : vars
		        .entrySet()) {
			bshIntpr.unset(i.getKey());
		}
		
		return new ScriptUtils();
	}
	
	public static synchronized ScriptUtils regVarFromArray(
	        final String[] names,
	        final Object[] vars)
	        throws EvalError {
		return ScriptUtils.regVarFromArray(ScriptUtils.bshIntpr, names, vars);
	}
	
	public static synchronized ScriptUtils regVarFromArray(
	        final Interpreter bshIntpr,
	        final String[] names,
	        final Object[] vars)
	        throws EvalError {
		final int j = 0;
		for (final String i : names) {
			bshIntpr.set(i, j < vars.length ? vars[j] : null);
		}
		
		return new ScriptUtils();
	}
	
	public synchronized static ScriptUtils regVarFromOneArray(
	        final Object... args)
	        throws EvalError {
		return ScriptUtils.regVarFromOneArray(ScriptUtils.bshIntpr, args);
	}
	
	public synchronized static ScriptUtils regVarFromOneArray(
	        final Interpreter bshIntpr,
	        final Object... args)
	        throws EvalError {
		int i;
		for (i = 0; i < args.length; i += 2) {
			if (args[i] != null) {
				bshIntpr.set(args[i].toString(), args[i + 1]);
			}
		}
		
		if (--i < args.length) {
			if (args[i] != null) {
				bshIntpr.set(args[i].toString(), null);
			}
		}
		
		return new ScriptUtils();
	}
	
	public synchronized static ScriptUtils unregVarFromOneArray(
	        final Object... args)
	        throws EvalError {
		return ScriptUtils.unregVarFromOneArray(ScriptUtils.bshIntpr, args);
	}
	
	public synchronized static ScriptUtils unregVarFromOneArray(
	        final Interpreter bshIntpr,
	        final Object... args)
	        throws EvalError {
		int i;
		for (i = 0; i < args.length; i += 2) {
			if (args[i] != null) {
				try {
					bshIntpr.unset(args[i].toString());
				} catch (final Throwable t) {
					Logger.log(t);
				}
			}
		}
		
		if (--i < args.length) {
			if (args[i] != null) {
				try {
					bshIntpr.unset(args[i].toString());
				} catch (final Throwable t) {
					Logger.log(t);
				}
			}
		}
		
		return new ScriptUtils();
	}
	
	public synchronized static Object evaluateRowRetainVar(
	        final String exp,
	        final Object... args) throws EvalError {
		return ScriptUtils
		        .evaluateRowRetainVar(ScriptUtils.bshIntpr, exp, args);
	}
	
	public synchronized static Object evaluateRowRetainVar(
	        final Interpreter bshIntpr,
	        final String exp,
	        final Object... args) throws EvalError {
		ScriptUtils.regVarFromOneArray(bshIntpr, args);
		
		return bshIntpr.eval(exp);
	}
	
	public synchronized static Object evaluateRowRemoveVar(
	        final String exp,
	        final Object... args) throws EvalError {
		return ScriptUtils
		        .evaluateRowRemoveVar(ScriptUtils.bshIntpr, exp, args);
	}
	
	public synchronized static Object evaluateRowRemoveVar(
	        final Interpreter bshIntpr,
	        final String exp,
	        final Object... args) throws EvalError {
		try {
			return ScriptUtils.evaluateRowRetainVar(bshIntpr, exp, args);
		} finally {
			ScriptUtils.unregVarFromOneArray(bshIntpr, args);
		}
	}
}
