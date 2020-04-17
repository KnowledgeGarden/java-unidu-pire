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

 
// $Id: ParserFilter.java,v 1.6 2005/02/21 17:29:28 huesselbeck Exp $
package de.unidu.is.text;

import java.util.Iterator;

import de.unidu.is.util.SingleItemIterator;

/**
 * This filter splits a string into tokens (by converting all non-letter 
 * characters are converted into whitespaces, splitting the resulting string 
 * is split into tokens with whitespaces as token boundaries, and considering 
 * only tokens with at least 3 characters), converts the tokens into 
 * lowercase, computes the stems of the tokens, and removed stopwords.
 * 
 * @author Henrik Nottelmann
 * @since 2003-07-04
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:28 $
 */
public class ParserFilter extends AbstractFilter {

	/**
	 * Creates a new instance.
	 * 
	 */
	public ParserFilter() {
		super(
			new StopwordFilter(
				new StemmerFilter(
					new LowercaseFilter(new WordSplitterFilter(null)))));
	}

	/**
	 * Parses the objects.
	 * 
	 * @param value string to be parsed
	 * @return iterator over tokens
	 * @see de.unidu.is.text.AbstractFilter#filter(java.lang.Object)
	 */
	protected Iterator filter(Object value) {
		// actual work is done in other filters in filter chain
		return new SingleItemIterator(value);
	}

}
