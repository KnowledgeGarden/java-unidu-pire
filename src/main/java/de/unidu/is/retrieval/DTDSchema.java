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

// $Id: DTDSchema.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import com.wutka.dtd.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A schema parsed from a DTD.
 *
 * @author Bertrand Noutsa, Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 * @since 2004-06-16
 */
public class DTDSchema extends Schema {

    /**
     * Creates a new object.
     */
    public DTDSchema() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param schemaName schema name
     */
    public DTDSchema(String schemaName) {
        super(schemaName);
    }

    /**
     * Creates a new object.
     *
     * @param schemaName schema name
     * @param dtd        file containing the DTD
     * @throws IOException
     */
    public DTDSchema(String schemaName, File dtd) throws IOException {
        this(schemaName, new DTDParser(dtd));
    }

    /**
     * Creates a new object.
     *
     * @param schemaName schema name
     * @param dtd        URL containing the DTD
     * @throws IOException
     */
    public DTDSchema(String schemaName, URL dtd) throws IOException {
        this(schemaName, new DTDParser(dtd));
    }

    /**
     * Creates a new object.
     *
     * @param schemaName schema name
     * @param dtd        reader returning the DTD
     * @throws IOException
     */
    public DTDSchema(String schemaName, Reader dtd) throws IOException {
        this(schemaName, new DTDParser(dtd));
    }

    /**
     * Creates a new object.
     *
     * @param schemaName schema name
     * @param dtd        DTD
     * @throws IOException
     */
    public DTDSchema(String schemaName, String dtd) throws IOException {
        this(schemaName, new StringReader(dtd));
    }

    /**
     * Creates a new object.
     *
     * @param schemaName schema name
     * @param parser     DTD parser
     * @throws IOException
     */
    public DTDSchema(String schemaName, DTDParser parser) throws IOException {
        super(schemaName);
        DTD dtd = parser.parse();
        processDTD(dtd);
    }

    /**
     * Processes the parsed DTD, and fills this schema.
     *
     * @param dtd DTD object
     */
    private void processDTD(DTD dtd) {
        Map map = new LinkedHashMap();
        Map children = new LinkedHashMap();
        List allchildren = new LinkedList();

        // create schema elements
        Object[] o = dtd.getItems();
        for (Object element : o) {
            // for element definitions
            if (element instanceof DTDElement) {
                List listofchildren = new LinkedList();
                DTDElement el = (DTDElement) element;
                map.put(el.name, new SchemaElement(el.name));
                DTDItem it = el.content;
                if (it instanceof DTDSequence) {
                    DTDSequence seq = new DTDSequence();
                    seq = (DTDSequence) it;
                    DTDItem[] it2 = seq.getItem();
                    for (DTDItem dtdItem : it2) {
                        if (dtdItem instanceof DTDName) {
                            DTDName na = (DTDName) dtdItem;
                            listofchildren.add(na.value);
                            allchildren.add(na.value);
                        }
                    }
                }
                children.put(el.name, listofchildren);
            }
            // for attribute definitions
            if (element instanceof DTDAttlist) {
                DTDAttlist at = (DTDAttlist) element;
                DTDAttribute[] c = at.getAttribute();
                SchemaElement sch = (SchemaElement) map.get(at.getName());
                for (DTDAttribute dtdAttribute : c) sch.add(new SchemaElement("@" + dtdAttribute.name));
            }
        }

        // find root element, add children to schema elements
        for (Object item : children.entrySet()) {
            Map.Entry entry = (Map.Entry) item;

            String key = (String) entry.getKey();
            if (!allchildren.contains(key)) {
                SchemaElement rootEl = (SchemaElement) map.get(key);
                this.setRootElement(rootEl);
            }

            List list = (List) entry.getValue();
            if (list.size() > 0) {
                if (map.containsKey(key)) {
                    SchemaElement sch2 = (SchemaElement) map.get(key);
                    for (Object value : list) {
                        String ch = (String) value;
                        SchemaElement sch3 = (SchemaElement) map.get(ch);
                        if (sch3 != null)
                            sch2.add(sch3);
                    }

                }
            }
        }
    }

}