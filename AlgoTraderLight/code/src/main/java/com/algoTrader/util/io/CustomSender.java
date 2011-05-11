package com.algoTrader.util.io;

import java.util.Map;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.marketData.Bar;
import com.algoTrader.service.MarketDataService;
import com.algoTrader.service.RuleService;
import com.algoTrader.vo.RawTickVO;
import com.algoTrader.vo.BarVO;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esperio.AbstractSendableEvent;
import com.espertech.esperio.AbstractSender;

public class CustomSender extends AbstractSender {

	public void sendEvent(AbstractSendableEvent event, Object beanToSend) {

		// raw Ticks are always sent using MarketDataService
		if (beanToSend instanceof RawTickVO) {

			MarketDataService marketDataService = ServiceLocator.commonInstance().getMarketDataService();

			Tick tick = marketDataService.completeRawTick((RawTickVO) beanToSend);
			marketDataService.propagateMarketDataEvent(tick);

		// Bars are always sent using MarketDataService
		} else if (beanToSend instanceof BarVO) {

				MarketDataService marketDataService = ServiceLocator.commonInstance().getMarketDataService();

				Bar bar = marketDataService.completeBar((BarVO) beanToSend);
				marketDataService.propagateMarketDataEvent(bar);

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