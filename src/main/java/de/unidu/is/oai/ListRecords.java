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


// $Id: ListRecords.java,v 1.5 2005/03/14 17:33:13 nottelma Exp $

/*
 * $Log: ListRecords.java,v $
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
 * added "$Id: ListRecords.java,v 1.5 2005/03/14 17:33:13 nottelma Exp $"
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.4  2004/07/12 12:08:22  fischer
 * undid previous tries at quick parsing
 *
 * Revision 1.3  2004/07/07 12:50:40  fischer
 * kdm greeting
 *
 * Revision 1.2  2004/06/08 20:57:27  fischer
 * *** empty log message ***
 *
 * Revision 1.1  2004/06/08 14:07:01  fischer
 * added ListRecords
 *
 */
package de.unidu.is.oai;

import org.apache.log4j.Category;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author fischer
 * @version $Revision: 1.5 $
 */
public class ListRecords extends OAIListRequest {

    private static final Category logger =
            Category.getInstance(ListRecords.class.getName());

    private final Collection recordIds = new LinkedList();

    /**
     * @param url
     * @param userAgent
     * @param fromEmail
     * @param prefix
     */
    public ListRecords(
            String url,
            String userAgent,
            String fromEmail,
            String prefix)
            throws OAIException {
        super(url, userAgent, fromEmail, "ListRecords");
        setModifiers("&metadataPrefix=" + prefix);
        // TODO add Sets
        doRequest();
        // no further parsing
    }

    /**
     * @param url
     * @param userAgent
     * @param fromEmail
     * @param prefix
     * @param resumptionToken
     */
    public ListRecords(
            String url,
            String userAgent,
            String fromEmail,
            String prefix,
            String resumptionToken)
            throws OAIException {
        super(url, userAgent, fromEmail, "ListRecords", resumptionToken);
        setModifiers("&metadataPrefix=" + prefix);
        // TODO add Sets
        doRequest();
        // no further parsing
    }

    /*
	protected void doListRecords() throws OAIException {
		Element root = getRelevantElement("ListRecords");
		Collection s = extractRecordIds(root);
		Iterator i = s.iterator();
		logger.debug("Got Records:");
		while (i.hasNext()) {
			String recordId = (String) i.next();
			logger.debug(recordId);
			this.addRecordId(recordId);
		}
	}

	protected void addRecordId(String recordId) {
		if (!(recordIds.contains(recordId))) {
			recordIds.add(recordId);
		}
	}

	protected Collection extractRecordIds(Element root) {
		List result = new LinkedList();
		NodeList recordNL = root.getElementsByTagName("record");
		if ((recordNL != null) && (recordNL.getLength() > 0)) {
			for (int j = 0; j < recordNL.getLength(); j++) {
				if (recordNL.item(j) instanceof Element) {
					Element recordElement = (Element) recordNL.item(j);
					// get the identifier
					NodeList idNL =
						recordElement.getElementsByTagName("identifier");
					if ((idNL != null) && (idNL.getLength() > 0)) {
						if (idNL.item(0) instanceof Element) {
							Element idElement = (Element) idNL.item(0);
							Node idNode = idElement.getFirstChild();
							if (idNode instanceof Text) {
								String identifier =
									((Text) idNode).getNodeValue();
								identifier =
									(identifier != null)
										? identifier.trim()
										: null;
								if ((identifier != null)
									&& (!(identifier.equals("")))) {
									result.add(identifier);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	public Collection getRecordIds() {
		return recordIds;
	}

	*/

}
