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

 
// $Id: MySQLDB.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import org.apache.log4j.Logger;

/**
 * This class encapsulates a MySQL RDBMS (using JDBC), where connections are 
 * managed by a connection pool.<p>
 * 
 * This class uses the logger "unidu.db" by default.
 * 
 * @author Henrik Nottelmann
 * @since 2004-01-04
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class MySQLDB extends DB {

	/**
	 * Name of the old driver.
	 */
	private static final String DRIVERNAME_OLD = "org.gjt.mm.mysql.Driver";
	
	/**
	 * Name of the new driver.
	 */
	private static final String DRIVERNAME_NEW = "com.mysql.jdbc.Driver";

	/**
	 * Name of the driver.
	 */
	private static final String DRIVERNAME = DRIVERNAME_NEW;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param host MySQL server host
	 * @param db MySQL database
	 * @param user MySQL user
	 * @param password MySQL password
	 */
	public MySQLDB(String host, String db, String user, String password) {
		super("jdbc:mysql://" + host + "/" + db,DRIVERNAME,user,password);
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param host MySQL server host
	 * @param db MySQL database
	 * @param user MySQL user
	 * @param password MySQL password
	 * @param logger DB logger
	 */
	public MySQLDB(String host, String db, String user, String password,Logger logger) {
		super("jdbc:mysql://" + host + "/" + db,DRIVERNAME,user,password,logger);
	}

}
