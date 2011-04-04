
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class IBErrors {
	public static class ECode implements IBErr {
		private int		errorCode;
		
		private String	errorMsg;
		private int		id;
		
		@Deprecated
		public ECode() {}
		
		public ECode(final int id, final int errorCode, final String errorMsg) {
			this.id = id;
			this.errorCode = errorCode;
			this.errorMsg = errorMsg;
		}
		
		public int getErrorCode() {
			return this.errorCode;
		}
		
		public String getErrorMsg() {
			return this.errorMsg;
		}
		
		public int getId() {
			return this.id;
		}
		
		public void setErrorCode(final int errorCode) {
			this.errorCode = errorCode;
		}
		
		public void setErrorMsg(final String errorMsg) {
			this.errorMsg = errorMsg;
		}
		
		public void setId(final int id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return BasicUtils.toString(this);
		}
	}
	
	public static class EExp implements IBErr {
		private Exception	e;
		
		@Deprecated
		public EExp() {}
		
		public EExp(final Exception e) {
			this.e = e;
		}
		
		public Exception getE() {
			return this.e;
		}
		
		public void setE(final Exception e) {
			this.e = e;
		}
		
		@Override
		public String toString() {
			return BasicUtils.toString(this);
		}
	}
	
	public static class EStr implements IBErr {
		private String	str;
		
		@Deprecated
		public EStr() {}
		
		public EStr(final String str) {
			this.str = str;
		}
		
		public String getStr() {
			return this.str;
		}
		
		public void setStr(final String str) {
			this.str = str;
		}
		
		@Override
		public String toString() {
			return BasicUtils.toString(this);
		}
	}
	
	public static interface IBErr extends DataItem {}
	
	private IBErr	value;
	
	@Deprecated
	public IBErrors() {}
	
	public IBErrors(final IBErr value) {
		this.value = value;
	}
	
	public IBErr getValue() {
		return this.value;
	}
	
	public <T extends IBErr> T getValueAsType() {
		return (T) this.value;
	}
	
	public void setValue(final IBErr value) {
		if (value instanceof EExp || value instanceof EStr
				|| value instanceof ECode) this.value = value;
		else throw new IllegalArgumentException(
				"The argument should be one of the inner types.");
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
