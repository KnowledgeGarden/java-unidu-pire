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

 
// $Id: SingleItemFilter.java,v 1.5 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

/**
 * A filter which converts each object into exactly one object (or into null).
 * This property makes it easy to use the filter also in environments where
 * it is not desireable to use an iterator.
 * 
 * @author Henrik Nottelmann
 * @since 2003-07-04
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:28 $
 */
public interface SingleItemFilter {

	/**
	 * Applies this filter on the specified object, and returns a single 
	 * object (or null).
	 *  
	 * @param value value to be modified by this filter
	 * @return resulting object, or null
	 */
	public Object run(Object value);
}
