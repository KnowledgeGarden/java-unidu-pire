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


// $Id: NormalDistributionSource.java,v 1.5 2005/03/19 16:20:00 nottelma Exp $
package de.unidu.is.gnuplot;

import de.unidu.is.statistics.NormalDistribution;

/**
 * A class describing a normal distrbution for the gnuplot <code>plot</code>.
 *
 * @author Henriki Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/03/19 16:20:00 $
 * @since 2003-07-27
 */
public class NormalDistributionSource extends Source {

    /**
     * The normal distribution.
     */
    private final NormalDistribution distribution;

    /**
     * Creates a new instance.
     *
     * @param title        title of this curve, or <code>null</code>
     * @param distribution normal distribution
     */
    public NormalDistributionSource(
            String title,
            NormalDistribution distribution) {
        super(title);
        this.distribution = distribution;
    }

    /**
     * Returns a gnuplot expression for the corresponding normal
     * distribution for the gnuplot <code>plot</code> command.
     *
     * @return gnuplot expression defining the normal distribution
     */
    public String getCommand() {
        double expectation = distribution.getExpectation();
        double variance = distribution.getVariance();
        return "1/sqrt(2*pi*"
                + variance
                + ")*exp(-(x-"
                + expectation
                + ")*(x-"
                + expectation
                + ")/(2*"
                + variance
                + "))";
    }

}
