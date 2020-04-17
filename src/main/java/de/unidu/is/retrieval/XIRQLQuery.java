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

 
// $Id: XIRQLQuery.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;


/**
 * An abstract XIRQL query.
 * 
 * @author Henrik Nottelmann
 * @since 2005-01-03
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 */
public abstract class XIRQLQuery extends Query{

	/**
	 * Creates a new object.
	 */
	public XIRQLQuery() {
		super();
	}

	/**
	 * Creates a new object.
	 *
	 * @param queryID
	 * @param numDocs
	 */
	public XIRQLQuery(String queryID, int numDocs) {
		super(queryID, numDocs);
	}

	/**
	 * Returns the XIRQL query string.
	 * 
	 * @return XIRQL query string
	 */
	public abstract String getXIRQL();

	/*
	 * @see de.unidu.is.retrieval.Query#toStringAdditional()
	 */
	protected String toStringAdditional() {
		return "XIRQL=" + getXIRQL();
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Query))
			return false;
		XIRQLQuery q = (XIRQLQuery) obj;
		return super.equals(obj) && q.getXIRQL().equals(getXIRQL());
	}

}
