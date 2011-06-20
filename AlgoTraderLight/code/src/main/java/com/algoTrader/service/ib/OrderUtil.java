package com.algoTrader.service.ib;

import java.math.BigDecimal;
import java.util.Date;

import com.algoTrader.entity.trade.Fill;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.RoundUtil;
import com.algoTrader.vo.ib.ExecDetails;
import com.algoTrader.vo.ib.OpenOrder;

public class OrderUtil {

	public static Fill getFill(Order order, OpenOrder openOrder, ExecDetails details) {

		Date dateTime = DateUtil.getCurrentEPTime();
		Side side = Side.fromValue(openOrder.getOrder().m_action);
		int quantity = details.getExecution().m_shares;
		BigDecimal price = RoundUtil.getBigDecimal(details.getExecution().m_price);
		BigDecimal commission = new BigDecimal("0"); // TODO get from OrderStatus

		Fill fill = Fill.Factory.newInstance();
		fill.setDateTime(dateTime);
		fill.setSide(side);
		fill.setQuantity(quantity);
		fill.setPrice(price);
		fill.setCommission(commission);
		fill.setOrder(order);
		return fill;
	}
}
