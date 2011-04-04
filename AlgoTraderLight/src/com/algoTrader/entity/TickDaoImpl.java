package com.algoTrader.entity;

import org.hibernate.Hibernate;

import com.algoTrader.vo.RawTickVO;

public class TickDaoImpl extends TickDaoBase {

	public void toRawTickVO(Tick tick, RawTickVO rawTickVO) {

		super.toRawTickVO(tick, rawTickVO);

		completeRawTickVO(tick, rawTickVO);
	}

	public RawTickVO toRawTickVO(final Tick tick) {

		RawTickVO rawTickVO = super.toRawTickVO(tick);

		completeRawTickVO(tick, rawTickVO);

		return rawTickVO;
	}

	private void completeRawTickVO(Tick tick, RawTickVO rawTickVO) {

		rawTickVO.setIsin(tick.getSecurity().getIsin());
	}

	public Tick rawTickVOToEntity(RawTickVO rawTickVO) {
	
		Tick tick = new TickImpl();
		super.rawTickVOToEntity(rawTickVO, tick, true);
	
		Security security = getSecurityDao().findByIsinFetched(rawTickVO.getIsin());
	
		// initialize the proxys
		Hibernate.initialize(security.getUnderlaying());
		Hibernate.initialize(security.getSecurityFamily());
	
		tick.setSecurity(security);
	
		return tick;
	}
}