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

// $Id: PIRE.java,v 1.30 2005/03/18 22:02:33 nottelma Exp $
package de.unidu.is.retrieval.pire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.unidu.is.pdatalog.RelationBase;
import de.unidu.is.pdatalog.ds.Constant;
import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.retrieval.QueryCondition;
import de.unidu.is.retrieval.Schema;
import de.unidu.is.retrieval.SchemaElement;
import de.unidu.is.retrieval.WeightedQueryCondition;
import de.unidu.is.retrieval.pire.dt.DT;
import de.unidu.is.statistics.Moments;
import de.unidu.is.util.DB;
import de.unidu.is.util.HashPropertyMap;
import de.unidu.is.util.Log;
import de.unidu.is.util.PropertyMap;
import de.unidu.is.util.StringUtilities;

/**
 * An IR engine based on probabilistic Datalog ("Probabilistic Datalog IR
 * Engine").
 * <p>
 * 
 * This IR engine uses a document models consisting of attributes (which refer
 * to a datatype). Datatypes are modelled by special classes in the package
 * <code>de.unidu.is.retrieval.pire.dt</code>, where the class name is the
 * datatype name plus <code>DT</code>.
 * <p>
 * 
 * Probabilistic Datalog is used in an <code>Index</code>. Currently a
 * <code>PDatalogIndex</code> is used, which used the code in
 * <code>de.unidu.is.pdatalog</code>. If another pDatalog implementation
 * should be used, only the method <code>newIndex(String)</code> has to be
 * overwritten in a subclass.
 * 
 * @author Henrik Nottelmann
 * @since 2003-08-16
 * @version $Revision: 1.30 $, $Date: 2005/03/18 22:02:33 $
 */
public class PIRE {

	/**
	 * The name of this collection.
	 */
	protected String collectionName;

	/**
	 * Indexes, specified by their name.
	 */
	protected Map indexes;

	/**
	 * Schema.
	 */
	protected Schema schema;

	/**
	 * Map with temporary conditions for retrieval with weighted-sum queries.
	 * Keys are query IDs (as strings), values are lists of rules (as
	 * WeightedQueryCondition objects).
	 */
	protected Map conditions;

	/**
	 * Map with temporary rules for retrieval with Boolean-style queries. Keys
	 * are query IDs (as strings), values are lists of rules (as Rule objects).
	 */
	protected Map rules;

	/**
	 * Property map with temporary counter for sub-queries. Keys are query IDs
	 * (as strings), values are the counters (as integers).
	 */
	protected PropertyMap counts;

	/**
	 * Dummy index used for retrieval.
	 */
	protected Index dummyIndex;

	/**
	 * The relation base.
	 */
	protected RelationBase base;

	/**
	 * Creates a new instance.
	 * 
	 * @param db
	 *                   database parameters
	 * @param collectionName
	 *                   name of the collection
	 */
	public PIRE(DB db, String collectionName) {
		this.collectionName = collectionName;
		this.base = new RelationBase(db);
		// initialisation now done automatically
		//		new LessEqualsRelation(base);
		//		new LessThanRelation(base);
		//		new GreaterEqualsRelation(base);
		//		new GreaterThanRelation(base);
		//		new EqualsRelation(base);
		//		new StartsWithRelation(base);
		indexes = new HashMap();
		schema = new Schema();
		schema.setRootElement(new SchemaElement("doc"));
		conditions = new HashMap();
		rules = new HashMap();
		counts = new HashPropertyMap();
		dummyIndex = newIndex(null); //"dummy");
	}

	/**
	 * Returns a data type object for the specified schema element
	 * 
	 * @param element
	 *                   schema element
	 * @return data type object
	 */
	protected DT getDT(SchemaElement element) {
		String datatype = element.getDatatypeName();
		DT dt = null;
		try {
			dt = (DT) Class.forName(
					DT.class.getPackage().getName() + "." + datatype + "DT")
					.newInstance();
		} catch (Exception e) {
			Log.error(e);
		}
		return dt;
	}

