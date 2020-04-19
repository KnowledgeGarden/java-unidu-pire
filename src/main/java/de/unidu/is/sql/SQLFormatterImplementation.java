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

// $Id: SQLFormatterImplementation.java,v 1.9 2005/03/18 22:01:19 nottelma Exp $
package de.unidu.is.sql;

import de.unidu.is.expressions.Expression;
import de.unidu.is.util.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of a formatter for SQL statements.
 * <p>
 * <p>
 * A formatter converts an abstract SQL statement into an SQL string suitable
 * for a specific database. This allows for bridging the subtle differences in
 * the SQL syntax of the different relational database management systems.
 * <p>
 * <p>
 * This implemenation uses a configuration file, located in the directory
 * <code>conf/db</code> relatively to the CLASSPATH. The name is the part of
 * the JDBC URI between the first and the second colon.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.9 $, $Date: 2005/03/18 22:01:19 $
 * @since 2003-10-08
 */
public class SQLFormatterImplementation implements SQLFormatter {

    /**
     * The DB parameters.
     */
    protected DB db;

    /**
     * Property map for formatting SQL statements.
     */
    protected final StreamPropertyMap config;

    /**
     * Creates a new formatter.
     *
     * @param db DB parameters
     */
    protected SQLFormatterImplementation(final DB db) {
        this.db = db;
        String uri = db.getDbURI();
        int h1 = uri.indexOf(':');
        int h2 = uri.indexOf(':', h1 + 1);
        config = new FilePropertyMap(new URL[]{SystemUtilities.getResourceURL("db/"
                + uri.substring(h1 + 1, h2) + ".conf")});
        System.out.println("SQLFomatterImpl " + uri + " | " + config + " | " + uri.substring(h1 + 1, h2));
    }

    /**
     * Creates a table.
     *
     * @param tableName name of the table
     * @param cols      names of the columns
     * @param types     types of the columns, can be subject to substitution
     */
    public void create(String tableName, String[] cols, String[] types) {
        PropertyMap map = new HashPropertyMap();
        List l = new ArrayList();
        for (int i = 0; i < cols.length; i++) {
            l.add(cols[i] + " " + types[i]);
        }
        map.setString("table.create.table", tableName);
        map.setString("table.create.args", StringUtilities.implode(l, ","));
        remove(tableName);
        db.executeUpdate(config.getString("table.create", map));
    }

    /**
     * Clear the specified tables, which means deletes all rows.
     *
     * @param tableName name of the table
     */
    public void clear(String tableName) {
        PropertyMap map = new HashPropertyMap();
        map.setString("table.clear.table", tableName);
        db.executeUpdate(config.getString("table.clear", map));
    }

    /**
     * Transforms the specified abstract SQL insert statement into a concrete
     * SQL statement for this DB, and performs it.
     *
     * @param sql             abstract SQL statement
     * @param resultTableName name of the result table (if <code>null</code>, the insert
     *                        name from the SQL statement will be used)
     */
    public void perform(SQL sql, String resultTableName) {
        PropertyMap map = new HashPropertyMap();
        map.setString("table.insert.table", resultTableName == null ? sql
                .getInsertTable() : resultTableName);
        map.setString("table.insert.select", getSelect(sql));
        db.executeUpdate(config.getString("table.insert", map));
    }

    /**
     * Transforms the specified abstract SQL select statement into a concrete
     * SQL statement for this DB, performs it, and returns a corresponding
     * <code>ResultSet</code>.
     *
     * @param sql abstract SQL statement
     * @return result set
     */
    public ResultSet performQuery(SQL sql) throws SQLException {
        return db.executeQuery(getSelect(sql));
    }

    /**
     * Closes the specified <code>ResultSet</code>.
     *
     * @param rs result set
     */
    public void close(ResultSet rs) {
        db.close(rs);
    }

