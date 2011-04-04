package TradeApp.Data.copy;

import java.util.ArrayList;
import java.util.List;

import TradeApp.Util.Logger;

public class SimpleSinkSourceLink implements DataPipeLink, DataSource,
	DataSink,
			Runnable {
	private final ArrayList<TSDataItem>	data	= new ArrayList<TSDataItem>();
	private final int							bufferLength;
	
	public SimpleSinkSourceLink(final int buffLen) {
		bufferLength = buffLen;
	}
	
	public SimpleSinkSourceLink() {
		this(0);
	}
	
	@Override
	public <T extends TSDataItem> void writeOuputElements(final List<T> items) {
		synchronized (data) {
			data.addAll(items);
			data.notify();
		}
	}
	
	@Override
	public <T extends TSDataItem> List<T> readInputElements() {
		synchronized (data) {
			while (data.size() <= bufferLength)
				try {
					data.wait();
				} catch (final InterruptedException e) {
					Logger.log(e);
				}
			
			final ArrayList<T> arr = new ArrayList<T>();
			for (final TSDataItem i : data)
				arr.add((T) i);
			
			data.clear();
			
			return arr;
		}
	}
	
	public int getBufferLength() {
		return bufferLength;
	}
	
	@Override
	public void run() {
		for (;;)
			this.writeOuputElements(this.readInputElements());
	}
}
