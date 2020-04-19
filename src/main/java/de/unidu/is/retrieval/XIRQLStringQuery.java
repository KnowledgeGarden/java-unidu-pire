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


// $Id: XIRQLStringQuery.java,v 1.4 2005/02/21 17:29:23 huesselbeck Exp $
package de.unidu.is.retrieval;


/**
 * A XIRQL query defined by a string.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:23 $
 * @since 2005-01-03
 */
public class XIRQLStringQuery extends XIRQLQuery {

    /**
     * The XIRQL query string.
     */
    private String xirql;


    /**
     * Creates a new object.
     */
    public XIRQLStringQuery() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param queryID
     * @param xirql   XIRQL query
     * @param numDocs
     */
    public XIRQLStringQuery(String queryID, String xirql, int numDocs) {
        super(queryID, numDocs);
        setXIRQL(xirql);
    }

    /**
     * Returns the XIRQL query string.
     *
     * @return XIRQL query string
     */
    public String getXIRQL() {
        return xirql;
    }

    /**
     * Sets the XIRQL query string.
     *
     * @param xirql XIRQL query string
     */
    public void setXIRQL(String xirql) {
        this.xirql = xirql;
    }

    /**
     * Returns the xirql.
     *
     * @return xirql.
     */
    public String getXirql() {
        return xirql;
    }

    /**
     * Sets the xirql.
     *
     * @param xirql xirql to set.
     */
    public void setXirql(String xirql) {
        this.xirql = xirql;
    }
}
