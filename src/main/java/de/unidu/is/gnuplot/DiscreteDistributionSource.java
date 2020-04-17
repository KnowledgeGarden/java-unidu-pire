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

 
// $Id: DiscreteDistributionSource.java,v 1.6 2005/03/14 17:33:15 nottelma Exp $
package de.unidu.is.gnuplot;

import java.io.File;

import de.unidu.is.statistics.DiscreteDistribution;

/**
 * A class for modelling a discrete distribution as source for Gnuplot.
 * 
 * @author Henrik Nottelmann
 * @since 2003-07-27
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:15 $
 */
public class DiscreteDistributionSource extends ArrayDataSource {

	/**
	 * Creates a new instance.
	 * 
	 * @param title title of this curve, or <code>null</code>
	 * @param file data output file
	 * @param distribution discrete distribution to be used
	 */
	public DiscreteDistributionSource(
		String title,
		File file,
		DiscreteDistribution distribution) {
		this(title, file.toString(), distribution);
	}

	/**
	 * Constructs a new instance. 
	 *
	 * @param title title of this curve, or <code>null</code>
	 * @param filename name of the data output file
	 * @param distribution discrete distribution to be used
	 */
	public DiscreteDistributionSource(
		String title,
		String filename,
		DiscreteDistribution distribution) {
		super(
			title,
			filename,
			getDistributionX(distribution.getX()),
			getDistributionY(distribution.getY()));
		setMin(distribution.getMin());
		setMax(distribution.getMax());
	}

	/**
	 * Returns the x values of the distribution for plotting with Gnuplot, 
	 * based on the specified sample x values of the underlying distribution.
	 * 
	 * @param x sample x values of the underlying distribution
	 * @return x values of the distribution for plotting with Gnuplot
	 */
	private static double[] getDistributionX(double[] x) {
		double E = 0.0000001;
		double[] xx = new double[x.length * 2];
		for (int i = 0; i < x.length; i++) {
			xx[2 * i] = x[i];
			if (i < x.length - 1)
				xx[2 * i + 1] = x[i + 1] - E;
			else
				xx[2 * i + 1] = x[i] + E;
		}
		return xx;
	}

	/**
	 * Returns the y values of the distribution for plotting with Gnuplot, 
	 * based on the specified sample y values of the underlying distribution.
	 * 
	 * @param y sample y values of the underlying distribution
	 * @return y values of the distribution for plotting with Gnuplot
	 */
	private static double[] getDistributionY(double[] y) {
		double[] yy = new double[y.length * 2];
		for (int i = 0; i < y.length; i++) {
			yy[2 * i] = y[i];
			yy[2 * i + 1] = y[i];
		}
		return yy;
	}

}
