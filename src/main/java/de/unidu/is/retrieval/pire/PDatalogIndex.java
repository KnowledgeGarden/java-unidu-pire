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

// $Id: PDatalogIndex.java,v 1.20 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.pire;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.unidu.is.expressions.EqualsExpression;
import de.unidu.is.expressions.Expression;
import de.unidu.is.expressions.FunctionExpression;
import de.unidu.is.pdatalog.EDBRelation;
import de.unidu.is.pdatalog.IDBRelation;
import de.unidu.is.pdatalog.Relation;
import de.unidu.is.pdatalog.RelationBase;
import de.unidu.is.pdatalog.ds.Constant;
import de.unidu.is.pdatalog.ds.DBColExpression;
import de.unidu.is.pdatalog.ds.DBProbExpression;
import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.pdatalog.ds.Literal;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.ds.Variable;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.retrieval.ProbDoc;
import de.unidu.is.sql.SQL;

/**
 * A document index, represented by specific relations. using pDatalog++.
 * 
 * @author Henrik Nottelmann
 * @since 2003-11-09
 * @version $Revision: 1.20 $, $Date: 2005/03/14 17:33:14 $
 */
public class PDatalogIndex implements Index {

	/**
	 * The relation base used by this index.
	 *  
	 */
	protected RelationBase base;

	/**
	 * The name of the corresponding collection.
	 *  
	 */
	protected String collectionName;

	/**
	 * The prefix for the relations, containing the collection name and the
	 * index key.
	 */
	protected String name;

	/**
	 * Map with relations which names of temporary relations which have to be
	 * deleted after retrieval. Keys are query IDs (as strings), the values are
	 * sets of relation names (as strings).
	 */
	protected Map removeRelations;

	/**
	 * Creates a new index.
	 * 
	 * @param base
	 *                   relation base
	 * @param collectionName
	 *                   name of the collection
	 * @param key
	 *                   index key
	 */
	public PDatalogIndex(RelationBase base, String collectionName, String key) {
		this.base = base;
		this.collectionName = collectionName;
		this.name = collectionName + (key != null ? "_" + key : "");
		this.removeRelations = new HashMap();
		initRelations(false);
	}

	/*
	 * Start: Low-level method
	 */

	/**
	 * Converts a relative relation name into an absolute one, using the
	 * collection name and the index key as a prefix.
	 * 
	 * @param relationName
	 *                   relative name of the relation
	 * @return absolute name of the relation
	 */
	public String convert(String relationName) {
		return name + "_" + relationName;
	}

	/**
	 * Converts a relative relation name into an absolute one, using only the
	 * collection name as a prefix.
	 * 
	 * @param relationName
	 *                   relative name of the relation
	 * @return absolute name of the relation
	 */
	public String convertCollection(String relationName) {
		return collectionName + "_" + relationName;
	}

	/*
	 * Indexing
	 */

	/**
	 * Inits the index.
	 *  
	 */
	public void init() {
		initRelations(true);
	}

	/**
	 * Inits standard relations for this index.
	 * 
	 * @param create
	 *                   if true, the relations are physically created
	 */
	private void initRelations(boolean create) {
		addEDBRelation(convert(DOCID_RELATION), 1, create);
		addEDBRelation(convert(TF_RELATION), 3, create);
		addIDBRelation(convert(WEIGHT_RELATION), 2, create);
		addIDBRelation(convert(DF_RELATION), 2, create);
		addEDBRelation(convert(RD_RELATION), 2, create);
		addIDBRelation(convert(IDB_RD_RELATION), 2, create);
	}

	/**
	 * Inserts the document id to the corresponding table.
	 * 
	 * @param docID
	 *                   document id
	 */
	public void insert(String docID) {
		if (true)
			base.add(new Fact(new Literal(convert(DOCID_RELATION),
					new Expression[]{new Constant(docID)}, true)));
		//		else if (false)
		//			base.add(convert(DOCID_RELATION), docID + ",1");
		//		else
		//			base.add(convert(DOCID_RELATION), "('" + docID + "',1)");
	}

