
package TradeApp.Data;

import java.util.List;

public class DataFlowLink {
	private final DataSource	source;
	private final List<Pipe>	pipes;
	private final DataSink		sink;
	
	public DataFlowLink(final DataSource source, final List<Pipe> pipes,
			final DataSink sink) {
		
		this.source = source;
		this.pipes = pipes;
		this.sink = sink;
		
		DataSource tmp = source;
		
		if (pipes == null || pipes.isEmpty())
			throw new IllegalStateException("Empty list of pipes");
		
		Pipe p_ = null;
		for (final Pipe p : pipes) {
			final SimpleSinkSourceLink ssl = new SimpleSinkSourceLink(0);
			p.setSource(tmp);
			p.setSink(ssl);
			tmp = ssl;
			p_ = p;
		}
		
		p_.setSink(sink);
	}

	public DataSource getSource() {
		return source;
	}

	public List<Pipe> getPipes() {
		return pipes;
	}

	public DataSink getSink() {
		return sink;
	}
}
