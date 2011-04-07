package com.ib.client;

import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

public class EWrapperMsgGenerator extends AnyWrapperMsgGenerator {
	public static final String	SCANNER_PARAMETERS	= "SCANNER PARAMETERS:";
	public static final String	FINANCIAL_ADVISOR	= "FA:";
	
	static public String tickPrice(final int tickerId, final int field,
	        final double price, final int canAutoExecute) {
		return "id=" + tickerId + "  " + TickType.getField(field) + "=" +
		        price + " " +
		        (canAutoExecute != 0 ? " canAutoExecute" : " noAutoExecute");
	}
	
	static public String tickSize(final int tickerId, final int field,
	        final int size) {
		return "id=" + tickerId + "  " + TickType.getField(field) + "=" + size;
	}
	
	static public String tickOptionComputation(final int tickerId,
	        final int field, final double impliedVol,
	        final double delta, final double optPrice, final double pvDividend,
	        final double gamma, final double vega, final double theta,
	        final double undPrice) {
		final String toAdd = "id=" +
		        tickerId +
		        "  " +
		        TickType.getField(field) +
		        ": vol = " +
		        (impliedVol >= 0 && impliedVol != Double.MAX_VALUE ? Double
		                .toString(impliedVol) : "N/A") +
		        " delta = " +
		        (Math.abs(delta) <= 1 ? Double.toString(delta) : "N/A") +
		        " gamma = " +
		        (Math.abs(gamma) <= 1 ? Double.toString(gamma) : "N/A") +
		        " vega = " +
		        (Math.abs(vega) <= 1 ? Double.toString(vega) : "N/A") +
		        " theta = " +
		        (Math.abs(theta) <= 1 ? Double.toString(theta) : "N/A") +
		        " optPrice = " +
		        (optPrice >= 0 && optPrice != Double.MAX_VALUE ? Double
		                .toString(optPrice) : "N/A") +
		        " pvDividend = " +
		        (pvDividend >= 0 && pvDividend != Double.MAX_VALUE ? Double
		                .toString(pvDividend) : "N/A") +
		        " undPrice = " +
		        (undPrice >= 0 && undPrice != Double.MAX_VALUE ? Double
		                .toString(undPrice) : "N/A");
		return toAdd;
	}
	
	static public String tickGeneric(final int tickerId, final int tickType,
	        final double value) {
		return "id=" + tickerId + "  " + TickType.getField(tickType) + "=" +
		        value;
	}
	
	static public String tickString(final int tickerId, final int tickType,
	        final String value) {
		return "id=" + tickerId + "  " + TickType.getField(tickType) + "=" +
		        value;
	}
	
	static public String tickEFP(final int tickerId, final int tickType,
	        final double basisPoints,
	        final String formattedBasisPoints, final double impliedFuture,
	        final int holdDays,
	        final String futureExpiry, final double dividendImpact,
	        final double dividendsToExpiry) {
		return "id=" + tickerId + "  " + TickType.getField(tickType)
		        + ": basisPoints = " + basisPoints + "/" + formattedBasisPoints
		        + " impliedFuture = " + impliedFuture + " holdDays = " +
		        holdDays +
		        " futureExpiry = " + futureExpiry + " dividendImpact = " +
		        dividendImpact +
		        " dividends to expiry = " + dividendsToExpiry;
	}
	
	static public String orderStatus(final int orderId, final String status,
	        final int filled, final int remaining,
	        final double avgFillPrice, final int permId, final int parentId,
	        final double lastFillPrice,
	        final int clientId, final String whyHeld) {
		return "order status: orderId=" + orderId + " clientId=" + clientId +
		        " permId=" + permId +
		        " status=" + status + " filled=" + filled + " remaining=" +
		        remaining +
		        " avgFillPrice=" + avgFillPrice + " lastFillPrice=" +
		        lastFillPrice +
		        " parent Id=" + parentId + " whyHeld=" + whyHeld;
	}
	
