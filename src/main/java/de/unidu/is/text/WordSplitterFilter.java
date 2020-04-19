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


// $Id: WordSplitterFilter.java,v 1.11 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This filter splits a string into tokens. First, all non-letter characters are
 * converted into whitespaces. Then, the resulting string is split into tokens
 * (the whitespaces are the token boundaries). Only tokens with at least 3
 * characters are returned.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.11 $, $Date: 2005/02/21 17:29:28 $
 * @since 2003-07-03
 */
public class WordSplitterFilter extends AbstractFilter {

    /**
     * The minimum token length, <code>3</code> by default.
     */
    private int length = 3;

    /**
     * By default, digits are not allowed in the output (i.e. they are removed).
     */
    private boolean allowDigits = false;

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public WordSplitterFilter(Filter nextFilter) {
        super(nextFilter);
    }

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public WordSplitterFilter(Filter nextFilter, int length) {
        super(nextFilter);
        setLength(length);
    }

    /**
     * Applies only this filter on the specified object, without considering the
     * other filters from the filter chain.
     * <p>
     * <p>
     * This method splits a string into tokens. First, all non-letter characters
     * are converted into whitespaces. Then, the resulting string is split into
     * tokens (the whitespaces are the token boundaries). Only tokens with at
     * least 3 characters are returned.
     *
     * @param value value to be modified by this filter
     * @return iterator over the resulting objects
     * @see de.unidu.is.text.AbstractFilter#filter(java.lang.Object)
     */
    protected Iterator filter(Object value) {
        List retList = new ArrayList();
        StringBuffer buffer = value instanceof StringBuffer
                ? (StringBuffer) value
                : new StringBuffer(value.toString());
        handleBuffer(buffer);
        List list = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(buffer.toString());
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.length() >= getLength())
                list.add(token);
        }
        return list.iterator();
    }

    /**
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * @param i
     */
    public void setLength(int i) {
        if (i != -1)
            length = i;
    }

    /**
     * Returns whether digits are allowed in the output.
     *
     * @return true iff digits are allowed in the output
     */
    public boolean isAllowDigits() {
        return allowDigits;
    }

    /**
     * Sets whether digits are allowed in the output.
     *
     * @param allowDigits if true, digits are allowed in the output
     */
    public void setAllowDigits(boolean allowDigits) {
        this.allowDigits = allowDigits;
    }

    /**
     * Handles the specified buffer before splitting it into tokens.
     * <p>
     * <p>
     * The current implementation replaces every non-letter character by a
     * space.
     *
     * @param buffer string buffer to be handled
     */
    protected void handleBuffer(StringBuffer buffer) {
        for (int i = 0; i < buffer.length(); i++) {
            char c = buffer.charAt(i);
            if (c != ' '
                    && ((!Character.isUnicodeIdentifierStart(c) && !allowDigits) || (!Character
                    .isLetterOrDigit(c) && allowDigits)))
                buffer.replace(i, i + 1, " ");
        }
    }

}