    /**
     * Returns a concrete select statement for this DB, obtained from the
     * specified abstract SQL select statement.
     *
     * @param sql abstract SQL statement
     * @return concrete select statement for this DB
     */
    public String getSelect(SQL sql) {
        PropertyMap map = new HashPropertyMap();
        String command;
        if (sql.getFrom() != null && sql.getFrom().size() > 0) {
            command = "table.select.select";
            List l = new ArrayList();
            if (sql.getSelect() != null)
                for (Object o : sql.getSelect()) {
                    Expression arg = (Expression) o;
                    l.add(arg.getSQLTemplate());
                }
            if (sql.isDistinct())
                map.setString("distinct", "${table.select.distinct}");
            map.setString("table.select.from.option", "${table.select.from}");
            //command += "${table.select.select} ${table.select.from}";
            map.setString("select", StringUtilities.implode(l, ","));
            map.setString("from", StringUtilities.implode(sql.getFrom(), ","));
            if (sql.getWhere() != null && sql.getWhere().size() > 0) {
                //command += " ${table.select.where}";
                map.setString("table.select.where.option",
                        "${table.select.where}");
                List where = new ArrayList();
                for (Object o : sql.getWhere()) {
                    Expression arg = (Expression) o;
                    where.add(arg.getSQLTemplate());
                }
                map.setString("where", StringUtilities.implode(where,
                        " ${table.select.where.and} "));
            }
            if (sql.getGroup() != null && sql.getGroup().size() > 0) {
                map.setString("table.select.group.option",
                        "${table.select.group}");
                map.setString("group", StringUtilities.implode(sql.getGroup(),
                        ","));
            }
            if (sql.getOrder() != null && sql.getOrder().size() > 0) {
                map.setString("table.select.order.option",
                        "${table.select.order}");
                map.setString("order", StringUtilities.implode(sql.getOrder(),
                        ","));
                if (sql.isOrderDesc())
                    map.setString("table.select.order.desc.option",
                            "${table.select.order.desc}");

            }
            if (sql.getLimit() > 0) {
                map.setString("table.select.limit.option",
                        "${table.select.limit}");
                map.setInt("limit", sql.getLimit());
            }
        } else {
            command = "table.select.values";
            map.setString("table.select.values.option",
                    "${table.select.values}");
            List l = new ArrayList();
            if (sql.getSelect() != null)
                for (Object o : sql.getSelect()) {
                    Expression arg = (Expression) o;
                    l.add(arg.getSQLTemplate());
                }
            map.setString("values", StringUtilities.implode(l, ","));
        }
        command = config.convertString("${" + command + "}", map);
        return command;
    }

    /**
     * Removes the specified table.
     *
     * @param tableName name of the table to remove
     */
    public void remove(String tableName) {
        PropertyMap map = new HashPropertyMap();
        map.set("table.remove.table", tableName);
        String str = config.getString("table.remove", map);
        db.executeUpdate(str);
    }

    /**
     * Adds an index to the specified table.
     *
     * @param tableName name of the table
     * @param indexName name of the new index
     * @param cols      imvolved columns
     * @param textCols  flag indicating that the column is a text column
     */
    public void addIndex(String tableName, String indexName, String[] cols,
                         boolean[] textCols) {
        PropertyMap map = new HashPropertyMap();
        map.set("table.dropindex.table", tableName);
        map.set("table.dropindex.index", tableName + "_" + indexName);
        // TODO: not nice, but necessary
        db.executeUpdateIgnoreError(config.getString("table.dropindex", map));

        map.clear();
        map.set("table.addindex.table", tableName);
        map.set("table.addindex.index", tableName + "_" + indexName);
        List list = new ArrayList();
        for (int i = 0; i < cols.length; i++)
            list.add(cols[i]
                    + (textCols[i] ? config
                    .getString("table.addindex.textcolsuffix") : ""));
        map
                .set("table.addindex.definition", StringUtilities.implode(list,
                        ","));
        String str = config.getString("table.addindex", map);
        db.executeUpdate(str);
    }

    /**
     * Returns the DB parameters.
     *
     * @return DB parameters
     */
    public DB getDB() {
        return db;
    }

    /**
     * Sets the DB parameters.
     *
     * @param db DB parameters
     */
    public void setDB(DB db) {
        this.db = db;
    }

}