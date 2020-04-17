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

 
// $Id: SumExpression.java,v 1.4 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.expressions;

/**
 * An expression for the sum of two arguments, eg. arg1+arg2.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-27
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:18 $
 */
public class SumExpression extends Arg2Expression {

	/**
	 * Creates a new expression object.
	 * 
	 * @param arg1 first argument of this expression
	 * @param arg2 second argument of this expression
	 */
	public SumExpression(Expression arg1, Expression arg2) {
		super("+", arg1, arg2);
	}

}
