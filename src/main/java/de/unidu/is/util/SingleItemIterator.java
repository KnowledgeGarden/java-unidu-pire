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

 
// $Id: SingleItemIterator.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A iterator that returns only one single item.
 *
 * @author Henrik Nottelmann
 * @since 2001-04-29
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public class SingleItemIterator implements Iterator {

	/**
	 * The only object returned by this iterator.
	 * 
	 */
	protected Object item;

	/**
	 * Indicates if next() has ever been called.
	 *
	 */
	protected boolean called;

	/**
	 * Creates a new iterator.
	 * 
	 * @param item only object returned by this iterator
	 */
	public SingleItemIterator(Object item) {
		this.item = item;
		this.called = false;
	}

	/**
	 * Returns true if this iterator has more elements.
	 *
	 * @return true if the iterator has more elements
	 */
	public boolean hasNext() {
		return !called;
	}

	/**
	 * Returns the next object.
	 * 
	 * @return next object
	 * @throws NoSuchElementException if there is no object available
	 */
	public Object next() {
		if (called)
			throw new NoSuchElementException();
		called = true;
		return item;
	}

	/**
	 * <b>Removing objects is unsupported!</B>
	 * 
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
