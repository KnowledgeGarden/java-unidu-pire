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

 
// $Id: ListSets.java,v 1.5 2005/03/14 17:33:13 nottelma Exp $

/*
 * $Log: ListSets.java,v $
 * Revision 1.5  2005/03/14 17:33:13  nottelma
 * *** empty log message ***
 *
 * Revision 1.4  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.3  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: ListSets.java,v 1.5 2005/03/14 17:33:13 nottelma Exp $"
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.2  2004/07/07 12:50:40  fischer
 * kdm greeting
 *
 * Revision 1.1  2004/06/03 16:07:11  fischer
 * added ListSets, fromId, toId
 *
 */
package de.unidu.is.oai;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author fischer
 * @version $Revision: 1.5 $
 */
public class ListSets extends OAIListRequest {

	private static Category logger =
		Category.getInstance(ListSets.class.getName());
		
	private Map sets=new HashMap();
	
	/**
	 * @param url
	 * @param userAgent
	 * @param fromEmail
	 */
	public ListSets(
		String url,
		String userAgent,
		String fromEmail) throws OAIException {
		super(url, userAgent, fromEmail, "ListSets");
		doRequest();
		doListSets();
	}

	/**
	 * @param url
	 * @param userAgent
	 * @param fromEmail
	 * @param resumptionToken
	 */
	public ListSets(
		String url,
		String userAgent,
		String fromEmail,
		String resumptionToken) throws OAIException {
		super(url, userAgent, fromEmail, "ListSets", resumptionToken);
		doRequest();
		doListSets();
	}

	protected void doListSets() throws OAIException {
		Element root = getRelevantElement("ListSets");
		Collection s = extractSets(root);
		Iterator i = s.iterator();
		logger.info("Sets:");
		while (i.hasNext()) {
			Set set = (Set) i.next();
			logger.info(set.getId());
			this.addSet(set);
		}
	}
	
	protected void addSet(Set set) {
		if (!(sets.containsKey(set.getSetSpec()))) {
			sets.put(set.getSetSpec(),set);
		}
	}
	
	public Set getSet(String setSpec) {
		return (Set) sets.get(setSpec);
	}

	protected Collection extractSets(Element root) throws OAIException {
		List result = new LinkedList();
		NodeList setList = root.getElementsByTagName("set");
		if (setList.getLength() > 0) {
			for (int i = 0; i < setList.getLength(); i++) {
				if (setList.item(i) instanceof Element) {
					String setSpec = "";
					String setName = "";
					Element s = (Element) setList.item(i);

					NodeList specList =
						s.getElementsByTagName("setSpec");
					if ((specList.getLength() > 0)
						&& (specList.item(0) instanceof Element)) {
						Element p = (Element) specList.item(0);
						Node c = p.getFirstChild();
						if (c instanceof Text) {
							setSpec = ((Text) c).getNodeValue();
						} else {
							logger.warn(
								"Warning: "
									+ getUrl()
									+ " - setSpec element is not of type text");
						}
					} else {
						logger.warn(
							"Warning: "
								+ getUrl()
								+ " - setSpec element missing");
					}
					NodeList nameList = s.getElementsByTagName("setName");
					if ((nameList.getLength() > 0)
						&& (nameList.item(0) instanceof Element)) {
						Element p = (Element) nameList.item(0);
						Node c = p.getFirstChild();
						if (c instanceof Text) {
							setName = ((Text) c).getNodeValue();
						} else {
							logger.warn(
								"Warning: "
									+ getUrl()
									+ " - setName element is not of type text");
						}
					} else {
						logger.warn(
							"Warning: "
								+ getUrl()
								+ " - setName element missing");
					}
					if ((!(setSpec.equals(""))) && (!(setName.equals("")))) {
						Set newSet =
							new Set(setSpec,setName);
						result.add(newSet);
						//this.addset(prefix);
					}
				} else {
					logger.warn(
						"Warning: "
							+ getUrl()
							+ " - illegal set: "
							+ setList.item(i).toString());
				}
			}
		} else {
			throw new OAIException(getUrl() + ": no sets found");
		}
		return result;
	}

	public Collection getSets() {
		return sets.values();
	}

	public Iterator getSetsIterator() {
		return sets.values().iterator();
	}
	
	public Collection getSetSpecs() {
		return sets.keySet();
	}
	
	public Iterator getSetSpecsIterator() {
		return sets.keySet().iterator();
	}

}
