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

// $Id: HSQLDBFileDB.java,v 1.1 2005/02/25 12:50:35 nottelma Exp $
package de.unidu.is.util;

import java.io.File;

/**
 * This class encapsulates a HSQLDB 1.7.1 RDBMS (using JDBC) which saves data
 * into files, where connections are managed by a connection pool.
 * <p>
 * 
 * For HSQLDB 1.7.2, the DB URI prefix must be "jdbc:hsqldb:file:".
 * <p>
 * 
 * This class uses the logger "unidu.db" by default.
 * 
 * @author Henrik Nottelmann
 * @since 2005-02-25
 * @version $Revision: 1.1 $, $Date: 2005/02/25 12:50:35 $
 */
public class HSQLDBFileDB extends DB {

	/**
	 * Creates a new instance.
	 */
	public HSQLDBFileDB(String filename) {
		super("jdbc:hsqldb:" + filename, "org.hsqldb.jdbcDriver", "sa", "");
	}

	/**
	 * Creates a new instance.
	 */
	public HSQLDBFileDB(File file) {
		super("jdbc:hsqldb:" + file, "org.hsqldb.jdbcDriver", "sa", "");
	}

}