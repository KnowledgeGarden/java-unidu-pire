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


// $Id: AbstractExpression.java,v 1.4 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.expressions;

import java.util.Map;

/**
 * An abstract class for expressions.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:18 $
 * @since 2003-11-08
 */
public class AbstractExpression implements Expression {

    /**
     * Performs an substitution for variables, e.g. for each key
     * <code>variable</code> in the specified binding, all occurences of
     * <code>${key}</code> are replaced by the corresponding value in the
     * map.<p>
     * <p>
     * Here, the same untouched expression is returned. Subclasses can
     * overwrite this behaviour.
     *
     * @param binding variable binding
     * @return expression after substitution
     */
    public Expression substitute(Map binding) {
        return this;
    }


    /**
     * Returns a string representation for this expression which can be
     * used as a template in an SQL statement.
     *
     * @return template for SQL statements
     */
    public String getSQLTemplate() {
        return toString();
    }

}
