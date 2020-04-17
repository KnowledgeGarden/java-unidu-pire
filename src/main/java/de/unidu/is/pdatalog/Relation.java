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

// $Id: Relation.java,v 1.7 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.pdatalog;

/**
 * An abstract pDatalog++ relation. This class will be extended by classes for
 * stored relations, computed relations, derived relations etc.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-07
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:13 $
 */
public abstract class Relation {

	/**
	 * The corresponding relation base.
	 */
	protected RelationBase base;

	/**
	 * The relation name.
	 */
	protected String name;

	/**
	 * The arity (number of arguments) of this relation.
	 */
	protected int arity;

	/**
	 * Constructs a new relation, without adding it to the relation base.
	 * 
	 * @param name
	 *                   relatio n name
	 * @param arity
	 *                   arity of the relation
	 */
	public Relation(String name, int arity) {
		this(null, name, arity, true);
	}

	/**
	 * Constructs a new relation, and automatically adds it to the relation
	 * base.
	 * 
	 * @param base
	 *                   corresponding relation base
	 * @param name
	 *                   relatio n name
	 * @param arity
	 *                   arity of the relation
	 */
	public Relation(RelationBase base, String name, int arity) {
		this(base, name, arity, true);
	}

	/**
	 * Constructs a new relation, and automatically adds it to the relation
	 * base.
	 * 
	 * @param base
	 *                   corresponding relation base
	 * @param name
	 *                   relatio n name
	 * @param arity
	 * arity of the relation @ param create if true, the relation is physically
	 * created @ status finished 2003-12-08
	 */
	public Relation(RelationBase base, String name, int arity, boolean create) {
		this.base = base;
		this.name = name;
		this.arity = arity;
		if (base != null)
			base.add(this, create);
	}

	/**
	 * Returns the name of this relation.
	 * 
	 * @return name of this relation
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the arity of this relation.
	 * 
	 * @return arity of this relation
	 */
	public int getArity() {
		return arity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Relation:" + name;
	}

}