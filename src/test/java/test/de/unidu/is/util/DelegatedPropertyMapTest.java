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

 
// $Id: DelegatedPropertyMapTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import de.unidu.is.util.DelegatedPropertyMap;

/**
 * @author nottelma
 * @since Feb 29, 2004
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class DelegatedPropertyMapTest extends TestCase {

	private DelegatedPropertyMap map1;
	private DelegatedPropertyMap map2;

	/**
	 * Constructor for DelegatedPropertyMapTest.
	 * @param arg0
	 */
	public DelegatedPropertyMapTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		Map m = new HashMap();
		m.put("a", "42");
		m.put("b", "foo bar");
		map1 = new DelegatedPropertyMap(m);
		map2 = new DelegatedPropertyMap(new HashMap(), true);
	}

	public void testGetAll() {
		assertEquals(Collections.singletonList("42"), map1.getAll("a"));
		map1.set("a", "4711");
		assertEquals(Collections.singletonList("4711"), map1.getAll("a"));

		map2.set("a", "1");
		map2.set("a", "2");
		map2.set("a", "3");
		map2.set("b", "1");
		List l = new ArrayList();
		l.add("1");
		l.add("2");
		l.add("3");
		assertEquals(l, map2.getAll("a"));
	}

	/*
	 * Test for void remove(Object, Object)
	 */
	public void testRemoveObjectObject() {
		//TODO Implement remove().
	}

	/*
	 * Test for void DelegatedPropertyMap(Map)
	 */
	public void testDelegatedPropertyMapMap() {
		//TODO Implement DelegatedPropertyMap().
	}

	/*
	 * Test for void DelegatedPropertyMap(Map, boolean)
	 */
	public void testDelegatedPropertyMapMapboolean() {
		//TODO Implement DelegatedPropertyMap().
	}

	public void testInit() {
		//TODO Implement init().
	}

	public void testGetInitKeys() {
		//TODO Implement getInitKeys().
	}

	public void testClear() {
		assertFalse(map1.isEmpty());
		map1.clear();
		assertTrue(map1.isEmpty());
	}

	public void testContainsKey() {
		//TODO Implement containsKey().
	}

	public void testContainsValue() {
		//TODO Implement containsValue().
	}

	public void testEntrySet() {
		//TODO Implement entrySet().
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		//TODO Implement equals().
	}

	public void testIsEmpty() {
		assertFalse(map1.isEmpty());
		map1.clear();
		assertTrue(map1.isEmpty());
		map1.set("a", "aaa");
		assertFalse(map1.isEmpty());
	}

	public void testKeySet() {
		//TODO Implement keySet().
	}

	public void testPutAll() {
		//TODO Implement putAll().
	}

	/*
	 * Test for Object remove(Object)
	 */
	public void testRemoveObject() {
		//TODO Implement remove().
	}

	public void testSize() {
		assertEquals(map1.size(), 2);
		map1.set("a", "xyz");
		assertEquals(map1.size(), 2);
		map1.set("x", "Haha");
		assertEquals(map1.size(), 3);
		assertEquals(map2.size(), 0);
		map2.set("a", "1");
		assertEquals(map2.size(), 1);
		map2.set("a", "2");
		assertEquals(map2.size(), 1);
	}

	public void testValues() {
		//TODO Implement values().
	}

	public void testSetGet() {
		map1.setString("a1", "b");
		map1.setInt("a2", 1);
		map1.setLong("a3", 2);
		map1.setDouble("a4", 4.2);
		map1.setBoolean("a5", true);
		map1.setBoolean("a6", false);
		map1.setString("a7", "42");
		map1.put("a8", new Integer(1));
		assertEquals(map1.getString("a1"), "b");
		assertEquals(map1.getInt("a2"), 1);
		assertEquals(map1.getLong("a3"), 2);
		assertEquals(map1.getDouble("a4"), 4.2, 0.0001);
		assertEquals(map1.getBoolean("a5"), true);
		assertEquals(map1.getBoolean("a6"), false);
		try {
			assertEquals(map1.getInt("a1"), 2);
			fail("a1 - int");
		} catch (Exception e) {
		}
		assertEquals(map1.getBoolean("a1"), false);
		assertEquals(map1.getInt("a7"), 42);
		assertEquals(map1.getInt("a8"), 1);
	}

	public void testSetGetMultiple() {
		// TODO
	}

	public void testDelegation() {
		// delegation is created automatically, so only test basic stuff
		Map map12 = new HashMap();
		map12.put("a", "10");
		map12.put("b", "Test");
		map1 = new DelegatedPropertyMap(map12);
		assertEquals(map1.getInt("a"), 10);
		assertEquals(map1.getString("b"), "Test");
		assertTrue(!map1.containsKey("c"));
		map12.put("a", "11");
		assertEquals(map1.getInt("a"), 11);
	}

	public void testChanged() {
		final List changed = new ArrayList(); // counter for calls to change
		map1 = new DelegatedPropertyMap(new HashMap()) {
			protected void changed() {
				changed.add("*");
			}
		};
		// test setXYZ()
		map1.setString("a", "x");
		assertEquals(changed.size(), 1);
		map1.setInt("b", 1);
		map1.setDouble("c", 2.2);
		map1.setBoolean("d", true);
		assertEquals(changed.size(), 4);
		// getXYZ() should not call change
		map1.getString("a");
		map1.getInt("b");
		assertEquals(changed.size(), 4);
		// test other methods which change the map1
		map1.remove("a");
		assertEquals(changed.size(), 5);
		map1.put("x", "10");
		assertEquals(changed.size(), 6);
		map1.putAll(new HashMap());
		assertEquals(changed.size(), 7);
	}

	public void testConversion() {
		map1.setString("my.a", "${my.b}");
		assertEquals(map1.getString("my.a"), "");
		map1.setString("my.b", "42");
		assertEquals(map1.getString("my.a"), "42");
		assertEquals(map1.getDouble("my.a"), 42, 0.001);
		assertEquals(map1.getInt("my.a"), 42);
		map1.setBoolean("my.b", true);
		assertEquals(map1.getBoolean("my.a"), true);
		map1.setBoolean("my.b", false);
		assertEquals(map1.getBoolean("my.a"), false);

		// TODO: test also for map2
	}

}
