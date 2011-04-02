package com.ceptrader.tradeapp;

import java.util.Arrays;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public aspect MainHelper {
	pointcut log() : call(* com.ceptrader.tradeapp..*.*(..)) &&
		!err() && 
		!call(* *toString*(..)) &&
		!within(MainHelper);
	
	pointcut err() : (call(* *.*err*(..))) && 
		!call(* *toString*(..)) &&
		!within(MainHelper) &&
		cflow(within(com.ceptrader.tradeapp..*)) &&
		!cflowbelow(within(com.ceptrader.tradeapp..*));
	
	before() : log() {
		final SourceLocation loc = thisJoinPoint.getSourceLocation();
		final Object[] args = thisJoinPoint.getArgs();		
		final Object target = thisJoinPoint.getTarget();
		final Signature sig = thisJoinPoint.getSignature();
		final String kind   = thisJoinPoint.getKind();
		
		System.out.println(MainHelper.locationInfo(kind, loc, args, target, sig));
	}
	
	before() : err() {
		final SourceLocation loc = thisJoinPoint.getSourceLocation();
		final Object[] args = thisJoinPoint.getArgs();		
		final Object target = thisJoinPoint.getTarget();
		final Signature sig = thisJoinPoint.getSignature();
		final String kind   = thisJoinPoint.getKind();
		
		System.err.println(MainHelper.locationInfo(kind, loc, args, target, sig));
	}
	
	public static StringBuilder locationInfo(final String kind, final SourceLocation loc, final Object[] args, final Object target, final Signature sig) {
		final StringBuilder sb = new StringBuilder(kind);
		sb.append(" @ :- ");
		sb.append(loc);
		
		sb.append("\n|\tTarget: ");
		if (target == null) {
			sb.append("<No / Unkown Target>");
		} else {
			sb.append("{Class: ");
			sb.append(target.getClass().getName());
			sb.append(", Value: ");
			sb.append(target.toString());
			sb.append("} ");
		}
		
		sb.append("\n|\tSignature: ");
		if (sig == null) {
			sb.append("<No Signature>");
		} else {
			sb.append("{");
			sb.append(sig.toLongString());
			sb.append("}");
		}
		
		sb.append("\n|\tWith Arguments: ");
		if (args == null || args.length == 0) {
			sb.append("<No Arguments>");
		} else {
			sb.append("(");
			sb.append(MainHelper.argListToString(args));
			sb.append(")");
		}
		
		sb.append("\n|\tStack Frame: ");
		Throwable t = new Throwable("Intospecting stack frame");
		t = t.fillInStackTrace();
		sb.append(Arrays.asList(t.getStackTrace()));
		sb.append("\n.");
		
		return sb;
	}
	
	
	public static StringBuilder argListToString(final Object[] args) {
		final StringBuilder sb = new StringBuilder();
		
		if (args.length > 0) {
			for (final Object i : args) {
				sb.append(i);
				sb.append(", ");
			}
		} else {
			sb.append("NA");
		}
		
		return sb;
	}	
	
	after() throwing(final Throwable e) : withincode(* com.ceptrader.tradeapp..*(..)) && call(* com.ceptrader.tradeapp..*(..)) && !within(*Helper*) {
		System.err.println(e);
	}
}
