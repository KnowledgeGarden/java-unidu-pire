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

 
// $Id: DataSource.java,v 1.4 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.gnuplot;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

import de.unidu.is.util.Log;

/**
 * An abstract class describing a single plot curve which is defined by data
 * points (instead of a function).<p>
 *
 * For using this class, the name of the data output file has to be specified.
 * For constructing the data file inside of <code>getCommand()</code>, the
 * program iterates through (x,y) data pairs with <code>next()</code> together
 * with <code>getX()</code> and <code>getY()</code>:<p>
 *
 * <pre>
 *   while(next()) {
 *       double x = getX(); // current x
 *       double y = getY(); // current y
 *       doSomething(x,y);
 *   }
 * </pre>
 * Thus, one instance can only be used once for iterating.<p>
 *
 * This class has to be subclassed for specific application areas.
 *
 * @author Henrik Nottelmann
 * @since 2002-04-10
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:18 $
 */
public abstract class DataSource extends FileSource {

	/**
	 * Creates a new instance.
	 * 
	 * @param filename name of the corresponding file with data points
	 */
	public DataSource(String title,String filename) {
		super(title,filename);
	}

	/**
	 * Writes the data in the witer.
	 * 
	 * @param writer writer to which the file is written to
	 */
	public void write(Writer writer) {
		try {
			PrintWriter pw = new PrintWriter(writer);
			while (next())
				pw.println(getX() + " " + getY());
			pw.close();
		} catch (Exception ex) {
			Log.error(ex);
		}
	}

	/**
	 * Writes the data in the file.
	 * 
	 */
	public void write() {
		try {
			write(new FileWriter(filename));
		} catch (Exception ex) {
			Log.error(ex);
		}
	}

	/**
	 * Creates the datafile and returns a gnuplot expression defining
	 * the data file for the gnuplot <code>plot</code> command (with
	 * <code>smooth unique</code> is set).
	 *
	 * @return gnuplot expression defining the function or data file
	 */
	public String getCommand() {
		write();
		return super.getCommand();
	}

	/**
	 * Switches to the next possible (x,y) data pair if possible.<p>
	 *
	 * This method has to be overridden in subclasses.
	 *
	 * @return true iff another data pair is available
	 */
	public abstract boolean next();

	/**
	 * Returns the x value of the current (x,y) data pair.<p>
	 *
	 * This method has to be overridden in subclasses.
	 *
	 * @return x value
	 */
	public abstract double getX();

	/**
	 * Returns the y value of the current (x,y) data pair.<p>
	 *
	 * This method has to be overridden in subclasses.
	 *
	 * @return y value
	 */
	public abstract double getY();

}
