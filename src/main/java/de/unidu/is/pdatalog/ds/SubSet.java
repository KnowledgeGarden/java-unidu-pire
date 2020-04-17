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

// $Id: SubSet.java,v 1.4 2005/02/28 22:27:55 nottelma Exp $
// Achtung: Die Kommentare sind teilweise veraltet, für die Korrektheit wird
// daher keine "Garantie" übernommen!
// Bei Fragen oder Bugs bitte Email an nottelma@ls6.cs.uni-dortmund.de.

package de.unidu.is.pdatalog.ds;

/**
 * A subset of a given set {0,...,n-1}. Every number <code>i</code> can be
 * present (<code>flags[i]</code> is true) or absent (<code>flags[i]</code>
 * is false). n is the cardinality of the superset.
 * <p>
 * 
 * The SubSet can be represented as reverse binary string (by
 * {@link #toString()}) or a corresponding integer (by {@link #getValue()}).
 * E.g.: The superset may be {1,2,3,4}, and the SubSet may be {1,3,4}. Then the
 * binary string is "1011" and the integer value is 13.
 * 
 * @author Henrik Nottelmann
 * @since 2000-07-27
 * @version $Revision: 1.4 $, $Date: 2005/02/28 22:27:55 $
 */
public class SubSet {

	/**
	 * The flags indicating if a number is present of absent.
	 * <code>flags[i]</code> is true if <code>i</code> is present in this
	 * SubSet.
	 */
	protected boolean[] flags;

	/**
	 * The maximum number of any corresponding integer value plus one. It is
	 * <code>max == 2^(flags.length)</code>.
	 */
	protected int max;

	/**
	 * Creates a new SubSet of a set {0,...,card-1}. The SubSet is initially
	 * empty.
	 * 
	 * @param card
	 *                   the cardinality of the superset
	 * @see #SubSet(int,int)
	 * @see #SubSet(int,String)
	 */
	public SubSet(int card) {
		setCard(card);
	}

	/**
	 * Creates a new SubSet of a set {0,...,card-1}. The SubSet contains the
	 * numbers corresponding to the integer <code>value</code>.
	 * 
	 * @param card
	 *                   the cardinality of the superset
	 * @param value
	 *                   the value describing this SubSet
	 * @see #SubSet(int)
	 * @see #SubSet(int,String)
	 */
	public SubSet(int card, int value) {
		this(card);
		setValue(value);
	}

	/**
	 * Creates a new SubSet of a set {0,...,card-1}. The SubSet contains the
	 * numbers corresponding to the String <code>value</code>.
	 * 
	 * @param card
	 *                   the cardinality of the superset
	 * @param value
	 *                   the value describing this SubSet
	 * @see #SubSet(int)
	 * @see #SubSet(int,int)
	 */
	public SubSet(int card, String value) {
		this(card);
		setValue(value);
	}

	/**
	 * An auxiliary method returning 2^<code>exp</code>.
	 * 
	 * @param exp
	 *                   the exponent
	 * @return 2^<code>exp</code>
	 */
	public static int pow2(int exp) {
		return (int) Math.pow(2, exp);
	}

	/**
	 * Returns the cardinality of the superset.
	 * 
	 * @return the cardinality of the superset
	 */
	public int getCard() {
		return flags.length;
	}

	/**
	 * Sets the cardinality of the superset and clears this SubSet.
	 * 
	 * @param card
	 *                   the cardinality of the superset
	 */
	public void setCard(int card) {
		flags = new boolean[card];
		max = pow2(card);
	}

	/**
	 * Returns the maximum number of any corresponding integer value plus one.
	 * 
	 * @return the maximum number of any corresponding integer value plus one
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Returns the integer value corresponding to this SubSet. This is the
	 * integer value of the reverse of the binary string returned by
	 * {@link #toString()}.
	 * 
	 * @return the integer value corresponding to this SubSet
	 */
	public int getValue() {
		int value = 0;
		for (int i = 0; i < flags.length; i++)
			value += flags[i] ? pow2(i) : 0;
		return value;
	}

	/**
	 * An auxiliary method calculating <code>value % max</code>.
	 * 
	 * @param value
	 *                   the value to be trimmed
	 * @return <code>value % max</code>
	 */
	protected int trim(int value) {
		return (value + max) % max;
	}

	/**
	 * An auxiliary method calculating <code>value % 2^card</code>.
	 * 
	 * @param value
	 *                   the value to be trimmed
	 * @param card
	 *                   the cardinality
	 * @return <code>value % 2^card</code>
	 */
	protected static int trim(int value, int card) {
		int max = pow2(card);
		return (value + max) % max;
	}

	/**
	 * Sets this SubSet corresponding to an integer value.
	 * 
	 * @param value
	 *                   the integer value describing the new content of this SubSet
	 */
	public void setValue(int value) {
		value = trim(value);
		for (int i = 0; i < flags.length; i++) {
			flags[i] = (value % 2) == 1;
			value /= 2;
		}
	}

	/**
	 * Sets this SubSet corresponding to a reverse binary string.
	 * 
	 * @param value
	 *                   the reverse binary string describing the new content of this
	 *                   SubSet
	 */
	public void setValue(String value) {
		for (int i = 0; i < flags.length; i++)
			flags[i] = (value.charAt(flags.length - i - 1) % 2) == 1;
	}

	/**
	 * Tests is the specified number <code>num</code> is in this SubSet.
	 * 
	 * @param num
	 *                   the number to be testes
	 * @return true is number is in this SubSet, i.e. <code>flags[num]</code>
	 *               is true
	 */
	public boolean contains(int num) {
		return flags[num];
	}

