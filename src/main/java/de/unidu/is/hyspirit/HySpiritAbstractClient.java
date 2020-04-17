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

//$Id: HySpiritAbstractClient.java,v 1.8 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.hyspirit;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * An abtract implementation of the interface for connecting to the HySpirit
 * inference engine for pDatalog built by our group (the version implemented in
 * the Beta object-oriented programming language).<p>
 * 
 * Sub-classes have to set up the reader and the writer.
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/03/14 17:33:13 $
 */

public abstract class HySpiritAbstractClient implements HySpirit {

	/**
	 * Reader from HySpirit, has to be initialised in a sub-class.
	 */
	protected BufferedReader in;

	/**
	 * Writer to HySpirit, has to be initialised in a sub-class.
	 */
	protected PrintWriter out;

	/**
	 * Sends text (facts, rules, queries etc.) to HySpirit, and receives the result.
	 * 
	 * @param text text to be sent to HySpirit
	 * @return HySpirit answer
	 */
	public String send(String text) {
		try {
			if (!text.endsWith("\n"))
				text += "\n";
			text += "~t BREAK\n";
			out.print(text);
			out.flush();
			String ret = "";
			String currentLine;
			while (true) {
				currentLine = getLine();
				if (currentLine.equals("(* BREAK *)\n"))
					break;
				ret += currentLine;
			}
			return ret;
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return null;
	}

	/**
	 * Reads in a line, and returns it.
	 * 
	 * @return line from HySpirit
	 */
	protected String getLine() {
		String ret = "";
		try {
			while (true) {
				int ci = in.read();
				if (ci == -1)
					continue;
				char c = (char) ci;
				ret += c;
				if (ci == 10)
					break;
			}
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return ret;
	}

	/**
	 * Closes tje client.
	 */
	public void close() {
		try {
			in.close();
			out.close();
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
	}

}