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


// $Id: DBSet.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set which is backed by a RDBMS table.<p>
 * <p>
 * This class uses a table wich one column (the default name is "value", but
 * this can be changed) with the SQL type "text" for storing the set. This
 * means that this set can only be used with strings, or with objects for
 * which the string representation is sufficient.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 * @since 2003-06-22
 */
public class DBSet implements Set {

    /**
     * The DB which is used for backing this set.
     */
    protected final DB db;

    /**
     * The table used for backing this set.
     */
    protected final String tableName;

    /**
     * The column name used for this set.
     */
    protected final String col;

    /**
     * Creates a new instance (and the corresponding table, if it does not
     * exists), and sets the column  to "value".
     *
     * @param db        DB used by this set
     * @param tableName name of the table used by this set
     */
    public DBSet(DB db, String tableName) {
        this(db, tableName, "value");
    }

    /**
     * Creates a new instance (and the corresponding table, if it does not
     * exists), and sets the column name.
     *
     * @param db        DB used by this set
     * @param tableName name of the table used by this set
     * @param col       column name
     */
    public DBSet(DB db, String tableName, String col) {
        this.db = db;
        this.tableName = tableName;
        this.col = col;
        db.createTableIfNotExists(
                tableName,
                col + " text,key(" + col + "(30))");
    }

    /**
     * Deletes the corresponding table.
     */
    public void delete() {
        db.dropTable(tableName);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#size()
     */
    public int size() {
        int size = 0;
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select count(*) as count from " + tableName);
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

    /* (non-Javadoc)
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return size() != 0;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        boolean contains = false;
        ResultSet rs = null;
        try {
            rs =
                    db.executeQuery(
                            "select * from "
                                    + tableName
                                    + " where "
                                    + col
                                    + "=\""
                                    + o
                                    + "\"");
            contains = rs.next();
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return contains;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#iterator()
     */
    public Iterator iterator() {
        return asHashSet().iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return asHashSet().toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a) {
        return asHashSet().toArray(a);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object o) {
        if (contains(o))
            return false;
        db.executeUpdate(
                "insert into " + tableName + " values (\"" + o + "\")");
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        if (contains(o))
            return true;
        db.executeUpdate(
                "delete from " + tableName + " where " + col + "=\"" + o + "\"");
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= add(o);
        }
        return changed;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        boolean changed = false;
        for (Object o : this) {
            if (!c.contains(o))
                changed |= remove(o);
        }
        return changed;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    /* (non-Javadoc)
     * @see java.util.Collection#clear()
     */
    public void clear() {
        db.executeUpdate("delete from " + tableName);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Set))
            return false;
        return asHashSet().equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /* (non-Javadoc)
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
    private Set asHashSet() {
        Set set = new HashSet();
        ResultSet rs = null;
        try {
            rs = db.executeQuery("select * from " + tableName);
            while (rs.next())
                set.add(rs.getString(col));
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            db.close(rs);
        }
        return set;
    }

}
