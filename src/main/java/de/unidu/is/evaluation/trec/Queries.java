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


// $Id: Queries.java,v 1.8 2005/02/25 14:33:29 nottelma Exp $
package de.unidu.is.evaluation.trec;

import de.unidu.is.retrieval.KeywordQuery;
import de.unidu.is.retrieval.XIRQLQuery;
import de.unidu.is.retrieval.XIRQLStringQuery;
import de.unidu.is.text.*;
import de.unidu.is.util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * A class with only static methods for working with TREC queries.
 * <p>
 * <p>
 * TREC queries are derived from topic descriptions stored on disk (directory
 * <code>topics</code> relative to the directory specified by the config
 * property <code>trec.path</code>). Each topic description is stored in one
 * file with the name <code>[topic
 * number]</code>.
 * <p>
 * <p>
 * Query ids have the form <code>[topic number]</code> or <code>[topic
 * number]_[modifier]</code>,
 * for using different queries (e.g., derived from different fields of the topic
 * description).
 * <p>
 * <p>
 * At the moment, TREC queries for topics 1 - 150 and 451 - 550 are supported
 * with three different types ("modifiers"):
 * <ul>
 * <li>Without modifier (e.g., 51): only field "description"</li>
 * <li>With modifier "_short": only field "title"</li>
 * <li>With modifier "_long": all fields (lines after tag lines plus field
 * "title")</li>
 * </ul>
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/02/25 14:33:29 $
 * @since 2002-04-11
 */
public class Queries {

    /**
     * Directory with the topic files.
     */
    private static File topicDir;

    /**
     * Minimum token length, <code>-1</code> denotes that the standard value
     * from the word splitter filter is used.
     */
    private static int length = -1;

    /**
     * Sets the directory with the topic files.
     *
     * @param topicDir directory with topic files.
     */
    public static void setTopicDir(File topicDir) {
        Queries.topicDir = topicDir;
    }

