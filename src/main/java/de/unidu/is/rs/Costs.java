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

 
// $Id: Costs.java,v 1.6 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.rs;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A class for estimated costs when retrieving of i documents,
 * 1<=i<query.getNumDocs() for different cost sources.
 * 
 * @author Henrik Nottelmann
 * @since 2002-06-06
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:14 $
 */
public class Costs extends HashMap {

	/**
	 * Returns the costs when retrieving of i documents,
	 * 1<=i<query.getNumDocs() for the specified cost source.
	 * 
	 * @param source cost source
	 * @return cost array
	 */
	public double[] getCosts(String source) {
		return (double[]) get(source);
	}

	/**
	 * Sets the costs when retrieving of i documents,
	 * 1<=i<query.getNumDocs() for the specified cost source.
	 * 
	 * @param source cost source
	 * @param costs cost array
	 */
	public void setCosts(String source, double[] costs) {
		put(source, costs);
	}

	/**
	 * Returns the names of all cost sources for which costs are
	 * stored in this instance.
	 *
	 * @return iterator over cost names
	 */
	public Iterator costSources() {
		return keySet().iterator();
	}

}
