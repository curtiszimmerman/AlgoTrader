package com.algoTrader.starter;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.service.SimulationServiceImpl;
import com.algoTrader.util.MyLogger;

public class SimulationStarter {

	public static Logger logger = MyLogger.getLogger(SimulationServiceImpl.class.getName());

	public static void main(String[] args) throws ConvergenceException, FunctionEvaluationException {

		ServiceLocator.serverInstance().init("beanRefFactorySimulation.xml");

		ServiceLocator.serverInstance().getSimulationService().simulateWithCurrentParams();

		ServiceLocator.serverInstance().shutdown();
	}
}