    /**
     * Returns the TREC query topic for the specified query id, removing a query
     * modifier.
     *
     * @param queryID query id
     * @return TREC query topic
     */
    public static int getQueryTopic(String queryID) {
        String q = queryID;
        int h = q.indexOf('_');
        if (h != -1)
            q = q.substring(0, h);
        try {
            return Integer.parseInt(q);
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return 0;
    }

    /**
     * Returns the TREC query modifier for the specified query id (without the
     * query topic number).
     *
     * @param queryID query id
     * @return TREC query modifier
     */
    public static String getQueryModifier(String queryID) {
        int h = queryID.indexOf('_');
        if (h != -1)
            return queryID.substring(h + 1);
        return "";
    }

    /**
     * Returns the query terms together with the term frequencies in the
     * specified query.
     *
     * @param queryID query id
     * @return list of tuples (0: term, 1: tf)
     */
    public static List getQueryTerms(String queryID) {
        // extract topic description
        String fn = "";
        int queryNum = getQueryTopic(queryID);
        if (queryNum >= 1 && queryNum <= 50)
            fn += "001-050";
        if (queryNum >= 51 && queryNum <= 100)
            fn += "051-100";
        if (queryNum >= 101 && queryNum <= 150)
            fn += "101-150";
        if (queryNum >= 451 && queryNum <= 500)
            fn += "451-500";
        if (queryNum >= 501 && queryNum <= 550)
            fn += "501-550";
        if (queryNum >= 700)
            fn += "cmu";
        // read topic file
        String mod = getQueryModifier(queryID);
        StringBuilder buf = new StringBuilder();
        BufferedReader in = null;
        try {
            if (topicDir == null) {
                final String key = "evaluation.trec.topics";
                String dir = Config.getString(key);
                if (dir == null)
                    dir = new FilePropertyMap("localconfig").getString(key);
                if (dir != null)
                    topicDir = new File(dir);
            }
            File file = new File(topicDir, fn);
            in = new BufferedReader(new FileReader(file));
            String line;
            boolean found = false;
            boolean num = false;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("<")) {
                    if (line.startsWith("<num>")) {
                        if (num) // finished
                            break;
                        // is correct topic?
                        num = line.endsWith(" " + queryNum)
                                || line.endsWith(" 0" + queryNum);
                    }
                    if (mod.isEmpty())
                        found = line.startsWith("<desc>"); // is
                    // description?
                    if ((mod.equals("short") || mod.equals("long")) && num
                            && line.startsWith("<title>")) {
                        line = line.substring(line.indexOf('>') + 1).trim();
                        final String TOPIC_PREFIX = "Topic:";
                        if (line.startsWith(TOPIC_PREFIX))
                            line = line.substring(TOPIC_PREFIX.length()).trim();
                        if (line.length() == 0)
                            line = in.readLine().trim();
                        buf.append(line);
                        buf.append(" ");
                    }
                    if (mod.equals("long"))
                        found = true;
                } else {
                    if (num && found) { // correct topic, is
                        // description?
                        buf.append(line);
                        buf.append(" ");
                    }
                }
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally { // close file
            try {
                if (in != null)
                    in.close();
            } catch (Exception ex) {
            }
        }
        return getTF(buf.toString());
    }

    /**
     * Returns the query terms together with the term frequencies in the
     * specified string.
     *
     * @param terms query terms, separated by spaces
     * @return list of tuples (0: term, 1: tf)
     */
    protected static List getTF(String terms) {
        PropertyMap map = new HashPropertyMap();
        Filter filter = new CounterFilter(new StemmerFilter(new StopwordFilter(
                new LowercaseFilter(new WordSplitterFilter(null, length) {
                    protected void handleBuffer(StringBuffer buffer) {
                        for (int i = 0; i < buffer.length(); i++) {
                            char c = buffer.charAt(i);
                            if (c != ' ' && !Character.isJavaIdentifierPart(c))
                                buffer.replace(i, i + 1, " ");
                        }
                    }
                }))));
        return CollectionUtilities.toList(filter.apply(terms));
    }

    /**
     * Returns the query for the specified query id.
     *
     * @param queryID  query id
     * @param attName  attribute (schema element or alias) name
     * @param operator operator name
     * @return query
     */
    public static KeywordQuery getQuery(String queryID, String attName,
                                        String operator) {
        return getQuery(queryID, null, attName, operator);
    }

    /**
     * Returns the query for the specified query id with the specified postfix.
     *
     * @param queryID  query id
     * @param postfix  query postfix, may be null
     * @param attName  attribute (schema element or alias) name
     * @param operator operator name
     * @return query
     */
    public static KeywordQuery getQuery(String queryID, String postfix,
                                        String attName, String operator) {
        StringBuilder termBuf = new StringBuilder();
        List terms = getQueryTerms(queryID);
        for (Object o : terms) {
            Tuple tuple = (Tuple) o;
            String term = tuple.getString(0);
            int tf = tuple.getInt(1);
            for (int i = 0; i < tf; i++)
                termBuf.append(term).append(" ");
        }
        KeywordQuery query = new KeywordQuery(queryID
                + (postfix == null ? "" : "-" + postfix), termBuf.toString()
                .trim(), 100);
        return query;
    }

    /**
     * Returns the query for the specified query id with the specified postfix.
     *
     * @param queryID  query id
     * @param postfix  query postfix, may be null
     * @param attName  attribute (schema element or alias) name
     * @param operator operator name
     * @return query
     */
    public static XIRQLQuery getXIRQLQuery(String queryID, String postfix,
                                           String attName, String operator) {
        StringBuilder xirql = new StringBuilder();
        List terms = getQueryTerms(queryID);
        double dl = 0;
        for (Object value : terms) {
            Tuple tuple = (Tuple) value;
            int tf = tuple.getInt(1);
            dl += tf;
        }
        for (Object o : terms) {
            Tuple tuple = (Tuple) o;
            String term = tuple.getString(0);
            int tf = tuple.getInt(1);
            if (xirql.length() > 0)
                xirql.append(",");
            xirql.append(tf / dl).append(",/#PCDATA $").append(attName).append(
                    ":").append(operator).append("$ \"").append(term).append(
                    "\"");
        }
        xirql.insert(0, "wsum(");
        xirql.append(")");
        XIRQLQuery query = new XIRQLStringQuery(queryID
                + (postfix == null ? "" : "-" + postfix), xirql.toString(), 100);
        return query;
    }

    /**
     * Returns the query for the specified query id.
     *
     * @param queryID  query id
     * @param attName  attribute (schema element or alias) name
     * @param operator operator name
     * @return query
     */
    public static XIRQLQuery getXIRQLQuery(String queryID, String attName,
                                           String operator) {
        return getXIRQLQuery(queryID, null, attName, operator);
    }

    /**
     * Returns the minumum token length.
     *
     * @return minumum token length
     */
    public static int getLength() {
        return length;
    }

    /**
     * Sets the minumum token length.
     *
     * @param length minumum token length
     */
    public static void setLength(int length) {
        Queries.length = length;
        Config.setInt("trec.queries.mintokenlength", length);
    }
}
