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

 
// $Id: AcceptFilterIterator.java,v 1.5 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A iterator that filters another iterator. The method accept(Object) is called
 * for every item in the another iterator to test if that object should be
 * returned by this filter iterator.
 * 
 * @author Henrik Nottelmann
 * @since 2001-04-23
 * @version $Revision: 1.5 $, $Date: 2005/02/21 17:29:29 $
 */
public abstract class AcceptFilterIterator implements Iterator, AcceptFilter {

	/**
	 * The iterator to be filtered.
	 * 
	 */
	protected Iterator iterator;

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
	 * Creates a new filter iterator which has to be initialised.
	 * 
	 */
	public AcceptFilterIterator() {
	}

	/**
	 * Creates a new filter iterator.
	 * 
	 * @param iterator
	 *                   iterator to be filtered
	 */
	public AcceptFilterIterator(Iterator iterator) {
		init(iterator);
	}

	/**
	 * Inits this iterator.
	 * 
	 * @param iterator
	 *                   iterator to be filtered
	 */
	public void init(Iterator iterator) {
		this.iterator = iterator;
		this.next = null;
		this.called = false;
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
	 * @throws NoSuchElementException
	 *                   if there is no object available
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
	 */
	protected void calcNext() {
		next = null;
		if (iterator != null)
			while (iterator.hasNext()) {
				Object object = iterator.next();
				if (accept(object)) {
					next = object;
					break;
				}
			}
	}

	/**
	 * Returns true if the specified object should be returned by next(). This
	 * method has to be overridden!
	 * 
	 * @param object
	 *                   object to be testet
	 * @return true if the specified object should be returned by next()
	 */
	public abstract boolean accept(Object object);

	/**
	 * <b>Removing objects is unsupported! </B>
	 * 
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