	/**
	 * Inserts the specified value into the given document.
	 * 
	 * @param docID
	 *                   document id
	 * @param token
	 *                   token
	 * @param tokencount
	 *                   number of occurences of the value
	 */
	public void insert(String docID, String token, int tokencount) {
		if (true)
			base
					.add(new Fact(
							new Literal(convert(TF_RELATION),
									new Expression[]{
											new Constant(docID),
											new Constant(token),
											new Constant(Integer
													.toString(tokencount))},
									true)));
		//		else if (false)
		//			base.add(convert(TF_RELATION), docID + "," + token + ","
		//					+ tokencount + ",1");
		//		else
		//			base.add(convert(TF_RELATION), "('" + docID + "','" + token + "','"
		//					+ tokencount + "',1)");

	}

	/**
	 * Computes the moments of the indexing weights.
	 */
	public void computeMoments() {
		Relation rel = getRelation(convert(WEIGHT_RELATION));
		String weights = convert(WEIGHT_RELATION);
		String df = convert(DF_RELATION);
		String rd = convert(IDB_RD_RELATION);
		Rule rule = null;

		// check if df has to be computed
		int dfCount = base.getTupleCount(base.get(df));
		if (dfCount == 0) {
			rule = Parser.parseRule(df + "(T,DF) :- " + "count(DF,D,{ "
					+ weights + "(D,T,~) }).");
			rule.setOptimizable(true);
			compute(rule);
		}

		// compute expectation
		String exphelp = convert("exphelp");
		addIDBRelation(exphelp, 1, true);
		rule = Parser.parseRule(exphelp + "(T) :- " + weights
				+ "(D,T,Y) | (&groupsum(PROB)).");
		rule.setOptimizable(true);
		compute(rule); // sum of weights
		String expectation = convert(EXPECTATION_RELATION);
		addIDBRelation(expectation, 1, true);
		rule = Parser.parseRule(expectation + "(T) :- " + exphelp + "(T) & "
				+ rd + "('numdocs',NUMDOCS) | (PROB/NUMDOCS).");
		rule.setOptimizable(true);
		compute(rule); // sum of weights/number of documents
		removeRelation(exphelp);

		// compute variance
		String varhelp = convert("varhelp");
		addIDBRelation(varhelp, 1, true);
		rule = Parser.parseRule(varhelp + "(T) :- " + weights + "(X,T) & "
				+ expectation
				+ "(T) | (&groupsum((PROB1-PROB2)*(PROB1-PROB2))).");
		rule.setOptimizable(true);
		compute(rule); // sum of (weight-expectation)^2
		String variance = convert(VARIANCE_RELATION);
		addIDBRelation(variance, 1, true);
		rule = Parser.parseRule(variance + "(T) :- " + varhelp + "(T) & " + df
				+ "(T,DF) & " + rd + "('numdocs',NUMDOCS) & " + expectation
				+ "(T) | ((PROB1+((NUMDOCS-DF)*(PROB4*PROB4)))/(NUMDOCS-1)).");
		rule.setOptimizable(true);
		compute(rule); // add missing docs, scale
		removeRelation(varhelp);
	}

	/**
	 * Removes the index.
	 */
	public void remove() {
		removeRelation(convert(DOCID_RELATION));
		removeRelation(convert(TF_RELATION));
		removeRelation(convert(DF_RELATION));
		removeRelation(convert(WEIGHT_RELATION));
		removeRelation(convert(RD_RELATION));
		removeRelation(convert(IDB_RD_RELATION));
		removeRelation(convert(EXPECTATION_RELATION));
		removeRelation(convert(VARIANCE_RELATION));
	}

	/*
	 * Retrieval
	 */

	/**
	 * Initialises the relation for the specified query.
	 * 
	 * @param queryID
	 *                   query id
	 */
	public void initQuery(String queryID) {
		String result = getProbRelation(queryID, null);
		addIDBRelation(result, 1, true);
	}

	/**
	 * Add a rule with the given relations and a single argument to the
	 * specified list.
	 * 
	 * @param queryID
	 *                   query id
	 * @param relations
	 *                   array of relation names
	 * @param prob
	 *                   probability of the rule
	 * @param addList
	 *                   list to which the rule is added
	 */
	public void addRule(String queryID, String[] relations, double prob,
			List addList) {
		List body = new LinkedList();
		Set rr = (Set) removeRelations.get(queryID);
		if (rr == null) {
			rr = new HashSet();
			removeRelations.put(queryID, rr);
		}
		for (int i = 0; i < relations.length; i++) {
			body.add(new Literal(relations[i], new Expression[]{new Variable(
					"D")}, true));
			rr.add(relations[i]);
		}
		Rule rule = new Rule(prob, new Literal(getProbRelation(queryID, null),
				new Expression[]{new Variable("D")}, true), body);
		rule.setOptimizable(true);
		addList.add(rule);
	}

