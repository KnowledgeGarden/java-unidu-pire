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


// $Id: Source.java,v 1.4 2005/02/21 17:29:19 huesselbeck Exp $
package de.unidu.is.gnuplot;

/**
 * An abstract class describing a single plot curve. Therefore, a
 * gnuplot expression defining the function or data file for the
 * gnuplot <code>plot</code> command has to be specified as well as
 * the "title" of the curve.<p>
 * <p>
 * Furthermore, the minimum and maximum value for the x-axis of this curve
 * can be specified (for the <code>mind.evalution.gnuplot.GnuPlot</code>
 * instance). By default, both values are <code>0</code>, thus they are
 * ignored.<p>
 * <p>
 * This class has to be subclassed for specific application areas.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:19 $
 * @since 2002-04-10
 */
public abstract class Source {

    /**
     * The minimum value for the x-axis (default is <code>0</code>).
     */
    protected double min;

    /**
     * The maximum value for the x-axis (default is <code>0</code>).
     */
    protected double max;

    /**
     * The title of this curve.
     */
    protected String title;

    /**
     * Creates a new, empty instance.
     */
    public Source() {
    }

    /**
     * Creates a new, empty instance and sets the title.
     *
     * @param title title of this plot
     */
    public Source(String title) {
        this.title = title;
    }

    /**
     * Returns a gnuplot expression defining the function or data file
     * for the gnuplot <code>plot</code> command.
     * <p>
     * This method has to be overridden in subclasses.
     *
     * @return gnuplot expression defining the function or data file
     */
    public abstract String getCommand();

    /**
     * Returns the title of the curve.
     *
     * @return title of the curve
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the minimum value for the x-axis.
     *
     * @return minimum value for the x-axis
     */
    public double getMin() {
        return min;
    }

    /**
     * Sets the minimum value for the x-axis.
     *
     * @param min minimum value for the x-axis
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Returns the maximum value for the x-axis.
     *
     * @return maximum value for the x-axis
     */
    public double getMax() {
        return max;
    }

    /**
     * Sets the maximum value for the x-axis.
     *
     * @param max maximum value for the x-axis
     */
    public void setMax(double max) {
        this.max = max;
    }

}
