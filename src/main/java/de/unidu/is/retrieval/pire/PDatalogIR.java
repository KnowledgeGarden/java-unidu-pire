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

// $Id: PDatalogIR.java,v 1.20 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.pire;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import de.unidu.is.retrieval.AbstractRetriever;
import de.unidu.is.retrieval.AndQueryNode;
import de.unidu.is.retrieval.DocumentMismatchException;
import de.unidu.is.retrieval.DocumentNotFoundException;
import de.unidu.is.retrieval.DocumentNotStorableException;
import de.unidu.is.retrieval.IndexException;
import de.unidu.is.retrieval.MomentsIR;
import de.unidu.is.retrieval.OrQueryNode;
import de.unidu.is.retrieval.ProbDoc;
import de.unidu.is.retrieval.Query;
import de.unidu.is.retrieval.QueryCondition;
import de.unidu.is.retrieval.QueryNode;
import de.unidu.is.retrieval.Schema;
import de.unidu.is.retrieval.SchemaElement;
import de.unidu.is.retrieval.StructuredQuery;
import de.unidu.is.retrieval.UnsupportedQueryException;
import de.unidu.is.retrieval.WSumQuery;
import de.unidu.is.retrieval.WeightedQueryCondition;
import de.unidu.is.retrieval.hyrex.HyREXSchema;
import de.unidu.is.statistics.Moments;
import de.unidu.is.util.DB;
import de.unidu.is.util.XMLUtilities;

/**
 * An IR interface (for XML documents) to PIRE (using probabilistic Datalog).
 * <p>
 * 
 * PIRE only deals with flat documents, but that is sufficient for the IR
 * specification. Datatype and operator names are automatically converted into
 * the PIRE versions.
 * <p>
 * 
 * Currently, only <code>wsum</code> queries are supported.
 * 
 * @author Henrik Nottelmann
 * @since 2004-01-03
 * @version $Revision: 1.20 $, $Date: 2005/03/14 17:33:14 $
 */
public class PDatalogIR extends AbstractRetriever implements MomentsIR {

	/**
	 * The PIRE instance used by this IR implementation.
	 */
	private PIRE ir;

	/**
	 * The schema used by this IR implementation.
	 */
	private Schema schema;

	/**
	 * The directory in which documents will be saved.
	 */
	private String docDir;

	/**
	 * Creates a new instance, which does not store added documents on disc.
	 * 
	 * @param db
	 *                   database
	 * @param collectionName
	 *                   name of the collection
	 */
	public PDatalogIR(DB db, String collectionName) {
		this(db, collectionName, null);
	}

	/**
	 * Creates a new instance, which stores added documents on disc.
	 * 
	 * @param db
	 *                   database
	 * @param collectionName
	 *                   name of the collection
	 * @param docDir
	 *                   directory for added documents
	 */
	public PDatalogIR(DB db, String collectionName, String docDir) {
		ir = new PIRE(db, collectionName);
		this.docDir = docDir;
	}

	/**
	 * Registers the specified schema.
	 * 
	 * @param schema
	 *                   collection schema
	 * @see de.unidu.is.retrieval.IR#registerSchema(de.unidu.is.retrieval.Schema)
	 */
	public void registerSchema(Schema schema) {
		this.schema = schema;
		schema.addAliases();
		for (Iterator it = schema.getAliases().iterator(); it.hasNext();) {
			String alias = (String) it.next();
			//one path is enough
			String xpath = schema.getXPath(alias);
			SchemaElement element = schema.getElement(xpath);
			// convert datatype name
			String datatypeName = convertDatatypeName(element.getDatatypeName());
			// convert operator names
			List originalOperators = element.getOperators();
			List operators = new ArrayList();
			for (Iterator it2 = originalOperators.iterator(); it2.hasNext();) {
				String operator = (String) it2.next();
				operators.add(convertOperatorName(operator));
			}
			// add attribute
			if (operators.size() > 0)
				ir.registerAttribute(alias, datatypeName, operators);
		}
	}

	/**
	 * Converts a XPath path by removing the first element name and "/text()",
	 * transforming "/" into "__", "-" into "_" and "@" into "at". If there is
	 * an alias for the XPath, that alias is returned instead.
	 * 
	 * @param path
	 *                   XPath
	 * @return converted alias or path
	 */
	private String convPath(String path) {
		String alias = schema.getAlias(path);
		if (alias != null)
			return alias;
		return convPath(path);
	}

	/**
	 * Converts the datatype name to the PIRE notions.
	 * 
	 * @param datatypeName
	 *                   name of the datatype
	 * @return converted datatype name
	 */
	private String convertDatatypeName(String datatypeName) {
		return HyREXSchema.convertDatatypeName(datatypeName);
	}

	/**
	 * Converts the operator name to the PIRE notions.
	 * 
	 * @param operator
	 *                   name of the operator
	 * @return converted operator name
	 */
	private String convertOperatorName(String operator) {
		return HyREXSchema.convertOperatorName(operator);
	}

