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


// $Id: IDBRelation.java,v 1.7 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.pdatalog;

import de.unidu.is.pdatalog.ds.Rule;

import java.util.Collection;

/**
 * A class for pDatalog++ relation derived by rules (corresponding to an
 * intensional predicate).
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:13 $
 * @since 2003-10-07
 */
public class IDBRelation extends Relation {

    /**
     * Constructs a new relation, and automatically adds it to the
     * relation base.
     *
     * @param base  corresponding relation base
     * @param name  relatio n name
     * @param arity arity of the relation
     */
    public IDBRelation(RelationBase base, String name, int arity) {
        super(base, name, arity);
    }

    /**
     * Constructs a new relation, and automatically adds it to the
     * relation base.
     *
     * @param base   corresponding relation base
     * @param name   relatio n name
     * @param arity  arity of the relation
     * @param create if true, the relation is physically created
     */
    public IDBRelation(RelationBase base, String name, int arity, boolean create) {
        super(base, name, arity, create);
    }

    /**
     * Computes the result of the specified rule.
     *
     * @param rule single rulefor this relation
     */
    public void compute(Rule rule) {
		/*List rules = new ArrayList();
		rules.add(rule);
		base.computeDisjoint(this,rules);*/
        base.compute(this, rule);
    }

    /**
     * Computes the result of the specified rules.
     *
     * @param rules collection of rules for this relation
     */
    public void compute(Collection rules) {
        base.compute(this, rules);
    }

    /**
     * Computes the result of the specified rules. The results from the
     * single rules are considered to be disjoint.
     *
     * @param rules collection of rules for this relation
     */
    public void computeDisjoint(Collection rules) {
        base.computeDisjoint(this, rules);
    }

}
