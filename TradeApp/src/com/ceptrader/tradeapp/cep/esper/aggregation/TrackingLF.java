package com.ceptrader.tradeapp.cep.esper.aggregation;

import java.util.Arrays;

import com.espertech.esper.epl.agg.AggregationSupport;
import com.espertech.esper.epl.agg.AggregationValidationContext;

public class TrackingLF extends AggregationSupport {
	private double	value;
	
	@Override
	public void clear() {
		value = 0;
	}
	
	private int	winLen	= 0;
	
	@Override
	public synchronized void enter(final Object arg0) {
		final Object[] p;
		final double curr;
		
		switch (numArg) {
			case 1:
				p = new Object[0];
				curr = (Double) arg0;
				gain = 0;
				target = value;
				break;
			case 2:
				p = (Object[]) arg0;
				curr = (Double) p[0];
				alpha = (Double) p[1];
				gain = 0;
				target = value;
				break;
			case 3:
				p = (Object[]) arg0;
				curr = (Double) p[0];
				alpha = (Double) p[1];
				gain = (Double) p[2];
				target = value;
				break;
			case 4:
				p = (Object[]) arg0;
				curr = (Double) p[0];
				alpha = (Double) p[1];
				gain = (Double) p[2];
				target = (Double) p[3];
				break;
			default:
				curr = 0;
				p = (Object[]) arg0;
		}
		
		double theGain = gain * (curr - target);
		int i = 0;
		for (i = 4; i < p.length; i += 2) {
			final double g = (Double) p[i + 1], t = (Double) p[i];
			theGain += g * (curr - t);
		}
		
		final int n = i / 2;
		i--;
		if (i < p.length) {
			theGain += curr - (Double) p[i];
		}
		
		theGain /= n;
		
		value = alpha * (curr + theGain) + (1 - alpha) * value;
		
		winLen++;
	}
	
	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public Class<Double> getValueType() {
		return double.class;
	}
	
	@Override
	public void leave(final Object arg0) {
		final Object[] p = (Object[]) arg0;
		
		synchronized (this) {
			winLen--;
		}
		
		if (numArg == 1) {
			alpha = winLen / (winLen + 2);
		}
	}
	
	private double	alpha	 = 0;
	private double	gain	 = 0;
	private double	feedback	= 0;
	private double	target	 = 0;
	private int	   numArg;
	
	@Override
	public void validate(final AggregationValidationContext validationContext) {
		try {
			final int numArg = validationContext.getParameterTypes().length;
			
			switch (numArg) {
				case 1:
					break;
				
				case 5:
					if (validationContext.getIsConstantValue()[4]) {
						target = (Double) validationContext.getConstantValues()[4];
					}
					
				case 4:
					if (validationContext.getIsConstantValue()[3]) {
						feedback = (Double) validationContext
						        .getConstantValues()[3];
					}
					
				case 3:
					if (validationContext.getIsConstantValue()[2]) {
						gain = (Double) validationContext.getConstantValues()[2];
					}
					
				case 2:
					if (validationContext.getIsConstantValue()[1]) {
						alpha = (Double) validationContext.getConstantValues()[1];
					}
					break;
				default:
					if (numArg <= 0) { throw new IllegalArgumentException(
					            "Wrong number of arguments: " +
					                    Arrays.toString(validationContext
					                            .getParameterTypes())); }
			}
			
		} catch (final Exception e) {
			throw new IllegalArgumentException(
			        "Cannot run aggregation please check parameters: " +
			                Arrays.toString(validationContext
			                        .getParameterTypes()), e);
		}
	}
	
}
