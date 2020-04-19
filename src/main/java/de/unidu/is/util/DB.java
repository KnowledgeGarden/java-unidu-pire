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

// $Id: DB.java,v 1.29 2005/03/18 22:02:04 nottelma Exp $
package de.unidu.is.util;

import de.unidu.is.sql.SQLFormatter;
import de.unidu.is.sql.SQLFormatterFactory;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class encapsulates a RDBMS (using JDBC), where connections are managed
 * by a connection pool.
 * <p>
 * <p>
 * This class uses the logger "unidu.db".
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.29 $, $Date: 2005/03/18 22:02:04 $
 * @since 2003-06-01
 */
public class DB {

    /**
     * Logger name.
     */
    private static final String DEFAULT_LOGGERNAME = "unidu.db";
    /**
     * A map of data sources, one for each database URL.
     */
    protected static final Map<String, PoolingDataSource> dataSources = new ConcurrentHashMap();

    /**
     * Logger.
     */
    protected final Logger logger;
    /**
     * Mapping from result sets to their connections, used for freeing them.
     */
    protected final Map result2conn;
    /**
     * Mapping from result sets to their statements, used for closing them.
     */
    protected final Map result2stmt;
    /**
     * The database URI.
     */
    protected final String dbURI;
    /**
     * The database user.
     */
    protected final String dbUser;
    /**
     * The data source of this specific database.
     */
    protected final DataSource dataSource;
    /**
     * SQL formatter.
     */
    protected final SQLFormatter formatter;

    /**
     * Creates a new instance.
     *
     * @param uri      RDBMS URI
     * @param driver   class name of JDBC driver
     * @param user     user name
     * @param password user password
     */
    public DB(String uri, String driver, String user, String password) {
        this(uri, driver, user, password, DEFAULT_LOGGERNAME);
    }