	static public String openOrder(final int orderId, final Contract contract,
	        final Order order, final OrderState orderState) {
		String msg = "open order: orderId=" + orderId +
		        " action=" + order.m_action +
		        " quantity=" + order.m_totalQuantity +
		        " symbol=" + contract.m_symbol +
		        " exchange=" + contract.m_exchange +
		        " secType=" + contract.m_secType +
		        " type=" + order.m_orderType +
		        " lmtPrice=" + order.m_lmtPrice +
		        " auxPrice=" + order.m_auxPrice +
		        " TIF=" + order.m_tif +
		        " localSymbol=" + contract.m_localSymbol +
		        " client Id=" + order.m_clientId +
		        " parent Id=" + order.m_parentId +
		        " permId=" + order.m_permId +
		        " outsideRth=" + order.m_outsideRth +
		        " hidden=" + order.m_hidden +
		        " discretionaryAmt=" + order.m_discretionaryAmt +
		        " triggerMethod=" + order.m_triggerMethod +
		        " goodAfterTime=" + order.m_goodAfterTime +
		        " goodTillDate=" + order.m_goodTillDate +
		        " faGroup=" + order.m_faGroup +
		        " faMethod=" + order.m_faMethod +
		        " faPercentage=" + order.m_faPercentage +
		        " faProfile=" + order.m_faProfile +
		        " shortSaleSlot=" + order.m_shortSaleSlot +
		        " designatedLocation=" + order.m_designatedLocation +
		        " ocaGroup=" + order.m_ocaGroup +
		        " ocaType=" + order.m_ocaType +
		        " rule80A=" + order.m_rule80A +
		        " allOrNone=" + order.m_allOrNone +
		        " minQty=" + order.m_minQty +
		        " percentOffset=" + order.m_percentOffset +
		        " eTradeOnly=" + order.m_eTradeOnly +
		        " firmQuoteOnly=" + order.m_firmQuoteOnly +
		        " nbboPriceCap=" + order.m_nbboPriceCap +
		        " auctionStrategy=" + order.m_auctionStrategy +
		        " startingPrice=" + order.m_startingPrice +
		        " stockRefPrice=" + order.m_stockRefPrice +
		        " delta=" + order.m_delta +
		        " stockRangeLower=" + order.m_stockRangeLower +
		        " stockRangeUpper=" + order.m_stockRangeUpper +
		        " volatility=" + order.m_volatility +
		        " volatilityType=" + order.m_volatilityType +
		        " deltaNeutralOrderType=" + order.m_deltaNeutralOrderType +
		        " deltaNeutralAuxPrice=" + order.m_deltaNeutralAuxPrice +
		        " continuousUpdate=" + order.m_continuousUpdate +
		        " referencePriceType=" + order.m_referencePriceType +
		        " trailStopPrice=" + order.m_trailStopPrice +
		        " scaleInitLevelSize=" +
		        Util.IntMaxString(order.m_scaleInitLevelSize) +
		        " scaleSubsLevelSize=" +
		        Util.IntMaxString(order.m_scaleSubsLevelSize) +
		        " scalePriceIncrement=" +
		        Util.DoubleMaxString(order.m_scalePriceIncrement) +
		        " account=" + order.m_account +
		        " settlingFirm=" + order.m_settlingFirm +
		        " clearingAccount=" + order.m_clearingAccount +
		        " clearingIntent=" + order.m_clearingIntent +
		        " notHeld=" + order.m_notHeld +
		        " whatIf=" + order.m_whatIf;
		
		if ("BAG".equals(contract.m_secType)) {
			if (contract.m_comboLegsDescrip != null) {
				msg += " comboLegsDescrip=" + contract.m_comboLegsDescrip;
			}
			if (order.m_basisPoints != Double.MAX_VALUE) {
				msg += " basisPoints=" + order.m_basisPoints;
				msg += " basisPointsType=" + order.m_basisPointsType;
			}
		}
		
		if (contract.m_underComp != null) {
			final UnderComp underComp = contract.m_underComp;
			msg +=
			        " underComp.conId =" + underComp.m_conId +
			                " underComp.delta =" + underComp.m_delta +
			                " underComp.price =" + underComp.m_price;
		}
		
		if (!Util.StringIsEmpty(order.m_algoStrategy)) {
			msg += " algoStrategy=" + order.m_algoStrategy;
			msg += " algoParams={";
			if (order.m_algoParams != null) {
				final Vector algoParams = order.m_algoParams;
				for (int i = 0; i < algoParams.size(); ++i) {
					final TagValue param = (TagValue) algoParams.elementAt(i);
					if (i > 0) {
						msg += ",";
					}
					msg += param.m_tag + "=" + param.m_value;
				}
			}
			msg += "}";
		}
		
		final String orderStateMsg =
		        " status=" + orderState.m_status
		                + " initMargin=" + orderState.m_initMargin
		                + " maintMargin=" + orderState.m_maintMargin
		                + " equityWithLoan=" + orderState.m_equityWithLoan
		                + " commission=" +
		                Util.DoubleMaxString(orderState.m_commission)
		                + " minCommission=" +
		                Util.DoubleMaxString(orderState.m_minCommission)
		                + " maxCommission=" +
		                Util.DoubleMaxString(orderState.m_maxCommission)
		                + " commissionCurrency=" +
		                orderState.m_commissionCurrency
		                + " warningText=" + orderState.m_warningText;
		
		return msg + orderStateMsg;
	}
	
