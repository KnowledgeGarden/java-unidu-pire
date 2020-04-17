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

 
// $Id: ListIdentifiers.java,v 1.4 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: ListIdentifiers.java,v $
 * Revision 1.4  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.3  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: ListIdentifiers.java,v 1.4 2005/02/21 17:29:20 huesselbeck Exp $"
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.3  2004/07/07 12:50:40  fischer
 * kdm greeting
 *
 * Revision 1.2  2004/06/03 16:07:11  fischer
 * added ListSets, fromId, toId
 *
 * Revision 1.1  2004/06/02 16:03:31  fischer
 * a quick shot at OAI harvesting
 *
 */
package de.unidu.is.oai;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author fischer
 * @version $Revision: 1.4 $
 *
 */
// TODO: add sets
public class ListIdentifiers extends OAIListRequest {

	private static Category logger =
		Category.getInstance(ListIdentifiers.class.getName());

	private Collection ids = new LinkedList();

	public ListIdentifiers(
		String url,
		String userAgent,
		String fromEmail,
		String prefix)
		throws OAIException {
		super(url, userAgent, fromEmail, "ListIdentifiers");
		setModifiers("&metadataPrefix=" + prefix);
		doRequest();
		doListIdentifiers();
	}

	public ListIdentifiers(
		String url,
		String userAgent,
		String fromEmail,
		String prefix,
		String resumptionToken)
		throws OAIException {
		super(url, userAgent, fromEmail, "ListIdentifiers", resumptionToken);
		setModifiers("&metadataPrefix=" + prefix);
		doRequest();
		doListIdentifiers();
	}

	protected void doListIdentifiers() throws OAIException {
		Element root = getRelevantElement("ListIdentifiers");
		Collection s = extractIdentifiers(root);
		Iterator i = s.iterator();
		logger.debug("Got IDs:");
		while (i.hasNext()) {
			String identifier = (String) i.next();
			logger.debug(identifier);
			this.addIdentifier(identifier);
		}
	}

	/**
	 * @param identifier
	 */
	protected void addIdentifier(String identifier) {
		if (!(ids.contains(identifier))) {
			ids.add(identifier);
		}
	}

	protected Collection extractIdentifiers(Element root) throws OAIException {
		List result = new LinkedList();
		// get all identifiers
		NodeList idNL = root.getElementsByTagName("identifier");
		if ((idNL != null) && (idNL.getLength() > 0)) {
			for (int i = 0; i < idNL.getLength(); i++) {
				if (idNL.item(i) instanceof Element) {
					Element idElement = (Element) idNL.item(i);
					Node idNode = idElement.getFirstChild();
					if (idNode instanceof Text) {
						String identifier = ((Text) idNode).getNodeValue();
						identifier =
							(identifier != null) ? identifier.trim() : null;
						if ((identifier != null)
							&& (!(identifier.equals("")))) {
							result.add(identifier);
						}
					}
				}
			}
		}
		return result;
	}

	public Collection getIdentifiers() {
		return ids;
	}

	/**
	 * @return
	 */
	public Iterator getIdentifiersIterator() {
		return ids.iterator();
	}

}
