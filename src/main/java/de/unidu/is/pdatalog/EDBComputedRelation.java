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

 
// $Id: EDBComputedRelation.java,v 1.5 2005/02/28 22:27:54 nottelma Exp $
package de.unidu.is.pdatalog;

import java.util.List;

import de.unidu.is.pdatalog.ds.Literal;

/**
 * A class for pDatalog++ relation which is computed on demand.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-07
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:54 $
 */
public abstract class EDBComputedRelation extends Relation {

	/**
	 * Constructs a new relation, and automatically adds it to the 
	 * relation base.
	 * 
	 * @param base corresponding relation base
	 * @param name relatio n name
	 * @param arity arity of the relation
	 */
	public EDBComputedRelation(RelationBase base, String name, int arity) {
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
	public EDBComputedRelation(
		RelationBase base,
		String name,
		int arity,
		boolean create) {
		super(base, name, arity, create);
	}

	/**
	 * Adds arguments for computing a probability for the literal to the 
	 * specified list. 
	 * 
	 * @param literal literal to be handled
	 * @param prob list of arguments for computing a probability
	 */
	public abstract void addProb(Literal literal, List prob);

	/**
	 * Adds arguments for filtering for the literal to the specified list. 
	 * 
	 * @param literal literal to be handled
	 * @param where list of arguments for filtering
	 */
	public abstract void addWhere(Literal literal, List where);

}
