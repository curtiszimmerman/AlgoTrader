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
	
	private static Logger	                      logger	         = MyLogger
	                                                                         .getLogger(RuleServiceImpl.class
	                                                                                 .getName());
	
	private static final long	                  initTime	         = 631148400000l;	                            // 01.01.1990
	                                                                                                                
	private final Map<String, AdapterCoordinator>	coordinators	 = new HashMap<String, AdapterCoordinator>();
	private final Map<String, Boolean>	          internalClock	     = new HashMap<String, Boolean>();
	private final Map<String, EPServiceProvider>	serviceProviders	= new HashMap<String, EPServiceProvider>();
	
	@Override
	protected void handleInitServiceProvider(final String strategyName) {
		
		final String providerURI = getProviderURI(strategyName);
		
		final Configuration configuration = new Configuration();
		configuration.configure("esper-" + providerURI.toLowerCase() +
		        ".cfg.xml");
		
		initVariables(strategyName, configuration);
		
		final Strategy strategy = getLookupService().getStrategyByNameFetched(
		        strategyName);
		configuration.getVariables().get("engineStrategy")
		        .setInitializationValue(strategy);
		
		final EPServiceProvider serviceProvider = EPServiceProviderManager
		        .getProvider(providerURI, configuration);
		
		// must send time event before first schedule pattern
		serviceProvider.getEPRuntime().sendEvent(
		        new CurrentTimeEvent(RuleServiceImpl.initTime));
		internalClock.put(strategyName, false);
		
		serviceProviders.put(providerURI, serviceProvider);
		
		RuleServiceImpl.logger.debug("initialized service provider: " +
		        strategyName);
	}
	
	@Override
	protected boolean handleIsInitialized(final String strategyName)
	        throws Exception {
		
		return serviceProviders.containsKey(getProviderURI(strategyName));
	}
	
	@Override
	protected void handleDestroyServiceProvider(final String strategyName) {
		
		getServiceProvider(strategyName).destroy();
		serviceProviders.remove(getProviderURI(strategyName));
		
		RuleServiceImpl.logger.debug("destroyed service provider: " +
		        strategyName);
	}
	
	@Override
	protected void handleDeployRule(final String strategyName,
	        final String moduleName, final String ruleName) throws Exception {
		
		deployRule(strategyName, moduleName, ruleName, null);
	}
	
	@Override
	protected void handleDeployRule(final String strategyName,
	        final String moduleName, final String ruleName,
	        final Integer targetId) throws Exception {
		
		final EPAdministrator administrator = getServiceProvider(strategyName)
		        .getEPAdministrator();
		
		// do nothing if the statement already exists
		final EPStatement oldStatement = administrator.getStatement(ruleName);
		if (oldStatement != null && oldStatement.isStarted()) { return; }
		
		// read the statement from the module
		final EPDeploymentAdmin deployAdmin = administrator
		        .getDeploymentAdmin();
		final Module module = deployAdmin.read("module-" + moduleName + ".epl");
		final List<ModuleItem> items = module.getItems();
		
		// go through all statements in the module
		EPStatement newStatement = null;
		items: for (final ModuleItem item : items) {
			String exp = item.getExpression();
			
			// get the ObjectModel for the statement
			EPStatementObjectModel model;
			EPPreparedStatementImpl prepared = null;
			if (exp.contains("?")) {
				prepared = (EPPreparedStatementImpl) administrator
				        .prepareEPL(exp);
				model = prepared.getModel();
			} else {
				model = administrator.compileEPL(exp);
			}
			
			// go through all annotations and check if the statement has the
			// 'name' 'ruleName'
			final List<AnnotationPart> annotations = model.getAnnotations();
			for (final AnnotationPart annotation : annotations) {
				if (annotation.getName().equals("Name")) {
					for (final AnnotationAttribute attribute : annotation
					        .getAttributes()) {
						if (attribute.getValue().equals(ruleName)) {
							
							// for preparedStatements set the target
							if (prepared != null) {
								exp = exp
								        .replace("?", String.valueOf(targetId));
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
			
			RuleServiceImpl.logger.warn("statement " + ruleName +
			        " was not found");
		} else {
			addSubscriberAndListeners(newStatement);
			
			RuleServiceImpl.logger.debug("deployed rule " +
			        newStatement.getName() + " on service provider: " +
			        strategyName);
		}
	}
	
	@Override
	protected void handleDeployModule(final String strategyName,
	        final String moduleName) throws java.lang.Exception {
		
		final EPAdministrator administrator = getServiceProvider(strategyName)
		        .getEPAdministrator();
		final EPDeploymentAdmin deployAdmin = administrator
		        .getDeploymentAdmin();
		final Module module = deployAdmin.read("module-" + moduleName + ".epl");
		final DeploymentResult deployResult = deployAdmin.deploy(module,
		        new DeploymentOptions());
		final List<EPStatement> statements = deployResult.getStatements();
		
		for (final EPStatement statement : statements) {
			
			addSubscriberAndListeners(statement);
		}
		
		RuleServiceImpl.logger.debug("deployed module " + moduleName +
		        " on service provider: " + strategyName);
	}
	
	@Override
	protected void handleDeployAllModules(final String strategyName)
	        throws Exception {
		
		final Strategy strategy = getLookupService().getStrategyByName(
		        strategyName);
		final String[] modules = strategy.getModules().split(",");
		for (final String module : modules) {
			deployModule(strategyName, module);
		}
	}
	
	@Override
	protected boolean handleIsDeployed(final String strategyName,
	        final String ruleName) throws Exception {
		
		final EPStatement statement = getServiceProvider(strategyName)
		        .getEPAdministrator().getStatement(ruleName);
		
		if (statement != null && statement.isStarted()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void handleUndeployRule(final String strategyName,
	        final String ruleName) throws Exception {
		
		// destroy the statement
		final EPStatement statement = getServiceProvider(strategyName)
		        .getEPAdministrator().getStatement(ruleName);
		
		if (statement != null && statement.isStarted()) {
			statement.destroy();
			RuleServiceImpl.logger.debug("undeployed rule " + ruleName);
		}
	}
	
	@Override
	protected void handleUndeployRuleByTarget(final String strategyName,
	        final String ruleName, final int targetId) throws Exception {
		
		if (hasServiceProvider(strategyName)) {
			final EPStatement statement = getServiceProvider(strategyName)
			        .getEPAdministrator().getStatement(ruleName);
			if (statement != null && statement.isStarted() &&
			        statement.getText().contains(String.valueOf(targetId))) {
				statement.destroy();
				RuleServiceImpl.logger.debug("undeployed rule " + ruleName);
			}
		}
	}
	
	@Override
	protected void handleUndeployModule(final String strategyName,
	        final String moduleName) throws java.lang.Exception {
		
		final EPAdministrator administrator = getServiceProvider(strategyName)
		        .getEPAdministrator();
		final EPDeploymentAdmin deployAdmin = administrator
		        .getDeploymentAdmin();
		for (final DeploymentInformation deploymentInformation : deployAdmin
		        .getDeploymentInformation()) {
			if (deploymentInformation.getModule().getName().equals(moduleName)) {
				deployAdmin.undeploy(deploymentInformation.getDeploymentId());
			}
		}
		
		RuleServiceImpl.logger.debug("undeployed module " + moduleName);
	}
	
	@Override
	protected void handleSendEvent(final String strategyName, final Object obj) {
		
		getServiceProvider(strategyName).getEPRuntime().sendEvent(obj);
	}
	
	@Override
	protected void
	        handleRouteEvent(final String strategyName, final Object obj) {
		
		// routing always goes to the local engine
		getServiceProvider(strategyName).getEPRuntime().route(obj);
	}
	
	@Override
	protected Object handleGetLastEvent(final String strategyName,
	        final String ruleName) {
		
		final EPStatement statement = getServiceProvider(strategyName)
		        .getEPAdministrator().getStatement(ruleName);
		if (statement != null && statement.isStarted()) {
			final SafeIterator<EventBean> it = statement.safeIterator();
			try {
				if (it.hasNext()) { return it.next().getUnderlying(); }
			} finally {
				it.close();
			}
		}
		return null;
	}
	
	@Override
	protected Object handleGetLastEventProperty(final String strategyName,
	        final String ruleName, final String property) throws Exception {
		
		final EPStatement statement = getServiceProvider(strategyName)
		        .getEPAdministrator().getStatement(ruleName);
		if (statement != null && statement.isStarted()) {
			final SafeIterator<EventBean> it = statement.safeIterator();
			try {
				return it.next().get(property);
			} finally {
				it.close();
			}
		}
		return null;
	}
	
	@Override
	protected List<Object> handleGetAllEvents(final String strategyName,
	        final String ruleName) {
		
		final EPStatement statement = getServiceProvider(strategyName)
		        .getEPAdministrator().getStatement(ruleName);
		final List<Object> list = new ArrayList<Object>();
		if (statement != null && statement.isStarted()) {
			final SafeIterator<EventBean> it = statement.safeIterator();
			try {
				while (it.hasNext()) {
					final EventBean bean = it.next();
					final Object underlaying = bean.getUnderlying();
					list.add(underlaying);
				}
			} finally {
				it.close();
			}
		}
		return list;
	}
	
	@Override
	protected List<Object> handleGetAllEventsProperty(
	        final String strategyName, final String ruleName,
	        final String property) throws Exception {
		
		final EPStatement statement = getServiceProvider(strategyName)
		        .getEPAdministrator().getStatement(ruleName);
		final List<Object> list = new ArrayList<Object>();
		if (statement != null && statement.isStarted()) {
			final SafeIterator<EventBean> it = statement.safeIterator();
			try {
				while (it.hasNext()) {
					final EventBean bean = it.next();
					final Object underlaying = bean.get(property);
					list.add(underlaying);
				}
			} finally {
				it.close();
			}
		}
		return list;
	}
	
	@Override
	protected void handleSetInternalClock(final String strategyName,
	        final boolean internal) {
		
		internalClock.put(strategyName, internal);
		
		if (internal) {
			sendEvent(strategyName, new TimerControlEvent(
			        TimerControlEvent.ClockType.CLOCK_INTERNAL));
			final EPServiceProviderImpl provider = (EPServiceProviderImpl) getServiceProvider(strategyName);
			provider.getTimerService().enableStats();
		} else {
			sendEvent(strategyName, new TimerControlEvent(
			        TimerControlEvent.ClockType.CLOCK_EXTERNAL));
			final EPServiceProviderImpl provider = (EPServiceProviderImpl) getServiceProvider(strategyName);
			provider.getTimerService().disableStats();
		}
		
		RuleServiceImpl.logger.debug("set internal clock to: " + internal +
		        " for strategy: " + strategyName);
	}
	
	@Override
	protected boolean handleIsInternalClock(final String strategyName) {
		
		return internalClock.get(strategyName);
	}
	
	@Override
	protected void
	        handleSetCurrentTime(final CurrentTimeEvent currentTimeEvent) {
		
		// sent currentTime to all local engines
		for (final String providerURI : EPServiceProviderManager
		        .getProviderURIs()) {
			sendEvent(providerURI, currentTimeEvent);
		}
	}
	
	@Override
	protected long handleGetCurrentTime(final String strategyName) {
		
		return getServiceProvider(strategyName).getEPRuntime().getCurrentTime();
	}
	
	@Override
	protected void handleInitCoordination(final String strategyName)
	        throws Exception {
		
		coordinators.put(strategyName, new AdapterCoordinatorImpl(
		        getServiceProvider(strategyName), true, true));
	}
	
	@Override
	protected void handleCoordinate(final String strategyName,
	        final CSVInputAdapterSpec csvInputAdapterSpec) throws Exception {
		
		InputAdapter inputAdapter;
		if (csvInputAdapterSpec instanceof CsvTickInputAdapterSpec) {
			inputAdapter = new CsvTickInputAdapter(
			        getServiceProvider(strategyName),
			        (CsvTickInputAdapterSpec) csvInputAdapterSpec);
		} else {
			inputAdapter = new CSVInputAdapter(
			        getServiceProvider(strategyName), csvInputAdapterSpec);
		}
		coordinators.get(strategyName).coordinate(inputAdapter);
	}
	
	@Override
	protected void handleStartCoordination(final String strategyName)
	        throws Exception {
		
		coordinators.get(strategyName).start();
	}
	
	@Override
	protected void handleSetProperty(final String strategyName, String key,
	        final String value) {
		
		key = key.replace(".", "_");
		final EPRuntime runtime = getServiceProvider(strategyName)
		        .getEPRuntime();
		if (runtime.getVariableValueAll().containsKey(key)) {
			final Class<?> clazz = runtime.getVariableValue(key).getClass();
			final Object castedObj = JavaClassHelper.parse(clazz, value);
			runtime.setVariableValue(key, castedObj);
		}
	}
	
	private String getProviderURI(final String strategyName) {
		
		return strategyName == null || "".equals(strategyName) ? StrategyImpl.BASE
		        : strategyName.toUpperCase();
	}
	
	private EPServiceProvider getServiceProvider(final String strategyName) {
		
		final String providerURI = getProviderURI(strategyName);
		
		final EPServiceProvider serviceProvider = serviceProviders
		        .get(providerURI);
		if (serviceProvider == null) { throw new RuleServiceException(
		        "strategy " + providerURI + " is not initialized yet!"); }
		
		return serviceProvider;
	}
	
	private boolean hasServiceProvider(final String strategyName) {
		
		final String providerURI = getProviderURI(strategyName);
		
		final EPServiceProvider serviceProvider = serviceProviders
		        .get(providerURI);
		
		return serviceProvider != null;
	}
	
	/**
	 * initialize all the variables from the Configuration
	 */
	private void initVariables(final String strategyName,
	        final Configuration configuration) {
		
		try {
			final Map<String, ConfigurationVariable> variables = configuration
			        .getVariables();
			for (final Map.Entry<String, ConfigurationVariable> entry : variables
			        .entrySet()) {
				final String key = entry.getKey().replace("_", ".");
				final String obj = ConfigurationUtil.getStrategyConfig(
				        strategyName).getString(key);
				if (obj != null) {
					final Class<?> clazz = Class.forName(entry.getValue()
					        .getType());
					final Object castedObj = JavaClassHelper.parse(clazz, obj);
					entry.getValue().setInitializationValue(castedObj);
				}
			}
		} catch (final ClassNotFoundException e) {
			throw new RuleServiceException(e);
		}
	}
	
	private void addSubscriberAndListeners(final EPStatement statement)
	        throws ClassNotFoundException, InstantiationException,
	        IllegalAccessException {
		
		final Annotation[] annotations = statement.getAnnotations();
		for (final Annotation annotation : annotations) {
			if (annotation instanceof Tag) {
				final Tag tag = (Tag) annotation;
				if (tag.name().equals("subscriber")) {
					final Class<?> cl = Class.forName(tag.value());
					final Object obj = cl.newInstance();
					statement.setSubscriber(obj);
				} else if (tag.name().equals("listeners")) {
					final String[] listeners = tag.value().split("\\s");
					for (final String listener : listeners) {
						final Class<?> cl = Class.forName(listener);
						final Object obj = cl.newInstance();
						if (obj instanceof StatementAwareUpdateListener) {
							statement
							        .addListener((StatementAwareUpdateListener) obj);
						} else {
							statement.addListener((UpdateListener) obj);
						}
					}
				}
			}
		}
	}
}