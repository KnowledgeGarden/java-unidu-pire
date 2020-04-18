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

 
// $Id: HTMLFilter.java,v 1.9 2005/03/09 08:59:15 nottelma Exp $
package de.unidu.is.text;

/**
 * This filter extracts all text from a specified HTML string, and returns 
 * the text content in a single string. 
 * 
 * @author Henrik Nottelmann
 * @since 2003-07-04
 * @version $Revision: 1.9 $, $Date: 2005/03/09 08:59:15 $
 */
public class HTMLFilter extends AbstractSingleItemFilter {

	/**
	 * Creates a new instance and sets the next filter in the chain.
	 * 
	 * @param nextFilter next filter in the filter chain
	 */
	public HTMLFilter(Filter nextFilter) {
		super(nextFilter);
	}

	/**
	 * Extracts the text from HTML, removes all tags, replaces
	 * well-known entities and removes the rest of them, and returns a single
	 * strring.
	 *
	 * @param value HTML string
	 * @return text content of the HTML string
	 */
	public Object run(Object value) {
		if (value == null)
			return null;
		StringBuffer buffer = new StringBuffer(value.toString());
		try {
			int TEXT = 0;
			int COMMENT = 1;
			int TAG = 2;
			int SCRIPT = 3;
			int type = TEXT;
			int start = 0;
			for (int i = 0; i < buffer.length(); i++) {
				char c = buffer.charAt(i);
				if (c == '<') {
					if (type == COMMENT || type == SCRIPT)
						continue;
					if (substringStartsWith(buffer, i, "<!--"))
						type = COMMENT;
					else if (substringStartsWith(buffer, i, "<script"))
						type = SCRIPT;
					else
						type = TAG;
					start = i;
					continue;
				}
				if (c == '>') {
					String text = buffer.substring(start + 1, i).trim() + " ";
					text = text.toLowerCase();
					if (type == COMMENT && !text.endsWith("-- "))
						continue;
					if (type == SCRIPT && !text.endsWith("/script "))
						continue;
					String replace = " ";
					if (text.startsWith("p ") || text.startsWith("br "))
						replace = "\n\n";
					buffer.replace(start, i + 1, replace);
					i = start - 1;
					type = TEXT;
					continue;
				}
				if (type == TEXT
					&& c == '&'
					&& (buffer.charAt(i + 1) == '#'
						|| Character.isUnicodeIdentifierStart(
							buffer.charAt(i + 1)))) {
					for (int j = i + 1; j < buffer.length(); j++) {
						char cc = buffer.charAt(j);
						if (cc == ';') {
							String text = buffer.substring(i + 1, j);
							String replace = "";
							if (text.equals("nbsp"))
								replace = " ";
							if (text.equals("gt"))
								replace = ">";
							if (text.equals("lt"))
								replace = "<";
							if (text.equals("amp"))
								replace = "&";
							if (text.equals("auml"))
								replace = "?";
							if (text.equals("ouml"))
								replace = "?";
							if (text.equals("uuml"))
								replace = "?";
							if (text.equals("Auml"))
								replace = "?";
							if (text.equals("Ouml"))
								replace = "?";
							if (text.equals("Uuml"))
								replace = "?";
							if (text.equals("szlig"))
								replace = "?";
							if (text.equals("#10"))
								replace = "\n";
							if (text.equals("#13"))
								replace = "\r";
							if (text.equals("#9"))
								replace = "\t";
							buffer.replace(i, j + 1, replace);
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return buffer.toString().replaceAll("  "," ").replaceAll("^ ","").replaceAll(" $","");
	}

	/**
	 * Tests whether the specified string buffer starts (from the
	 * specified index) with the specified string (where all letters
	 * are converted to lowercase).
	 *
	 * @param buffer string buffer to tested
	 * @param index starting index
	 * @param str string to test
	 * @return true if the specified string buffer starts (from the
	 *         specified index) with the specified string
	 */
	protected boolean substringStartsWith(
		StringBuffer buffer,
		int index,
		String str) {
		if (index + str.length() > buffer.length())
			return false;
		String s = buffer.substring(index, index + str.length()).toLowerCase();
		return s.equals(str);
	}

}
