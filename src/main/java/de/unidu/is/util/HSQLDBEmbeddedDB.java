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


// $Id: HSQLDBEmbeddedDB.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import org.apache.log4j.Logger;

/**
 * This class encapsulates a HSQLDB embedded (in memory) RDBMS (using JDBC),
 * where connections are managed by a connection pool.<p>
 * <p>
 * This class uses the logger "unidu.db" by default.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 * @since 2004-01-04
 */
public class HSQLDBEmbeddedDB extends DB {

    /**
     * Creates a new instance.
     */
    public HSQLDBEmbeddedDB() {
        super("jdbc:hsqldb:.", "org.hsqldb.jdbcDriver", "sa", "");
    }

    /**
     * Creates a new instance.
     *
     * @param logger DB logger
     */
    public HSQLDBEmbeddedDB(Logger logger) {
        super("jdbc:hsqldb:.", "org.hsqldb.jdbcDriver", "sa", "", logger);
    }

}
