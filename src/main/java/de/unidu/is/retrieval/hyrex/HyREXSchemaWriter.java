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

import de.unidu.is.retrieval.Schema;
import de.unidu.is.retrieval.SchemaElement;
import de.unidu.is.util.FileISOWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * A class for writing HyREX DDL files from schemas.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/03/14 17:33:14 $
 * @since 2005-07-19
 */
public class HyREXSchemaWriter {

    /**
     * Directory for the files.
     */
    protected final File dir;

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
     * @param schema    schema to write
     * @param structure defines if structure or plain schemas are used
     */
    public void write(Schema schema, boolean structure) {
        try {
            PrintWriter pw = new PrintWriter(new FileISOWriter(new File(dir,
                    schema.getSchemaName() + ".xml")));
            pw.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            pw.println("<hyrex>");
            pw.println();
            for (Object item : schema.getAliases()) {
                String alias = (String) item;
                SchemaElement element = schema.getElement(alias);
                pw.println("<attribute name=\"" + alias + "\">");
                pw.println("<datatype classname=\"" + element.getDatatypeName()
                        + "\">");
                for (Object value : schema.getXPathsForAlias(alias)) {
                    String xpath = (String) value;
                    pw.println("<query query=\"" + xpath + "\"/>");
                }
                for (Object o : element.getOperators()) {
                    String operator = (String) o;
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
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            de.unidu.is.util.Log.error(e);
        }
    }

}