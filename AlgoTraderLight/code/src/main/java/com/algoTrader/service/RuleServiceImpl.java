package com.algoTrader.service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.io.CsvTickInputAdapter;
import com.algoTrader.util.io.CsvTickInputAdapterSpec;
import com.algoTrader.util.io.CsvBarInputAdapter;
import com.algoTrader.util.io.CsvBarInputAdapterSpec;
import com.espertech.esper.adapter.InputAdapter;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationVariable;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPPreparedStatementImpl;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.annotation.Tag;
import com.espertech.esper.client.deploy.DeploymentInformation;
import com.espertech.esper.client.deploy.DeploymentOptions;
import com.espertech.esper.client.deploy.DeploymentResult;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;
import com.espertech.esper.client.deploy.ModuleItem;
import com.espertech.esper.client.soda.AnnotationAttribute;
import com.espertech.esper.client.soda.AnnotationPart;
import com.espertech.esper.client.soda.EPStatementObjectModel;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.TimerControlEvent;
import com.espertech.esper.core.EPServiceProviderImpl;
import com.espertech.esper.util.JavaClassHelper;
import com.espertech.esperio.AdapterCoordinator;
import com.espertech.esperio.AdapterCoordinatorImpl;
import com.espertech.esperio.csv.CSVInputAdapter;
import com.espertech.esperio.csv.CSVInputAdapterSpec;

public class RuleServiceImpl extends RuleServiceBase {

	private static Logger logger = MyLogger.getLogger(RuleServiceImpl.class.getName());

	private static final long initTime = 631148400000l; // 01.01.1990

	private Map<String, AdapterCoordinator> coordinators = new HashMap<String, AdapterCoordinator>();
	private Map<String, Boolean> internalClock = new HashMap<String, Boolean>();
	private Map<String, EPServiceProvider> serviceProviders = new HashMap<String, EPServiceProvider>();

	protected void handleInitServiceProvider(String strategyName) {

		String providerURI = getProviderURI(strategyName);

		Configuration configuration = new Configuration();
		configuration.configure("esper-" + providerURI.toLowerCase() + ".cfg.xml");

		initVariables(strategyName, configuration);

		Strategy strategy = getLookupService().getStrategyByNameFetched(strategyName);
		configuration.getVariables().get("engineStrategy").setInitializationValue(strategy);

		EPServiceProvider serviceProvider = EPServiceProviderManager.getProvider(providerURI, configuration);

		// must send time event before first schedule pattern
		serviceProvider.getEPRuntime().sendEvent(new CurrentTimeEvent(initTime));
		this.internalClock.put(strategyName, false);

		this.serviceProviders.put(providerURI, serviceProvider);

		logger.debug("initialized service provider: " + strategyName);
	}

	protected boolean handleIsInitialized(String strategyName) throws Exception {

		return this.serviceProviders.containsKey(getProviderURI(strategyName));
	}

	protected void handleDestroyServiceProvider(String strategyName) {

		getServiceProvider(strategyName).destroy();
		this.serviceProviders.remove(getProviderURI(strategyName));

		logger.debug("destroyed service provider: " + strategyName);
	}

	protected void handleDeployRule(String strategyName, String moduleName, String ruleName) throws Exception {
	
		deployRule(strategyName, moduleName, ruleName, null);
	}

	protected void handleDeployRule(String strategyName, String moduleName, String ruleName, Integer targetId) throws Exception {

		EPAdministrator administrator = getServiceProvider(strategyName).getEPAdministrator();

		// do nothing if the statement already exists
		EPStatement oldStatement = administrator.getStatement(ruleName);
		if (oldStatement != null && oldStatement.isStarted()) {
			return;
		}

		// read the statement from the module
		EPDeploymentAdmin deployAdmin = administrator.getDeploymentAdmin();
		Module module = deployAdmin.read("module-" + moduleName + ".epl");
		List<ModuleItem> items = module.getItems();

		// go through all statements in the module
		EPStatement newStatement = null;
		items: for (ModuleItem item : items) {
			String exp = item.getExpression();

			// get the ObjectModel for the statement
			EPStatementObjectModel model;
			EPPreparedStatementImpl prepared = null;
			if (exp.contains("?")) {
				prepared = ((EPPreparedStatementImpl) administrator.prepareEPL(exp));
				model = prepared.getModel();
			} else {
				model = administrator.compileEPL(exp);
			}

			// go through all annotations and check if the statement has the 'name' 'ruleName'
			List<AnnotationPart> annotations = model.getAnnotations();
			for (AnnotationPart annotation : annotations) {
				if (annotation.getName().equals("Name")) {
					for (AnnotationAttribute attribute : annotation.getAttributes()) {
						if (attribute.getValue().equals(ruleName)) {

							// for preparedStatements set the target
							if (prepared != null) {
								exp = exp.replace("?", String.valueOf(targetId));
							}

							// create the statement
							newStatement = administrator.createEPL(exp);

							// break iterating over the statements
							break items;
						}
					}
				}
			}
		}

		if (newStatement == null) {

			logger.warn("statement " + ruleName + " was not found");
		} else {
			addSubscriberAndListeners(newStatement);

			logger.debug("deployed rule " + newStatement.getName() + " on service provider: " + strategyName);
		}
	}

