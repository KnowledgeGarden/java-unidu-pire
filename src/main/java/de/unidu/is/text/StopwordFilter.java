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

// $Id: StopwordFilter.java,v 1.17 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.text;

import de.unidu.is.util.CollectionUtilities;
import de.unidu.is.util.StringUtilities;
import de.unidu.is.util.SystemUtilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * This filter is used for removing stop words.
 * <p>
 * <p>
 * The stopwords are taken from <code>conf/common_words</code>.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.17 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-07-04
 */
public class StopwordFilter extends AbstractSingleItemFilter {

    /**
     * Minimum length for words used as stop words, provided for backward
     * compatibility with earlier experiments.
     * <p>
     * This default is set to 0, but this can be changed using
     * <code>setMinWordLength(int)</code>.
     */
    private static final int minWordLength = 0;

//	/**
//	 * The default stopword list. The keys are strings (the file names), the
//	 * values are sets of stop words.
//	 */
//	private static Map defaultStopwords = new HashMap();

    /**
     * The stopwords.
     */
    private final Set stopwords;

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public StopwordFilter(Filter nextFilter) {
        this(nextFilter, new HashSet(getDefaultStopwords()));
    }

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     * @param stopwords  set of stopwords used instead of the default set
     */
    public StopwordFilter(Filter nextFilter, Set stopwords) {
        super(nextFilter);
        this.stopwords = stopwords;
    }

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     * @param fileName   name of file with stopwords
     */
    public StopwordFilter(Filter nextFilter, String fileName) {
        this(nextFilter, new HashSet(readStopwords(fileName)));
    }

    /**
     * Returns the stopword list, and leads it if required.
     */
    public static synchronized Set getDefaultStopwords() {
        return readStopwords("common_words");
    }

    /**
     * Returns the stopword list, and leads it if required.
     *
     * @param fileName file name with stop word list
     */
    public static synchronized Set readStopwords(String fileName) {
//		Set words = (Set) defaultStopwords.get(fileName);
//		if (words == null) {
        HashSet words = new HashSet();
        try {
//				URL url = SystemUtilities.getResourceURL(fileName);
//				if (url == null)
//					fileName = "stopwords";
            BufferedReader in = new BufferedReader(new InputStreamReader(SystemUtilities
                    .getConfAsStream(fileName)));
            Filter parser = new WordSplitterFilter(new LowercaseFilter(
                    new StemmerFilter(null)), minWordLength);
            for (String line; (line = in.readLine()) != null; )
                words.addAll(CollectionUtilities.toSet(parser
                        .apply(line)));
            in.close();
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return words;
    }

    /**
     * Returns null if the specified value is a stopword, and the specified
     * value else.
     *
     * @param value string to be tested
     * @return null iff the value is a stopword, and the value else
     */
    public Object run(Object value) {
        if (isStopword(value.toString()))
            return null;
        return value;
    }

//	/**
//	 * Returns a set containing all stopwords.
//	 *
//	 * @return set containing all stopwords
//	 */
//	public Set getStopwordsSet() {
//		return stopwords;
//	}

    /**
     * Tests if term is (after stemming) a stopword.
     *
     * @param term term to test
     * @return true if term is (after stemming) a stopword
     */
    public boolean isStopword(String term) {
        return isStopwordStemmed(StringUtilities.stem(term));
    }

    /**
     * Tests if term is a stopword.
     *
     * @param term term to test
     * @return true if term is a stopword
     */
    public boolean isStopwordStemmed(String term) {
        return stopwords.contains(term);
    }

//	/**
//	 * Returns the minimum length for words used as stop words.
//	 *
//	 * @return minimum length for words used as stop words
//	 */
//	public static int getMinWordLength() {
//		return minWordLength;
//	}

//	/**
//	 * Sets the minimum length for words used as stop words. If the set of
//	 * stopwords has already been loaded, then this set of loaded again (but
//	 * only worls for new filters).
//	 *
//	 * @param minWordLength
//	 *                   minimum length for words used as stop words
//	 */
//	public static synchronized void setMinWordLength(int minWordLength) {
//		int oldLength = StopwordFilter.minWordLength;
//		StopwordFilter.minWordLength = minWordLength;
//		if (defaultStopwords != null && oldLength != minWordLength) {
//			defaultStopwords.clear();
//		}
//	}

}