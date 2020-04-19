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

// $Id: StreamPropertyMap.java,v 1.15 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.util;

import java.io.*;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A PropertyMap which can be loaded from and saved into a stream. The format is
 * nearly the same as for <code>java.util.Properties</code> (which means it
 * consists of lines <code>key=value<code>, and lines starting with
 * a hash are ignored), only colons are not escaped (altough files with an
 * escaped colon can be read).<p>
 * <p>
 * Instances can either use a HashMap or any other Map for storing the items
 * loaded from the stream.<p>
 * <p>
 * Changes to this map are directly written into a stream.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.15 $, $Date: 2005/03/14 17:33:13 $
 * @since 2003-06-20
 */
public abstract class StreamPropertyMap extends DelegatedPropertyMap {

    /**
     * Starting header (may be null).
     */
    protected String header;

    /**
     * Creates a new instance without loading its properties.
     *
     * @param header starting header (may be null)
     */
    public StreamPropertyMap(String header) {
        super(new LinkedHashMap());
        this.header = header;
    }

    /**
     * Creates a new instance without loading its properties.
     *
     * @param map    to which calls are delegated
     * @param header starting header (may be null)
     */
    public StreamPropertyMap(Map map, String header) {
        super(map);
        this.header = header;
    }

    /**
     * Creates a new instance and loads the properties given by the array of
     * URLs.
     *
     * @param urls array of URLs from which the properties are loaded
     */
    public StreamPropertyMap(URL[] urls) {
        super(new LinkedHashMap());
        for (URL url : urls) add(url);
    }

    /**
     * Creates a new instance and loads the properties given by the array of
     * readers.
     *
     * @param streams array of readers from which the properties are loaded
     */
    public StreamPropertyMap(Reader[] streams) {
        super(new LinkedHashMap());
        for (Reader stream : streams) add(stream);
    }

    /**
     * Creates a new instance without loading its properties.
     *
     * @param header   starting header (may be null)
     * @param multiple values if true, multiple values can be hold for a key
     */
    public StreamPropertyMap(String header, boolean multiple) {
        super(new LinkedHashMap(), multiple);
        this.header = header;
    }

    /**
     * Creates a new instance without loading its properties.
     *
     * @param map      to which calls are delegated
     * @param header   starting header (may be null)
     * @param multiple values if true, multiple values can be hold for a key
     */
    public StreamPropertyMap(Map map, String header, boolean multiple) {
        super(map, multiple);
        this.header = header;
    }

    /**
     * Creates a new instance and loads the properties given by the array of
     * URLs.
     *
     * @param urls     array of URLs from which the properties are loaded
     * @param multiple multiple values if true, multiple values can be hold for a key
     */
    public StreamPropertyMap(URL[] urls, boolean multiple) {
        super(new LinkedHashMap(), multiple);
        for (URL url : urls) add(url);
    }

    /**
     * Creates a new instance and loads the properties given by the array of
     * readers.
     *
     * @param streams  array of readers from which the properties are loaded
     * @param multiple multiple values if true, multiple values can be hold for a key
     */
    public StreamPropertyMap(Reader[] streams, boolean multiple) {
        super(new LinkedHashMap(), multiple);
        for (Reader stream : streams) add(stream);
    }

    /**
     * Adds the map from the url.
     *
     * @param url url from where to read
     */
    public void add(URL url) {
        System.out.println("StreamProperty" + url);
        try {
            add(new URLISOReader(url));
        } catch (Exception e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Adds the map from the sream.
     *
     * @param reader stream from where to read
     */
    public void add(Reader reader) {
        try {
            if (reader != null) {
                BufferedReader in = new BufferedReader(reader);
                //while (!in.ready()); // wait if not yet accessible
                // the previous line is unfortunately necessary
                for (String line; (line = in.readLine()) != null; ) {
                    if (line.trim().length() == 0 || line.charAt(0) == '#')
                        continue;
                    int i = line.indexOf('=');
                    if (i != -1) {
                        String pname = line.substring(0, i);
                        String pvalue = line.substring(i + 1);
                        // backward compatibility
                        pname = StringUtilities.replace(pname, "\\:", ":");
                        pvalue = StringUtilities.replace(pvalue, "\\:", ":");
                        putNoChange(pname, pvalue);
                    }
                }
                in.close();
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Writes this map into a stream, if that is not <code>null</code>.
     *
     * @param writer stream into which this map is written.
     */
    public void write(Writer writer) {
        try {
            if (writer != null) {
                PrintWriter out = new PrintWriter(new BufferedWriter(writer));
                if (header != null)
                    out.println("# " + header);
                Set keys = new LinkedHashSet(keySet());
                keys.removeAll(getInitKeys());
                for (Object key : keys) {
                    Object value = get(key);
                    out.println(key + "=" + value);
                }
                out.close();
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Loads this map from a stream.
     */
    public void load() {
        try {
            add(getReader());
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Saves this map into a stream.
     * <p>
     * <p>
     * The map is written to the writer returned by <code>getWriter()</code>,
     * if that method does not return <code>null</code>
     */
    public void save() {
        try {
            write(getWriter());
        } catch (IOException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Saves the properties in the config file.
     */
    protected void changed() {
        save();
    }

    /**
     * Returns a Reader from which keys and values are read.
     * <p>
     * <p>
     * Subclasses have to overwrite this method.
     *
     * @return Reader from which keys and values are read
     */
    protected abstract Reader getReader() throws IOException;

    /**
     * Returns a Writer to which keys and values are written.
     * <p>
     * <p>
     * Subclasses have to overwrite this method.
     *
     * @return Reader to which keys and values are written
     */
    protected abstract Writer getWriter() throws IOException;

}