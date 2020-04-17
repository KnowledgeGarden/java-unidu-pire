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

 
//$Id: BasicTool.java,v 1.9 2005/03/04 11:26:09 nottelma Exp $

package de.unidu.is.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import de.izsoz.fiola.dispatcher.Dispatcher;
import de.izsoz.fiola.wob.desktop.InternalTool;
import de.izsoz.fiola.wob.desktop.InternalView;
import de.izsoz.fiola.wob.desktop.ToolView;
import de.izsoz.fiola.wob.desktop.WobEvent;

/**
 * A default tool that just shows a dummy icon (with OPEN/CLOSED states) and a dummy text.<br>
 * It opens an empty internal view.
 * @version $Revision: 1.9 $
 * @author André Schaefer
 * @stereotype implementationClass
 */
public class BasicTool extends InternalTool implements ActionListener, BasicPanelActionListener{

	private class PopupListener extends MouseAdapter {
		/**
		 * Call maybeShowPopup as a mousedown event may be a popup trigger.
		 * @see maybeShowPopup
		 */
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		/**
		 * Call maybeShowPopup as a 'mouse released' event may be a popup trigger.
		 * @see maybeShowPopup
		 */
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		/** If the event is a popup trigger, open the in Desktop.initialize() instantiated popup menu */
		public void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				getPopup().show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	private JPopupMenu ivjJPopupMenu1 = null;
	private javax.swing.JPopupMenu fieldPopup = new JPopupMenu();
	private JMenuItem ivjJMenuItem1 = null;
	private JMenuItem ivjJMenuItem2 = null;
	private JMenuItem ivjJMenuItem3 = null;
	private JSeparator ivjJSeparator1 = null;
	private ButtonGroup ivjGroup = null;

	private static java.util.ResourceBundle resdesktop =
		java.util.ResourceBundle.getBundle("desktop");

	public BasicTool() {
		super();
		initialize();
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == getJMenuItem1()) {
			ActionEvent arg1 = e;
			this.closeAllViewsPerformed(arg1);
		}
		if (e.getSource() == getJMenuItem2()) {
			ActionEvent arg1 = e;
			this.closeViewPerformed(arg1);
		}
		if (e.getSource() == getJMenuItem3()){
			this.showCurrentPerformed(e);
		}
	}

	protected void closeAllViewsPerformed(
		java.awt.event.ActionEvent actionEvent) {
		closeAllViews();
		return;
	}

	protected void closeViewPerformed(java.awt.event.ActionEvent actionEvent) {
		ToolView v = getCurrentView();
		closeView(v);
	}

	/** Creates a new view. initializes it with the tool as internal frame listener. */
	public ToolView createView() {
	BasicView aView = new BasicView(this);
		if (aView != null) {
			aView.addInternalFrameListener(this.getFrameListener());
		}
		return aView;
	}

	/** Comment */
	public void defaultToolPerformed(java.awt.event.ActionEvent actionEvent) {
		openView();
		return;
	}

