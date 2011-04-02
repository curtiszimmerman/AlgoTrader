package com.ceptrader.esper.epl;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.tradeapp.util.BasicUtils;
import com.ceptrader.tradeapp.util.Logger;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;

public class EPLDebug extends EsperEventListner {
	public EPLDebug(final String[] stm) {
		super(stm);
	}
	
	public EPLDebug(final String stm) {
		super(stm);
	}
	
	public EPLDebug(final String[] stm, final boolean unMatched) {
		super(stm, unMatched);
	}
	
	public EPLDebug(final String stm, final boolean unMatched) {
		super(stm, unMatched);
	}
	
	public EPLDebug() {
		super(CEPMan.getCEPMan().getEpService().getEPAdministrator()
		        .getStatementNames(), true);
	}
	
	@Override
	public void update(final EventBean[] e1, final EventBean[] e2) {
		Logger.log("****************** Update Listner *************");
		Logger.log(BasicUtils.toString(e1));
		Logger.log(BasicUtils.toString(e2));
	}
	
	@Override
	public void update(final EventBean[] e1, final EventBean[] e2,
	        final EPStatement stm,
	        final EPServiceProvider provider) {
		Logger.log("****************** Statement Aware Update Listner *************");
		Logger.log(BasicUtils.toString(e1));
		Logger.log(BasicUtils.toString(e2));
		Logger.log(BasicUtils.toString(stm));
		Logger.log(BasicUtils.toString(provider));
	}
	
	public void update(final EventBean e) {
		Logger.log("****************** Unmatched Event Listner *************");
		Logger.log(BasicUtils.toString(e));
	}
	
}
