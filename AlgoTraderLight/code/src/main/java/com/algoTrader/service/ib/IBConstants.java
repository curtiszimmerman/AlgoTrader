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
	public static final int MAX_NUM_CONCURRENT_CALLS_TO_REQMKTDATA = 100;
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
	
	//tick types (bid size =0, bid price =1 , etc)
	//for tickprice
	public static final int BID = 1;
	public static final int ASK = 2;
	public static final int LAST = 4;
	public static final int HIGH = 6;
	public static final int LOW = 7;
	public static final int CLOSE = 9;
	public static final int NOT_ELIGIBLE_FOR_AUTOMATIC_EXECUTION = 0;
    public static final int ELIGIBLE_FOR_AUTOMATIC_EXECUTION = 1;
	
	//TODO add the generic tick types
	//for updateMktDepth and updateMktDepthL2
	public static final int MARKET_DEPTH_OPERATION_INSERT = 0;
	public static final int MARKET_DEPTH_OPERATION_UPDATE = 1;
	public static final int MARKET_DEPTH_OPERATION_DELETE = 2;
	public static final int MARKET_DEPTH_SIDE_ASK = 0;
	public static final int MARKET_DEPTH_SIDE_BID = 1;
	
	//for updateNewsBulletin
	public static final int REGULAR_NEWS_BULLETIN_MSG_TYPE = 0;
	public static final int EXCHANGE_NO_LONGER_AVAILABLE_FOR_TRADING = 1;
	public static final int  EXCHANGE_IS_AVAILABLE_FOR_TRADING = 2;
	
    //tickOptionComputation
    public static final int BID_OPTION_COMPUTATION = 10;
    public static final int ASK_OPTION_COMPUTATION = 11;
    public static final int LAST_OPTION_COMPUTATION = 12;
    
	//for tickSize
	public static final int BID_SIZE = 0;
	public static final int ASK_SIZE = 3;
	public static final int LAST_SIZE = 5;
	public static final int VOLUME = 8;

	//for orderStatus
	/**
	 * indicates that you have transmitted the order, but have not yet received confirmation that it has been accepted by the
	 * order destination. NOTE: This order status is not sent by TWS and should be explicitly set by the API developer when 
	 * an order is submitted.
	 */
	public static final String PENDING_SUBMIT = "PendingSubmit";
	/** 
	 * indicates that you have sent a request to cancel the order but have not yet received cancel confirmation from the order destination.
	 * At this point, your order is not confirmed canceled. You may still receive an execution while your cancellation request is pending. 
	 * NOTE: This order status is not sent by TWS and should be explicitly set by the API developer when an order is canceled.
	 */
	public static final String PENDING_CANCEL = "PendingCancel"; 
	/** 
	 * indicates that a simulated order type has been accepted by the IB system and that this order has yet to be elected. 
	 * The order is held in the IB system until the election criteria are met. At that time the order is transmitted to the 
	 * order destination as specified .
	 */
	public static final String PRESUBMITTED = "PreSubmitted";
	//- indicates that your order has been accepted at the order destination and is working.
	public static final String SUBMITTED = "Submitted";
	/**
	 * indicates that the balance of your order has been confirmed canceled by the IB system. 
	 * This could occur unexpectedly when IB or the destination has rejected your order.
	 */
	public static final String CANCELLED = "Cancelled";
	// - the order has been completely filled.
	public static final String FILLED = "Filled";
	
	//TODO add the constants in the Appendix A - Extended Order Attributes
	
	//TODO add the IBAlgo Parameters
	
	//TODO Extended Order Attributes
	
	//Available Market Scanners, scanner subscription constants
	/**
	 * Put option volumes are divided by call option volumes and the top underlying symbols with the lowest ratios are displayed.
	 */
	public static final String LOW_OPT_VOL_PUT_CALL_RATIO = "LOW_OPT_VOL_PUT_CALL_RATIO";
	/**
	 * Shows the top underlying contracts (stocks or indices) with the largest divergence between implied and historical volatilities.
	 */
	public static final String HIGH_OPT_IMP_VOLAT_OVER_HIST = "HIGH_OPT_IMP_VOLAT_OVER_HIST";
	/**
	 * Shows the top underlying contracts (stocks or indices) with the smallest divergence between implied and historical volatilities.
	 */
	public static final String LOW_OPT_IMP_VOLAT_OVER_HIST = "LOW_OPT_IMP_VOLAT_OVER_HIST";
	/**
	 * Shows the top underlying contracts (stocks or indices) with the highest vega-weighted implied volatility of 
	 * near-the-money options with an expiration date in the next two months.
	 */
	public static final String HIGH_OPT_IMP_VOLAT = "HIGH_OPT_IMP_VOLAT";
	/**
	 * Shows the top underlying contracts (stocks or indices) with the largest percent gain between current implied 
	 * volatility and yesterday's closing value of the 15 minute average of implied volatility.
	 */
	//private final static String TOP_OPT_IMP_VOLAT_GAIN = "";
	/**
	 * Put option volumes are divided by call option volumes and the top underlying symbols with the highest ratios are displayed.
	 */
	public static final String HIGH_OPT_VOLUME_PUT_CALL_RATIO = "HIGH_OPT_VOLUME_PUT_CALL_RATIO";
	/**
	 * Put option volumes are divided by call option volumes and the top underlying symbols with the lowest ratios are displayed.
	 */
	public static final String LOW_OPT_VOLUME_PUT_CALL_RATIO = "LOW_OPT_VOLUME_PUT_CALL_RATIO";
	/**
	 * Displays the most active contracts sorted descending by options volume.
	 */
	public static final String OPT_VOLUME_MOST_ACTIVE = "OPT_VOLUME_MOST_ACTIVE";
	/**
	 * Shows the top underlying contracts for highest options volume over a 10-day average.
	 */
	public static final String HOT_BY_OPT_VOLUME = "HOT_BY_OPT_VOLUME";
	/**
	 * Returns the top 50 contracts with the highest put/call ratio of outstanding option contracts.
	 */
	public static final String HIGH_OPT_OPEN_INTEREST_PUT_CALL_RATIO = "HIGH_OPT_OPEN_INTEREST_PUT_CALL_RATIO";
	/*
	 * Returns the top 50 contracts with the lowest put/call ratio of outstanding option contracts.
	 */
	public static final String LOW_OPT_OPEN_INTEREST_PUT_CALL_RATIO = "LOW_OPT_OPEN_INTEREST_PUT_CALL_RATIO";
	/**
	 * Contracts whose last trade price shows the highest percent increase from the previous night's closing price. 
	 */
	//private final static String TOP_PERC_GAIN = "";
	/**
	 * Contracts with the highest trading volume today, based on units used by TWS (lots for US stocks; contract for derivatives and non-US stocks).
	 */
	public static final String MOST_ACTIVE = "MOST_ACTIVE";
	/**
	 * Contracts whose last trade price shows the lowest percent increase from the previous night's closing price. 
	 */
	public static final String TOP_PERC_LOSE = "TOP_PERC_LOSE";
	/**
	 * Contracts where: today's Volume/avgDailyVolume is highest. avgDailyVolume is a 30-day exponential moving average of the contract's daily volume.
	 */
	public static final String HOT_BY_VOLUME = "HOT_BY_VOLUME";
	/**
	 * Shows the top underlying contracts (stocks or indices) with the largest percent loss between current implied volatility
	 * and yesterday's closing value of the 15 minute average of implied volatility.
	 */

	/**
	 * Futures whose last trade price shows the highest percent increase from the previous night's closing price.
	 */
	public static final String TOP_PERC_GAIN = "TOP_PERC_GAIN";
	
	/**
	 * Contracts where:
(lastTradePrice-prevClose)/avgDailyChange is highest in absolute value (positive or negative). 
The avgDailyChange is defined as an exponential moving average of the contract's (dailyClose-dailyOpen)
	 */
	public static final String HOT_BY_PRICE = "HOT_BY_PRICE";
	/**
	 * The top trade count during the day.
	 */
	public static final String TOP_TRADE_COUNT = "TOP_TRADE_COUNT";
	/**
	 * Contracts with the highest number of trades in the past 60 seconds (regardless of the sizes of those trades).
	 */
	public static final String TOP_TRADE_RATE = "TOP_TRADE_RATE";
	/**
	 * The largest difference between today's high and low, or yesterday's close if outside of today's range.
	 */
	public static final String TOP_PRICE_RANGE = "TOP_PRICE_RANGE";
	/**
	 * The largest price range (from Top Price Range calculation) over the volatility.
	 */
	public static final String HOT_BY_PRICE_RANGE = "HOT_BY_PRICE_RANGE";

	/**
	 * The top volume rate per minute.

	 */
	public static final String TOP_VOLUME_RATE = "TOP_VOLUME_RATE";
	
	/**
	 * Shows the top underlying contracts (stocks or indices) with the lowest vega-weighted implied volatility of 
	 * near-the-money options with an expiration date in the next two months.
	 */
	//private final static String LOW_OPT_IMP_VOLAT = "";
	
	/**
	 * Returns the top 50 underlying contracts with the (highest number of outstanding call contracts) + (highest number of outstanding put contracts)
	 */
	public static final String OPT_OPEN_INTEREST_MOST_ACTIVE = "OPT_OPEN_INTEREST_MOST_ACTIVE";
	
	/**
	 * Contracts that have not traded today.
	 */
	public static final String NOT_OPEN = "NOT_OPEN";
	
	/**
	 * Contracts for which trading has been halted.
	 */
	public static final String HALTED = "HALTED";
	
	/**
	 * Shows contracts with the highest percent price INCREASE between the last trade and opening prices.
	 */
	public static final String TOP_OPEN_PERC_GAIN = "TOP_OPEN_PERC_GAIN";
	
	/**
	 * Shows contracts with the highest percent price DECREASE between the last trade and opening prices.
	 */
	public static final String TOP_OPEN_PERC_LOSE = "TOP_OPEN_PERC_LOSE";
	
	/**
	 * Shows contracts with the highest percent price INCREASE between the previous close and today's opening prices.
	 */
	public static final String HIGH_OPEN_GAP = "HIGH_OPEN_GAP";
	
	/**
	 * Shows contracts with the highest percent price DECREASE between the previous close and today's opening prices.
	 */
	public static final String LOW_OPEN_GAP = "LOW_OPEN_GAP";
	
	/**
	 * Shows the top underlying contracts (stocks or indices) with the lowest vega-weighted implied volatility of 
	 * near-the-money options with an expiration date in the next two months.
	 */
	public static final String LOW_OPT_IMP_VOLAT = "LOW_OPT_IMP_VOLAT";
	
	/**
	 * Shows the top underlying contracts (stocks or indices) with the largest percent gain between current implied 
	 * volatility and yesterday's closing value of the 15 minute average of implied volatility.
	 */
	public static final String TOP_OPT_IMP_VOLAT_GAIN = "TOP_OPT_IMP_VOLAT_GAIN";
	
	/**
	 * Shows the top underlying contracts (stocks or indices) with the largest percent loss between current implied 
	 * volatility and yesterday's closing value of the 15 minute average of implied volatility.
	 */
	public static final String TOP_OPT_IMP_VOLAT_LOSE = "TOP_OPT_IMP_VOLAT_LOSE";
	
	/*
	 * The highest price for the past 13 weeks.
	 */
	public static final String HIGH_VS_13W_HL = "HIGH_VS_13W_HL";
	
	/**
	 * The lowest price for the past 13 weeks.
	 */
	public static final String LOW_VS_13W_HL = "LOW_VS_13W_HL";
	
	/**
	 * The highest price for the past 26 weeks.
	 */
	public static final String HIGH_VS_26W_HL = "HIGH_VS_26W_HL";
	
	/**
	 * The lowest price for the past 26 weeks.
	 */
	public static final String LOW_VS_26W_HL = "LOW_VS_26W_HL";
	
	/**
	 * The highest price for the past 52 weeks
	 */
	public static final String HIGH_VS_52W_HL = "HIGH_VS_52W_HL";
	
	/**
	 * The lowest price for the past 52 weeks.
	 */
	public static final String LOW_VS_52W_HL = "LOW_VS_52W_HL";
	
	/**
	 * Highlights the highest synthetic EFP interest rates available. These rates are computed by taking the price 
	 * differential between the SSF and the underlying stock and netting dividends to calculate an annualized synthetic
	 *  implied interest rate over the period of the SSF. The High rates may present an investment opportunity.
	 */
	public static final String HIGH_SYNTH_BID_REV_NAT_YIELD = "HIGH_SYNTH_BID_REV_NAT_YIELD";
	
	/**
	 * Highlights the lowest synthetic EFP interest rates available. These rates are computed by taking the price 
	 * differential between the SSF and the underlying stock and netting dividends to calculate an annualized synthetic 
	 * implied interest rate over the period of the SSF. The Low rates may present a borrowing opportunity.
 	 */
	public static final String LOW_SYNTH_BID_REV_NAT_YIELD = "LOW_SYNTH_BID_REV_NAT_YIELD";
	
	
	//TODO Supported Time Zones (link is not working on IB site, will have to talk to tech support about this.)
	
	//market data tick types
	public static final int OPTION_VOLUME = 100;
	public static final int OPTION_OPEN_INTEREST = 101;
	public static final int HISTORICAL_VOLATILITY = 104;
	public static final int AVERAGE_OPT_VOLUME = 105;
	public static final int OPTION_IMPLIED_VOLATILITY = 106;
	public static final int CLOSE_IMPLIED_VOLATILITY = 107;
	public static final int BOND_ANALYTIC_DATA = 125;
	public static final int MISC_STATS = 165;
	public static final int CSCREEN = 166;
	public static final int AUCTION = 225;
	public static final int MARK_PRICE = 232;
	public static final int RTVOLUME = 233;
	public static final int INVENTORY = 236;
	public static final int FUNDAMENTALS = 258;
	public static final int TRADECOUNT = 293;
	public static final int TRADERATE = 294;
	public static final int VOLUMERATE = 295;
	public static final int LASTRTHTRADE = 318;
	public static final int PARTICIPATIONMONITOR = 370;
	public static final int CTTTICKTAG = 377;
	public static final int IB_RATE = 381;
	public static final int RFQTICKRESPTAG = 384;
	public static final int DMM = 387;
	public static final int ISSUER_FUNDAMENTALS = 388;
	public static final int IBWARRANTIMPVOLCOMPETETICK = 391;
	public static final int FUTURE_MARGINS = 407;
	public static final int REAL_TIME_HISTORIC_VOLATILITY = 411;
}
