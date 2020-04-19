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


// $Id: DBColumnExpression.java,v 1.5 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.sql;

import de.unidu.is.expressions.AbstractExpression;

/**
 * An expression with encoding a table column, specified by the table and
 * the column name.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-10-21
 */
public class DBColumnExpression extends AbstractExpression {

    /**
     * The table name.
     */
    protected String table;

    /**
     * The column name.
     */
    protected String col;

    /**
     * Creates a new expression object.
     *
     * @param table table name
     * @param col   column name
     */
    public DBColumnExpression(String table, String col) {
        this.table = table;
        this.col = col;
    }

    /**
     * Returns the expression in infix notation, embedded in round brackets.
     *
     * @return expression as a string
     */
    public String toString() {
        return table + "." + col;
    }

    /**
     * Tests whether this object equals the specified one.
     *
     * @param o object to test
     * @return true iff both objects are the same
     */
    public boolean equals(Object o) {
        if (!(o instanceof DBColumnExpression))
            return false;
        return o.toString().equals(toString());
    }

    /**
     * Returns a hashcode for this object.
     *
     * @return hashcode for this object
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Returns the table name.
     *
     * @return table name
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the table name.
     *
     * @param string table name
     */
    public void setTable(String string) {
        table = string;
    }

    /**
     * Returns the column name.
     *
     * @return column name
     */
    public String getCol() {
        return col;
    }

    /**
     * Sets the column name.
     *
     * @param col column name
     */
    public void setCol(String col) {
        this.col = col;
    }

}
