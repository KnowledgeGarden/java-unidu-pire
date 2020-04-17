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

 
// $Id: AbstractDT.java,v 1.12 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval.pire.dt;

import java.util.Iterator;
import java.util.List;

import de.unidu.is.expressions.Expression;
import de.unidu.is.pdatalog.ds.Constant;
import de.unidu.is.pdatalog.ds.Literal;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.ds.Variable;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.retrieval.pire.Index;
import de.unidu.is.text.Filter;
import de.unidu.is.util.Tuple;

/**
 * An abstract class for IR datatypes.
 * 
 * @author Henrik Nottelmann
 * @since 2003-09-23
 * @version $Revision: 1.12 $, $Date: 2005/03/14 17:33:13 $
 */
public abstract class AbstractDT implements DT {

	/**
	 * Returns a filter for converting a document value into tokens/token
	 * frequency tuples.
	 * 
	 * @param operator
	 *                   operator name
	 * @return filter
	 */
	protected abstract Filter getFilter(String operator);

	/**
	 * Returns a filter for converting a condition comparison value into
	 * tokens/token frequency tuples.
	 * 
	 * @param operator
	 *                   operator name
	 * @return filter
	 */
	protected abstract Filter getQueryFilter(String operator);

	/**
	 * Converts the operator name into an identifier.
	 * <p>
	 * 
	 * This implementation returns the operator name. Subclasses can override
	 * this behaviour.
	 * 
	 * @param operator
	 *                   operator name
	 * @return identifier for operator
	 */
	public String convertOperator(String operator) {
		return operator;
	}

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
	 *                   operator name
	 * @param value
	 *                   attribute value
	 */
	public void addToIndex(Index index, String docID, String operator,
			Object value) {
		Filter filter = getFilter(operator);
		for (Iterator it = filter.apply(value); it.hasNext();) {
			Object o = it.next();
			String token = null;
			int tf = 1;
			if (o instanceof Tuple) {
				Tuple tuple = (Tuple) o;
				token = tuple.getString(0);
				tf = tuple.getInt(1);
			} else
				token = o.toString();
			index.insert(docID, token, tf);
		}
	}

	/**
	 * Computes the indexing weights for the specified index and the operator.
	 * <p>
	 * 
	 * This implementation uses a binary weighting scheme. Subclasses can use
	 * another weighting scheme by overriding this method.
	 * 
	 * @param index
	 *                   underlying index
	 * @param operator
	 *                   operator name
	 */
	public void computeIndex(Index index, String operator) {
		String weight = index.convert(Index.WEIGHT_RELATION);
		index.addIDBRelation(weight, 2, true);
		Rule rule = new Rule(new Literal(index.convert("weight"),
				new Expression[]{new Variable("D"), new Variable("V")}, true),
				new Literal(index.convert("tf"), new Expression[]{
						new Variable("D"), new Variable("V"),
						new Variable("TF")}, true));
		rule.setOptimizable(true);
		index.compute(rule);
	}

	/**
	 * Returns an iterator over all tokens for whom the indexing weight moments
	 * have to be computed.
	 * <p>
	 * 
	 * The default implementation returns null, specifying that all tokens which
	 * stored in the index should be used. Other implementations can override
	 * this behaviour.
	 * 
	 * TODO: really useful?
	 * 
	 * @param index
	 *                   underlying index
	 * @param operator
	 *                   operator name
	 * @return iterator over all tokens
	 */
	public Iterator getIndexTokens(Index index, String operator) {
		return null;
	}

	/**
	 * Removes the index.
	 */
	public void removeIndex(Index index, String operator) {
	}

	/**
	 * Tests whether the RSVs are already stored in the <code>weight</code>
	 * table, or if they are computed from outside.
	 * <p>
	 * 
	 * The default implemenation returns <code>true</code>.
	 * 
	 * TODO: really useful?
	 * 
	 * @param operator
	 *                   search operator
	 * @return true, if the RSVs are already stored
	 */
	public boolean storedRSVs(String operator) {
		return true;
	}

	/**
	 * Adds and evaluates a query condition.
	 * <p>
	 * 
	 * This implementation adds a rule for using the indexing weight directly
	 * for the RSV.
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
	 *                   list to where the condition has to be added
	 */
	public void addRSVRules(Index index, String queryID, String subqueryID,
			String operator, double weight, Object value, List addList) {
		value = getQueryFilter(operator).apply(value).next();
		String rsvRelation = index.getRSVRelation(queryID, subqueryID);
		Rule rule = new Rule(weight, new Literal(rsvRelation,
				new Expression[]{new Variable("D")}, true), new Literal(index
				.convert(Index.WEIGHT_RELATION), new Expression[]{
				new Variable("D"), new Constant(value)}, true));
		rule.setOptimizable(true);
		addList.add(rule);
	}

	/**
	 * Returns a template for computing probabilities of relevance.
	 * <p>
	 * 
	 * The template string is an expression which contains the key
	 * <code>PROB</code>.
	 * 
	 * @param index
	 *                   underlying index
	 * @param queryID
	 *                   query id
	 * @param subqueryID
	 *                   subquery id
	 * @param operator
	 *                   operator name
	 * 
	 * @return template for computing probabilities of relevance
	 */
	protected String getProbsTemplate(Index index, String queryID,
			String subqueryID, String operator) {
		return "(PROB)";
	}

	/**
	 * Computes probabilities of relevance based on the RSVs (probabilities of
	 * inference).
	 * 
	 * @param index
	 *                   underlying index
	 * @param queryID
	 *                   query id
	 * @param subqueryID
	 *                   subquery id
	 * @param operator
	 *                   search operator
	 * @param addList
	 *                   list to which rules can be appended
	 */
	public void addProbRules(Index index, String queryID, String subqueryID,
			String operator, List addList) {
		String rsvRelation = index.getRSVRelation(queryID, subqueryID);
		String probRelation = index.getProbRelation(queryID, subqueryID);
		String template = getProbsTemplate(index, queryID, subqueryID, operator);
		Rule rule = Parser.parseRule(probRelation + "(D) :- " + rsvRelation
				+ "(D) | " + template + ".");
		rule.setOptimizable(true);
		addList.add(rule);
	}

}
