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


// $Id: CounterFilter.java,v 1.7 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import de.unidu.is.util.HashPropertyMap;
import de.unidu.is.util.PropertyMap;
import de.unidu.is.util.SingleItemIterator;
import de.unidu.is.util.Tuple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This filter counts occurences of objects, and returns a list of object-frequency pairs.
 *
 * @author Henrik Nottelmann
 * @version $Revision $, $Date: 2005/02/21 17:29:28 $
 * @since 2003-09-11
 */
public class CounterFilter implements Filter {

    /**
     * The next filter in the filter chain.
     */
    private final Filter nextFilter;

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public CounterFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    /**
     * Applies this filter on the specified single object.
     *
     * @param seed object on which the filter is applied
     * @return iterator over the resulting objects
     * @see de.unidu.is.text.Filter#apply(java.lang.Object)
     */
    public Iterator apply(Object seed) {
        return apply(new SingleItemIterator(seed));
    }

    /**
     * Applies this filter on each object returned by the specified
     * iterator.<p>
     * <p>
     * This method first calls the next filter in the filter chain (if
     * existing), and then applies this filter on the specified iterator.
     *
     * @param iterator iterator over objects on which the filter is applied
     * @return iterator over the resulting objects
     * @see de.unidu.is.text.Filter#apply(java.util.Iterator)
     */
    public Iterator apply(Iterator iterator) {
        if (nextFilter != null)
            iterator = nextFilter.apply(iterator);
        PropertyMap counter = new HashPropertyMap();
        for (Iterator it = iterator; it.hasNext(); ) {
            Object object = it.next();
            int count = 0;
            try {
                count = counter.getInt(object);
            } catch (RuntimeException e) {
            }
            counter.setInt(object, count + 1);
        }
        List results = new ArrayList();
        for (Object object : counter.keySet()) {
            int count = counter.getInt(object);
            results.add(new Tuple(new Object[]{object, count}));
        }
        return results.iterator();
    }

}
