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

// $Id: Async2SyncRetriever.java,v 1.6 2005/03/01 09:20:45 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.List;

import org.w3c.dom.Document;

import de.unidu.is.util.AsyncCallResponseEvent;
import de.unidu.is.util.AsyncCallResponseListener;

/**
 * A synchronous variant of an asynchronous retriever.
 * 
 * @author Henrik Nottelmann
 * @since 13-Oct-2004
 * @version $Revision: 1.6 $, $Date: 2005/03/01 09:20:45 $
 */
public class Async2SyncRetriever implements Retriever {

	/**
	 * Listener.
	 * 
	 * @author Henrik Nottelmann
	 * @since 13-Oct-2004
	 */
	private static class Listener implements AsyncCallResponseListener {
		private Object response = null;
		private Exception exception = null;
		private boolean finished = false;
		public void resultEvent(AsyncCallResponseEvent event) {
			response = event.getResponse();
			finished = true;
			synchronized (this) {
				notifyAll();
			}
		}
		public void faultEvent(AsyncCallResponseEvent event) {
			exception = (Exception) event.getResponse();
			synchronized (this) {
				notify();
			}
			finished = true;
		}
		public Exception getException() {
			return exception;
		}
		public Object getResponse() {
			return response;
		}
		public boolean waitUntilReady() {
			while (!finished)
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					if (exception == null)
						exception = e;
					return false;
				}
			return exception == null;
		}
	}

	/**
	 * The asynchronous retriever.
	 */
	protected AsyncRetriever asyncRetriever;

	/**
	 * Creates a new object.
	 * 
	 * @param asyncRetriever
	 *                   asynchronous retriever
	 */
	public Async2SyncRetriever(AsyncRetriever asyncRetriever) {
		this.asyncRetriever = asyncRetriever;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getSchema()
	 */
	public Schema getSchema() {
		Listener listener = new Listener();
		asyncRetriever.getSchemaCall(listener);
		if (listener.waitUntilReady())
			return (Schema) listener.getResponse();
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getResult(de.unidu.is.retrieval.Query)
	 */
	public List getResult(Query query) throws UnsupportedQueryException,
			IndexException {
		Listener listener = new Listener();
		asyncRetriever.getResultCall(listener, query);
		if (listener.waitUntilReady())
			return (List) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof UnsupportedQueryException)
				throw (UnsupportedQueryException) ex;
			if (ex instanceof IndexException)
				throw (IndexException) ex;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getResultSummaries(de.unidu.is.retrieval.Query)
	 */
	public List getResultSummaries(Query query)
			throws UnsupportedQueryException, IndexException,
			DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getResultSummariesCall(listener, query);
		if (listener.waitUntilReady())
			return (List) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof UnsupportedQueryException)
				throw (UnsupportedQueryException) ex;
			if (ex instanceof IndexException)
				throw (IndexException) ex;
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getSummary(java.lang.String)
	 */
	public Document getSummary(String docID) throws DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getSummaryCall(listener, docID);
		if (listener.waitUntilReady())
			return (Document) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof DocumentNotFoundException)
				throw (DocumentNotFoundException) ex;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getSummary(de.unidu.is.retrieval.ProbDoc)
	 */
	public Document getSummary(ProbDoc doc) throws DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getSummaryCall(listener, doc);
		if (listener.waitUntilReady())
			return (Document) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof DocumentNotFoundException)
				throw (DocumentNotFoundException) ex;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getSummaries(java.util.List)
	 */
	public List getSummaries(List result) throws DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getSummariesCall(listener, result);
		if (listener.waitUntilReady())
			return (List) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof DocumentNotFoundException)
				throw (DocumentNotFoundException) ex;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getDocument(java.lang.String)
	 */
	public Document getDocument(String docID) throws DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getDocumentCall(listener, docID);
		if (listener.waitUntilReady())
			return (Document) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof DocumentNotFoundException)
				throw (DocumentNotFoundException) ex;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getDocument(de.unidu.is.retrieval.ProbDoc)
	 */
	public Document getDocument(ProbDoc doc) throws DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getDocumentCall(listener, doc);
		if (listener.waitUntilReady())
			return (Document) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof DocumentNotFoundException)
				throw (DocumentNotFoundException) ex;
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#getDocuments(java.util.List)
	 */
	public List getDocuments(List result) throws DocumentNotFoundException {
		Listener listener = new Listener();
		asyncRetriever.getDocumentsCall(listener, result);
		if (listener.waitUntilReady())
			return (List) listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof DocumentNotFoundException)
				throw (DocumentNotFoundException) ex;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.Retriever#close()
	 */
	public void close() throws IndexException {
		Listener listener = new Listener();
		asyncRetriever.closeCall(listener);
		if (listener.waitUntilReady())
			listener.getResponse();
		else {
			Exception ex = listener.getException();
			if (ex instanceof IndexException)
				throw (IndexException) ex;
		}
	}

}