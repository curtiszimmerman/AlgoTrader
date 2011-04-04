
package TradeApp.IB.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import TradeApp.Events.SimpleBar;

public class IBUtils {
	public static <T extends SimpleBar> List<SimpleBar> combineBars(
			final List<T> bars, final int n) {
		if (n < 1)
			throw new IllegalArgumentException(
					"Number of bars to combine must be +ve. Size: " + n);
		
		int i = 0;
		double open = 0, high = 0, low = 0, close = 0;
		double volume = 0;
		Date date = null;
		synchronized (bars) {
			final ArrayList<SimpleBar> combBars = new ArrayList<SimpleBar>();
			for (final T b : bars) {
				final int imod = i % n;
				if (imod == 0) {
					open = b.getOpen();
					high = b.getHigh();
					low = b.getLow();
					volume = b.getVolume();
					date = b.getDateValue();
				} else if (imod == n - 1) {
					close = b.getClose();
					volume += b.getVolume();
					low = b.getLow() < low ? b.getLow() : low;
					high = b.getHigh() > high ? b.getHigh() : high;
					
					combBars.add(new SimpleBar(b.getTicker(), b.getSecs() * n, date,
							open, high, low, close));
				} else {
					volume += b.getVolume();
					low = b.getLow() < low ? b.getLow() : low;
					high = b.getHigh() > high ? b.getHigh() : high;
				}
				
				i++;
			}
			
			return combBars;
		}
	}
}
