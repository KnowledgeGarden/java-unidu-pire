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

// $Id: Sync2AsyncRetriever.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.List;

import de.unidu.is.util.AsyncCallResponseEvent;
import de.unidu.is.util.AsyncCallResponseListener;

/**
 * An asynchronous interface to an existing synchronous retriever object.
 * 
 * @author Henrik Nottelmann
 * @since 12-Oct-2004
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 */
public class Sync2AsyncRetriever implements AsyncRetriever {

	/**
	 * Retriever object to be used.
	 */
	protected Retriever retriever;

	/**
	 * Creates a new object.
	 * 
	 * @param retriever
	 *                   retriever object to be used
	 */
	public Sync2AsyncRetriever(Retriever retriever) {
		this.retriever = retriever;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getSchemaCall(de.unidu.is.util.AsyncCallResponseListener)
	 */
	public void getSchemaCall(final AsyncCallResponseListener listener) {
		new Thread() {
			public void run() {
				listener.resultEvent(new AsyncCallResponseEvent(
						Sync2AsyncRetriever.this, retriever.getSchema()));
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getDocumentCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           de.unidu.is.retrieval.ProbDoc)
	 */
	public void getDocumentCall(final AsyncCallResponseListener listener,
			final ProbDoc probDoc) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getDocument(probDoc)));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getDocumentCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           java.lang.String)
	 */
	public void getDocumentCall(final AsyncCallResponseListener listener,
			final String docID) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getDocument(docID)));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getDocumentsCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           java.util.List)
	 */
	public void getDocumentsCall(final AsyncCallResponseListener listener,
			final List docList) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getDocuments(docList)));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getResultCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           de.unidu.is.retrieval.Query)
	 */
	public void getResultCall(final AsyncCallResponseListener listener,
			final Query query) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getResult(query)));
				} catch (UnsupportedQueryException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				} catch (IndexException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getResultSummariesCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           de.unidu.is.retrieval.Query)
	 */
	public void getResultSummariesCall(
			final AsyncCallResponseListener listener, final Query query) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getResultSummaries(query)));
				} catch (UnsupportedQueryException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				} catch (IndexException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getSummariesCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           java.util.List)
	 */
	public void getSummariesCall(final AsyncCallResponseListener listener,
			final List docs) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getSummaries(docs)));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getSummaryCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           de.unidu.is.retrieval.ProbDoc)
	 */
	public void getSummaryCall(final AsyncCallResponseListener listener,
			final ProbDoc probDoc) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getSummary(probDoc)));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#getSummaryCall(de.unidu.is.util.AsyncCallResponseListener,
	 *           java.lang.String)
	 */
	public void getSummaryCall(final AsyncCallResponseListener listener,
			final String docID) {
		new Thread() {
			public void run() {
				try {
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, retriever
									.getSummary(docID)));
				} catch (DocumentNotFoundException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.retrieval.AsyncRetriever#closeCall(de.unidu.is.util.AsyncCallResponseListener)
	 */
	public void closeCall(final AsyncCallResponseListener listener) {
		new Thread() {
			public void run() {
				try {
					retriever.close();
					listener.resultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this));
				} catch (IndexException e) {
					listener.faultEvent(new AsyncCallResponseEvent(
							Sync2AsyncRetriever.this, e));
				}
			}
		}.start();
	}
}