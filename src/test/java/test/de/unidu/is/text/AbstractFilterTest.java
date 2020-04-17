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

 
// $Id: AbstractFilterTest.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import de.unidu.is.text.AbstractFilter;
import de.unidu.is.util.CollectionUtilities;

/**
 * @author nottelma
 * @since Jul 8, 2003
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class AbstractFilterTest extends TestCase {

	private AbstractFilter filter;
	private AbstractFilter complexFilter;

	/**
	 * Constructor for AbstractFilterTest.
	 * @param arg0
	 */
	public AbstractFilterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		filter = new AbstractFilter(null) {
			protected Iterator filter(Object value) {
				List list = new ArrayList();
				list.add(value);
				list.add(value + "!");
				return list.iterator();
			}
		};
		complexFilter = new AbstractFilter(new AbstractFilter(null) {
			protected Iterator filter(Object value) {
				int v = 0;
				try {
					v = Integer.parseInt(value.toString());
				} catch (Exception ex) {
					fail("Exception in complexFilter.filter(Object): " + ex);
				}
				List list = new ArrayList();
				list.add("" + (v));
				list.add("" + (v + 1));
				list.add("" + (v + 2));
				return list.iterator();
			}
		}) {
			protected Iterator filter(Object value) {
				List list = new ArrayList();
				list.add(value);
				list.add(value + "!");
				return list.iterator();
			}
		};
	}

	/*
	 * Test for Iterator apply(Object)
	 */
	public void testApplyObject() {
		List list = CollectionUtilities.toList(filter.apply("xyz"));
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), "xyz");
		assertEquals(list.get(1), "xyz!");
	}

	/*
	 * Test for Iterator apply(Iterator)
	 */
	public void testApplyIterator() {
		List l = new ArrayList();
		l.add("10");
		l.add("20");
		List list = CollectionUtilities.toList(filter.apply(l.iterator()));
		assertEquals(list.size(), 4);
		assertEquals(list.get(0), "10");
		assertEquals(list.get(1), "10!");
		assertEquals(list.get(2), "20");
		assertEquals(list.get(3), "20!");
	}

	/*
	 * Test for Iterator apply(Object)
	 */
	public void testApplyIteratorComplex() {
		List l = new ArrayList();
		l.add("10");
		l.add("20");
		List list =
			CollectionUtilities.toList(complexFilter.apply(l.iterator()));
		System.out.println(list);
		assertEquals(list.size(), 12);
		assertEquals(list.get(0), "10");
		assertEquals(list.get(1), "10!");
		assertEquals(list.get(2), "11");
		assertEquals(list.get(3), "11!");
		assertEquals(list.get(4), "12");
		assertEquals(list.get(5), "12!");
		assertEquals(list.get(6), "20");
		assertEquals(list.get(7), "20!");
		assertEquals(list.get(8), "21");
		assertEquals(list.get(9), "21!");
		assertEquals(list.get(10), "22");
		assertEquals(list.get(11), "22!");
	}

}
