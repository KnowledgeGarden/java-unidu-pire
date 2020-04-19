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


// $Id: PorterStemmer.java,v 1.7 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

/**
 * This class provides the PORTER stemming algorithm. This class will be
 * removed sooner or later, please use
 * <code>de.unidu.is.text.StemmerFilter</code> or
 * <code>StringUtilities.stem(String)</code> instead.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/28 22:27:55 $
 * @see de.unidu.is.util.StringUtilities#stem(String)
 * @see de.unidu.is.text.StemmerFilter
 * @since 2003-07-03
 * @deprecated Use <code>StringUtilities.stem(String)</code> instead.
 */
public class PorterStemmer {

    /**
     * Returns the stemmed word.
     *
     * @param str word to stem
     * @return stemmed word
     * @deprecated Use <code>StringUtilities.stem(String)</code> instead.
     */
    public static String getStemmed(String str) {
        return StringUtilities.stem(str);
    }

    /**
     * Returns the stemmed word.
     *
     * @param str word to stem
     * @return stemmed word
     * @deprecated Use <code>StringUtilities.stem(String)</code> instead.
     */
    public static String stem(String str) {
        return StringUtilities.stem(str);
    }

}
