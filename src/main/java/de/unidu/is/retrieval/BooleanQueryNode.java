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

// $Id: BooleanQueryNode.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A query node representating a Boolean connector.
 * 
 * @author Henrik Nottelmann
 * @since 2004-05-22
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public abstract class BooleanQueryNode implements QueryNode {

	/**
	 * Set of children nodes (QueryNode instances).
	 */
	protected Set children;

	/**
	 * Creates a new object.
	 */
	public BooleanQueryNode() {
		children = new LinkedHashSet();
	}

	/**
	 * Creates a new object.
	 * 
	 * @param children
	 *                   children nodes
	 */
	public BooleanQueryNode(Set children) {
		this();
		setChildren(children);
	}

	/**
	 * Returns the children.
	 * 
	 * @return children nodes
	 */
	public Set getChildren() {
		return children;
	}

	/**
	 * Sets the children.
	 * 
	 * @param list
	 *                   children nodes
	 */
	public void setChildren(Set list) {
		children.addAll(list);
	}

	/**
	 * Adds the specified query node as a child.
	 * 
	 * @param node
	 *                   new child node
	 */
	public void add(QueryNode node) {
		children.add(node);
	}

	/**
	 * Returns a XIRQL representation of the connector represented by this node.
	 * 
	 * @return XIRQL representation of the connector
	 */
	protected abstract String getConnector();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		String conn = " " + getConnector() + " ";
		for (Iterator it = iterator(); it.hasNext();) {
			QueryNode node = (QueryNode) it.next();
			if (buf.length() > 0)
				buf.append(conn);
			buf.append(node);
		}
		return "(" + buf.toString() + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.QueryNode#toPrefix()
	 */
	public String toPrefix() {
		StringBuffer buf = new StringBuffer();
		for (Iterator it = iterator(); it.hasNext();) {
			QueryNode node = (QueryNode) it.next();
			if (buf.length() > 0)
				buf.append(",");
			buf.append(node.toPrefix());
		}
		return getConnector() + "[" + buf + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.QueryNode#iterator()
	 */
	public Iterator iterator() {
		return children.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.QueryNode#simplifiedNode()
	 */
	public QueryNode simplifiedNode() {
		BooleanQueryNode qn = newNode();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			QueryNode node = (QueryNode) iter.next();
			node = node.simplifiedNode();
			if (sameClass(node)) {
				for (Iterator iter2 = node.iterator(); iter2.hasNext();)
					qn.add(((QueryNode) iter2.next()).simplifiedNode());
			} else
				qn.add(node);
		}
		return qn;
	}

	/**
	 * Returns true iff the specified query node represents the same class as
	 * this node.
	 * 
	 * @param node
	 *                   query node to compare
	 * @return true iff the specified query node represents the same class as
	 *               this node
	 */
	protected abstract boolean sameClass(QueryNode node);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.QueryNode#cloneNode()
	 */
	public QueryNode cloneNode() {
		BooleanQueryNode node = newNode();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			QueryNode n = (QueryNode) iter.next();
			node.add(n.cloneNode());
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof BooleanQueryNode))
			return false;
		BooleanQueryNode qn = (BooleanQueryNode) obj;
		if (!sameClass(qn))
			return false;
		return children.equals(qn.children);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getClass().hashCode() + children.hashCode();
	}

	/**
	 * Returns a new, empty node of this type.
	 * 
	 * @return new, empty node of this type
	 */
	protected abstract BooleanQueryNode newNode();

}