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

// $Id: PropertyMap.java,v 1.13 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.util;

import java.util.*;

/**
 * A map which provides convenient setXYZ() and getXYZ() methods for setting and
 * returning values of type XYZ, given String keys. In addition,
 * <code>${foo.bar}</code> is replaced by the value of the property
 * <code>foo.bar</code> when a value is returned in one of the getXYZ()
 * methods.
 * <p>
 * <p>
 * The map can be configured so that it can hold multiple values for a key. The
 * methods inhereted from <code>java.util.Map</code> then operate on the first
 * stored value, unless otherwise noted.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.13 $, $Date: 2005/03/14 17:33:13 $
 * @since 2003-06-20
 */
public abstract class PropertyMap implements Map {

    /**
     * Smallest detected change in double values.
     */
    private final double EPSILON = 1E-4;

    /**
     * A flag specifying if multiple values can be hold for a key.
     */
    protected boolean multipleValues;

    /**
     * Creates a new instance with only one value per key.
     */
    public PropertyMap() {
        multipleValues = false;
    }

    /**
     * Creates a new instance.
     *
     * @param multipleValues values if true, multiple values can be hold for a key
     */
    public PropertyMap(boolean multipleValues) {
        this.multipleValues = multipleValues;
    }

    /**
     * Tests if multiple values for the same key are allowed.
     *
     * @return returns true iff multiple values for the same key are allowed
     */
    public boolean isMultipleValues() {
        return multipleValues;
    }

    /**
     * Sets whether multiple values for the same key are allowed.
     *
     * @param multipleValues if true, multiple values for the same key are allowed
     */
    public void setMultipleValues(boolean multipleValues) {
        this.multipleValues = multipleValues;
    }

    /**
     * Tests if multiple values for the same key are allowed.
     *
     * @return returns true iff multiple values for the same key are allowed
     * @deprecated Use <code>isMultipleValues()</code> instead
     */
    public boolean hasMultipleValues() {
        return multipleValues;
    }

    /**
     * Sets the string value of the specified key.
     *
     * @param key   property key
     * @param value string value of the specified key
     */
    public void set(Object key, String value) {
        put(key, value);
    }

    /**
     * Returns a list with all values stored for the specified key.
     *
     * @param key property key
     * @return list with all values stored for the specified key
     */
    public abstract List getAll(Object key);

    /**
     * Removes the specified value for the specified key.
     *
     * @param key   property key
     * @param value value to be removed
     * @return <<changed return type to boolean for jdk1.8 - park>>
     */
    public abstract boolean remove(Object key, Object value);

    /**
     * Converts the value. Here, <code>${foo.bar}</code> is replaced by the
     * value of the property <code>foo.bar</code> in this or the specified
     * property map or a system property.
     *
     * @param str original value
     * @param map another property map
     * @param map another map from where values can be taken while converting
     */
    private String convert(String str, PropertyMap map) {
        for (int h = 0; (h = str.indexOf('$', h)) != -1; ) {
            int j = str.indexOf('}', h);
            String key = str.substring(h + 2, j);
            String rep = containsKey(key) ? get(key).toString() : null;
            if (rep == null && map != null)
                rep = map.containsKey(key) ? map.get(key).toString() : null;
            if (rep == null)
                rep = System.getProperty(key);
            if (rep == null)
                rep = "";
            str = str.substring(0, h) + rep + str.substring(j + 1);
        }
        return str;
    }

    /**
     * Returns the String value of the specified key.
     *
     * @param key property key
     * @return String value of the specified key
     */
    public String getString(Object key) {
        return getString(key, null);
    }

