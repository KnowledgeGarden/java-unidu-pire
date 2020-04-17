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

 
// $Id: DT.java,v 1.8 2005/02/21 17:29:25 huesselbeck Exp $
package de.unidu.is.retrieval.pire.dt;

import java.util.Iterator;
import java.util.List;

import de.unidu.is.retrieval.pire.Index;

/**
 * A class for an IR datatype.
 * 
 * @author Henrik Nottelmann
 * @since 2004-08-16
 * @version $Revision: 1.8 $, $Date: 2005/02/21 17:29:25 $
 */
public interface DT {

	/**
	 * Converts the operator name into an identifier.
	 * 
	 * @param operator
	 *                   search operator name
	 * @return identifier for operator
	 */
	public abstract String convertOperator(String operator);

	/*
	 * Start: Indexing
	 */

	/**
	 * Add the document content of the specified index.
	 * <p>
	 * 
	 * The specified value is applied to the filter corresponding to the
	 * operator filter, and the resulting tokens are added to the
	 * <code>tf</code> relation (together with the document id and the
	 * frequency of the token).
	 * 
	 * @param index
	 *                   underlying index
	 * @param docID
	 *                   document id
	 * @param operator
	 *                   search operator name
	 * @param value
	 *                   attribute value
	 */
	public abstract void addToIndex(Index index, String docID, String operator,
			Object value);

	/**
	 * Computes the indexing weights for the specified index and the operator.
	 * <p>
	 * 
	 * The indexing weights are computed based on the <code>tf</code>
	 * relation.
	 * 
	 * @param index
	 *                   underlying index
	 * @param operator
	 *                   search operator name
	 */
	public abstract void computeIndex(Index index, String operator);

	/**
	 * Returns an iterator over all tokens for whom the indexing weight moments
	 * have to be computed.
	 * 
	 * TODO: really useful?
	 * 
	 * @param index
	 *                   underlying index
	 * @param operator
	 *                   search operator name
	 * @return iterator over all tokens
	 */
	public abstract Iterator getIndexTokens(Index index, String operator);

	/**
	 * Removes the datatype/operator specific relations from the specified
	 * index.
	 * 
	 * @param index
	 *                   underlying index
	 * @param operator
	 *                   search operator name
	 */
	public abstract void removeIndex(Index index, String operator);

	/*
	 * Start: Retrieval (RSVs)
	 */

	/**
	 * Tests whether the RSVs are already stored in the <code>weight</code>
	 * table, or if they are computed from outside.
	 * 
	 * TODO: why do we need that?
	 * 
	 * @param operator
	 *                   search operator name
	 * @return true, if the RSVs are already stored
	 */
	public abstract boolean storedRSVs(String operator);

	/**
	 * Adds rules for evaluating the specified query condition to the specified
	 * list.
	 * <p>
	 * 
	 * @param index
	 *                   underlying index
	 * @param queryID
	 *                   query id
	 * @param subqueryID
	 *                   subquery id
	 * @param operator
	 *                   search operator name
	 * @param weight
	 *                   condition weight
	 * @param value
	 *                   comparison value
	 * @param addList
	 *                   list to where the condition rules have to be added
	 */
	public abstract void addRSVRules(Index index, String queryID,
			String subqueryID, String operator, double weight, Object value,
			List addList);

	/**
	 * Adds rules for computing probabilities of relevance to the list. The
	 * probabilities of relevance are computed from the RSVs (probabilities of
	 * inference) by applying a mapping function.
	 * 
	 * @param index
	 *                   underlying index
	 * @param queryID
	 *                   query id
	 * @param subqueryID
	 *                   subquery id
	 * @param operator
	 *                   search operator name
	 * @param addList
	 *                   list to which rules can be appended
	 * 
	 */
	public abstract void addProbRules(Index index, String queryID,
			String subqueryID, String operator, List addList);

}
