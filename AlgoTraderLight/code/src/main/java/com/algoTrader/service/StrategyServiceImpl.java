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
	private static int maxObjectInStream = ConfigurationUtil.getBaseConfig().getInt("maxObjectInStream");

	private Map<String, Socket> socketMap = new HashMap<String, Socket>();
	private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();
	private Map<String, Integer> objectsMap = new HashMap<String, Integer>();

	@Override
	protected void handleRegisterStrategy(String strategyName) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);

		Socket socket = new Socket("localhost", basePort + strategy.getId());
		this.socketMap.put(strategyName, socket);

		ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
		this.streamMap.put(strategyName, stream);

		this.objectsMap.put(strategyName, 0);

		logger.debug("registered strategy: " + strategyName);
	}

	@Override
	protected void handleUnregisterStrategy(String strategyName) throws Exception {

		try {
			ObjectOutputStream stream = this.streamMap.get(strategyName);
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			logger.warn("stream not available anymore: " + strategyName);
		} finally {
			this.streamMap.remove(strategyName);
		}

		try {
			Socket socket = this.socketMap.get(strategyName);
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			logger.warn("socket not available anymore: " + strategyName);
		} finally {
			this.socketMap.remove(strategyName);
		}

		logger.debug("unregistered strategy: " + strategyName);
	}

	@Override
	protected boolean handleIsStrategyRegistered(String strategyName) throws Exception {

		return this.socketMap.containsKey(strategyName);
	}

	@Override
	protected void handleSendEvent(String strategyName, Object obj) {

		if (this.socketMap.containsKey(strategyName)) {
			ObjectOutputStream stream = this.streamMap.get(strategyName);

			try {
				stream.writeObject(obj);
				stream.flush();

				// reset the stream after n objects have been written to the Stream
				int objectsWritten = this.objectsMap.get(strategyName);
				if (objectsWritten > maxObjectInStream) {

					stream.reset();

					this.objectsMap.put(strategyName, 0);
					logger.debug("stream " + strategyName + " has been reset");

				} else {

					this.objectsMap.put(strategyName, ++objectsWritten);
				}

			} catch (IOException e) {

				logger.warn("remote client not available anymore: " + strategyName);

				// probably the client has been shut down, so unregister
				unregisterStrategy(strategyName);
			}
		}
	}

	@Override
	public void destroy() throws Exception {

		for (Map.Entry<String, Socket> entry : this.socketMap.entrySet()) {
			unregisterStrategy(entry.getKey());
		}
	}
}
