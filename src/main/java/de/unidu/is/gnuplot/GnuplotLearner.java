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

 
// $Id: GnuplotLearner.java,v 1.6 2005/02/21 17:29:19 huesselbeck Exp $
package de.unidu.is.gnuplot;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.unidu.is.learning.Learner;
import de.unidu.is.util.Config;
import de.unidu.is.util.Log;

/**
 * A class for learning parameters of a function with the Linux/Unix tool 
 * <code>gnuplot</code>.
 *
 * @author Henrik Nottelmann
 * @since 2002-05-14
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:19 $
*/
public class GnuplotLearner implements Learner {

	/**
	 * The general proxy logger (for all proxies).
	 *
	 */
	protected static Logger logger = Log.getLogger("unidu.learner");

	/**
	 * An ID for distinguishing temporary files created by this class.
	 *
	 */
	private static int learnID = 0;

	/**
	 * Returns the next ID.
	 * 
	 * @return next ID
	 */
	private static synchronized int nextID() {
		return learnID++;
	}

	/**
	 * Returns the directory used for tempory files.
	 * 
	 * @return directory used for tempory files
	 */
	private File getTmpDir() {
		return new File(Config.getString("tmp.path"));
	}

	/**
	 * Writes (x,y) data points (stored in two arrays) into the specified file.
	 *
	 * @param file file to which the data is written
	 * @param x array with x values
	 * @param y array with y values
	 */
	private void writeData(File file, double[] x, double[] y) {
		new ArrayDataSource("dummy", file, x, y).write();
	}

	/**
	 * Writes (x,y) data points (stored in two arrays) into a temporary file.
	 *
	 * @param x array with x values
	 * @param y array with y values
	 * @return file to which the data was written
	 */
	private File writeData(double[] x, double[] y) {
		File file = new File(getTmpDir(), "learner." + nextID());
		writeData(file, x, y);
		return file;
	}

	/**
	 * Learns the specified variables of the specified function with
	 * the given data points.<p>
	 *
	 * Internally, the Linux/Unix tool <code>gnuplot</code> is used
	 * for learning.
	 *
	 * @param x array with x values
	 * @param y array with y values
	 * @param function function
	 * @param vars variables to learn 
	 * @return map with variables and values
	 */
	public Map learn(double[] x, double[] y, String function, String[] vars) {
		String filename = writeData(x, y).toString();
		return learn(filename, " ", function, vars);
	}

	/**
	 * Learns the specified variables of the specified function with
	 * the given data points.<p>
	 *
	 * Internally, the Linux/Unix tool <code>gnuplot</code> is used
	 * for learning.
	 *
	 * @param filename name of file with data points
	 * @param separator string separating x and y values
	 * @param function function
	 * @param vars variables to learn 
	 * @return map with variables and values
	 */
	public Map learn(
		String filename,
		String separator,
		String function,
		String[] vars) {
		Map results = new HashMap();
		try {
			String fname = function.substring(0, function.indexOf("(")).trim();
			File log = new File(getTmpDir(), "fit" + nextID() + ".log");
			log.delete();
			StringBuffer buf = new StringBuffer();
			buf.append(function + "\n");
			buf.append("FIT_LIMIT = 1e-10\n");
			String var = "";
			for (int i = 0; i < vars.length; i++)
				var += vars[i] + ",";
			var = var.substring(0, var.length() - 1);
			buf.append(
				"fit "
					+ fname
					+ "(x) '"
					+ filename
					+ "' using 1:2 via "
					+ var
					+ "\n");
			if (logger.isDebugEnabled())
				logger.debug(
					"File=" + filename + ", " + function + ", Vars: " + var);
			BufferedReader reader =
				new BufferedReader(
					GnuplotCall.call(buf.toString(), true, true));
			for (String line = null;(line = reader.readLine()) != null;) {
				logger.debug(line);
				int h = line.indexOf("=");
				if (h != -1) {
					String n = line.substring(0, h).trim();
					for (int i = 0; i < vars.length; i++) {
						if (n.equals(vars[i])) {
							String v = line.substring(h + 2);
							h = v.indexOf(" ");
							if (h != -1)
								v = v.substring(0, h);
							results.put(n, new Double(v));
						}
					}
				}
			}
			reader.close();
			log.delete();
			logger.info(
				"File="
					+ filename
					+ ", "
					+ function
					+ ", Vars: "
					+ var
					+ ", Results: "
					+ results);
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return results;
	}

}
