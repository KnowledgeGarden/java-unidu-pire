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

 
// $Id: Set.java,v 1.4 2005/02/21 17:29:21 huesselbeck Exp $

/*
 * $Log: Set.java,v $
 * Revision 1.4  2005/02/21 17:29:21  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.3  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: Set.java,v 1.4 2005/02/21 17:29:21 huesselbeck Exp $"
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.1  2004/06/03 16:07:11  fischer
 * added ListSets, fromId, toId
 *
 */
package de.unidu.is.oai;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author fischer
 * @version $Revision: 1.4 $
 */
public class Set {
	
	private String setSpec=null;
	private String setName=null;
	private Set parent=null;
	private Collection children=new LinkedList();

	/**
	 * 
	 */
	public Set(String setSpec,String setName) {
		setSetSpec(setSpec);
		setSetName(setName);
	}
	/**
	 * @param setName
	 */
	protected void setSetName(String setName) {
		this.setName=setName;
	}
	
	/**
	 * @param setSpec
	 */
	protected void setSetSpec(String setSpec) {
		this.setSpec=setSpec;
	}
	
	public void setParent(Set parent) {
		this.parent=parent;
	}
	
	public void addChild(Set child) {
		if (!(children.contains(child))) {
			children.add(child);
		}
	}
	
	public String getId() {
		return getSetSpec();
	}
	
	public String getSetSpec() {
		return setSpec;
	}
	
	public String getSetName() {
		return setName;
	}
	
	public Set getParent() {
		return parent;
	}
	
	public String getParentId() {
		int i=setSpec.indexOf(":");
		if (i<1) {
			return null;
		} else {
			return setSpec.substring(i);
		}
	}
		
	public boolean equals(Object o) {
		if (o instanceof Set) {
			return ((Set) o).getSetSpec().equals(getSetSpec());
		} else {
			return false;
		}
	}

}
