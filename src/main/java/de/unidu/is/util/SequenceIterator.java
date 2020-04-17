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

 
// $Id: SequenceIterator.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A iterator that is a sequence of several iterators. <p>

 * The sequence iterator has an iterator returning objects which can deliver
 * iterators. The delivered iterators are traversed one after another by this
 * iterator. 
 *
 * @author Henrik Nottelmann
 * @since 2001-06-29
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 */
public abstract class SequenceIterator implements Iterator {

	/**
	 * The iterator returning objects which can deliver iterators.
	 * 
	 */
	protected Iterator iterator;

	/**
	 * The current iterator producing items which are returned by this class.
	 * 
	 */
	protected Iterator currentIterator;

	/**
	 * The next object to be returned.
	 * 
	 */
	protected Object next;

	/**
	 * Indicates if next() has ever been called. This is needed for 
	 * initialisation outside the constructor (because that does not work!).
	 *
	 */
	protected boolean called;

	/**
	 * Creates a new sequence iterator.
	 * 
	 * @param iterator iterator returning objects which can deliver iterators
	 */
	public SequenceIterator(Iterator iterator) {
		this.iterator = iterator;
	}

	/**
	 * Returns true if this iterator has more elements.
	 *
	 * @return true if the iterator has more elements
	 */
	public boolean hasNext() {
		if (!called) {
			called = true;
			calcNext();
		}
		return next != null;
	}

	/**
	 * Returns the next object.
	 * 
	 * @return next object
	 * @throws NoSuchElementException if there is no object available
	 */
	public Object next() {
		if (!called) {
			called = true;
			calcNext();
		}
		if (next == null)
			throw new NoSuchElementException();
		Object ret = next;
		next = null;
		calcNext();
		return ret;
	}

	/**
	 * Calculates the next object to be returned.
	 * 
	 */
	protected void calcNext() {
		while (currentIterator == null || !currentIterator.hasNext())
			if (iterator.hasNext())
				currentIterator = createIterator(iterator.next());
			else
				return;
		next = currentIterator.next();
	}

	/**
	 * Calls the speicified object to create an iterator.
	 * This method has to be overridden!
	 * 
	 * @param object object which should create an iterator
	 * @return iterator
	 */
	public abstract Iterator createIterator(Object object);

	/**
	 * <b>Removing objects is unsupported!</B>
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
