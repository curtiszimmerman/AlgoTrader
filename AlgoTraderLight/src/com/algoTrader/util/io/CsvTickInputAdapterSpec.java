package com.algoTrader.util.io;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.algoTrader.util.CustomDate;
import com.espertech.esperio.AdapterInputSource;
import com.espertech.esperio.csv.CSVInputAdapterSpec;

public class CsvTickInputAdapterSpec extends CSVInputAdapterSpec {

	private File file;

	public CsvTickInputAdapterSpec(File file) {

		super(new AdapterInputSource(file), "RawTick");

		this.file = file;

		String[] tickPropertyOrder = new String[] { "dateTime", "last", "lastDateTime", "volBid", "volAsk", "bid", "ask", "vol", "openIntrest", "settlement" };

		Map<String, Object> tickPropertyTypes = new HashMap<String, Object>();

		tickPropertyTypes.put("dateTime", CustomDate.class);
		tickPropertyTypes.put("last", BigDecimal.class);
		tickPropertyTypes.put("lastDateTime", CustomDate.class);
		tickPropertyTypes.put("volBid", int.class);
		tickPropertyTypes.put("volAsk", int.class);
		tickPropertyTypes.put("bid", BigDecimal.class);
		tickPropertyTypes.put("ask", BigDecimal.class);
		tickPropertyTypes.put("vol", int.class);
		tickPropertyTypes.put("openIntrest", int.class);
		tickPropertyTypes.put("settlement", BigDecimal.class);

		setPropertyOrder(tickPropertyOrder);
		setPropertyTypes(tickPropertyTypes);

		setTimestampColumn("dateTime");

		setUsingExternalTimer(true);
	}

	public File getFile() {

		return this.file;
	}
}