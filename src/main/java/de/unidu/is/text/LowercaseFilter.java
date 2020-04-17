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

 
// $Id: LowercaseFilter.java,v 1.6 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

/**
 * This filter converts a specified string into lowercase.
 * 
 * @author Henrik Nottelmann
 * @since 2002-06-23
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:28 $
 */
public class LowercaseFilter extends AbstractSingleItemFilter {

	/**
	 * Creates a new instance and sets the next filter in the chain.
	 * 
	 * @param nextFilter next filter in the filter chain
	 */
	public LowercaseFilter(Filter nextFilter) {
		super(nextFilter);
	}

	/**
	 * Converts the specified string into lowercase.
	 *
	 * @param value string
	 * @return string content in lowercase
	 */
	public Object run(Object value) {
		return value.toString().toLowerCase();
	}

}
