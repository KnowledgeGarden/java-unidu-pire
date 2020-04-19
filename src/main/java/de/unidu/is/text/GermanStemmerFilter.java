///*
// Copyright 2000-2005 University Duisburg-Essen, Working group "Information Systems"
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use
// this file except in compliance with the License. You may obtain a copy of the
// License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.
// */
//
//// $Id: GermanStemmerFilter.java,v 1.1 2005/03/19 23:52:11 nottelma Exp $
//package de.unidu.is.text;
//
//import net.sf.snowball.ext.germanStemmer;
//
///**
// * This filter converts a specified German string into stemmed version. The
// * stemmer is taken from Snowball.
// *
// * @author Henrik Nottelmann
// * @since 2005-03-20
// * @version $Revision: 1.1 $, $Date: 2005/03/19 23:52:11 $
// */
//public class GermanStemmerFilter extends AbstractSingleItemFilter {
//
//	/**
//	 * German stemmer from Snowball.
//	 */
//	protected germanStemmer stemmer;
//
//	/**
//	 * Creates a new instance and sets the next filter in the chain.
//	 *
//	 * @param nextFilter
//	 *                   next filter in the filter chain
//	 */
//	public GermanStemmerFilter(Filter nextFilter) {
//		super(nextFilter);
//		stemmer = new germanStemmer();
//	}
//
//	/**
//	 * Converts the specified word into its stemmed version.
//	 *
//	 * @param value
//	 *                   word to stem
//	 * @return stemmed word
//	 */
//	public Object run(Object value) {
//		synchronized (stemmer) {
//			stemmer.setCurrent(value.toString());
//			stemmer.stem();
//			return stemmer.getCurrent();
//		}
//	}
//
//}