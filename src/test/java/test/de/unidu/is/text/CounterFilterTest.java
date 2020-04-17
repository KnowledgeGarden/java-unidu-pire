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

 
// $Id: CounterFilterTest.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.text;

import java.util.Set;

import junit.framework.TestCase;
import de.unidu.is.text.CounterFilter;
import de.unidu.is.text.Filter;
import de.unidu.is.text.WordSplitterFilter;
import de.unidu.is.util.CollectionUtilities;
import de.unidu.is.util.Tuple;

/**
 * @author nottelma
 * @since 2003-09-11
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class CounterFilterTest extends TestCase {

	private Filter filter;

	/**
	 * Constructor for SoundexFilterTest.
	 * @param arg0
	 */
	public CounterFilterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		filter = new CounterFilter(new WordSplitterFilter(null));
	}

	public void testRun() {
		Set set =
			CollectionUtilities.toSet(
				filter.apply("the quick brown fox jumps over the fox fence"));
		assertEquals(set.size(), 7);
		assertTrue(
			set.contains(new Tuple(new Object[] { "the", new Integer(2)})));
		assertTrue(
			set.contains(new Tuple(new Object[] { "quick", new Integer(1)})));
		assertTrue(
			set.contains(new Tuple(new Object[] { "brown", new Integer(1)})));
		assertTrue(
			set.contains(new Tuple(new Object[] { "fox", new Integer(2)})));
		assertTrue(
			set.contains(new Tuple(new Object[] { "jumps", new Integer(1)})));
		assertTrue(
			set.contains(new Tuple(new Object[] { "over", new Integer(1)})));
		assertTrue(
			set.contains(new Tuple(new Object[] { "fence", new Integer(1)})));
	}

}
