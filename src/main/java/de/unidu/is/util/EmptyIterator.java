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


// $Id: EmptyIterator.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A iterator over an empty set.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 * @since 2003-07-05
 */
public class EmptyIterator implements Iterator {


    /**
     * Returns always <code>false</code>.
     *
     * @return always false
     */
    public boolean hasNext() {
        return false;
    }

    /**
     * Always throws a <code>NoSuchElementException</code>.
     *
     * @return nothing (an exception will be thrown)
     * @throws NoSuchElementException
     */
    public Object next() {
        throw new NoSuchElementException();
    }

    /**
     * <b>Removing objects is unsupported!</B>
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
