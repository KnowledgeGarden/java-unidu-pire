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

// $Id: HyREXRetriever.java,v 1.10 2005/02/28 22:27:56 nottelma Exp $
package de.unidu.is.retrieval.hyrex;

import de.unidu.is.retrieval.*;
import de.unidu.is.util.XMLUtilities;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An retrieval interface to a HyREX server, supporting only querying a HyREX
 * server.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.10 $, $Date: 2005/02/28 22:27:56 $
 * @since 2004-03-14
 */
public class HyREXRetriever extends AbstractRetriever {

    /**
     * The low-level, multi-purpose HyREX client.
     */
    protected final HyREXClient client;

    /**
     * Creates a new HyREX instance.
     *
     * @param hostname name of the HyREX server host
     * @param port     port of the HyREX server host
     * @param db       HyREX database
     * @param cls      HyREX class
     */
    public HyREXRetriever(String hostname, int port, String db, String cls) {
        client = new HyREXClient(hostname, port, db, cls);
    }

    /**
     * Creates a new HyREX instance.
     *
     * @param hostname name of the HyREX server host
     * @param port     port of the HyREX server host
     * @param db       HyREX database
     * @param cls      HyREX class
     * @param schema   schema
     */
    public HyREXRetriever(String hostname, int port, String db, String cls,
                          Schema schema) {
        this(hostname, port, db, cls);
        this.schema = schema;
    }

    /**
     * Returns result for the specified query.
     *
     * @param query XIRQL query
     * @return list of ProbDoc instances
     */
    public List getResult(Query query) throws IndexException,
            UnsupportedQueryException {
        if (!(query instanceof XIRQLQuery))
            throw new UnsupportedQueryException(query.toString());
        XIRQLQuery xirql = (XIRQLQuery) query;
        List result = new ArrayList();
        try {
            client.commandIgnoreResult("hits " + xirql.getNumDocs());
            List pathList = client.find(xirql.getXIRQL());
            pathList = pathList.subList(1, pathList.size());
            // Path is subclass of ProbDoc, so pathList content can be reused
            result.addAll(pathList);
        } catch (IOException e) {
            throw new IndexException(e.getMessage());
        }
        return result;
    }

    /**
     * Returns the full XML document
     *
     * @param docID document id
     * @return full XML document, or null if it does not exist
     * @see de.unidu.is.retrieval.IR#getDocument(java.lang.String)
     */
    public Document getDocument(String docID) throws DocumentNotFoundException {
        try {
            return XMLUtilities.parseText(client.document_id(docID));
        } catch (Exception e) {
        }
        throw new DocumentNotFoundException(docID);
    }

    /**
     * Closes the retriever and the corresponding HyREX connection.
     */
    public void close() throws IndexException {
        try {
            client.close();
        } catch (IOException e) {
            throw new IndexException(
                    "Error while closing the HyREX connection", e);
        }
    }

}