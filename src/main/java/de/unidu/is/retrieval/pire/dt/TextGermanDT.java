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


// $Id: TextGermanDT.java,v 1.5 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval.pire.dt;

import de.unidu.is.pdatalog.IDBRelation;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.retrieval.pire.Index;
import de.unidu.is.text.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

//import de.unidu.is.text.GermanStemmerFilter;

/**
 * A class for the IR datatype "text", containing the operators:
 * <ol>
 * <li>"contains": with stemming and stop word removal, BM25 indexing weights,
 * experimental use only</li>
 * <li>"stemen": English stemming and stop word removal, BM25 indexing weights
 * </li>
 * <li>"nostem": without stemming, but with stop word removal, BM25 indexing
 * weights</li>
 * <li>"stemen_tf": English stemming and stop word removal, normalised TF
 * indexing weights</li>
 * <li>"nostem_tf": without stemming, but with stop word removal, normalised
 * TFindexing weights</li>
 * </ol>
 * <p>
 * <p>
 * By default, the RSVs are divided by the maximum RSV, and no logistic mapping
 * function is applied. Both can be changed.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/03/14 17:33:13 $
 * @since 2003-08-16
 */
public class TextGermanDT extends AbstractDT {

    /**
     * The name of this datatype.
     */
    public static final String NAME = "Text";

    /**
     * Operator name "contains" for experiments.
     */
    public static final String CONTAINS_EXP = "contains";

    /**
     * Operator name "contains" for experiments.
     */
    public static final String PLAIN_EXP = "plain";

    /**
     * Operator name "stemen" (English stemming, stopword removal, BM25).
     */
    public static final String STEMEN = "stemen";

    /**
     * Operator name "nostem" (no stemming, stopword removal, BM25).
     */
    public static final String NOSTEM = "nostem";

    /**
     * Operator name "stemen_tf" (English stemming, stopword removal,
     * normalised TF).
     */
    public static final String STEMEN_TF = "stemen_tf";

    /**
     * Operator name "nostem_tf" (no stemming, stopword removal, normalised
     * TF).
     */
    public static final String NOSTEM_TF = "nostem_tf";

    /**
     * Operator name "stemen_tfidf" (English stemming, stopword removal,
     * normalised TF.IDF).
     */
    public static final String STEMEN_TFIDF = "stemen_tfidf";

    /**
     * Operator name "nostem_tfidf" (no stemming, stopword removal, normalised
     * TF.IDF).
     */
    public static final String NOSTEM_TFIDF = "nostem_tfidf";
    /**
     * Formatter for probabilities.
     */
    private static final DecimalFormat formatter = new DecimalFormat(
            "0.0000000000000000", new DecimalFormatSymbols(Locale.ENGLISH));
    /**
     * Name of the document length relation.
     */
    private static final String DL_RELATION = "dl";
    /**
     * The index filter for the operator "stemen" and "stemen_tf".
     */
    private static final Filter stemenFilter;
    /**
     * The index filter for the operator "nostem" and "nostem_tf".
     */
    private static final Filter nostemFilter;
    /**
     * The query filter for the operators "stemen" and "stemen_tf".
     */
    private static final Filter stemenQueryFilter;
    /**
     * The query filter for the operator "nostem" and "nostem_tf".
     */
    private static final Filter nostemQueryFilter;
    /**
     * The indexfilter for the operator "plain".
     */
    private static final Filter plainFilter;
    /**
     * The query filter for the operator "plain".
     */
    private static final Filter plainQueryFilter;
    /**
     * The filter for splitting into tokens.
     */
    private static final WordSplitterFilter wordSplitterFilter;
    /**
     * Flag for scaling for experimental use only. By default, the RSVs are
     * divided by the maximum RSV, so that the normalised RSV for the top-ranked
     * document is one.
     *
     * @deprecated
     */
    private static boolean domap = true;
    /**
     * Flag for using a logistic mapping function for experimental use only. By
     * default, no mapping function is used.
     *
     * @deprecated
     */
    private static boolean doscale = false;
    /**
     * Flag for scaling for non-experimental use. By default, the RSVs are
     * divided by the maximum RSV, so that the normalised RSV for the top-ranked
     * document is one.
     */
    private static boolean doScaleForBM25 = true;
    /**
     * Flag for using a logistic mapping function for non-experimental use. By
     * default, no mapping function is used.
     */
    private static boolean doMapForBM25 = false;

