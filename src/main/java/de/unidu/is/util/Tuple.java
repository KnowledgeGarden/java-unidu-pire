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

// $Id: Tuple.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

/**
 * A simple tuple, i.e. an array of objects. In contrast to an object array,
 * this class provides convenient methods as well as implementations of
 * <code>equals(Object)</code>,<code>hashCode()</code> and
 * <code>toString()</code>.
 * 
 * @author Henrik Nottelmann
 * @since 2003-06-28
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 */
public class Tuple {

	/**
	 * Array with the tuple values.
	 */
	protected Object[] array;

	/**
	 * Creates a new object.
	 * 
	 * @param array
	 *                   objects
	 */
	public Tuple(Object[] array) {
		this.array = array;
	}

	/**
	 * Returns the length, i.e. the number of objects.
	 * 
	 * @return number of objects
	 */
	public int length() {
		return array.length;
	}

	/**
	 * Returns the length, i.e. the number of objects.
	 * 
	 * @return number of objects
	 */
	public int size() {
		return length();
	}

	/**
	 * Returns the i-th object.
	 * 
	 * @param i
	 *                   index number
	 * @return object at this index number
	 */
	public Object get(int i) {
		return array[i];
	}

	/**
	 * Sets the i-th object.
	 * 
	 * @param i
	 *                   index number
	 * @param o
	 *                   object to be set at this index number
	 */
	public void set(int i, Object o) {
		array[i] = o;
	}

	/**
	 * Returns the i-th object as a double.
	 * 
	 * @param i
	 *                   index number
	 * @return object at this index number
	 */
	public double getDouble(int i) {
		Double ret = (Double) get(i);
		return ret == null ? 0 : ret.doubleValue();
	}

	/**
	 * Returns the i-th object as an integer.
	 * 
	 * @param i
	 *                   index number
	 * @return object at this index number
	 */
	public int getInt(int i) {
		Integer ret = (Integer) get(i);
		return ret == null ? 0 : ret.intValue();
	}

	/**
	 * Returns the i-th object as a string.
	 * 
	 * @param i
	 *                   index number
	 * @return object at this index number
	 */
	public String getString(int i) {
		Object ret = get(i);
		if (ret instanceof String)
			return (String) ret;
		return ret == null ? null : ret.toString();
	}

	public boolean equals(Object o) {
		if (!(o instanceof Tuple))
			return false;
		Tuple t = (Tuple) o;
		if (t.length() != length())
			return false;
		for (int i = 0; i < length(); i++) {
			Object o1 = get(i);
			Object o2 = get(i);
			if (o1 == null && o2 == null)
				continue;
			if (o1 == null || o2 == null || !get(i).equals(t.get(i)))
				return false;
		}
		return true;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("Tuple[");
		for (int i = 0; i < length(); i++) {
			buf.append(get(i));
			if (i < length() - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
}