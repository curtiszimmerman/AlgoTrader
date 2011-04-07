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
	
	@Override
	public void sendEvent(final AbstractSendableEvent event,
	        final Object beanToSend) {
		
		// raw Ticks are always sent using TickService
		if (beanToSend instanceof RawTickVO) {
			
			final TickService tickService = ServiceLocator.commonInstance()
			        .getTickService();
			
			final Tick tick = tickService
			        .completeRawTick((RawTickVO) beanToSend);
			tickService.propagateTick(tick);
			
			// currentTimeEvents are sent to all started strategies
		} else if (beanToSend instanceof CurrentTimeEvent) {
			
			final RuleService ruleService = ServiceLocator.commonInstance()
			        .getRuleService();
			ruleService.setCurrentTime((CurrentTimeEvent) beanToSend);
			
			// everything else (especially Ticks) are sent to the specified
			// runtime
		} else {
			runtime.sendEvent(beanToSend);
		}
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void sendEvent(final AbstractSendableEvent event,
	        final Map mapToSend, final String eventTypeName) {
		runtime.sendEvent(mapToSend, eventTypeName);
	}
	
	@Override
	public void onFinish() {
		// do nothing
	}
}