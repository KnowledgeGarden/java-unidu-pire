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

// $Id: Index.java,v 1.12 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.pire;

import de.unidu.is.pdatalog.EDBRelation;
import de.unidu.is.pdatalog.IDBRelation;
import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.pdatalog.ds.Rule;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This interface encapsulates a document index, represented by specific
 * relations.
 * <p>
 * <p>
 * Standard relations:
 * <ul>
 * <li><code>docid</code> storing the ids of the documents,</li>
 * <li><code>tf</code> storing the indexing values ("tokens") of the
 * documents together with the number of their ocurrence in the documents,</li>
 * <li><code>df</df> storing the document frequencies of the index
 * tokens,</li>
 * <li><code>rd</code> storing values which are independent from the
 * document- and indexing tokens,</li>
 * <li><code>weight</code> is optional and contains the indexing weights,</li>
 * <li><code>expectation</code> storing the expectations of the index
 * tokens,</li>
 * <li><code>variance</code> storing the variances of the index
 * tokens,</li>
 * <li><code>result{query id}</code> contains the document RSVs.</li>
 * </ul>
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.12 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-08-14
 */
public interface Index {

    /**
     * Name of the docid relation.
     */
    String DOCID_RELATION = "docid";

    /**
     * Name of the token relation.
     */
    String TF_RELATION = "tf";

    /**
     * Name of the document frequency relation.
     */
    String DF_RELATION = "df";

    /**
     * Name of the rd relation.
     */
    String RD_RELATION = "rd";

    /**
     * Name of the IDB rd relation (values computed by rules).
     */
    String IDB_RD_RELATION = "idb_rd";

    /**
     * Name of the indexing weight relation.
     */
    String WEIGHT_RELATION = "weight";

    /**
     * Name of the expectation relation.
     */
    String EXPECTATION_RELATION = "expectation";

    /**
     * Name of the variance relation.
     */
    String VARIANCE_RELATION = "variance";

    /**
     * Prefix of the rsv relations.
     */
    String RSV_RELATION = "rsv";

    /**
     * Prefix of the result relations.
     */
    String PROB_RELATION = "prob";

    /*
     * Start: Low-level method
     */

    /**
     * Converts a relative relation name into an absolute one, using the
     * collection name and the index key as a prefix.
     *
     * @param relationName relative name of the relation
     * @return absolute name of the relation
     */
    String convert(String relationName);

    /**
     * Converts a relative relation name into an absolute one, using only the
     * collection name as a prefix.
     *
     * @param relationName relative name of the relation
     * @return absolute name of the relation
     */
    String convertCollection(String relationName);

    /*
     * Indexing
     */

    /**
     * Inits the index.
     */
    void init();

    /**
     * Inserts the document id to the corresponding table.
     *
     * @param docID document id
     */
    void insert(String docID);

    /**
     * Inserts the specified value into the given document.
     *
     * @param docID      document id
     * @param token      token
     * @param tokencount number of occurences of the value
     */
    void insert(String docID, String token, int tokencount);

    /**
     * Computes the moments of the indexing weights.
     */
    void computeMoments();

    /**
     * Removes the index.
     */
    void remove();

    /*
     * Retrieval
     */

    /**
     * Initialises the relation for the specified query.
     *
     * @param queryID query id
     */
    void initQuery(String queryID);

    /**
     * Add a rule with the given relations and a single argument to the
     * specified list.
     *
     * @param queryID   query id
     * @param relations array of relation names
     * @param prob      probability of the rule
     * @param addList   list to which the rule is added
     */
    void addRule(String queryID, String[] relations, double prob,
                 List addList);

    /**
     * Computes the probabilities of relevance for the given query based on the
     * RSVs specified by the list of rules.
     *
     * @param queryID         query id
     * @param addList         list of rules for computing the probabilities of relevance
     * @param computeDisjoint if true, disjointness is assumed
     */
    void computeProbs(String queryID, List addList,
                      boolean computeDisjoint);

    /**
     * Returns the maximum RSV for the specified query.
     *
     * @param queryID     query id
     * @param conditionID condition identifier (in the relation name)
     * @return maximum RSV for the specified query
     */
    double getMaxRSV(String queryID, String conditionID);

