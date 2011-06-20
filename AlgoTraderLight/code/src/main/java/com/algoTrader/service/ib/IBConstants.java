package com.algoTrader.service.ib;

public class IBConstants {

	/** Supported Order Types */
	public static final String MARKET_TO_LIMIT = "MTL";
	public static final String MARKET_WITH_PROTECTION = "MKT PRT";
	public static final String REQUEST_FOR_QUOTE = "QUOTE";
	public static final String STOP = "STP";
	public static final String STOP_LIMIT = "STP LMT";
	public static final String TRAILING_LIMIT_IF_TOUCHED = "TRAIL LIT";
	public static final String TRAILING_MARKET_IF_TOUCHED = "TRAIL MIT";
	public static final String TRAILING_STOP = "TRAIL";
	public static final String TRAILING_STOP_LIMIT = "TRAIL LIMIT";

	public static final String MARKET = "MKT";
	public static final String MARKET_IF_TOUCHED = "MIT";
	public static final String MARKET_ON_CLOSE = "MOC";
	public static final String MARKET_ON_OPEN = "MOO";

	public static final String PEGGED_TO_MARKET = "PEG MKT";
	public static final String RELATIVE = "REL";
	public static final String BOX_TOP = "BOX TOP";
	public static final String LIMIT = "LMT";
	public static final String LIMIT_ON_CLOSE = "LOC";
	public static final String LIMIT_ON_OPEN = "LOO";
	public static final String LIMIT_IF_TOUCHED = "LIT";
	public static final String PEGGED_TO_MIDPOINT = "PEG MID";
	public static final String VWAP = "VWAP";

	public static final String GOOD_AFTER_TIME_DATE = "GAT";
	public static final String GOOD_TILL_DATE_TIME = "GTD";
	public static final String GOOD_TILL_CANCELED = "GTC";

	public static final String IMMEDIATE_OR_CANCEL = "IOC";
	public static final String ONE_CANCELS_ALL = "OCA";
	public static final String VOLATILITY = "VOL";
	
	//exchanges
	public static final String NASDAQ_EXCHANGE = "STK.NASDAQ";
	public static final String NYSE_EXCHANGE = "STK.NYSE";
	public static final String AMEX_EXCHANGE = "STK.AMEX";
	public static final String PINK_OTCBB_EXCHANGE = "STK.US.MINOR";
	
	//max request constants
	public static final int MAX_NUM_CONCURRENT_CALLS_TO_REQMKTDATA =  100; 
	public static final int MAX_NUM_CONCURRENT_CALLS_REQMKTDEPTH = 3; 
	
	/**
	 * make sure don't exceed 1 requests per 10 seconds 
	 */
	public static final int MMAX_NUM_CONCURRENT_CALLS_REQREALTIMEBARS = 120; 
	
	/**
	 * 60 requests per 10 minutes. Click the link below for entire historical data limitation 
	 * http://individuals.interactivebrokers.com/php/apiUsersGuide/apiguide/api/historical_data_limitations.htm 
	 */
	public static final int MAX_HISTORICAL_DATA_REQUESTS = 60; 
	
	//TODO add the API Message codes (100=Max rate of messages per second has been exceeded., etc)
	
	//TODO add the tick types (bid size =0, bid price =1 , etc)
	
	//TODO add the generic tick types
	
	//TODO add the constants in the Appendix A - Extended Order Attributes
	
	//TODO add the IBAlgo Parameters
	
	//TODO Extended Order Attributes
	
	//TODO Available Market Scanners
	
	//TODO Supported Time Zones (link is not working on IB site, will have to talk to tech support about this.)
}
