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


// $Id: CostParameters.java,v 1.6 2005/02/28 22:27:56 nottelma Exp $
package de.unidu.is.rs;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Cost parameters of a query.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:56 $
 * @since 2001-04-20
 */
public class CostParameters extends HashMap {

    /**
     * Returns a value of a parameter.
     *
     * @param name name of the parameter
     * @return value of the parameter or 0, if the parameter does not exist
     */
    public double getParameter(String name) {
        Double ret = (Double) get(name);
        return ret == null ? 0 : ret;
    }

    /**
     * Returns a value of a parameter as an integer.
     *
     * @param name name of the parameter
     * @return value of the parameter or 0, if the parameter does not exist
     */
    public int getInt(String name) {
        return (int) (getParameter(name) + 0.5);
    }

    /**
     * Tests if there is a parameter with the specified name
     *
     * @return true if there is a parameter with the specified name
     */
    public boolean hasParameter(String name) {
        Double ret = (Double) get(name);
        return ret != null;
    }

    /**
     * Sets a parameter.
     *
     * @param name  name of the parameter
     * @param value value of the parameter
     */
    public void setParameter(String name, double value) {
        put(name, value);
    }

    /**
     * Returns a text representation of the current object.
     *
     * @return text representation of the current object
     */
    public String toString() {
        return "CostParameters" + super.toString();
    }

    /**
     * Returns an iterator over all parameter names.
     *
     * @return iterator over all parameter names
     */
    public Iterator parameterNames() {
        return keySet().iterator();
    }

}
