
package TradeApp.IB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import TradeApp.CEP.CEPMan;
import TradeApp.Data.DataSource;
import TradeApp.Data.TSDataItem;
import TradeApp.Events.SimpleBar;
import TradeApp.IB.Events.HistoricalData;
import TradeApp.IB.Events.RealtimeBar;
import TradeApp.Util.Logger;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

public class HistoricRealTimeBridge implements DataSource {
	private final ArrayList<TSDataItem>	data	= new ArrayList<TSDataItem>();
	private final int							bufferLength;
	
	public HistoricRealTimeBridge(final String hisroricDataClass,
			final String realTimeDataClass, final int bufferLen) {
		this.bufferLength = bufferLen;
		
		final StatementAwareUpdateListener listner = new StatementAwareUpdateListener() {
			@Override
			public synchronized void update(final EventBean[] newEvents,
					final EventBean[] oldEvents, final EPStatement statement,
					final EPServiceProvider epServiceProvider) {
				final EventBean[] eventsArg = newEvents;
				
				if (eventsArg == null) return;
				
				synchronized (HistoricRealTimeBridge.this.data) {
					for (final EventBean eb : eventsArg) {
						final Object obj = eb.getUnderlying();
						if (obj instanceof HistoricalData) {
							final HistoricalData hd = (HistoricalData) obj;
							
							final SimpleBar bar = new SimpleBar(
									IBClient.getReqIdContract(hd.getReqId()).m_symbol,
									IBClient.getReqIdFrequency(hd.getReqId()),
									hd.getDate(), hd.getOpen(), hd.getHigh(),
									hd.getLow(), hd.getClose(), hd.getVolume());
							
							HistoricRealTimeBridge.this.data.add(bar);
						} else {
							final RealtimeBar rb = (RealtimeBar) obj;
							
							final SimpleBar bar = new SimpleBar(
									IBClient.getReqIdContract(rb.getReqId()).m_symbol,
									IBClient.getReqIdFrequency(rb.getReqId()), new Date(
											rb.getTime()), rb.getOpen(), rb.getHigh(),
									rb.getLow(), rb.getClose(), rb.getVolume());
							
							HistoricRealTimeBridge.this.data.add(bar);
						}
					}
				}
			}
		};
		
		CEPMan.getCEPMan().registerStatementListner(
				new String[] { "select * from " + hisroricDataClass },
				new StatementAwareUpdateListener[] { listner });
		
		CEPMan.getCEPMan().registerStatementListner(
				new String[] { "select * from " + realTimeDataClass },
				new StatementAwareUpdateListener[] { listner });
	}
	
	@Override
	public <T extends TSDataItem> List<T> readInputElements() {
		synchronized (this.data) {
			while (this.data.size() <= this.bufferLength)
				try {
					this.data.wait();
				} catch (final InterruptedException e) {
					Logger.log(e);
				}
			
			final ArrayList<T> arr = new ArrayList<T>();
			for (final TSDataItem i : this.data)
				arr.add((T) i);
			
			this.data.clear();
			
			return arr;
		}
	}
}
