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

 
// $Id: FunctionExpression.java,v 1.5 2005/03/01 09:31:24 nottelma Exp $
package de.unidu.is.expressions;

import java.util.Map;

/**
 * An expression for functions.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-27
 * @version $Revision: 1.5 $, $Date: 2005/03/01 09:31:24 $
 */
public class FunctionExpression extends AbstractExpression {

	/**
	 * Function name.
	 * 
	 */
	protected String name;

	/**
	 * Function argument.
	 * 
	 */
	protected Expression arg;

	/**
	 * Creates a new expression object.
	 * 
	 * @param name function name
	 * @param arg function argument
	 */
	public FunctionExpression(String name, Expression arg) {
		this.name = name;
		this.arg = arg;
	}

	/**
	 * Performs an substitution for variables, e.g. for each key 
	 * <code>variable</code> in the specified binding, all occurences of 
	 * <code>${key}</code> are replaced by the corresponding value in the 
	 * map.<p>
	 * 
	 * Only the argument is substituted.
	 * 
	 * @param binding variable binding
	 * @return expression after substitution
	 */
	public Expression substitute(Map binding) {
		Expression newArg = arg.substitute(binding);
		if (newArg == arg)
			return this;
		return new FunctionExpression(name, newArg);
	}

	/**
	 * Returns the expression, starting with an ampersand.
	 * 
	 * @return expression as a string
	 */
	public String toString() {
		return "&" + name + "(" + arg + ")";
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.expressions.Expression#getSQLTemplate()
	 */
	public String getSQLTemplate() {
		return name + "(" + arg.getSQLTemplate() + ")";
	}

	/**
	 * Returns the function argument.
	 * 
	 * @return function argument
	 */
	public Expression getArgument() {
		return arg;
	}

	/**
	 * Returns the  function name.
	 * 
	 * @return function name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets  the function argument.
	 * 
	 * @param argument function argument
	 */
	public void setArgument(Expression argument) {
		arg = argument;
	}

	/**
	 * Sets the  function name.
	 * 
	 * @param string function name
	 */
	public void setName(String string) {
		name = string;
	}

}
