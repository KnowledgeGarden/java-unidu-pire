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

 
// $Id: EDBRelation.java,v 1.6 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.pdatalog;

import de.unidu.is.expressions.StringExpression;
import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.pdatalog.ds.Literal;

/**
 * A class for pDatalog++ relation whose facts are stored in a database table 
 * (corresponding to an extensional predicate).
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-07
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:13 $
 */
public class EDBRelation extends Relation {

	/**
	 * Constructs a new relation, and automatically adds it to the 
	 * relation base.
	 * 
	 * @param base corresponding relation base
	 * @param name relatio n name
	 * @param arity arity of the relation
	 */
	public EDBRelation(RelationBase base, String name, int arity) {
		super(base, name, arity);
	}

	/**
	 * Constructs a new relation, and automatically adds it to the 
	 * relation base.
	 * 
	 * @param base corresponding relation base
	 * @param name relatio n name
	 * @param arity arity of the relation
	 * @param create if true, the relation is physically created
	 */
	public EDBRelation(
		RelationBase base,
		String name,
		int arity,
		boolean create) {
		super(base, name, arity, create);
	}

	/**
	 * Adds the specified fact to the knowledge base.
	 * 
	 * @param args arguments
	 */
	public void add(StringExpression[] args) {
		add(1, args);
	}

	/**
	 * Adds the specified fact to the knowledge base.
	 * 
	 * @param prob probability of this fact 
	 * @param args arguments
	 */
	public void add(double prob, StringExpression[] args) {
		add(new Fact(prob, new Literal(name, args, true)));
	}

	/**
	 * Adds the specified fact to the knowledge base.
	 * 
	 * @param arguments arguments
	 */
	public void add(String[] arguments) {
		add(1, arguments);
	}

	/**
	 * Adds the specified fact to the knowledge base.
	 * 
	 * @param prob probability of this fact 
	 * @param arguments arguments
	 */
	public void add(double prob, String[] arguments) {
		StringExpression[] args = new StringExpression[arguments.length];
		for (int i = 0; i < args.length; i++)
			args[i] = new StringExpression(arguments[i]);
		add(new Fact(prob, new Literal(name, args, true)));
	}

	/**
	 * Adds the specified fact to the knowledge base.
	 * 
	 * @param fact fact to be added
	 */
	public void add(Fact fact) {
		base.add(fact);
	}

}
