package TradeApp.copy;

import java.util.Arrays;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public aspect MainHelper {
	pointcut log() : call(* TradeApp..*.*(..)) &&
		!err() && 
		!call(* *toString*(..)) &&
		!within(MainHelper);
	
	pointcut err() : (call(* *.*err*(..))) && 
		!call(* *toString*(..)) &&
		!within(MainHelper) &&
		cflow(within(TradeApp..*)) &&
		!cflowbelow(within(TradeApp..*));
	
	before() : log() {
		SourceLocation loc = thisJoinPoint.getSourceLocation();
		Object[] args = thisJoinPoint.getArgs();		
		Object target = thisJoinPoint.getTarget();
		Signature sig = thisJoinPoint.getSignature();
		String kind   = thisJoinPoint.getKind();
		
		System.out.println(locationInfo(kind, loc, args, target, sig));
	}
	
	before() : err() {
		SourceLocation loc = thisJoinPoint.getSourceLocation();
		Object[] args = thisJoinPoint.getArgs();		
		Object target = thisJoinPoint.getTarget();
		Signature sig = thisJoinPoint.getSignature();
		String kind   = thisJoinPoint.getKind();
		
		System.err.println(locationInfo(kind, loc, args, target, sig));
	}
	
	public static StringBuilder locationInfo(String kind, SourceLocation loc, Object[] args, Object target, Signature sig) {
		StringBuilder sb = new StringBuilder(kind);
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
			sb.append(argListToString(args));
			sb.append(")");
		}
		
		sb.append("\n|\tStack Frame: ");
		Throwable t = new Throwable("Intospecting stack frame");
		t = t.fillInStackTrace();
		sb.append(Arrays.asList(t.getStackTrace()));
		sb.append("\n.");
		
		return sb;
	}
	
	
	public static StringBuilder argListToString(Object[] args) {
		StringBuilder sb = new StringBuilder();
		
		if (args.length > 0) {
			for (Object i : args) {
				sb.append(i);
				sb.append(", ");
			}
		} else {
			sb.append("NA");
		}
		
		return sb;
	}	
	
	after() throwing(Throwable e) : withincode(* TradeApp..*(..)) && call(* TradeApp..*(..)) && !within(*Helper*) {
		System.err.println(e);
	}
}
