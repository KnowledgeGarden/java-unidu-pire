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


// $Id: PlainExpression.java,v 1.7 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.expressions;

import java.util.List;
import java.util.Map;

/**
 * An expression for plain content.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:14 $
 * @since 2003-10-21
 */
public class PlainExpression extends AbstractExpression {

    /**
     * The content of this expression.
     */
    protected String content;

    /**
     * Creates a new expression object.
     *
     * @param content expression content
     */
    public PlainExpression(String content) {
        this.content = content;
    }

    /**
     * Tests whether this expressions equals the specified PlainExpression.
     *
     * @param o expression to test
     * @return true iff both expressions are equal
     */
    public boolean equals(Object o) {
        if (!(o instanceof PlainExpression))
            return false;
        return ((PlainExpression) o).content.equals(content);
    }

    /**
     * Returns the content of this expression.
     *
     * @return content of this expression
     */
    public String toString() {
        return content;
    }

    /**
     * Returns the hashcode of this expression object.
     *
     * @return hashcode of this expression object
     */
    public int hashCode() {
        return content.hashCode();
    }

    /**
     * Performs an substitution for variables, e.g. for each key
     * <code>variable</code> in the specified binding, all occurences of
     * <code>${key}</code> are replaced by the corresponding value in the
     * map.<p>
     * <p>
     * How the substitution is managed is left to the implementing class. It
     * is explicitly allowed to return a new expression object.
     *
     * @param binding variable binding
     * @return expression after substitution
     */
    public Expression substitute(Map binding) {
        List list = (List) binding.get(this);
        if (list == null)
            return this;
        return ((Expression) list.get(0)).substitute(binding);
    }

    /**
     * Sets the content of this expression.
     *
     * @param content new content
     */
    public void set(String content) {
        this.content = content;
    }

}
