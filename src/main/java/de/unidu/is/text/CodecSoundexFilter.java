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

 
// $Id: CodecSoundexFilter.java,v 1.4 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.text;

/**
 * This filter converts a specified string into its soundex representation,
 * using code from Apache Jakarta Commons Codec.
 * 
 * @author Henrik Nottelmann
 * @since 2004-12-09
 * @version $Revision: 1.4 $, $Date: 2005/03/14 17:33:14 $
 */
public class CodecSoundexFilter extends AbstractSingleItemFilter {
	
	/**
	 * This is a default mapping of the 26 letters used in US English. A value
	 * of <code>0</code> for a letter position means do not encode.
	 * <p>
	 * (This constant is provided as both an implementation convenience and to
	 * allow Javadoc to pick up the value for the constant values page.)
	 * </p>
	 * 
	 * @see #US_ENGLISH_MAPPING
	 */
	public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";

	/**
	 * This is a default mapping of the 26 letters used in US English. A value
	 * of <code>0</code> for a letter position means do not encode.
	 */
	public static final char[] US_ENGLISH_MAPPING = US_ENGLISH_MAPPING_STRING
			.toCharArray();

	/**
	 * Every letter of the alphabet is "mapped" to a numerical value. This char
	 * array holds the values to which each letter is mapped. This
	 * implementation contains a default map for US_ENGLISH
	 */
	private char[] soundexMapping = US_ENGLISH_MAPPING;

	/**
	 * Creates a new instance and sets the next filter in the chain.
	 * 
	 * @param nextFilter
	 *                   next filter in the filter chain
	 */
	public CodecSoundexFilter(Filter nextFilter) {
		super(nextFilter);
	}

	/**
	 * Converts the specified object into its soundex value.
	 * 
	 * @param value
	 *                   string to be converted
	 * @return soundex representation of the specified value
	 */
	public Object run(Object value) {
		return soundex(value.toString());
	}

	/**
	 * Used internally by the SoundEx algorithm.
	 * 
	 * Consonants from the same code group separated by W or H are treated as
	 * one.
	 * 
	 * @param str
	 *                   the cleaned working string to encode (in upper case).
	 * @param index
	 *                   the character position to encode
	 * @return Mapping code for a particular character
	 * @throws IllegalArgumentException
	 *                   if the character is not mapped
	 */
	private char getMappingCode(String str, int index) {
		// map() throws IllegalArgumentException
		char mappedChar = this.map(str.charAt(index));
		// HW rule check
		if (index > 1 && mappedChar != '0') {
			char hwChar = str.charAt(index - 1);
			if ('H' == hwChar || 'W' == hwChar) {
				char preHWChar = str.charAt(index - 2);
				char firstCode = this.map(preHWChar);
				if (firstCode == mappedChar || 'H' == preHWChar
						|| 'W' == preHWChar) {
					return 0;
				}
			}
		}
		return mappedChar;
	}

	/**
	 * Returns the soundex mapping.
	 * 
	 * @return soundexMapping.
	 */
	private char[] getSoundexMapping() {
		return this.soundexMapping;
	}

	/**
	 * Maps the given upper-case character to it's Soudex code.
	 * 
	 * @param ch
	 *                   An upper-case character.
	 * @return A Soundex code.
	 * @throws IllegalArgumentException
	 *                   Thrown if <code>ch</code> is not mapped.
	 */
	private char map(char ch) {
		int index = ch - 'A';
		if (index < 0 || index >= this.getSoundexMapping().length) {
			throw new IllegalArgumentException("The character is not mapped: "
					+ ch);
		}
		return this.getSoundexMapping()[index];
	}

	/**
	 * Retreives the Soundex code for a given String object.
	 * 
	 * @param str
	 *                   String to encode using the Soundex algorithm
	 * @return A soundex code for the String supplied
	 * @throws IllegalArgumentException
	 *                   if a character is not mapped
	 */
	private String soundex(String str) {
		if (str == null) {
			return null;
		}
		str = clean(str);
		if (str.length() == 0) {
			return str;
		}
		char out[] = {'0', '0', '0', '0'};
		char last, mapped;
		int incount = 1, count = 1;
		out[0] = str.charAt(0);
		// getMappingCode() throws IllegalArgumentException
		last = getMappingCode(str, 0);
		while ((incount < str.length()) && (count < out.length)) {
			mapped = getMappingCode(str, incount++);
			if (mapped != 0) {
				if ((mapped != '0') && (mapped != last)) {
					out[count++] = mapped;
				}
				last = mapped;
			}
		}
		return new String(out);
	}

	private String clean(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		int len = str.length();
		char[] chars = new char[len];
		int count = 0;
		for (int i = 0; i < len; i++) {
			if (Character.isLetter(str.charAt(i))) {
				chars[count++] = str.charAt(i);
			}
		}
		if (count == len) {
			return str.toUpperCase();
		}
		return new String(chars, 0, count).toUpperCase();
	}

}

