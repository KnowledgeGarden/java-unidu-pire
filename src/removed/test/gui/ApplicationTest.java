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

 
// $Id: ApplicationTest.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $

package test.de.unidu.is.gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import junit.framework.TestCase;
import de.unidu.is.gui.Application;
/**
 * @author schaefer
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public class ApplicationTest extends TestCase {
	private static Application app;
	/**
	 * Constructor for ApplicationTest.
	 * @param arg0
	 */
	public ApplicationTest(String arg0) {
		super(arg0);
	}
	/*
	 * Test für void Application()
	 */
	public void testApplication() {
		app= new Application();
		assertNotNull(app);
	}
	public void testApplicationDisplays() {
		assertNotNull(app);
		Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
		/* Pack frame on the screen */
		app.pack();
		/* Center frame on the screen */
		Dimension frameSize= app.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height= screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width= screenSize.width;
		}
		app.setLocation(
			(screenSize.width - frameSize.width) / 2,
			(screenSize.height - frameSize.height) / 2);
		/* Add a windowListener for the windowClosedEvent */
		app.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		app.setVisible(true);
	}
	public void testGetDesktop() {
	}
	public void testGetDesktopInstance() {
	}
}
