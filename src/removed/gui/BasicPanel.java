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

 
//$Id: BasicPanel.java,v 1.6 2005/02/21 17:29:19 huesselbeck Exp $

package de.unidu.is.gui;

import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.ListModel;

import de.izsoz.fiola.dispatcher.Dispatcher;
import de.izsoz.fiola.dispatcher.EventReciever;

/**
 * @author schaefer
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:19 $
 */
public class BasicPanel extends JPanel implements EventReciever{
	private javax.swing.JButton jButton= null;

	private javax.swing.JLabel jLabel= null;

	private javax.swing.JList jList= null;

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton() {
		if (jButton == null) {
			jButton= new javax.swing.JButton(); // Generated
			jButton.setText("A Button"); // Generated
			jButton.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					// Notify tool that the Button was pressed
						Dispatcher.sendEvent(new BasicPanelAction(this));
				} 
			
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel= new javax.swing.JLabel(); // Generated
			jLabel.setText("Some Text"); // Generated
		}
		return jLabel;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getJList() {
		if (jList == null) {
			jList= new javax.swing.JList(); // Generated
			jList.addPropertyChangeListener(new java.beans.PropertyChangeListener() { 
				public void propertyChange(java.beans.PropertyChangeEvent e) { 
					if ((e.getPropertyName().equals("model"))) { 
						System.out.println("propertyChange(model)"); // TODO Auto-generated stub "model" 
					} 
				}
			});
			
		}
		return jList;
	}


	/* (Kein Javadoc)
	 * @see de.izsoz.fiola.dispatcher.EventReciever#handleIZEvent(java.util.EventObject)
	 */
	public boolean handleIZEvent(EventObject arg0) {
		// TODO Automatisch erstellter Methoden-Stub
		return false;
	}
	/**
	 * This is the default constructor
	 */
	public BasicPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new java.awt.BorderLayout());  // Generated
		this.add(getJList(), java.awt.BorderLayout.CENTER);  // Generated
		this.add(getJLabel(), java.awt.BorderLayout.NORTH);  // Generated
		this.add(getJButton(), java.awt.BorderLayout.SOUTH);  // Generated
		this.setSize(300, 200);
	}

	/**
	 * @param model
	 */
	public void setModel(ListModel model) {
		getJList().setModel(model);			
	}

}
