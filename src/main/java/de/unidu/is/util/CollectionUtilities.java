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


// $Id: CollectionUtilities.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.util.*;

/**
 * This class provides some convenient methods for dealing with collections.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 * @since 2003-07-03
 */
public class CollectionUtilities {

    /**
     * Adds all elements returned by the specified iterator to the specified
     * collection.
     *
     * @param collection collection to which objects are added
     * @param iterator   iterator returning objects which have to be added to
     *                   the collection
     */
    public static void addAll(Collection collection, Iterator iterator) {
        while (iterator != null && iterator.hasNext())
            collection.add(iterator.next());
    }

    /**
     * Creates an empty set and adds all elements returned by the specified
     * iterator to this set.
     *
     * @param iterator returning objects which have to be added to
     *                 the set
     * @return set containing all elements returned by the iterator
     */
    public static Set toSet(Iterator iterator) {
        Set set = new HashSet();
        addAll(set, iterator);
        return set;
    }

    /**
     * Creates an empty list and adds all elements returned by the specified
     * iterator to this list.
     *
     * @param iterator returning objects which have to be added to
     *                 the list
     * @return list containing all elements returned by the iterator
     */
    public static List toList(Iterator iterator) {
        List list = new ArrayList();
        addAll(list, iterator);
        return list;
    }

}
