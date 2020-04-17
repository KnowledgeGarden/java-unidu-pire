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

 
// $Id: Function.java,v 1.7 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

/**
 * The abstract super-class of (parametrisable) functions.
 * 
 * @author Henrik Nottelmann
 * @since 2004-06-21
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:29 $
 */
public abstract class Function {

	/**
	 * Parameters of this function (stored as doubles).
	 */
	protected PropertyMap parameters;

	/**
	 * Apply the value <code>x</code> onto the function, and return the
	 * function value.
	 * 
	 * @param x
	 *                   value to apply
	 * @return result for x
	 */
	public abstract double apply(double x);

	/**
	 * Returns the parameter names.
	 * 
	 * @return parameter names
	 */
	public abstract String[] getParameterNames();

	/**
	 * Returns all parameters.
	 * 
	 * @return all parameters
	 */
	public PropertyMap getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param map
	 *                   function parameters
	 */
	public void setParameters(PropertyMap map) {
		parameters = map;
	}

	/**
	 * Returns the value of the specified parameter.
	 * 
	 * @param name
	 *                   parameter name
	 * @return parameter value (0 if parameter does not exist)
	 */
	public double getParameter(String name) {
		return parameters == null ? 0 : parameters.getDouble(name);
	}

	/**
	 * Initialises the function with the specified values. This implementation
	 * does nothing. Subclasses can override this method e.g. for computing a
	 * parameter as the maximum value in the specified property map.
	 * 
	 * @param values
	 *                   values for initialisation.
	 */
	public void init(PropertyMap values) {
	}

}
