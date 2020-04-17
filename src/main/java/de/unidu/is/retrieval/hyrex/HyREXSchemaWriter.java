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

// $Id: HyREXSchemaWriter.java,v 1.8 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.hyrex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import de.unidu.is.retrieval.Schema;
import de.unidu.is.retrieval.SchemaElement;
import de.unidu.is.util.FileISOWriter;

/**
 * A class for writing HyREX DDL files from schemas.
 * 
 * @author Henrik Nottelmann
 * @since 2005-07-19
 * @version $Revision: 1.8 $, $Date: 2005/03/14 17:33:14 $
 */
public class HyREXSchemaWriter {

	/**
	 * Directory for the files.
	 */
	protected File dir;

	/**
	 * Creates a new instance.
	 * 
	 * @param dir directory for the files
	 */
	public HyREXSchemaWriter(File dir) {
		this.dir = dir;
	}

	/**
	 * Writes the schema.
	 * 
	 * @param schema
	 *                   schema to write
	 * @param structure
	 *                   defines if structure or plain schemas are used
	 */
	public void write(Schema schema, boolean structure) {
		try {
			PrintWriter pw = new PrintWriter(new FileISOWriter(new File(dir,
					schema.getSchemaName() + ".xml")));
			pw.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			pw.println("<hyrex>");
			pw.println();
			for (Iterator iter = schema.getAliases().iterator(); iter.hasNext();) {
				String alias = (String) iter.next();
				SchemaElement element = schema.getElement(alias);
				pw.println("<attribute name=\"" + alias + "\">");
				pw.println("<datatype classname=\"" + element.getDatatypeName()
						+ "\">");
				for (Iterator iterator = schema.getXPathsForAlias(alias)
						.iterator(); iterator.hasNext();) {
					String xpath = (String) iterator.next();
					pw.println("<query query=\"" + xpath + "\"/>");
				}
				for (Iterator iterator = element.getOperators().iterator(); iterator
						.hasNext();) {
					String operator = (String) iterator.next();
					pw.println("<predicate name=\"" + operator + "\"/>");
				}
				pw.println("</datatype>");
				pw.println("</attribute>");
				pw.println();
			}
			if (structure)
				pw
						.println("<structure classname=\"HyREX::HyPath::Structure::Path\"/>");
			else
				pw
						.println("<structure classname=\"HyREX::HyPath::Structure::NoStruct\"/>");
			pw.println("</hyrex>");
			pw.close();
		} catch (UnsupportedEncodingException e) {
			de.unidu.is.util.Log.error(e);
		} catch (FileNotFoundException e) {
			de.unidu.is.util.Log.error(e);
		}
	}

}