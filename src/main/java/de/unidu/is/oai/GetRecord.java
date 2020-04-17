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

 
// $Id: GetRecord.java,v 1.5 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: GetRecord.java,v $
 * Revision 1.5  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.4  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.3  2005/02/21 15:10:38  huesselbeck
 * removes status line(s)
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: GetRecord.java,v 1.5 2005/02/21 17:29:20 huesselbeck Exp $"
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
 * Revision 1.1  2004/06/02 16:03:31  fischer
 * a quick shot at OAI harvesting
 *
 */
package de.unidu.is.oai;

import org.apache.log4j.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author fischer
 * @version $Revision: 1.5 $
 *
 */
public class GetRecord extends OAIRequest {

	private String status;
	private String schema;
	private String prefix = null;
	private String identifier = null;

	private static Category logger =
		Category.getInstance(GetRecord.class.getName());

	/**
	 * @param url
	 * @param userAgent
	 * @param fromEmail
	 */
	public GetRecord(
		String url,
		String userAgent,
		String fromEmail,
		String identifier,
		String prefix) throws OAIException {
		super(url, userAgent, fromEmail,"GetRecord");
		setId(identifier);
		setPrefix(prefix);
		setModifiers("&identifier="+ identifier+"&metadataPrefix=" + prefix);
		doRequest();
		doGetRecord();
	}

	/**
	 * @param prefix
	 */
	protected void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	protected void doGetRecord() throws OAIException {
		Element root = getRelevantElement("GetRecord");
		extractId(root);
		extractSchema(root);
		// extractResponseDate(root);
		// extractDatestamp();
		extractStatus(root);
	}

	/**
	 * @param root
	 */
	private void extractStatus(Element root) {
		/** extract the datestamp from the header
		 */
        String result=null;
		NodeList headers = root.getElementsByTagName("header");
		if ((headers.getLength()>0) && (headers.item(0) instanceof Element)) {
			Element h=(Element) headers.item(0);
			result=h.getAttribute("status");

			if ((result==null) || (result.trim().equals(""))) {
				// just to be on the safe side, look also for a status Element
				// (if status is still unknown)
				NodeList statusNodes = h.getElementsByTagName("status");
				if ((statusNodes.getLength()>0) && (statusNodes.item(0) instanceof Element)) {
					Element e=(Element) statusNodes.item(0);
					Node c=e.getFirstChild();
					if (c instanceof Text) {
						result=((Text) c).getNodeValue();
						logger.warn("Record "+getId()+" from "+getUrl()+" does not conform to OAIPMH 2.0, "+
									"status is an element of header instead of an attribute");
					}
				}
                
			}
            
		} else {
			throw new IllegalArgumentException
				("Invalid OAI record, no header found");
		}
        
		if (result!=null) {
			result.trim();
			result.toLowerCase();
			// logger.info("id='"+this.getRecordId()+"', status="+status);
		}
		setStatus(result);
	}

	/**
	 * @param status
	 */
	protected void setStatus(String status) {
		this.status=status;
	}

	/**
	 * @param root
	 */
	private void extractSchema(Element root) {
		String result = null;
		// OAI 2.0: request tag, before GetRecord
		NodeList urls = root.getElementsByTagName("request");
		if (urls.getLength() > 0) {
			// first Node should be the request Element, and that has an attribute metadataPrefix...
			Element h = (Element) urls.item(0);
			result = h.getAttribute("metadataPrefix");
		} else {
			// OAI 1.x: requestURL, inside GetRecord
			urls = root.getElementsByTagName("requestURL");
			if ((urls.getLength() > 0) && (urls.item(0) instanceof Element)) {
				Element e = (Element) urls.item(0);
				Node c = e.getFirstChild();
				if (c instanceof Text) {
					result = ((Text) c).getNodeValue();
					result.trim();
					int i = result.indexOf("?");
					if (i >= 0) {
						result = result.substring(i);
						i = result.indexOf("metadataPrefix=");
						if (i >= 0) {
							result = result.substring(i+"metadataPrefix=".length());
							i = result.indexOf(";");
							if (i >= 0) {
								result = result.substring(0, i);
							}
						}
					}
				}
			}
		}
		if (result!=null) {
			result=result.trim();
		}
		if ((result!=null) && (!(result.equals(prefix)))) {
			logger.warn("Record "+getId()+" from "+getUrl()+": requested prefix was "+prefix+", but got schema "+result);
		}
	}

	/**
	 * @return
	 */
	public String getId() {
		return identifier;
	}

	/**
	 * @param root
	 */
	private void extractId(Element root) throws OAIException {
		String id = null;
		/** extract the record identifier from the header
		 */
		NodeList headers = root.getElementsByTagName("header");
		if ((headers.getLength() > 0)
			&& (headers.item(0) instanceof Element)) {
			Element h = (Element) headers.item(0);
			NodeList ids = h.getElementsByTagName("identifier");
			if ((ids.getLength() > 0) && (ids.item(0) instanceof Element)) {
				Element e = (Element) ids.item(0);
				Node c = e.getFirstChild();
				if (c instanceof Text) {
					id = ((Text) c).getNodeValue();
					if (id != null) {
						id = id.trim();
					}
				} else {
					throw new OAIException("Invalid OAI record, identifier is not of type Text");
				}
			} else {
				throw new OAIException("Invalid OAI record, no identifier in the header");
			}
		} else {
			throw new OAIException("Invalid OAI record, no header found");
		}
		if ((id!=null) && (!(id.equals(getId())))) {
			logger.warn("Record "+getId()+" from "+getUrl()+": requested identifier was "+getId()+", but got ID "+id);
		}
	}

	/**
	 * @param id
	 */
	protected void setId(String id) {
		this.identifier=id;
	}
	
	public boolean isDeleted() {
		return ((status!=null) && (status.equals("deleted")));
	}

}
