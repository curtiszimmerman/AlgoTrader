
package TradeApp.Data.copy;

public interface HasSource {
	public <T extends DataSource> void setSource(final T source);
	
	public <T extends DataSource> T getSource();
}
