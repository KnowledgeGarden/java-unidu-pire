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

 
// $Id: DiscreteDistribution.java,v 1.4 2005/02/21 17:29:27 huesselbeck Exp $
package de.unidu.is.statistics;

import java.util.Arrays;

/**
 * An class modelling discrete (empirical) probabilistic distributions.<p>
 *
 * The original values from which the distribution is derived are
 * sorted into bins of equal size. The first bin starts at the minimum
 * value, the last one at the maximum value. This allows for using this
 * distribution for plotting.<p>
 *
 * If the distribution should be used for computing actual values, the
 * original values are used.
 *
 * @author Henrik Nottelmann
 * @since 2002-04-09
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:27 $
 */
public class DiscreteDistribution extends Distribution {

	/**
	 * The x values (bin starting points) of the distribution when
	 * plotted.
	 *
	 */
	protected double[] x;

	/**
	 * The y values (bin frequencies) of the distribution when
	 * plotted.
	 *
	 */
	protected double[] y;

	/**
	 * Number of bins minus 1.
	 *
	 */
	protected static final int parts = 500;

	/**
	 * Original values from which the distribution is derived.
	 *
	 */
	protected double[] values;

	/**
	 * Minimum original value.
	 *
	 */
	protected double min;

	/**
	 * Maximum original value.
	 *
	 */
	protected double max;

	/**
	 * Maximum original value s.th. the integral from 0 to this value if 
	 * less than 0.999.
	 *
	 */
	protected double max2;

	/**
	 * Constructs a new distribution, based on the specified original 
	 * values.<p>
	 *
	 * This constructor also creates the bins. The first bin starts at min,
	 * the last one at max.
	 *
	 * @param values original values from which the distribution is derived
	 */
	public DiscreteDistribution(double[] values) {
		this.values = new double[values.length];
		System.arraycopy(values, 0, this.values, 0, values.length);
		Arrays.sort(this.values);
		min = values[0];
		max = values[0];
		for (int i = 1; i < values.length; i++) {
			min = Math.min(min, values[i]);
			max = Math.max(max, values[i]);
		}
		x = new double[parts + 1];
		y = new double[parts + 1];
		double factor = parts / ((double) (max - min));
		for (int i = 0; i < values.length; i++) {
			int j = (int) ((values[i] - min) * factor);
			y[j] += factor / values.length;
		}
		double sum = 0;
		for (int i = 0; i <= parts; i++) {
			x[i] = min + ((double) i) / ((double) parts) * (max - min);
			sum += y[i] / factor;
			if (sum > 0.999 && max2 == 0)
				max2 = x[i];
		}
	}

	/**
	 * Returns an array with the x values (bin starting points) of the
	 * distribution when plotted.
	 *
	 * @return array with the x values (bin starting points)
	 */
	public double[] getX() {
		return x;
	}

	/**
	 * Returns an array with the y values (bin frequencies) of the
	 * distribution when plotted.
	 *
	 * @return array with the y values (bin frequencies)
	 */
	public double[] getY() {
		return y;
	}

	/**
	 * Returns the minimum original value.
	 *
	 * @return minimum original value
	 */
	public double getMin() {
		return min;
	}

	/**
	 * Returns the maximum original value.
	 *
	 * @return maximum original value
	 */
	public double getMax() {
		return max;
	}

	/**
	 * Eliminates info about the distribution at zero. This is only useful
	 * for discrete distributions with a high peak at zero, as then a
	 * plot might look better.
	 *
	 */
	public void eliminateZero() {
		if (min == 0)
			y[0] = 0;
	}

	/**
	 * Computes actual values based on the underlying distribution when
	 * having the specified number of items, and writes them in decreasing
	 * order into the specified array (as many values as possible).<p>
	 *
	 * This method uses the original values and repeates them
	 * <code>num/values.lentgh</code> times. Thus, <code>num >=
	 * values.lentgh</code> must hold!
	 *
	 * @param num number of items in total
	 * @param ret array for the actual values
	 */
	public void computeValues(int num, double[] ret) {
		int j = 0;
		double ratio = num / ((double) values.length);
		double rest = 0;
		for (int i = values.length - 1; i >= 0; i--) {
			double nn = ratio + rest;
			int n = (int) nn;
			rest = nn - n;
			for (int k = 0; k < n; k++) {
				if (j == ret.length)
					break;
				ret[j++] = values[i];
			}
		}
	}
	
}
