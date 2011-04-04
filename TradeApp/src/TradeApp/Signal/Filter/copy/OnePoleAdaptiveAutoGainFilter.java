package TradeApp.Signal.Filter.copy;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.regression.SimpleRegression;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.AbstractSignal;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Signal;
import TradeApp.Signal.Events.OnePoleAdaptiveAutoGainFilterValue;
import TradeApp.Signal.Events.SignalValue;

public class OnePoleAdaptiveAutoGainFilter<T extends TSDataItem> extends
		AbstractSignal implements Computable, Signal {
	protected double								alpha;
	protected double								val;
	protected double								lagNoGainVal;
	protected double								altVal;
	protected final LinkedList<TSDataItem>	regValTmp	= new LinkedList<TSDataItem>();
	protected final LinkedList<Double>		rtnTmp		= new LinkedList<Double>();
	protected TSDataItem							sv;
	
	public OnePoleAdaptiveAutoGainFilter(final String ticker,
			final int inputSize, final int outputSize) {
		super(ticker, inputSize, outputSize);
		
		final int len = 30;
		
		this.alpha = 2 / (len + 1);
		final TSDataItem TSItem = new TSDataItem() {
			
			@Override
			public double getTypicalValue() {
				return 0;
			}
			
			@Override
			public double getTypicalValue(final int i) {
				return 0;
			}
			
			@Override
			public void setDate(final String dateTime) {
			}
			
			@Override
			public String getDate() {
				return null;
			}
			
			@Override
			public Date getDateValue() {
				return null;
			}
			
			@Override
			public int[] getRange() {
				return new int[] { 0, 0 };
			}
			
			@Override
			public double getTypicalValue(final String name) {
				return 0;
			}
			
			@Override
			public String[] getNames() {
				return null;
			}
		};
		
		this.sv = TSItem;
		for (int i = 0; i < 2 * len; i++) {
			this.regValTmp.add(TSItem);
			this.rtnTmp.add(0.0);
		}
	}
	
	public static final long				milliSecPerDay	= 1000 * 60 * 60 * 24;
	protected final SimpleRegression		sr					= new SimpleRegression();
	protected final StandardDeviation	sd					= new StandardDeviation();
	protected Mean								mean				= new Mean();
	
	protected int								startIdx			= 0;
	
	protected void doAdj() {
		final T deQueued = (T) this.regValTmp.get(this.startIdx);
		
		final double daysDeQueued = deQueued.getDateValue().getTime()
				/ OnePoleAdaptiveAutoGainFilter.milliSecPerDay;
		final double lnPLg = Math.log(deQueued.getTypicalValue());
		this.sr.removeData(lnPLg, daysDeQueued);
	}
	
	@Override
	synchronized public void compute() {
		for (final TSDataItem ele : this.readInputElements()) {
			final T dat = (T) ele;
			
			final double dayVal = dat.getDateValue().getTime()
					/ OnePoleAdaptiveAutoGainFilter.milliSecPerDay;
			final double lnP = Math.log(dat.getTypicalValue());
			this.sr.addData(lnP, dayVal);
			
			this.doAdj();
			
			final double lnPLst = Math.log(this.regValTmp.peekLast()
					.getTypicalValue());
			double currRtn = lnP - lnPLst;
			currRtn = Double.isNaN(currRtn) ? 0 : currRtn;
			
			this.rtnTmp.add(currRtn);
			this.rtnTmp.pollFirst();
			
			final SignalValue sv_rtn = new SignalValue(this.getName(), "currRtn",
					dat.getDate(), currRtn);
			
			final double[] rtns = new double[this.rtnTmp.size()];
			
			int i = 0;
			for (final double r : this.rtnTmp)
				rtns[i++] = r;
			
			final double sdVal = this.sd.evaluate(rtns);
			final double theMean = this.mean.evaluate(rtns);
			
			final double slope = this.sr.getSlope();
			final double rSqr = this.sr.getRSquare();
			
			final SignalValue sv_sdVal = new SignalValue(this.getName(), "sdVal",
					dat.getDate(), sdVal);
			final SignalValue sv_theMean = new SignalValue(this.getName(),
					"theMean", dat.getDate(), theMean);
			final SignalValue sv_slope = new SignalValue(this.getName(), "slope",
					dat.getDate(), slope);
			final SignalValue sv_rSqr = new SignalValue(this.getName(), "rSqr",
					dat.getDate(), rSqr);
			
			final double sqrtError = Math.sqrt(this.sr.getMeanSquareError());
			final double regEstRtn = slope * (1 - rSqr) + sqrtError / 2;
			
			final SignalValue sv_sqEr = new SignalValue(this.getName(),
					"sqrtError", dat.getDate(), sqrtError);
			final SignalValue sv_regRtn = new SignalValue(this.getName(),
					"regEstRtn", dat.getDate(), regEstRtn);
			
			final double len = 1 / regEstRtn;
			this.alpha = 2 / (len + 1);
			
			final SignalValue sv_len = new SignalValue(this.getName(), "len",
					dat.getDate(), len);
			final SignalValue sv_alpha = new SignalValue(this.getName(), "alpha",
					dat.getDate(), this.alpha);
			
			final double gain1 = Math.tan(Math.abs(Math.atan(slope * len)
					- Math.atan(currRtn * len)))
					* rSqr;
			
			final SignalValue sv_gain1 = new SignalValue(this.getName(), "gain1",
					dat.getDate(), gain1);
			
			final double gain2 = Math.tan(0.5 * Math.atan(sdVal)
					+ Math.atan(theMean));
			
			final SignalValue sv_gain2 = new SignalValue(this.getName(), "gain2",
					dat.getDate(), gain2);
			
			final double regVal = this.sr.predict(dayVal);
			
			final SignalValue sv_regVal = new SignalValue(this.getName(),
					"regVal", dat.getDate(), regVal);
			
			final double prevVal = Math.log(this.sv.getTypicalValue());
			final double currLnP = Math.log(dat.getTypicalValue());
			
			this.val = this.alpha
					* (currLnP + 0.5 * gain1 * (regVal - prevVal) + 0.5 * gain2
							* (this.lagNoGainVal - prevVal)) + (1 - this.alpha)
					* prevVal;
			
			final SignalValue svLnVal = new SignalValue(this.getName(), "Ln",
					dat.getDate(), this.val);
			
			this.lagNoGainVal = this.alpha * currLnP + (1 - this.alpha)
					* this.lagNoGainVal;
			
			final SignalValue svLn_lagNoGainVal = new SignalValue(this.getName(),
					"ln_lagNoGainVal", dat.getDate(), this.lagNoGainVal);
			
			final SignalValue sv_lagNoGainVal = new SignalValue(this.getName(),
					"lagNoGainVal", dat.getDate(), Math.exp(this.lagNoGainVal));
			
			this.altVal = 1 / 3 * (regVal * rSqr + currLnP
					* (1 - rSqr + 2 * this.alpha) + (1 - this.alpha)
					* (this.altVal + this.lagNoGainVal));
			
			final SignalValue svLn_altVal = new SignalValue(this.getName(),
					"ln_altVal", dat.getDate(), this.altVal);
			
			final SignalValue sv_altVal = new SignalValue(this.getName(),
					"altVal", dat.getDate(), Math.exp(this.altVal));
			
			genValue(dat, sv_rtn, sv_sdVal, sv_theMean, sv_slope, sv_rSqr,
				sv_sqEr, sv_regRtn, sv_len, sv_alpha, sv_gain1, sv_gain2,
				sv_regVal, svLnVal, svLn_altVal);
			
			this.regValTmp.add(dat);
			this.regValTmp.pollFirst();
			
			this.writeOuputElements(Collections.singletonList(this.sv));
		}
	}
	
	protected void genValue(final T dat, final SignalValue sv_rtn,
		final SignalValue sv_sdVal, final SignalValue sv_theMean,
		final SignalValue sv_slope, final SignalValue sv_rSqr,
		final SignalValue sv_sqEr, final SignalValue sv_regRtn,
		final SignalValue sv_len, final SignalValue sv_alpha,
		final SignalValue sv_gain1, final SignalValue sv_gain2,
		final SignalValue sv_regVal, final SignalValue svLnVal,
		final SignalValue svLn_altVal) {
		this.sv = new OnePoleAdaptiveAutoGainFilterValue(this.getName(),
			dat.getDate(),
				Math.exp(this.val), svLnVal, svLn_altVal, svLn_altVal, sv_gain1,
				sv_gain2, sv_regVal, sv_alpha, sv_len, sv_sqEr, sv_regRtn,
				sv_rSqr, sv_slope, sv_theMean, sv_sdVal, sv_rtn);
	}
}