	/**
	 * Return the Group property value.
	 * @return javax.swing.ButtonGroup
	 */
	private javax.swing.ButtonGroup getGroup() {
		if (ivjGroup == null) {
			try {
				ivjGroup = new javax.swing.ButtonGroup();
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjGroup;
	}

	/**
		 * Return the JMenuItem1 property value.
		 * @return javax.swing.JMenuItem
		 */
	private javax.swing.JMenuItem getJMenuItem1() {
		if (ivjJMenuItem1 == null) {
			try {
				ivjJMenuItem1 = new javax.swing.JMenuItem();
				ivjJMenuItem1.setName("JMenuItem1");
				ivjJMenuItem1.setMnemonic('c');
				ivjJMenuItem1.setText(resdesktop.getString("Close_all_Views"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJMenuItem1;
	}

	/**
	 * Return the JMenuItem2 property value.
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJMenuItem2() {
		if (ivjJMenuItem2 == null) {
			try {
				ivjJMenuItem2 = new javax.swing.JMenuItem();
				ivjJMenuItem2.setName("JMenuItem2");
				ivjJMenuItem2.setMnemonic('v');
				ivjJMenuItem2.setText("Close current View");
				ivjJMenuItem2.setActionCommand("Close View");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJMenuItem2;
	}

	/**
	 * Return the JMenuItem3 property value.
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJMenuItem3() {
		if (ivjJMenuItem3 == null) {
			try {
				ivjJMenuItem3 = new javax.swing.JMenuItem();
				ivjJMenuItem3.setName("JMenuItem3");
				ivjJMenuItem3.setMnemonic('s');
				ivjJMenuItem3.setText("Show current View");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJMenuItem3;
	}

	/**
	 * Return the JPopupMenu1 property value.
	 * @return javax.swing.JPopupMenu
	 */
	private javax.swing.JPopupMenu getJPopupMenu1() {
		if (ivjJPopupMenu1 == null) {
			try {
				ivjJPopupMenu1 = new javax.swing.JPopupMenu();
				ivjJPopupMenu1.setName("JPopupMenu1");
				ivjJPopupMenu1.add(getJMenuItem1());
				ivjJPopupMenu1.add(getJMenuItem2());
				ivjJPopupMenu1.add(getJMenuItem3());
				ivjJPopupMenu1.add(getJSeparator1());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPopupMenu1;
	}

	/**
	 * Return the JSeparator1 property value.
	 * @return javax.swing.JSeparator
	 */
	private javax.swing.JSeparator getJSeparator1() {
		if (ivjJSeparator1 == null) {
			try {
				ivjJSeparator1 = new javax.swing.JSeparator();
				ivjJSeparator1.setName("JSeparator1");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJSeparator1;
	}
	public javax.swing.JPopupMenu getPopup() {
		return getJPopupMenu1();
	}

	/** Button and context menu action handling */
	public void handleActionPerformed(java.awt.event.ActionEvent e) {
		System.out.println(e.getActionCommand());
		// examples!
		// close current (topmost) view
		if (e.getActionCommand().equals("Close View")) {
			System.out.println(resdesktop.getString("Closing_View_Message"));
			closeView(getCurrentView());
		} else // print number of topmost view
			if (e.getActionCommand().equals("Get Topmost View")) { //$NON-NLS-1$
				ToolView aView = (ToolView) getTopmostView();
			}
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {
		/* Uncomment the following lines to print uncaught exceptions to stdout */

		System.out.println("--------- UNCAUGHT EXCEPTION ---------"); //$NON-NLS-1$
		de.unidu.is.util.Log.error(exception);
	}

	/**
	 * @see de.izsoz.fiola.wob.desktop.InternalTool
	 */
	public boolean handleIZEvent(java.util.EventObject ev) {
		if (ev instanceof NewListModelEvent){
			openView();
			BasicView view = (BasicView) getCurrentView();
			view.setListModel(((NewListModelEvent)ev).getModel());
		}
		return false;
	}

	/**
	 * Initializes connections
	 * @exception java.lang.Exception The exception description.
	 */
	private void initConnections() throws java.lang.Exception {
		getJMenuItem1().addActionListener(this);
		getJMenuItem2().addActionListener(this);
		getJMenuItem3().addActionListener(this);
		Dispatcher.registerInterest(this,BasicPanelAction.class);
		Dispatcher.registerInterest(this,NewListModelEvent.class);
	}

	/** Initialize the class. Put Icon and Text preferences here. */
	private void initialize() {
		try {
			setName("Tool");
			setText(resdesktop.getString("DefaultTool"));
			setSize(100, 56);
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		Dispatcher.registerInterest(this, WobEvent.class);
		MouseListener popupListener = new PopupListener();
		getButton().addMouseListener(popupListener);
		// user code end
	}
	/**
	 * itemStateChanged method comment.
	 */
	public void itemStateChanged(java.awt.event.ItemEvent e) {
		JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getItem();
		String s =
			(e.getStateChange() == ItemEvent.DESELECTED
				? "Deselected"
				: "Selected");

		System.out.println(this.getText() + " " + item.getText() + ":" + s);

	}
	/**
	 * Sets the popup property (javax.swing.JPopupMenu) value.
	 * @param popup The new value for the property.
	 * @see #getPopup
	 */
	public void setPopup(javax.swing.JPopupMenu popup) {
		fieldPopup = popup;
	}
	/** Comment */
	protected void showCurrentPerformed(
		java.awt.event.ActionEvent actionEvent) {
		ToolView aView = getCurrentView();
		if (aView != null) {
			try {
				((InternalView) aView).setSelected(true);
			} catch (java.beans.PropertyVetoException ex) {
				System.out.println(resdesktop.getString("PrpptyVeto"));
			}
			System.out.println(
				resdesktop.getString("Topmost_View") + aView.getIndexNumber());
		}
		return;
	}

	/* (Kein Javadoc)
	 * @see de.unidu.is.gui.BasicPanelActionListener#handleBasicPanelAction(de.unidu.is.gui.BasicPanelAction)
	 */
	public boolean handleBasicPanelAction(BasicPanelAction action) {
		JOptionPane.showMessageDialog(this,"Received:"+action);
		return true;
	}
}
