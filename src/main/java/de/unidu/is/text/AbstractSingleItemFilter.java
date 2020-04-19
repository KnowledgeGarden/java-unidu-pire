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


// $Id: AbstractSingleItemFilter.java,v 1.6 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import de.unidu.is.util.EmptyIterator;
import de.unidu.is.util.SingleItemIterator;

import java.util.Iterator;

/**
 * This is an abstract filter implementation which converts every object into
 * exactly one object (or into null), and which allows for chaining filters.<p>
 * <p>
 * Subclasses have to implement the actual filtering method.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:28 $
 * @since 2003-07-04
 */
public abstract class AbstractSingleItemFilter
        extends AbstractFilter
        implements SingleItemFilter {

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public AbstractSingleItemFilter(Filter nextFilter) {
        super(nextFilter);
    }

    /**
     * Applies only this filter on the specified object, without considering
     * the other filters from the filter chain.<p>
     * <p>
     * This method applies <code>run(String)</code> on the specified object,
     * and then returns a <code>SingleItemIterator</code> returning the
     * resulting object. If <code>run(String)</code> returns null, a
     * <codeEmptyIterator</code> is returned.
     *
     * @param value value to be modified by this filter
     * @return iterator over the resulting objects
     * @see de.unidu.is.text.AbstractFilter#filter(java.lang.Object)
     */
    protected Iterator filter(Object value) {
        value = run(value);
        if (value == null)
            return new EmptyIterator();
        return new SingleItemIterator(value);
    }

}
