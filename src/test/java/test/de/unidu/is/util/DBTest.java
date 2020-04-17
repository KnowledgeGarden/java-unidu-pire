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

 
// $Id: DBTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.util;

import java.sql.ResultSet;

import junit.framework.TestCase;
import de.unidu.is.util.DB;
import de.unidu.is.util.HashPropertyMap;
import de.unidu.is.util.PropertyMap;

/**
 * @author nottelma
 * @since Jul 7, 2003
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class DBTest extends TestCase {

	/**
	 * Constructor for DBTest.
	 * @param arg0
	 */
	public DBTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) throws Exception {
		PropertyMap map = new HashPropertyMap();
		map.setString("database.uri","jdbc:mysql://mind.is.informatik.uni-duisburg.de/mind");
		map.setString("database.driver","org.gjt.mm.mysql.Driver");
		map.setString("database.user","mind");
		System.out.println(1);
		map.setString("database.password","mind");
		System.out.println(map);
		DB db = new DB(map,"database");
		System.out.println(db);
		ResultSet rs = db.executeQuery("select * from google_rd");
		while(rs.next()) { 
			System.out.println(rs.getString("xpath") + "=" + rs.getString("value"));
		}
		db.close(rs);
		System.out.println("FINISHED");
		System.exit(0);
	}
}
