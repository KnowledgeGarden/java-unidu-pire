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


// $Id: NumberDT.java,v 1.12 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.retrieval.pire.dt;

import de.unidu.is.expressions.Expression;
import de.unidu.is.pdatalog.ds.Constant;
import de.unidu.is.pdatalog.ds.Literal;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.ds.Variable;
import de.unidu.is.retrieval.pire.Index;
import de.unidu.is.text.AbstractSingleItemFilter;
import de.unidu.is.text.Filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class for the IR datatype "number", containing the deterministic
 * operators "&lt;", "&lt;=", "&gt;", "&gt;=" and the vague operators
 * "~&lt;", "~&gt;" and "~=". The identity mapping function is used for
 * the deterministic operators, a logistic function for the vague ones.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.12 $, $Date: 2005/03/14 17:33:13 $
 * @since 2004-01-05
 */
public class NumberDT extends AbstractDT {

    /**
     * The index/query filter for all operators.
     */
    private static final Filter filter;

    static {
        filter = new AbstractSingleItemFilter(null) {
            public Object run(Object value) {
                return value;
            }
        };
    }

    /**
     * Returns a filter for converting a document value into
     * tokens/token frequency tuples.
     *
     * @param operator operator name
     * @return filter
     */
    protected Filter getFilter(String operator) {
        return filter;
    }

    /**
     * Returns a filter for converting a condition comparison value into
     * tokens/token frequency tuples.
     *
     * @param operator operator name
     * @return filter
     */
    protected Filter getQueryFilter(String operator) {
        return filter;
    }

    /**
     * Converts the operator name into an identifier.<p>
     * <p>
     * This implementation returns the operator name. Subclasses can override
     * this behaviour.
     *
     * @param operator operator name
     * @return identifier for operator
     */
    public String convertOperator(String operator) {
        String identifier = "";
        if (isVague(operator)) {
            identifier += "v";
            operator = operator.substring(1);
        }
        if (operator.equals("<"))
            identifier += "lt";
        if (operator.equals("<="))
            identifier += "le";
        if (operator.equals(">"))
            identifier += "gt";
        if (operator.equals(">="))
            identifier += "ge";
        if (operator.equals("="))
            identifier += "eq";
        if (identifier.length() == 0 || identifier.equals("v"))
            identifier = operator;
        return identifier;
    }

    /* (non-Javadoc)
     * @see de.unidu.is.pire.dt.DT#getIndexTokens(de.unidu.is.pire.index.Index, java.lang.String)
     */
    public Iterator getIndexTokens(Index index, String operator) {
        final int MIN = 0;
        final int MAX = 65535;
        final int DIFF = MAX - MIN + 1;
        final int MAX2 = (int) Math.pow(DIFF, 2);
        if (operator.equals("="))
            return super.getIndexTokens(index, operator);
        return new Iterator() {
            protected int i = MIN;

            public boolean hasNext() {
                return i <= MAX;
            }

            public Object next() {
                return String.valueOf(i++);
            }

            public void remove() {
            }
        };
    }

    /**
     * Tests whether the specified operator contains a "less than"
     * component.
     *
     * @param operator search operator
     * @return true iff the specified operator contains a "less than"
     * component
     */
    protected static boolean includesLess(String operator) {
        return operator.contains("<") || operator.contains("l");
    }

    /**
     * Tests whether the specified operator contains a "greater than"
     * component.
     *
     * @param operator search operator
     * @return true iff the specified operator contains a "greater
     * than" component
     */
    protected static boolean includesGreater(String operator) {
        return operator.contains(">") || operator.contains("g");
    }

    /**
     * Tests whether the specified operator contains a "equals"
     * component.
     *
     * @param operator search operator
     * @return true iff the specified operator contains a "equals" component
     */
    protected static boolean includesEqual(String operator) {
        return operator.contains("=") || operator.contains("e");
    }

    /**
     * Tests whether the specified operator is a vague operator.
     *
     * @param operator search operator
     * @return true iff the specified operator is a vague operator
     */
    protected static boolean isVague(String operator) {
        return operator.contains("~") || operator.contains("v");
    }

    /**
     * Tests whether the RSVs are already stored in the <code>weight</code>
     * table, or if they are computed from outside.
     *
     * @param operator search operator
     * @return true, if the RSVs are already stored
     */
    public boolean storedRSVs(String operator) {
        return operator.equals("=");
    }

    /**
     * Adds and evaluates a query condition.
     *
     * @param index      underlying index
     * @param queryID    query id
     * @param subqueryID subquery id
     * @param operator   search operator
     * @param weight     condition weight
     * @param value      comparison value
     * @param addList    list to where the condition has to be added
     */
    public void addRSVRules(
            Index index,
            String queryID,
            String subqueryID,
            String operator,
            double weight,
            Object value, List addList) {
        value = getQueryFilter(operator).apply(value).next();
        String headOperator = index.getRSVRelation(queryID, subqueryID);
        if (isVague(operator)) {
			/*
			double v = Double.parseDouble(value.toString());
			Operator diff =
				new Product(
					new Constant(includesGreater(operator) ? 1 : -1),
					new Fraction(
						new Difference(
							new Constant(v),
							new TokenGetAsNumber(Index.WEIGHT_RELATION)),
						new Constant(v)));
			if (includesEqual(operator)) // vague ==
				index.applyDefinition(
					new AddWeightForallDocs(
						queryID,
						weight,
						new Difference(
							new Constant(1),
							new Product(diff, diff)),
						null));
			else // vague < or vague >
				index.applyDefinition(
					new AddWeightForallDocs(
						queryID,
						weight,
						new Difference(new Constant(1), diff),
						null));*/
        } else {
            String pred = "";
            if (includesLess(operator))
                pred += "l";
            else if (includesGreater(operator))
                pred += "g";
            if (includesEqual(operator)) {
                if (pred.length() == 0)
                    pred = "eq";
                else
                    pred += "e";
            } else
                pred += "t";

            List body = new ArrayList(2);
            body.add(
                    new Literal(
                            index.convert(Index.WEIGHT_RELATION),
                            new Expression[]{new Variable("D"), new Variable("V")},
                            true));
            body.add(
                    new Literal(
                            pred,
                            new Expression[]{new Variable("V"), new Constant(value)},
                            true));
            Rule rule =
                    new Rule(
                            weight,
                            new Literal(
                                    headOperator,
                                    new Expression[]{new Variable("D")},
                                    true),
                            body);
            rule.setOptimizable(true);
            addList.add(rule);
        }
    }

    /**
     * Returns a template for computing probabilities of relevance.<p>
     * <p>
     * The template string is an expression which contains the key
     * <code>${PROB}</code>.
     *
     * @param index    underlying index
     * @param queryID  query id
     * @param operator operator name
     * @return template for computing probabilities of relevance
     */
    public String getProbsTemplate(
            Index index,
            String queryID,
            String subqueryID,
            String operator) {
        if (isVague(operator)) { // logistic function
            double b0;
            double b1;
            if (includesEqual(operator)) {
                b0 = index.getRD("b0", 4.7054);
                b1 = index.getRD("b2", 12590100);
            } else {
                b0 = index.getRD("b0", 4.79401);
                b1 = index.getRD("b1", 5557.9);
            }
            b0 -= b1;
            String part = "&exp(" + b0 + "+" + "(" + b1 + "*PROB)";
            return "(" + part + "/(1+" + part + "))";
        }
        return "(PROB)"; // identity function
    }

}
