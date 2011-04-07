package com.ceptrader.ib.esper.adapters;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.apache.commons.beanutils.MethodUtils;

import com.ceptrader.esper.generic.pojoevents.BuyLmt;
import com.ceptrader.esper.generic.pojoevents.BuyMkt;
import com.ceptrader.esper.generic.pojoevents.BuyStop;
import com.ceptrader.esper.generic.pojoevents.SellLmt;
import com.ceptrader.esper.generic.pojoevents.SellMkt;
import com.ceptrader.esper.generic.pojoevents.SellStop;
import com.ceptrader.util.BasicUtils;
import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Order;

public class IBUtils {
	static {
		IBClient.connect();
	}
	
	public static void pumpTradeEvent(final String mName, final Object[] obj)
	        throws NoSuchMethodException, IllegalAccessException,
	        InvocationTargetException {
		final IBClient ibClient = IBClient.getIBClient();
		MethodUtils.invokeMethod(ibClient, mName, obj);
	}
	
	public static void pumpTradeEventWithParams(final String mName,
	        final Object[] obj,
	        final Class[] param)
	        throws NoSuchMethodException, IllegalAccessException,
	        InvocationTargetException {
		final IBClient ibClient = IBClient.getIBClient();
		MethodUtils.invokeMethod(ibClient, mName, obj, param);
	}
	
	public static Contract getContractFut(
	        final String currency,
	        final String symbol, final String exchange, final String exp,
	        final int mult) {
		final Contract contract = new Contract();
		contract.m_secType = "FUT";
		contract.m_currency = currency;
		contract.m_symbol = symbol;
		contract.m_exchange = exchange;
		contract.m_multiplier = String.valueOf(mult);
		contract.m_expiry = exp;
		
		return contract;
	}
	
	public synchronized static int reqQuotesFut(
	        final String currency,
	        final String symbol, final String exchange, final String exp,
	        final int mult, final int rows) {
		final Contract contract = new Contract();
		contract.m_secType = "FUT";
		contract.m_currency = currency;
		contract.m_symbol = symbol;
		contract.m_exchange = exchange;
		contract.m_multiplier = String.valueOf(mult);
		contract.m_expiry = exp;
		
		return IBUtils.reqQuoteForContract(contract, rows);
	}
	
	public static Contract getContractOpt(
	        final String currency,
	        final String symbol, final String exchange, final String exp,
	        final String right,
	        final double strike, final int mult) {
		final Contract contract = new Contract();
		contract.m_secType = "OPT";
		contract.m_currency = currency;
		contract.m_symbol = symbol;
		contract.m_exchange = exchange;
		contract.m_right = right;
		contract.m_strike = strike;
		contract.m_multiplier = String.valueOf(mult);
		contract.m_expiry = exp;
		
		return contract;
	}
	
	public synchronized static int reqQuotesOpt(
	        final String currency,
	        final String symbol, final String exchange, final String exp,
	        final String right,
	        final double strike, final int mult, final int rows) {
		final Contract contract = new Contract();
		contract.m_secType = "OPT";
		contract.m_currency = currency;
		contract.m_symbol = symbol;
		contract.m_exchange = exchange;
		contract.m_right = right;
		contract.m_strike = strike;
		contract.m_multiplier = String.valueOf(mult);
		contract.m_expiry = exp;
		
		return IBUtils.reqQuoteForContract(contract, rows);
	}
	
	public synchronized static int reqQuotes(final String type,
	        final String currency,
	        final String symbol, final String exchange, final int rows) {
		final Contract contract = new Contract();
		contract.m_secType = type;
		contract.m_currency = currency;
		contract.m_symbol = symbol;
		contract.m_exchange = exchange;
		
		return IBUtils.reqQuoteForContract(contract, rows);
	}
	
	public static synchronized int reqQuoteForContract(final Contract contract,
	        final int rows) {
		final IBClient c = IBClient.getIBClient();
		
		final int reqID = IBAdapter.getNextValidId();
		if (rows <= 0) {
			c.reqMktData(reqID, contract, null, false);
		} else {
			c.reqMktDepth(reqID, contract, rows);
		}
		return reqID;
	}
	
	public static Contract getContract(final String type,
	        final String currency,
	        final String symbol, final String exchange) {
		final Contract contract = new Contract();
		contract.m_secType = type;
		contract.m_currency = currency;
		contract.m_symbol = symbol;
		contract.m_exchange = exchange;
		
		return contract;
	}
	
	public static Contract getCombo(final String exchange,
	        final String crncy, final String desc, final ComboLeg... cl) {
		final Contract c = new Contract();
		
		c.m_symbol = "USD";
		c.m_secType = "BAG";
		c.m_exchange = exchange;
		c.m_currency = crncy;
		
		final Vector<ComboLeg> legs = new Vector<ComboLeg>();
		for (final ComboLeg leg : cl) {
			legs.add(leg);
		}
		
		c.m_comboLegs = legs;
		c.m_comboLegsDescrip = desc;
		
		return c;
	}
	
	public static ComboLeg getComboLeg(final ContractDetails cd,
	        final int ratio, final String action, final String exchange) {
		final ComboLeg cl = new ComboLeg();
		cl.m_conId = cd.m_summary.m_conId;
		cl.m_ratio = ratio;
		cl.m_action = action;
		cl.m_exchange = exchange;
		
		return cl;
	}
	
	public static Order getLongLmt(final BuyLmt ord) {
		return IBUtils.getOrder("BUY", ord.getOrderType(), ord.getSize(),
		        ord.getLevel(), 0, 100, false,
		        ord.getParent(), 0, ord.getTouch(), ord.getGoodAfter(),
		        ord.getGoodUntil());
	}
	
