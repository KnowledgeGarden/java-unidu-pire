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


// $Id: WSumQuery.java,v 1.6 2005/02/21 17:29:23 huesselbeck Exp $
package de.unidu.is.retrieval;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A XIRQL weighted-sum query.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:23 $
 * @since 2004-01-03
 */
public class WSumQuery extends XIRQLStringQuery {

    /**
     * Creates a new query.
     */
    public WSumQuery() {
        super();
    }

    /**
     * Creates a new query.
     *
     * @param queryID query ID
     * @param xirql   XIRQL query string
     * @param numDocs number of documents
     */
    public WSumQuery(String queryID, String xirql, int numDocs) {
        super(queryID, xirql, numDocs);
    }

    /**
     * Returns a list with the conditions in the stored wsum query.<p>
     *
     * @return list with the WeightedQueryCondition instances
     */
    public List getConditionsTuples() {
        List condList = new LinkedList();
        String str = getXIRQL();
        if (!str.startsWith("wsum("))
            str = "wsum(1," + str + ")";
        str = str.substring("wsum(".length());
        str = str.substring(0, str.length() - 1);
        StringTokenizer tok = new StringTokenizer(str, ",");
        while (tok.hasMoreTokens()) {
            String c = tok.nextToken() + "," + tok.nextToken();
            condList.add(new WeightedQueryCondition(c));
        }
        return condList;
    }

}
