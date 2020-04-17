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

 
// $Id: ArrayDataSource.java,v 1.4 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.gnuplot;

import java.io.File;

/**
 * A class which uses two <code>double</code> arrays for storing the
 * (x,y) data pairs.
 *
 * @author Henrik Nottelmann
 * @since 2002-04-10
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:18 $
 */
public class ArrayDataSource extends DataSource {

	/**
	 * The x values of this curve.
	 *
	 */
	protected double[] x;

	/**
	 * The y values of this curve.
	 *
	 */
	protected double[] y;

	/**
	 * The counter for to the current position when iterating through
	 * the (x,y) data pairs.
	 *
	 */
	protected int count;

	/**
	 * Constructs a new instance, sets the specified values, and sets
	 * the instance variable <code>count</code> to <code>-1</code>. 
	 *
	 * @param title title of this curve, or <code>null</code>
	 * @param file data output file
	 * @param x x values of this curve
	 * @param y y values of this curve
	 */
	public ArrayDataSource(
		String title,
		File file,
		double[] x,
		double[] y) {
		this(title, file.toString(), x, y);
	}

	/**
	 * Constructs a new instance, sets the specified values, and sets
	 * the instance variable <code>count</code> to <code>-1</code>. 
	 *
	 * @param title title of this curve, or <code>null</code>
	 * @param filename name of the data output file
	 * @param x x values of this curve
	 * @param y y values of this curve
	 */
	public ArrayDataSource(
		String title,
		String filename,
		double[] x,
		double[] y) {
		super(title,filename);
		this.x = x;
		this.y = y;
		count = -1;
	}

	/**
	 * Constructs a new instance, sets the specified values, and sets
	 * the instance variable <code>count</code> to <code>-1</code>.<p>
	 *
	 * The x values are created as <code>startx</code>,
	 * <code>startx+1</code>, <code>startx+2</code>, ...,
	 * <code>startx+y.length-1</code>.
	 *
	 * @param title title of this curve, or <code>null</code>
	 * @param filename name of the data output file
	 * @param startx start values of the x values of this curve
	 * @param y y values of this curve
	 */
	public ArrayDataSource(
		String title,
		String filename,
		double startx,
		double[] y) {
		super(title,filename);
		this.x = new double[y.length];
		for (int i = 0; i < x.length; i++)
			x[i] = i + startx;
		this.y = y;
		count = -1;
	}

	/**
	* Switches to the next possible (x,y) data pair if possible.<p>
	*
	* @return true iff another data pair is available
	*/
	public boolean next() {
		count++;
		return count < x.length;
	}

	/**
	 * Returns the x value of the current (x,y) data pair.<p>
	 *
	 * @return x value
	 */
	public double getX() {
		return x[count];
	}

	/**
	 * Returns the y value of the current (x,y) data pair.<p>
	 *
	 * @return y value
	 */
	public double getY() {
		return y[count];
	}

}
