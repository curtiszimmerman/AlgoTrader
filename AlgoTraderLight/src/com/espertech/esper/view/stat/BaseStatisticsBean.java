// add prodX functionality
/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.stat;

import java.io.Serializable;

import com.espertech.esper.client.EPException;

/**
 * Bean for performing statistical calculations. The bean keeps sums of X and Y
 * datapoints and sums on squares that can be reused by subclasses. The bean
 * calculates standard deviation (sample and population), variance, average and
 * sum.
 */
public class BaseStatisticsBean implements Cloneable, Serializable {
	private double sumX;
	private double sumXSq;
	private double prodX = 1;
	private double sumY;
	private double sumYSq;
	private double sumXY;
	private long dataPoints;
	private static final long serialVersionUID = 7985193760056277184L;

	private void initialize() {
		this.sumX = 0;
		this.sumXSq = 0;
		this.prodX = 1;
		this.sumY = 0;
		this.sumYSq = 0;
		this.sumXY = 0;
		this.dataPoints = 0;
	}

	/**
	 * Add a data point for the X data set only.
	 * 
	 * @param x
	 *            is the X data point to add.
	 */
	public final void addPoint(double x) {
		this.dataPoints++;
		this.sumX += x;
		this.sumXSq += x * x;
		this.prodX *= 1 + x;
	}

	/**
	 * Add a data point.
	 * 
	 * @param x
	 *            is the X data point to add.
	 * @param y
	 *            is the Y data point to add.
	 */
	public final void addPoint(double x, double y) {
		this.dataPoints++;
		this.sumX += x;
		this.sumXSq += x * x;
		this.prodX *= 1 + x;
		this.sumY += y;
		this.sumYSq += y * y;
		this.sumXY += x * y;
	}

	/**
	 * Remove a X data point only.
	 * 
	 * @param x
	 *            is the X data point to remove.
	 */
	public final void removePoint(double x) {
		this.dataPoints--;
		if (this.dataPoints <= 0) {
			initialize();
		} else {
			this.sumX -= x;
			this.sumXSq -= x * x;
			this.prodX /= 1 + x;
		}
	}

	/**
	 * Remove a data point.
	 * 
	 * @param x
	 *            is the X data point to remove.
	 * @param y
	 *            is the Y data point to remove.
	 */
	public final void removePoint(double x, double y) {
		this.dataPoints--;
		if (this.dataPoints <= 0) {
			initialize();
		} else {
			this.sumX -= x;
			this.sumXSq -= x * x;
			this.prodX /= 1 + x;
			this.sumY -= y;
			this.sumYSq -= y * y;
			this.sumXY -= x * y;
		}
	}

	/**
	 * Calculates standard deviation for X based on the entire population given
	 * as arguments. Equivalent to Microsoft Excel formula STDEVPA.
	 * 
	 * @return standard deviation assuming population for X
	 */
	public final double getXStandardDeviationPop() {
		if (this.dataPoints == 0) {
			return Double.NaN;
		}

		double temp = (this.sumXSq - this.sumX * this.sumX / this.dataPoints) / this.dataPoints;
		return Math.sqrt(temp);
	}

	/**
	 * Calculates standard deviation for Y based on the entire population given
	 * as arguments. Equivalent to Microsoft Excel formula STDEVPA.
	 * 
	 * @return standard deviation assuming population for Y
	 */
	public final double getYStandardDeviationPop() {
		if (this.dataPoints == 0) {
			return Double.NaN;
		}

		double temp = (this.sumYSq - this.sumY * this.sumY / this.dataPoints) / this.dataPoints;
		return Math.sqrt(temp);
	}

	/**
	 * Calculates standard deviation for X based on the sample data points
	 * supplied. Equivalent to Microsoft Excel formula STDEV.
	 * 
	 * @return standard deviation assuming sample for X
	 */
	public final double getXStandardDeviationSample() {
		if (this.dataPoints < 2) {
			return Double.NaN;
		}

		double variance = getXVariance();
		return Math.sqrt(variance);
	}

	/**
	 * Calculates standard deviation for Y based on the sample data points
	 * supplied. Equivalent to Microsoft Excel formula STDEV.
	 * 
	 * @return standard deviation assuming sample for Y
	 */
	public final double getYStandardDeviationSample() {
		if (this.dataPoints < 2) {
			return Double.NaN;
		}

		double variance = getYVariance();
		return Math.sqrt(variance);
	}

	/**
	 * Calculates standard deviation for X based on the sample data points
	 * supplied. Equivalent to Microsoft Excel formula STDEV.
	 * 
	 * @return variance as the square of the sample standard deviation for X
	 */
	public final double getXVariance() {
		if (this.dataPoints < 2) {
			return Double.NaN;
		}

		return (this.sumXSq - this.sumX * this.sumX / this.dataPoints) / (this.dataPoints - 1);
	}

	/**
	 * Calculates standard deviation for Y based on the sample data points
	 * supplied. Equivalent to Microsoft Excel formula STDEV.
	 * 
	 * @return variance as the square of the sample standard deviation for Y
	 */
	public final double getYVariance() {
		if (this.dataPoints < 2) {
			return Double.NaN;
		}

		return (this.sumYSq - this.sumY * this.sumY / this.dataPoints) / (this.dataPoints - 1);
	}

