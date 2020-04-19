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


// $Id: StringUtilities.java,v 1.8 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import de.unidu.is.text.*;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class provides some convenient static methods for handling strings.
 * Some of the methods simply use filters from <code>de.unidu.is.text</code>.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/02/21 17:29:29 $
 * @since 2003-07-05
 */
public class StringUtilities {

    /**
     * The stemmer filter.
     */
    private static final SoundexFilter soundex;

    /**
     * The stemmer filter.
     */
    private static final StemmerFilter stemmer;

    /**
     * The stemmer filter.
     */
    private static final StopwordFilter stopwords;

    /**
     * The stemmer filter.
     */
    private static final WordSplitterFilter splitter;

    /**
     * The stemmer filter.
     */
    private static final ParserFilter parser;

    /**
     * The tag removing filter.
     */
    private static final UntagFilter untag;

    /**
     * The HTML extracting filter.
     */
    private static final HTMLFilter html;

    /*
     * Initialise the static members.
     *
     */
    static {
        soundex = new SoundexFilter(null);
        stemmer = new StemmerFilter(null);
        stopwords = new StopwordFilter(null);
        splitter = new WordSplitterFilter(null);
        parser = new ParserFilter();
        untag = new UntagFilter(null);
        html = new HTMLFilter(null);
    }

    /**
     * Implodes the array by concatenating all elements, separated by the
     * specified string.
     *
     * @param array     array whose elements have to be concatenated
     * @param separator string used for separating the array elements
     */
    public static String implode(Object[] array, String separator) {
        StringBuilder buf = new StringBuilder();
        for (Object o : array) buf.append(o).append(separator);
        String ret = buf.toString();
        if (ret.endsWith(separator))
            ret = ret.substring(0, ret.length() - separator.length());
        return ret;
    }

    /**
     * Implodes the collection by concatenating all elements, separated by the
     * specified string.
     *
     * @param collection collection whose elements have to be concatenated
     * @param separator  string used for separating the colleciton elements
     */
    public static String implode(Collection collection, String separator) {
        StringBuilder buf = new StringBuilder();
        for (Object o : collection) buf.append(o).append(separator);
        String ret = buf.toString();
        if (ret.endsWith(separator))
            ret = ret.substring(0, ret.length() - separator.length());
        return ret;
    }

    /**
     * Converts some characters in a string into entities:
     * <p>
     * These characters are converted:
     * <ul>
     * <li>&szlig;</li>
     * <li>&quot;</li>
     * <li>&lt;</li>
     * <li>&gt;</li>
     * <li>&amp;</li>
     * </ul>
     *
     * @param text text
     * @return converted text
     */
    public static String toXML(String text) {
        text = replace(text, "&", "&amp;");
        text = replace(text, "\"", "&quot;");
        text = replace(text, "<", "&lt;");
        text = replace(text, ">", "&gt;");
        return text;

    }

    /**
     * Resolves some entities in an XML string.
     * <p>
     * These entities are resolved:
     * <ul>
     * <li>&amp;szlig;</li>
     * <li>&amp;quot;</li>
     * <li>&amp;lt;</li>
     * <li>&amp;gt;</li>
     * <li>&amp;amp;</li>
     * </ul>
     *
     * @param text XML text
     * @return converted text
     */
    public static String fromXML(String text) {
        text = replace(text, "&quot;", "\"");
        text = replace(text, "&lt;", "<");
        text = replace(text, "&gt;", ">");
        text = replace(text, "&amp;", "&");
        return text;
    }

    /**
     * Removes all occurences of a string from another string.
     *
     * @param str      string to modify
     * @param matchStr string to remove
     * @return modified string
     */
    public static String remove(String str, String matchStr) {
        return replace(str, matchStr, "");
    }

    /**
     * Replaces all occurences of a string by another string.
     *
     * @param str        string to modify
     * @param matchStr   string to replace
     * @param replaceStr replacement string
     * @return modified string
     */
    public static String replace(
            String str,
            String matchStr,
            String replaceStr) {
        if (str == null)
            return null;
        int i;
        int h = 0;
        StringBuilder returnStr = new StringBuilder();
        while ((i = str.indexOf(matchStr, h)) != -1) {
            returnStr.append(str, h, i).append(replaceStr);
            h = i + matchStr.length();
        }
        returnStr.append(str.substring(h));
        return returnStr.toString();
    }

    /*
     * Methods calling filters
     */

    /**
     * Returns the soundex representation of a string.
     *
     * @param text string to convert
     * @return soundex representation
     */
    public static String getSoundex(String text) {
        return soundex.run(text).toString();
    }

    /**
     * Removes all tags from a string.
     *
     * @param str string to modify
     * @return modified string
     */
    public static String removeTags(String str) {
        return untag.run(str).toString();
    }

    /**
     * Extracts the text from HTML, removes all tags, replaces
     * well-known entities and removes the rest of them.
     *
     * @param content HTML string
     * @return text embedded in the HTML
     */
    public static String extractFromHTML(String content) {
        return html.run(content).toString();
    }

    /**
     * Returns the stemmed term.
     *
     * @param term string to be stemmed
     * @return stemed string
     */
    public static String stem(String term) {
        return stemmer.run(term).toString();
    }

    /**
     * Tests whether the specified (unstemmed) term is a stopword.
     *
     * @param term term to be tested
     * @return true iff the term is a stopword
     */
    public static boolean isStopword(String term) {
        return stopwords.isStopword(term);
    }

    /**
     * Tests whether the specified (already stemmed) term is a stopword.
     *
     * @param term term to be tested
     * @return true iff the term is a stopword
     */
    public static boolean isStopwordStemmed(String term) {
        return stopwords.isStopwordStemmed(term);
    }

    /**
     * Returns an iterator over all (stemmed) terms embedded in the specified
     * string, after removing stopwords. Every non-letter character is
     * considered as a whitespace, and terms with less than 3 characters are
     * discarded.
     *
     * @param text text to be parsed
     * @return iterator over the terms in the specified text
     */
    public static Iterator parseText(String text) {
        return parser.apply(text);
    }

    /**
     * Formats the specified number. If the resulting string is shorter
     * than the specified lenght, it is filled (at the beginning) with zeros.
     *
     * @param num    number to format
     * @param length exact length of the resulting string
     * @return formatted number as a string, filled with zeros if needed
     */
    public static String toString(int num, int length) {
        String pattern = "000000000000000000000000000000000".substring(0, length);
        return new DecimalFormat(pattern).format(num);
    }

}