    /**
     * Returns the maximum probability for the specified relation.
     *
     * @param relation relation name
     * @return maximum probability for the specified relation
     */
    double getMax(String relation);

    /**
     * Returns the document ids and probabilities of relevance of the first
     * documents.
     *
     * @param queryID query id
     * @param numDocs number of documents to be retrieved
     * @return list of ProbDoc instances
     */
    List getProbs(String queryID, int numDocs);

    /**
     * Finishes the processing of this query and frees used resources.
     *
     * @param queryID query id
     */
    void closeQuery(String queryID);

    /*
     * Computing moments for query
     */

    /**
     * Adds a query condition for computing moments. This method can apply the
     * condition directly (i.e., it computes the moments for that condition) or
     * store it for later reference in <code>computeMoments</code>.
     *
     * @param queryID query id
     * @param weight  condition weight
     * @param value   comparison value
     */
    void addMomentsCondition(String queryID, double weight,
                             Object value, List addList);

    /**
     * Finishes the computation of the moments.
     *
     * @param queryID query id
     */
    void computeMoments(String queryID, List ruleList);

    /*
     * Relation stuff.
     */

    /**
     * Returns the value corresponding to the specified key in the
     * <code>rd</code> relation.
     *
     * @param key value key
     * @return value
     */
    double getRD(String key);

    /**
     * Returns the value corresponding to the specified key in the
     * <code>rd</code> relation or a default value if no entry is found.
     *
     * @param key          value key
     * @param defaultValue default value used if no value is found
     * @return value for the specified key
     */
    double getRD(String key, double defaultValue);

    /**
     * Sets the value corresponding to the specified key in the <code>rd</code>
     * relation.
     *
     * @param key   value key
     * @param value value
     */
    void setRD(String key, double value);

    /**
     * Adds the fact to the knowledge base.
     *
     * @param fact fact to be added
     */
    void add(Fact fact);

    /**
     * Computes the result of the specified rule.
     *
     * @param rule single rule for target relation
     */
    void compute(Rule rule);

    /**
     * Computes the result of the specified rules (all corresponding to the
     * specified IDB relation).
     *
     * @param relation target relation
     * @param rules    collection of rules for target relation
     */
    void compute(IDBRelation relation, Collection rules);

    /**
     * Computes the result of the specified rules (all corresponding to the
     * specified IDB relation). The results from the single rules are considered
     * to be disjoint.
     *
     * @param relation target relation
     * @param rules    collection of rules for target relation
     */
    void computeDisjoint(IDBRelation relation, Collection rules);

    /**
     * Computes the result of the specified rules (all corresponding to the
     * specified IDB relation). The results from the single rules are considered
     * to be disjoint.
     *
     * @param relationName target relation name
     * @param arity        arity
     * @param rules        collection of rules for target relation
     */
    void computeDisjoint(String relationName, int arity, Collection rules);

    /**
     * Returns an iterator over the specified relation.
     *
     * @param relation relation name
     * @return iterator over the specified relation (Fact instances)
     */
    Iterator iterator(String relation);

    /**
     * Creates a new EDB relation.
     *
     * @param name   name of the relation
     * @param arity  arity of the relation
     * @param create if true, the relation is physically created
     * @return new EDB relation
     */
    EDBRelation addEDBRelation(String name, int arity, boolean create);

    /**
     * Creates a new IDB relation.
     *
     * @param name   name of the relation
     * @param arity  arity of the relation
     * @param create if true, the relation is physically created
     * @return new IDB relation
     */
    IDBRelation addIDBRelation(String name, int arity, boolean create);

    /**
     * Removes the specified relation from this index.
     *
     * @param name relation name
     */
    void removeRelation(String name);

    /**
     * Signals that the specified relation is complete.
     *
     * @param name relation name
     */
    void completeRelation(String name);

    /**
     * @param queryID
     * @param subqueryID
     */
    String getRSVRelation(String queryID, String subqueryID);

    /**
     * @param queryID
     * @param subqueryID
     */
    String getProbRelation(String queryID, String subqueryID);

}