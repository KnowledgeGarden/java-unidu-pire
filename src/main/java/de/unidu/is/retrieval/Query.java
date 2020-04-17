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

 
// $Id: Query.java,v 1.11 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval;

import de.unidu.is.util.HashPropertyMap;
import de.unidu.is.util.PropertyMap;

/**
 * An abstract query class.
 * 
 * @author Henrik Nottelmann
 * @since 2004-01-03
 * @version $Revision: 1.11 $, $Date: 2005/03/14 17:33:13 $
 */
public abstract class Query {

	/**
	 * The query ID.
	 */
	private String queryID;

	/**
	 * The number of documents which should be retrieved.
	 */
	private int numDocs;

	/**
	 * Query metadata (for free use).
	 */
	private PropertyMap metadata;

	/**
	 * Creates a new query.
	 */
	public Query() {
		metadata = new HashPropertyMap();
	}

	/**
	 * Creates a new query.
	 * 
	 * @param queryID query ID
	 * @param numDocs number of documents
	 */
	public Query(String queryID, int numDocs) {
		this();
		setQueryID(queryID);
		setNumDocs(numDocs);
	}

	/**
	 * Sets the query ID.
	 * 
	 * @param queryID query ID
	 */
	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}

	/**
	 * Returns the query ID.
	 * 
	 * @return query ID query ID
	 */
	public String getQueryID() {
		return queryID;
	}

	/**
	 * Sets the number of documents which should be retrieved.
	 * 
	 * @param numDocs number of documents which should be retrieved
	 */
	public void setNumDocs(int numDocs) {
		this.numDocs = numDocs;
	}

	/**
	 * Returns the number of documents which should be retrieved.
	 * 
	 * @return number of documents which should be retrieved
	 */
	public int getNumDocs() {
		return numDocs;
	}

	/**
	 * Returns the metadata.
	 * 
	 * @return query metadata
	 */
	public PropertyMap getMetadata() {
		return metadata;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Query))
			return false;
		Query q = (Query) obj;
		return q.queryID.equals(queryID)
			&& q.numDocs == numDocs
			&& q.metadata.equals(metadata);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return queryID.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = toStringAdditional();
		return "Query[id="
			+ queryID + (s==null ? "" : ", " + s)
			+ ", numDocs="
			+ numDocs
			+ ", metadata="
			+ metadata
			+ "]";
	}

	/**
	 * Returns a compact representation.
	 * 
	 * @return compact representation
	 */
	protected String toStringAdditional() {
		return null;
	}
	
}