	/**
	 * Computes the probabilities of relevance for the given query based on the
	 * RSVs specified by the list of rules.
	 * 
	 * @param queryID
	 *                   query id
	 * @param list
	 *                   list of rules for computing the probabilities of relevance
	 * @param computeDisjoint
	 *                   if true, disjointness is assumed
	 */
	public void computeProbs(String queryID, List list, boolean computeDisjoint) {
		String relationName = getProbRelation(queryID, null);
		if (computeDisjoint)
			computeDisjoint(relationName, 1, list);
		else
			compute(relationName, 1, list);

		// remove temporary relations
		Set rr = (Set) removeRelations.get(queryID);
		if (rr != null) {
			for (Iterator iter = rr.iterator(); iter.hasNext();) {
				String relation = (String) iter.next();
				removeRelation(relation);
			}
			removeRelations.remove(queryID);
		}
	}

	/**
	 * Returns the maximum RSV for the specified query condition.
	 * 
	 * @param queryID
	 *                   query id
	 * @param conditionID
	 *                   condition identifier (in the relation name)
	 * @return maximum RSV for the specified query
	 */
	public double getMaxRSV(String queryID, String conditionID) {
		return getMax(getRSVRelation(queryID, conditionID));
	}

	/**
	 * Returns the maximum probability for the specified relation.
	 * 
	 * @param relation
	 *                   relation name
	 * @return maximum probability for the specified relation
	 */
	public double getMax(String relation) {
		double max = 0;
		SQL sql = new SQL();
		sql.setSelect(Collections.singletonList(new FunctionExpression("max",
				new DBProbExpression(relation))));
		sql.setFrom(Collections.singletonList(relation));
		ResultSet rs = null;
		try {
			rs = base.performQuery(sql);
			if (rs.next())
				max = rs.getDouble(1);
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			if (rs != null)
				base.close(rs);
		}
		return max;
	}

	/**
	 * Returns the document ids and probabilities of relevance of the first
	 * documents.
	 * 
	 * @param queryID
	 *                   query id
	 * @param numDocs
	 *                   number of documents to be retrieved
	 * @return list of ProbDoc instances
	 */
	public List getProbs(String queryID, int numDocs) {
		List results = new ArrayList();
		String relation = getProbRelation(queryID, null);
		if (!base.existsRelation(relation))
			return results;
		SQL sql = new SQL();
		List l1 = new ArrayList();
		l1.add(new DBColExpression(relation, 0));
		l1.add(new DBProbExpression(relation));
		sql.setSelect(l1);
		List l2 = new ArrayList();
		l2.add(relation);
		sql.setFrom(l2);
		List l3 = new ArrayList();
		l3.add(new DBProbExpression(relation));
		sql.setOrder(l3);
		sql.setOrderDesc(true);
		sql.setLimit(numDocs);
		ResultSet rs = null;
		try {
			rs = base.performQuery(sql);
			while (rs.next()) {
				String docID = rs.getString(1);
				double prob = rs.getDouble(2);
				results.add(new ProbDoc(docID, prob));
			}
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			if (rs != null)
				base.close(rs);
		}
		return results;
	}

	/**
	 * Finishes the processing of this query and frees used resources.
	 * 
	 * @param queryID
	 *                   query id
	 */
	public void closeQuery(String queryID) {
		// remove result relation
		removeRelation(getProbRelation(queryID, null));
	}

	/*
	 * Computing moments for query
	 */