	/**
	 * Returns the index specified by its name (created based on the attribute
	 * name and the operator name). If no index exists so far, a new one will be
	 * created a added to the indexes map.
	 * 
	 * @param attName
	 *                   attribute name
	 * @param operator
	 *                   name
	 * @return corresponding index
	 */
	public Index getIndex(String attName, String operator) {
		DT dt = getDT(getElement(attName));
		return getIndex(attName + "-" + dt.convertOperator(operator));
	}

	/**
	 * Returns the index specified by its name. If no index exists so far, a new
	 * one will be created a added to the indexes map.
	 * 
	 * @param key
	 *                   index name
	 * @return corresponding index
	 */
	protected Index getIndex(String key) {
		Index index = (Index) indexes.get(key);
		if (index == null) {
			index = newIndex(key);
			indexes.put(key, index);
		}
		return index;
	}

	/**
	 * Creates a new PDatalog index with the specified name.
	 * 
	 * @param key
	 *                   index name
	 * @return new, empty index
	 */
	protected Index newIndex(String key) {
		return new PDatalogIndex(base, StringUtilities.replace(collectionName,
				"-", "_"), key != null
				? StringUtilities.replace(key, "-", "_")
				: null);
	}

	/**
	 * Returns the attribute with the specified name.
	 * 
	 * @param attName
	 *                   attribute name
	 * @return attribute definition
	 */
	protected SchemaElement getElement(String attName) {
		if (!attName.startsWith("/"))
			attName = "/doc/" + attName + "/text()";
		return (SchemaElement) schema.getElement(attName);
	}

	/**
	 * Registers the specified attribute. This only has to be called once per
	 * attribute after creating this IR engine.
	 * 
	 * @param attName
	 *                   attribute name
	 * @param datatype
	 *                   corresponding datatype
	 * @param operators
	 *                   list of operators
	 */
	public void registerAttribute(String attName, String datatype,
			List operators) {
		SchemaElement root = schema.getRootElement();
		SchemaElement e = new SchemaElement(attName);
		SchemaElement t = new SchemaElement("text()", datatype, operators);
		e.add(t);
		root.add(e);
		schema.addAlias("/" + root.getName() + "/" + attName + "/text()",
				attName);
	}

	/**
	 * Returns the value corresponding to the specified key in the
	 * <code>rd</code> relation in the specified index.
	 * 
	 * @param attName
	 *                   schema attribute name
	 * @param operator
	 *                   search operator
	 * @param key
	 *                   value key
	 * @return value
	 */
	public double getRD(String attName, String operator, String key) {
		return getIndex(attName, operator).getRD(key);
	}

	/**
	 * Sets the value corresponding to the specified key in the <code>rd</code>
	 * relation in the specified index.
	 * 
	 * @param attName
	 *                   schema attribute name
	 * @param operator
	 *                   search operator
	 * @param key
	 *                   value key
	 * @param value
	 *                   value
	 */
	public void setRD(String attName, String operator, String key, double value) {
		getIndex(attName, operator).setRD(key, value);
	}

	/**
	 * Inits the index.
	 */
	public void initIndex() {
		indexes.clear();
		for (Iterator it = schema.getXPaths().iterator(); it.hasNext();) {
			String xpath = (String) it.next();
			String attName = schema.getAlias(xpath);
			SchemaElement att = schema.getElement(xpath);
			List operators = att.getOperators();
			for (Iterator it2 = operators.iterator(); it2.hasNext();) {
				String operator = (String) it2.next();
				getIndex(attName, operator).init();
			}
		}
	}

	/**
	 * Add the document id to the corresponding table.
	 * 
	 * @param docID
	 *                   document id
	 */
	public void addToIndex(String docID) {
		for (Iterator it = schema.getXPaths().iterator(); it.hasNext();) {
			String xpath = (String) it.next();
			String attName = schema.getAlias(xpath);
			SchemaElement att = schema.getElement(xpath);
			List operators = att.getOperators();
			for (Iterator it2 = operators.iterator(); it2.hasNext();) {
				String operator = (String) it2.next();
				getIndex(attName, operator).insert(docID);
			}
		}
	}

