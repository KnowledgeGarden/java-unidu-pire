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


// $Id: ArgNExpression.java,v 1.4 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.expressions;

import de.unidu.is.util.StringUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An expression with an operator and a list of arguments, e.g. the sum of
 * expressions.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:18 $
 * @since 2003-10-27
 */

public class ArgNExpression extends AbstractExpression {

    /**
     * Expression operator.
     */
    protected final String op;

    /**
     * List of arguments of this expression.
     */
    protected final List args;

    /**
     * Creates a new expression object.
     *
     * @param op   expression operator
     * @param args list of arguments of this expression
     */
    public ArgNExpression(String op, List args) {
        this.op = op;
        this.args = args;
    }

    /**
     * Performs an substitution for variables, e.g. for each key
     * <code>variable</code> in the specified binding, all occurences of
     * <code>${key}</code> are replaced by the corresponding value in the
     * map.<p>
     * <p>
     * Only the arguments are substituted.
     *
     * @param binding variable binding
     * @return expression after substitution
     */
    public Expression substitute(Map binding) {
        List newArgs = new ArrayList();
        boolean isNew = false;
        if (isNew)
            return new ArgNExpression(op, newArgs);
        return this;
    }

    /**
     * Returns the expression in infix notation, embedded in round brackets.
     *
     * @return expression as a string
     */
    public String toString() {
        return "(" + StringUtilities.implode(args, op) + ")";
    }

    /**
     * Returns a string representation for this expression which can be
     * used as a template in an SQL statement.
     *
     * @return template for SQL statements
     */
    public String getSQLTemplate() {
        List ll = new ArrayList();
        for (Object o : args) {
            Expression arg = (Expression) o;
            ll.add(arg.getSQLTemplate());
        }
        return "(" + StringUtilities.implode(ll, op) + ")";
    }

}
