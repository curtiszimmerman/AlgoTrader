package com.algoTrader.util;

import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;

import com.algoTrader.entity.trade.LimitOrder;
import com.algoTrader.entity.trade.Order;

/**
 * provides order modification methods, which will create a clone of the order first (because Esper Events should not be modified)
 * @author aflury
 *
 */
public class OrderUtil {

	public static LimitOrder modifyOrderLimit(LimitOrder order, BigDecimal limit) {

		try {
			LimitOrder newOrder = (LimitOrder) BeanUtils.cloneBean(order);
			newOrder.setLimit(limit);
			return newOrder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Order modifyOrderQuantity(Order order, long quantity) {

		try {
			Order newOrder = (Order) BeanUtils.cloneBean(order);
			newOrder.setQuantity(quantity);
			return newOrder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
