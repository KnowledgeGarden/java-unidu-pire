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

 
// $Id: AsyncRetriever.java,v 1.6 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.List;

import de.unidu.is.util.AsyncCallResponseListener;

/**
 * An asynchronous variant of <codeRetriever</code>, an interface for
 * abstracting from different XML-based IR engines (e.g. the XML PIRE extension,
 * HyREX). This interface only supports the retrieval part.
 * 
 * @author Henrik Nottelmann
 * @since 2004-10-12
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:13 $
 */
public interface AsyncRetriever {

	/**
	 * Asynchronous variant of getSchema(), which returns the schema used by
	 * this retriever.
	 * 
	 * @param listener
	 *                   response listener
	 */
	public void getSchemaCall(AsyncCallResponseListener listener);

	/**
	 * Asynchronous variant of getResult
	 * 
	 * @param listener
	 *                   response listener
	 * @param query
	 *                   XIRQL query
	 */
	public void getResultCall(AsyncCallResponseListener listener, Query query);

	/**
	 * Asynchronous variant of getResultSummaries
	 * 
	 * @param listener
	 *                   response listener
	 * @param query
	 *                   XIRQL query
	 */
	public void getResultSummariesCall(AsyncCallResponseListener listener,
			Query query);

	/**
	 * Asynchronous variant of getSummary
	 * 
	 * @param listener
	 *                   response listener
	 * @param docID
	 *                   document id
	 */
	public void getSummaryCall(AsyncCallResponseListener listener, String docID);

	/**
	 * Asynchronous variant of getSummary
	 * 
	 * @param listener
	 *                   response listener
	 * @param probDoc
	 *                   document
	 */
	public void getSummaryCall(AsyncCallResponseListener listener,
			ProbDoc probDoc);

	/**
	 * Asynchronous variant of getSummaries.
	 * 
	 * @param listener
	 *                   response listener
	 * @param docs
	 *                   list of ProbDoc instances
	 */
	public void getSummariesCall(AsyncCallResponseListener listener, List docs);

	/**
	 * Asynchronous variant of getDocument.
	 * 
	 * @param listener
	 *                   response listener
	 * @param docID
	 *                   document id
	 */
	public void getDocumentCall(AsyncCallResponseListener listener, String docID);

	/**
	 * Asynchronous variant of getDocument.
	 * 
	 * @param probDoc
	 *                   document descriptor
	 */
	public void getDocumentCall(AsyncCallResponseListener listener,
			ProbDoc probDoc);

	/**
	 * Asynchronous variant of getDocuments.
	 * 
	 * @param listener
	 *                   response listener
	 * @param docList
	 *                   list of ProbDoc instances
	 */
	public void getDocumentsCall(AsyncCallResponseListener listener,
			List docList);

	/**
	 * Asynchronous variant of getClose(), which closes the retriever, and
	 * optionally frees used system resources (e.g. closes a network
	 * connection).
	 * 
	 * @param listener
	 *                   response listener
	 */
	public void closeCall(AsyncCallResponseListener listener);

}
