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

 
// $Id: LiteralExpression.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import de.unidu.is.expressions.AbstractExpression;


/**
 * An expression holding a literal, used for aggregation operators.
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public class LiteralExpression extends AbstractExpression {

	/**
	 * The literal.
	 */
	protected Literal literal;

	/**
	 * Creates a new object.
	 *
	 * @param literal literal to be used
	 */
	public LiteralExpression(Literal literal) {
		this.literal = literal;
	}

	/**
	 * Returns the literal.
	 * 
	 * @return literal
	 */
	public Literal getLiteral() {
		return literal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{" + literal + "}";
	}

}
