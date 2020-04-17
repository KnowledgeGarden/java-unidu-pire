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

 
// $Id: Identify.java,v 1.4 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: Identify.java,v $
 * Revision 1.4  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.3  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: Identify.java,v 1.4 2005/02/21 17:29:20 huesselbeck Exp $"
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
 * Revision 1.2  2004/06/08 20:57:27  fischer
 * *** empty log message ***
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
 * @version $Revision: 1.4 $
 * 
 * This class encapsulates the Identify request
 */
public class Identify extends OAIRequest {

	private static Category logger =
		Category.getInstance(Identify.class.getName());

	private String id = null;
	private String description = null;
	private String adminEmail = null;
	private String repositoryName = null;
		
	/**
	 * @param url
	 * @param userAgent
	 * @param fromEmail
	 */
	public Identify(String url, String userAgent, String fromEmail) throws OAIException {
		super(url,userAgent,fromEmail,"Identify");
		doRequest();
		doIdentify();
	}

	protected void doIdentify() throws OAIException {
		Element root = getRelevantElement("Identify");
		extractBaseUrl(root);
		extractRepositoryName(root);
		extractAdminEmail(root);
		extractId(root);
		extractDescription(root);

		logger.info("url=" + getUrl());
		logger.info("repositoryName=" + this.repositoryName);
		logger.info("adminEmail=" + this.adminEmail);
		logger.info("id=" + this.getId());
		logger.info("description=" + this.description+"\n");
	}

	/**
	 * @return repository id
	 */
	public String getId() {
		return id;
	}

	protected String extractBaseUrl(Element root) throws OAIException {
		String baseUrl = "";
		NodeList baseUrlList = root.getElementsByTagName("baseURL");
		if ((baseUrlList.getLength() > 0)
			&& (baseUrlList.item(0) instanceof Element)) {
			Element b = (Element) baseUrlList.item(0);
			Node c = b.getFirstChild();
			if (c instanceof Text) {
				baseUrl = ((Text) c).getNodeValue();
				baseUrl.trim();
				if ((getUrl() != null)
					&& (!(getUrl().equals("")))
					&& (!(baseUrl.equalsIgnoreCase(getUrl())))) {
					logger.warn(
						"Warning: identify for "
							+ getUrl()
							+ " returns url="
							+ baseUrl);
				} else {
					setUrl(baseUrl);
				}
			} else {
				throw new OAIException(
					getUrl() + ": baseURL element is not " + "of type text");
			}
		} else {
			throw new OAIException(getUrl() + ": no baseURL element");
		}
		return baseUrl;
	}

	protected String extractRepositoryName(Element root) throws OAIException {
		String result = "";
		NodeList repositoryNameList =
			root.getElementsByTagName("repositoryName");
		if ((repositoryNameList.getLength() > 0)
			&& (repositoryNameList.item(0) instanceof Element)) {
			Element b = (Element) repositoryNameList.item(0);
			Node c = b.getFirstChild();
			if (c instanceof Text) {
				result = ((Text) c).getNodeValue();
				this.setRepositoryName(result);
			} else {
				throw new OAIException(
					getUrl()
						+ ": repositoryName element is not "
						+ "of type text");
			}
		}
		return result;
	}

	/**
	 * @param repositoryName
	 */
	protected void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	protected String extractAdminEmail(Element root) throws OAIException {
		String result = "";
		NodeList adminEmailList = root.getElementsByTagName("adminEmail");
		if ((adminEmailList.getLength() > 0)
			&& (adminEmailList.item(0) instanceof Element)) {
			Element b = (Element) adminEmailList.item(0);
			Node c = b.getFirstChild();
			if (c instanceof Text) {
				result = ((Text) c).getNodeValue();
				this.setAdminEmail(result);
			} else {
				throw new OAIException(
					getUrl() + ": adminEmail element is not " + "of type text");
			}
		}
		return result;
	}

	/**
	 * @param adminEmail
	 */
	protected void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	protected String extractId(Element root) throws OAIException {
		String oaiId = null;
		NodeList oaiIdList = root.getElementsByTagName("oai-identifier");
		if ((oaiIdList.getLength() > 0)
			&& (oaiIdList.item(0) instanceof Element)) {
			Element b = (Element) oaiIdList.item(0);
			NodeList idList = b.getElementsByTagName("repositoryIdentifier");
			if ((idList.getLength() > 0)
				&& (idList.item(0) instanceof Element)) {
				Element i = (Element) idList.item(0);
				Node c = i.getFirstChild();
				if (c instanceof Text) {
					oaiId = ((Text) c).getNodeValue();
					if (oaiId.equals("")) {
						throw new OAIException(
							getUrl() + ": empty repositoryIdentifier");
					} else {
						this.id = oaiId;
					}
				} else {
					throw new OAIException(
						getUrl()
							+ ": repositoryIdentifier element is not "
							+ "of type text");
				}
			} else {
				logger.warn(
					getUrl() + ": no repositoryIdentifier element");
			}

		} else {
			logger.warn(getUrl() + ": no oai-identifier element");
		}
		return oaiId;
	}

	protected String extractDescription(Element root) throws OAIException {
		String result = null;
		NodeList eprintsList = root.getElementsByTagName("eprints");
		if ((eprintsList.getLength() > 0)
			&& (eprintsList.item(0) instanceof Element)) {
			Element b = (Element) eprintsList.item(0);
			NodeList contentList = b.getElementsByTagName("content");
			if ((contentList.getLength() > 0)
				&& (contentList.item(0) instanceof Element)) {
				Element content = (Element) contentList.item(0);
				NodeList textList = content.getElementsByTagName("text");
				if ((textList.getLength() > 0)
					&& (textList.item(0) instanceof Element)) {
					Element text = (Element) textList.item(0);
					Node c = text.getFirstChild();
					if (c instanceof Text) {
						result = ((Text) c).getNodeValue();
						this.setDescription(result);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param description
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

}