	/**
	 * Adds a query condition for computing moments. This method can apply the
	 * condition directly (i.e., it computes the moments for that condition) or
	 * store it for later reference in <code>computeMoments</code>.
	 * 
	 * @param queryID
	 *                   query id
	 * @param weight
	 *                   condition weight
	 * @param value
	 *                   comparison value
	 */
	public void addMomentsCondition(String queryID, double weight,
			Object value, List addList) {
		String relation = getProbRelation(queryID, null);
		Rule rule = new Rule(weight, new Literal(relation,
				new Expression[]{new Constant(Index.EXPECTATION_RELATION)},
				true), new Literal(convert(Index.EXPECTATION_RELATION),
				new Expression[]{new Constant(value)}, true));
		rule.setOptimizable(true);
		addList.add(rule);
		rule = new Rule(weight * weight, new Literal(relation,
				new Expression[]{new Constant(Index.VARIANCE_RELATION)}, true),
				new Literal(convert(Index.VARIANCE_RELATION),
						new Expression[]{new Constant(value)}, true));
		rule.setOptimizable(true);
		addList.add(rule);
	}

	/**
	 * Finishes the computation of the moments.
	 * 
	 * @param queryID
	 *                   query id
	 */
	public void computeMoments(String queryID, List ruleList) {
		String result = getProbRelation(queryID, null);
		IDBRelation rel = (IDBRelation) getRelation(result);
		computeDisjoint(rel, ruleList);
	}

	/*
	 * Relation stuff.
	 */

	/**
	 * Returns the value corresponding to the specified key in the
	 * <code>rd</code> relation.
	 * <p>
	 * 
	 * Currently, only extensionally added values (via
	 * <code>setRD(String,double)</code>) can be retrieved, but not computed
	 * values.
	 * 
	 * @param key
	 *                   value key
	 * @return value
	 */
	public double getRD(String key) {
		return getRD(key, 0);
	}

	/**
	 * Returns the value corresponding to the specified key in the
	 * <code>rd</code> or <code>idb_rd</code> relations or a default value
	 * if no entry is found.
	 * <p>
	 * 
	 * @param key
	 *                   value key
	 * @param defaultValue
	 *                   default value used if no value is found
	 * @return value for the specified key
	 */
	public double getRD(String key, double defaultValue) {
		String relation = convert(RD_RELATION);
		SQL sql = new SQL();
		sql.setSelect(Collections
				.singletonList(new DBColExpression(relation, 1)));
		sql.setFrom(Collections.singletonList(relation));
		sql.setWhere(Collections.singletonList(new EqualsExpression(
				new DBColExpression(relation, 0), new Constant(key))));
		double ret = defaultValue;
		boolean found = false;
		ResultSet rs = null;
		try {
			rs = base.performQuery(sql);
			if (rs.next()) {
				ret = rs.getDouble(1);
				found = true;
			}
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			if (rs != null)
				base.close(rs);
		}
		if (!found) {
			relation = convert(IDB_RD_RELATION);
			sql = new SQL();
			sql.setSelect(Collections.singletonList(new DBColExpression(
					relation, 1)));
			sql.setFrom(Collections.singletonList(relation));
			sql.setWhere(Collections.singletonList(new EqualsExpression(
					new DBColExpression(relation, 0), new Constant(key))));
			rs = null;
			try {
				rs = base.performQuery(sql);
				if (rs.next()) {
					ret = rs.getDouble(1);
					found = true;
				}
			} catch (SQLException e) {
				de.unidu.is.util.Log.error(e);
			} finally {
				if (rs != null)
					base.close(rs);
			}
		}
		return ret;
	}

	/**
	 * Sets the value corresponding to the specified key in the <code>rd</code>
	 * relation.
	 * 
	 * @param key
	 *                   value key
	 * @param value
	 *                   value
	 */
	public void setRD(String key, double value) {
		add(new Fact(new Literal(convert(RD_RELATION), new Expression[]{
				new Constant(key), new Constant(Double.toString(value))}, true)));
	}

	/**
	 * Adds the fact to the knowledge base.
	 * 
	 * @param fact
	 *                   fact to be added
	 */
	public void add(Fact fact) {
		base.add(fact);
	}

	/**
	 * Computes the result of the specified rule.
	 * 
	 * @param rule
	 *                   single rule for target relation
	 */
	public void compute(Rule rule) {
		((IDBRelation) base.get(rule.getPredicateName())).compute(rule);
	}

	/**
	 * Computes the result of the specified rules (all corresponding to the
	 * specified IDB relation).
	 * 
	 * @param relation
	 *                   target relation
	 * @param rules
	 *                   collection of rules for target relation
	 */
	public void compute(IDBRelation relation, Collection rules) {
		base.compute(relation, rules);
	}

