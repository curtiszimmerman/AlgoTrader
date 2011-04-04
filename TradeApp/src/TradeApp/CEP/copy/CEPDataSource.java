package TradeApp.CEP.copy;

import java.util.ArrayList;
import java.util.List;

import TradeApp.Data.DataSource;
import TradeApp.Data.TSDataItem;
import TradeApp.Util.Logger;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

public class CEPDataSource implements DataSource {
	private final ArrayList<TSDataItem>	data	= new ArrayList<TSDataItem>();
	final StatementAwareUpdateListener	listner	=
	                                                    new StatementAwareUpdateListener() {
		                                                    @Override
		                                                    public synchronized
		                                                            void
		                                                            update(
		                                                                    final EventBean[] newEvents,
		                                                                    final EventBean[] oldEvents,
		                                                                    final EPStatement statement,
		                                                                    final EPServiceProvider epServiceProvider) {
			                                                    final EventBean[] eventsArg =
			                                                            newEvents;
			                                                    
			                                                    if (eventsArg == null) {
			                                                                return;
			                                                            }
			                                                            
			                                                            synchronized (data) {
				                                                            for (final EventBean eb : eventsArg) {
					                                                            final Object obj =
					                                                                    eb.getUnderlying();
					                                                            
					                                                            data.add((TSDataItem) obj);
					                                                            if (maxBuffer > 0 &&
					                                                                    data.size() > maxBuffer) {
						                                                            data.remove(0);
					                                                            }
				                                                            }
				                                                            
				                                                            data.notify();
			                                                            }
		                                                            }
	                                                    };
	
	private final int	                bufferLength;
	private final int	                maxBuffer;
	
	public CEPDataSource(final int buffLen, final int maxBuffer) {
		bufferLength = buffLen;
		this.maxBuffer = maxBuffer;
	}
	
	public static final int	defaultMaxBuffer	= 30;
	
	public CEPDataSource() {
		this(0, CEPDataSource.defaultMaxBuffer);
	}
	
	public CEPDataSource(final int buffLen, final int maxBuffer,
	        final String stm) {
		this(buffLen, maxBuffer);
		addStatement(stm);
	}
	
	public CEPDataSource(final int buffLen, final String stm) {
		this(buffLen, buffLen + CEPDataSource.defaultMaxBuffer);
		addStatement(stm);
	}
	
	public CEPDataSource(final String stm) {
		this(0, CEPDataSource.defaultMaxBuffer);
		
		addStatement(stm);
	}
	
	public static String buildStatement(final String event,
	        final String whereField,
	        final String whereValue, final boolean isString) {
		final StringBuilder sb = new StringBuilder("Select * From ");
		sb.append(event);
		
		if (whereField != null && whereField.length() > 0) {
			sb.append(" where ").append(whereField).append(" = ");
			
			if (isString) {
				sb.append("\"");
			}
			
			sb.append(whereValue);
			
			if (isString) {
				sb.append("\"");
			}
		}
		
		return sb.toString();
	}
	
	public void addStatement(final String stmt) {
		CEPMan.getCEPMan().registerStatementListner(new String[] {
			    stmt
		}, new StatementAwareUpdateListener[] {
			    listner
		});
	}
	
	@Override
	public <T extends TSDataItem> List<T> readInputElements() {
		synchronized (data) {
			while (data.size() <= bufferLength) {
				try {
					data.wait();
				} catch (final InterruptedException e) {
					Logger.log(e);
				}
			}
			
			final ArrayList<T> arr = new ArrayList<T>();
			for (final TSDataItem i : data) {
				arr.add((T) i);
			}
			
			data.clear();
			
			return arr;
		}
	}
}
