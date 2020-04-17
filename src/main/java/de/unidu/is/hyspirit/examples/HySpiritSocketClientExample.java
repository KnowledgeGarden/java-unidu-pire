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

 
//$Id: HySpiritSocketClientExample.java,v 1.1 2005/02/28 22:27:56 nottelma Exp $

package de.unidu.is.hyspirit.examples;

import de.unidu.is.hyspirit.HySpirit;
import de.unidu.is.hyspirit.HySpiritSocketClient;

/**
 * An example for connecting to HySpirit via a socket (on port 4711).<p>
 * 
 * HySpirit has to be started as:
 * <pre>
 *   hyspirit -s 4711
 * </pre>
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/02/28 22:27:56 $
 */
public class HySpiritSocketClientExample {

	public static void main(String[] args) {
		HySpirit hy = new HySpiritSocketClient("localhost",4711);
		hy.send("a(1).a(2).b(1).");
		hy.send("x(X) :- a(X).");
		hy.send("y(X) :- a(X) & b(X).");
		System.out.println(hy.send("?- x(X)."));
		System.out.println(hy.send("?- y(X)."));
		hy.close();
	}

}
