package com.algoTrader.starter;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.service.SimulationServiceImpl;
import com.algoTrader.util.MyLogger;

public class SimulationStarter {

	private static Logger logger = MyLogger.getLogger(SimulationServiceImpl.class.getName());

	public static void main(String[] args) throws ConvergenceException, FunctionEvaluationException {

		ServiceLocator.serverInstance().init("beanRefFactorySimulation.xml");

		if ("simulateWithCurrentParams".equals(args[0])) {

			ServiceLocator.serverInstance().getSimulationService().simulateWithCurrentParams();

		} else if ("optimizeSingleParamLinear".equals(args[0])) {

			String strategyName = args[1];
			for (int i = 2; i < args.length; i++) {
				String[] params = args[i].split(":");
				String parameter = params[0];
				double min = Double.parseDouble(params[1]);
				double max = Double.parseDouble(params[2]);
				double increment = Double.parseDouble(params[3]);

				ServiceLocator.serverInstance().getSimulationService().optimizeSingleParamLinear(strategyName, parameter, min, max, increment);

			}
		}

		ServiceLocator.serverInstance().shutdown();
	}
}
