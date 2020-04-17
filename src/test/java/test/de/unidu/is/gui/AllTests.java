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

 
// $Id: AllTests.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $

package test.de.unidu.is.gui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author schaefer
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class AllTests {
	public static void main(String[] args) {
	}
	public static Test suite() {
		TestSuite suite= new TestSuite("Test for test.de.unidu.is.gui");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(BasicViewTest.class));
		suite.addTest(new TestSuite(DesktopTest.class));
		suite.addTest(new TestSuite(ApplicationTest.class));
		suite.addTest(new TestSuite(BasicToolTest.class));
		//$JUnit-END$
		return suite;
	}
}