	protected void handleDeployModule(String strategyName, String moduleName) throws java.lang.Exception {
	
		EPAdministrator administrator = getServiceProvider(strategyName).getEPAdministrator();
		EPDeploymentAdmin deployAdmin = administrator.getDeploymentAdmin();
		Module module = deployAdmin.read("module-" + moduleName + ".epl");
		DeploymentResult deployResult = deployAdmin.deploy(module, new DeploymentOptions());
		List<EPStatement> statements = deployResult.getStatements();

		for (EPStatement statement : statements) {

			addSubscriberAndListeners(statement);
		}

		logger.debug("deployed module " + moduleName + " on service provider: " + strategyName);
	}

	protected void handleDeployAllModules(String strategyName) throws Exception {

		Strategy strategy = getLookupService().getStrategyByName(strategyName);
		String[] modules = strategy.getModules().split(",");
		for (String module : modules) {
			deployModule(strategyName, module);
		}
	}

	protected boolean handleIsDeployed(String strategyName, String ruleName) throws Exception {
	
		EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
	
		if (statement != null && statement.isStarted()) {
			return true;
		} else {
			return false;
		}
	}

	protected void handleUndeployRule(String strategyName, String ruleName) throws Exception {
	
		// destroy the statement
		EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
	
		if (statement != null && statement.isStarted()) {
			statement.destroy();
			logger.debug("undeployed rule " + ruleName);
		}
	}

	protected void handleUndeployRuleByTarget(String strategyName, String ruleName, int targetId) throws Exception {

		if (hasServiceProvider(strategyName)) {
			EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
			if (statement != null && statement.isStarted() && statement.getText().contains(String.valueOf(targetId))) {
				statement.destroy();
				logger.debug("undeployed rule " + ruleName);
			}
		}
	}

	protected void handleUndeployModule(String strategyName, String moduleName) throws java.lang.Exception {
	
		EPAdministrator administrator = getServiceProvider(strategyName).getEPAdministrator();
		EPDeploymentAdmin deployAdmin = administrator.getDeploymentAdmin();
		for (DeploymentInformation deploymentInformation : deployAdmin.getDeploymentInformation()) {
			if (deploymentInformation.getModule().getName().equals(moduleName)) {
				deployAdmin.undeploy(deploymentInformation.getDeploymentId());
			}
		}

		logger.debug("undeployed module " + moduleName);
	}

	protected void handleSendEvent(String strategyName, Object obj) {
	
		getServiceProvider(strategyName).getEPRuntime().sendEvent(obj);
	}

	protected void handleRouteEvent(String strategyName, Object obj) {
	
		// routing always goes to the local engine
		getServiceProvider(strategyName).getEPRuntime().route(obj);
	}

	protected Object handleGetLastEvent(String strategyName, String ruleName) {
	
		EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
		if (statement != null && statement.isStarted()) {
			SafeIterator<EventBean> it = statement.safeIterator();
			try {
				if (it.hasNext()) {
					return it.next().getUnderlying();
				}
			} finally {
				it.close();
			}
		}
		return null;
	}

	protected Object handleGetLastEventProperty(String strategyName, String ruleName, String property) throws Exception {

		EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
		if (statement != null && statement.isStarted()) {
			SafeIterator<EventBean> it = statement.safeIterator();
			try {
				return it.next().get(property);
			} finally {
				it.close();
			}
		}
		return null;
	}

	protected List<Object> handleGetAllEvents(String strategyName, String ruleName) {
	
		EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
		List<Object> list = new ArrayList<Object>();
		if (statement != null && statement.isStarted()) {
			SafeIterator<EventBean> it = statement.safeIterator();
			try {
				while (it.hasNext()) {
					EventBean bean = it.next();
					Object underlaying = bean.getUnderlying();
					list.add(underlaying);
				}
			} finally {
				it.close();
			}
		}
		return list;
	}

	protected List<Object> handleGetAllEventsProperty(String strategyName, String ruleName, String property) throws Exception {

		EPStatement statement = getServiceProvider(strategyName).getEPAdministrator().getStatement(ruleName);
		List<Object> list = new ArrayList<Object>();
		if (statement != null && statement.isStarted()) {
			SafeIterator<EventBean> it = statement.safeIterator();
			try {
				while (it.hasNext()) {
					EventBean bean = it.next();
					Object underlaying = bean.get(property);
					list.add(underlaying);
				}
			} finally {
				it.close();
			}
		}
		return list;
	}

