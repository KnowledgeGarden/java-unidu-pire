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

 
// $Id: HTMLFilterTest.java,v 1.5 2005/03/09 09:00:05 nottelma Exp $
package test.de.unidu.is.text;

import junit.framework.TestCase;
import de.unidu.is.text.HTMLFilter;

/**
 * @author nottelma
 * @since Jul 8, 2003
 * @version $Revision: 1.5 $, $Date: 2005/03/09 09:00:05 $
 */
public class HTMLFilterTest extends TestCase {

	private HTMLFilter filter;
	
	/**
	 * Constructor for SoundexFilterTest.
	 * @param arg0
	 */
	public HTMLFilterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		filter = new HTMLFilter(null);
	}

	public void testRun() {
		assertEquals(filter.run("<font><b>Hello</b> world</font>"),"Hello world");
	}

	public void testRunComment() {
		assertEquals(filter.run("<!--<b>Hello</b> -->world"),"world");
	}

	public void testRunScript() {
		assertEquals(filter.run("<script><b>Hello</b></script> world"),"world");
	}

	public void testRunP() {
		assertEquals(filter.run("<p>Hello</p>"),"\n\nHello");
	}

	public void testRunBR() {
		assertEquals(filter.run("Hello<br>"),"Hello\n\n");
	}

	public void testRunEntities() {
		assertEquals(filter.run("&auml;&nbsp;&Ouml;&#13;&#10;&#9;"),"ä Ö\r\n\t");
	}

}
