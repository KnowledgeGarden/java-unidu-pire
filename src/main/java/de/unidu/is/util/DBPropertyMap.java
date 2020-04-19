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

// $Id: DBPropertyMap.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

import java.sql.ResultSet;
import java.util.*;

/**
 * A property map backed by a RDBMS table.
 * <p>
 * <p>
 * This class uses a table with two columns (the default names are "propkey" and
 * "propvalue", but these can be changed) with the SQL type "text" for storing
 * the property map. This means that this set can only be used with strings, or
 * with objects for which the string representation is sufficient.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 * @since 2003-06-22
 */
public class DBPropertyMap extends PropertyMap {

    /**
     * The DB which is used for backing this map.
     */
    protected final DB db;

    /**
     * The table used for backing this map.
     */
    protected final String tableName;

    /**
     * The column name for the keys used for this map.
     */
    protected final String keyCol;

    /**
     * The column name for the values used for this map.
     */
    protected final String valueCol;

    /**
     * Creates a new instance (and the corresponding table, if it does not
     * exists), and sets the columns to "propkey" and "propvalue".
     *
     * @param db        DB used by this set
     * @param tableName name of the table used by this set
     */
    public DBPropertyMap(DB db, String tableName) {
        this(db, tableName, "propkey", "propvalue");
    }

    /**
     * Creates a new instance (and the corresponding table, if it does not
     * exists), and sets the column names.
     *
     * @param db        DB used by this set
     * @param tableName name of the table used by this set
     * @param keyCol    column name for keys
     * @param valueCol  column name for values
     * @param multiple  values if true, multiple values can be hold for a key
     */
    public DBPropertyMap(DB db, String tableName, String keyCol,
                         String valueCol, boolean multiple) {
        super(multiple);
        this.db = db;
        this.tableName = tableName;
        this.keyCol = keyCol;
        this.valueCol = valueCol;
        db.createTableIfNotExists(tableName, keyCol + " text," + valueCol
                + " text,key(" + keyCol + "(30))");
    }

    /**
     * Creates a new instance (and the corresponding table, if it does not
     * exists), and sets the columns to "propkey" and "propvalue".
     *
     * @param db        DB used by this set
     * @param tableName name of the table used by this set
     * @param multiple  values if true, multiple values can be hold for a key
     */
    public DBPropertyMap(DB db, String tableName, boolean multiple) {
        this(db, tableName, "propkey", "propvalue", multiple);
    }

    /**
     * Creates a new instance (and the corresponding table, if it does not
     * exists), and sets the column names.
     *
     * @param db        DB used by this set
     * @param tableName name of the table used by this set
     * @param keyCol    column name for keys
     * @param valueCol  column name for values
     */
    public DBPropertyMap(DB db, String tableName, String keyCol, String valueCol) {
        this.db = db;
        this.tableName = tableName;
        this.keyCol = keyCol;
        this.valueCol = valueCol;
        db.createTableIfNotExists(tableName, keyCol + " text," + valueCol
                + " text,key(" + keyCol + "(30))");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#clear()
     */
    public void clear() {
        db.executeUpdate("delete from " + tableName);
    }

    /**
     * Deletes the corresponding table.
     */
    public void delete() {
        db.dropTable(tableName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        boolean ret = false;
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select * from " + tableName + " where "
                    + valueCol + "=\"" + value + "\"");
            ret = rs.next();
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        Set set = new HashSet();
        fill(set, keyCol);
        return set;
    }

    /**
     * Adds all column elements (where the column is either the key or the value
     * column) to the specified collection.
     *
     * @param collection collection to which values are added
     * @param col        key column name or value column name
     */
    private void fill(Collection collection, String col) {
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select " + col + " as col from " + tableName);
            while (rs.next())
                collection.add(rs.getString("col"));
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(Object key) {
        String value = null;
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select " + valueCol + " as value from "
                    + tableName + " where " + keyCol + "=\"" + key + "\"");
            if (rs.next()) {
                value = rs.getString("value");
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return size() != 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        Set set = new HashSet();
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select distinct " + keyCol + " as key from "
                    + tableName);
            while (rs.next())
                set.add(rs.getString("key"));
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return set;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value) {
        Object oldValue = get(key);
        if (!isMultipleValues() && oldValue != null)
            db.executeUpdate("update " + tableName + " set " + valueCol
                    + " = \"" + value + "\" where " + keyCol + "=\"" + key
                    + "\"");
        else
            db.executeUpdate("insert into " + tableName + " values (\"" + key
                    + "\",\"" + value + "\")");
        return oldValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map t) {
        for (Object o : t.keySet()) {
            String key = o.toString();
            put(key, t.get(key));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(Object key) {
        Object oldValue = get(key);
        db.executeUpdate("delete from " + tableName + " where " + keyCol
                + "=\"" + key + "\"");
        return oldValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#size()
     */
    public int size() {
        int size = 0;
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select count(distinct key) as count from "
                    + tableName);
            if (rs.next()) {
                size = rs.getInt("count");
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return size;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#values()
     */
    public Collection values() {
        List list = new ArrayList();
        fill(list, valueCol);
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Map))
            return false;
        return asHashMap().equals(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return db + "-" + tableName;
    }

    /**
     * Returns a HashSet instance for this set.
     *
     * @return HashSet instance for this set
     */
    private Map asHashMap() {
        PropertyMap map = new HashPropertyMap(isMultipleValues());
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select * from " + tableName);
            while (rs.next())
                map.set(rs.getString(keyCol), rs.getString(valueCol));
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.util.PropertyMap#getAll(java.lang.Object)
     */
    public List getAll(Object key) {
        List ret = new ArrayList();
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select " + valueCol + " as value from "
                    + tableName + " where " + keyCol + "=\"" + key + "\"");
            while (rs.next()) {
                ret.add(rs.getString("value"));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.util.PropertyMap#remove(java.lang.Object,
     *           java.lang.Object)
     *  <<changed return to boolean - park >>
     */
    public boolean remove(Object key, Object value) {
        int x = db.executeUpdate("delete from " + tableName + " where " + keyCol
                + "=\"" + key + "\" and " + valueCol + "=\"" + value + "\"");
        return (x > -1);
    }

}