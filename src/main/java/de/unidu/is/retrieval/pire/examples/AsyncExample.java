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

// $Id: AsyncExample.java,v 1.6 2005/03/01 09:21:33 nottelma Exp $
package de.unidu.is.retrieval.pire.examples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unidu.is.retrieval.AsyncRetriever;
import de.unidu.is.retrieval.DocumentMismatchException;
import de.unidu.is.retrieval.DocumentNotFoundException;
import de.unidu.is.retrieval.DocumentNotStorableException;
import de.unidu.is.retrieval.IR;
import de.unidu.is.retrieval.IndexException;
import de.unidu.is.retrieval.Sync2AsyncRetriever;
import de.unidu.is.retrieval.UnsupportedQueryException;
import de.unidu.is.util.AsyncCallResponseEvent;
import de.unidu.is.util.AsyncCallResponseListener;
import de.unidu.is.util.StopWatch;

/**
 * A simple example for asynchronous retrievers.
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/01 09:21:33 $
 */
public class AsyncExample {

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
		final Set set = new HashSet();
		asyncRetriever.getResultCall(new AsyncCallResponseListener() {
			public void resultEvent(AsyncCallResponseEvent event) {
				System.out.println("Result: " + event.getResponse());
				set.add(event.getResponse());
			}
			public void faultEvent(AsyncCallResponseEvent event) {
				System.err.println(event.getResponse());
			}
		}, PIREExampleUtils.getWSumQuery());
		while (set.isEmpty())
			System.out.println("Waiting for response ...");
		PIREExampleUtils.printResult(ir, (List) set.iterator().next());
		watch.stop();
		System.out.println(watch);

		ir.close();
		System.exit(0);
	}

}