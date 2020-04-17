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

// $Id: HySpiritFact.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.hyspirit;

import de.unidu.is.expressions.Expression;
import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.pdatalog.ds.Literal;

/**
 * A fact whose string representation matches the one of HySpirit (thus, it is
 * pDatalog and not pDatalog++).
 * 
 * @author Henrik Nottelmann
 * @since 2004-07-17
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public class HySpiritFact extends Fact {

	/**
	 * Creates a new object.
	 * 
	 * @param head
	 *                   literal used for creating this fact
	 * @param mapping
	 *                   mapping for computing the probability of this fact
	 */
	public HySpiritFact(Literal head, Expression mapping) {
		super(head, mapping);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param head
	 *                   literal used for creating this fact
	 */
	public HySpiritFact(Literal head) {
		super(head);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param prob
	 *                   probability of the fact
	 * @param head
	 *                   literal used for creating this fact
	 */
	public HySpiritFact(double prob, Literal head) {
		super(prob, head);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param fact
	 *                   original fact.
	 */
	public HySpiritFact(Fact fact) {
		super(fact);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.pdatalog.ds.Rule#toString()
	 */
	public String toString() {
		String temp;
		int j = 0;
		temp = super.toString();
		char[] retarr = new char[temp.length()];
		for (int i = 0; i < temp.length(); i++) {
			if ((temp.charAt(i) != '\'') & (temp.charAt(i) != '|'))
				retarr[j++] = temp.charAt(i);
			else if (temp.charAt(i) == '|') {
				retarr[j++] = '.';
				i = temp.length();
			}
		}
		return new String(retarr).trim();
	}

}