	/**
	 * Add the document value of the specified attribute to the index. Parsing
	 * the value is left to the datatype implementation; so, for text this can
	 * be a fulltext string.
	 * 
	 * @param docID
	 *                   document id
	 * @param attName
	 *                   attribute name
	 * @param value
	 *                   attribute value
	 */
	public void addToIndex(String docID, String attName, Object value) {
		SchemaElement att = getElement(attName);
		if (att != null) {
			List operators = att.getOperators();
			DT dt = getDT(att);
			for (Iterator it = operators.iterator(); it.hasNext();) {
				String operator = (String) it.next();
				Index index = getIndex(attName, operator);
				dt.addToIndex(index, docID, operator, value);
			}
		}
	}

	//public void upload() {
	//base.upload();
	//}

	/**
	 * Computes the index, based on the document values added.
	 */
	public void computeIndex() {
		for (Iterator it = schema.getXPaths().iterator(); it.hasNext();) {
			String xpath = (String) it.next();
			String attName = schema.getAlias(xpath);
			SchemaElement att = schema.getElement(xpath);
			List operators = att.getOperators();
			DT dt = getDT(att);
			for (Iterator it2 = operators.iterator(); it2.hasNext();) {
				String operator = (String) it2.next();
				Index index = getIndex(attName, operator);
				dt.computeIndex(index, operator);
			}
		}
	}

	/**
	 * Computes the moments of the indexing weights.
	 */
	public void computeMoments() {
		for (Iterator it = schema.getXPaths().iterator(); it.hasNext();) {
			String xpath = (String) it.next();
			String attName = schema.getAlias(xpath);
			SchemaElement att = schema.getElement(xpath);
			List operators = att.getOperators();
			for (Iterator it2 = operators.iterator(); it2.hasNext();) {
				String operator = (String) it2.next();
				Index index = getIndex(attName, operator);
				index.computeMoments();
			}
		}
	}

	/**
	 * Removes the index.
	 */
	public void removeIndex() {
		for (Iterator it = schema.getXPaths().iterator(); it.hasNext();) {
			String xpath = (String) it.next();
			String attName = schema.getAlias(xpath);
			SchemaElement att = schema.getElement(xpath);
			List operators = att.getOperators();
			DT dt = getDT(att);
			for (Iterator it2 = operators.iterator(); it2.hasNext();) {
				String operator = (String) it2.next();
				Index index = getIndex(attName, operator);
				index.remove();
				dt.removeIndex(index, operator);
			}
		}
	}

	/**
	 * Inits the query.
	 * 
	 * @param queryID
	 *                   query id
	 */
	public void initQuery(String queryID) {
		dummyIndex.initQuery(queryID);
	}

	/**
	 * Adds a condition for a weighted sum query. These conditions are collected
	 * and evaluated later in <code>computeProbs(String)</code>, as
	 * conditions for the same attribute/operator pair have to be evaluated
	 * together.
	 * 
	 * @param queryID
	 *                   query id
	 * @param attName
	 *                   schema attribute name
	 * @param operator
	 *                   search operator
	 * @param weight
	 *                   condition weight
	 * @param value
	 *                   comparison value
	 */
	public void addCondition(String queryID, String attName, String operator,
			double weight, Object value) {
		addCondition(queryID, new WeightedQueryCondition(weight, attName,
				operator, value.toString()));
	}

	/**
	 * Adds a condition for a weighted sum query. These conditions are collected
	 * and evaluated later in <code>computeProbs(String)</code>, as
	 * conditions for the same attribute/operator pair have to be evaluated
	 * together.
	 * 
	 * @param queryID
	 *                   query id
	 * @param cond
	 *                   weighted query condition
	 */
	public void addCondition(String queryID, WeightedQueryCondition cond) {
		getConditionList(queryID).add(cond);
	}

