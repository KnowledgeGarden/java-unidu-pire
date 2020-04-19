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

// $Id: Lemur.java,v 1.10 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.lemur;

import de.unidu.is.evaluation.trec.TRECResult;
import de.unidu.is.retrieval.KeywordQuery;
import de.unidu.is.retrieval.Query;
import de.unidu.is.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A wrapper class for the Lemur (distributed) retrieval system.
 *
 * @author Henrik Nottelma
 * @version $Revision: 1.10 $, $Date: 2005/03/14 17:33:14 $
 * @since 2004-02-18
 */
public class Lemur {

    /**
     * The ID for the tf.idf ranking method.
     */
    public static final int tfidf = 0;

    /**
     * The ID for the OKAPI ranking method.
     */
    public static final int okapi = 1;

    /**
     * The ID for the KL-Divergance ranking method.
     */
    public static final int kl = 2;

    /**
     * The ID for the INQUERY ranking method.
     */
    public static final int inquery = 3;

    /**
     * Path for the Lemur binaries.
     * <p>
     * TODO
     */
    private static final String LEMUR_BIN = "/usr/sw-ir/lemur/2.0.1-hn1/lemur/bin/";

    /**
     * The base directory.
     */
    private File dir;

    /**
     * The directory for the text files (for indexing).
     */
    private File textDir;

    /**
     * The directory for the samples files (for CORI indexing).
     */
    private File sampleDir;

    /**
     * The directory for the index files (only for retrieval).
     */
    private File indexDir;

    /**
     * The directory for the topic files.
     */
    private File topicDir;

    /**
     * The directory for the result files.
     */
    private File resultDir;

    /**
     * The directory for the index files (only for CORI).
     */
    private File indexCORIDir;

    /**
     * Creates a new instance and initis the directories.
     *
     * @param dir       base directory
     * @param useSample if true, the sample will be used for the CORI index
     */
    public Lemur(File dir, boolean useSample) {
        this.dir = dir;
        textDir = new File(dir, "text");
        textDir.mkdirs();
        sampleDir = useSample ? new File(dir, "sample") : textDir;
        sampleDir.mkdirs();
        topicDir = new File(dir, "topics");
        topicDir.mkdirs();
        indexDir = new File(dir, "index");
        indexDir.mkdirs();
        resultDir = new File(dir, "result");
        resultDir.mkdirs();
        indexCORIDir = indexDir;
    }

    /**
     * Creates a text file (with the terms in it) in TREC format for the
     * specified collection from the database.
     *
     * @param coll    collection name
     * @param termMap document-termlist map
     */
    public File createTextFile(String coll, Map termMap) {
        PrintWriter pw = null;
        File file = getTextFile(coll);
        try {
            pw = new PrintWriter(new FileWriter(file));
            for (Object value : termMap.keySet()) {
                String docID = (String) value;
                List terms = (List) termMap.get(docID);
                pw.println("<DOC>");
                pw.println("<DOCNO>" + docID + "</DOCNO>");
                pw.println("<TEXT>");
                for (Object o : terms) {
                    String term = (String) o;
                    pw.println(term);
                }
                pw.println("</TEXT>");
                pw.println("</DOC>");
            }
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (pw != null)
                pw.close();
        }
        return file;
    }

    /**
     * Indexes the collection.
     *
     * @param coll  collection name
     * @param doLog if true, the output of the Lemur program is logged
     */
    public void index(String coll, boolean doLog) {
        PrintWriter pw;
        // Create index
        File textFile = getTextFile(coll);
        if (!textFile.exists()) {
            try {
                pw = new PrintWriter(new FileWriter(textFile));
                pw.close();
            } catch (IOException e1) {
                de.unidu.is.util.Log.error(e1);
            }
        }
        File indexFile = getIndexFile(coll);
        File paramFile = null;
        pw = null;
        try {
            paramFile = File.createTempFile("pushindexer", ".param");
            pw = new PrintWriter(new FileWriter(paramFile));
            pw.println("index=" + indexFile + ";");
            pw.println("docFormat=trec;");
            pw.close();
            pw = null;
            callProgram(new String[]{LEMUR_BIN + "PushIndexer",
                            paramFile.getAbsolutePath(), textFile.getAbsolutePath()},
                    doLog);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (pw != null)
                pw.close();
            if (paramFile != null)
                paramFile.delete();
        }
    }

