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

 
// $Id: YearDT.java,v 1.7 2005/02/21 17:29:26 huesselbeck Exp $
package de.unidu.is.retrieval.pire.dt;

import java.util.Iterator;

import de.unidu.is.retrieval.pire.Index;

/**
 * A class for the IR datatype "year", containing the deterministic 
 * operators "&lt;", "&lt;=", "&gt;", "&gt;=" and the vague operators
 * "~&lt;", "~&gt;" and "~=". The identity mapping function is used for
 * the deterministic operators, a logistic function for the vague ones.
 * 
 * @author Henrik Nottelmann
 * @since 2003-09-23
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:26 $
 */
public class YearDT extends NumberDT {

	/**
	 * The name of this datatype.
	 * 
	 */
	public static final String NAME = "Year";

	/**
	 * Operator name "=".
	 * 
	 */
	public static final String EQUALS = "=";

	/**
	 * Operator name "=".
	 * 
	 */
	public static final String EQ = "=";

	/**
	 * Operator name "<".
	 * 
	 */
	public static final String LT = "<";

	/**
	 * Operator name "<=".
	 * 
	 */
	public static final String LE = "<=";

	/**
	 * Operator name ">".
	 * 
	 */
	public static final String GT = ">";

	/**
	 * operator name ">=".
	 * 
	 */
	public static final String GE = ">=";

	/**
	 * Operator name "~=".
	 * 
	 */
	public static final String VEQ = "~=";

	/**
	 * Operator name "~<".
	 * 
	 */
	public static final String VLT = "~<";

	/**
	 * Operator name "~>".
	 * 
	 */
	public static final String VGT = "~>";

	/* (non-Javadoc)
	 * @see de.unidu.is.pire.dt.DT#getIndexTokens(de.unidu.is.pire.index.Index, java.lang.String)
	 */
	public Iterator getIndexTokens(Index index, String operator) {
		final int MIN = 0;
		final int MAX = 2100;
		final int DIFF = MAX - MIN + 1;
		final int MAX2 = (int) Math.pow(DIFF, 2);
		if (operator.equals(EQUALS))
			return super.getIndexTokens(index, operator);
		return new Iterator() {
			protected int i = MIN;
			public boolean hasNext() {
				return i <= MAX;
			}
			public Object next() {
				return "" + (i++);
			}
			public void remove() {
			}
		};
	}

}
