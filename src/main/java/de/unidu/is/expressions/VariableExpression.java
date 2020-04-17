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

 
// $Id: VariableExpression.java,v 1.5 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.expressions;

import java.util.List;
import java.util.Map;

/**
 * An expression for a variable.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-21
 * @version $Revision: 1.5 $, $Date: 2005/03/14 17:33:14 $
 */
public class VariableExpression extends AbstractExpression  {

	/**
	 * The variable name of this expression.
	 * 
	 */
	protected String name;

	/**
	 * Creates a new expression object.
	 * 
	 * @param name variable name of this expression
	 */
	public VariableExpression(String name) {
		this.name = name;
	}

	/**
	 * Returns the variable name of this expression.
	 * 
	 * @return variable name of this expression
	 */
	public String get() {
		return name;
	}

	/**
	 * Sets the variable name of this expression.
	 * 
	 * @param name name of this expression
	 */
	public void set(String name) {
		this.name = name;
	}

	/**
	 * Returns the variable name of this expression.
	 * 
	 * @return content of this expression
	 */
	public String toString() {
		return name;
	}

	/**
	 * Tests whether this expressions equals the specified VariableExpression.
	 *
	 * @param o expression to test 
	 * @return true iff both expressions are equal
	 */
	public boolean equals(Object o) {
		if (!(o instanceof VariableExpression))
			return false;
		return ((VariableExpression) o).get().equals(name);
	}

	/**
	 * Returns the hashcode of this expression object.
	 * 
	 * @return hashcode of this expression object
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Performs an substitution for variables, e.g. for each key 
	 * <code>variable</code> in the specified binding, all occurences of 
	 * <code>${key}</code> are replaced by the corresponding value in the 
	 * map.<p>
	 * 
	 * Here, the variable is substituted if possible.
	 * 
	 * @param binding variable binding
	 * @return expression after substitution
	 */
	public Expression substitute(Map binding) {
		List list = (List) binding.get(this);
		if (list == null)
			return this;
		return ((Expression) list.get(0)).substitute(binding);
	}

}