    static {
        // operator "plain"
        plainFilter = new CounterFilter(new WordSplitterFilter(null, 0) {
            protected void handleBuffer(StringBuffer buffer) {
            }
        });
        plainQueryFilter = new WordConcatenatorFilter(new WordSplitterFilter(
                null, 0) {
            protected void handleBuffer(StringBuffer buffer) {
            }
        });

        // operators "stemen", "stemen_tf", "stemen_tfidf"
        wordSplitterFilter = new WordSplitterFilter(null);
//		Filter rawContainsQueryFilter = new GermanStemmerFilter(new StopwordFilter(
//				new LowercaseFilter(wordSplitterFilter),"stopwords_german"));
        Filter rawContainsQueryFilter = new Filter() {

            @Override
            public Iterator apply(Object seed) {
                return Collections.emptyIterator(); //TODO
            }

            @Override
            public Iterator apply(Iterator iterator) {
                return Collections.emptyIterator(); //TODO
            }
        };
        stemenQueryFilter = new WordConcatenatorFilter(rawContainsQueryFilter);
        stemenFilter = new CounterFilter(rawContainsQueryFilter);

        // operator "nostem", "nostem_tf", "nostem_tf"
        Filter rawContainsNoStemQueryFilter = new StopwordFilter(
                new LowercaseFilter(wordSplitterFilter), "stopwords_german");
        nostemQueryFilter = new WordConcatenatorFilter(
                rawContainsNoStemQueryFilter);
        nostemFilter = new CounterFilter(rawContainsNoStemQueryFilter);
    }

    /**
     * Sets the scaling flag for experiments.
     *
     * @param scale scaling flag
     * @deprecated
     */
    public static void setScale(boolean scale) {
        doscale = scale;
    }

    /**
     * Sets the mapping function flag for experiments.
     *
     * @param map mapping function flag
     * @deprecated
     */
    public static void setMap(boolean map) {
        domap = map;
    }

    /**
     * Sets the flag for enabling or disabling the RSV scaling.
     *
     * @param flag flag for enabling or disabling the RSV scaling
     */
    public static void setDoMapForBM25(boolean flag) {
        TextGermanDT.doMapForBM25 = flag;
    }

    /**
     * Sets the flag for enabling or disabling the logistic mapping function.
     *
     * @param flag flag for enabling or disabling the logistic mapping function
     */
    public static void setDoScaleForBM25(boolean flag) {
        TextGermanDT.doScaleForBM25 = flag;
    }

    /**
     * Returns the word splitter filter.
     *
     * @return word splitter filter
     */
    public static WordSplitterFilter getWordSplitterFilter() {
        return wordSplitterFilter;
    }

    /**
     * Tests whether the operator is a operator for experiments.
     *
     * @param operator operator name
     * @return true iff the operator is a operator for experiments
     */
    private static boolean isExpOperator(String operator) {
        return operator.equals(CONTAINS_EXP) || operator.equals(PLAIN_EXP);
    }

    /**
     * Tests whether the operator is a operator with normalised TF indexing
     * weights.
     *
     * @param operator operator name
     * @return true iff the operator is a operator with normalised TF indexing
     * weights
     */
    private static boolean isTFOperator(String operator) {
        return operator.endsWith("_tf");
    }

