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

 
// $Id: WeightedQueryCondition.java,v 1.4 2005/02/21 17:29:23 huesselbeck Exp $
package de.unidu.is.retrieval;

/**
 * A weighted query condition.
 * 
 * @author Henrik Nottelmann
 * @since 2004-04-12
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:23 $
 */
public class WeightedQueryCondition extends QueryCondition {

	/**
	 * Condition weight.
	 */
	protected double weight;

	/**
	 * Creates a new object.
	 */
	public WeightedQueryCondition() {
		super();
		setWeight(1);
	}

	/**
	 * Creates a new object by parsing the XIRQL condition string.
	 *
	 * @param condition XIRQL condition string
	 */
	public WeightedQueryCondition(String condition) {
		super(condition);
	}

	/**
	 * Creates a new object.
	 *
	 * @param path path
	 * @param operator name
	 * @param value comparison value
	 */
	public WeightedQueryCondition(
		String path,
		String operator,
		String value) {
		super(path, operator, value);
		weight = 1;
	}

	/**
	 * Creates a new object.
	 *
	 * @param weight condition weight
	 * @param path path
	 * @param operator name
	 * @param value comparison value
	 */
	public WeightedQueryCondition(
		double weight,
		String path,
		String operator,
		String value) {
		super(path, operator, value);
		setWeight(weight);
	}


	/**
	 * Returns the weight.
	 *
	 * @return weight.
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Sets the weight.
	 *
	 * @param weight weight to set.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return weight + "," + super.toString();
	}

	/* (non-Javadoc)
	 * @see de.unidu.is.retrieval.QueryCondition#parse(java.lang.String)
	 */
	protected void parse(String condition) {
		weight = 1;
		int h = condition.indexOf(",");
		if (h != -1) {
			String w = condition.substring(0, h);
			try {
				weight = Double.parseDouble(w);
				condition = condition.substring(h + 1);
			} catch (NumberFormatException e) {
			}
		}
		super.parse(condition);
	}

}
