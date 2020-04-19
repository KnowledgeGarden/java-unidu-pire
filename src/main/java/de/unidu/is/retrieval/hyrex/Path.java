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

// $Id: Path.java,v 1.7 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.hyrex;

import de.unidu.is.retrieval.ProbDoc;

import java.util.StringTokenizer;

/**
 * Represents a query result from HyREX. A path consists of a weight, a document
 * identifier, and a path. Here, the document identifier is represented as an
 * integer, the weight is a double, and the path is a string.
 *
 * @author Kai Grossjohann, Gudrun Fischer, Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:14 $
 * @since 2004-01-04 (early version from 2001/2002, now fused with ProbDoc)
 */
public class Path extends ProbDoc {

    /**
     * The path associated with the document.
     */
    protected String path;

    /**
     * Creates a new, empty instance.
     */
    public Path() {
    }

    /**
     * Creates a new instance.
     *
     * @param docID  document ID
     * @param path   document path
     * @param weight document weight
     */
    public Path(String docID, String path, double weight) {
        super(docID, weight);
        this.path = path;
    }

    /**
     * Creates a new instance from a HyREX string.
     * <p>
     * <p>
     * The argument string has the following format:
     * <code>5 0.42 4711 /foo[1]/bar[2]</code>
     * <p>
     * <p>
     * This is a string of four tab-separated fields. The first field (5) is an
     * index number (5 means the fifth item from the ranking list). The second
     * field (0.42) gives the weight and is a double between 0 and 1. The third
     * field (4711) is an integer and gives the document identifier. The fourth
     * field (/foo[1]/bar[2]) is a path specifying an XML node.
     *
     * @param query_result argument string
     */
    public Path(String query_result) {
        StringTokenizer tok = new StringTokenizer(query_result);
        if (!tok.hasMoreTokens())
            throw new IllegalArgumentException(
                    "wrong string format: missing index");
        tok.nextToken();
        if (!tok.hasMoreTokens())
            throw new IllegalArgumentException(
                    "wrong string format: missing weight");
        weight = Double.parseDouble(tok.nextToken());
        if (!tok.hasMoreTokens())
            throw new IllegalArgumentException(
                    "wrong string format: missing docid");
        docID = tok.nextToken();
        if (!tok.hasMoreTokens())
            throw new IllegalArgumentException(
                    "wrong string format: missing path");
        path = tok.nextToken();
    }

    /**
     * Returns the path.
     *
     * @return path
     * @deprecated Use {@link #getPath()}instead.
     */
    public String path() {
        return path;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return string representation
     */
    public String toString() {
        return weight + " " + docID + ":" + path;
    }

    /**
     * Returns the path.
     *
     * @return path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }
}