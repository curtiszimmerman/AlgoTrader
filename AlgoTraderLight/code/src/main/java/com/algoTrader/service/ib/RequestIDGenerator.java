package com.algoTrader.service.ib;

import java.util.ArrayList;
import java.util.List;

public final class RequestIDGenerator {

	private static RequestIDGenerator singleton;
	private int requestId = 1;
	private int orderId = 1;
	private List<Integer> requestsCompleted = new ArrayList<Integer>();

	private RequestIDGenerator() {
		super();
	}

	public static synchronized RequestIDGenerator singleton() {

		if (singleton == null) {
			singleton = new RequestIDGenerator();
		}
		return singleton;
	}

	public int getNextOrderId() {
		return this.orderId++;
	}

	public int getNextRequestId() {
		return this.requestId++;
	}

	public void addToRequestsCompleted(int requestId) {
		this.requestsCompleted.add(Integer.valueOf(requestId));
	}

	public void initializeOrderId(int orderId) {
		this.orderId = orderId;
	}

	public boolean isOrderIdInitialized() {

		return this.orderId != -1;
	}

	public boolean isRequestComplete(int requestId) {

		return this.requestsCompleted.contains(Integer.valueOf(requestId));
	}
}
