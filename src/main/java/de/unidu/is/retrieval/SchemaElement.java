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

// $Id: SchemaElement.java,v 1.8 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A schema element, having a name, corresponding to a datatype, and having a
 * (potentially empty) list of operator names (from the list of operators
 * supported by the corresponding datatype).
 * 
 * @author Henrik Nottelmann
 * @since 2004-04-10
 * @version $Revision: 1.8 $, $Date: 2005/03/14 17:33:13 $
 */
public class SchemaElement {

	/**
	 * The schema element name.
	 *  
	 */
	protected String name;

	/**
	 * The schema element datatype.
	 *  
	 */
	protected String datatypeName;

	/**
	 * The list of operators (as strings) for this schema element.
	 *  
	 */
	protected List operators;

	/**
	 * The element children.
	 *  
	 */
	protected List children;

	/**
	 * Creates a new, empty schema element.
	 *  
	 */
	public SchemaElement() {
		this(null);
	}

	/**
	 * Creates a new schema element.
	 * 
	 * @param name
	 *                   name of the schema element
	 */
	public SchemaElement(String name) {
		this(name, null, new LinkedList());
	}

	/**
	 * Creates a new schema element.
	 * 
	 * @param name
	 *                   name of the schema element
	 * @param datatypeName
	 *                   datatype of the schema element
	 * @param operators
	 *                   list of operators (as strings)
	 */
	public SchemaElement(String name, String datatypeName, List operators) {
		this.name = name;
		this.datatypeName = datatypeName;
		this.operators = operators;
		this.children = new LinkedList();
	}

	/**
	 * Returns the name of this schema element.
	 * 
	 * @return name of this schema element
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this schema element.
	 * 
	 * @param name
	 *                   name of this schema element
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the the datatype name.
	 * 
	 * @return datatype name
	 */
	public String getDatatypeName() {
		return datatypeName;
	}

	/**
	 * Sets the datatype name.
	 * 
	 * @param datatypeName
	 *                   datatype name
	 */
	public void setDatatypeName(String datatypeName) {
		this.datatypeName = datatypeName;
	}

	/**
	 * Returns the list of schema element operators.
	 * 
	 * @return list of schema element operators (as strings)
	 */
	public List getOperators() {
		return operators;
	}

	/**
	 * Sets the list of schema element operators.
	 * 
	 * @param operators
	 *                   list of schema element operators (as strings)
	 */
	public void setOperators(List operators) {
		this.operators = operators;
	}

	/**
	 * Adds the specified schema element as a child.
	 * 
	 * @param element
	 *                   new child element
	 */
	public boolean add(SchemaElement element) {
		return children.add(element);
	}

	/**
	 * Adds all schema elements in the specified collection to the children set.
	 *  
	 * @param c collection with schema elements
	 */
	public boolean addAll(Collection c) {
		return children.addAll(c);
	}

	/**
	 * Removes all children.
	 */
	public void clear() {
		children.clear();
	}

	/**
	 * Returns true iff the specified element is a child.
	 * 
	 * @param element true iff the specified element is a child
	 */
	public boolean contains(SchemaElement element) {
		return children.contains(element);
	}

	/**
	 * Returns the i-th child.
	 * 
	 * @param index index number
	 * @return i-th element child
	 */
	public SchemaElement get(int index) {
		return (SchemaElement) children.get(index);
	}

	/**
	 * Returns true iff there is no child.
	 * 
	 * @return true iff there is no child
	 */
	public boolean isEmpty() {
		return children.isEmpty();
	}

	/**
	 * Returns an iterator over the children.
	 * 
	 * @return iterator over children
	 */
	public Iterator children() {
		return children.iterator();
	}

	/**
	 * Removes the specified schema element as a child.
	 * 
	 * @param element
	 *                   element to be removed
	 */
	public boolean remove(SchemaElement element) {
		return children.remove(element);
	}

	/**
	 * Returns the number of direct children.
	 * 
	 * @return number of direct children.
	 */
	public int childrenCount() {
		return children.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof SchemaElement)
			return ((SchemaElement) obj).getName().equals(name);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + "[ "
				+ (datatypeName != null ? datatypeName + operators + " " : " ")
				+ children + " ]";
	}

	/**
	 * Returns all XPath expressions for this schema subtree.
	 * 
	 * @return
	 */
	public List getXPaths() {
		if (children == null || childrenCount() == 0)
			return Collections.singletonList("/" + name);
		List ret = new LinkedList();
		for (Iterator it = children(); it.hasNext();) {
			SchemaElement element = (SchemaElement) it.next();
			for (Iterator it2 = element.getXPaths().iterator(); it2.hasNext();) {
				String path = (String) it2.next();
				ret.add("/" + name + path);
			}
		}
		return ret;
	}

	/**
	 * Returns the schema element specified by a XPath.
	 * 
	 * @param xpath
	 *                   XPath
	 * @return corresponding element or null, of not existing
	 */
	public SchemaElement getElement(String xpath) {
		if (xpath.equals("/" + name))
			return this;
		if (xpath.startsWith("/" + name + "/")) {
			xpath = xpath.substring(xpath.indexOf("/", 1));
			for (Iterator it = children(); it.hasNext();) {
				SchemaElement element = (SchemaElement) it.next();
				SchemaElement ret = element.getElement(xpath);
				if (ret != null)
					return ret;
			}
		}
		return null;
	}

	/**
	 * Appends a description of this schema element to the hierarchical view.
	 * 
	 * @param buf
	 *                   string buffer for the hierarchical view
	 * @param depth
	 *                   element depth
	 */
	public void appendHierarchicalView(StringBuffer buf, int depth) {
		for (int i = 0; i < depth; i++)
			buf.append("    ");
		buf.append(getName()).append(" (Datatype: ").append(getDatatypeName())
				.append(", Operators: ").append(getOperators()).append(")\n");
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			SchemaElement element = (SchemaElement) iter.next();
			element.appendHierarchicalView(buf, depth + 1);
		}
	}

}