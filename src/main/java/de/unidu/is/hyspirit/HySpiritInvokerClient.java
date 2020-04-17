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

//$Id: HySpiritInvokerClient.java,v 1.7 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.hyspirit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * A class for starting and connecting to the HySpirit inference engine for
 * pDatalog built by our group (the version implemented in the Beta
 * object-oriented programming language).
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/28 22:27:55 $
 */
public class HySpiritInvokerClient extends HySpiritAbstractClient {

	/**
	 * HySpirit process.
	 */
	protected Process process;

	/**
	 * Creates a new object.
	 *
	 * @param hyspiritPath path to the HySpirit program
	 */
	public HySpiritInvokerClient(String hyspiritPath) {
		try {
			process = Runtime.getRuntime().exec(
					new String[]{hyspiritPath, "-i"});
			Thread.sleep(1000);
			in = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			out = new PrintWriter(process.getOutputStream());
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
	}

	/**
	 * Closes the HySpirit instance.
	 */
	public void close() {
		send("~e");
		super.close();
	}

}