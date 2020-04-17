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

 
// $Id: Arg2Expression.java,v 1.5 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.expressions;

import java.util.Map;

/**
 * An expression with an operator and two arguments, e.g. the sum of two 
 * expressions.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-27
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:18 $
 */
public class Arg2Expression extends AbstractExpression {

	/**
	 * Expression operator.
	 * 
	 */
	protected String op;

	/**
	 * First argument of this expression.
	 * 
	 */
	protected Expression arg1;

	/**
	 * Second argument of this expression.
	 * 
	 */
	protected Expression arg2;

	/**
	 * Creates a new expression object.
	 * 
	 * @param op expression operator
	 * @param arg1 first argument of this expression
	 * @param arg2 second argument of this expression
	 */
	public Arg2Expression(String op, Expression arg1, Expression arg2) {
		this.op = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	/**
	 * Performs an substitution for variables, e.g. for each key 
	 * <code>variable</code> in the specified binding, all occurences of 
	 * <code>${key}</code> are replaced by the corresponding value in the 
	 * map.<p>
	 * 
	 * Only the arguments are substituted.
	 * 
	 * @param binding variable binding
	 * @return expression after substitution
	 */
	public Expression substitute(Map binding) {
		Expression newArg1 = arg1.substitute(binding);
		Expression newArg2 = arg2.substitute(binding);
		if (arg1 != newArg1 || arg2 != newArg2)
			return new Arg2Expression(op, newArg1, newArg2);
		return this;
	}

	/**
	 * Returns the expression in infix notation, embedded in round brackets.
	 * 
	 * @return expression as a string
	 */
	public String toString() {
		return "(" + arg1 + op + arg2 + ")";
	}

	/**
	 * Returns a string representation for this expression which can be 
	 * used as a template in an SQL statement.
	 * 
	 * @return template for SQL statements
	 */
	public String getSQLTemplate() {
		return "(" + arg1.getSQLTemplate() + op + arg2.getSQLTemplate() + ")";
	}

    
	/**
	 * Returns the first argument.
	 * 
	 * @return first argument
	 */
	public Expression getArg1() {
		return arg1;
	}

	/**
	 * Returns the second argument.
	 * 
	 * @return second argument
	 */
	public Expression getArg2() {
		return arg2;
	}

	/**
	 * Returns the operator symbol.
	 * 
	 * @return operator symbol
	 */
	public String getOp() {
		return op;
	}

}
