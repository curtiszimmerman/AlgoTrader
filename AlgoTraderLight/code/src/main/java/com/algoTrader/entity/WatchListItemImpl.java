package com.algoTrader.entity;

public class WatchListItemImpl extends WatchListItem {

	private static final long serialVersionUID = -4095123636197045014L;

	public String toString() {

		return getStrategy() + " " + getSecurity();
	}
}
