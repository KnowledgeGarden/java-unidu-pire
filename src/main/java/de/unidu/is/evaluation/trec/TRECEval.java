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

// $Id: TRECEval.java,v 1.6 2005/03/04 11:19:18 nottelma Exp $
package de.unidu.is.evaluation.trec;

import java.io.*;

/**
 * An ecapsulation for the trec_eval program for evaluating TREC results.
 * <p>
 * <p>
 * The relevance judgement file must have the format
 * <code>[query id] * [document id] [0/1]</code>
 * <p>
 * <p>
 * The result file must be in TREC format, e.g. created by
 * <code>de.unidu.is.evaluation.trec.TRECResult</code>.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/04 11:19:18 $
 * @since 2004-01-06
 */
public class TRECEval {

    /**
     * The file for the trec_eval program.
     */
    private final File trecEvalFile;

    /**
     * The file for the relevance judgements
     */
    private final File qrelFile;

    /**
     * Creates a new instance.
     *
     * @param trecEvalFile file for the trec_eval program
     * @param qrelFile     file for the relevance judgements
     */
    public TRECEval(File trecEvalFile, File qrelFile) {
        this.trecEvalFile = trecEvalFile;
        this.qrelFile = qrelFile;
    }

    /**
     * Runs trec_eval file on the specified result file (in TREC format), and
     * optionally writes the precision in the 11 standard recall points and the
     * output of trec_eval in two different files. This method returns the
     * precision at ranks 5, 10, 15, 20 and 30 and the average precision.
     *
     * @param resultFile  file with the TREC result
     * @param dataOutFile unless null, file for the precision points
     * @param evalOutFile unless null, file for the trec_eval output
     * @return array with 6 elements (precision at 5, 10, 15, 20, 30, AP)
     * @throws IOException
     */
    public double[] run(File resultFile, File dataOutFile, File evalOutFile)
            throws IOException {
        return run(resultFile, dataOutFile == null ? null : new FileWriter(
                dataOutFile), evalOutFile == null ? null : new FileWriter(
                evalOutFile));
    }

    /**
     * Runs trec_eval file on the specified result file (in TREC format), and
     * optionally writes the precision in the 11 standard recall points and the
     * output of trec_eval in two different streams. This method returns the
     * precision at ranks 5, 10, 15, 20 and 30 and the average precision.
     *
     * @param resultFile    file with the TREC result
     * @param dataOutWriter unless null, stream for the precision points
     * @param evalOutWriter unless null, stream for the trec_eval output
     * @return array with 6 elements (precision at 5, 10, 15, 20, 30, AP)
     * @throws IOException
     */
    public double[] run(File resultFile, Writer dataOutWriter,
                        Writer evalOutWriter) throws IOException {
        double[] array = new double[6];
        Process p = Runtime.getRuntime().exec(
                trecEvalFile + " " + qrelFile + " " + resultFile);
        BufferedReader in = null;
        PrintWriter dataOut = null;
        PrintWriter evalOut = null;
        try {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if (dataOutWriter != null)
                dataOut = new PrintWriter(dataOutWriter);
            if (evalOutWriter != null)
                evalOut = new PrintWriter(evalOutWriter);
            final String format1 = "    at ";
            final String format2 = "  At   ";
            final String format3 = "                  ";
            int count = 0;
            for (String line; (line = in.readLine()) != null; ) {
                if (evalOut != null)
                    evalOut.println(line);
                if (line.startsWith(format1)) {
                    line = line.substring(format1.length());
                    if (dataOut != null)
                        dataOut.println(line);
                }
                if (line.startsWith(format2)) {
                    line = line.substring(format2.length());
                    int h = line.indexOf(':');
                    line = line.substring(h + 1).trim();
                    array[count++] = Double.parseDouble(line);
                }
                if (line.startsWith(format3)) {
                    line = line.substring(format3.length());
                    array[5] = Double.parseDouble(line);
                }
            }
        } catch (NumberFormatException e) {
            de.unidu.is.util.Log.error(e);
        } finally {
            if (in != null)
                in.close();
            if (dataOut != null)
                dataOut.close();
            if (evalOut != null)
                evalOut.close();
        }
        return array;
    }

    /**
     * Computes the number of retrieved documents, number of total relevant
     * documents, number the retrieved relevant documents and the number of
     * queries.
     *
     * @param resultFile result file
     * @return array with 4 elements (order see above)
     * @throws IOException
     */
    public int[] runAccuracy(File resultFile) throws IOException {
        int[] array = new int[4];
        Process p = Runtime.getRuntime().exec(
                trecEvalFile + " " + qrelFile + " " + resultFile);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            final String[] format = {"    Retrieved:", "    Relevant:",
                    "    Rel_ret:", "Queryid (Num):"};
            int count = 0;
            for (String line; (line = in.readLine()) != null; ) {
                for (int i = 0; i < format.length; i++)
                    if (line.startsWith(format[i])) {
                        line = line.substring(format[i].length()).trim();
                        array[i] = Integer.parseInt(line);
                    }
            }
        } catch (NumberFormatException e) {
            de.unidu.is.util.Log.error(e);
        }
        return array;
    }

    /**
     * Returns the file for the trec_eval program.
     *
     * @return file for the trec_eval program
     */
    public File getTrecEvalFile() {
        return trecEvalFile;
    }

    /**
     * Returns the file for the relevance judgements.
     *
     * @return file for the relevance judgements
     */
    public File getQrelFile() {
        return qrelFile;
    }

}