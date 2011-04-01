package com.ceptrader.tradeapp.cep.esper.integration.ib;

import java.net.URL;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.esper.epl.scripts.EsperEPLUtils;
import com.ceptrader.tradeapp.util.Logger;


public class IBIntegrationUtils {
	public static void deployIBEventTranslations() {
		IBIntegrationUtils
		        .deployIBEventTranslations("/Esper/EPL/Scripts/IBInit.epl");
	}
	
	public static void deployIBEventTranslations(final String res) {
		final URL IBInit = EsperEPLUtils.class
		        .getResource(res);
		
		if (!CEPMan.getCEPMan().isStarted()) {
			CEPMan.getCEPMan().start();
		}
		
		Logger.log(CEPMan.getCEPMan().deploy(IBInit));
	}
	
	public static void deployIBTradeTranslations() {
		IBIntegrationUtils
		        .deployIBEventTranslations("/Esper/EPL/Scripts/IBTradeRouting.epl");
	}
	
	public static void deployIBTradeTranslations(final String res) {
		final URL IBInit = EsperEPLUtils.class
		        .getResource(res);
		
		if (!CEPMan.getCEPMan().isStarted()) {
			CEPMan.getCEPMan().start();
		}
		
		Logger.log(CEPMan.getCEPMan().deploy(IBInit));
	}
}
