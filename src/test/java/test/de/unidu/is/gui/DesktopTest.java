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

 
// $Id: DesktopTest.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $

package test.de.unidu.is.gui;

import de.unidu.is.gui.Desktop;
import junit.framework.TestCase;

/**
 * @author schaefer
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class DesktopTest extends TestCase {
	private static Desktop desk;
	/**
	 * Constructor for DesktopTest.
	 * @param arg0
	 */
	public DesktopTest(String arg0) {
		super(arg0);
	}
	public void testDesktopCreation() {
		desk= new Desktop();
		assertNotNull("Test if View is created", desk);
	}
	public void testDesktopDisplays() throws InterruptedException {
		javax.swing.JFrame frame= new javax.swing.JFrame();
		frame.setContentPane(desk);
		
		frame.setSize(desk.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.setVisible(true);
		desk.setVisible(true);
		assertTrue(desk.isDisplayable());
		Thread.sleep(1000);
	}
}
