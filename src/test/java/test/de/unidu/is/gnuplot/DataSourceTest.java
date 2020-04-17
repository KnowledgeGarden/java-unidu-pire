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

 
// $Id: DataSourceTest.java,v 1.5 2005/03/09 09:00:19 nottelma Exp $
package test.de.unidu.is.gnuplot;

import java.io.StringWriter;

import de.unidu.is.gnuplot.DataSource;

/**
 * @author nottelma
 * @since Aug 2, 2003
 * @version $Revision: 1.5 $, $Date: 2005/03/09 09:00:19 $
 */
public class DataSourceTest extends FileSourceTest {

	private DataSource dataSource;
	private final double[] x = { 0, 2, 5, 6 };
	private final double[] y = { 1, 2, 3, 4 };

	/**
	 * Constructor for DataSourceTest.
	 * @param arg0
	 */
	public DataSourceTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		dataSource = new DataSource(TITLE, FILENAME) {
			private int count = -1;
			public boolean next() {
				return count++ < x.length - 1;
			}
			public double getX() {
				return x[count];
			}
			public double getY() {
				return y[count];
			}
		};
		fileSource = dataSource;
		source = dataSource;
	}

//	public void testGetCommand() {
//		new File(FILENAME).delete();		
//		super.testGetCommand();
//		assertTrue(new File(FILENAME).exists());		
//	}

	public void testWrite() {
		StringWriter sw = new StringWriter();
		dataSource.write(sw);
		assertEquals(
			sw.toString(),
			x[0]
				+ " "
				+ y[0]
				+ "\n"
				+ x[1]
				+ " "
				+ y[1]
				+ "\n"
				+ x[2]
				+ " "
				+ y[2]
				+ "\n"
				+ x[3]
				+ " "
				+ y[3]
				+ "\n");
	}

	public void testNext() {
		assertTrue(dataSource.next());
		assertTrue(dataSource.next());
		assertTrue(dataSource.next());
		assertTrue(dataSource.next());
		assertFalse(dataSource.next());
	}

	public void testGetX() {
		dataSource.next();
		assertEquals(dataSource.getX(), x[0], EPS);
		assertEquals(dataSource.getX(), x[0], EPS);
		dataSource.next();
		assertEquals(dataSource.getX(), x[1], EPS);
		dataSource.next();
		assertEquals(dataSource.getX(), x[2], EPS);
		dataSource.next();
		assertEquals(dataSource.getX(), x[3], EPS);
	}

	public void testGetY() {
		dataSource.next();
		assertEquals(dataSource.getY(), y[0], EPS);
		assertEquals(dataSource.getY(), y[0], EPS);
		dataSource.next();
		assertEquals(dataSource.getY(), y[1], EPS);
		dataSource.next();
		assertEquals(dataSource.getY(), y[2], EPS);
		dataSource.next();
		assertEquals(dataSource.getY(), y[3], EPS);
	}

}
