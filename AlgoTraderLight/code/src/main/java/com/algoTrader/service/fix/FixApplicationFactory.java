package com.algoTrader.service.fix;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.SessionID;
import quickfix.SessionSettings;

public class FixApplicationFactory {

	private static final String SETTING_HANDLER_CLASS_NAME = "HandlerClassName";

	private final SessionSettings settings;

	public FixApplicationFactory(SessionSettings settings) {
		this.settings = settings;
	}

	public Application create(SessionID sessionID) throws ConfigError {

		String className = null;
		try {
			if (this.settings.isSetting(sessionID, SETTING_HANDLER_CLASS_NAME)) {
				className = this.settings.getString(sessionID, SETTING_HANDLER_CLASS_NAME);
			} else {
				throw new IllegalStateException(SETTING_HANDLER_CLASS_NAME + " not defined");
			}
			Object messageHandler = Class.forName(className).newInstance();
			return new FixApplication(messageHandler);
		} catch (ClassNotFoundException e) {
			throw new ConfigError(SETTING_HANDLER_CLASS_NAME + "=" + className + " class not found");
		} catch (Exception ex) {
			throw new ConfigError(SETTING_HANDLER_CLASS_NAME + " failed", ex);
		}
	}
}
