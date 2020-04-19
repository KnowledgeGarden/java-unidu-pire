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

 
//$Id: Desktop.java,v 1.9 2005/03/14 17:33:13 nottelma Exp $

package de.unidu.is.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.log4j.Category;

import de.izsoz.fiola.dispatcher.Dispatcher;
import de.izsoz.fiola.wob.desktop.StatusEvent;
import de.izsoz.fiola.wob.desktop.ToolBar;
import de.izsoz.fiola.wob.desktop.ToolController;
import de.izsoz.fiola.wob.desktop.UserInfo;
import de.izsoz.fiola.wob.desktop.WobEvent;

/**
 * <p>The desktop containing the  tool bar and the  JDesktopPane for
 * displaying tools and their views. </p> Creation date: (23.08.2000 09:09:34)
 * @author André Schaefer
 * @see ToolBar
 * @see JDesktopPane
 * @version $Revision: 1.9 $
 */
public class Desktop
	extends de.izsoz.fiola.wob.desktop.Desktop
	implements ActionListener {

	/**
	 * This inner class shall listen for popup triggers on the desktop
	 * pane and open a popup menu accordingly.
	 * @author André Schaefer
	 */
	public class PopupListener extends MouseAdapter {
		/**
		 * Call maybeShowPopup as a mousedown event may be a
		 * popup trigger.
		 */
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		/**
		 * Call maybeShowPopup as a 'mouse released' event may be a
		 * popup trigger.
		 */
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		/**
		 * If the event is a popup trigger, open the in
		 * Desktop.initialize() instantiated popup menu
		 */
		public void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	private JComponent desktopContents = null;
	private JDesktopPane ivjJDesktopPane1 = null;
	private ToolBar ivjToolBar1 = null;
	protected JPopupMenu popup;
	public final static int HORIZONTAL = 0;
	public final static int VERTICAL = 1;
	private JPanel ivjStatusBarPane = null;
	private JButton ivjStatusMsg1 = null;
	private JLabel ivjStatusMsg2 = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements java.awt.event.ComponentListener {
		public void componentHidden(java.awt.event.ComponentEvent e) {
		};
		public void componentMoved(java.awt.event.ComponentEvent e) {
		};
		public void componentResized(java.awt.event.ComponentEvent e) {
			connEtoC1(e);
		};
		public void componentShown(java.awt.event.ComponentEvent e) {
			connEtoC1(e);
		};
	};
	/**
	 * ctor.
	 * @number txmiid26
	 */
	public Desktop() {
		super();
		initialize();
	}
	public Desktop(LayoutManager par1) {
		super(par1);
	}
	public Desktop(LayoutManager par1, boolean par2) {
		super(par1, par2);
	}
	public Desktop(boolean par1) {
		super(par1);
	}
	public void actionPerformed(ActionEvent e) {
		Dispatcher.postEvent(new WobEvent(this));
	}
	/**
	 * connEtoC1:  (Desktop.component.componentShown(java.awt.event.ComponentEvent) --> Desktop.desktop_ComponentShown(Ljava.awt.event.ComponentEvent;)V)
	 * @param arg1 java.awt.event.ComponentEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.ComponentEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.desktop_ComponentShown(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}
	/**
	 * Comment
	 */
	public void desktop_ComponentResized(ComponentEvent componentEvent) {

		return;
	}

	/**
	 * Comment
	 */
	public void desktop_ComponentShown(
		java.awt.event.ComponentEvent componentEvent) {
		return;
	}
	/**
	 * Insert the method's description here. Creation date:
	 * (31.08.2000 10:20:02)
	 * @return javax.swing.JDesktopPane
	 */
	public javax.swing.JDesktopPane getJDesktopPane1() {
		if (ivjJDesktopPane1 == null) {
			try {
				ivjJDesktopPane1 = new javax.swing.JDesktopPane();
				ivjJDesktopPane1.setName("JDesktopPane1");
				// user code begin {1}
				ivjJDesktopPane1.setSize(790, 490);
				// user code end
				ivjJDesktopPane1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black,1));  // Generated
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDesktopPane1;
	}
	/**
	 * Return the JSplitPane1 property value.
	 * @return javax.swing.JSplitPane
	 * @number txmiid2a
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	public javax.swing.JComponent getDesktopContents() {
		if (desktopContents == null) {
			try {
				desktopContents =
					new JPanel(new BorderLayout(),true);
				desktopContents.setName("DesktopContents");
				getDesktopContents().add(getJDesktopPane1(), BorderLayout.CENTER);
				getDesktopContents().add(getToolBar1(), BorderLayout.SOUTH);
				// user code begin {1}
				desktopContents.setMinimumSize(new Dimension(0, 50));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return desktopContents;
	}
	/**
	 * Method generated to support the promotion of the
	 * SplitPaneOrientation attribute.
	 * @return int
	 * @deprecated No split pane any more
	 */
	public int getSplitPaneOrientation() {
		//            return getDesktopContents().getOrientation();
		return 0;
	}
	/**
	 * Return the StatusBarPane property value.
	 * @return javax.swing.JPanel
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	public javax.swing.JPanel getStatusBarPane() {
		if (ivjStatusBarPane == null) {
			try {
				ivjStatusBarPane = new javax.swing.JPanel();
				ivjStatusBarPane.setName("StatusBarPane");
				ivjStatusBarPane.setLayout(new java.awt.BorderLayout());
				getStatusBarPane().add(getStatusMsg1(), "West");
				ivjStatusBarPane.add(getStatusMsg2(), java.awt.BorderLayout.CENTER);  // Generated
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjStatusBarPane;
	}
	/**
	 * Method generated to support the promotion of the
	 * statusField1 attribute.
	 * @return javax.swing.JButton
	 */
	public JButton getStatusField1() {
		return getStatusMsg1();
	}
	/**
	 * Return the StatusMsg1 property value.
	 * @return javax.swing.JButton
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JButton getStatusMsg1() {
		if (ivjStatusMsg1 == null) {
			try {
				ivjStatusMsg1 = new javax.swing.JButton();
				ivjStatusMsg1.setName("StatusMsg1");
				ivjStatusMsg1.setBorder(new javax.swing.border.EtchedBorder());
				ivjStatusMsg1.setText("StatusMsg1    ");
				// user code begin {1}
				ivjStatusMsg1.setBorder(BorderFactory.createEtchedBorder());
				ivjStatusMsg1.setText("Connecting...");
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjStatusMsg1;
	}
	/**
	 * Return the StatusMsg2 property value.
	 * @return javax.swing.JLabel
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	public javax.swing.JLabel getStatusMsg2() {
		if (ivjStatusMsg2 == null) {
			try {
				ivjStatusMsg2 = new javax.swing.JLabel();
				ivjStatusMsg2.setName("StatusMsg2");
				ivjStatusMsg2.setBorder(new javax.swing.border.EtchedBorder());
				ivjStatusMsg2.setText("StatusMsg2");
				// user code begin {1}

				ivjStatusMsg2.setBorder(
					BorderFactory.createLoweredBevelBorder());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjStatusMsg2;
	}
	/**
	 * Return the ToolBar property value.
	 * @return de.izsoz.fiola.wob.desktop.ToolBar
	 * @number txmiid2c
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	protected ToolBar getToolBar1() {
		if (ivjToolBar1 == null) {
			try {
				ivjToolBar1 = new de.izsoz.fiola.wob.desktop.ToolBar();
				ivjToolBar1.setName("ToolBar1");
				// user code begin {1}
				ivjToolBar1.setBackground(
					javax.swing.UIManager.getColor("Desktop.background"));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjToolBar1;
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 * @number txmiid2d
	 */
	private void handleException(Throwable exception) {
		/* Uncomment the following lines to print uncaught
		exceptions to stdout */

		LOG.error("Exception cought:", exception);
	}
	/**
	 * Handle the Events for which we have registered interest.
	 */
	public boolean handleIZEvent(EventObject ev) {

		if (ev instanceof StatusEvent) {
			StatusEvent stEv = (StatusEvent) ev;
			this.getStatusMsg2().setText(
				stEv.getSource() + ": " + stEv.getText());
		}
		return true;
	}
	/**
	 * Initializes connections
	 * @exception java.lang.Exception The exception description.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		this.addComponentListener(ivjEventHandler);
	}
	/**
	 * Initialize the class.
	 * @number txmiid2f
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private void initialize() {
		try {
			ToolController.getInstance().setDesktop(this);
			setName("Desktop");
			setLayout(new java.awt.BorderLayout());
			setSize(798, 598);
			add(getDesktopContents(), "Center");
			add(getStatusBarPane(), "South");
			this.setPreferredSize(new java.awt.Dimension(1024,768));  // Generated
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	/**
	 * @see java.beans.PropertyChangeListener
	 */
	public void propertyChange(PropertyChangeEvent evt) {
	}
	/**
	 * Insert the method's description here. Creation date:
	 * (31.08.2000 10:20:02)
	 * @param newJDesktopPane1 javax.swing.JDesktopPane
	 */
	public void setJDesktopPane1(JDesktopPane newJDesktopPane1) {
		ivjJDesktopPane1 = newJDesktopPane1;
	}
	/**
	 * Method generated to support the promotion of the
	 * SplitPaneOrientation attribute.
	 * @param arg1 int
	 * @deprecated No Split Pane any more...
	 */
	public void setSplitPaneOrientation(int arg1) {
		//            getDesktopContents().setOrientation(arg1);
	}
	public void viewStatusBar() {
		/* Hide or show the statusbar */

		getStatusBarPane().setVisible(!(getStatusBarPane().isVisible()));
	}
	/**
	 * @return
	 */
	public  UserInfo getUserInfo() {
		// TODO Automatisch erstellter Methoden-Stub
		return fieldUserInfo;
	}
	protected UserInfo fieldUserInfo;
	protected static Category LOG = Category.getInstance(Desktop.class.getName());
	/**
		 * Set th einfo about the currently logged in user. Creation date: (18.04.2001 10:11:34)
		 * @param newUserInfo de.izsoz.daffodil.UserInfo
		 */
	public void setUserInfo(UserInfo newUserInfo) {
		fieldUserInfo = newUserInfo;
		LOG.debug("Desktop: Got UserInfo for - " + newUserInfo);
	}
	/* (Kein Javadoc)
	 * @see de.izsoz.fiola.wob.desktop.Desktop#initTools()
	 */
	protected void initTools() {
		// TODO use jfcUnit to separate this to Unit test
		ToolController.getInstance().addTool(new BasicTool());
		Dispatcher.threadEvent(new StatusEvent(this, "Search Tool done."));
		super.initTools();
	}

}
