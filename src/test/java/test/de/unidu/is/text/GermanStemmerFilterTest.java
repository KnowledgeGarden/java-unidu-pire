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

 
// $Id: GermanStemmerFilterTest.java,v 1.1 2005/03/19 23:52:11 nottelma Exp $
package test.de.unidu.is.text;

import junit.framework.TestCase;
import de.unidu.is.text.GermanStemmerFilter;

/**
 * @author nottelma
 * @since Jul 8, 2003
 * @version $Revision: 1.1 $, $Date: 2005/03/19 23:52:11 $
 */
public class GermanStemmerFilterTest extends TestCase {

	private GermanStemmerFilter filter;
	
	/**
	 * Constructor for SoundexFilterTest.
	 * @param arg0
	 */
	public GermanStemmerFilterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		filter = new GermanStemmerFilter(null);
	}

	public void testRun() {
		assertEquals(filter.run("deutscher"),filter.run("deutsch"));
	}

	public void testRunIdemPotency() {
		assertEquals(filter.run("deutscher"),filter.run("deutsch"));
	}

	public void testRunNotSame() {
		assertNotSame(filter.run("deutsch"),filter.run("welt"));
	}

}
