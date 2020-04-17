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

 
// $Id: ListMetadataFormats.java,v 1.4 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: ListMetadataFormats.java,v $
 * Revision 1.4  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.3  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: ListMetadataFormats.java,v 1.4 2005/02/21 17:29:20 huesselbeck Exp $"
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
 * @version $Revision: 1.4 $
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListMetadataFormats extends OAIListRequest {

	private static Category logger =
		Category.getInstance(ListMetadataFormats.class.getName());

	private Map mdFormats = new HashMap();

	/**
	 * @param url
	 * @param userAgent
	 * @param fromEmail
	 */
	public ListMetadataFormats(String url, String userAgent, String fromEmail)
		throws OAIException {
		super(url, userAgent, fromEmail,"ListMetadataFormats");
		doRequest();
		doListMetadataFormats();
	}

	public ListMetadataFormats(
		String url,
		String userAgent,
		String fromEmail,
		String resumptionToken)
		throws OAIException {
		super(url, userAgent, fromEmail, "ListMetadataFormats", resumptionToken);
		doRequest();
		doListMetadataFormats();
	}

	public void listMetadataFormats() throws OAIException {
	}

	protected void doListMetadataFormats() throws OAIException {
		Element root = getRelevantElement("ListMetadataFormats");
		Collection s = extractSchemas(root);
		Iterator i = s.iterator();
		logger.info("Schemas:");
		while (i.hasNext()) {
			Schema schema = (Schema) i.next();
			logger.info(schema.getPrefix());
			this.addSchema(schema);
		}
	}

	/**
	 * @param schema
	 */
	protected void addSchema(Schema schema) {
		if (!(mdFormats.containsKey(schema.getPrefix()))) {
			mdFormats.put(schema.getPrefix(),schema);
		}
	}

	protected Collection extractSchemas(Element root) throws OAIException {
		List result = new LinkedList();
		NodeList schemaList = root.getElementsByTagName("metadataFormat");
		if (schemaList.getLength() > 0) {
			for (int i = 0; i < schemaList.getLength(); i++) {
				if (schemaList.item(i) instanceof Element) {
					String prefix = "";
					String schemaUrl = "";
					String namespace = "";
					Element s = (Element) schemaList.item(i);

					NodeList prefixList =
						s.getElementsByTagName("metadataPrefix");
					if ((prefixList.getLength() > 0)
						&& (prefixList.item(0) instanceof Element)) {
						Element p = (Element) prefixList.item(0);
						Node c = p.getFirstChild();
						if (c instanceof Text) {
							prefix = ((Text) c).getNodeValue();
						} else {
							logger.warn(
								"Warning: "
									+ getUrl()
									+ " - metadataPrefix element is not of type text");
						}
					} else {
						logger.warn(
							"Warning: "
								+ getUrl()
								+ " - metadataPrefix element missing");
					}
					NodeList urlList = s.getElementsByTagName("schema");
					if ((urlList.getLength() > 0)
						&& (urlList.item(0) instanceof Element)) {
						Element p = (Element) urlList.item(0);
						Node c = p.getFirstChild();
						if (c instanceof Text) {
							schemaUrl = ((Text) c).getNodeValue();
						} else {
							logger.warn(
								"Warning: "
									+ getUrl()
									+ " - schema element is not of type text");
						}
					} else {
						logger.warn(
							"Warning: "
								+ getUrl()
								+ " - schema element missing");
					}
					NodeList nsList =
						s.getElementsByTagName("metadataNamespace");
					if ((nsList.getLength() > 0)
						&& (nsList.item(0) instanceof Element)) {
						Element p = (Element) nsList.item(0);
						Node c = p.getFirstChild();
						if (c instanceof Text) {
							namespace = ((Text) c).getNodeValue();
						} else {
							logger.warn(
								"Warning: "
									+ getUrl()
									+ " - namespace element is not of type text");
						}
					}
					if ((!(prefix.equals(""))) && (!(schemaUrl.equals("")))) {
						Schema newSchema =
							new Schema(prefix, schemaUrl, namespace);
						result.add(newSchema);
						//this.addSchema(prefix);
					}
				} else {
					logger.warn(
						"Warning: "
							+ getUrl()
							+ " - illegal schema: "
							+ schemaList.item(i).toString());
				}
			}
		} else {
			throw new OAIException(getUrl() + ": no schemas found");
		}
		return result;
	}

	public Collection getMetadataFormats() {
		return mdFormats.values();
	}

	public Iterator getMetadataFormatsIterator() {
		return mdFormats.values().iterator();
	}
	
	public Collection getMetadataPrefixes() {
		return mdFormats.keySet();
	}

	public Iterator getMetadataPrefixesIterator() {
		return mdFormats.keySet().iterator();
	}

}