    /**
     * Generates a smoothing file for the collection.
     *
     * @param coll  collection name
     * @param doLog if true, the output of the Lemur program is logged
     */
    public void generateSmooth(String coll, boolean doLog) {
        PrintWriter pw = null;
        File indexFile = getIndexFile(coll);
        File paramFile = null;
        try {
            paramFile = File.createTempFile("generatesmooth", ".param");
            pw = new PrintWriter(new FileWriter(paramFile));
            pw.println("index=" + indexFile + ".ifp;");
            pw.println("smoothSupportFile=" + indexFile + ".supp;");
            pw.close();
            pw = null;
            callProgram(new String[]{LEMUR_BIN + "GenerateSmoothSupport",
                    paramFile.getAbsolutePath()}, doLog);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (pw != null)
                pw.close();
            if (paramFile != null)
                paramFile.delete();
        }
    }

    /**
     * Creates a topic file (with the terms in it) in TREC format for the
     * specified node.
     *
     * @param topicsName name for the topic
     * @param query      query
     * @param append     if true, appends to that file
     */
    public void createTopicFile(String topicsName, Query query, boolean append) {
        createTopicFile(getTopicFile(topicsName), query, append);
    }

    /**
     * Creates a topic file (with the terms in it) in TREC format for the
     * specified node.
     *
     * @param topicFile file
     * @param query     query
     * @param append    if true, appends to that file
     */
    public static void createTopicFile(File topicFile, Query query, boolean append) {
        KeywordQuery keywordQuery = (KeywordQuery) query;
        try (PrintWriter pw = new PrintWriter(new FileWriter(topicFile, append))) {
            String qID = query.getQueryID();
            int h = qID.indexOf('_');
            if (h != -1)
                qID = qID.substring(0, h);
            pw.println("<DOC " + qID + ">");
            for (Object o : keywordQuery.getKeywords()) {
                String term = (String) o;
                pw.println(term);
            }
            pw.println("</DOC>");
		} catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Returns the result for the specified query.
     *
     * @param coll    collection name
     * @param query   query
     * @param mode    Lemur mode
     * @param numDocs number of documents
     * @param doLog   if true, the output of the Lemur program is logged
     * @return list of ProbDoc instances
     */
    public List getResult(String coll, Query query, int mode, int numDocs,
                          boolean doLog) {
        List result = new ArrayList();
        File topicFile = null;
        File resultFile = null;
        try {
            topicFile = File.createTempFile("topics", ".trec");
            resultFile = File.createTempFile("result", ".result");
            createTopicFile(topicFile, query, false);
            evaluate(coll, topicFile, resultFile, mode, numDocs, doLog);
            result.addAll(TRECResult.readResult(resultFile, numDocs));
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (topicFile != null)
                topicFile.delete();
            if (resultFile != null)
                resultFile.delete();
        }
        return result;
    }

    /**
     * Evaluates the given topics on the specified collection.
     *
     * @param coll       collection name
     * @param topicsName name of the topics (for the topic and the result file)
     * @param mode       Lemur mode
     * @param numDocs    number of documents
     * @param doLog      if true, the output of the Lemur program is logged
     */
    public void evaluate(String coll, String topicsName, int mode, int numDocs,
                         boolean doLog) {
        File topicsFile = getTopicFile(topicsName);
        File resultFile = getResultFile(topicsName, coll);
        evaluate(coll, topicsFile, resultFile, mode, numDocs, doLog);
    }

    /**
     * Evaluates the given topics on the specified collection.
     *
     * @param coll       collection name
     * @param topicsFile topics file
     * @param resultFile file for the results
     * @param mode       Lemur mode
     * @param numDocs    number of documents
     * @param doLog      if true, the output of the Lemur program is logged
     */
    public void evaluate(String coll, File topicsFile, File resultFile,
                         int mode, int numDocs, boolean doLog) {
        PrintWriter pw;
        File indexFile = getIndexFile(coll);
        File paramFile = null;
        pw = null;
        try {
            paramFile = File.createTempFile("reteval-" + coll, ".param");
            pw = new PrintWriter(new FileWriter(paramFile));
            pw.println("index=" + indexFile + ".ifp;");
            pw.println("retModel=" + mode + ";");
            pw.println("TF_factor=0.5;");
            pw.println("TF_baseline=1.5;");
            pw.println("feedbackDocCount=0;");
            pw.println("resultCount=" + numDocs + ";");
            pw.println("textQuery=" + topicsFile + ";");
            pw.println("resultFile=" + resultFile + ";");
            if (mode == kl) {
                pw.println(" smoothSupportFile=" + indexFile + ".supp;");
                pw.println("smoothMethod=0;");
                pw.println("smoothStrategy=0;");
            }
            pw.close();
            pw = null;
            callProgram(new String[]{LEMUR_BIN + "RetEval",
                    paramFile.getAbsolutePath()}, doLog);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (pw != null)
                pw.close();
            if (paramFile != null)
                paramFile.delete();
        }
    }

    /**
     * Computes a CORI index and creates for each collection a parameter file
     * which will be used for retrieval if the collection will be selected.
     *
     * @param colls collections to be used for the index
     * @param doLog if true, the output of the Lemur program is logged
     */
    public void indexCORI(String[] colls, boolean doLog) {
        File indexCORIFile = getIndexCORIFile();
        File indexCORIDFFile = getIndexCORIDFFile();
        File indexCORIDFDocsFile = getIndexCORIDFDocsFile();
        File indexCORICTFFile = getIndexCORICTFFile();
        PrintWriter pw = null;
        try {
            File paramFile = File.createTempFile("collselindex", ".param");
            File dlFile = File.createTempFile("collselindex", ".files");
            pw = new PrintWriter(new FileWriter(paramFile));
            pw.println("dfIndex=" + indexCORIFile + ";");
            pw.println("dfCounts=" + indexCORIDFFile + ";");
            pw.println("dfDocs=" + indexCORIDFDocsFile + ";");
            pw.println("ctfIndex=" + indexCORICTFFile + ";");
            pw.println("docFormat=trec;");
            pw.close();
            pw = new PrintWriter(new FileWriter(dlFile));
            for (String s : colls) pw.println(getSampleFile(s));
            pw.close();
            pw = null;
            callProgram(new String[]{LEMUR_BIN + "CollSelIndex",
                            paramFile.getAbsolutePath(), dlFile.getAbsolutePath()},
                    doLog);

            // write param files
            for (String coll : colls) {
                pw = new PrintWriter(new FileWriter(getIndexParamFile(coll)));
                pw.println("index=" + getIndexFile(coll) + ".ifp;");
                pw.println("retModel=3;");
                pw.println("TF_factor=0.5;");
                pw.println("TF_baseline=1.5;");
                pw.println("collCounts=USE_INDEX_COUNTS;");
                pw.close();
                pw = null;
            }
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (pw != null)
                pw.close();
        }
    }

    /**
     * Returns the result for the specified query after resource selection with
     * CORI.
     *
     * @param colls   collection names
     * @param query   query
     * @param numDocs number of documents
     * @param numDLs  number of DLs to be selected
     * @param doLog   if true, the output of the Lemur program is logged
     * @return array of two lists of ProbDoc instances (0: result, 1: ranks)
     */
    public List[] getCORIResult(String[] colls, Query query, int numDocs,
                                int numDLs, boolean doLog) {
        List[] result = new ArrayList[2];
        result[0] = new ArrayList();
        result[1] = new ArrayList();
        File topicFile = null;
        File ranksFile = null;
        File resultFile = null;
        try {
            topicFile = File.createTempFile("topics", ".trec");
            ranksFile = File.createTempFile("result", ".ranks");
            resultFile = File.createTempFile("result", ".result");
            createTopicFile(topicFile, query, false);
            evaluateCORI(colls, topicFile, ranksFile, resultFile, numDocs,
                    numDLs, doLog);
            result[0].addAll(TRECResult.readResult(resultFile, numDocs));
            result[1].addAll(TRECResult.readResult(ranksFile, numDLs));
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (topicFile != null)
                topicFile.delete();
            if (ranksFile != null)
                resultFile.delete();
            if (resultFile != null)
                resultFile.delete();
        }
        return result;
    }

    /**
     * Evaluates the given topics on the specified collections, employing
     * resource selection with CORI.
     *
     * @param colls      collection names
     * @param topicsName name of the topics (for the topic and the result file)
     * @param numDocs    number of documents
     * @param numDLs     number of DLs to be selected
     * @param doLog      if true, the output of the Lemur program is logged
     */
    public void evaluateCORI(String[] colls, String topicsName, int numDocs,
                             int numDLs, boolean doLog) {
        File topicsFile = getTopicFile(topicsName);
        File ranksFile = getRanksCORIFile(topicsName);
        File resultFile = getResultCORIFile(topicsName);
        evaluateCORI(colls, topicsFile, ranksFile, resultFile, numDocs, numDLs,
                doLog);
    }

    /**
     * Evaluates the given topics on the specified collections, employing
     * resource selection with CORI.
     *
     * @param colls      collection names
     * @param topicsFile topics file
     * @param ranksFile  file for the ranks
     * @param resultFile file for the results
     * @param numDocs    number of documents
     * @param numDLs     number of DLs to be selected
     * @param doLog      if true, the output of the Lemur program is logged
     */
    public void evaluateCORI(String[] colls, File topicsFile, File ranksFile,
                             File resultFile, int numDocs, int numDLs, boolean doLog) {
        PrintWriter pw;
        File indexCORIFile = getIndexCORIFile();
        File indexCORIDFFile = getIndexCORIDFFile();
        File indexCORIDFDocsFile = getIndexCORIDFDocsFile();
        File indexCORICTFFile = getIndexCORICTFFile();
        File paramFile = null;
        pw = null;
        try {
            paramFile = File.createTempFile("distreteval", ".param");
            pw = new PrintWriter(new FileWriter(paramFile));
            pw.println("index=" + indexCORIFile + ".inv;");
            pw.println("collCounts=" + indexCORIDFFile + ";");
            pw.println("cutoff=" + numDLs + ";");
            pw.println("resultCount=" + numDocs + ";");
            pw.println("textQuery=" + topicsFile + ";");
            pw.println("resultFile=" + resultFile + ";");
            pw.println("ranksFile=" + ranksFile + ";");
            for (String coll : colls) {
                pw.println(coll + "=" + getIndexParamFile(coll) + ";");
            }
            pw.close();
            pw = null;
            callProgram(new String[]{LEMUR_BIN + "DistRetEval",
                    paramFile.getAbsolutePath()}, doLog);
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (pw != null)
                pw.close();
            if (paramFile != null)
                paramFile.delete();
        }
    }

    /**
     * Returns the file name for the evaluation results.
     *
     * @param topicsName name of the topics (for the topic and the result file)
     * @param coll       colection name
     * @return file name for the evaluation results
     */
    public File getResultFile(String topicsName, String coll) {
        return new File(resultDir, coll + "-" + topicsName + ".result");
    }

    /**
     * Returns the file name for the ranks of the CORI evaluation.
     *
     * @param topicsName name of the topics (for the topic and the result file)
     * @return file name for the evaluation ranks
     */
    public File getRanksCORIFile(String topicsName) {
        return new File(resultDir, "cori-" + topicsName + ".ranks");
    }

    /**
     * Returns the file name for the results of the CORI evaluation.
     *
     * @param topicsName name of the topics (for the topic and the result file)
     * @return file name for the evaluation results
     */
    public File getResultCORIFile(String topicsName) {
        File resultFile = new File(resultDir, "cori-" + topicsName + ".result");
        return resultFile;
    }

    // Auxilliary methods

    /**
     * Calls the Lemur program, and optionally logs the output of STDOUT and
     * STDERR to the error logger.
     *
     * @param args  Lemur arguments
     * @param doLog if true, logs the output
     */
    private static void callProgram(String[] args, boolean doLog) throws IOException {
        final Process p = Runtime.getRuntime().exec(args);
        if (!doLog) {
            try {
                p.waitFor();
            } catch (Exception ex) {
                de.unidu.is.util.Log.error(ex);
            }
        } else {
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(p.getInputStream()));
                    for (String line; (line = in.readLine()) != null; ) {
                        System.out.println(line);
                    }
                    in.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }).start();
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(p.getErrorStream()));
                    for (String line; (line = in.readLine()) != null; )
                        System.out.println(line);
                    in.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }).start();
            try {
                p.waitFor();
            } catch (Exception ex) {
                de.unidu.is.util.Log.error(ex);
            }
        }
    }

    /**
     * Returns text file for the specified collection.
     *
     * @param coll collection
     * @return file
     */
    private File getTextFile(String coll) {
        return new File(textDir, coll + ".trec");
    }

    /**
     * Returns the sample file for the specified collection.
     *
     * @param coll collection
     * @return file
     */
    private File getSampleFile(String coll) {
        return new File(sampleDir, coll + ".trec");
    }

    /**
     * Returns the index parameter file for the specified collection.
     *
     * @param coll collection
     * @return file
     */
    private File getIndexParamFile(String coll) {
        return new File(indexDir, coll + ".param");
    }

    /**
     * Returns the index file for the specified collection.
     *
     * @param coll collection
     * @return file
     */
    private File getIndexFile(String coll) {
        return new File(indexDir, coll + ".index");
    }

    /**
     * Returns the topics file for the specified topic.
     *
     * @param topicsName topic name
     * @return file
     */
    private File getTopicFile(String topicsName) {
        return new File(topicDir, "topic-" + topicsName + ".trec");
    }

    /**
     * Returns the CORI index file.
     *
     * @return file
     */
    private File getIndexCORIFile() {
        return new File(indexCORIDir, "cori.df");
    }

    /**
     * Returns the CORI DF counts file.
     *
     * @return file
     */
    private File getIndexCORIDFFile() {
        return new File(indexCORIDir, "cori.dfcounts");
    }

    /**
     * Returns the CORI DF docs  file.
     *
     * @return file
     */
    private File getIndexCORIDFDocsFile() {
        return new File(indexCORIDir, "cori.dfdocs");
    }

    /**
     * Returns the CORI CTF file.
     *
     * @return file
     */
    private File getIndexCORICTFFile() {
        return new File(indexCORIDir, "cori.ctf");
    }

    /**
     * Returns the base directory.
     *
     * @return base directory
     */
    public File getDir() {
        return dir;
    }

    /**
     * Sets the base directory.
     *
     * @param file base directory
     */
    public void setDir(File file) {
        setDir(file, false);
    }

    /**
     * Returns the CORI index directory.
     *
     * @return directory
     */
    public File getIndexCORIDir() {
        return indexCORIDir;
    }

    /**
     * Sets the CORI index directory
     *
     * @param file directory
     */
    public void setIndexCORIDir(File file) {
        indexCORIDir = file;
        file.mkdirs();
    }

    /**
     * Returns the index directory.
     *
     * @return directory
     */
    public File getIndexDir() {
        return indexDir;
    }

    /**
     * Sets the index directory
     *
     * @param file directory
     */
    public void setIndexDir(File file) {
        indexDir = file;
        file.mkdirs();
    }

    /**
     * Returns the result directory.
     *
     * @return directory
     */
    public File getResultDir() {
        return resultDir;
    }

    /**
     * Sets the result directory
     *
     * @param file directory
     */
    public void setResultDir(File file) {
        resultDir = file;
        file.mkdirs();
    }

    /**
     * Returns the sample directory.
     *
     * @return directory
     */
    public File getSampleDir() {
        return sampleDir;
    }

    /**
     * Sets the sample directory
     *
     * @param file directory
     */
    public void setSampleDir(File file) {
        sampleDir = file;
        file.mkdirs();
    }

    /**
     * Returns the text directory.
     *
     * @return directory
     */
    public File getTextDir() {
        return textDir;
    }

    /**
     * Sets the text directory
     *
     * @param file directory
     */
    public void setTextDir(File file) {
        textDir = file;
        file.mkdirs();
    }

    /**
     * Returns the topics directory.
     *
     * @return directory
     */
    public File getTopicDir() {
        return topicDir;
    }

    /**
     * Sets the topic directory
     *
     * @param file directory
     */
    public void setTopicDir(File file) {
        topicDir = file;
        file.mkdirs();
    }

    /**
     * Sets the base directory.
     *
     * @param file      base directory
     * @param useSample if true, uses samples instead of full text
     */
    public void setDir(File file, boolean useSample) {
        dir = file;
        setTextDir(new File(dir, "text"));
        setSampleDir(useSample ? new File(dir, "sample") : textDir);
        setTopicDir(new File(dir, "topics"));
        setIndexDir(new File(dir, "index"));
        setResultDir(new File(dir, "result"));
        setIndexCORIDir(indexDir);
    }

}