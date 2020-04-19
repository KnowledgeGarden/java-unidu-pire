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


// $Id: Learner.java,v 1.4 2005/02/21 17:29:19 huesselbeck Exp $
package de.unidu.is.learning;

import java.util.Map;

/**
 * An interface for learning parameters of a function.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:19 $
 * @since 2003-07-17
 */
public interface Learner {

    /**
     * Learns the specified variables of the specified function with
     * the given data points.
     *
     * @param x        array with x values
     * @param y        array with y values
     * @param function function
     * @param vars     variables to learn
     * @return map with variables and values
     */
    Map learn(
            double[] x,
            double[] y,
            String function,
            String[] vars);

    /**
     * Learns the specified variables of the specified function with
     * the given data points.<p>
     * <p>
     * Internally, the Linux/Unix tool <code>gnuplot</code> is used
     * for learning.
     *
     * @param filename  name of file with data points
     * @param separator string separating x and y values
     * @param function  function
     * @param vars      variables to learn
     * @return map with variables and values
     */
    Map learn(
            String filename,
            String separator,
            String function,
            String[] vars);

}