    /**
     * Returns the String value of the specified key, using another map from
     * where values can be taken while converting.
     *
     * @param key property key
     * @param map another map from where values can be taken while converting
     * @return String value of the specified key
     */
    public String getString(Object key, PropertyMap map) {
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof String)
            return convert((String) value, map);
        return convert(value.toString(), map);
    }

    /**
     * Returns the specified String value, using another map from where values
     * can be taken while converting.
     *
     * @param value value
     * @param map   another map from where values can be taken while converting
     * @return converted String value
     */
    public String convertString(Object value, PropertyMap map) {
        if (value == null)
            return null;
        if (value instanceof String)
            return convert((String) value, map);
        return convert(value.toString(), map);
    }

    /**
     * Sets the String value of the specified key.
     *
     * @param key   property key
     * @param value String value of the specified key
     */
    public void setString(Object key, String value) {
        set(key, value);
    }

    /**
     * Returns the long value of the specified key.
     *
     * @param key property key
     * @return long value of the specified key
     */
    public long getLong(Object key) {
        Object value = get(key);
        if (value == null)
            return 0;
        if (value instanceof Long)
            return (Long) value;
        return Long.parseLong(convert(value.toString(), null));
    }

    /**
     * Sets the long value of the specified key.
     *
     * @param key   property key
     * @param value long value of the specified key
     */
    public void setLong(Object key, long value) {
        put(key, value);
    }

    /**
     * Returns the int value of the specified key.
     *
     * @param key property key
     * @return int value of the specified key
     */
    public int getInt(Object key) {
        Object value = get(key);
        if (value == null)
            return 0;
        if (value instanceof Integer)
            return (Integer) value;
        return Integer.parseInt(convert(value.toString(), null));
    }

    /**
     * Sets the int value of the specified key.
     *
     * @param key   property key
     * @param value int value of the specified key
     */
    public void setInt(Object key, int value) {
        put(key, value);
    }

    /**
     * Returns the double value of the specified key.
     *
     * @param key property key
     * @return double value of the specified key
     */
    public double getDouble(Object key) {
        Object value = get(key);
        if (value == null)
            return 0;
        if (value instanceof Double)
            return (Double) value;
        return Double.parseDouble(convert(value.toString(), null));
    }

    /**
     * Sets the double value of the specified key.
     *
     * @param key   property key
     * @param value double value of the specified key
     */
    public void setDouble(Object key, double value) {
        put(key, value);
    }

    /**
     * Returns the boolean value of the specified key.
     *
     * @param key property key
     * @return int value of the specified key
     */
    public boolean getBoolean(Object key) {
        Object value = get(key);
        if (value == null)
            return false;
        if (value instanceof Boolean)
            return (Boolean) value;
        return Boolean.parseBoolean(convert(value.toString(), null));
    }

    /**
     * Sets the boolean value of the specified key.
     *
     * @param key   property key
     * @param value boolean value of the specified key
     */
    public void setBoolean(Object key, boolean value) {
        put(key, value);
    }

    /**
     * Returns the key with the maximum double value.
     *
     * @return key with the maximum double value
     */
    public Object getMaxKey() {
        double max = 0;
        Object maxKey = null;
        boolean first = true;
        for (Object key : keySet()) {
            double value = getDouble(key);
            if (first) {
                max = value;
                maxKey = key;
                first = false;
            } else if (value > max) {
                max = value;
                maxKey = key;
            }
        }
        return maxKey;
    }

    /**
     * Returns the sum of all values in this property map, considered as double
     * values.
     *
     * @return sum of all values
     */
    public double getSum() {
        double sum = 0;
        for (Object key : keySet()) {
            sum += getDouble(key);
        }
        return sum;
    }

    /**
     * Returns the sum of all values in this property map, considered as double
     * values.
     *
     * @return sum of all values
     */
    public double getLength() {
        return getSum();
    }

    /**
     * Normalises the length of this property map, i.e. that afterwards the sum
     * of all values in this property map is one.
     */
    public void normalizeLength() {
        double sum = getSum();
        if (sum > 0)
            divDouble(sum);
    }

    /**
     * Removes all entries where the value is close to zero (considered as a
     * double).
     */
    public void removeCloseToZero() {
        Set deleteKeys = new HashSet();
        for (Object key : keySet()) {
            if (Math.abs(getDouble(key)) < EPSILON)
                deleteKeys.add(key);
        }
        for (Object key : deleteKeys) {
            remove(key);
        }
    }

    /**
     * Remove all entries where the value equals zero (considered as a double).
     */
    public void removeZero() {
        Set deleteKeys = new HashSet();
        for (Object key : keySet()) {
            if (getDouble(key) == 0)
                deleteKeys.add(key);
        }
        for (Object key : deleteKeys) {
            remove(key);
        }
    }

    /**
     * Applies the specified function on every value (considered as a double),
     * and replaces the original value.
     *
     * @param func function to be applied
     */
    public void apply(Function func) {
        for (Object key : keySet()) {
            setDouble(key, func.apply(getDouble(key)));
        }
    }

    /**
     * Returns the maximum double value in this property map.
     *
     * @return maximum double value in this property map
     */
    public double getMaxDouble() {
        double max = 0;
        boolean first = true;
        for (Object key : keySet()) {
            double value = getDouble(key);
            max = first ? value : Math.max(value, max);
            first = false;
        }
        return max;
    }

    /**
     * Returns the minimum double value in this property map.
     *
     * @return minimum double value in this property map
     */
    public double getMinDouble() {
        double min = 0;
        boolean first = true;
        for (Object key : keySet()) {
            double value = getDouble(key);
            min = first ? value : Math.min(value, min);
            first = false;
        }
        return min;
    }

    /**
     * Inits the property map by setting the value of every key in the specified
     * collection to the specified initial double value.
     *
     * @param keys keys for initialisation
     * @param init initial double value
     */
    public void initDouble(Collection keys, double init) {
        for (Object key : keys) {
            setDouble(key, init);
        }
    }

    /**
     * Divides each value (considered as an integer) by the specified number.
     *
     * @param i number
     */
    public void divInt(int i) {
        for (Object key : keySet()) {
            divInt(key, i);
        }
    }

    /**
     * Subtracts the specified number from each value (considered as an
     * integer).
     *
     * @param i number
     */
    public void subInt(int i) {
        for (Object key : keySet()) {
            subInt(key, i);
        }
    }

    /**
     * Multiplies the specified number with each value (considered as an
     * integer).
     *
     * @param i number
     */
    public void multInt(int i) {
        for (Object key : keySet()) {
            multInt(key, i);
        }
    }

    /**
     * Adds the specified number to each value (considered as an integer).
     *
     * @param i number
     */
    public void addInt(int i) {
        for (Object key : keySet()) {
            addInt(key, i);
        }
    }

    /**
     * Divides each value (considered as a double) by the specified number.
     *
     * @param d number
     */
    public void divDouble(double d) {
        for (Object key : keySet()) {
            divDouble(key, d);
        }
    }

    /**
     * Subtracts the specified number from each value (considered as a double).
     *
     * @param d number
     */
    public void subDouble(double d) {
        for (Object key : keySet()) {
            subDouble(key, d);
        }
    }

    /**
     * Multiplies the specified number with each value (considered as a double).
     *
     * @param d number
     */
    public void multDouble(double d) {
        for (Object key : keySet()) {
            multDouble(key, d);
        }
    }

    /**
     * Adds the specified number to each value (considered as a double).
     *
     * @param d number
     */
    public void addDouble(double d) {
        for (Object key : keySet()) {
            addDouble(key, d);
        }
    }

    /**
     * Divides each value (as an integer) by the corresponding value in the
     * specified map, for each key occurring in the specified map.
     *
     * @param map property map as operand
     */
    public void divInt(PropertyMap map) {
        for (Object key : map.keySet()) {
            divInt(key, map.getInt(key));
        }
    }

    /**
     * Subtracts each value (as an integer) in the specified map from the
     * corresponding value in this map, for each key occurring in the specified
     * map.
     *
     * @param map property map as operand
     */
    public void subInt(PropertyMap map) {
        for (Object key : map.keySet()) {
            subInt(key, map.getInt(key));
        }
    }

    /**
     * Multiplies each value (as an integer) in the specified map with the
     * corresponding value in this map, for each key occurring in the specified
     * map.
     *
     * @param map property map as operand
     */
    public void multInt(PropertyMap map) {
        for (Object key : map.keySet()) {
            multInt(key, map.getInt(key));
        }
    }

    /**
     * Adds each value (as an integer) in the specified map to the corresponding
     * value in this map, for each key occurring in the specified map.
     *
     * @param map property map as operand
     */
    public void addInt(PropertyMap map) {
        for (Object key : map.keySet()) {
            addInt(key, map.getInt(key));
        }
    }

    /**
     * Divides each value (as a double) by the corresponding value in the
     * specified map, for each key occurring in the specified map.
     *
     * @param map property map as operand
     */
    public void divDouble(PropertyMap map) {
        for (Object key : map.keySet()) {
            divDouble(key, map.getDouble(key));
        }
    }

    /**
     * Subtracts each value (as a double) in the specified map from the
     * corresponding value in this map, for each key occurring in the specified
     * map.
     *
     * @param map property map as operand
     */
    public void subDouble(PropertyMap map) {
        for (Object key : map.keySet()) {
            subDouble(key, map.getDouble(key));
        }
    }

    /**
     * Multiplies each value (as a double) in the specified map with the
     * corresponding value in this map, for each key occurring in the specified
     * map.
     *
     * @param map property map as operand
     */
    public void multDouble(PropertyMap map) {
        for (Object key : map.keySet()) {
            multDouble(key, map.getDouble(key));
        }
    }

    /**
     * Multiplies each value (as a double) in the specified map with the
     * corresponding value in this map, for each key occurring in the specified
     * map. If the boolean specified value is set to true, sets the value to one
     * if no entries exists so far.
     *
     * @param map property map as operand Multiplies every value (considered as
     *            a double) with the specified
     * @param b   if true, sets the value to one if no entries exists so far
     */
    public void multDouble(PropertyMap map, boolean b) {
        for (Object key : map.keySet()) {
            if (b && !containsKey(key))
                setDouble(key, 1);
            multDouble(key, map.getDouble(key));
        }
    }

    /**
     * Adds each value (as a double) in the specified map to the corresponding
     * value in this map, for each key occurring in the specified map.
     *
     * @param map property map as operand
     */
    public void addDouble(PropertyMap map) {
        for (Object key : map.keySet()) {
            addDouble(key, map.getDouble(key));
        }
    }

    /**
     * Increments the value (considered as an integer) corresponding to the
     * specified key by one.
     *
     * @param key key
     */
    public void inc(Object key) {
        addInt(key, 1);
    }

    /**
     * Divides the value (considered as an integer) for the specified key by the
     * specified number.
     *
     * @param key key
     * @param i   number
     */
    public void divInt(Object key, int i) {
        setInt(key, getInt(key) / i);
    }

    /**
     * Subtracts the specified number from the value (as an integer) for the
     * specified key.
     *
     * @param key key
     * @param i   number
     */
    public void subInt(Object key, int i) {
        setInt(key, getInt(key) - i);
    }

    /**
     * Multiplies the specified number with the value (as an integer) for the
     * specified key.
     *
     * @param key key
     * @param i   number
     */
    public void multInt(Object key, int i) {
        setInt(key, getInt(key) * i);
    }

    /**
     * Adds the specified number to the value (as an integer) for the specified
     * key.
     *
     * @param key key
     * @param i   number
     */
    public void addInt(Object key, int i) {
        setInt(key, getInt(key) + i);
    }

    /**
     * Divides the value (considered as a double) for the specified key by the
     * specified number.
     *
     * @param key key
     * @param d   number
     */
    public void divDouble(Object key, double d) {
        setDouble(key, getDouble(key) / d);
    }

    /**
     * Subtracts the specified number from the value (as a double) for the
     * specified key.
     *
     * @param key key
     * @param d   number
     */
    public void subDouble(Object key, double d) {
        setDouble(key, getDouble(key) - d);
    }

    /**
     * Multiplies the specified number with the value (as a double) for the
     * specified key.
     *
     * @param key key
     * @param d   number
     */
    public void multDouble(Object key, double d) {
        setDouble(key, getDouble(key) * d);
    }

    /**
     * Adds the specified number to the value (as a double) for the specified
     * key.
     *
     * @param key key
     * @param d   number
     */
    public void addDouble(Object key, double d) {
        setDouble(key, getDouble(key) + d);
    }

}