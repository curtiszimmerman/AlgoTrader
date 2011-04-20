package com.algoTrader.util.io;

import java.util.Map;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.service.MarketDataService;
import com.algoTrader.service.RuleService;
import com.algoTrader.vo.RawTickVO;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esperio.AbstractSendableEvent;
import com.espertech.esperio.AbstractSender;

public class CustomSender extends AbstractSender {

	public void sendEvent(AbstractSendableEvent event, Object beanToSend) {

		// raw Ticks are always sent using MarketDataService
		if (beanToSend instanceof RawTickVO) {

			MarketDataService marketDataService = ServiceLocator.commonInstance().getIbMarketDataService(); // TODO replace with generic Market DataService

			Tick tick = marketDataService.completeRawTick((RawTickVO) beanToSend);
			marketDataService.propagateTick(tick);

			// currentTimeEvents are sent to all started strategies
		} else if (beanToSend instanceof CurrentTimeEvent) {

			RuleService ruleService = ServiceLocator.commonInstance().getRuleService();
			ruleService.setCurrentTime((CurrentTimeEvent) beanToSend);

			// everything else (especially Ticks) are sent to the specified runtime
		} else {
			this.runtime.sendEvent(beanToSend);
		}
	}

	@SuppressWarnings("rawtypes")
	public void sendEvent(AbstractSendableEvent event, Map mapToSend, String eventTypeName) {
		this.runtime.sendEvent(mapToSend, eventTypeName);
	}

	public void onFinish() {
		// do nothing
	}
}