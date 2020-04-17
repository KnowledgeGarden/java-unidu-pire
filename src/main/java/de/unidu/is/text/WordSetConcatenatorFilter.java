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

 
// $Id: WordSetConcatenatorFilter.java,v 1.4 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import java.util.Iterator;
import java.util.Set;

import de.unidu.is.util.CollectionUtilities;
import de.unidu.is.util.SingleItemIterator;

/**
 * This filter concatenes all values together (separated by a space), 
 * where each word only occurs once. The original order is not preserved. 
 * 
 * @author Henrik Nottelmann
 * @since 2004-03-12
 * @version $Revision $, $Date: 2005/02/21 17:29:28 $
 */
public class WordSetConcatenatorFilter implements Filter {

	/**
	 * The next filter in the filter chain.
	 * 
	 */
	protected Filter nextFilter;

	/**
	 * Creates a new instance and sets the next filter in the chain.
	 * 
	 * @param nextFilter next filter in the filter chain
	 */
	public WordSetConcatenatorFilter(Filter nextFilter) {
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
	 * 
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
		Set set = CollectionUtilities.toSet(iterator);
		StringBuffer buf = new StringBuffer();
		for (Iterator it = set.iterator(); it.hasNext();)
			buf.append(" ").append(it.next());
		return new SingleItemIterator(buf.toString().trim());
	}

}
