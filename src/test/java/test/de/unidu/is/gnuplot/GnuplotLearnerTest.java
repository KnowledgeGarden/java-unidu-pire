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

 
// $Id: GnuplotLearnerTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package test.de.unidu.is.gnuplot;

import java.util.Map;

import junit.framework.TestCase;
import de.unidu.is.learning.Learner;
import de.unidu.is.learning.LearnerFactory;

/**
 * @author Henrik Nottelmann
 * @since 2003-07-26
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class GnuplotLearnerTest extends TestCase {

	private Learner learner;
	
	/**
	 * Constructor for GnuplotLearnerTest.
	 * @param arg0
	 */
	public GnuplotLearnerTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		learner = LearnerFactory.newLearner();
	}

	/*
	 * Test for Map learn(double[], double[], String, String[])
	 */
	public void testLearndoubleArraydoubleArrayStringStringArray1() {
		Map results =
			learner.learn(
				new double[] { 1, 2, 5, 6 },
				new double[] { 3, 4, 7, 8 },
				"f(y)=l*y+a",
				new String[] { "l", "a" });
		assertEquals(((Double) results.get("l")).doubleValue(),1,0.0001);
		assertEquals(((Double) results.get("a")).doubleValue(),2,0.0001);
	}

	/*
	 * Test for Map learn(double[], double[], String, String[])
	 */
	public void testLearndoubleArraydoubleArrayStringStringArray2() {
		Map results =
			learner.learn(
				new double[] { 1, 2, 5, 6 },
				new double[] { 3, 6, 15, 18 },
				"f(y)=l*y+a",
				new String[] { "l", "a" });
		assertEquals(((Double) results.get("l")).doubleValue(),3,0.0001);
		assertEquals(((Double) results.get("a")).doubleValue(),0,0.0001);
	}

	/*
	 * Test for Map learn(String, String, String, String[])
	 */
	public void testLearnStringStringStringStringArray() {
		// no test as already tested with testLearndoubledArrayStringArray
	}

}
