package TradeApp.Data.copy;

public interface CRUDDataListner<T extends DataItem, K extends DataItem>
	extends DataListner<T> {
	void dataUpdate(T oldDataItem,
		K newDataItem);
	
	void dataRemove(T dataItem);
	
	T get(int i);
}