	public static Order getShortLmt(final SellLmt ord) {
		return IBUtils.getOrder("SELL", ord.getOrderType(), ord.getSize(),
		        ord.getLevel(), 0, 100, false,
		        ord.getParent(), 0, ord.getTouch(), ord.getGoodAfter(),
		        ord.getGoodUntil());
	}
	
	public static Order getLongLmtTrail(final BuyLmt ord) {
		return IBUtils.getOrder("BUY", ord.getOrderType(), ord.getSize(),
		        ord.getLevel(), 0, 100, false,
		        ord.getParent(), ord.isTrailPct() ? ord.getTrail() : 0,
		        !ord.isTrailPct() ? ord.getTrail() : 0,
		        ord.getGoodAfter(), ord.getGoodUntil());
	}
	
	public static Order getShortLmtTrail(final SellLmt ord) {
		return IBUtils.getOrder("SELL", ord.getOrderType(), ord.getSize(),
		        ord.getLevel(), 0, 100, false,
		        ord.getParent(), ord.isTrailPct() ? ord.getTrail() : 0,
		        !ord.isTrailPct() ? ord.getTrail() : 0,
		        ord.getGoodAfter(), ord.getGoodUntil());
	}
	
	public static Order getLongMkt(final BuyMkt ord) {
		return IBUtils.getOrder("BUY", ord.getOrderType(), ord.getSize(),
		        0, 0, 100, false,
		        ord.getParent(), 0, ord.getTouch(), ord.getGoodAfter(),
		        ord.getGoodUntil());
	}
	
	public static Order getShortMkt(final SellMkt ord) {
		return IBUtils.getOrder("SELL", ord.getOrderType(), ord.getSize(),
		        0, 0, 100, false,
		        ord.getParent(), 0, ord.getTouch(), ord.getGoodAfter(),
		        ord.getGoodUntil());
	}
	
	public static Order getLongStop(final BuyStop ord) {
		return IBUtils.getOrder("BUY", ord.getOrderType(), (int) ord.getSize(),
		        ord.getLimit(), ord.getLevel(), 100, false,
		        ord.getParent(), ord.isTrailPct() ? ord.getTtrail() : 0,
		        !ord.isTrailPct() ? ord.getTtrail() : 0, ord.getGoodAfter(),
		        ord.getGoodUntil());
	}
	
	public static Order getShortStop(final SellStop ord) {
		return IBUtils.getOrder("SELL", ord.getOrderType(),
		        (int) ord.getSize(),
		        ord.getLimit(), ord.getLevel(), 100, false,
		        ord.getParent(), ord.isTrailPct() ? ord.getTtrail() : 0,
		        !ord.isTrailPct() ? ord.getTtrail() : 0, ord.getGoodAfter(),
		        ord.getGoodUntil());
	}
	
	public static Order getOrder(final String action, final String type,
	        final int qty,
	        final double lmt, final double stop, final int min,
	        final boolean allOrNone, final int parent, final double pctOffSet,
	        final double trailAmt, final String goodAfter,
	        final String goodUntil) {
		final Order o = new Order();
		
		o.m_action = action;
		o.m_allOrNone = allOrNone;
		o.m_lmtPrice = lmt;
		o.m_auxPrice = lmt > 0 && stop > 0 ? stop : trailAmt;
		o.m_totalQuantity = qty;
		o.m_tif = type;
		o.m_trailStopPrice = trailAmt <= 0 || pctOffSet <= 0 ? 0 : stop;
		o.m_percentOffset = pctOffSet;
		o.m_minQty = min;
		o.m_parentId = parent;
		o.m_goodAfterTime = goodAfter;
		o.m_goodTillDate = goodUntil;
		
		return o;
	}
	
	public static void placeOrder(final int id, final Contract contract,
	        final Order order) {
		IBClient.getIBClient().placeOrder(id, contract, order);
	}
	
	public static long dateToMilli(final String date) {
		return BasicUtils.checkDateTime(date).getMillis();
	}
	
	public static final long	milliInASecond	= 1000;
	public static final long	milliInAMinute	= 1000 * 60;
	public static final long	milliInAHour	= 1000 * 60 * 60;
	public static final long	milliInADay	   = 1000 * 60 * 60 * 24;
	public static final long	milliInAWeek	= 1000 * 60 * 60 * 24 * 7;
	public static final long	milliInAMonth	= (long) (1000 * 60 * 60 * 24 *
	                                                   365.2425 / 12);
	public static final long	milliInAYear	= (long) (1000 * 60 * 60 * 24 *
	                                                   365.2425);
	
	public static long duration(final String duration) {
		final int space = duration.lastIndexOf(' ');
		final String units = duration.substring(space + 1).trim().toUpperCase();
		final String valueStr = duration.substring(0, space).trim();
		long value = Long.parseLong(valueStr);
		
		if (units.startsWith("SEC")) {
			value *= IBUtils.milliInASecond;
		} else if (units.startsWith("MIN")) {
			value *= IBUtils.milliInAMinute;
		} else if (units.startsWith("HOUR")) {
			value *= IBUtils.milliInAHour;
		} else if (units.startsWith("DAY")) {
			value *= IBUtils.milliInADay;
		} else if (units.startsWith("WEEK")) {
			value *= IBUtils.milliInAWeek;
		} else if (units.startsWith("MONTH")) {
			value *= IBUtils.milliInAMonth;
		} else if (units.startsWith("YEAR")) {
			value *= IBUtils.milliInAYear;
		}
		
		return value;
	}
}
