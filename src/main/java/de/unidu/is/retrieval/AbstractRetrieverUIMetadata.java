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

 
// $Id: AbstractRetrieverUIMetadata.java,v 1.9 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import de.unidu.is.util.StringUtilities;

/**
 * An abstract class describing metadata for a MIND retriever which can be 
 * used by UIs for a reasonable handling of queries and documents. 
 * 
 * TODO: getAliases instead of getXPath?
 * TODO: comments
 * 
 * @author Henrik Nottelmann
 * @since 2004-03-18
 * @version $Revision: 1.9 $, $Date: 2005/02/28 22:27:55 $
 */
public abstract class AbstractRetrieverUIMetadata
	implements RetrieverUIMetadata {

	/**
	 * 
	 */
	protected Retriever retriever;

	/**
	 * 
	 */
	protected Schema schema;

	protected String[] sortedSearchAttributes;
	protected String[] sortedAttributes;
	protected String[] sortModel;
	protected String[] collections;

	/**
	 * 
	 */
	public AbstractRetrieverUIMetadata(Retriever retriever) {
		this.retriever = retriever;
		this.schema = retriever.getSchema();
	}

	/**
	 * Returns the associated retriever.
	 * 
	 * @return retriever
	 * @see de.unidu.is.retrieval.RetrieverUIMetadata#getRetriever()
	 */
	public Retriever getRetriever() {
		return retriever;
	}

	/**
	 * Returns the schema used by the retriever.
	 * 
	 * @return schema used by the retriever
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * Returns the attributes names with could be used for searching.<p>
	 * 
	 * This implementation returns all attributes with a search predicate
	 * in the order of appearence in the schema.
	 * 
	 * @return attributes names with could be used for searching
	 */
	public String[] getSortedSearchAttributeNames() {
		if (sortedSearchAttributes == null) {
			List attributes = new ArrayList(schema.getXPaths());
			List l = new LinkedList();
			for (Iterator it = attributes.iterator(); it.hasNext();) {
				String xpath = (String) it.next();
				if (!getSchema().getElement(xpath).getOperators().isEmpty()) {
					String alias = schema.getAlias(xpath);
					l.add(alias != null ? alias : xpath);
				}
			}
			sortedSearchAttributes = (String[]) l.toArray(new String[l.size()]);
		}
		return sortedSearchAttributes;
	}

	/**
	 * Returns the attributes names with could be used for displaying details.<p>
	 * 
	 * This implementation returns all attributes in the order of 
	 * appearence in the schema.
	 * 
	 * @return attributes names with could be used for displaying details
	 */
	public String[] getSortedAttributeNames() {
		if (sortedAttributes == null) {
			List attributes = new ArrayList(schema.getXPaths());
			List l = new LinkedList();
			for (Iterator it = attributes.iterator(); it.hasNext();) {
				String xpath = (String) it.next();
				String alias = schema.getAlias(xpath);
				l.add(alias != null ? alias : xpath);
			}
			sortedAttributes = (String[]) l.toArray(new String[l.size()]);
		}
		return sortedAttributes;
	}

	/**
	 * Returns the attributes names with could be used for sorting.<p>
	 * 
	 * This implementation returns all attributes with a search predicate 
	 * (except "image") in the order of appearence in the schema.
	 * 
	 * @return attributes names with could be used for sorting
	 * @see de.unidu.is.retrieval.RetrieverUIMetadata#getSortModel()
	 */
	public String[] getSortModel() {
		if (sortModel == null) {
			List attributes = new ArrayList(schema.getXPaths());
			List l = new LinkedList();
			for (Iterator it = attributes.iterator(); it.hasNext();) {
				String xpath = (String) it.next();
				if (xpath.equals("image"))
					continue;
				String alias = schema.getAlias(xpath);
				l.add(alias != null ? alias : xpath);
			}
			sortModel = (String[]) l.toArray(new String[l.size()]);
		}
		return sortModel;
	}

	/**
	 * Converts the specified attribute name into a human-readable label.<p>
	 * 
	 * This implementation converts the first character into upper-case, 
	 * and replaces hyphens by spaces.
	 * 
	 * @param attName attribute name 
	 * @return human-readable label
	 */
	public String toHuman(String attName) {
		return attName.substring(0, 1).toUpperCase()
			+ StringUtilities.replace(attName.substring(1), "-", " ");
	}

	/**
	 * Extracts the content of the specified XML document given by the 
	 * XPath expressions of the specified attribute. Multiple occurrences 
	 * are concatenated (with "; " as separator).
	 * 
	 * @param doc XML docoument
	 * @param attName attribute name
	 * @return extracted content
	 */
	public String extract(Document doc, String attName) {
		return StringUtilities.implode(extractList(doc, attName), "; ");
	}

	/**
	 * Extracts the content of the specified XML document given by the 
	 * XPath expressions of the specified attribute. Each list entry equals one
	 * occurrence in the document. 
	 * 
	 * @param doc XML docoument
	 * @param attName attribute name
	 * @return extracted content
	 */
	public List extractList(Document doc, String attName) {
		String xpath = schema.getXPath(attName);
		List l = new LinkedList();
		try {
			NodeIterator ni = XPathAPI.selectNodeIterator(doc, xpath);
			for (Node n;(n = ni.nextNode()) != null;)
				l.add(n.getNodeValue().trim());
		} catch (TransformerException e) {
			de.unidu.is.util.Log.error(e);
		}
		return l;
	}

	/**
	 * Returns the collection which is encoded in the specified doc ID.
	 * 
	 * @param docID document ID
	 * @return collection encoded in the doc ID, or null (no collection)
	 */
	public String getCollectionFromDocID(String docID) {
		return null;
	}

}