	/**
	 * Computes the result of the specified rules (all corresponding to the
	 * specified IDB relation).
	 * 
	 * @param relationName
	 *                   target relation name
	 * @param arity
	 *                   target relation arity
	 * @param rules
	 *                   collection of rules for target relation
	 */
	public void compute(String relationName, int arity, Collection rules) {
		IDBRelation relation = new IDBRelation(base, relationName, arity);
		compute(relation, rules);
	}

	/**
	 * Computes the result of the specified rules (all corresponding to the
	 * specified IDB relation). The results from the single rules are considered
	 * to be disjoint.
	 * 
	 * @param relation
	 *                   target relation
	 * @param rules
	 *                   collection of rules for target relation
	 */
	public void computeDisjoint(IDBRelation relation, Collection rules) {
		base.computeDisjoint(relation, rules);
	}

	/**
	 * Computes the result of the specified rules (all corresponding to the
	 * specified IDB relation). The results from the single rules are considered
	 * to be disjoint.
	 * 
	 * @param relationName
	 *                   target relation name
	 * @param arity
	 *                   arity
	 * @param rules
	 *                   collection of rules for target relation
	 */
	public void computeDisjoint(String relationName, int arity, Collection rules) {
		IDBRelation relation = new IDBRelation(base, relationName, arity);
		computeDisjoint(relation, rules);
	}

	/**
	 * Returns an iterator over the specified relation.
	 * 
	 * @param relation
	 *                   relation name
	 * @return iterator over the specified relation (Fact instances)
	 */
	public Iterator iterator(String relation) {
		List list = new ArrayList();
		SQL sql = new SQL();
		List l1 = new ArrayList();
		Relation rel = getRelation(relation);
		for (int i = 0; i < rel.getArity(); i++)
			l1.add(new DBColExpression(relation, i));
		l1.add(new DBProbExpression(relation));
		sql.setSelect(l1);
		List l2 = new ArrayList();
		l2.add(relation);
		sql.setFrom(l2);
		ResultSet rs = null;
		try {
			rs = base.performQuery(sql);
			while (rs.next()) {
				Expression[] array = new Expression[rel.getArity()];
				for (int i = 0; i < rel.getArity(); i++)
					array[i] = new Constant(rs.getString(i + 1));
				double prob = rs.getDouble(rel.getArity() + 1);
				list.add(new Fact(prob, new Literal(relation, array, true)));
			}
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			if (rs != null)
				base.close(rs);
		}
		return list.iterator();
	}

	/**
	 * Returns the specified relation.
	 * 
	 * @param name
	 *                   relation name
	 * @return relation object
	 */
	public Relation getRelation(String name) {
		return base.get(name);
	}

	/**
	 * Creates a new EDB relation.
	 * 
	 * @param name
	 *                   name of the relation
	 * @param arity
	 *                   arity of the relation
	 * @return new EDB relation
	 * @param create
	 *                   if true, the relation is physically created
	 */
	public EDBRelation addEDBRelation(String name, int arity, boolean create) {
		return new EDBRelation(base, name, arity, create);
	}

	/**
	 * Creates a new IDB relation.
	 * 
	 * @param name
	 *                   name of the relation
	 * @param arity
	 *                   arity of the relation
	 * @return new IDB relation
	 * @param create
	 *                   if true, the relation is physically created
	 */
	public IDBRelation addIDBRelation(String name, int arity, boolean create) {
		return new IDBRelation(base, name, arity, create);
	}

	/**
	 * Signals that the specified relation is complete.
	 * 
	 * @param name
	 *                   relation name
	 */
	public void completeRelation(String name) {
		// adds an DB index
		base.addIndex(base.get(name));
	}

	/**
	 * Removes the specified relation from this index.
	 * 
	 * @param name
	 *                   relation name
	 */
	public void removeRelation(String name) {
		base.remove(name);
	}

	/**
	 * @param queryID
	 * @param subqueryID
	 */
	public String getRSVRelation(String queryID, String subqueryID) {
		return convert(RSV_RELATION + queryID
				+ (subqueryID != null ? "_" + subqueryID : ""));
	}

	/**
	 * @param queryID
	 * @param subqueryID
	 */
	public String getProbRelation(String queryID, String subqueryID) {
		return convert(PROB_RELATION + queryID
				+ (subqueryID != null ? "_" + subqueryID : ""));
	}

}