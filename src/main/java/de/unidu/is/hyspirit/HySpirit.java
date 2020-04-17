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

// $Id: HySpirit.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.hyspirit;

/**
 * An interface definition for connecting to the HySpirit inference engine for
 * pDatalog built by our group (the version implemented in the Beta
 * object-oriented programming language).
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public interface HySpirit {

	/**
	 * Sends text (facts, rules, queries etc.) to HySpirit, and receives the result.
	 * 
	 * @param text text to be sent to HySpirit
	 * @return HySpirit answer
	 */
	public String send(String text);

	/**
	 * Closes the connection.
	 *
	 */
	public void close();

}