	/**
	 * Inits the index.
	 * 
	 * @see de.unidu.is.retrieval.IR#initIndex()
	 */
	public void initIndex() throws IndexException {
		try {
			ir.initIndex();
		} catch (Exception e) {
			throw new IndexException("Unable to initialize the index", e);
		}
	}

	/**
	 * Add the XML document to the index.
	 * <p>
	 * 
	 * If a directory was specified in the constructor, then the document is
	 * also stored in that directory.
	 * 
	 * @param docID
	 *                   document id
	 * @param document
	 *                   XML document
	 * @see de.unidu.is.retrieval.IR#addToIndex(java.lang.String,
	 *           org.w3c.dom.Document)
	 */
	public void addToIndex(String docID, Document document)
			throws DocumentMismatchException, DocumentNotStorableException {
		if (document == null)
			throw new DocumentMismatchException("Document is not XML");
		ir.addToIndex(docID);

		// extract all attributes
		for (Iterator it = schema.getAliases().iterator(); it.hasNext();) {
			String attName = (String) it.next();
			List l = schema.getXPathsForAlias(attName);
			if (l != null) {
				StringBuffer buf = new StringBuffer(1000);
				for (Iterator iter = l.iterator(); iter.hasNext();) {
					String xpath = (String) iter.next();
					try {
						NodeIterator nodeIt = XPathAPI.selectNodeIterator(
								document, xpath);
						for (Node node; (node = nodeIt.nextNode()) != null;) {
							String value = node.getNodeValue();
							buf.append(value).append(" ");
						}
					} catch (TransformerException e) {
						throw new DocumentMismatchException(
								"Document and XPath expressions in schema do not match");
					}
				} // for(String xpath:l)
				ir.addToIndex(docID, attName, buf.toString());
			} // if
		}

		// save document
		if (docDir != null) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileWriter(new File(docDir, docID
						+ ".xml")));
				pw.print(XMLUtilities.toString(document));
			} catch (IOException e) {
				throw new DocumentNotStorableException(e.getMessage());
			} finally {
				if (pw != null)
					pw.close();
			}
		}
	}

	/**
	 * Add the XML document to the index.
	 * <p>
	 * 
	 * If a directory was specified in the constructor, then the document is
	 * also stored in that directory.
	 * 
	 * @param docID
	 *                   document id
	 * @param document
	 *                   XML document
	 * @see de.unidu.is.retrieval.IR#addToIndex(java.lang.String,
	 *           java.lang.String)
	 */
	public void addToIndex(String docID, String document)
			throws DocumentMismatchException, DocumentNotStorableException {
		addToIndex(docID, XMLUtilities.parseText(document));
	}

	/**
	 * Computes the index, based on the document values which were added before.
	 * 
	 * @see de.unidu.is.retrieval.IR#computeIndex()
	 */
	public void computeIndex() throws IndexException {
		try {
			ir.computeIndex();
		} catch (Exception e) {
			throw new IndexException("Unable to compute the index", e);
		}
	}

	/**
	 * Computes the moments of the indexing weights.
	 */
	public void computeMoments() throws IndexException {
		try {
			ir.computeMoments();
		} catch (Exception e) {
			throw new IndexException(
					"Unable to compute the moments in the index", e);
		}
	}

	/**
	 * Removes the index.
	 */
	public void removeIndex() throws IndexException {
		try {
			ir.removeIndex();
		} catch (Exception e) {
			throw new IndexException(
					"Unable to compute the moments in the index", e);
		}
	}

	/**
	 * Returns result for the specified query.
	 * <p>
	 * 
	 * Currently, only <code>wsum</code> queries are supported.
	 * 
	 * @param query
	 *                   XIRQL query
	 * @return list of ProbDoc instances
	 * @see de.unidu.is.retrieval.IR#getResult(de.unidu.is.retrieval.Query)
	 */
	public List getResult(Query query) throws UnsupportedQueryException {
		String QUERYID = query.getQueryID();
		ir.initQuery(QUERYID);
		if (query instanceof StructuredQuery) {
			QueryNode node = ((StructuredQuery) query).getNode()
					.simplifiedNode().toDF().simplifiedNode();
			if (node instanceof OrQueryNode) {
				for (Iterator iter = node.iterator(); iter.hasNext();) {
					QueryNode n = (QueryNode) iter.next();
					ir.addConjunction(QUERYID, convNode(n));
				}
			} else
				// single rule
				ir.addConjunction(QUERYID, convNode(node));
		} else if (query instanceof WSumQuery) {
			WSumQuery wsum = (WSumQuery) query;
			for (Iterator it = wsum.getConditionsTuples().iterator(); it
					.hasNext();) {
				WeightedQueryCondition cond = (WeightedQueryCondition) it
						.next();
				ir.addCondition(QUERYID, convPath(cond.getPath()),
						convertOperatorName(cond.getOperator()), cond
								.getWeight(), cond.getValue());
			}
		} else
			throw new UnsupportedQueryException(query.toString());
		ir.computeProbs(QUERYID);
		List result = ir.getResult(QUERYID, query.getNumDocs());
		ir.closeQuery(QUERYID);
		return result;
	}

	/**
	 * Converts a query node (an AndQueryNode or a query condition) into an
	 * array of query conditions.
	 * 
	 * @param node
	 *                   query node
	 * @return all query conditions under the query node
	 */
	private QueryCondition[] convNode(QueryNode node) {
		QueryCondition[] conds = null;
		if (node instanceof AndQueryNode) {
			AndQueryNode aqn = (AndQueryNode) node;
			conds = new QueryCondition[aqn.getChildren().size()];
			int i = 0;
			for (Iterator iter = aqn.iterator(); iter.hasNext(); i++) {
				conds[i] = convCondition((QueryCondition) iter.next());
			}
		} else
			conds = new QueryCondition[]{convCondition((QueryCondition) node)};
		return conds;
	}

	/**
	 * Converts a single query condition (path, operator name).
	 * 
	 * @param condition
	 *                   query condition
	 * @return converted query condition
	 */
	private QueryCondition convCondition(QueryCondition condition) {
		return new QueryCondition(convPath(condition.getPath()),
				convertOperatorName(condition.getOperator()), condition
						.getValue());
	}

	/**
	 * Returns the full XML document
	 * 
	 * @param docID
	 *                   document id
	 * @return full XML document, or null if it does not exist
	 * @see de.unidu.is.retrieval.IR#getDocument(java.lang.String)
	 */
	public Document getDocument(String docID) throws DocumentNotFoundException {
		Document doc = null;
		File docFile = new File(docDir, docID + ".xml");
		if (!docFile.exists())
			docFile = new File(docDir, docID);
		if (docDir != null)
			doc = XMLUtilities.parse(docFile);
		if (doc == null)
			throw new DocumentNotFoundException(docFile.toString());
		return doc;
	}

	/**
	 * Returns the full XML document
	 * 
	 * @param doc
	 *                   document descriptor
	 * @return full XML document, or null if it does not exist
	 * @see de.unidu.is.retrieval.IR#getDocument(de.unidu.is.retrieval.ProbDoc)
	 */
	public Document getDocument(ProbDoc doc) throws DocumentNotFoundException {
		return getDocument(doc.getDocID());
	}

	/**
	 * Returns the expectation and the variance of the RSVs for the specified
	 * query.
	 * <p>
	 * 
	 * Currently, only <code>wsum</code> queries are supported.
	 * 
	 * @param query
	 *                   XIRQL query
	 * @return moments (expectation and variance) or null, if not supported
	 */
	public Moments getMoments(Query query) throws UnsupportedQueryException {
		String QUERYID = query.getQueryID();
		ir.initQuery(QUERYID);
		WSumQuery wsum = null;
		try {
			wsum = (WSumQuery) query;
		} catch (ClassCastException ex) {
			throw new UnsupportedQueryException(query.toString());
		}
		for (Iterator it = wsum.getConditionsTuples().iterator(); it.hasNext();) {
			WeightedQueryCondition cond = (WeightedQueryCondition) it.next();
			ir.addCondition(QUERYID, convPath(schema.getXPath(cond.getPath())),
					convertOperatorName(cond.getOperator()), cond.getWeight(),
					cond.getValue());
		}
		ir.computeProbs(QUERYID);
		Moments moments = ir.getMoments(QUERYID);
		ir.closeQuery(QUERYID);
		return moments;
	}

	/**
	 * Returns the value corresponding to the specified key in the resource
	 * description (the <code>rd</code> relation), e.g. for defining the
	 * parameters of the mapping function.
	 * 
	 * @param attName
	 *                   attribute name
	 * @param operator
	 *                   operator name
	 * @param key
	 *                   value key
	 * @return value
	 */
	public double getRD(String attName, String operator, String key) {
		return ir.getRD(attName, convertOperatorName(operator), key);
	}

	/**
	 * Sets the value corresponding to the specified key in the resource
	 * description (the <code>rd</code> relation), e.g. for defining the
	 * parameters of the mapping function.
	 * 
	 * @param attName
	 *                   attribute name
	 * @param operator
	 *                   operator name
	 * @param key
	 *                   value key
	 * @param value
	 *                   value
	 */
	public void setRD(String attName, String operator, String key, double value) {
		ir.setRD(attName, convertOperatorName(operator), key, value);
	}

	/**
	 * Returns the PIRE object used internally.
	 *  
	 */
	public PIRE getPIRE() {
		return ir;
	}

	/**
	 * Closes the retriever, and optionally frees used system resources (e.g.
	 * closes a network connection).
	 * 
	 * @see de.unidu.is.retrieval.Retriever#close()
	 */
	public void close() {
		// TODO: do anything here?
	}

}