    /**
     * Creates a new instance.
     *
     * @param uri      RDBMS URI
     * @param driver   class name of JDBC driver
     * @param user     user name
     * @param password user password
     * @param logger   DB logger
     */
    public DB(String uri, String driver, String user, String password,
              Logger logger) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            de.unidu.is.util.Log.error(e);
        }
        this.dbURI = uri;
        this.dbUser = user;
        this.logger = logger;
        result2conn = new ConcurrentHashMap();
        result2stmt = new ConcurrentHashMap();

        dataSource = getDataSource(uri, driver, user, password);

        formatter = SQLFormatterFactory.newFormatter(this);
    }

    /**
     * Creates a new instance, where the parameters are taken from a map. The
     * prefix of the parameters can be chosen, the suffix are ".uri", ".driver",
     * ".user", ".password" and ".log".
     *
     * @param map    map containing the parameters
     * @param prefix parameter prefix
     */
    public DB(PropertyMap map, String prefix) {
        this(map, prefix, DEFAULT_LOGGERNAME);
    }

    /**
     * Creates a new instance, where the parameters are taken from a map. The
     * prefix of the parameters can be chosen, the suffix are ".uri", ".driver",
     * ".user", ".password" and ".log".
     *
     * @param map        map containing the parameters
     * @param prefix     parameter prefix
     * @param loggerName name of DB logger
     */
    public DB(PropertyMap map, String prefix, String loggerName) {
        this(map, prefix, Log.getLogger(loggerName));
    }

    /**
     * Creates a new instance, where the parameters are taken from a map. The
     * prefix of the parameters can be chosen, the suffix are ".uri", ".driver",
     * ".user", ".password" and ".log".
     *
     * @param map    map containing the parameters
     * @param prefix parameter prefix
     * @param logger DB logger
     */
    public DB(PropertyMap map, String prefix, Logger logger) {
        this(map.getString(prefix + ".uri"), map.getString(prefix + ".driver"),
                map.getString(prefix + ".user"), map.getString(prefix
                        + ".password"), logger);
    }

    /**
     * Creates a new instance.
     *
     * @param uri      RDBMS URI
     * @param driver   class name of JDBC driver
     * @param user     user name
     * @param password user password
     * @param logfile  ignored parameter
     * @param logger   DB logger
     * @deprecated
     */
    public DB(String uri, String driver, String user, String password,
              String logfile, Logger logger) {
        this(uri, driver, user, password, logger);
    }

    /**
     * Creates a new instance.
     *
     * @param uri      RDBMS URI
     * @param driver   class name of JDBC driver
     * @param user     user name
     * @param password user password
     * @param logfile  ignored parameter
     * @deprecated
     */
    public DB(String uri, String driver, String user, String password,
              String logfile) {
        this(uri, driver, user, password, logfile, DEFAULT_LOGGERNAME);
    }

    /**
     * Creates a new instance.
     *
     * @param uri        RDBMS URI
     * @param driver     class name of JDBC driver
     * @param user       user name
     * @param password   user password
     * @param logfile    ignored parameter
     * @param loggerName name of DB logger
     * @deprecated
     */
    public DB(String uri, String driver, String user, String password,
              String logfile, String loggerName) {
        this(uri, driver, user, password, Log.getLogger(loggerName));
    }

    /**
     * @param uri
     * @param driver
     * @param user
     * @param password
     * @return @throws
     * IOException
     */
    private static DataSource getDataSource(String uri, String driver, String user,
                                            String password) {
        return dataSources.computeIfAbsent(uri, u-> {
            ObjectPool connectionPool = new GenericObjectPool(null);
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                    u, user, password);
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                    connectionFactory, connectionPool, null, null, false, true);
            return new PoolingDataSource(connectionPool);
        });
    }

    /**
     * Returns a connection for this instance out of the connection pool.
     *
     * @return connection
     */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            de.unidu.is.util.Log.error(e);
        }
        return null;
    }

    /**
     * Frees a connection i.e. puts it back to the connection pool.
     *
     * @param conn connection
     */
    protected static void freeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Converts a specified string into an identifier useful for databases.
     * <p>
     * <p>
     * The identifier is the specified string, where dots are replaced by
     * underscores, and only letters (lowercase), digits and underscores are
     * used (all other characters are removed).
     *
     * @param str string to convert
     * @return converted string (identifier)
     */
    public static String makeIdentifier(String str) {
        StringBuilder ret = new StringBuilder();
        String p = str.toLowerCase();
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '.')
                c = '_';
            if (Character.isUnicodeIdentifierStart(c) || Character.isDigit(c)
                    || (c == '_'))
                ret.append(c);
        }
        return ret.toString();
    }

    /**
     * Executes a SQL query and returns a <code>java.sql.ResultSet</code>.
     * <p>
     * <p>
     * The corresponding statement is stored s.th. it can be closed lateron.
     *
     * @param sql SQL query
     * @return result set
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return executeQuery(sql, false);
    }

    /**
     * Executes a SQL query and returns a <code>java.sql.ResultSet</code>.
     * <p>
     * <p>
     * The corresponding statement is stored s.th. it can be closed lateron.
     *
     * @param sql  SQL query
     * @param info flag indicating whether logging as info is wanted
     * @return result set
     */
    public ResultSet executeQuery(String sql, boolean info) throws SQLException {
        logSQL(sql, info);
        int h = sql.indexOf("select ");
        if (h != -1)
            printExplain(sql.substring(h).trim());
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            result2stmt.put(result, stmt);
            result2conn.put(result, conn);
            return result;
        } catch (SQLException ex) {
            if (logger != null)
                logger.error(sql, ex);
            freeConnection(conn);
            throw ex;
        }
    }

    /**
     * Inserts elements into table if a certain field values doesn't exist. This
     * means: The method checks if the fieldsToCheck and its value contained in
     * the map elements exists in the table, If it already exists, nothing is
     * inserted. If it doesn't exist, the elements given in the map are inserted
     * into the table. An example application of this method would be to check
     * if a certain primary key element was inserted before into the table.
     *
     * @param table        the table name
     * @param elements     the elements to be inserted
     * @param fieldToCheck the field to be checked first. The corresponding value is
     *                     taken from the elements map
     * @param info         flag indicating whether logging as info is wanted
     * @return the number of changed rows, or -1 if an error occurs
     */
    public int insertIfMissing(String table, Map elements, String fieldToCheck,
                               boolean info) {
        Vector fields = new Vector();
        fields.add(fieldToCheck);
        return insertIfMissing(table, elements, fields, info);
    }

    /**
     * Inserts elements into table if certain field values don't exist. This
     * means: the vector fieldsToCheck contains all fields which have to be
     * checked first. The method checks if these fields exist in the table with
     * their values given in the map elements. If they already exist with the
     * given value, nothing is inserted. If they don't exist, the elements given
     * in the map are inserted into the table.
     *
     * @param table         the table name
     * @param elements      the elements to be inserted
     * @param fieldsToCheck the fields to be checked first. The corresponding values are
     *                      take from the elements map
     * @param info          flag indicating whether logging as info is wanted
     * @return the number of changed rows, or -1 if an error occurs
     */
    public int insertIfMissing(String table, Map elements,
                               Vector fieldsToCheck, boolean info) {
        int ret = -1;
        String sql = null;
        Connection conn = null;
        boolean insert = true;
        PreparedStatement pstmt;
        try {
            if (table != null && !table.isEmpty() && fieldsToCheck != null
                    && fieldsToCheck.size() > 0) {
                StringBuilder whereClause = null;
                Vector values = null;
                for (Enumeration e = fieldsToCheck.elements(); e
                        .hasMoreElements(); ) {
                    String field = (String) e.nextElement();
                    String value = (String) elements.get(field);
                    if (value != null) {
                        if (whereClause == null) {
                            whereClause = new StringBuilder(field + " = ?");
                            values = new Vector();
                        } else
                            whereClause.append(" AND ").append(field).append(" = ?");
                        values.add(value);
                    }
                }

                if (whereClause != null) {
                    sql = "SELECT count(*) FROM " + table + " WHERE "
                            + whereClause;
                    logSQL(sql, info);
                    conn = getConnection();
                    pstmt = conn.prepareStatement(sql);
                    int i = 0;
                    for (Enumeration e = values.elements(); e.hasMoreElements(); ) {
                        pstmt.setObject(++i, e.nextElement());
                    }
                    ResultSet r = pstmt.executeQuery();
                    if (r.first()) {
                        int count = r.getInt("count(*)");
                        if (count > 0)
                            insert = false;
                    }
                    r.close();
                    pstmt.close();
                }
            }
            if (insert)
                ret = insert(table, elements, info);
            else
                ret = 0;
        } catch (Exception ex) {
            if (logger != null)
                logger.error(sql, ex);
            System.err.println(sql);
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (conn != null)
                freeConnection(conn);
        }
        return ret;

    }

    /**
     * Inserts field/value pairs into the given database table.
     *
     * @param table    the table name
     * @param elements the elements - the key of the map is the field name, the value
     *                 its value.
     * @return number of changed rows, or -1, if an error occurs
     */
    public int insert(String table, Map elements) {
        return insert(table, elements, false);
    }

    /**
     * Inserts field/value pairs into the given database table.
     *
     * @param table    the table name
     * @param elements the elements - the key of the map is the field name, the value
     *                 its value.
     * @param info     flag indicating whether logging as info is wanted
     * @return number of changed rows, or -1, if an error occurs
     */
    public int insert(String table, Map elements, boolean info) {
        Connection conn = null;
        int ret = -1;
        String sql = null;
        if (elements != null && table != null && !table.isEmpty()) {
            try {
                String fields = ""; // all field names
                StringBuilder parameters = new StringBuilder(); // all "?" parameters for
                // prep. stmt.
                boolean firstElem = true;
                for (Object item : elements.keySet()) {
                    if (!firstElem) {
                        fields += ",";
                        parameters.append(",");
                    } else
                        firstElem = false;
                    fields += (String) item;
                    parameters.append("?");
                }
                sql = "INSERT INTO " + table + "(" + fields + ")" + "VALUES ("
                        + parameters + ")";
                logSQL(sql, info);
                conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                int i = 0;
                for (Object o : elements.values()) {
                    i++;
                    pstmt.setObject(i, o);
                }
                ret = pstmt.executeUpdate();
                pstmt.close();
            } catch (Exception ex) {
                if (logger != null)
                    logger.error(sql, ex);
                System.err.println(sql);
                de.unidu.is.util.Log.error(ex);
            } finally {
                if (conn != null)
                    freeConnection(conn);
            }
        }
        return ret;
    }

    /**
     * Removes all objects in a table identified by a given remove condition.
     * The query condition is given by a map of fields (key) and corresponding
     * values. The conjunction of all single remove conditions is checked. If
     * the map is null or empty, every entry in the table will be removed!
     *
     * @param table           the name of the table
     * @param removeCondition the remove condition
     * @param info            flag indicating whether logging as info is wanted
     * @return number of changed rows, or -1 if an error occurred
     */
    public int remove(String table, Map removeCondition, boolean info) {
        int removed = -1;
        String sql = null;
        Connection conn = null;
        boolean insert = true;
        PreparedStatement pstmt;
        try {
            if (table != null && !table.isEmpty()) {
                StringBuilder whereClause = null;
                Vector values = null;
                for (Object o : removeCondition.keySet()) {
                    String field = (String) o;
                    String value = (String) removeCondition.get(field);
                    if (value != null) {
                        if (whereClause == null) {
                            whereClause = new StringBuilder(field + " = ?");
                            values = new Vector();
                        } else
                            whereClause.append(" AND ").append(field).append(" = ?");
                        values.add(value);
                    }
                }

                sql = "DELETE FROM " + table;
                if (whereClause != null)
                    sql += " WHERE " + whereClause;
                logSQL(sql, info);
                conn = getConnection();
                pstmt = conn.prepareStatement(sql);
                int i = 0;
                for (Enumeration e = values.elements(); e.hasMoreElements(); ) {
                    pstmt.setObject(++i, e.nextElement());
                }
                removed = pstmt.executeUpdate();
            }
        } catch (Exception ex) {
            if (logger != null)
                logger.error(sql, ex);
            System.err.println(sql);
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (conn != null)
                freeConnection(conn);
        }
        return removed;
    }

    /**
     * Executes a SQL update and returns the number of changed rows.
     * <p>
     * <p>
     * The corresponding statement is already closed.
     *
     * @param sql SQL upodate
     * @return number of changed rows, or -1, if an error occurs
     */
    public int executeUpdate(String sql) {
        return executeUpdate(sql, false);
    }

    /**
     * Executes a SQL update and returns the number of changed rows. Errors are
     * ignored.
     * <p>
     * <p>
     * The corresponding statement is already closed.
     *
     * @param sql SQL upodate
     * @return number of changed rows, or -1, if an error occurs
     */
    public int executeUpdateIgnoreError(String sql) {
        logSQL(sql, false);
        int h = sql.indexOf("select ");
        if (h != -1)
            printExplain(sql.substring(h).trim());
        int ret = -1;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ret = stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
        } finally {
            if (conn != null)
                freeConnection(conn);
        }
        return ret;
    }

    /**
     * Executes a SQL update and returns the number of changed rows.
     * <p>
     * <p>
     * The corresponding statement is already closed.
     *
     * @param sql  SQL upodate
     * @param info flag indicating whether logging as info is wanted
     * @return number of changed rows, or -1, if an error occurs
     */
    public int executeUpdate(String sql, boolean info) {
        logSQL(sql, info);
        int h = sql.indexOf("select ");
        if (h != -1)
            printExplain(sql.substring(h).trim());
        int ret = -1;
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ret = stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            if (logger != null)
                logger.error(sql, ex);
            System.out.println(sql);
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (conn != null)
                freeConnection(conn);
        }
        return ret;
    }

    /**
     * Executes a SQLsStatment and returns the number of changed rows.
     * <p>
     * <p>
     * The corresponding statement is already closed.
     *
     * @param sql  SQL upodate
     * @param info flag indicating whether logging as info is wanted
     * @return number of changed rows, or -1, if an error occurs
     */
    public int executeUpdate(PreparedStatement sql, boolean info) {
        logSQL(sql.toString(), info);
        int h = sql.toString().indexOf("select ");
        if (h != -1)
            printExplain(sql.toString().substring(h).trim());
        int ret = -1;
        Connection conn = null;
        try {
            conn = getConnection();
            ret = sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            if (logger != null)
                logger.error(sql, ex);
            System.out.println(sql);
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (conn != null)
                freeConnection(conn);
        }
        return ret;
    }

    /**
     * Fetches the attributes of a table.
     * <p>
     *
     * @param table Tablename
     * @return number of changed rows, or -1, if an error occurs
     */
    public ResultSet getColumns(String table) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            ResultSet rs = conn.getMetaData().getColumns("", "", table, "");
            result2conn.put(rs, conn);
            return rs;
        } catch (SQLException ex) {
            System.out.println("Could not fetch columns from table " + table);
            de.unidu.is.util.Log.error(ex);
            if (conn != null)
                freeConnection(conn);
            throw ex;
        }
    }

    /**
     * Prompts the result of the "explain" command. Currently disabled.
     *
     * @param sql sql statement
     */
    private void printExplain(String sql) {
        if (true)
            return;
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("explain " + sql);
            result2stmt.put(result, stmt);
            result2conn.put(result, conn);
            ResultSetMetaData rsmtd = result.getMetaData();
            while (result.next()) {
                for (int i = 1; i <= rsmtd.getColumnCount(); i++) {
                    System.out.println(rsmtd.getColumnName(i) + ":\t"
                            + result.getString(i));
                }
            }
            close(result);
        } catch (SQLException ex) {
            if (logger != null)
                logger.error(sql, ex);
        }
    }

    /**
     * Logs the specified SQL statement
     *
     * @param sql  SQL statement
     * @param info flag indicating whether logging as info is wanted
     */
    private void logSQL(String sql, boolean info) {
        if (logger != null) {
            if (!info)
                logger.debug(sql);
            else
                logger.info(sql);
        }
        //System.out.println(sql);
    }

    /**
     * Creates a table (and removes an already existing table first).
     * <p>
     * <p>
     * The <code>definition</code> must not contain the surrounding brackets!
     * <p>
     * <p>
     * This command is logged as info.
     *
     * @param tableName  name of the table to be created
     * @param definition definition of the table
     */
    public void createTable(String tableName, String definition) {
        dropTable(tableName);
        executeUpdate("create table " + tableName + " (" + definition + ")",
                true);
    }

    /**
     * Checks whether the table name exists.
     *
     * @param tableName name of the table to be checked
     */
    public boolean existsTable(String tableName) {
        boolean ret = false;
        /*
         * try { Connection conn = getConnection(); DatabaseMetaData md =
         * conn.getMetaData(); ResultSet rs = md.getTables(null, null,
         * tableName, null); ret = rs.next(); System.out.println("ret=" + ret);
         * rs.close(); } catch (Exception ex) { }
         */
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select count(*) from " + tableName)) {
            ret = rs.next();
        } catch (SQLException ex) {
        }
        freeConnection(conn);
        return ret;
    }

    /**
     * Creates a table if it does not exist.
     * <p>
     * <p>
     * The <code>definition</code> must not contain the surrounding brackets!
     * <p>
     * <p>
     * This command is logged as info.
     *
     * @param tableName  name of the table to be created
     * @param definition definition of the table
     */
    public void createTableIfNotExists(String tableName, String definition) {
        if (!existsTable(tableName))
            createTable(tableName, definition);
    }

    /**
     * Removes an already existing table.
     *
     * @param tableName name of the table to be removed
     */
    public void dropTable(String tableName) {
        formatter.remove(tableName);
    }

    /**
     * Closes the connection belonging to a specific
     * <code>java.sql.ResultSet</code>, derived from a
     * <code>executeQuery(String)</code> call.
     *
     * @param result ResultSet
     */
    public void close(ResultSet result) {
        if (result == null)
            return;
        Statement stmt = (Statement) result2stmt.get(result);
        result2stmt.remove(result);
        Connection conn = (Connection) result2conn.get(result);
        result2conn.remove(result);
        try {
            result.close();
            stmt.close();
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        freeConnection(conn);
    }

    /**
     * Closes all open connections.
     */
    public void close() {
        // Create a copy of the HashMap. Iterator of the copy HashMap, so we can
        // remove in the original hashmap
        HashMap m = new HashMap(result2stmt);
        for (Object o : m.keySet()) close((ResultSet) o);

    }

    /**
     * Returns a text representation of the current object.
     *
     * @return text representation of the current object
     */
    public String toString() {
        return "DB[uri=" + dbURI + "," + dbUser + "]";
    }

    /**
     * Returns the URI of the database.
     *
     * @return URI of the database
     */
    public String getDbURI() {
        return dbURI;
    }

    /**
     * Returns the user of the database.
     *
     * @return user of the database
     */
    public String getDbUser() {
        return dbUser;
    }

}