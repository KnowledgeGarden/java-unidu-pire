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


// $Id: LearnerFactory.java,v 1.7 2005/02/21 17:29:19 huesselbeck Exp $
package de.unidu.is.learning;

import de.unidu.is.gnuplot.GnuplotLearner;
import de.unidu.is.util.Config;
import de.unidu.is.util.Log;

/**
 * A factory returning Learner instances. With this method, it is fairly easy
 * to replace the actual learning class.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:19 $
 * @since 2003-07-17
 */
public class LearnerFactory {

    /**
     * Returns a new Learner instance.<p>
     * <p>
     * This method looks into the config file for the value for
     * <code>de.unidu.is.gnuplot.Learner</code> which is used as the name
     * of the class. If no entry is found, an instance of
     * <code>de.unidu.is.learning.Learner</code> is returned.
     *
     * @return instance of Learner
     */
    public static Learner newLearner() {
        try {
            String classname = Config.getString("de.unidu.is.learning.Learner");
            Class cl =
                    (classname == null || classname.length() == 0)
                            ? GnuplotLearner.class
                            : Class.forName(classname);
            Learner learner = (Learner) cl.newInstance();
            return learner;
        } catch (Exception ex) {
            Log.error(ex);
        }
        return null;
    }

}
