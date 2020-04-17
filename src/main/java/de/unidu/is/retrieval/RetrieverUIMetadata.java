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

 
// $Id: RetrieverUIMetadata.java,v 1.4 2005/02/21 17:29:23 huesselbeck Exp $
package de.unidu.is.retrieval;

import java.util.List;

import org.w3c.dom.Document;

/**
 * This interface describes metadata for a retriever which can be used by 
 * UIs for a reasonable handling of queries and documents. 
 * 
 * @author Henrik Nottelmann
 * @since 2004-03-18
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:23 $
 */
public interface RetrieverUIMetadata {

	/**
	 * Returns the associated retriever.
	 * 
	 * @return retriever
	 */
	public abstract Retriever getRetriever();

	/**
	 * Returns the schema used by the retriever.
	 * 
	 * @return schema used by the retriever
	 */
	public abstract Schema getSchema();
	
	/**
	 * Returns all collections supported by the retriever.
	 * 
	 * @return attray of collection names, can be empty 
	 */
	public String[] getCollections();

	/**
	 * Returns the attributes names with could be used for searching.
	 * 
	 * @return attributes names with could be used for searching
	 */
	public abstract String[] getSortedSearchAttributeNames();
	
	/**
	 * Returns the attributes names with could be used for displaying details.
	 * 
	 * @return attributes names with could be used for displaying details
	 */
	public abstract String[] getSortedAttributeNames();
	
	/**
	 * Returns the attributes names with could be used for sorting.
	 * 
	 * @return attributes names with could be used for sorting
	 */
	public String[] getSortModel();
	
	/**
	 * Converts the specified attribute name into a human-readable label.
	 * 
	 * @param attName attribute name 
	 * @return human-readable label
	 */
	public String toHuman(String attName);
	
	/**
	 * Extracts the content of the specified XML document given by the 
	 * XPath expressions of the specified attribute. Multiple occurrences 
	 * are concatenated (with "; " as separator).
	 * 
	 * @param doc XML docoument
	 * @param attName attribute name
	 * @return extracted content
	 */
	public String extract(Document doc,String attName);

	/**
	 * Extracts the content of the specified XML document given by the 
	 * XPath expressions of the specified attribute. Each list entry equals one
	 * occurrence in the document. 
	 * 
	 * @param doc XML docoument
	 * @param attName attribute name
	 * @return extracted content
	 */
	public List extractList(Document doc,String attName);

	/**
	 * Returns all attribute names related to a potential title field.<p>
	 * 
	 * This can be used by a GUI for displaying the document title.
	 * 
	 * @return attribute names related to a potential title field
	 */
	public String[] getTitleAttributeNames();

	/**
	 * Returns all attribute names related to a potential author field.<p>
	 * 
	 * This can be used by a GUI for displaying the document authors.
	 * 
	 * @return attribute names related to a potential author field
	 */
	public String[] getAuthorAttributeNames();
	
	/**
	 * Returns all attribute names related to a potential year field.<p>
	 * 
	 * This can be used by a GUI for displaying years associated 
	 * with a document.
	 * 
	 * @return attribute names related to a potential year field
	 */
	public String[] getYearAttributeNames();

	/**
	 * Returns the collection which is encoded in the specified doc ID.
	 * 
	 * @param docID document ID
	 * @return collection encoded in the doc ID, or null (no collection)
	 */	
	public String getCollectionFromDocID(String docID);
	
}
