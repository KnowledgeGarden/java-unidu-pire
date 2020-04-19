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


// $Id: SQL.java,v 1.5 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.sql;

import java.util.List;

/**
 * A class for storing abstract SQL "select" and "insert into" statements.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-10-08
 */
public class SQL {

    /**
     * The name of the table where values have to be inserted.
     */
    protected String insertTable;

    /**
     * A list of expressions, forming the select part.
     */
    protected List select;

    /**
     * A list of table names, or of strings of the form
     * <code>{tablename} {alias}</code>.
     */
    protected List from;

    /**
     * A list of expressions, forming the where part.
     */
    protected List where;

    /**
     * A list of group by expressions.
     */
    protected List group;

    /**
     * A maximum number of documents to be selected, or zero if there is
     * no limit part.
     */
    protected int limit;

    /**
     * A list of columns specifiying the order of the rows.
     */
    protected List order;

    /**
     * A flag specifying if the order is ascending (flag is false) or
     * descending (flag is true).
     */
    protected boolean orderDesc;

    /**
     * A flag specifying if a distinct clause has to be used in the select
     * part.
     */
    protected boolean isDistinct;

    /**
     * Returns the name of the name in which the selected data should be
     * inserted into.
     *
     * @return table name
     */
    public String getInsertTable() {
        return insertTable;
    }

    /**
     * Sets the name of the name in which the selected data should be
     * inserted into.
     *
     * @param name table name
     */
    public void setInsertTable(String name) {
        insertTable = name;
    }

    /**
     * Returns the list of select expression.
     *
     * @return list of select expressions
     */
    public List getSelect() {
        return select;
    }

    /**
     * Sets the list of select expressions.
     *
     * @param list list of select expressions
     */
    public void setSelect(List list) {
        select = list;
    }

    /**
     * Returns whether the rows have to be distinct.
     *
     * @return true iff the rows have to be distinct
     */
    public boolean isDistinct() {
        return isDistinct;
    }

    /**
     * Sets whether the rows have to be distinct.
     *
     * @param flag return true iff the rows have to be distinct
     */
    public void setDistinct(boolean flag) {
        isDistinct = flag;
    }

    /**
     * Returns the from list.
     *
     * @return from list
     */
    public List getFrom() {
        return from;
    }

    /**
     * Sets the from list.
     *
     * @param list from list
     */
    public void setFrom(List list) {
        from = list;
    }

    /**
     * Returns the list of where expressions.
     *
     * @return list of where expressions
     */
    public List getWhere() {
        return where;
    }

    /**
     * Sets the list of where expressions.
     *
     * @param list list of where expressions
     */
    public void setWhere(List list) {
        where = list;
    }

    /**
     * Returns the group list.
     *
     * @return group list
     */
    public List getGroup() {
        return group;
    }

    /**
     * Sets the group list
     *
     * @param list group list
     */
    public void setGroup(List list) {
        group = list;
    }

    /**
     * Returns the maximum number of documents to retrieve.
     *
     * @return maximum number of documents to retrieve, or zero if no limit
     * is given
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the maximum number of documents to retrieve.
     *
     * @param limit maximum number of documents to retrieve
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the order list.
     *
     * @return order list
     */
    public List getOrder() {
        return order;
    }

    /**
     * Sets the order list
     *
     * @param list order list
     */
    public void setOrder(List list) {
        order = list;
    }

    /**
     * Returns whether the order is descening, if ther is an order part.
     *
     * @return true iff the order is descening
     */
    public boolean isOrderDesc() {
        return orderDesc;
    }

    /**
     * Sets if it is descending or ascending.
     *
     * @param b if true, then the order is descending
     */
    public void setOrderDesc(boolean b) {
        orderDesc = b;
    }

    /**
     * Sets the order list and if it is descending or ascending.
     *
     * @param list      order list
     * @param orderDesc if true, then the order is descending
     */
    public void setOrder(List list, boolean orderDesc) {
        order = list;
        this.orderDesc = orderDesc;
    }

    /**
     * Returns a textual description of this object.
     *
     * @return textual description of this object
     */
    public String toString() {
        return "insert="
                + insertTable
                + ",select="
                + select
                + ",from="
                + from
                + ",where="
                + where
                + ",group="
                + group
                + ",limit="
                + limit
                + "order"
                + (orderDesc ? " desc" : "asc")
                + "="
                + order;
    }

}
