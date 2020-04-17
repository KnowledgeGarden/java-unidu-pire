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

// $Id: DelegatedPropertyMap.java,v 1.11 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class delegates all calls to another map. This map implementation can be
 * initialised by another map. The keys of this initialisation map are stored
 * separately, so that these entries can be distinguished from the "ordinary"
 * entries lateron.
 * 
 * @author Henrik Nottelmann
 * @since 2003-06-20
 * @version $Revision: 1.11 $, $Date: 2005/03/14 17:33:13 $
 */
public class DelegatedPropertyMap extends PropertyMap {

	/**
	 * Map implementation to which all calls are delegated.
	 */
	protected Map map;

	/**
	 * Set containing initial keys.
	 */
	protected Set initKeys;

	/**
	 * Creates a new instance.
	 * 
	 * @param map
	 *                   to which calls are delegated
	 */
	public DelegatedPropertyMap(Map map) {
		this(map, false);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param map
	 *                   to which calls are delegated
	 * @param multipleValues
	 *                   values if true, multiple values can be hold for a key
	 */
	public DelegatedPropertyMap(Map map, boolean multipleValues) {
		super(multipleValues);
		this.map = map == null ? Collections.EMPTY_MAP : map;
		initKeys = new HashSet();
	}

	/**
	 * Inits the map by setting the initial keys and values. These values are
	 * not original entries and can be distinguished lateron.
	 * 
	 * @param init
	 *                   map for initialisation (may be null)
	 */
	public void init(Map init) {
		if (init != null)
			for (Iterator it = init.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				putInitial(key, init.get(key));
			}
	}

	/**
	 * Calls whenever the map is changed.
	 * <p>
	 * 
	 * Subclasses can override this method.
	 */
	protected void changed() {
	}

	/**
	 * Removes all entries.
	 */
	public void clear() {
		map.clear();
		changed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return values().contains(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet() {
		return map.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return map.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		if (isMultipleValues()) {
			List l = getAll(key);
			return l == null || l.isEmpty() ? null : l.get(0);
		}
		return map.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return map.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		return map.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		return put(key, value, true);
	}

	/**
	 * Puts the (key,vaue) pair into the map without calling the
	 * <code>changed()</code> method.
	 * 
	 * @param key
	 *                   key
	 * @param value
	 *                   value for the key
	 * @return previous value
	 */
	public Object putNoChange(Object key, Object value) {
		return put(key, value, false);
	}

	/**
	 * Puts the (key,vaue) pair into the map. If the specified boolean value is
	 * true, the <code>changed()</code> method is called.
	 * 
	 * @param key
	 *                   key
	 * @param value
	 *                   value for the key
	 * @param callChanged
	 *                   if true, calls <code>changed()</code>
	 * @return previous value
	 */
	private Object put(Object key, Object value, boolean callChanged) {
		if (isMultipleValues()) {
			List l = getAll(key);
			if (l == null) {
				l = new ArrayList();
				map.put(key, l);
			}
			Object ret = l.isEmpty() ? null : l.get(0);
			if (!l.contains(value))
				l.add(value);
			if (callChanged)
				changed();
			return ret;
		}
		Object ret = map.put(key, value);
		if (callChanged)
			changed();
		return ret;
	}

	/**
	 * Sets an initial value which can be distinguished from "ordinary" entries
	 * lateron.
	 * 
	 * @param key
	 *                   entry key
	 * @param value
	 *                   entry value
	 * @return previous value associated with specified key, or
	 *               <code>null</code> if no value is available
	 */
	public Object putInitial(Object key, Object value) {
		Object ret = put(key, value, false);
		initKeys.add(key);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map t) {
		if (isMultipleValues()) {
			for (Iterator it = t.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				put(key, t.get(key), false);
			}
		} else
			map.putAll(t);
		changed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		Object ret = map.remove(key);
		if (isMultipleValues()) {
			List l = (List) ret;
			ret = l == null || l.isEmpty() ? null : l.get(0);
		}
		changed();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	public int size() {
		return map.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return map.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection values() {
		if (isMultipleValues()) {
			List l = new LinkedList();
			for (Iterator it = keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				l.addAll(getAll(key));
			}
			return l;
		}
		return map.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.util.PropertyMap#getAll(java.lang.Object)
	 */
	public List getAll(Object key) {
		Object value = map.get(key);
		if (value == null)
			return null;
		if (value instanceof List)
			return (List) value;
		return Collections.singletonList(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unidu.is.util.PropertyMap#remove(java.lang.Object,
	 *           java.lang.Object)
	 * <<changed return type to boolean - park >>
	 */
	public boolean remove(Object key, Object value) {
		List l = getAll(key);
		if (l != null) {
			l.remove(value);
			return true;
		}
		return false;

	}

	/**
	 * Returns the map to which calls are delegated.
	 * 
	 * @return map to which calls are delegated
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Sets the map to which calls are delegated.
	 * 
	 * @param map
	 *                   map to which calls are delegated
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * Sets the initial keys.
	 * 
	 * @param initKeys
	 *                   initial keys
	 */
	public void setInitKeys(Set initKeys) {
		this.initKeys = initKeys;
	}

	/**
	 * Returns the initial keys.
	 * 
	 * @return set containing the initial keys
	 */
	public Set getInitKeys() {
		return initKeys;
	}
}