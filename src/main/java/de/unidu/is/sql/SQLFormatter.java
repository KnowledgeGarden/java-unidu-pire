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


// $Id: SQLFormatter.java,v 1.6 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.sql;

import de.unidu.is.util.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface of a formatter for SQL statements.<p>
 * <p>
 * A formatter converts an abstract SQL statement into an SQL string
 * suitable for a specific database. This allows for bridging the subtle
 * differences in the SQL syntax of the different relational database
 * management systems.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-11-25
 */
public interface SQLFormatter {

    /**
     * Creates a table.
     *
     * @param tableName name of the table
     * @param cols      names of the columns
     * @param types     types of the columns, can be subject to substitution
     */
    void create(String tableName, String[] cols, String[] types);

    /**
     * Clear the specified tables, which means deletes all rows.
     *
     * @param tableName name of the table
     */
    void clear(String tableName);

    /**
     * Transforms the specified abstract SQL insert statement into a concrete
     * SQL statement for this DB, and performs it.
     *
     * @param sql             abstract SQL statement
     * @param resultTableName name of the result table (if <code>null</code>,
     *                        the insert name from the SQL statement will
     *                        be used)
     */
    void perform(SQL sql, String resultTableName);

    /**
     * Transforms the specified abstract SQL select statement into a concrete
     * SQL statement for this DB, performs it, and returns a corresponding
     * <code>ResultSet</code>.
     *
     * @param sql abstract SQL statement
     * @return result set
     */
    ResultSet performQuery(SQL sql) throws SQLException;

    /**
     * Closes the specified <code>ResultSet</code>.
     *
     * @param rs result set
     */
    void close(ResultSet rs);

    /**
     * Returns a concrete select statement for this DB, obtained from the
     * specified abstract SQL select statement.
     *
     * @param sql abstract SQL statement
     * @return concrete select statement for this DB
     */
    String getSelect(SQL sql);

    /**
     * Removes the specified table.
     *
     * @param tableName name of the table to remove
     */
    void remove(String tableName);

    /**
     * Adds an index to the specified table.
     *
     * @param tableName name of the table
     * @param indexName name of the new index
     * @param cols      imvolved columns
     * @param textCols  flag indicating that the column is a text column
     */
    void addIndex(
            String tableName,
            String indexName,
            String[] cols,
            boolean[] textCols);

    /**
     * Returns the DB parameters.
     *
     * @return DB parameters
     */
    DB getDB();

    /**
     * Sets the DB parameters.
     *
     * @param db DB parameters
     */
    void setDB(DB db);

}
