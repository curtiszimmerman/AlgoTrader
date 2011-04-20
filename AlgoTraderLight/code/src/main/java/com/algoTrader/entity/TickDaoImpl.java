package com.algoTrader.entity;

import org.hibernate.Hibernate;

import com.algoTrader.vo.RawTickVO;

public class TickDaoImpl extends TickDaoBase {
	
	@Override
	public void toRawTickVO(final Tick tick, final RawTickVO rawTickVO) {
		
		super.toRawTickVO(tick, rawTickVO);
		
		completeRawTickVO(tick, rawTickVO);
	}
	
	@Override
	public RawTickVO toRawTickVO(final Tick tick) {
		
		final RawTickVO rawTickVO = super.toRawTickVO(tick);
		
		completeRawTickVO(tick, rawTickVO);
		
		return rawTickVO;
	}
	
	private void completeRawTickVO(final Tick tick, final RawTickVO rawTickVO) {
		
		rawTickVO.setIsin(tick.getSecurity().getIsin());
	}
	
	@Override
	public Tick rawTickVOToEntity(final RawTickVO rawTickVO) {
		
		final Tick tick = new TickImpl();
		super.rawTickVOToEntity(rawTickVO, tick, true);
		
		final Security security = getSecurityDao().findByIsinFetched(
		        rawTickVO.getIsin());
		
		// initialize the proxys
		Hibernate.initialize(security.getUnderlaying());
		Hibernate.initialize(security.getSecurityFamily());
		
		tick.setSecurity(security);
		
		return tick;
	}
}