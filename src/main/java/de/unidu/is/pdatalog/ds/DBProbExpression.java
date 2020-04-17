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

// $Id: DBProbExpression.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import de.unidu.is.expressions.AbstractExpression;

/**
 * An expression representing the column for the fact probabilities in a
 * relational table.
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 */
public class DBProbExpression extends AbstractExpression {

	/**
	 * Table name.
	 */
	protected String table;

	/**
	 * Creates a new object.
	 * 
	 * @param table
	 *                   table name
	 */
	public DBProbExpression(String table) {
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.plogics.Argument#get()
	 */
	public String get() {
		return toString();
	}

	/**
	 * Sets the table name (the string can be the table name or
	 * <code>[table].[col]</code>).
	 * 
	 * @param arg
	 *                   specification of table name
	 */
	public void set(String arg) {
		int h = arg.indexOf(".");
		table = arg.substring(0, h);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return table + ".prob";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (!(o instanceof DBProbExpression))
			return false;
		return ((DBProbExpression) o).toString().equals(toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
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
	 * Sets the table name
	 * 
	 * @param string
	 *                   table name
	 */
	public void setTable(String string) {
		table = string;
	}

}