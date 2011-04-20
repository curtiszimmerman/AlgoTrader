package com.algoTrader.util.io;

import com.algoTrader.vo.RawTickVO;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esperio.SendableBeanEvent;
import com.espertech.esperio.SendableEvent;
import com.espertech.esperio.csv.CSVInputAdapter;

public class CsvTickInputAdapter extends CSVInputAdapter {
	
	private final CsvTickInputAdapterSpec	spec;
	
	public CsvTickInputAdapter(final EPServiceProvider epService,
	        final CsvTickInputAdapterSpec spec) {
		
		super(epService, spec);
		this.spec = spec;
	}
	
	@Override
	public SendableEvent read() throws EPException {
		final SendableBeanEvent event = (SendableBeanEvent) super.read();
		
		if (event != null && event.getBeanToSend() instanceof RawTickVO) {
			
			final RawTickVO tick = (RawTickVO) event.getBeanToSend();
			final String isin = spec.getFile().getName().split("\\.")[0];
			tick.setIsin(isin);
		}
		return event;
	}
}
