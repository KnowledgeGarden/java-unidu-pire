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

 
// $Id: PDatalogSQLFormatter.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.unidu.is.sql.SQL;
import de.unidu.is.sql.SQLFormatter;
import de.unidu.is.sql.SQLFormatterFactory;
import de.unidu.is.util.DB;

/**
 * An formatter for pDatalog++ into SQL statements. 
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-08
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public class PDatalogSQLFormatter implements SQLFormatter {

	/**
	 * The formatter to be used.
	 */
	protected SQLFormatter formatter;

	/**
	 * Creates a new formatter.
	 * 
	 * @param db DB parameters
	 */
	public PDatalogSQLFormatter(DB db) {
		formatter = SQLFormatterFactory.newFactory().newFormatter(db);
	}

	/**
	 * Creates a relation table.
	 * 
	 * @param relation relation for which a table will be created
	 */
	public void create(Relation relation) {
		create(relation.getArity(), relation.getName());
	}

	/**
	 * Creates a relation table.
	 * 
	 * @param arity arity of the relation (number of arguments) 
	 * @param relationName name of the relation
	 */
	public void create(int arity, String relationName) {
		String[] cols = new String[arity + 1];
		String[] types = new String[arity + 1];
		for (int i = 0; i < arity; i++) {
			cols[i] = "arg" + i;
			types[i] = "${type.text}";
		}
		cols[arity] = "prob";
		types[arity] = "${type.double}";
		create(relationName, cols, types);
	}

	/**
	 * Clear the specified relation, which means deletes all facts for 
	 * that relation.
	 * 
	 * @param relation relation to be cleared
	 */
	public void clear(Relation relation) {
		clear(relation.getName());
	}

	/**
	 * Dumps the content of the specified relation to STDOUT.
	 * 
	 * @param relation relation to be dumped
	 */
	public void dump(Relation relation) {
		dump(relation, System.out);
	}

	/**
	 * Dumps the content of the specified relation to STDOUT.
	 * 
	 * @param arity arity of the relation to be dumped
	 * @param relationName name of the relation to be dumped
	 */
	public void dump(int arity, String relationName) {
		dump(arity, relationName, System.out);
	}

	/**
	 * Dumps the content of the specified relation to the specified stream.
	 * 
	 * @param relation relation to be dumped
	 * @param out stream to which the relation will be dumped
	 */
	public void dump(Relation relation, PrintStream out) {
		dump(relation.getArity(), relation.getName());
	}

	/**
	 * Dumps the content of the specified relation to the specified stream.
	 * 
	 * @param arity arity of the relation to be dumped
	 * @param relationName name of the relation to be dumped
	 * @param out stream to which the relation will be dumped
	 */
	public void dump(int arity, String relationName, PrintStream out) {
		out.println(relationName + ":");
		ResultSet rs = null;
		try {
			String sql =
				"select * from " + relationName + " order by prob desc";
			rs = getDB().executeQuery(sql);
			while (rs.next()) {
				for (int i = 1; i <= arity + 1; i++)
					out.print(rs.getString(i) + "\t");
				out.println();
			}
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			close(rs);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#clear(java.lang.String)
	 */
	public void clear(String tableName) {
		formatter.clear(tableName);
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#close(java.sql.ResultSet)
	 */
	public void close(ResultSet rs) {
		formatter.close(rs);
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#create(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	public void create(String tableName, String[] cols, String[] types) {
		formatter.create(tableName, cols, types);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return formatter.equals(obj);
	}

	/**
	 * Returns the underlying DB object.
	 * 
	 * @return underlying DB object
	 */
	public DB getDB() {
		return formatter.getDB();
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#getSelect(de.unidu.is.sql.SQL)
	 */
	public String getSelect(SQL sql) {
		return formatter.getSelect(sql);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return formatter.hashCode();
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#perform(de.unidu.is.sql.SQL, java.lang.String)
	 */
	public void perform(SQL sql, String resultTableName) {
		formatter.perform(sql, resultTableName);
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#performQuery(de.unidu.is.sql.SQL)
	 */
	public ResultSet performQuery(SQL sql) throws SQLException {
		return formatter.performQuery(sql);
	}

	/*
	 *  (non-Javadoc)
	 * @see de.unidu.is.sql.SQLFormatter#remove(java.lang.String)
	 */
	public void remove(String tableName) {
		formatter.remove(tableName);
	}

	/**
	 * Adds an index to the specified table.
	 * 
	 * @param tableName name of the table
	 * @param indexName name of the new index
	 * @param cols imvolved columns
	 * @param textCols flag indicating that the column is a text column
	 */
	public void addIndex(
		String tableName,
		String indexName,
		String[] cols,
		boolean[] textCols) {
		formatter.addIndex(tableName, indexName, cols, textCols);
	}

	/**
	 * Sets the specified DB object.
	 * 
	 * @param db DB object
	 */
	public void setDB(DB db) {
		formatter.setDB(db);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return formatter.toString();
	}

}
