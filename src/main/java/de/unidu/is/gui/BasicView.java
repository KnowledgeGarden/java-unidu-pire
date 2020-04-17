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

 
//$Id: BasicView.java,v 1.7 2005/02/21 17:29:19 huesselbeck Exp $

package de.unidu.is.gui;
import javax.swing.ListModel;

import de.izsoz.fiola.wob.desktop.InternalView;
import de.izsoz.fiola.wob.desktop.Tool;
/**
 * A Basic view to show the structure. Clone this for own Classes.
 * Creation date: (20.09.2000 16:03:21)
 * @author: André Schaefer
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:19 $
 */
public class BasicView extends InternalView {
	private de.unidu.is.gui.BasicPanel basicPanel = null;
	/**
	 * DefaultView constructor comment.
	 */
	public BasicView() {
		super();
		initialize();
	}
	/**
	 * DefaultView constructor comment.
	 * @param aTool de.izsoz.fiola.wob.desktop.Tool
	 */
	public BasicView(Tool aTool) {
		super(aTool);
		initialize();
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		// TODO Output via Log4J  or System out... Or popup a dialog?
		throw new RuntimeException("BasicViewInternalError");
	}
	/**
	 * Initialize the class.
	 */
	protected void initialize() {
		try {
			this.setContentPane(getBasicPanel());  // Generated
			setName("DefaultView");
			setSize(282, 226);
			setTitle("Dummy Title");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		if (parentTool != null) {
			// Overwrite title String, if parent Tool maintains more than one view
			this.setTitle(
				parentTool.getText() + " View #" + this.getIndexNumber());
		}
	}
	/**
	 * This method initializes basicPanel
	 * 
	 * @return de.unidu.is.gui.BasicPanel
	 */
	private de.unidu.is.gui.BasicPanel getBasicPanel() {
		if(basicPanel == null) {
			basicPanel = new de.unidu.is.gui.BasicPanel();  // Generated
		}
		return basicPanel;
	}
	/**
	 * @param model
	 */
	public void setListModel(ListModel model) {
		getBasicPanel().setModel(model);	
	}
}
