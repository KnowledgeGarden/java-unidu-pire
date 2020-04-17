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

 
// $Id: BasicToolTest.java,v 1.8 2005/02/21 17:29:29 huesselbeck Exp $

package test.de.unidu.is.gui;

import java.util.EventObject;

import javax.swing.DefaultListModel;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import de.izsoz.fiola.dispatcher.Dispatcher;
import de.izsoz.fiola.dispatcher.EventReciever;
import de.izsoz.fiola.wob.desktop.Desktop;
import de.izsoz.fiola.wob.desktop.ToolController;
import de.unidu.is.gui.Application;
import de.unidu.is.gui.BasicTool;
import de.unidu.is.gui.NewListModelEvent;

/**
 * @author schaefer
 * @version $Revision: 1.8 $, $Date: 2005/02/21 17:29:29 $
 */
public class BasicToolTest extends TestCase implements EventReciever {

	static Application app = new Application();
	static Desktop desk = app.getDesktopInstance();
	static BasicTool bt = null;
	
	private static Logger LOG = Logger.getLogger(BasicToolTest.class);
	private static ToolController tc= ToolController.getInstance();
	/**
	 * Constructor for HistoryToolTest.
	 * @param name
	 */
	public BasicToolTest(String name) {
		super(name);
				
	}

	/* (Kein Javadoc)
	 * @see de.izsoz.fiola.dispatcher.EventReciever#handleIZEvent(java.util.EventObject)
	 */
	public boolean handleIZEvent(EventObject ev) {
		LOG.debug(ev);
		return false;
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		java.net.URL url = BasicToolTest.class.getResource("/daffodil.lcf");
		org.apache.log4j.PropertyConfigurator.configure(url);
	}

	/**
	 * tests if tool and view can be created errorfree.
	 *
	 */
	public void testCreateView() {
		
		bt = new BasicTool();
		assertNotNull(bt);
		
		
	}
	
	public void testToolOpens() throws InterruptedException{
		app.setVisible(true);
		tc.addTool(bt);
		bt.doClick();
		Thread.sleep(1000);
	}
   /**
    * Testet, ob der ToolView etwas anzeigt, wenn man ein neues List Modell 
    * über den Dispatcher broadcastet. Das BasicTool lauscht auf solche Events 
    * und wenn einer eintrifft, öffnet es seinen View und reicht das LIst Modell 
    * daran weiter. Man kann noch dahingehend verbessern, dass man die 
    * Weitergabe an den View auch über Events und loses koppeln 
    * (etwa über allgemeine PropertyChanged Events) weitergibt. 
    * Durch die Enge Kopplung waren public setters (setModel) für den 
    * View und das eingebettete Panel nötig. 
    *
    */
	public void testToolDisplays() {
		DefaultListModel model = new DefaultListModel();
		model.addElement("Erstens");
		model.addElement("Zweitens");
		model.addElement("Drittens");
		model.addElement("Viertens");
	
		Dispatcher.sendEvent(new NewListModelEvent(this,model)); 	
	}
	
	/** handles IZEvents */
	public void testHandleIZEvent() {
		
		
	}
	

}
