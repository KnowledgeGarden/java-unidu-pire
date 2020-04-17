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

 
// $Id: ProbDoc.java,v 1.7 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval;

/**
 * A (document ID,weight) pair.
 * 
 * @author Henrik Nottelmann
 * @since 2004-01-02
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:13 $
 */
public class ProbDoc implements Comparable {

	/**
	 * The ID of the document.
	 */
	protected String docID;

	/**
	 * The weight of the document.
	 */
	protected double weight;

	/**
	 * Creates a new, empty instance.
	 */
	public ProbDoc() {
	}

	/**
	 * Creates a new instance.
	 */
	public ProbDoc(String docID, double weight) {
		setDocID(docID);
		setWeight(weight);
	}

	/**
	 * Returns the ID of the document.
	 * 
	 * @return ID of the document
	 */
	public String getDocID() {
		return docID;
	}

	/**
	 * Sets the ID of the document.
	 * 
	 * @param docID ID of the document.
	 */
	public void setDocID(String docID) {
		this.docID = docID;
	}

	/**
	 * Returns the weight of the document.
	 *  
	 * @return weight of the document.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of the document.
	 * 
	 * @param weight weight of the document
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof ProbDoc))
			return false;
		ProbDoc doc = (ProbDoc) obj;
		return doc.docID.equals(docID) && doc.weight == weight;
	}

	/**
	 * Compares this entry (the score in decreasing order) with the
	 * specified one.
	 * 
	 * @param obj the reference object with which to compare.
	 * @return negative, zero, positive
	 */
	public int compareTo(Object obj) {
		ProbDoc doc = (ProbDoc) obj;
		if (doc.weight > weight)
			return 1;
		if (doc.weight < weight)
			return -1;
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return toString().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "ProbDoc[" + docID + "," + weight + "]";
	}

}
