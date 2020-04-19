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


// $Id: KeywordQuery.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import de.unidu.is.util.StringUtilities;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A keyword-based query.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 * @since 2005-01-03
 */
public class KeywordQuery extends Query {

    /**
     * The keywords.
     */
    private List keywords;

    /**
     * Creates a new object.
     */
    public KeywordQuery() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param queryID
     * @param numDocs
     */
    public KeywordQuery(String queryID, int numDocs) {
        super(queryID, numDocs);
    }

    /**
     * Creates a new query.
     *
     * @param queryID query ID
     * @param kw      keywords
     * @param numDocs number of documents
     */
    public KeywordQuery(String queryID, String kw, int numDocs) {
        this();
        setQueryID(queryID);
        setKeywords(parseKeywords(kw));
        setNumDocs(numDocs);
    }

    /**
     * Creates a new query.
     *
     * @param queryID  query ID
     * @param keywords keywords
     * @param numDocs  number of documents
     */
    public KeywordQuery(String queryID, List keywords, int numDocs) {
        this();
        setQueryID(queryID);
        setKeywords(keywords);
        setNumDocs(numDocs);
    }

    /**
     * Parses the keywords and returns them.
     *
     * @param kw keywords, separated by spaces
     * @return keywords
     */
    private static List parseKeywords(String kw) {
        List list = new LinkedList();
        for (StringTokenizer tok = new StringTokenizer(kw, " "); tok
                .hasMoreTokens(); ) {
            String term = tok.nextToken();
            list.add(term);
        }
        return list;
    }

    /**
     * Returns the keywords.
     *
     * @return keywords
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * Sets the keywords.
     *
     * @param keywords keywords to set.
     */
    public void setKeywords(List keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns the keywords as a string.
     *
     * @return keywords as a string
     */
    public String getKeywordString() {
        return StringUtilities.implode(keywords, " ");
    }

    /*
     *  (non-Javadoc)
     * @see de.unidu.is.retrieval.Query#toStringAdditional()
     */
    protected String toStringAdditional() {
        return "Keywords=" + getKeywordString();
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Query))
            return false;
        KeywordQuery q = (KeywordQuery) obj;
        return super.equals(obj) && q.getKeywords().equals(getKeywords());
    }

}
