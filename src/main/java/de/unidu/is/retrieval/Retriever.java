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


// $Id: Retriever.java,v 1.5 2005/02/21 17:29:22 huesselbeck Exp $
package de.unidu.is.retrieval;

import org.w3c.dom.Document;

import java.util.List;

/**
 * An interface for abstracting from different XML-based IR engines (e.g. the
 * XML PIRE extension, HyREX). This interface only supports the
 * retrieval part.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:22 $
 * @since 2004-03-14
 */
public interface Retriever {

    /**
     * Returns the schema used by this retriever.
     *
     * @return schema used by this retriever
     */
    Schema getSchema();

    /**
     * Returns result for the specified query.
     *
     * @param query XIRQL query
     * @return list of ProbDoc instances
     */
    List getResult(Query query)
            throws UnsupportedQueryException, IndexException;

    /**
     * Returns a summarised result for the specified query.
     *
     * @param query XIRQL query
     * @return list of XMLDoc instances
     */
    List getResultSummaries(Query query)
            throws UnsupportedQueryException, IndexException, DocumentNotFoundException;

    /**
     * Returns a summary of the XML document.
     *
     * @param docID document id
     * @return summary of  XML document
     */
    Document getSummary(String docID)
            throws DocumentNotFoundException;

    /**
     * Returns a summary of the XML document.
     *
     * @param doc document descriptor
     * @return summary of XML document
     */
    Document getSummary(ProbDoc doc)
            throws DocumentNotFoundException;

    /**
     * Returns summaries of the XML documents.
     *
     * @param result list of ProbDoc instances
     * @return list of XMLDoc instances
     */
    List getSummaries(List result)
            throws DocumentNotFoundException;

    /**
     * Returns the full XML document.
     *
     * @param docID document id
     * @return full XML document
     */
    Document getDocument(String docID)
            throws DocumentNotFoundException;

    /**
     * Returns the full XML document.
     *
     * @param doc document descriptor
     * @return full XML document
     */
    Document getDocument(ProbDoc doc)
            throws DocumentNotFoundException;

    /**
     * Returns the full XML documents.
     *
     * @param result list of ProbDoc instances
     * @return list of XMLDoc instances
     */
    List getDocuments(List result)
            throws DocumentNotFoundException;

    /**
     * Closes the retriever, and optionally frees used system resources
     * (e.g. closes a network connection).
     */
    void close() throws IndexException;

}
