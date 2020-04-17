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

 
// $Id: HashPropertyMap.java,v 1.7 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.util.LinkedHashMap;

/**
 * A property map which is backed by a LinkedHashMap.
 * 
 * @author Henrik Nottelmann
 * @since 2003-06-22
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:29 $
 * @see java.util.HashMap
 */
public class HashPropertyMap extends DelegatedPropertyMap {

	/**
	 * Creates a new (empty) instance.
	 */
	public HashPropertyMap() {
		super(new LinkedHashMap());
	}

	/**
	 * Creates a new (empty) instance.
	 * 
	 * @param multiple
	 *                   values if true, multiple values can be hold for a key
	 */
	public HashPropertyMap(boolean multiple) {
		super(new LinkedHashMap(), multiple);
	}

}
