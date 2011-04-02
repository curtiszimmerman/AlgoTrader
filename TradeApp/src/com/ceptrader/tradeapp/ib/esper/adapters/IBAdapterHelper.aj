package TradeApp.IB;

import com.ceptrader.tradeapp.ib.esper.adapters.*;

import com.ib.client.EClientSocket;

public aspect IBAdapterHelper {
	pointcut isConn(EClientSocket ibSoc): call(* EClientSocket.connectionError(..)) && target(ibSoc);
		
	before(final EClientSocket ibSoc) : isConn(ibSoc) {
		synchronized (ibSoc) {
			if (!ibSoc.isConnected()) {
					IBClient.connect();
			}
		}
	}
}
