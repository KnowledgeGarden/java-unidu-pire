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

// $Id: StemmerFilter.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.text;

/**
 * This filter converts a specified string into stemmed version.
 * <p>
 * 
 * This implementation uses the nonfree version (not packaged here) if available
 * (for backwards compatibility), and the Apache Lucene implementation
 * otherwise.
 * 
 * @author Henrik Nottelmann
 * @since 2003-07-04
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 */
public class StemmerFilter extends AbstractSingleItemFilter {

	/**
	 * Soundex filter to which calls are delegated.
	 */
	protected SingleItemFilter stemmerFilter;

	/**
	 * Creates a new instance and sets the next filter in the chain.
	 * 
	 * @param nextFilter
	 *                   next filter in the filter chain
	 */
	public StemmerFilter(Filter nextFilter) {
		super(nextFilter);
		try {
			// test for nonfree version
			stemmerFilter = (SingleItemFilter) Class.forName(
					"de.unidu.is.text.NonFreeStemmerFilter").getConstructor(
					new Class[]{Filter.class}).newInstance(new Object[]{null});
		} catch (Exception e) {
			// use Codec instead
			stemmerFilter = new LuceneStemmerFilter(null);
		}
	}

	/**
	 * Converts the specified word into its stemmed version.
	 * 
	 * @param value
	 *                   word to stem
	 * @return stemmed word
	 */
	public Object run(Object value) {
		return stemmerFilter.run(value);
	}

}