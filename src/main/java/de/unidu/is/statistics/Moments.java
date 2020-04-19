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


// $Id: Moments.java,v 1.8 2005/03/19 16:16:30 nottelma Exp $
package de.unidu.is.statistics;

import java.util.List;

/**
 * A class for encapsulating and computing statistical moments (expectation,
 * variance/standard deviation).
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/03/19 16:16:30 $
 * @since 2003-09-23
 */
public class Moments {

    /**
     * Sample values.
     */
    private List values;

    /**
     * Expectation.
     */
    private double expectation;

    /**
     * Variance.
     */
    private double variance;

    /**
     * Creates a new, empty instance.
     */
    public Moments() {
    }

    /**
     * Creates a new instance and sets the moments directly.
     *
     * @param exp expectation
     * @param var variance
     */
    public Moments(double exp, double var) {
        this.values = null;
        this.expectation = exp;
        this.variance = var;
    }

    /**
     * Creates a new instance and computes the moments from the sample values.
     *
     * @param values sample values
     */
    public Moments(List values) {
        this.values = values;
        expectation = 0;
        for (int i = 0; i < size(); i++)
            expectation += get(i);
        expectation /= size();
        variance = 0;
        for (int i = 0; i < values.size(); i++)
            variance += Math.pow(get(i) - expectation, 2);
        variance /= size() - 1;
    }

    /**
     * Returns the standard deviation.
     *
     * @return standard deviation
     */
    public double getStandardDeviation() {
        return Math.sqrt(variance);
    }

    /**
     * Returns an array with the moments.
     *
     * @return array with the moments (0: expectation, 1: variance)
     */
    public double[] getMoments() {
        return new double[]{expectation, variance};
    }

    /**
     * Returns the number of sample values.
     *
     * @return number of sample values
     */
    public int size() {
        return values == null ? 0 : values.size();
    }

    /**
     * Returns the sample value at the specified index.
     *
     * @param index index
     * @return sample value at the specified index
     */
    public double get(int index) {
        return (Double) values.get(index);
    }

    /**
     * Returns the expectation.
     *
     * @return expectation.
     */
    public double getExpectation() {
        return expectation;
    }

    /**
     * Sets the expectation.
     *
     * @param expectation expectation to set.
     */
    public void setExpectation(double expectation) {
        this.expectation = expectation;
    }

    /**
     * Returns the sample values.
     *
     * @return values.
     */
    public List getValues() {
        return values;
    }

    /**
     * Sets the sample values.
     *
     * @param values values to set.
     */
    public void setValues(List values) {
        this.values = values;
    }

    /**
     * Returns the variance.
     *
     * @return variance.
     */
    public double getVariance() {
        return variance;
    }

    /**
     * Sets the variance.
     *
     * @param variance variance to set.
     */
    public void setVariance(double variance) {
        this.variance = variance;
    }
}