	/**
	 * Adds the specified number to this SubSet if it is not already present.
	 * 
	 * @param num
	 *                   the number to be added
	 */
	public void add(int num) {
		flags[num] = true;
	}

	/**
	 * Removes the specified number to this SubSet if it is present.
	 * 
	 * @param num
	 *                   the number to be removed
	 */
	public void remove(int num) {
		flags[num] = false;
	}

	/**
	 * Sets this SubSet to the next subset corresponding to the integer value.
	 */
	public void next() {
		setValue(getValue() + 1);
	}

	/**
	 * Sets this SubSet to the previous subset corresponding to the integer
	 * value.
	 */
	public void prev() {
		setValue(getValue() - 1);
	}

	/**
	 * Returns the next subset corresponding to the integer value.
	 * 
	 * @return the next subset corresponding to the integer value
	 */
	public SubSet getNext() {
		return new SubSet(flags.length, getValue() + 1);
	}

	/**
	 * Returns the next subset corresponding to the integer value.
	 * 
	 * @return the next subset corresponding to the integer value
	 */
	public SubSet getPrev() {
		return new SubSet(flags.length, getValue() - 1);
	}

	/**
	 * Returns the cardinality of in this SubSet. I.e. the number of elements in
	 * this SubSet.
	 * 
	 * @return the number in this SubSet
	 */
	public int count() {
		int ret = 0;
		for (int i = 0; i < flags.length; i++)
			if (flags[i])
				ret++;
		return ret;
	}

	/**
	 * Returns the cardinality of in the SubSet created by <code>new 
	 * SubSet(card,value)</code>.
	 * I.e. the number of elements in the specified SubSet.
	 * 
	 * @param value
	 *                   the value of the specified SubSet
	 * @param card
	 *                   the cardinality of the superset of the specified SubSet
	 * @return the number in the specified SubSet
	 * @see #count()
	 */
	public static int count(int value, int card) {
		int ret = 0;
		value = trim(value, card);
		for (int i = 0; i < card; i++) {
			ret += value % 2;
			value /= 2;
		}
		return ret;
	}

	/**
	 * Tests if this SubSet is a subset of the specified SubSet.
	 * <p>
	 * 
	 * A SubSet A is a subset of another SubSet B if
	 * <ul>
	 * <li>A.getCard() == B.getCard(), and</li>
	 * <li>all numbers contained in A are also contained in B.</li>
	 * </ul>
	 * 
	 * @param subSet
	 *                   the SubSet to be tested
	 * @return true if this SubSet is a subset of the specified SubSet
	 */
	public boolean isSubSetFrom(SubSet subSet) {
		// true, wenn this Teilmenge von subset ist
		if (getCard() != subSet.getCard())
			return false;
		for (int i = 0; i < flags.length; i++)
			if (flags[i] && !subSet.flags[i])
				return false;
		return true;
	}

	/**
	 * Tests if the SubSet corresponding to the first specified integer is a
	 * subset of the SubSet corresponding to the second specified integer.
	 * <p>
	 * 
	 * A SubSet A is a subset of another SubSet B if
	 * <ul>
	 * <li>A.getCard() == B.getCard(), and</li>
	 * <li>all numbers contained in A are also contained in B.</li>
	 * </ul>
	 * 
	 * @param value1
	 *                   the integer corresponding to the first SubSet to be tested
	 * @param value2
	 *                   the integer corresponding to the second SubSet to be tested
	 * @return true if value1 if a subset of value2
	 */
	public static boolean isSubSetFrom(int value1, int value2) {
		return (value1 & value2) == value1;
	}

	/**
	 * Removes all numbers in the specified SubSet from this SubSet if they are
	 * present. So this methods performs a <code>this = this \ subSet</code>
	 * operation.
	 * 
	 * @param subSet
	 *                   the subSet containing all numbers to be removed from this
	 *                   SubSet
	 */
	public void minus(SubSet subSet) {
		for (int i = 0; i < flags.length; i++)
			if (subSet.flags[i])
				flags[i] = false;
	}

	/**
	 * Compares the specified Object with this SubSet for equality.
	 * <p>
	 * Two SubSets A and B are equal iff
	 * 
	 * <ul>
	 * <li>A.getCard() == B.getCard(), and</li>
	 * <li>all numbers contained in A are also contained in B, and</li>
	 * <li>all numbers contained in B are also contained in A.</li>
	 * </ul>
	 * 
	 * 
	 * @param o
	 *                   the Object to be compared for equality with this SubSet
	 * @return true if the specified Object is equal to this SubSet
	 */
	public boolean equals(Object o) {
		SubSet subset = (SubSet) o;
		return getValue() == subset.getValue();
	}

	/**
	 * Returns a string representation of this SubSet as a reverse binary
	 * string.
	 * <p>
	 * 
	 * E.g.: The superset may be {1,2,3,4}, and the SubSet may be {1,3,4}. Then
	 * the binary string returned by this method is "1011".
	 * 
	 * @return a string representation of this SubSet
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < flags.length; i++)
			str = (flags[i] ? "1" : "0") + str;
		return str;
	}

	/**
	 * Returns a string representation of the SubSet created by <code>new 
	 * SubSet(card,value)</code>.
	 * 
	 * @param value
	 *                   the value of the specified SubSet
	 * @param card
	 *                   the cardinality of the superset of the specified SubSet
	 * @return a string representation of this SubSet
	 * @see #toString()
	 */
	public static String toString(int value, int card) {
		value = trim(value, card);
		String str = "";
		for (int i = 0; i < card; i++) {
			str = (value % 2) + str;
			value /= 2;
		}
		return str;
	}

}