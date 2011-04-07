package com.ib.client;

public class ScannerSubscription {
	public final static int	NO_ROW_NUMBER_SPECIFIED	   = -1;
	
	private int	            m_numberOfRows	           = ScannerSubscription.NO_ROW_NUMBER_SPECIFIED;
	private String	        m_instrument;
	private String	        m_locationCode;
	private String	        m_scanCode;
	private double	        m_abovePrice	           = Double.MAX_VALUE;
	private double	        m_belowPrice	           = Double.MAX_VALUE;
	private int	            m_aboveVolume	           = Integer.MAX_VALUE;
	private int	            m_averageOptionVolumeAbove	= Integer.MAX_VALUE;
	private double	        m_marketCapAbove	       = Double.MAX_VALUE;
	private double	        m_marketCapBelow	       = Double.MAX_VALUE;
	private String	        m_moodyRatingAbove;
	private String	        m_moodyRatingBelow;
	private String	        m_spRatingAbove;
	private String	        m_spRatingBelow;
	private String	        m_maturityDateAbove;
	private String	        m_maturityDateBelow;
	private double	        m_couponRateAbove	       = Double.MAX_VALUE;
	private double	        m_couponRateBelow	       = Double.MAX_VALUE;
	private String	        m_excludeConvertible;
	private String	        m_scannerSettingPairs;
	private String	        m_stockTypeFilter;
	
	// Get
	public int numberOfRows() {
		return m_numberOfRows;
	}
	
	public String instrument() {
		return m_instrument;
	}
	
	public String locationCode() {
		return m_locationCode;
	}
	
	public String scanCode() {
		return m_scanCode;
	}
	
	public double abovePrice() {
		return m_abovePrice;
	}
	
	public double belowPrice() {
		return m_belowPrice;
	}
	
	public int aboveVolume() {
		return m_aboveVolume;
	}
	
	public int averageOptionVolumeAbove() {
		return m_averageOptionVolumeAbove;
	}
	
	public double marketCapAbove() {
		return m_marketCapAbove;
	}
	
	public double marketCapBelow() {
		return m_marketCapBelow;
	}
	
	public String moodyRatingAbove() {
		return m_moodyRatingAbove;
	}
	
	public String moodyRatingBelow() {
		return m_moodyRatingBelow;
	}
	
	public String spRatingAbove() {
		return m_spRatingAbove;
	}
	
	public String spRatingBelow() {
		return m_spRatingBelow;
	}
	
	public String maturityDateAbove() {
		return m_maturityDateAbove;
	}
	
	public String maturityDateBelow() {
		return m_maturityDateBelow;
	}
	
	public double couponRateAbove() {
		return m_couponRateAbove;
	}
	
	public double couponRateBelow() {
		return m_couponRateBelow;
	}
	
	public String excludeConvertible() {
		return m_excludeConvertible;
	}
	
	public String scannerSettingPairs() {
		return m_scannerSettingPairs;
	}
	
	public String stockTypeFilter() {
		return m_stockTypeFilter;
	}
	
	// Set
	public void numberOfRows(final int num) {
		m_numberOfRows = num;
	}
	
	public void instrument(final String txt) {
		m_instrument = txt;
	}
	
	public void locationCode(final String txt) {
		m_locationCode = txt;
	}
	
	public void scanCode(final String txt) {
		m_scanCode = txt;
	}
	
	public void abovePrice(final double price) {
		m_abovePrice = price;
	}
	
	public void belowPrice(final double price) {
		m_belowPrice = price;
	}
	
	public void aboveVolume(final int volume) {
		m_aboveVolume = volume;
	}
	
	public void averageOptionVolumeAbove(final int volume) {
		m_averageOptionVolumeAbove = volume;
	}
	
	public void marketCapAbove(final double cap) {
		m_marketCapAbove = cap;
	}
	
	public void marketCapBelow(final double cap) {
		m_marketCapBelow = cap;
	}
	
	public void moodyRatingAbove(final String r) {
		m_moodyRatingAbove = r;
	}
	
	public void moodyRatingBelow(final String r) {
		m_moodyRatingBelow = r;
	}
	
	public void spRatingAbove(final String r) {
		m_spRatingAbove = r;
	}
	
	public void spRatingBelow(final String r) {
		m_spRatingBelow = r;
	}
	
	public void maturityDateAbove(final String d) {
		m_maturityDateAbove = d;
	}
	
	public void maturityDateBelow(final String d) {
		m_maturityDateBelow = d;
	}
	
	public void couponRateAbove(final double r) {
		m_couponRateAbove = r;
	}
	
	public void couponRateBelow(final double r) {
		m_couponRateBelow = r;
	}
	
	public void excludeConvertible(final String c) {
		m_excludeConvertible = c;
	}
	
	public void scannerSettingPairs(final String val) {
		m_scannerSettingPairs = val;
	}
	
	public void stockTypeFilter(final String val) {
		m_stockTypeFilter = val;
	}
}