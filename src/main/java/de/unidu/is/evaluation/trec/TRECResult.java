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


// $Id: TRECResult.java,v 1.7 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.evaluation.trec;

import de.unidu.is.retrieval.ProbDoc;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A class for writing and reading TREC result files.<p>
 * <p>
 * The result file must have the format
 * <code>[query id] * [document id] * [weight] *</code>
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/28 22:27:55 $
 * @since 2004-01-06
 */
public class TRECResult {

    /**
     * Writes the specified result in the specified file (TREC format).
     *
     * @param file    file for the result
     * @param queryID query ID
     * @param result  list of ProbDoc instances
     */
    public static void writeResult(File file, String queryID, List result) {
        writeResult(file, queryID, result, false);
    }

    /**
     * Writes the specified result in the specified file (TREC format).
     *
     * @param file    file for the result
     * @param queryID query ID
     * @param result  list of ProbDoc instances
     * @param append  if true, appends the data
     */
    public static void writeResult(
            File file,
            String queryID,
            List result,
            boolean append) {
        writeResult(file, queryID, result, append, false);
    }

    /**
     * Writes the specified result in the specified file (TREC format).
     *
     * @param file                   file for the result
     * @param queryID                query ID
     * @param result                 list of ProbDoc instances
     * @param append                 if true, appends the data
     * @param dummyLineIfEmptyResult if true, prints a dummy line if the result is empty
     */
    public static void writeResult(
            File file,
            String queryID,
            List result,
            boolean append,
            boolean dummyLineIfEmptyResult) {
        try {
            writeResult(new FileWriter(file, append), queryID, result, dummyLineIfEmptyResult);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Writes the specified result in the specified file (TREC format).
     *
     * @param writer                 stream for the result
     * @param queryID                query ID
     * @param result                 list of ProbDoc instances
     * @param dummyLineIfEmptyResult if true, prints a dummy line if the result is empty
     */
    public static void writeResult(
            Writer writer,
            String queryID,
            List result,
            boolean dummyLineIfEmptyResult) {
        PrintWriter pw = new PrintWriter(writer);
        if (dummyLineIfEmptyResult && result.isEmpty())
            pw.println(queryID + " 0 xxx 1 1 1");
        for (Object o : result) {
            ProbDoc doc = (ProbDoc) o;
            String docID = doc.getDocID();
            double weight = doc.getWeight();
            pw.println(queryID + " 0 " + docID + " 1 " + weight + " 1");
        }
        pw.close();
    }

    /**
     * Reads the result from the specified file (TREC format).
     *
     * @param file    file for the result
     * @param numDocs maximum number of documents to read
     * @return list of ProbDoc instances
     */
    public static List readResult(File file, int numDocs) {
        try {
            return readResult(new FileReader(file), numDocs);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
        return new ArrayList();
    }

    /**
     * Reads the result from the specified file (TREC format).
     *
     * @param url     url for the result
     * @param numDocs maximum number of documents to read
     * @return list of ProbDoc instances
     */
    public static List readResult(URL url, int numDocs) {
        try {
            return readResult(new InputStreamReader(url.openStream()), numDocs);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
        return new ArrayList();
    }

    /**
     * Reads the result from the specified file (TREC format).
     *
     * @param reader  stream for the result
     * @param numDocs maximum number of documents to read
     * @return list of ProbDoc instances
     */
    public static List readResult(Reader reader, int numDocs) {
        List result = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(reader);
            int count = 0;
            for (String line;
                 (line = in.readLine()) != null && count < numDocs;
                 count++) {
                StringTokenizer tok = new StringTokenizer(line, " ");
                tok.nextToken(); // queryID
                tok.nextToken(); // always 0
                String docID = tok.nextToken(); // docID
                tok.nextToken(); // always 1
                double weight = Double.parseDouble(tok.nextToken()); // weight
                result.add(new ProbDoc(docID, weight));
            }
            in.close();
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
        return result;
    }

}
