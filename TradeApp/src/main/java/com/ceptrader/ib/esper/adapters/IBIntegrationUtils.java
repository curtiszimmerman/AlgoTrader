package com.ceptrader.ib.esper.adapters;

import java.net.URL;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.util.Logger;

public class IBIntegrationUtils {
	public static void deployIBEventTranslations() {
		IBIntegrationUtils
		        .deployIBEventTranslations("IBInit.epl");
	}
	
	public static void deployIBEventTranslations(final String res) {
		final URL IBInit = IBIntegrationUtils.class.getResource(res);
		
		if (!CEPMan.getCEPMan().isStarted()) {
			CEPMan.getCEPMan().start();
		}
		
		Logger.log(CEPMan.getCEPMan().deploy(IBInit));
	}
	
	public static void deployIBTradeTranslations() {
		IBIntegrationUtils
		        .deployIBEventTranslations("IBTradeRouting.epl");
	}
	
	public static void deployIBTradeTranslations(final String res) {
		final URL IBInit = IBIntegrationUtils.class
		        .getResource(res);
		
		if (!CEPMan.getCEPMan().isStarted()) {
			CEPMan.getCEPMan().start();
		}
		
		Logger.log(CEPMan.getCEPMan().deploy(IBInit));
	}
}
