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


// $Id: Filter.java,v 1.6 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import java.util.Iterator;

/**
 * A filter is used to modify objects (in most cases, objects) in a uniform
 * way.  The filter converts an object into a list of other objects,
 * represented by an iterator. In addition, a filter can be called with an
 * iterator; then, the filter is applied on each object returned by the
 * filter.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:28 $
 * @since 2003-07-03
 */
public interface Filter {

    /**
     * Applies this filter on the specified single object.
     *
     * @param seed object on which the filter is applied
     * @return iterator over the resulting objects
     */
    Iterator apply(Object seed);

    /**
     * Applies this filter on each object returned by the specified iterator.
     *
     * @param iterator iterator over objects on which the filter is applied
     * @return iterator over the resulting objects
     */
    Iterator apply(Iterator iterator);

}
