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

 
// $Id: GnuplotCall.java,v 1.7 2005/02/21 17:29:19 huesselbeck Exp $
package de.unidu.is.gnuplot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import de.unidu.is.util.Config;

/**
 * A class for calling the Linux/Unix tool <code>gnuplot</code>.
 * 
 * @author Henrik Nottelmann
 * @since 2003-07-2003
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:19 $
 */
public class GnuplotCall {

	/**
	 * Calls gnuplot and returns the output of STDERR.<p>
	 * 
	 * Actually, this method issues a call for the config parameter
	 * <code>gnuplot.bin</code>.
	 * 
	 * @param input gnuplot input
	 * @return reader for STDERR
	 * @throws IOException
	 */
	public static Reader call(String input) throws IOException {
		return call(input, true, true);
	}

	/**
	 * Calls gnuplot and returns the output on STDOUT or STDERR.<p>
	 * 
	 * Actually, this method issues a call for the config parameter
	 * <code>gnuplot.bin</code>.
	 * 
	 * @param input gnuplot input
	 * @param returnReader if true, return a reader
	 * @param errorReader if true, returns a reader for STDERR, otherwise for 
	 *                STDOUT, if returnReader==true
	 * @return reader for STDERR or STDOUT, or null
	 * @throws IOException
	 */
	public static Reader call(
		String input,
		boolean returnReader,
		boolean errorReader)
		throws IOException {
		Process p = Runtime.getRuntime().exec(Config.getString("gnuplot.bin"));
		PrintWriter pw = new PrintWriter(p.getOutputStream());
		pw.print(input);
		if (returnReader) // see below: bad hack
			pw.println("x");
		pw.close();
		if (!returnReader) {
			try {
				p.waitFor();
			} catch (Exception ex) {
				de.unidu.is.util.Log.error(ex);
			}
			return null;
		}
		StringWriter writer = new StringWriter();
		PrintWriter w = new PrintWriter(writer);
		InputStream is = errorReader ? p.getErrorStream() : p.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		for (String line = null;(line = reader.readLine()) != null;) {
			// bad hack to prevent hanging in readLine()
			if (line.indexOf(": invalid command") != -1
				|| line.startsWith("gnuplot> x"))
				break;
			w.println(line);
		}
		reader.close();
		w.close();
		return new StringReader(writer.getBuffer().toString());
	}

}
