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


// $Id: FileSource.java,v 1.6 2005/02/21 17:29:19 huesselbeck Exp $
package de.unidu.is.gnuplot;

import java.io.File;

/**
 * An class describing a single plot curve, defined by a data file for the
 * gnuplot <code>plot</code>.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:19 $
 * @since 2003-07-26
 */
public class FileSource extends Source {

    /**
     * The name (absolute or relative) of the data output file.
     */
    protected String filename;

    /**
     * Flag indicating if the curve has to be smoothed.
     */
    protected boolean nosmooth;

    /**
     * Creates a new instance.
     *
     * @param title    title of this curve, or <code>null</code>
     * @param filename name of the corresponding file with data points
     */
    public FileSource(String title, String filename) {
        super(title);
        this.filename = filename;
    }

    /**
     * Creates a new instance.
     *
     * @param title title of this curve, or <code>null</code>
     * @param file  corresponding file with data points
     */
    public FileSource(String title, File file) {
        this(title, file.toString());
    }

    /**
     * Returns the name (absolute or relative) of the data output file.
     *
     * @return name of the data output file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the name (absolute or relative) of the data output file.
     *
     * @param filename name of the data output file
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Tests whether the curve has to be smoothed.
     *
     * @return true if the curve has to be smoothed
     */
    public boolean isSmooth() {
        return !nosmooth;
    }

    /**
     * Specifies whether the curve has to be smoothed.
     *
     * @param smooth if true, the curve will be smoothed
     */
    public void setSmooth(boolean smooth) {
        nosmooth = !smooth;
    }

    /**
     * Creates the datafile and returns a gnuplot expression defining
     * the data file for the gnuplot <code>plot</code> command (with
     * <code>smooth unique</code> is set).
     *
     * @return gnuplot expression defining the function or data file
     */
    public String getCommand() {
        return "'" + filename + "'" + (nosmooth ? "" : " smooth unique");
    }

}
