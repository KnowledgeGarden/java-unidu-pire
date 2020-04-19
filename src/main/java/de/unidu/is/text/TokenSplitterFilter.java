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


// $Id: TokenSplitterFilter.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This filter splits a string into tokens. First, character which is not a
 * letter and not a digit is converted into a whitespace. Then, the resulting
 * string is split into tokens (the whitespaces are the token boundaries).
 *
 * @author Henrik Nottelmann
 * @version $Revision $, $Date: 2005/02/28 22:27:55 $
 * @since 2003-07-03
 */
public class TokenSplitterFilter extends AbstractFilter {

    /**
     * Minimum token length.
     */
    private int length = 2;

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public TokenSplitterFilter(Filter nextFilter) {
        super(nextFilter);
    }

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public TokenSplitterFilter(Filter nextFilter, int length) {
        super(nextFilter);
        setLength(length);
    }

    /**
     * Applies only this filter on the specified object, without considering
     * the other filters from the filter chain.<p>
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
        StringBuffer buffer =
                value instanceof StringBuffer
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
        length = i;
    }

    /**
     * Handles the specified buffer before splitting it into tokens.<p>
     * <p>
     * The current implementation replaces every character which is not a
     * letter and not a digit by space.
     *
     * @param buffer string buffer to be handled
     */
    protected static void handleBuffer(StringBuffer buffer) {
        for (int i = 0; i < buffer.length(); i++) {
            char c = buffer.charAt(i);
            if (c != ' ' && !Character.isLetterOrDigit(c))
                buffer.replace(i, i + 1, " ");
        }
    }
}
