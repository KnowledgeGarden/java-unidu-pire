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

 
// $Id: OAIListRequest.java,v 1.5 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: OAIListRequest.java,v $
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
 * added "$Id: OAIListRequest.java,v 1.5 2005/02/21 17:29:20 huesselbeck Exp $"
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.3  2004/07/12 12:08:22  fischer
 * undid previous tries at quick parsing
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
import org.w3c.dom.Text;

/**
 * @author fischer
 * @version $Revision: 1.5 $
 *
 */
public abstract class OAIListRequest extends OAIRequest {

	private static Category logger =
		Category.getInstance(OAIListRequest.class.getName());

	private String resumptionToken = null;

	/**
	 * @param url
	 * @param userAgent
	 * @param fromEmail
	 */
	public OAIListRequest(
		String url,
		String userAgent,
		String fromEmail,
		String verb) {
		super(url, userAgent, fromEmail, verb);
	}

	public OAIListRequest(
		String url,
		String userAgent,
		String fromEmail,
		String verb,
		String resumptionToken) {
		super(url, userAgent, fromEmail, verb);
		setResumptionToken(resumptionToken);
	}

	protected String doRequest() throws OAIException {
		String result = super.doRequest();
		// extract resumptionToken, if present
		String rt = null;
		try {
			rt =extractResumptionToken();
		} catch (OAIException e) {
			logger.debug("No resumption token found");
			rt = null;
		}
		setResumptionToken(rt);
		return result;
	}

	protected String extractResumptionToken() throws OAIException {
		String result = null;
		Element element=getRelevantElement("resumptionToken");
		Node rtNode = element.getFirstChild();
		if ((rtNode != null) && (rtNode instanceof Text)) {
			String rt = ((Text) rtNode).getNodeValue();
			rt = (rt != null) ? rt.trim() : null;
			result = ((rt != null) && (!(rt.equals("")))) ? rt : null;
		}
		return result;
	}

	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	protected String getRequest() {
		if (resumptionToken == null) {
			return super.getRequest();
		} else {
			return getUrl()
				+ "?verb="
				+ getVerb()
				+ "&resumptionToken="
				+ resumptionToken;
		}
	}
}
