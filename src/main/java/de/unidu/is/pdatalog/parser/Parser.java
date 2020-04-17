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

 
// $Id: Parser.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.parser;

import java.io.StringReader;

import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.pdatalog.ds.Literal;
import de.unidu.is.pdatalog.ds.Rule;

/**
 * A parser for pDatalog++ files, based on AntLR.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-21
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 */
public class Parser {

	/**
	 * Returns a AntLR parser instance for the given pDatalog++ string.
	 *  
	 * @param str pDatalog++ string
	 * @return AntLR parser instance
	 */
	private static SimpleParser getParser(String str) {
		SimpleLexer lexer = new SimpleLexer(new StringReader(str));
		return new SimpleParser(lexer);
	}

	/**
	 * Parses a pDatalog++ string and returns the corresponding fact object.
	 * 
	 * @param str pDatalog++ string
	 * @return fact object
	 */
	public static Fact parseFact(String str) {
		Rule rule = parseRule(str);
		if (rule instanceof Fact)
			return (Fact) rule;
		return null;
	}

	/**
	 * Parses a pDatalog++ string and returns the corresponding rule object.
	 * 
	 * @param str pDatalog++ string
	 * @return rule object
	 */
	public static Rule parseRule(String str) {
		try {
			return getParser(str).rule();
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return null;
	}
	
	/**
	 * Parses a pDatalog++ string (a tuple without predicate name) and 
	 * returns the corresponding tuple object.
	 * 
	 * @param str pDatalog++ string
	 * @return tuple object
	 */
	public static Fact parseTuple(String str) {
		try {
			return getParser(str).tuple();
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return null;
	}


	/**
	 * Parses a pDatalog++ string and returns the corresponding literal object.
	 * 
	 * @param str pDatalog++ string
	 * @return literal object
	 */
	public static Literal parseLiteral(String str) {
		try {
			return getParser(str).literal();
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return null;
	}

}
