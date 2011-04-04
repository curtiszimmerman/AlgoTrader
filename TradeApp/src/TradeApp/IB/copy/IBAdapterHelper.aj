package TradeApp.IB.copy;

import TradeApp.MainHelper;

import com.ib.client.*;

public aspect IBAdapterHelper {
	pointcut isConn(EClientSocket ibSoc): call(* EClientSocket.connectionError(..)) && target(ibSoc);
	
	pointcut reqWithContract(EClientSocket ibSoc, int reqId, Contract contract): call(* *..*req*(..)) && target(ibSoc) && args(reqId) && args(contract);
	
	pointcut reqHist(EClientSocket ibSoc, int reqId, String barSizeSetting): call(* *..*req*Hist*(..)) && target(ibSoc) && args(reqId) && args(barSizeSetting);
	
	before(EClientSocket ibSoc) : isConn(ibSoc) {
		synchronized (ibSoc) {
			if (!ibSoc.isConnected()) {
				if (ibSoc instanceof IBClient) {
					((IBClient)ibSoc).notifyDisconnection();
					((IBClient)ibSoc).connect();
				}
			}
		}
	}
	
	before(EClientSocket ibSoc, int reqId, Contract contract) : reqWithContract(ibSoc, reqId, contract) {
		if (ibSoc instanceof IBClient) 
			((IBClient)ibSoc).mapReqIdContract(reqId, contract);
	}
	
	before(EClientSocket ibSoc, int reqId, String barSizeSetting) : reqHist(ibSoc, reqId, barSizeSetting) {
		if (ibSoc instanceof IBClient) {
			int space = barSizeSetting.indexOf(" ");
			String num = barSizeSetting.substring(0, space);
			String timeUnit = barSizeSetting.substring(space + 1);
			
			int time = Integer.parseInt(num);
			if (timeUnit.indexOf("min") >= 0)
				time *= 60;
			if (timeUnit.indexOf("hour") >= 0)
				time *= 60 * 60;
			if (timeUnit.indexOf("day") >= 0)
				time *= 60 * 60 * 24;
			if (timeUnit.indexOf("week") >= 0)
				time *= 60 * 60 * 24 * 7;
			if (timeUnit.indexOf("month") >= 0)
				time *= 60 * 60 * 24 * 30;
			if (timeUnit.indexOf("year") >= 0)
				time *= (int)60 * 60 * 24 * 365.25;
			((IBClient)ibSoc).mapReqIdFrequency(reqId, time);
		}
	}
}
