/*
 Copyright 2000-2005 University Duisburg-Essen, Working group "Information Systems"

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 this file except in compliance with the License. You may obtain a copy of the
 License at

 http://www.apache.org/licenses/LICENSE-2.0 

 Unless required by applicable law or agreed to in writing, software distributed
 under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 CONDITIONS OF ANY KIND, either express or implied. See the License for the
 specific language governing permissions and limitations under the License. 
 */

// $Id: NormalDistribution.java,v 1.11 2005/03/19 16:13:23 nottelma Exp $
package de.unidu.is.statistics;

import java.lang.reflect.Method;

/**
 * An class modelling a normal (Gaussian) probabilistic distributions.
 * <p>
 * 
 * The normal distribution is defined by the expectation <code>E(x)</code> and
 * the variance <code>E((x^2 - * E(x)^2)</code>.
 * 
 * @author Henrik Nottelmann
 * @since 2002-04-09
 * @version $Revision: 1.11 $, $Date: 2005/03/19 16:13:23 $
 */
public class NormalDistribution extends Distribution {

	/**
	 * Moments of the normal distribution.
	 */
	protected Moments moments;

	/**
	 * Constructs a new object.
	 */
	public NormalDistribution() {
	}

	/**
	 * Constructs a new normal distribution, based on the specified expectation
	 * and variance.
	 * 
	 * @param moments
	 *                   moments of the normal distribution
	 */
	public NormalDistribution(Moments moments) {
		this.moments = moments;
	}

	/**
	 * Constructs a new normal distribution, based on the specified expectation
	 * and variance.
	 * 
	 * @param expectation
	 *                   expectation of the normal distribution
	 * @param variance
	 *                   variance of the normal distribution
	 */
	public NormalDistribution(double expectation, double variance) {
		this.moments = new Moments(expectation,variance);
	}

	/**
	 * The point a such that the integral from -infinity to a equals y.
	 * 
	 * @param y
	 *                   y
	 * @return a
	 */
	public double inverse(double y) {
		try { // is cern package is available, use it
			Class c = Class.forName("cern.jet.stat.Probability");
			Method[] array = c.getMethods();
			Method m = null;
			for (int i = 0; i < array.length; i++)
				if (array[i].getName().equals("normalInverse")) {
					m = array[i];
					break;
				}
			return moments.getExpectation()
					+ moments.getStandardDeviation()
					* ((Double) m.invoke(null, new Object[]{new Double(y)}))
							.doubleValue();
		} catch (Exception e) {
			//			System.out.println(y);
			//			e.printStackTrace();
		}
		// simple numerical computation
		int NUM = 10000;
		double integral = 0;
		for (long i = -10 * NUM; i <= 10 * NUM; i++) {
			double x = (((double) i) + 0.5) / NUM;
			integral += 1
					/ Math.sqrt(2 * Math.PI * moments.getVariance())
					* Math.exp(-Math.pow(x - getExpectation(),2)
							/ (2 * moments.getVariance())) / NUM;
			if (integral >= y)
				return x;
		}
		return 0;
	}

	/**
	 * Computes actual values based on the underlying normal distribution when
	 * having the specified number of items, and writes them in decreasing order
	 * into the specified array (as many values as possible).
	 * 
	 * @param num
	 *                   number of items in total
	 * @param ret
	 *                   array for the actual values
	 */
	public void computeValues(int num, double[] ret) {
		double y = 1 - 0.0001;
		for (int i = 0; i < Math.min(ret.length, num); i++) {
			double yn = 1 - (i + 1) / ((double) num);
			if (yn == 0)
				yn = 0.0001;
			//	    ret[i] = 1.0/2*(f(yn)+f(y)); // average
			ret[i] = ((double) 1)
					/ 8
					* (inverse(yn) + 3 * inverse((2 * yn + y) / 3) + 3
							* inverse((yn + 2 * y) / 3) + inverse(y));
			// 3/8 rule
			//	    ret[i] = f((y+yn)/2); // midpoint
			y = yn;
		}
	}

	/**
	 * Returns the expectation of this normal distribution.
	 * 
	 * @return expectation
	 */
	public double getExpectation() {
		return moments.getExpectation();
	}

	/**
	 * Returns the variance of this normal distribution.
	 * 
	 * @return variance
	 */
	public double getVariance() {
		return moments.getVariance();
	}

	/**
	 * Returns the moments of this normal distribution.
	 * 
	 * @return moments
	 */
	public Moments getMoments() {
		return moments;
	}

	/**
	 * Sets the moments.
	 *
	 * @param moments moments to set.
	 */
	public void setMoments(Moments moments) {
		this.moments = moments;
	}
}