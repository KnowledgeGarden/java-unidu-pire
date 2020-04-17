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

 
// $Id: IR.java,v 1.8 2005/02/21 17:29:22 huesselbeck Exp $
package de.unidu.is.retrieval;

import org.w3c.dom.Document;

/**
 * An interface for abstracting from different XML-based IR engines (e.g. the 
 * XML PIRE extension, HyREX). This interface supports both creating an index
 * as well as querying.
 * 
 * @author Henrik Nottelmann
 * @since 2003-12-16
 * @version $Revision: 1.8 $, $Date: 2005/02/21 17:29:22 $
 */
public interface IR extends Retriever {

	/**
	 * Registers the specified schema.
	 * 
	 * @param schema collection schema
	 */
	public void registerSchema(Schema schema);

	/*
	 * Start: Indexing
	 *
	 * - initIndex()
	 * - for every document:
	 *   addToIndex(String,Document)
	 * - computeIndex()
	 */

	/**
	 * Inits the index.
	 *
	 */
	public abstract void initIndex() throws IndexException;

	/**
	 * Add the XML document to the index.
	 *
	 * @param docID document id
	 * @param document XML document
	 */
	public abstract void addToIndex(String docID, Document document)
		throws
			IndexException, DocumentNotStorableException, DocumentMismatchException;

	/**
	 * Add the XML document to the index.
	 *
	 * @param docID document id
	 * @param document XML document
	 */
	public abstract void addToIndex(String docID, String document)
		throws
			IndexException, DocumentNotStorableException, DocumentMismatchException;

	/**
	 * Computes the index, based on the document values which were added 
	 * before.
	 * 
	 */
	public abstract void computeIndex() throws IndexException;

	/**
	 * Removes the index.
	 * 
	 */
	public abstract void removeIndex() throws IndexException;
}
