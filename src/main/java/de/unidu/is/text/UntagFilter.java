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


// $Id: UntagFilter.java,v 1.7 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * This filter removes XML/HTML tags from a specified string.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:28 $
 * @since 2003-07-04
 */
public class UntagFilter extends AbstractSingleItemFilter {

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public UntagFilter(Filter nextFilter) {
        super(nextFilter);
    }

    /**
     * Removes all XML/HTML tags from the specified value.
     *
     * @param value string to be converted
     * @return value without tags
     */
    public Object run(Object value) {
        if (value == null)
            return null;
        StringReader input = new StringReader(value.toString());
        StringWriter withoutTags = new StringWriter();
        boolean tagOpen = false;
        int currentChar = -2;
        while (currentChar != -1) {
            try {
                currentChar = input.read();
            } catch (IOException e) {
                currentChar = -1;
            }
            if (currentChar >= 0) {
                if (currentChar == '<') {
                    tagOpen = true;
                } else if (currentChar == '>') {
                    tagOpen = false;
                } else if (!(tagOpen)) {
                    withoutTags.write(currentChar);
                }
            } else {
                currentChar = -1; // end in any case
            }
        }
        return withoutTags.toString();
    }

}
