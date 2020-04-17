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

 
// $Id: DBPropertyMapTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.util;

import junit.framework.TestCase;
import de.unidu.is.util.DB;
import de.unidu.is.util.DBPropertyMap;
import de.unidu.is.util.HashPropertyMap;
import de.unidu.is.util.PropertyMap;

/**
 * @author nottelma
 * @since Jul 8, 2003
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class DBPropertyMapTest extends TestCase {

	/**
	 * Constructor for DBPropertyMapTest.
	 * @param arg0
	 */
	public DBPropertyMapTest(String arg0) {
		super(arg0);
	}


	public static void main(String[] args) throws Exception {
		PropertyMap map = new HashPropertyMap();
		map.setString(
			"database.uri",
			"jdbc:mysql://mind.is.informatik.uni-duisburg.de/mind");
		map.setString("database.driver", "org.gjt.mm.mysql.Driver");
		map.setString("database.user", "mind");
		map.setString("database.password", "mind");
		System.out.println(map);
		DB db = new DB(map, "database");
		DBPropertyMap dbMap =
			new DBPropertyMap(db, "google_rd", "xpath", "value");
		System.out.println(
			dbMap.get(
				"/resourceDescriptor/featuresOfContent/rs/costs/cost[@type='time']/@start"));

		dbMap.put("a", "b");
		System.out.println(dbMap.get("a"));
		dbMap.put("a", "bb");
		System.out.println(dbMap.get("a"));
		System.out.println(dbMap.size());
		dbMap.remove("a");
		System.out.println(dbMap.size());
		System.out.println(dbMap.entrySet());
		System.out.println(dbMap.values());
		System.out.println(dbMap.containsValue("6888.02"));
		System.exit(0);
	}

}
