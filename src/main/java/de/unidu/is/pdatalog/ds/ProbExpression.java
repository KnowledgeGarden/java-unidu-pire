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

// $Id: ProbExpression.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import de.unidu.is.expressions.AbstractExpression;
import de.unidu.is.expressions.Expression;

import java.util.Map;

/**
 * The expression representing the overall probability of facts derived by the
 * single, obtained by the independence assumption.
 *
 * @author nottelma
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 */
public class ProbExpression extends AbstractExpression {

    /**
     * Creates a new object.
     */
    public ProbExpression() {
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "PROB";
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return (o instanceof ProbExpression);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.plogics.Argument#replace(java.lang.String,
     *           java.lang.String)
     */
    public Expression substitute(Map binding) {
        return (Expression) binding.get(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return "PROB".hashCode();
    }

}