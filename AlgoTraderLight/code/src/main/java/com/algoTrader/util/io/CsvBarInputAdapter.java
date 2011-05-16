package com.algoTrader.util.io;

import com.algoTrader.vo.BarVO;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esperio.SendableBeanEvent;
import com.espertech.esperio.SendableEvent;
import com.espertech.esperio.csv.CSVInputAdapter;

public class CsvBarInputAdapter extends CSVInputAdapter {
	
	private CsvBarInputAdapterSpec spec;

	public CsvBarInputAdapter(EPServiceProvider epService, CsvBarInputAdapterSpec spec) {

		super(epService, spec);
		this.spec = spec;
	}

	public SendableEvent read() throws EPException {

		SendableBeanEvent event = (SendableBeanEvent)super.read();
		
		if (event != null && event.getBeanToSend() instanceof BarVO) {

			BarVO bar = (BarVO) event.getBeanToSend();
			String isin = this.spec.getFile().getName().split("\\.")[0];
			bar.setIsin(isin);
		}		
		return event;
	}
}
