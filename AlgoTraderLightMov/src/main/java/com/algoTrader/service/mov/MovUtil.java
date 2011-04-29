package com.algoTrader.service.mov;

import com.algoTrader.entity.Security;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.vo.ExitValueVO;

public class MovUtil {

	private static double stopLoss = ConfigurationUtil.getStrategyConfig("MOV").getDouble("stopLoss");

	public static ExitValueVO getExitValue(String strategyName, Security security, double underlayingSpot) {
	
		return new ExitValueVO(underlayingSpot * stopLoss);
	}

}
