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

 
// $Id: WeightedString.java,v 1.3 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

/**
 * A (string,weight) pair, which can be sorted with decreasing weights.
 * 
 * @author Henrik Nottelmann
 * @since 2005-01-10
 * @version $Revision: 1.3 $, $Date: 2005/02/21 17:29:29 $
 */
public class WeightedString implements Comparable {

	/**
	 * The string.
	 */
	protected String string;

	/**
	 * The weight of the string.
	 */
	protected double weight;

	/**
	 * Creates a new, empty instance.
	 */
	public WeightedString() {
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param string string
	 * @param weight weight
	 */
	public WeightedString(String string, double weight) {
		setString(string);
		setWeight(weight);
	}

	/**
	 * Returns the string.
	 * 
	 * @return string
	 */
	public String getString() {
		return string;
	}

	/**
	 * Sets the string.
	 * 
	 * @param string string.
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * Returns the weight of the string.
	 *  
	 * @return weight of the string.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of the string.
	 * 
	 * @param weight weight of the string
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof WeightedString))
			return false;
		WeightedString str = (WeightedString) obj;
		return str.string.equals(string) && str.weight == weight;
	}

	/**
	 * Compares this entry (the score in decreasing order) with the
	 * specified one.
	 * 
	 * @param obj the reference object with which to compare.
	 * @return negative, zero, positive
	 */
	public int compareTo(Object obj) {
		WeightedString str = (WeightedString) obj;
		if (str.weight > weight)
			return 1;
		if (str.weight < weight)
			return -1;
		return 0;
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
		return "WeightedString[" + string + "," + weight + "]";
	}

}