    /**
     * Tests whether the operator is a operator with normalised TF.IDF
     * indexing weights.
     *
     * @param operator operator name
     * @return true iff the operator is a operator with normalised TF.IDF
     * indexing weights
     */
    private static boolean isTFIDFOperator(String operator) {
        return operator.endsWith("_tfidf");
    }

    /**
     * Returns a filter for converting a document value into tokens/token
     * frequency tuples.
     *
     * @param operator operator name
     * @return filter
     */
    protected Filter getFilter(String operator) {
        if (operator.equals(PLAIN_EXP))
            return plainFilter;
        return operator.equals(CONTAINS_EXP) || operator.startsWith(STEMEN)
                ? stemenFilter
                : nostemFilter;
    }

    /**
     * Returns a filter for converting a condition comparison value into
     * tokens/token frequency tuples.
     *
     * @param operator operator name
     * @return filter
     */
    protected Filter getQueryFilter(String operator) {
        if (operator.equals(PLAIN_EXP))
            return plainQueryFilter;
        return operator.equals(CONTAINS_EXP) || operator.startsWith(STEMEN)
                ? stemenQueryFilter
                : nostemQueryFilter;
    }

    /**
     * Computes the indexing weights for the specified index and the operator.
     * <p>
     * <p>
     * The indexing weights are computed using a BM25 weighting scheme.
     *
     * @param index    underlying index
     * @param operator operator name
     */
    public void computeIndex(Index index, String operator) {
        String tf = index.convert(Index.TF_RELATION);
        String docid = index.convert(Index.DOCID_RELATION);
        String dl = index.convert(DL_RELATION);
        String rd = index.convert(Index.IDB_RD_RELATION);
        String weight = index.convert(Index.WEIGHT_RELATION);

        index.addIDBRelation(dl, 2, true);
        index.addEDBRelation(tf, 3, false);
        IDBRelation rdRelation = index.addIDBRelation(rd, 2, false);
        index.addIDBRelation(weight, 2, true);
        index.completeRelation(tf);

        Rule rule;

        // compute document length (dl)
        rule = Parser.parseRule(dl + "(D,DL) :- " + "sum(DL,TF,{ " + tf
                + "(D,~,TF) }).");
        rule.setOptimizable(true);
        index.compute(rule);

        // compute average document length (avgdl) only for BM25 operators
        // compute number of documents (numdocs)
        List rdRules = new ArrayList();
        if (!isTFOperator(operator) && !isTFIDFOperator(operator)) {
            rule = Parser.parseRule(rd + "('avgdl',AVGDL) :- avg(AVGDL,DL,{ "
                    + dl + "(~,DL) }).");
            rule.setOptimizable(true);
            rdRules.add(rule);
        }
        rule = Parser.parseRule(rd
                + "('numdocs',NUMDOCS) :- count(NUMDOCS,D,{ " + docid
                + "(D) }).");
        rule.setOptimizable(true);
        rdRules.add(rule);
        index.computeDisjoint(rdRelation, rdRules);

        if (isTFOperator(operator)) {
            // normalised TF weights

            // compute indexing weights
            rule = Parser.parseRule(weight + "(D,T) :- " + tf + "(D,T,TF) & "
                    + dl + "(D,DL)  | (TF/DL).");
            rule.setOptimizable(true);
            index.compute(rule);

            // index.removeRelation(dl);
        } else {
            // normalised TF.IDF weights or BM 25

            String df = index.convert(Index.DF_RELATION);
            index.addIDBRelation(df, 2, true);

            // compute document-value pair relation
            String dv = index.convert("dv");
            index.addIDBRelation(dv, 2, true);
            rule = Parser.parseRule(dv + "(D,T) :- " + tf + "(D,T).");
            rule.setOptimizable(true);
            index.compute(rule);

            // compute document frequencies (df)
            rule = Parser.parseRule(df + "(T,DF) :- " + "count(DF,D,{ " + dv
                    + "(D,T) }).");
            rule.setOptimizable(true);
            index.compute(rule);

            // compute indexing weights
            String tmp1 = index.convert("tmp1");
            index.addIDBRelation(tmp1, 2, true);
            rule = isTFIDFOperator(operator) ? rule = Parser.parseRule(tmp1
                    + "(D,T) :- " + tf + "(D,T,TF) & " + dl
                    + "(D,DL)  | (TF/DL).") : Parser.parseRule(tmp1
                    + "(D,T) :- " + tf + "(D,T,TF) & " + dl + "(D,DL) & " + rd
                    + "('avgdl',AVGDL)  | (TF/((TF+(0.5+1.5*(DL/AVGDL))))).");
            rule.setOptimizable(true);
            index.compute(rule);
            double numDocsInRD = index.getRD("numDocs");
            String nd = numDocsInRD == 0 ? "NUMDOCS" : String.valueOf(numDocsInRD);
            rule = isExpOperator(operator)
                    ? Parser
                    .parseRule(weight
                            + "(D,T) :- "
                            + tmp1
                            + "(D,T) & "
                            + df
                            + "(T,DF) & "
                            + rd
                            + "('numdocs',NUMDOCS) | (PROB1* (&log(NUMDOCS/DF)/&log("
                            + nd + "))).")
                    : Parser
                    .parseRule(weight
                            + "(D,T) :- "
                            + tmp1
                            + "(D,T) & "
                            + df
                            + "(T,DF) & "
                            + rd
                            + "('numdocs',NUMDOCS) | (PROB1* (&log((NUMDOCS+0.5)/DF)/&log(("
                            + nd + "+0.5)))).");
            rule.setOptimizable(true);
            index.compute(rule);

            //index.removeRelation(dl);
            index.removeRelation(tmp1);
            index.removeRelation(dv);
        }
    }

