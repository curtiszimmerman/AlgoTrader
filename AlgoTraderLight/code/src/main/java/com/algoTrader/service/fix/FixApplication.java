package com.algoTrader.service.fix;

import org.apache.log4j.Logger;

import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

import com.algoTrader.util.MyLogger;


public class FixApplication extends MessageCracker implements quickfix.Application {

	private static Logger logger = MyLogger.getLogger(FixApplication.class.getName());

	private SessionID sessionId;

	public FixApplication(Object messageHandler) {
		super(messageHandler);
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// do nothing
	}

	@Override
	public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		crack(message, sessionID);
	}

	@Override
	public void onCreate(SessionID sessionID) {
		// do nothing
	}

	@Override
	public void onLogon(SessionID sessionId) {
		this.sessionId = sessionId;
		logger.info("logon: " + sessionId);
	}

	@Override
	public void onLogout(SessionID sessionId) {
		this.sessionId = null;
		logger.info("logout: " + sessionId);
	}

	@Override
	public void toAdmin(Message message, SessionID sessionID) {
		// do nothing
	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// do nothing
	}

	public SessionID getSessionID(String qualifier) {
		return this.sessionId;
	}

	@Override
	protected void onMessage(Message message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		logger.debug("message: " + message);
	}
}
