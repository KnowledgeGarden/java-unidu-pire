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

// $Id: QueryCondition.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import de.unidu.is.util.EmptyIterator;
import de.unidu.is.util.StringUtilities;

import java.util.Iterator;

/**
 * A single query condition.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 * @since 2004-04-12
 */
public class QueryCondition implements QueryNode {

    /**
     * An XPath expression or an alias.
     */
    protected String path;

    /**
     * An operator used for retrieval.
     */
    protected String operator;

    /**
     * A comparison value.
     */
    protected String value;

    /**
     * Creates a new, empty object.
     */
    public QueryCondition() {
    }

    /**
     * Creates a new object by parsing the XIRQL condition string.
     *
     * @param condition XIRQL condition string
     */
    public QueryCondition(String condition) {
        parse(condition);
    }

    /**
     * Creates a new object.
     *
     * @param path     path
     * @param operator name
     * @param value    comparison value
     */
    public QueryCondition(String path, String operator, String value) {
        setPath(path);
        setOperator(operator);
        setValue(value);
    }

    /**
     * Fills this instance by parsing the XIRQL condition string.
     *
     * @param condition XIRQL condition string
     */
    protected void parse(String condition) {
        int h1 = condition.indexOf(' ');
        path = condition.substring(0, h1).trim();
        String pp = condition.substring(h1 + 1).trim();
        int h2 = pp.indexOf(' ');
        operator = StringUtilities.replace(pp.substring(0, h2).trim(), "$", "");
        value = pp.substring(h2).trim();
        value = value.substring(1, value.length() - 1);
        int h = operator.indexOf(':');
        if (h != -1) {
            path = operator.substring(0, h);
            operator = operator.substring(h + 1);
        }
    }

    /**
     * Returns the operator.
     *
     * @return operator.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the operator.
     *
     * @param operator operator to set.
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Returns the path.
     *
     * @return path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the value.
     *
     * @return value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof QueryCondition))
            return false;
        QueryCondition c = (QueryCondition) obj;
        return c.path.equals(path) && c.operator.equals(operator)
                && c.value.equals(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return path.hashCode() + operator.hashCode() + value.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (path.startsWith("/"))
            return path + " $" + operator + "$ \"" + value + "\"";
        return "/#PCDATA $" + path + ":" + operator + "$ \"" + value + "\"";
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#toPrefix()
     */
    public String toPrefix() {
        return toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#simplifiedNode()
     */
    public QueryNode simplifiedNode() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#toDF()
     */
    public QueryNode toDF() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#isDF()
     */
    public boolean isDF() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#iterator()
     */
    public Iterator iterator() {
        return new EmptyIterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#cloneNode()
     */
    public QueryNode cloneNode() {
        QueryCondition cond = new QueryCondition();
        cond.path = path;
        cond.operator = operator;
        cond.value = value;
        return cond;
    }
}