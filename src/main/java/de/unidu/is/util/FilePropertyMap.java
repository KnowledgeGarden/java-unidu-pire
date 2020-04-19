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


// $Id: FilePropertyMap.java,v 1.15 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.util;

import java.io.*;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A PropertyMap which can be loaded from and saved into a file. The
 * format is nearly the same as for <code>java.util.Properties</code> (which
 * means it consists of lines <code>key=value<code>, and lines starting with
 * a hash are ignored), only colons are not escaped (altough files with an
 * escaped colon can be read).<p>
 * <p>
 * Instances can either use a HashMap or any other Map for storing the items
 * loaded from the file.<p>
 * <p>
 * This class can also be used for merging multiple files (given by file names,
 * URLs or input streams).<p>
 * <p>
 * Changes to this map are directly written into the file if a single one is
 * specified (i.e., this map is not used for merging files).
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.15 $, $Date: 2005/03/14 17:33:13 $
 * @since 2003-06-20
 */
public class FilePropertyMap extends StreamPropertyMap {

    /**
     * A PropertyMap backed by the config file.
     */
    private static PropertyMap configuration;
    /**
     * Filename of this FilePropertyMap.
     */
    protected String filename;

    /**
     * Creates a new instance and loads its properties.
     *
     * @param name absolute file name
     */
    public FilePropertyMap(String name) {
        this(name, null);
    }

    /**
     * Creates a new instance and loads the properties given by the array
     * of URLs.
     *
     * @param urls array of URLs from which the properties are loaded
     */
    public FilePropertyMap(URL[] urls) {
        super(urls);
    }

    /**
     * Creates a new instance and loads the properties given by the array
     * of readers.
     *
     * @param streams array of readers from which the properties are loaded
     */
    public FilePropertyMap(Reader[] streams) {
        super(streams);
    }

    /**
     * Creates a new instance and loads the properties given by the URL
     *
     * @param url URL from which the properties are loaded
     */
    public FilePropertyMap(URL url) {
        super(new URL[]{url});
    }

    /**
     * Creates a new instance and loads the properties given by the reader.
     *
     * @param stream reader from which the properties are loaded
     */
    public FilePropertyMap(Reader stream) {
        super(new Reader[]{stream});
    }

    /**
     * Creates a new instance and loads its properties.
     *
     * @param name   absolute file name
     * @param map    to which calls are delegated
     * @param header file header (may be null)
     */
    public FilePropertyMap(String name, Map map, String header) {
        super(map, header);
        this.filename = name;
        load();
    }

    /**
     * Creates a new instance and loads its properties.
     *
     * @param name   absolute file name
     * @param header file header (may be null)
     */
    public FilePropertyMap(String name, String header) {
        this(name, new LinkedHashMap(), header);
    }

    /**
     * Creates a new instance and loads its properties.
     *
     * @param name     absolute file name
     * @param multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(String name, boolean multiple) {
        this(name, null, multiple);
    }

    /**
     * Creates a new instance and loads the properties given by the array
     * of URLs.
     *
     * @param urls     array of URLs from which the properties are loaded
     * @param multiple multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(URL[] urls, boolean multiple) {
        super(urls, multiple);
    }

    /**
     * Creates a new instance and loads the properties given by the array
     * of readers.
     *
     * @param streams  array of readers from which the properties are loaded
     * @param multiple multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(Reader[] streams, boolean multiple) {
        super(streams, multiple);
    }

    /**
     * Creates a new instance and loads the properties given by the URL.
     *
     * @param url      URL from which the properties are loaded
     * @param multiple multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(URL url, boolean multiple) {
        super(new URL[]{url}, multiple);
    }

    /**
     * Creates a new instance and loads the properties given by the reader.
     *
     * @param stream   reader from which the properties are loaded
     * @param multiple multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(Reader stream, boolean multiple) {
        super(new Reader[]{stream}, multiple);
    }

    /**
     * Creates a new instance and loads its properties.
     *
     * @param name     absolute file name
     * @param map      to which calls are delegated
     * @param header   file header (may be null)
     * @param multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(String name, Map map, String header, boolean multiple) {
        super(map, header, multiple);
        this.filename = name;
        load();
    }

    /**
     * Creates a new instance and loads its properties.
     *
     * @param name     absolute file name
     * @param header   file header (may be null)
     * @param multiple values if true, multiple values can be hold for a key
     */
    public FilePropertyMap(String name, String header, boolean multiple) {
        this(name, new LinkedHashMap(), header, multiple);
    }

    /**
     * Returns a PropertyMap backed by the config file.
     *
     * @return PropertyMap backed by the config file
     * @deprecated Use de.unidu.is.util.Config instead.
     */
    public static PropertyMap getConfiguration() {
        return Config.getMap();
    }

    /**
     * Adds the map from the file.
     *
     * @param file file from where to read
     */
    public void add(String file) {
        try {
            add(new FileReader(file));
        } catch (FileNotFoundException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Adds the map from the file.
     *
     * @param file file from where to read
     */
    public void add(File file) {
        try {
            add(new FileReader(file));
        } catch (FileNotFoundException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Returns a Reader from which keys and values are read.
     *
     * @return Reader from which keys and values are read
     */
    protected Reader getReader() throws IOException {
        return new FileISOReader(filename);
    }

    /**
     * Returns a Writer to which keys and values are written.<p>
     * <p>
     * This method returns <code>null</code> if no filename is given.
     * This will result in the fact the nothing is saved.
     *
     * @return Reader to which keys and values are written
     */
    protected Writer getWriter() throws IOException {
        if (filename == null)
            return null;
        return new FileISOWriter(filename);
    }

}
