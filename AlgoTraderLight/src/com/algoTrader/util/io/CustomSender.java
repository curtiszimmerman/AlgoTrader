package com.algoTrader.util.io;

import java.util.Map;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Tick;
import com.algoTrader.service.RuleService;
import com.algoTrader.service.TickService;
import com.algoTrader.vo.RawTickVO;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esperio.AbstractSendableEvent;
import com.espertech.esperio.AbstractSender;

public class CustomSender extends AbstractSender {

	public void sendEvent(AbstractSendableEvent event, Object beanToSend) {

		// raw Ticks are always sent using TickService
		if (beanToSend instanceof RawTickVO) {

			TickService tickService = ServiceLocator.commonInstance().getTickService();

			Tick tick = tickService.completeRawTick((RawTickVO) beanToSend);
			tickService.propagateTick(tick);

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