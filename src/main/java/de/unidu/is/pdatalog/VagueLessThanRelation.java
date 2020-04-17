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

 
// $Id: VagueLessThanRelation.java,v 1.6 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.pdatalog;

import java.util.List;

import de.unidu.is.expressions.Expression;
import de.unidu.is.expressions.DifferenceExpression;
import de.unidu.is.expressions.FractionExpression;
import de.unidu.is.expressions.Str2NumFunctionExpression;
import de.unidu.is.expressions.StringExpression;
import de.unidu.is.pdatalog.ds.Literal;

/**
 * A class for the pDatalog++ vague less-than relation which is computed on 
 * demand.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-08
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:13 $
 */
public class VagueLessThanRelation extends EDBComputedRelation {

	/**
	 * Constructs a new relation, and automatically adds it to the 
	 * relation base.
	 * 
	 * @param base corresponding relation base
	 */
	public VagueLessThanRelation(RelationBase base) {
		super(base, "vlt", 2);
	}

	/**
	 * Adds arguments for computing a probability for the literal to the 
	 * specified list. 
	 * 
	 * @param literal literal to be handled
	 * @param prob list of arguments for computing a probability
	 */
	public void addProb(Literal literal, List prob) {
		Expression arg0 = new Str2NumFunctionExpression(literal.getArgument(0));
		Expression arg1 = new Str2NumFunctionExpression(literal.getArgument(1));
		prob.add(
			new DifferenceExpression(
				new StringExpression("1"),
				new FractionExpression(
					new DifferenceExpression(arg0, arg1),
					arg0)));
	}

	/**
	 * Adds arguments for filtering for the literal to the specified list. 
	 * 
	 * @param literal literal to be handled
	 * @param where list of arguments for filtering
	 */
	public void addWhere(Literal literal, List where) {
	}

}