	/**
	 * Adds a conjunction for a Boolean-style query in disjunctive form. The
	 * conjunction forms one term in the disjunctive form. This method computes
	 * both the RSV and the probabilities of relevance for the given conditions,
	 * and adds a rule for computing the conjunction (of the probabilities of
	 * relevance) to the dummy index. These rules are later evaluated together
	 * in <code>computeProbs(String)</code>.
	 * 
	 * @param queryID
	 *                   query id
	 * @param conditions
	 *                   conditions forming a conjunction
	 */
	public void addConjunction(String queryID, QueryCondition[] conditions) {
		// create a single rule from the conditions
		List addList = getRuleList(queryID);
		String[] relations = new String[conditions.length];
		QueryCondition[] qc = new QueryCondition[1];
		for (int i = 0; i < conditions.length; i++) {
			qc[0] = conditions[i];
			// compute rsv and probability of relevance for single condition
			relations[i] = addConditions(queryID, qc);
		}
		// create rule for combining the probabilities of relevance
		dummyIndex.addRule(queryID, relations, 1, addList);
	}

	/**
	 * Computes probabilities of relevance based on the documents' RSV.
	 * 
	 * @param queryID
	 *                   query id
	 */
	public void computeProbs(String queryID) {
		List conditionList = (List) conditions.get(queryID);
		if (conditionList != null)
			handleWeightedSumConditions(queryID, conditionList);

		// compute probs
		List addList = getRuleList(queryID);
		boolean disjoint = conditionList != null;
		dummyIndex.computeProbs(queryID, addList, disjoint);

		// cleanup
		rules.remove(queryID);
		counts.remove(queryID);
		conditions.remove(queryID);
	}

	/**
	 * Creates rules based on the collected weighted sum query conditions.
	 * 
	 * @param queryID
	 *                   query id
	 * @param conditionList
	 *                   list of conditions
	 */
	private void handleWeightedSumConditions(String queryID, List conditionList) {
		for (Iterator iter = schema.getXPaths().iterator(); iter.hasNext();) {
			String alias = schema.getAlias((String) iter.next());
			SchemaElement element = schema.getElement(alias);
			List operators = element.getOperators();
			for (Iterator iterator = operators.iterator(); iterator.hasNext();) {
				String operator = (String) iterator.next();
				List l = new LinkedList();
				for (Iterator iterator2 = conditionList.iterator(); iterator2
						.hasNext();) {
					QueryCondition cond = (QueryCondition) iterator2.next();
					if (cond.getPath().equals(alias)
							&& cond.getOperator().equals(operator))
						l.add(cond);
				}
				if (!l.isEmpty()) {
					List addList = getRuleList(queryID);
					// rule for adding the probability of relevance for
					// attribute/operator pair
					QueryCondition[] qc = (QueryCondition[]) l
							.toArray(new QueryCondition[l.size()]);
					String[] relations = new String[]{addConditions(queryID, qc)};
					dummyIndex.addRule(queryID, relations, getScaling(qc),
							addList);
				}
			}
		}
	}

	/**
	 * Creates rules for specified conditions for the same attribute/operator
	 * pair.
	 * 
	 * @param queryID
	 *                   query id
	 * @param conditions
	 *                   query conditions
	 * @return name of prob relation for these conditions
	 */
	private String addConditions(String queryID, QueryCondition[] conditions) {
		List addList = new LinkedList();
		String subqueryID = "" + counts.getInt(queryID);
		counts.inc(queryID);
		Index index = null;
		String operator = null;
		DT dt = null;
		double scaling = getScaling(conditions);
		// get rules for RSV
		for (int i = 0; i < conditions.length; i++) {
			String attName = conditions[i].getPath();
			operator = conditions[i].getOperator();
			String value = conditions[i].getValue();
			double weight = conditions[i] instanceof WeightedQueryCondition
					? ((WeightedQueryCondition) conditions[i]).getWeight()
							/ scaling
					: 1;
			if (index == null) {
				SchemaElement att = getElement(attName);
				dt = getDT(att);
				index = getIndex(attName, operator);
			}
			dt.addRSVRules(index, queryID, subqueryID, operator, weight, value,
					addList);
		}
		String rsvRelation = index.getRSVRelation(queryID, subqueryID);
		String probRelation = index.getProbRelation(queryID, subqueryID);

		// apply rules for RSV
		index.computeDisjoint(rsvRelation, 1, addList);

		// get rules for probabilities of relevance
		List al = new ArrayList();
		dt.addProbRules(index, queryID, subqueryID, operator, al);

		// apply rules for probabilities of relevance
		index.computeDisjoint(probRelation, 1, al);

		// remove RSV relations
		index.removeRelation(rsvRelation);

		return probRelation;
	}

