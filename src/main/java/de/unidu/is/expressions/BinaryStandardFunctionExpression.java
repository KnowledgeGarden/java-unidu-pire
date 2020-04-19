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


// $Id: BinaryStandardFunctionExpression.java,v 1.1 2005/03/17 22:30:15 nottelma Exp $
package de.unidu.is.expressions;

import java.util.Map;

/**
 * An expression for binary functions without preceding ampersend.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/03/17 22:30:15 $
 * @since 2005-03-17
 */
public class BinaryStandardFunctionExpression extends AbstractExpression {

    /**
     * Function name.
     */
    protected final String funcName;

    /**
     * First argument of this expression.
     */
    protected final Expression arg1;

    /**
     * Second argument of this expression.
     */
    protected final Expression arg2;

    /**
     * Creates a new expression object.
     *
     * @param funcName function name
     * @param arg1     first argument of this expression
     * @param arg2     second argument of this expression
     */
    public BinaryStandardFunctionExpression(String funcName, Expression arg1, Expression arg2) {
        this.funcName = funcName;
        this.arg1 = arg1;
        this.arg2 = arg2;
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
        Expression newArg1 = arg1.substitute(binding);
        Expression newArg2 = arg2.substitute(binding);
        if (arg1 != newArg1 || arg2 != newArg2)
            return new BinaryStandardFunctionExpression(funcName, newArg1, newArg2);
        return this;
    }

    /**
     * Returns the expression.
     *
     * @return expression as a string
     */
    public String toString() {
        return funcName + "(" + arg1 + "," + arg2 + ")";
    }

    /**
     * Returns a string representation for this expression which can be
     * used as a template in an SQL statement.
     *
     * @return template for SQL statements
     */
    public String getSQLTemplate() {
        return funcName + "(" + arg1.getSQLTemplate() + "," + arg2.getSQLTemplate() + ")";
    }


    /**
     * Returns the first argument.
     *
     * @return first argument
     */
    public Expression getArg1() {
        return arg1;
    }

    /**
     * Returns the second argument.
     *
     * @return second argument
     */
    public Expression getArg2() {
        return arg2;
    }

    /**
     * Returns the function name.
     *
     * @return function name
     */
    public String getFunctionName() {
        return funcName;
    }

}
