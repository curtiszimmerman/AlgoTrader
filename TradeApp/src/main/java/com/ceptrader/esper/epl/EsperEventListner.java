package com.ceptrader.esper.epl;

import com.ceptrader.esper.CEPMan;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.client.UnmatchedListener;
import com.espertech.esper.client.UpdateListener;

public abstract class EsperEventListner implements UpdateListener,
        StatementAwareUpdateListener, UnmatchedListener {
	
	public EsperEventListner(final String[] stms) {
		for (final String s : stms) {
			Init(s, false);
		}
	}
	
	public EsperEventListner(final String stm) {
		Init(stm, false);
	}
	
	public EsperEventListner(final String[] stms, final boolean listenunMatched) {
		for (final String s : stms) {
			Init(s, listenunMatched);
		}
	}
	
	public EsperEventListner(final String stm, final boolean listenunMatched) {
		Init(stm, listenunMatched);
	}
	
	private void Init(final String stm, final boolean linstenUnMatched) {
		final EPServiceProvider epService = CEPMan.getCEPMan().getEpService();
		final EPRuntime epRT = epService.getEPRuntime();
		final EPStatement statement = epService.getEPAdministrator()
		        .getStatement(stm);
		statement.addListenerWithReplay(this);
		statement.addListener((StatementAwareUpdateListener) this);
		
		if (linstenUnMatched) {
			epRT.setUnmatchedListener(this);
		}
	}
	
	@Override
	abstract public void update(final EventBean[] e1, final EventBean[] e2);
	
	@Override
	abstract public void update(final EventBean[] e1, final EventBean[] e2,
	        final EPStatement stm,
	        final EPServiceProvider provider);
}
