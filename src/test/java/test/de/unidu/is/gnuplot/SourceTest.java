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

 
// $Id: SourceTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.gnuplot;

import junit.framework.TestCase;
import de.unidu.is.gnuplot.Source;

/**
 * @author Henrik Nottelmann
 * @since 2003-08-02
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class SourceTest extends TestCase {

	protected Source source;
	private final double VALUE1 = 10;
	private final double VALUE2 = 1;
	protected final double EPS = 0.00001;
	protected final String TITLE = "NT";

	/**
	 * Constructor for SourceTest.
	 * @param arg0
	 */
	public SourceTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		source = new Source(TITLE) {
			public String getCommand() {
				return null;
			}
		};
	}

	public void testSetGetMin() {
		source.setMin(VALUE1);
		assertEquals(source.getMin(),VALUE1,EPS);
		source.setMin(VALUE2);
		assertEquals(source.getMin(),VALUE2,EPS);
	}

	public void testSetGetMax() {
		source.setMax(VALUE1);
		assertEquals(source.getMax(),VALUE1,EPS);
		source.setMax(VALUE2);
		assertEquals(source.getMax(),VALUE2,EPS);
	}

	public void testGetTitle() {
		assertEquals(source.getTitle(), TITLE);
	}


}
