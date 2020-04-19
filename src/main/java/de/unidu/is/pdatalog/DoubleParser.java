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


// $Id: DoubleParser.java,v 1.4 2005/02/21 17:29:21 huesselbeck Exp $
package de.unidu.is.pdatalog;


/**
 * A parser class for double values which can handle null objects.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:21 $
 * @since 2004-04-18
 */
public class DoubleParser {

    /**
     * Returns a new <code>double</code> initialized to the value
     * represented by the specified <code>String</code>, or <code>0</code>
     * if <code>null</code> is specified.
     *
     * @param o string to parse
     * @return double value
     */
    public static double parseDouble(String o) {
        if (o == null)
            return 0;
        return Double.parseDouble(o);
    }
}
