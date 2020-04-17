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

 
// $Id: LowercaseFilterTest.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $

/*
 * Created on Jun 24, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package test.de.unidu.is.text;

import java.util.List;

import junit.framework.TestCase;
import de.unidu.is.text.LowercaseFilter;
import de.unidu.is.util.CollectionUtilities;

/**
 * @author nottelma
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LowercaseFilterTest extends TestCase {

	protected LowercaseFilter filter;
	
	/**
	 * Constructor for LowercaseFilterTest.
	 * @param arg0
	 */
	public LowercaseFilterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		filter = new LowercaseFilter(null);
	}

	private void test(Object str,String exp) {
		List list = CollectionUtilities.toList(filter.apply(str));
		assertTrue(list.size()==1);
		assertEquals(list.get(0),exp);
	}

	public void testFilter() {
		test(new Integer(1),"1");
		test("The brown Fox","the brown fox");
	}

}
