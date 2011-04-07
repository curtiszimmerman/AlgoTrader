package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.esper.generic.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class IBErrors implements DataItem {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	
	public static class ECode implements IBErr {
		private static final long	serialVersionUID	= 1L;
		private int		          errorCode;
		
		private String		      errorMsg;
		private int		          id;
		
		@Deprecated
		public ECode() {
		}
		
		public ECode(final int id, final int errorCode, final String errorMsg) {
			this.id = id;
			this.errorCode = errorCode;
			this.errorMsg = errorMsg;
		}
		
		public int getErrorCode() {
			return errorCode;
		}
		
		public String getErrorMsg() {
			return errorMsg;
		}
		
		public int getId() {
			return id;
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
		/**
         * 
         */
		private static final long	serialVersionUID	= 1L;
		private Exception		  e;
		
		@Deprecated
		public EExp() {
		}
		
		public EExp(final Exception e) {
			this.e = e;
		}
		
		public Exception getE() {
			return e;
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
		/**
         * 
         */
		private static final long	serialVersionUID	= 1L;
		private String		      str;
		
		@Deprecated
		public EStr() {
		}
		
		public EStr(final String str) {
			this.str = str;
		}
		
		public String getStr() {
			return str;
		}
		
		public void setStr(final String str) {
			this.str = str;
		}
		
		@Override
		public String toString() {
			return BasicUtils.toString(this);
		}
	}
	
	public static interface IBErr extends DataItem {
	}
	
	private IBErr	value;
	
	@Deprecated
	public IBErrors() {
	}
	
	public IBErrors(final IBErr value) {
		this.value = value;
	}
	
	public IBErr getValue() {
		return value;
	}
	
	public <T extends IBErr> T getValueAsType() {
		return (T) value;
	}
	
	public void setValue(final IBErr value) {
		if (value instanceof EExp || value instanceof EStr
		        || value instanceof ECode) {
			this.value = value;
		} else {
			throw new IllegalArgumentException(
			        "The argument should be one of the inner types.");
		}
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
