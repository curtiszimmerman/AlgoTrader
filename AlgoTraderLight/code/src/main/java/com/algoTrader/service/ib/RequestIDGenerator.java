package com.algoTrader.service.ib;

import java.util.ArrayList;
import java.util.List;

public class RequestIDGenerator {

    private static RequestIDGenerator singleton = null;
    private int requestId = 1;
    private int orderId = 1;
    private List<Integer> requestsCompleted = new ArrayList<Integer>();

    private RequestIDGenerator() {
    	super();
    }

    public synchronized static RequestIDGenerator singleton() {
    	
    	if (singleton == null) {
		    singleton = new RequestIDGenerator();
    	}
        return singleton;
    }

    public int getNextOrderId() {
        return orderId++;
    }

    public int getNextRequestId() {
        return requestId++;
    }

    public void addToRequestsCompleted(int requestId) {
        requestsCompleted.add(Integer.valueOf(requestId));
    }

    public void initializeOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isOrderIdInitialized() {
        if (orderId == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isRequestComplete(int requestId) {
        if (requestsCompleted.contains(Integer.valueOf(requestId))) {
            return true;
        } else {
            return false;
        }
    }
}
