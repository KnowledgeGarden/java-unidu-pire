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


// $Id: Distribution.java,v 1.6 2005/03/14 17:33:15 nottelma Exp $
package de.unidu.is.statistics;


/**
 * An abstract class modelling probabilistic distributions.<p>
 * <p>
 * This class has to be subclassed for specific distributions.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:15 $
 * @since 2002-04-09
 */
public abstract class Distribution {

    /**
     * Eliminates info about the distribution at zero. This is only useful
     * for discrete distributions with a high peak at zero, as then a
     * plot might look better.<p>
     * <p>
     * At the moment, this method does nothing. It may to be
     * overridden in subclasses.
     */
    public void eliminateZero() {
    }

    /**
     * Returns an array with actual values based on the underlying
     * distribution in decreasing order (as many values as possible).
     *
     * @param num number of items in total
     * @return array for the actual values
     */
    public double[] computeValues(int num) {
        double[] ret = new double[num];
        computeValues(num, ret);
        return ret;
    }

    /**
     * Computes actual values based on the underlying distribution when
     * having the specified number of items, and writes them in decreasing
     * order into the specified array (as many values as possible).<p>
     * <p>
     * This method has to be overridden in subclasses.
     *
     * @param num number of items in total
     * @param ret array for the actual values
     */
    public abstract void computeValues(int num, double[] ret);

}
