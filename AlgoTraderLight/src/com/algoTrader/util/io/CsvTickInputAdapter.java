package com.algoTrader.util.io;

import com.algoTrader.vo.RawTickVO;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esperio.SendableBeanEvent;
import com.espertech.esperio.SendableEvent;
import com.espertech.esperio.csv.CSVInputAdapter;

public class CsvTickInputAdapter extends CSVInputAdapter {

	private CsvTickInputAdapterSpec spec;

	public CsvTickInputAdapter(EPServiceProvider epService, CsvTickInputAdapterSpec spec) {

		super(epService, spec);
		this.spec = spec;
	}

	public SendableEvent read() throws EPException {
		SendableBeanEvent event = (SendableBeanEvent)super.read();

		if (event != null && event.getBeanToSend() instanceof RawTickVO) {

			RawTickVO tick = (RawTickVO) event.getBeanToSend();
			String isin = this.spec.getFile().getName().split("\\.")[0];
			tick.setIsin(isin);
		}
		return event;
	}
}
