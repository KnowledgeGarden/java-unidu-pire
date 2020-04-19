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


// $Id: SQLFormatterFactory.java,v 1.4 2005/02/21 17:29:27 huesselbeck Exp $
package de.unidu.is.sql;

import de.unidu.is.util.DB;

/**
 * A factory for SQLFormatter objects.<p>
 * <p>
 * The factory easily allows for using different SQLFormatter classes for
 * different databases, in case that the configuration file provided by
 * the standard implementation is not sufficient.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:27 $
 * @since 2003-11-25
 */
public enum SQLFormatterFactory { ;
//
//    /**
//     * A semaphor for creating the factotry.
//     */
//    private static final Object sem = new Object();





    /**
     * Returns a new SQL formatter.
     *
     * @param db database parameters
     * @return SQL formatter for that database
     */
    public static SQLFormatter newFormatter(DB db) {
        return new SQLFormatterImplementation(db);
    }

}