    /**
     * Removes the index.
     */
    public void removeIndex(Index index, String operator) {
        super.removeIndex(index, operator);
        index.removeRelation(index.convert(DL_RELATION));
    }

    /**
     * Returns a template for computing probabilities of relevance.
     * <p>
     * <p>
     * The template string is an expression which contains the key
     * <code>${PROB}</code>.
     *
     * @param index    underlying index
     * @param queryID  query id
     * @param operator operator name
     * @param suffix   relation suffix
     * @return template for computing probabilities of relevance
     */
    public String getProbsTemplate(Index index, String queryID, String suffix,
                                   String operator) {
        String str = "PROB";
        if (isExpOperator(operator)) {
            if (doscale) {
                double scale = index.getMaxRSV(queryID, suffix);
                // do not divide by scaling to stay compatible (no problem, as
                // only for experiments with one operator)
                if (scale == 0)
                    scale = 1;
                str = "(" + str + "/" + formatter.format(scale) + ")";
            }
            if (domap) {
                double b0 = index.getRD("b0", -4);
                double b1 = index.getRD("b1", 12);
                if (true) {
                    String part = "&exp(" + b0 + "+" + "(" + b1 + "*" + str
                            + "))";
                    str = "(" + part + " / (1 + " + part + ")) ";
                }
            }
        } else {
            if (isTFOperator(operator) || isTFIDFOperator(operator)) {
                // id function, nothing to do!
            } else {
                if (doScaleForBM25) {
                    double scale = index.getMaxRSV(queryID, suffix);
                    if (scale == 0)
                        scale = 1;
                    str = "(" + str + "/" + formatter.format(scale) + ")";
                }
                if (doMapForBM25) {
                    double b0 = index.getRD("b0", -4);
                    double b1 = index.getRD("b1", 12);
                    if (true) {
                        String part = "&exp(" + b0 + "+" + "(" + b1 + "*" + str
                                + "))";
                        str = "(" + part + " / (1 + " + part + ")) ";
                    }
                }
            }
        }
        if (!str.startsWith("("))
            str = "(" + str + ")";
        return str;
    }

}
