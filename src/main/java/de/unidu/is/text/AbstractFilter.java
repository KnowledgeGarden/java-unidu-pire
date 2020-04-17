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

 
// $Id: AbstractFilter.java,v 1.9 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import java.util.Iterator;

import de.unidu.is.util.SequenceIterator;
import de.unidu.is.util.SingleItemIterator;

/**
 * This is an abstract filter implementation which allows for chaining filters.
 * Chaining filters means that the output of one filter is used as the input 
 * of another filter. This makes it easy to combine simple filters for
 * advanced applications.
 * 
 * <code>
 * Filter filter = new StemmerFilter(new WordSplitterFilter(null));
 * Iterator iterator = filter.apply("The quick brown fox jumps");
 * </code>
 * 
 * This code block chains a <code>StemmerFilter</code> with a
 * <code>WordSplitterFilter</code>. This means that the string 
 * "The quick brown fox jumps" is first applied on the 
 * <code>WordSplitterFilter</code>, which leads to an iterator over
 * "the", "quick", "brown", "fox" and "jumps". These objects are then applied
 * on the  <code>StemmerFilter</code>, resulting in "the", "quick", "brown", 
 * "fox" and "jump" (instead of "jump").<p>
 * 
 * Subclasses have to implement the actual filtering method.
 *   
 * @author Henrik Nottelmann
 * @since 2003-07-03
 * @version $Revision: 1.9 $, $Date: 2005/02/21 17:29:28 $
 */
public abstract class AbstractFilter implements Filter {

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
	public AbstractFilter(Filter nextFilter) {
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
		return new SequenceIterator(iterator) {
			public Iterator createIterator(Object object) {
				return AbstractFilter.this.filter(object);
			}
		};
	}

	/**
	 * Applies only this filter on the specified object, without considering 
	 * the other filters from the filter chain.<p>
	 * 
	 * This method is the working horse of filters extending this class, and 
	 * the only method which has to be implemented in concrete filters. The
	 * chaining of filters in done on <code>apply(Iterator)</code>.
	 *  
	 * @param value value to be modified by this filter
	 * @return iterator over the resulting objects
	 */
	protected abstract Iterator filter(Object value);

}
