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


// $Id: StructuredQuery.java,v 1.6 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval;

/**
 * A XIRQL structured (Boolean-style) query, defined by a tree/graph on query
 * nodes.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:13 $
 * @since 2004-09-09
 */
public class StructuredQuery extends XIRQLQuery {

    /**
     * The query node.
     */
    protected QueryNode node;

    /**
     * Creates a new object.
     */
    public StructuredQuery() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param queryID query ID
     * @param node    query node
     * @param numDocs number of documents
     */
    public StructuredQuery(String queryID, QueryNode node, int numDocs) {
        super(queryID, numDocs);
        this.node = node;
    }

    /**
     * Returns the XIRQL query string.
     *
     * @return XIRQL query string
     */
    public String getXIRQL() {
        return node.toString();
    }

    /**
     * Returns the query root node.
     *
     * @return query root node
     */
    public QueryNode getNode() {
        return node;
    }

}
