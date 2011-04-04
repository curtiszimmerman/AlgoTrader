
package TradeApp.IB.copy;

import java.util.HashMap;

import TradeApp.Algo.Stratergy.ConnectionManager;
import TradeApp.Util.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;

public class IBClient extends EClientSocket {
	private static int								clientId	= 0;
	private static String							host		= null;
	private static int								port		= 4001;										// 7496;
	private static IBClient							ibc;
	private static IBAdapter						ibAdp;
	
	private static HashMap<Integer, Contract>	reqIdMap	= new HashMap<Integer, Contract>();
	private static HashMap<Integer, Integer>	reqIdFrq	= new HashMap<Integer, Integer>();
	
	public static Contract getReqIdContract(final int Id) {
		return IBClient.reqIdMap.get(Id);
	}
	
	public static int getReqIdFrequency(final int Id) {
		return IBClient.reqIdFrq.get(Id);
	}
	
	public static void mapReqIdContract(final int Id, final Contract c) {
		IBClient.reqIdMap.put(Id, c);
	}
	
	public static void mapReqIdFrequency(final int Id, final int f) {
		IBClient.reqIdFrq.put(Id, f);
	}
	
	public synchronized static IBClient getIBClient() {
		if (IBClient.ibc == null) {
			IBClient.ibAdp = IBAdapter.getAdapter();
			
			IBClient.ibc = new IBClient(IBClient.ibAdp);
		}
		
		return IBClient.ibc;
	}
	
	private IBClient(final EWrapper ew) {
		super(ew);
	}
	
	public synchronized static void connect() {
		if (IBClient.ibc == null || !IBClient.ibc.isConnected()) {
			IBClient.ibc.eConnect(IBClient.host, IBClient.port, IBClient.clientId);
			Logger.log("Connected", IBClient.class);
			
			IBClient.clientId++;
		} else throw new IllegalStateException("Already connected.");
	}
	
	private static final String	broker	= "IB";
	private static final String	account	= "LoggedInAccount";
	
	public synchronized static void notifyDisconnection() {
		ConnectionManager.disconnected(IBClient.broker, IBClient.account);
	}
	
	public synchronized static void disconnect() {
		if (IBClient.ibc != null) IBClient.ibc.eDisconnect();
	}
}
