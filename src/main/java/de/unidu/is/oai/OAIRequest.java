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

 
// $Id: OAIRequest.java,v 1.5 2005/02/21 17:29:21 huesselbeck Exp $

/*
 * $Log: OAIRequest.java,v $
 * Revision 1.5  2005/02/21 17:29:21  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.4  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.3  2005/02/21 13:11:47  huesselbeck
 * added "$Id: OAIRequest.java,v 1.5 2005/02/21 17:29:21 huesselbeck Exp $"
 *
 * Revision 1.2  2005/01/24 11:44:27  huesselbeck
 * replaced e.printStackTrace by de.unidu.is.util.Log.error()
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
 * Revision 1.2  2004/06/03 16:07:11  fischer
 * added ListSets, fromId, toId
 *
 * Revision 1.1  2004/06/02 16:03:32  fischer
 * a quick shot at OAI harvesting
 *
 */
package de.unidu.is.oai;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author fischer
 * @version $Revision: 1.5 $
 * 
 * This class represents exactly one OAI request
 */
public abstract class OAIRequest {

	private static Category logger =
		Category.getInstance(OAIRequest.class.getName());

	private String url = null;
	private String verb=null;
	private String modifiers="";
	private String userAgent =
		"Java OAI Harvester, by University of Duisburg-Essen, Germany";
	private String fromEmail = "insert your e-mail here";
	private String requestResult = null;
	private String header=null;
	private String footer=null;
	private String content=null;
	private Document document=null;

	/**
	 * 
	 */
	public OAIRequest(String url, String userAgent, String fromEmail,String verb) {
		setUserAgent(userAgent);
		setFromEmail(fromEmail);
		this.url = url;
		this.verb=verb;
	}

	protected void setUrl(String url) throws OAIException {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	protected String getVerb() {
		return verb;
	}
	
	protected String getModifiers() {
		return modifiers;
	}
	
	protected void setModifiers(String modifiers) {
		this.modifiers=modifiers;
	}

	public String getRequestResult() {
		return requestResult;
	}

	protected void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	protected void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	protected String doRequest() throws OAIException {
		String result = "";
		String request=getRequest();
		logger.debug(request);
		try {

			// Create an URL instance
			URL source = new URL(request);
			URLConnection connection = source.openConnection();
			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("From", fromEmail);
			InputStream in = connection.getInputStream();
			// Create a buffered input stream for efficency
			BufferedInputStream bufIn = new BufferedInputStream(in);
			// Repeat until end of file
			for (;;) {
				int data = bufIn.read(); // Check for EOF
				if (data == -1)
					break;
				else {
					// logger.debug ( (char) data);
					result += (char) data;
				}
			}
		} catch (MalformedURLException mue) {
			throw new OAIException("Request "+request + " returned MalformedURLException: " + mue.toString());
		} catch (IOException ioe) {
			throw new OAIException("Request "+request + " returned IOException: " + ioe.toString());
		}
		setRequestResult(result);
		logger.debug("Got result for "+request);
		return result;
	}

	/**
	 * @param result
	 */
	protected void setRequestResult(String result) {
		this.requestResult=result;
	}

	protected Element getRelevantElement(String elementName)
		throws OAIException {
		Element result = null;
		
		if (document==null) {
			// parse the XML
			try {
				document = parse(getRequestResult());
			} catch (SAXException e) {
				throw new OAIException(
					"Could not parse result of "
						+ elementName
						+ " for "
						+ this.url
						+ " : "
						+ e.getMessage());
			} catch (ParserConfigurationException e) {
				de.unidu.is.util.Log.error(e);
				throw new OAIException(
					"Could not parse result of "
						+ elementName
						+ ": "
						+ e.getMessage());
			} catch (IOException e) {
				throw new OAIException(
					"Could not parse result of "
						+ elementName
						+ ": "
						+ e.getMessage());
			}
		}

		NodeList resultNL = document.getElementsByTagName(elementName);
		if ((resultNL != null)
			&& (resultNL.getLength() > 0)
			&& (resultNL.item(0) instanceof Element)) {
			result = (Element) resultNL.item(0);
		} else {
			// OAI error?
			NodeList errorNL = document.getElementsByTagName("error");
			if ((errorNL != null) && (errorNL.getLength() > 0)) {
				logger.warn("OAI error:\n" + getRequestResult() + "\n");
				result = null;
			}
		}

		if (result == null) {
			throw new OAIException(
				"Get "
					+ elementName
					+ " for "
					+ this.url
					+ " : no "
					+ elementName
					+ " element");
		}
		return result;
	}

	protected synchronized Document parse(String source)
		throws SAXException, ParserConfigurationException, IOException {
		logger.debug("Parsing...");
		DocumentBuilderFactory dbf;
		DocumentBuilder xml_parser;
		dbf = DocumentBuilderFactory.newInstance();
		xml_parser = dbf.newDocumentBuilder();
		xml_parser.setErrorHandler(new DefaultHandler());
		dbf.setValidating(true);
		InputSource is = new InputSource(new StringReader(source));
		Document result = xml_parser.parse(is);
		logger.debug("Parsed result");
		return result;
	}
	
	protected String getRequest() {
		return getUrl()+"?verb="+getVerb()+getModifiers();
	}
	
}
