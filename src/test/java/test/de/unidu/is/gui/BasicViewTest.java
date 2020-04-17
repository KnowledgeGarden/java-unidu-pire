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

 
// $Id: BasicViewTest.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $

package test.de.unidu.is.gui;
import junit.framework.TestCase;
import de.unidu.is.gui.BasicView;
/**
 * @author schaefer
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class BasicViewTest extends TestCase {
	private static BasicView view;
	/**
	 * Constructor for BasicViewTest.
	 * @param arg0
	 */
	public BasicViewTest(String arg0) {
		super(arg0);
	}
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
	public void testBasicViewCreation() {
		view= new BasicView();
		assertNotNull("Test if View is created", view);
	}
	public void testBasicViewDisplays() throws InterruptedException {
		javax.swing.JFrame frame= new javax.swing.JFrame();
		frame.setContentPane(view);
		
		frame.setSize(view.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.setVisible(true);
		view.setVisible(true);
		assertTrue(view.isDisplayable());
		Thread.sleep(1000);
	}
}
