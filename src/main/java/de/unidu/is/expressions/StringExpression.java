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


// $Id: StringExpression.java,v 1.5 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.expressions;

/**
 * An expression for a string, enclosed in single quotes.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-10-21
 */
public class StringExpression extends AbstractExpression {

    /**
     * The content of this expression.
     */
    protected String content;

    /**
     * Creates a new expression object.
     *
     * @param content expression content
     */
    public StringExpression(String content) {
        this.content = content;
    }

    /**
     * Returns the content of this expression.
     *
     * @return expression content
     */
    public String get() {
        return content;
    }

    /**
     * Sets the content of this expression.
     *
     * @param content expression content
     */
    public void set(String content) {
        this.content = content;
    }

    /**
     * Returns the content of this expression, enclosed in single quotes.
     *
     * @return content of this expression
     */
    public String toString() {
        return "'" + content + "'";
    }

    /**
     * Tests whether this expressions equals the specified StringExpression.
     *
     * @param o expression to test
     * @return true iff both expressions are equal
     */
    public boolean equals(Object o) {
        if (!(o instanceof StringExpression))
            return false;
        return ((StringExpression) o).get().equals(content);
    }

    /**
     * Returns the hashcode of this expression object.
     *
     * @return hashcode of this expression object
     */
    public int hashCode() {
        return toString().hashCode();
    }

}