	/**
	 * Returns the number of data points.
	 * 
	 * @return number of data points
	 */
	public final long getN() {
		return this.dataPoints;
	}

	/**
	 * Returns the sum of all X data points.
	 * 
	 * @return sum of X data points
	 */
	public final double getXSum() {
		return this.sumX;
	}

	/**
	 * Returns the sum of all Y data points.
	 * 
	 * @return sum of Y data points
	 */
	public final double getYSum() {
		return this.sumY;
	}

	/**
	 * Returns the average of all X data points.
	 * 
	 * @return average of X data points
	 */
	public final double getXAverage() {
		if (this.dataPoints == 0) {
			return Double.NaN;
		}

		return this.sumX / this.dataPoints;
	}

	/**
	 * Returns the average of all Y data points.
	 * 
	 * @return average of Y data points
	 */
	public final double getYAverage() {
		if (this.dataPoints == 0) {
			return Double.NaN;
		}

		return this.sumY / this.dataPoints;
	}

	/**
	 * For use by subclasses, returns sum (X * X).
	 * 
	 * @return sum of X squared
	 */
	public final double getSumXSq() {
		return this.sumXSq;
	}

	/**
	 * For use by subclasses, returns sum (Y * Y).
	 * 
	 * @return sum of Y squared
	 */
	public final double getSumYSq() {
		return this.sumYSq;
	}

	/**
	 * For use by subclasses, returns sum (X * Y).
	 * 
	 * @return sum of X times Y
	 */
	public final double getSumXY() {
		return this.sumXY;
	}

	public final double getProdX() {
		return Math.pow(this.prodX, (1.0 / this.dataPoints)) - 1.0;
	}

	public final Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new EPException(e);
		}
	}

	public final String toString() {
		return "datapoints=" + this.dataPoints + "  sumX=" + this.sumX + "  sumXSq=" + this.sumXSq + "  sumY=" + this.sumY + "  sumYSq=" + this.sumYSq + "  sumXY=" + this.sumXY;
	}

	/**
	 * Sets the sum X.
	 * 
	 * @param sumX
	 *            to set
	 */
	public void setSumX(double sumX) {
		this.sumX = sumX;
	}

	/**
	 * Sets the sum X square.
	 * 
	 * @param sumXSq
	 *            to set
	 */
	public void setSumXSq(double sumXSq) {
		this.sumXSq = sumXSq;
	}

	public void setProdX(double prodX) {
		this.prodX = prodX;
	}

	/**
	 * Sets the sum Y.
	 * 
	 * @param sumY
	 *            to set
	 */
	public void setSumY(double sumY) {
		this.sumY = sumY;
	}

	/**
	 * Sets the sum Y square.
	 * 
	 * @param sumYSq
	 *            to set
	 */
	public void setSumYSq(double sumYSq) {
		this.sumYSq = sumYSq;
	}

	/**
	 * Sets the sum of x times y.
	 * 
	 * @param sumXY
	 *            sum of x times y.
	 */
	public void setSumXY(double sumXY) {
		this.sumXY = sumXY;
	}

	/**
	 * Sets the number of datapoints
	 * 
	 * @param dataPoints
	 *            to set
	 */
	public void setDataPoints(long dataPoints) {
		this.dataPoints = dataPoints;
	}

	/**
	 * Returns sum of x.
	 * 
	 * @return sum of x
	 */
	public double getSumX() {
		return this.sumX;
	}

	/**
	 * Returns sum of y.
	 * 
	 * @return sum of y
	 */
	public double getSumY() {
		return this.sumY;
	}

    /**
	 * Returns the number of datapoints.
	 * 
	 * @return datapoints
	 */
	public long getDataPoints() {
		return this.dataPoints;
	}

	/**
	 * Returns the Y intercept.
	 * 
	 * @return Y intercept
	 */
	public double getYIntercept() {
		double slope = getSlope();

		if (Double.isNaN(slope)) {
			return Double.NaN;
		}

		return getYSum() / getN() - getSlope() * getXSum() / getN();
	}

	/**
	 * Returns the slope.
	 * 
	 * @return regression slope
	 */
	public double getSlope() {
		if (this.getN() == 0) {
			return Double.NaN;
		}

		double ssx = getSumXSq() - getXSum() * getXSum() / getN();

		if (ssx == 0) {
			return Double.NaN;
		}

		double sp = getSumXY() - this.getXSum() * this.getYSum() / getN();

		return sp / ssx;
	}

	/**
	 * Return the correlation value for the two data series (Microsoft Excel
	 * function CORREL).
	 * 
	 * @return correlation value
	 */
	public final double getCorrelation() {
		if (this.getN() == 0) {
			return Double.NaN;
		}

		double dx = this.getSumXSq() - (this.getXSum() * this.getXSum()) / this.getN();
		double dy = this.getSumYSq() - (this.getYSum() * this.getYSum()) / this.getN();

		if (dx == 0 || dy == 0) {
			return Double.NaN;
		}

		double sp = this.getSumXY() - this.getXSum() * this.getYSum() / this.getN();
		return sp / Math.sqrt(dx * dy);
	}
}
