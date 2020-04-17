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

 
// $Id: AbstractRetriever.java,v 1.4 2005/02/21 17:29:22 huesselbeck Exp $
package de.unidu.is.retrieval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;

/**
 * An abstract gateway to retrievers.
 * 
 * @author Henrik Nottelmann
 * @since 2004-03-18
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:22 $
 */
public abstract class AbstractRetriever implements Retriever {

	/**
	 * The retriever schema.
	 * 
	 */
	protected Schema schema;

	/**
	 * Creates a new instance.
	 * 
	 */
	public AbstractRetriever() {
	}

	/**
	 * Returns the schema used by this retriever.
	 * 
	 * @return schema used by this retriever
	 * @see de.unidu.is.retrieval.Retriever#getSchema()
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * Returns the full XML document
	 * 
	 * @param doc document descriptor
	 * @return full XML document, or null if it does not exist
	 * @see de.unidu.is.retrieval.Retriever#getDocument(de.unidu.is.retrieval.ProbDoc)
	 */
	public Document getDocument(ProbDoc doc) throws DocumentNotFoundException {
		return getDocument(doc.getDocID());
	}

	/**
	 * Returns the full XML documents.
	 * 
	 * @param result list of ProbDoc instances
	 * @return list of XMLDoc instances
	 */
	public List getDocuments(List result) throws DocumentNotFoundException {
		List ret = new ArrayList();
		for (Iterator it = result.iterator(); it.hasNext();) {
			ProbDoc doc = (ProbDoc) it.next();
			String docID = doc.getDocID();
			ret.add(new XMLDoc(docID, getDocument(docID)));
		}
		return ret;
	}

	/**
	 * Returns a summary of the XML document.
	 * 
	 * @param docID document id
	 * @return summary of  XML document
	 * @see de.unidu.is.retrieval.Retriever#getSummary(java.lang.String)
	 */
	public Document getSummary(String docID) throws DocumentNotFoundException {
		return getDocument(docID);
	}

	/**
	 * Returns the full XML document
	 * 
	 * @param doc document descriptor
	 * @return full XML document, or null if it does not exist
	 * @see de.unidu.is.retrieval.Retriever#getDocument(de.unidu.is.retrieval.ProbDoc)
	 */
	public Document getSummary(ProbDoc doc) throws DocumentNotFoundException {
		return getSummary(doc.getDocID());
	}

	/**
	 * Returns the full XML documents.
	 * 
	 * @param result list of ProbDoc instances
	 * @return list of XMLDoc instances
	 */
	public List getSummaries(List result) throws DocumentNotFoundException {
		List ret = new ArrayList();
		for (Iterator it = result.iterator(); it.hasNext();) {
			ProbDoc doc = (ProbDoc) it.next();
			String docID = doc.getDocID();
			ret.add(new XMLDoc(docID, getSummary(docID)));
		}
		return ret;
	}

	/**
	  * Returns a summarised result for the specified query.
	  *
	  * @param query XIRQL query
	  * @return list of XMLDoc instances
	  * @see de.unidu.is.retrieval.Retriever#getResultSummaries(de.unidu.is.retrieval.Query)
	  */
	public List getResultSummaries(Query query)
		throws UnsupportedQueryException, IndexException, DocumentNotFoundException {
		List r = getResult(query);
		List result = new ArrayList();
		for (Iterator it = r.iterator(); it.hasNext();) {
			ProbDoc pd = (ProbDoc) it.next();
			result.add(new XMLDoc(pd, getSummary(pd)));
		}
		return result;
	}

	/**
	 * Closes the retriever, and optionally frees used system resources 
	 * (e.g. closes a network connection).
	 * 
	 * @see de.unidu.is.retrieval.Retriever#close()
	 */
	public void close() throws IndexException {
	}

}