	/**
	 * Computes the scaling factor as the sum of the weights of the conditions.
	 * 
	 * @param conditions
	 *                   query conditions
	 * @return scaling factor
	 */
	private double getScaling(QueryCondition[] conditions) {
		double scaling = 0;
		for (int i = 0; i < conditions.length; i++) {
			String attName = conditions[i].getPath();
			double weight = conditions[i] instanceof WeightedQueryCondition
					? ((WeightedQueryCondition) conditions[i]).getWeight()
					: 1;
			if (conditions[i] instanceof WeightedQueryCondition)
				scaling += weight;
			else
				scaling = 1;
		}
		return scaling;
	}

	/**
	 * Returns the probabilities of relevance for the top-ranked documents in
	 * decreasing order. Before this, <code>computeProbs()</code> has to be
	 * called.
	 * 
	 * @param queryID
	 *                   query id
	 * @param numDocs
	 *                   number of documents to retrieve
	 * @return list of ProbDoc instances
	 */
	public List getResult(String queryID, int numDocs) {
		// just need any index!
		return dummyIndex.getProbs(queryID, numDocs);
	}

	/**
	 * Finishes the processing of this query and frees used resources.
	 * 
	 * @param queryID
	 *                   query id
	 */
	public void closeQuery(String queryID) {
		dummyIndex.closeQuery(queryID);
	}

	/**
	 * Adds a query condition. The results for that condition are inserted into
	 * the results (for expectation and variance).
	 * 
	 * @param queryID
	 *                   query id
	 * @param attName
	 *                   schema attribute name
	 * @param operator
	 *                   search operator
	 * @param weight
	 *                   condition weight
	 * @param value
	 *                   comparison value
	 */
	public void addMomentsCondition(String queryID, String attName,
			String operator, double weight, Object value) {
		// add condition
		List addList = getRuleList(queryID);
		Index index = getIndex(attName, operator);
		index.addMomentsCondition(queryID, weight, value, addList);
	}

	/**
	 * Adds a query condition. The results for that condition are inserted into
	 * the results (for expectation and variance).
	 * 
	 * @param queryID
	 *                   query id
	 * @param cond
	 *                   weighted query condition
	 */
	public void addMomentsCondition(String queryID, WeightedQueryCondition cond) {
		addMomentsCondition(queryID, cond.getPath(), cond.getOperator(), cond
				.getWeight(), cond.getValue());
	}

	/**
	 * Returns the expectation and the variance of the RSVs.
	 * 
	 * @param queryID
	 *                   query id
	 * @return moments
	 */
	public Moments getMoments(String queryID) {
		List ruleList = (List) rules.get(queryID);
		if (ruleList != null) {
			dummyIndex.computeMoments(queryID, ruleList);

			// get expectation and variance
			double exp = 0;
			double var = 0;
			for (Iterator it = dummyIndex.iterator(dummyIndex.getProbRelation(
					queryID, null)); it.hasNext();) {
				Fact fact = (Fact) it.next();
				String name = ((Constant) fact.getConst(0)).get();
				double prob = fact.getProb();
				if (name.equals(Index.EXPECTATION_RELATION))
					exp = prob;
				if (name.equals(Index.VARIANCE_RELATION))
					var = prob;
			}
			return new Moments(exp, var);
		}
		return null;
	}

	/**
	 * Return the name of this collection.
	 * 
	 * @return name of this collection
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * Returns a list of rules for the specified query id.
	 * 
	 * @param queryID
	 *                   query id
	 * @return list of temporary rules
	 */
	protected List getRuleList(String queryID) {
		List addList = (List) rules.get(queryID);
		if (addList == null) {
			addList = new LinkedList();
			rules.put(queryID, addList);
		}
		return addList;
	}

	/**
	 * Returns a list of conditions for the specified query id.
	 * 
	 * @param queryID
	 *                   query id
	 * @return list of temporary conditions
	 */
	protected List getConditionList(String queryID) {
		List addList = (List) conditions.get(queryID);
		if (addList == null) {
			addList = new LinkedList();
			conditions.put(queryID, addList);
		}
		return addList;
	}

}