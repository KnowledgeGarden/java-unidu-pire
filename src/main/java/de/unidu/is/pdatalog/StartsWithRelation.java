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

// $Id: StartsWithRelation.java,v 1.1 2005/03/17 22:30:15 nottelma Exp $
package de.unidu.is.pdatalog;

import de.unidu.is.expressions.Arg2Expression;
import de.unidu.is.expressions.BinaryStandardFunctionExpression;
import de.unidu.is.expressions.Expression;
import de.unidu.is.expressions.PlainExpression;
import de.unidu.is.pdatalog.ds.Literal;

import java.util.List;

/**
 * A class for the pDatalog++ startswith relation which is computed on demand.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/03/17 22:30:15 $
 * @since 2005-03-17
 */
public class StartsWithRelation extends EDBComputedRelation {

    /**
     * Constructs a new relation, and automatically adds it to the relation
     * base.
     *
     * @param base corresponding relation base
     */
    public StartsWithRelation(RelationBase base) {
        super(base, "startswith", 2);
    }

    /**
     * Adds arguments for computing a probability for the literal to the
     * specified list.
     *
     * @param literal literal to be handled
     * @param prob    list of arguments for computing a probability
     */
    public void addProb(Literal literal, List prob) {
    }

    /**
     * Adds arguments for filtering for the literal to the specified list.
     *
     * @param literal literal to be handled
     * @param where   list of arguments for filtering
     */
    public void addWhere(Literal literal, List where) {
        Expression arg0 = literal.getArgument(0);
        Expression arg1 = literal.getArgument(1);
        //		where.add(new Arg2Expression(" like ", arg0,
        //				new PlainExpression(arg1.getSQLTemplate().replaceAll("_",
        //						"\\_").replaceAll("%", "\\%") + "%")));
        //		where.add(new Arg2Expression("=", new
        // BinaryStandardFunctionExpression(
        //				"\"" + getClass().getName() + ".startswith\"", arg0, arg1),
        //				new PlainExpression("1")));
        where.add(new Arg2Expression("=", new BinaryStandardFunctionExpression(
                "locate", arg1, arg0), new PlainExpression("1")));
    }

//	public static int startswith(String a, String b) {
//		return a.startsWith(b) ? 1 : 0;
//	}

}