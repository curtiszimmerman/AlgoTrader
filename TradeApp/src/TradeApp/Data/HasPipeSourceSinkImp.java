
package TradeApp.Data;

public class HasPipeSourceSinkImp implements HasPipeSourceSink {
	private DataSource	source;
	private DataSink		sink;
	
	@Deprecated
	public HasPipeSourceSinkImp() {}
	
	public HasPipeSourceSinkImp(final DataSource source, final DataSink sink) {
		this.source = source;
		this.sink = sink;
	}
	
	@Override
	public <T extends DataSource> void setSource(final T source) {
		this.source = source;
	}
	
	@Override
	public <T extends DataSource> T getSource() {
		return (T) this.source;
	}
	
	@Override
	public <T extends DataSink> void setSink(final T sink) {
		this.sink = sink;
	}
	
	@Override
	public <T extends DataSink> T getSink() {
		return (T) this.sink;
	}
}
