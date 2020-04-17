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

 
//$Id: BasicPanelAction.java,v 1.6 2005/02/21 17:29:19 huesselbeck Exp $

package de.unidu.is.gui;

import java.awt.event.ActionListener;

import de.izsoz.fiola.dispatcher.EventReciever;
import de.izsoz.fiola.dispatcher.SelfDispatchable;
import de.izsoz.fiola.wob.desktop.WobEvent;

/**
 * @author schaefer
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:19 $
 */
public class BasicPanelAction extends WobEvent implements SelfDispatchable {
	/**
	 * @param eventSource
	 */
	public BasicPanelAction(Object eventSource) {
		super(eventSource);
		// TODO Automatisch erstellter Konstruktoren-Stub
	}

	/* (Kein Javadoc)
	 * @see de.izsoz.fiola.dispatcher.SelfDispatchable#dispatchSelf(de.izsoz.fiola.dispatcher.EventReciever)
	 */
	public boolean dispatchSelf(EventReciever receiver) {
		if (receiver instanceof ActionListener) {
				return ((BasicPanelActionListener)receiver).handleBasicPanelAction(this) ;
			} else {
				return ((EventReciever)receiver).handleIZEvent(this) ;
			}
	}
}
