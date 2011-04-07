package com.algoTrader.starter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.algoTrader.ServerServiceLocator;
import com.algoTrader.ServiceLocator;
import com.algoTrader.util.MyLogger;

public class ServiceInvoker {
	
	private static Logger	      logger	= MyLogger
	                                             .getLogger(ServiceInvoker.class
	                                                     .getName());
	private static ServiceInvoker	invoker;
	
	public static void main(final String[] args) throws Exception {
		
		for (final String arg : args) {
			ServiceInvoker.getInstance().invoke(arg);
		}
	}
	
	public static ServiceInvoker getInstance() {
		
		if (ServiceInvoker.invoker == null) {
			ServiceInvoker.invoker = new ServiceInvoker();
		}
		return ServiceInvoker.invoker;
	}
	
	public Object invoke(final String call) {
		
		if (call == null) {
			ServiceInvoker.logger.warn("you must specifiy service and method");
			return "you must specifiy service and method";
		}
		
		final StringTokenizer tokenizer = new StringTokenizer(call, ":");
		
		final int len = tokenizer.countTokens();
		if (len < 2) {
			ServiceInvoker.logger.warn("you must specifiy service and method");
			return "you must specifiy service and method";
		}
		
		final String serviceName = tokenizer.nextToken();
		final String methodName = tokenizer.nextToken();
		
		final ServerServiceLocator serviceLocator = ServiceLocator
		        .serverInstance();
		
		try {
			final Method getServiceMethod = serviceLocator.getClass()
			        .getMethod("get" + serviceName, (Class[]) null);
			final Object service = getServiceMethod.invoke(serviceLocator,
			        new Object[] {});
			
			final Class<?>[] signature = new Class<?>[len - 2];
			final String[] params = new String[len - 2];
			int i = 0;
			while (tokenizer.hasMoreTokens()) {
				signature[i] = String.class;
				params[i] = tokenizer.nextToken();
				i++;
			}
			
			final Method method = service.getClass().getMethod(methodName,
			        signature);
			return method.invoke(service, (Object[]) params);
			
		} catch (final NoSuchMethodException e) {
			ServiceInvoker.logger.error(
			        "the specified service or method does not exist", e);
			return "the specified service or method does not exist";
		} catch (final InvocationTargetException e) {
			ServiceInvoker.logger.error("there was an error",
			        e.getTargetException());
			return e.getTargetException().getMessage();
		} catch (final Exception e) {
			ServiceInvoker.logger.error("there was an error", e);
			return e.getMessage();
		}
	}
}
