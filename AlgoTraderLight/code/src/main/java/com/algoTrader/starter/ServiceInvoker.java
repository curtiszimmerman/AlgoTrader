package com.algoTrader.starter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.algoTrader.ServerServiceLocator;
import com.algoTrader.ServiceLocator;
import com.algoTrader.util.MyLogger;

public class ServiceInvoker {

	private static Logger logger = MyLogger.getLogger(ServiceInvoker.class.getName());
	private static ServiceInvoker invoker;

	public static void main(String[] args) throws Exception {

		for (String arg : args) {
			getInstance().invoke(arg);
		}
	}

	public static ServiceInvoker getInstance() {

		if (invoker == null) invoker = new ServiceInvoker();
		return invoker;
	}

	public Object invoke(String call)  {

		if (call == null) {
			logger.warn("you must specifiy service and method");
			return "you must specifiy service and method";
		}

		StringTokenizer tokenizer = new StringTokenizer(call, ":");

		int len = tokenizer.countTokens();
		if (len < 2) {
			logger.warn("you must specifiy service and method");
			return "you must specifiy service and method";
		}

		String serviceName = tokenizer.nextToken();
		String methodName = tokenizer.nextToken();

		ServerServiceLocator serviceLocator = ServiceLocator.serverInstance();

		try {
			Method getServiceMethod = serviceLocator.getClass().getMethod("get" + serviceName, (Class[])null);
			Object service = getServiceMethod.invoke(serviceLocator, new Object[] {});

			Class<?>[] signature = new Class<?>[len - 2];
			String[] params = new String[len - 2];
			int i = 0;
			while (tokenizer.hasMoreTokens()) {
				signature[i] = String.class;
				params[i] = tokenizer.nextToken();
				i++;
			}

			Method method = service.getClass().getMethod(methodName, signature);
			return method.invoke(service, (Object[])params);

		} catch (NoSuchMethodException e) {
			logger.error("the specified service or method does not exist", e);
			return "the specified service or method does not exist";
		} catch (InvocationTargetException e) {
			logger.error("there was an error", e.getTargetException());
			return e.getTargetException().getMessage();
		} catch (Exception e) {
			logger.error("there was an error", e);
			return e.getMessage();
		}
	}
}
