package com.algoTrader.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import com.algoTrader.entity.Strategy;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class StrategyServiceImpl extends StrategyServiceBase implements DisposableBean {

	private static int basePort = ConfigurationUtil.getBaseConfig().getInt("basePort");
	private static Logger logger = MyLogger.getLogger(StrategyServiceImpl.class.getName());

	private Map<String, Socket> socketMap = new HashMap<String, Socket>();
	private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();

	protected void handleRegisterStrategy(String strategyName) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);

		Socket socket = new Socket("localhost", basePort + strategy.getId());
		this.socketMap.put(strategyName, socket);

		ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
		this.streamMap.put(strategyName, stream);

		logger.debug("registered strategy: " + strategyName);
	}

	protected void handleUnregisterStrategy(String strategyName) throws Exception {

		try {
			this.streamMap.get(strategyName).close();
		} catch (IOException e) {
			logger.warn("stream not available anymore: " + strategyName);
		} finally {
			this.streamMap.remove(strategyName);
		}

		try {
			this.socketMap.get(strategyName).close();
		} catch (IOException e) {
			logger.warn("socket not available anymore: " + strategyName);
		} finally {
			this.socketMap.remove(strategyName);
		}

		logger.debug("unregistered strategy: " + strategyName);
	}

	protected boolean handleIsStrategyRegistered(String strategyName) throws Exception {

		return this.socketMap.containsKey(strategyName);
	}

	protected void handleSendEvent(String strategyName, Object obj) {

		if (this.socketMap.containsKey(strategyName)) {
			ObjectOutputStream stream = this.streamMap.get(strategyName);

			try {
				stream.writeObject(obj);
				stream.flush();

			} catch (IOException e) {

				logger.warn("remote client not available anymore: " + strategyName);

				// probably the client has been shut down, so unregister
				unregisterStrategy(strategyName);
			}
		}
	}

	public void destroy() throws Exception {

		for (Map.Entry<String, Socket> entry : this.socketMap.entrySet()) {
			unregisterStrategy(entry.getKey());
		}
	}
}