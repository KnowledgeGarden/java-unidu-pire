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


// $Id: Fact.java,v 1.8 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import de.unidu.is.expressions.Expression;
import de.unidu.is.expressions.ProductExpression;

import java.util.ArrayList;

/**
 * The representation of a datalog probabilistic fact. A fact is a rule
 * with an emtpy (always true) body.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-10-27
 */
public class Fact extends Rule {

    /**
     * Creates a new probabilistic fact.
     *
     * @param head    "head" of the fact
     * @param mapping mapping function
     */
    public Fact(Literal head, Expression mapping) {
        super(head, new ArrayList(), mapping);
    }

    /**
     * Creates a new deterministic fact from the specified literal. This literal
     * is the head of the new fact.
     *
     * @param head literal describing this deterministic fact
     */
    public Fact(Literal head) {
        this(1, head);
    }

    /**
     * Creates a new fact from the specified literal with the given probability.
     * This literal is the head of the new fact.
     *
     * @param prob probability of this fact
     * @param head literal describing this fact
     */
    public Fact(double prob, Literal head) {
        super(prob, head, new ArrayList());
    }

    /**
     * Creates a new fact as a copy of the specified fact.
     *
     * @param fact fact to be copied
     */
    public Fact(Fact fact) {
        super(fact);
    }

    /**
     * Returns a string description of the constants of this fact, separated
     * by colons.
     *
     * @return the string description of the constants of this fact
     */
    public String getConsts() {
        return head.getArguments();
    }

    /**
     * Returns the arity of this fact's predicate.
     *
     * @return arity of this fact's predicate
     */
    public int getArity() {
        return head.getArity();
    }

    /**
     * Returns the constant of this fact at the specified index.
     *
     * @return constant of this fact at the specified index
     */
    public Expression getConst(int num) {
        return head.getArgument(num);
    }

    /**
     * Returns the probabilistic weight of this fact.
     *
     * @return probabilistic weight of this fact
     */
    public double getProb() {
        Expression mapping = getMapping();
        if ((!(mapping instanceof ProductExpression)))
            return 1;
        ProductExpression mapp = (ProductExpression) mapping;
        String probStr = mapp.getArg1().toString();
        try {
            return Double.parseDouble(probStr);
        } catch (NumberFormatException e) {
            de.unidu.is.util.Log.error(e);
        }
        return 1;
    }

}