	static public String openOrderEnd() {
		return " =============== end ===============";
	}
	
	static public String
	        updateAccountValue(final String key, final String value,
	                final String currency, final String accountName) {
		return "updateAccountValue: " + key + " " + value + " " + currency +
		        " " + accountName;
	}
	
	static public String updatePortfolio(final Contract contract,
	        final int position, final double marketPrice,
	                                     final double marketValue,
	        final double averageCost, final double unrealizedPNL,
	                                     final double realizedPNL,
	        final String accountName) {
		final String msg = "updatePortfolio: "
		        + EWrapperMsgGenerator.contractMsg(contract)
		        + position + " " + marketPrice + " " + marketValue + " " +
		        averageCost + " " + unrealizedPNL + " " + realizedPNL + " " +
		        accountName;
		return msg;
	}
	
	static public String updateAccountTime(final String timeStamp) {
		return "updateAccountTime: " + timeStamp;
	}
	
	static public String accountDownloadEnd(final String accountName) {
		return "accountDownloadEnd: " + accountName;
	}
	
	static public String nextValidId(final int orderId) {
		return "Next Valid Order ID: " + orderId;
	}
	
	static public String contractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		final Contract contract = contractDetails.m_summary;
		final String msg = "reqId = " + reqId +
		        " ===================================\n"
		        + " ---- Contract Details begin ----\n"
		        + EWrapperMsgGenerator.contractMsg(contract) +
		        EWrapperMsgGenerator.contractDetailsMsg(contractDetails)
		        + " ---- Contract Details End ----\n";
		return msg;
	}
	
	private static String contractDetailsMsg(
	        final ContractDetails contractDetails) {
		final String msg = "marketName = " + contractDetails.m_marketName +
		        "\n"
		        + "tradingClass = " + contractDetails.m_tradingClass + "\n"
		        + "minTick = " + contractDetails.m_minTick + "\n"
		        + "price magnifier = " + contractDetails.m_priceMagnifier +
		        "\n"
		        + "orderTypes = " + contractDetails.m_orderTypes + "\n"
		        + "validExchanges = " + contractDetails.m_validExchanges + "\n"
		        + "underConId = " + contractDetails.m_underConId + "\n"
		        + "longName = " + contractDetails.m_longName + "\n"
		        + "contractMonth = " + contractDetails.m_contractMonth + "\n"
		        + "industry = " + contractDetails.m_industry + "\n"
		        + "category = " + contractDetails.m_category + "\n"
		        + "subcategory = " + contractDetails.m_subcategory + "\n"
		        + "timeZoneId = " + contractDetails.m_timeZoneId + "\n"
		        + "tradingHours = " + contractDetails.m_tradingHours + "\n"
		        + "liquidHours = " + contractDetails.m_liquidHours + "\n";
		return msg;
	}
	
	static public String contractMsg(final Contract contract) {
		final String msg = "conid = " + contract.m_conId + "\n"
		        + "symbol = " + contract.m_symbol + "\n"
		        + "secType = " + contract.m_secType + "\n"
		        + "expiry = " + contract.m_expiry + "\n"
		        + "strike = " + contract.m_strike + "\n"
		        + "right = " + contract.m_right + "\n"
		        + "multiplier = " + contract.m_multiplier + "\n"
		        + "exchange = " + contract.m_exchange + "\n"
		        + "primaryExch = " + contract.m_primaryExch + "\n"
		        + "currency = " + contract.m_currency + "\n"
		        + "localSymbol = " + contract.m_localSymbol + "\n";
		return msg;
	}
	
	static public String bondContractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		final Contract contract = contractDetails.m_summary;
		final String msg = "reqId = " + reqId +
		        " ===================================\n"
		        + " ---- Bond Contract Details begin ----\n"
		        + "symbol = " + contract.m_symbol + "\n"
		        + "secType = " + contract.m_secType + "\n"
		        + "cusip = " + contractDetails.m_cusip + "\n"
		        + "coupon = " + contractDetails.m_coupon + "\n"
		        + "maturity = " + contractDetails.m_maturity + "\n"
		        + "issueDate = " + contractDetails.m_issueDate + "\n"
		        + "ratings = " + contractDetails.m_ratings + "\n"
		        + "bondType = " + contractDetails.m_bondType + "\n"
		        + "couponType = " + contractDetails.m_couponType + "\n"
		        + "convertible = " + contractDetails.m_convertible + "\n"
		        + "callable = " + contractDetails.m_callable + "\n"
		        + "putable = " + contractDetails.m_putable + "\n"
		        + "descAppend = " + contractDetails.m_descAppend + "\n"
		        + "exchange = " + contract.m_exchange + "\n"
		        + "currency = " + contract.m_currency + "\n"
		        + "marketName = " + contractDetails.m_marketName + "\n"
		        + "tradingClass = " + contractDetails.m_tradingClass + "\n"
		        + "conid = " + contract.m_conId + "\n"
		        + "minTick = " + contractDetails.m_minTick + "\n"
		        + "orderTypes = " + contractDetails.m_orderTypes + "\n"
		        + "validExchanges = " + contractDetails.m_validExchanges + "\n"
		        + "nextOptionDate = " + contractDetails.m_nextOptionDate + "\n"
		        + "nextOptionType = " + contractDetails.m_nextOptionType + "\n"
		        + "nextOptionPartial = " + contractDetails.m_nextOptionPartial +
		        "\n"
		        + "notes = " + contractDetails.m_notes + "\n"
		        + "longName = " + contractDetails.m_longName + "\n"
		        + " ---- Bond Contract Details End ----\n";
		return msg;
	}
	
	static public String contractDetailsEnd(final int reqId) {
		return "reqId = " + reqId + " =============== end ===============";
	}
	
	static public String execDetails(final int reqId, final Contract contract,
	        final Execution execution) {
		final String msg = " ---- Execution Details begin ----\n"
		        + "reqId = " + reqId + "\n"
		        + "orderId = " + execution.m_orderId + "\n"
		        + "clientId = " + execution.m_clientId + "\n"
		        + "symbol = " + contract.m_symbol + "\n"
		        + "secType = " + contract.m_secType + "\n"
		        + "expiry = " + contract.m_expiry + "\n"
		        + "strike = " + contract.m_strike + "\n"
		        + "right = " + contract.m_right + "\n"
		        + "contractExchange = " + contract.m_exchange + "\n"
		        + "currency = " + contract.m_currency + "\n"
		        + "localSymbol = " + contract.m_localSymbol + "\n"
		        + "execId = " + execution.m_execId + "\n"
		        + "time = " + execution.m_time + "\n"
		        + "acctNumber = " + execution.m_acctNumber + "\n"
		        + "executionExchange = " + execution.m_exchange + "\n"
		        + "side = " + execution.m_side + "\n"
		        + "shares = " + execution.m_shares + "\n"
		        + "price = " + execution.m_price + "\n"
		        + "permId = " + execution.m_permId + "\n"
		        + "liquidation = " + execution.m_liquidation + "\n"
		        + "cumQty = " + execution.m_cumQty + "\n"
		        + "avgPrice = " + execution.m_avgPrice + "\n"
		        + " ---- Execution Details end ----\n";
		return msg;
	}
	
	static public String execDetailsEnd(final int reqId) {
		return "reqId = " + reqId + " =============== end ===============";
	}
	
	static public String updateMktDepth(final int tickerId, final int position,
	        final int operation, final int side,
	                                     final double price, final int size) {
		return "updateMktDepth: " + tickerId + " " + position + " " +
		        operation + " " + side + " " + price + " " + size;
	}
	
	static public String updateMktDepthL2(final int tickerId,
	        final int position, final String marketMaker,
	                                       final int operation, final int side,
	        final double price, final int size) {
		return "updateMktDepth: " + tickerId + " " + position + " " +
		        marketMaker + " " + operation + " " + side + " " + price + " " +
		        size;
	}
	
	static public String updateNewsBulletin(final int msgId, final int msgType,
	        final String message, final String origExchange) {
		return "MsgId=" + msgId + " :: MsgType=" + msgType + " :: Origin=" +
		        origExchange + " :: Message=" + message;
	}
	
	static public String managedAccounts(final String accountsList) {
		return "Connected : The list of managed accounts are : [" +
		        accountsList + "]";
	}
	
	static public String receiveFA(final int faDataType, final String xml) {
		return EWrapperMsgGenerator.FINANCIAL_ADVISOR + " " +
		        EClientSocket.faMsgTypeName(faDataType) + " " + xml;
	}
	
	static public String historicalData(final int reqId, final String date,
	        final double open, final double high, final double low,
	                                    final double close, final int volume,
	        final int count, final double WAP, final boolean hasGaps) {
		return "id=" + reqId +
		        " date = " + date +
		        " open=" + open +
		        " high=" + high +
		        " low=" + low +
		        " close=" + close +
		        " volume=" + volume +
		        " count=" + count +
		        " WAP=" + WAP +
		        " hasGaps=" + hasGaps;
	}
	
	public static String realtimeBar(final int reqId, final long time,
	        final double open,
	        final double high, final double low, final double close,
	        final long volume, final double wap, final int count) {
		return "id=" + reqId +
		        " time = " + time +
		        " open=" + open +
		        " high=" + high +
		        " low=" + low +
		        " close=" + close +
		        " volume=" + volume +
		        " count=" + count +
		        " WAP=" + wap;
	}
	
	static public String scannerParameters(final String xml) {
		return EWrapperMsgGenerator.SCANNER_PARAMETERS + "\n" + xml;
	}
	
	static public String scannerData(final int reqId, final int rank,
	        final ContractDetails contractDetails,
	                                 final String distance,
	        final String benchmark, final String projection,
	                                 final String legsStr) {
		final Contract contract = contractDetails.m_summary;
		return "id = " + reqId +
		        " rank=" + rank +
		        " symbol=" + contract.m_symbol +
		        " secType=" + contract.m_secType +
		        " expiry=" + contract.m_expiry +
		        " strike=" + contract.m_strike +
		        " right=" + contract.m_right +
		        " exchange=" + contract.m_exchange +
		        " currency=" + contract.m_currency +
		        " localSymbol=" + contract.m_localSymbol +
		        " marketName=" + contractDetails.m_marketName +
		        " tradingClass=" + contractDetails.m_tradingClass +
		        " distance=" + distance +
		        " benchmark=" + benchmark +
		        " projection=" + projection +
		        " legsStr=" + legsStr;
	}
	
	static public String scannerDataEnd(final int reqId) {
		return "id = " + reqId + " =============== end ===============";
	}
	
	static public String currentTime(final long time) {
		return "current time = " + time +
		        " (" +
		        DateFormat.getDateTimeInstance().format(new Date(time * 1000)) +
		        ")";
	}
	
	static public String fundamentalData(final int reqId, final String data) {
		return "id  = " + reqId + " len = " + data.length() + '\n' + data;
	}
	
	static public String deltaNeutralValidation(final int reqId,
	        final UnderComp underComp) {
		return "id = " + reqId
		        + " underComp.conId =" + underComp.m_conId
		        + " underComp.delta =" + underComp.m_delta
		        + " underComp.price =" + underComp.m_price;
	}
	
	static public String tickSnapshotEnd(final int tickerId) {
		return "id=" + tickerId + " =============== end ===============";
	}
}