	protected void handleSetInternalClock(String strategyName, boolean internal) {
	
		this.internalClock.put(strategyName, internal);

		if (internal) {
			sendEvent(strategyName, new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_INTERNAL));
			EPServiceProviderImpl provider = (EPServiceProviderImpl) getServiceProvider(strategyName);
			provider.getTimerService().enableStats();
		} else {
			sendEvent(strategyName, new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));
			EPServiceProviderImpl provider = (EPServiceProviderImpl) getServiceProvider(strategyName);
			provider.getTimerService().disableStats();
		}

		logger.debug("set internal clock to: " + internal + " for strategy: " + strategyName);
	}

	protected boolean handleIsInternalClock(String strategyName) {
	
		return this.internalClock.get(strategyName);
	}

	protected void handleSetCurrentTime(CurrentTimeEvent currentTimeEvent) {

		// sent currentTime to all local engines
		for (String providerURI : EPServiceProviderManager.getProviderURIs()) {
			sendEvent(providerURI, currentTimeEvent);
		}
	}

	protected long handleGetCurrentTime(String strategyName) {

		return getServiceProvider(strategyName).getEPRuntime().getCurrentTime();
	}

	protected void handleInitCoordination(String strategyName) throws Exception {

		this.coordinators.put(strategyName, new AdapterCoordinatorImpl(getServiceProvider(strategyName), true, true));
	}

	protected void handleCoordinate(String strategyName, CSVInputAdapterSpec csvInputAdapterSpec) throws Exception {

		InputAdapter inputAdapter;
		if (csvInputAdapterSpec instanceof CsvTickInputAdapterSpec) {
			inputAdapter = new CsvTickInputAdapter(getServiceProvider(strategyName), (CsvTickInputAdapterSpec) csvInputAdapterSpec);
		} else if (csvInputAdapterSpec instanceof CsvBarInputAdapterSpec) {
			inputAdapter = new CsvBarInputAdapter(getServiceProvider(strategyName), (CsvBarInputAdapterSpec) csvInputAdapterSpec);
		} else {
			inputAdapter = new CSVInputAdapter(getServiceProvider(strategyName), csvInputAdapterSpec);
		}
		this.coordinators.get(strategyName).coordinate(inputAdapter);
	}

	protected void handleStartCoordination(String strategyName) throws Exception {

		this.coordinators.get(strategyName).start();
	}

	protected void handleSetProperty(String strategyName, String key, String value) {
	
		key = key.replace(".", "_");
		EPRuntime runtime = getServiceProvider(strategyName).getEPRuntime();
		if (runtime.getVariableValueAll().containsKey(key)) {
			Class<?> clazz = runtime.getVariableValue(key).getClass();
			Object castedObj = JavaClassHelper.parse(clazz, value);
			runtime.setVariableValue(key, castedObj);
		}
	}

	private String getProviderURI(String strategyName) {

		return (strategyName == null || "".equals(strategyName)) ? StrategyImpl.BASE : strategyName.toUpperCase();
	}

	private EPServiceProvider getServiceProvider(String strategyName) {

		String providerURI = getProviderURI(strategyName);

		EPServiceProvider serviceProvider = this.serviceProviders.get(providerURI);
		if (serviceProvider == null) {
			throw new RuleServiceException("strategy " + providerURI + " is not initialized yet!");
		}

		return serviceProvider;
	}

	private boolean hasServiceProvider(String strategyName) {

		String providerURI = getProviderURI(strategyName);

		EPServiceProvider serviceProvider = this.serviceProviders.get(providerURI);

		return (serviceProvider != null);
	}

	/**
	 * initialize all the variables from the Configuration
	 */
	private void initVariables(String strategyName, Configuration configuration) {

		try {
			Map<String, ConfigurationVariable> variables = configuration.getVariables();
			for (Map.Entry<String, ConfigurationVariable> entry : variables.entrySet()) {
				String key = entry.getKey().replace("_", ".");
				String obj = ConfigurationUtil.getStrategyConfig(strategyName).getString(key);
				if (obj != null) {
					Class<?> clazz = Class.forName(entry.getValue().getType());
					Object castedObj = JavaClassHelper.parse(clazz, obj);
					entry.getValue().setInitializationValue(castedObj);
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuleServiceException(e);
		}
	}

	private void addSubscriberAndListeners(EPStatement statement) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Annotation[] annotations = statement.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Tag) {
				Tag tag = (Tag) annotation;
				if (tag.name().equals("subscriber")) {
					Class<?> cl = Class.forName(tag.value());
					Object obj = cl.newInstance();
					statement.setSubscriber(obj);
				} else if (tag.name().equals("listeners")) {
					String[] listeners = tag.value().split("\\s");
					for (String listener : listeners) {
						Class<?> cl = Class.forName(listener);
						Object obj = cl.newInstance();
						if (obj instanceof StatementAwareUpdateListener) {
							statement.addListener((StatementAwareUpdateListener) obj);
						} else {
							statement.addListener((UpdateListener) obj);
						}
					}
				}
			}
		}
	}
}