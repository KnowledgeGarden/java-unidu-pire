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


// $Id: XMLDoc.java,v 1.7 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval;

import de.unidu.is.util.XMLUtilities;
import org.w3c.dom.Document;

/**
 * A (document ID,xml document) pair.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:13 $
 * @since 2004-03-15
 */
public class XMLDoc {

    /**
     * The ID and weight of the document.
     */
    protected ProbDoc doc;

    /**
     * The XML document.
     */
    protected Document xml;

    /**
     * Creates a new, empty instance.
     */
    public XMLDoc() {
    }

    /**
     * Creates a new instance.
     *
     * @param docID ID of the document
     * @param xml   XML content
     */
    public XMLDoc(String docID, Document xml) {
        this(new ProbDoc(docID, 1), xml);
    }

    /**
     * Creates a new instance.
     *
     * @param doc document
     * @param xml XML content
     */
    public XMLDoc(ProbDoc doc, Document xml) {
        setProbDoc(doc);
        setXML(xml);
    }

    /**
     * Returns the ID of the document.
     *
     * @return ID and weight of the document
     */
    public ProbDoc getProbDoc() {
        return doc;
    }

    /**
     * Sets the ID and weight of the document.
     *
     * @param doc ID and weight of the document.
     */
    public void setProbDoc(ProbDoc doc) {
        this.doc = doc;
    }

    /**
     * Returns the XML document.
     *
     * @return XML document
     */
    public Document getXML() {
        return xml;
    }

    /**
     * Sets the XML document.
     *
     * @param xml XML document
     */
    public void setXML(Document xml) {
        this.xml = xml;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof XMLDoc))
            return false;
        XMLDoc xdoc = (XMLDoc) obj;
        return xdoc.getProbDoc().getDocID().equals(doc.getDocID());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return doc.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "XMLDoc[" + doc + "," + XMLUtilities.toString(xml) + "]";
    }

}
