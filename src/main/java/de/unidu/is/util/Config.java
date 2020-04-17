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

// $Id: Config.java,v 1.10 2005/03/19 16:59:20 nottelma Exp $
package de.unidu.is.util;

import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * A config file as an extension to PropertyMap, allowing for
 * automatically loading from an existing file in directory conf/config
 * (relative to a path in the system property "java.class.path") and saving in
 * it. It is allowed that a key exists multiple times.
 * <p>
 * 
 * A property <code>foo.bar</code> which is already read from the
 * configuration file can be referenced by <code>${foo.bar}</code>.
 * Currently, the property <code>mind_home</code> is set to the base directory
 * of the system (the directory containing the configuration directory).
 * 
 * @author Henrik Nottelmann
 * @since 2001-08-02
 * @version $Revision: 1.10 $, $Date: 2005/03/19 16:59:20 $
 */
public class Config {

	/**
	 * Property map storing the configuration data.
	 */
	private static FilePropertyMap map;

	/**
	 * Semaphore for synchronisation.
	 */
	private static Object sem = new Object();

	/**
	 * Inits the configuration class, and loads all config files "conf/config"
	 * which can be found if the property map is not set yet. In addition, the
	 * entries from the specified map are added to the config as initial keys.
	 * 
	 * @param init
	 *                   map with initial keys
	 */
	public static void init(Map init) {
		init();
		map.init(init);
	}

	/**
	 * Inits the configuration class, and loads all config files "conf/config"
	 * which can be found if the property map is not set yet.
	 */
	private static void init() {
		if (map == null) {
			synchronized (sem) {
				if (map == null)
					map = new FilePropertyMap(SystemUtilities
							.getConfURLs("config"));
			}
		}
	}

	/**
	 * Writes the config to the specified writer.
	 * 
	 * @param writer
	 *                   writer for the config
	 */
	public static void write(Writer writer) {
		map.write(writer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Hashtable#clear()
	 */
	public static void clear() {
		init();
		map.clear();
	}

	/**
	 * Adds the content of the specified file to the config.
	 * 
	 * @param file
	 *                   file to be added
	 */
	public static void add(File file) {
		init();
		map.add(file);
	}

	/**
	 * Adds the content of the specified reader to the config.
	 * 
	 * @param reader
	 *                   reader to be added
	 */
	public static void add(Reader reader) {
		init();
		map.add(reader);
	}

	/**
	 * Adds the content of the specified URL to the config.
	 * 
	 * @param url
	 *                   URL to be added
	 */
	public static void add(URL url) {
		init();
		map.add(url);
	}

	/**
	 * Retrieves a value from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @return value or null if the key is not stored
	 */
	public static Object get(Object key) {
		init();
		return map.get(key);
	}

	/**
	 * Retrieves a value as a boolean from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @return value or null if the key is not stored
	 */
	public static boolean getBoolean(Object key) {
		init();
		return map.getBoolean(key);
	}

	/**
	 * Retrieves a value as a double from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @return value or null if the key is not stored
	 */
	public static double getDouble(Object key) {
		init();
		return map.getDouble(key);
	}

	/**
	 * Retrieves a value as an integer from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @return value or null if the key is not stored
	 */
	public static int getInt(Object key) {
		init();
		return map.getInt(key);
	}

	/**
	 * Retrieves a value as a long from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @return value or null if the key is not stored
	 */
	public static long getLong(Object key) {
		init();
		return map.getLong(key);
	}

	/**
	 * Retrieves a value as a string from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @return value or null if the key is not stored
	 */
	public static String getString(Object key) {
		init();
		return map.getString(key);
	}

	/**
	 * Retrieves a value as a string from the config specified by its key.
	 * 
	 * @param key
	 *                   key to be retrieved
	 * @param map
	 *                   map which is used if the key is not found in the config
	 * @return value or null if the key is not stored
	 */
	public static String getString(Object key, PropertyMap map) {
		init();
		return map.getString(key, map);
	}

	/**
	 * Adds an entry to this config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 */
	public static void set(Object key, String value) {
		init();
		map.set(key, value);
	}

	/**
	 * Adds an entry as a boolean to this config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 */
	public static void setBoolean(Object key, boolean value) {
		init();
		map.setBoolean(key, value);
	}

	/**
	 * Adds an entry as a double to this config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 */
	public static void setDouble(Object key, double value) {
		init();
		map.setDouble(key, value);
	}

	/**
	 * Adds an entry as an integer to this config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 */
	public static void setInt(Object key, int value) {
		init();
		map.setInt(key, value);
	}

	/**
	 * Adds an entry as a long to this config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 */
	public static void setLong(Object key, long value) {
		init();
		map.setLong(key, value);
	}

	/**
	 * Adds an entry as a string to this config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 */
	public static void setString(Object key, String value) {
		init();
		map.setString(key, value);
	}

	/**
	 * Adds an entry to the config.
	 * 
	 * @param key
	 *                   key to be added
	 * @param value
	 *                   value to be added
	 * @return the object previously stored for the specified key
	 */
	public static Object put(Object key, Object value) {
		init();
		return map.put(key, value);
	}

	/**
	 * Adds all entries in the specified map to the config
	 * 
	 * @param m
	 *                   map which will be added to the config
	 */
	public static void putAll(Map m) {
		init();
		map.putAll(m);
	}

	/**
	 * Removes the entry defined by the specified key.
	 * 
	 * @param key
	 *                   key to be removed
	 * @return the object previously stored for the specified key
	 */
	public static Object remove(Object key) {
		init();
		return map.remove(key);
	}

	/**
	 * Tests if the config contains the specified key.
	 * 
	 * @param key
	 *                   key to be tested
	 * @return true iff the config contains the specified key
	 */
	public static boolean containsKey(Object key) {
		init();
		return map.containsKey(key);
	}

	/**
	 * Tests if the config contains the specified value.
	 * 
	 * @param value
	 *                   value to be tested
	 * @return true iff the config contains the specified value
	 */
	public static boolean containsValue(Object value) {
		init();
		return map.containsValue(value);
	}

	/**
	 * Tests if this config is empty.
	 * 
	 * @return true iff this config is empty
	 */
	public static boolean isEmpty() {
		init();
		return map.isEmpty();
	}

	/**
	 * Returns the number of entries in the config
	 * 
	 * @return number of entries in the config
	 */
	public static int size() {
		init();
		return map.size();
	}

	/**
	 * Returns the keys in the config.
	 * 
	 * @return keys in the config
	 */
	public static Set keySet() {
		init();
		return map.keySet();
	}

	/**
	 * Returns the values in the config.
	 * 
	 * @return values in the config
	 */
	public static Collection values() {
		init();
		return map.values();
	}

	/**
	 * Returns the complete map.
	 * 
	 * @return complete map
	 */
	public static PropertyMap getMap() {
		init();
		return map;
	}

}