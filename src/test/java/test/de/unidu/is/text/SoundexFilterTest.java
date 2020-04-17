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

 
// $Id: SoundexFilterTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.text;

import junit.framework.TestCase;
import de.unidu.is.text.SoundexFilter;

/**
 * @author nottelma
 * @since Jul 8, 2003
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class SoundexFilterTest extends TestCase {

	private SoundexFilter filter;
	
	/**
	 * Constructor for SoundexFilterTest.
	 * @param arg0
	 */
	public SoundexFilterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		filter = new SoundexFilter(null);
	}

	public void testRunHello() {
		assertEquals(filter.run("HELLO"),filter.run("hallo"));
	}

	public void testRunNotSame() {
		assertNotSame(filter.run("hello"),filter.run("world"));
	}

}
