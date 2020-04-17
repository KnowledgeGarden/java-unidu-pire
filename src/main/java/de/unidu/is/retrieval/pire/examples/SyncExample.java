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

// $Id: SyncExample.java,v 1.6 2005/03/01 09:21:33 nottelma Exp $
package de.unidu.is.retrieval.pire.examples;

import de.unidu.is.retrieval.Async2SyncRetriever;
import de.unidu.is.retrieval.AsyncRetriever;
import de.unidu.is.retrieval.DocumentMismatchException;
import de.unidu.is.retrieval.DocumentNotFoundException;
import de.unidu.is.retrieval.DocumentNotStorableException;
import de.unidu.is.retrieval.IR;
import de.unidu.is.retrieval.IndexException;
import de.unidu.is.retrieval.Retriever;
import de.unidu.is.retrieval.Sync2AsyncRetriever;
import de.unidu.is.retrieval.UnsupportedQueryException;
import de.unidu.is.util.StopWatch;

/**
 * A simple example for asynchronous and synchronous retrievers.
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/01 09:21:33 $
 */
public class SyncExample {

	public static void main(String[] args) throws UnsupportedQueryException,
			DocumentNotFoundException, DocumentNotStorableException,
			IndexException, DocumentMismatchException {
		// init index
		IR ir = PIREExampleUtils.createIR();
		PIREExampleUtils.indexXMLDocuments(ir);

		// chain asynchronous and synchronous retrievers
		StopWatch watch = new StopWatch();
		watch.start();
		AsyncRetriever asyncRetriever = new Sync2AsyncRetriever(ir);
		Retriever retriever = new Async2SyncRetriever(asyncRetriever);
		PIREExampleUtils.retrieveAndPrint(retriever, PIREExampleUtils
				.getWSumQuery());
		watch.stop();
		System.out.println(watch);

		ir.close();
		System.exit(0);
	}

}