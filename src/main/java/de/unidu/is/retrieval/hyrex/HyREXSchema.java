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


// $Id: HyREXSchema.java,v 1.15 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.retrieval.hyrex;

import de.unidu.is.retrieval.Schema;
import de.unidu.is.retrieval.SchemaElement;
import de.unidu.is.retrieval.pire.dt.NameDT;
import de.unidu.is.retrieval.pire.dt.TextDT;
import de.unidu.is.retrieval.pire.dt.YearDT;
import de.unidu.is.util.HashPropertyMap;
import de.unidu.is.util.PropertyMap;
import de.unidu.is.util.XMLUtilities;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A schema, created from a HyREX DDL file.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.15 $, $Date: 2005/03/14 17:33:14 $
 * @since 2004-01-03
 */
public class HyREXSchema extends Schema {

    /**
     * Default operators for data types.
     */
    private static final PropertyMap defaultOperators;

    static {
        defaultOperators = new HashPropertyMap(true);
        defaultOperators.put(TextDT.NAME, TextDT.STEMEN);
        defaultOperators.put(NameDT.NAME, NameDT.PLAINNAME);
        defaultOperators.put(NameDT.SOUNDEX, NameDT.SOUNDEX);
        defaultOperators.put(YearDT.NAME, YearDT.EQUALS);
        defaultOperators.put("Number", "=");
    }

    /**
     * DDL as XML document.
     */
    protected Document ddl;

    /**
     * Creates a new schema from a DDL document.
     *
     * @param ddlFile file with DDL document
     */
    public HyREXSchema(File ddlFile) {
        this(loadDDL(ddlFile));
    }

    /**
     * Creates a new schema from a DDL document.
     *
     * @param schemaName schema name
     * @param ddlFile    file with DDL document
     */
    public HyREXSchema(String schemaName, File ddlFile) {
        this(schemaName, loadDDL(ddlFile));
    }

    /**
     * Creates a new schema from a DDL document.
     *
     * @param schema schema DDL document
     */
    public HyREXSchema(Document schema) {
        this(null, schema);
    }

    /**
     * Creates a new schema from a DDL document.
     *
     * @param schemaName schema name
     * @param schema     schema DDL document
     */
    public HyREXSchema(String schemaName, Document schema) {
        super(schemaName);
        this.ddl = schema;
        String cn = XMLUtilities.extract(schema, "/hyrex/structure/@classname");
        setUseXPathForQuery(cn == null
                || !cn.equals("HyREX::HyPath::Structure::NoStruct"));
        try {
            NodeIterator it = XPathAPI.selectNodeIterator(schema,
                    "/hyrex/attribute");
            for (Node node; (node = it.nextNode()) != null; ) {
                Element element = (Element) node;
                String attName = element.getAttribute("name");
                String datatypeName = XMLUtilities.extract(element,
                        "datatype/@classname");
                List operators = new ArrayList();
                NodeIterator it2 = XPathAPI.selectNodeIterator(element,
                        "datatype/predicate/@name");
                for (Node node2; (node2 = it2.nextNode()) != null; )
                    operators.add(node2.getNodeValue());
                it2 = XPathAPI.selectNodeIterator(element,
                        "datatype/query/@query");
                for (Node n; (n = it2.nextNode()) != null; ) {
                    String xpath = n.getNodeValue();
                    if (!xpath.endsWith("/text()") && !xpath.contains("@"))
                        xpath += "/text()";
                    if (!useXPathForQuery)
                        addAlias(xpath, attName);
                    String path = xpath + "/";
                    SchemaElement on = null;
                    String op = null;
                    for (int i = 1; (i = path.indexOf('/', i)) != -1; i++) {
                        String pp = path.substring(0, i);
                        SchemaElement nn = getElement(pp);
                        if (nn == null) {
                            if (on == null) {
                                rootElement = new SchemaElement(pp.substring(1));
                                nn = rootElement;
                            } else {
                                int h = pp.lastIndexOf('/');
                                String lp = pp.substring(h + 1);
                                nn = new SchemaElement(lp);
                                if (lp.startsWith("@") || lp.equals("text()")) {
                                    nn.setDatatypeName(datatypeName);
                                    nn.setOperators(operators);
                                }
                                on.add(nn);
                            }
                        }
                        on = nn;
					}
                }
            }
        } catch (Exception e) {
            de.unidu.is.util.Log.error(e);
        }
    }

    /**
     * Loads the DDL file.
     *
     * @param ddlFile file with HyREX DDL
     * @return ddl
     */
    private static Document loadDDL(File ddlFile) {
        Document ddl = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new FileInputStream(ddlFile));
            is.setSystemId(ddlFile.getAbsoluteFile().toURL().toString());
            ddl = builder.parse(is);
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return ddl;
    }

    /**
     * Converts the datatype name to the PIRE notions.
     *
     * @param datatypeName name of the datatype
     * @return converted datatype name
     */
    public static String convertDatatypeName(String datatypeName) {
        if (datatypeName.equals("HyREX::HyPath::Datatype::Text::English"))
            return "Text";
        if (datatypeName.equals("HyREX::HyPath::Datatype::Name"))
            return "Name";
        if (datatypeName.equals("HyREX::HyPath::Datatype::Numeric"))
            return "Number";
        // year cannot be deduced, unfortunately
        return datatypeName;
    }

    /**
     * Converts the operator name to the PIRE notions.
     *
     * @param operator name of the operator
     * @return converted operator name
     */
    public static String convertOperatorName(String operator) {
        // datatype "text"
        if (operator.equals("stemen"))
            return TextDT.STEMEN;
        if (operator.equals("plaintexten"))
            return TextDT.NOSTEM;
        // datatype "year"
        if (operator.equals("equal"))
            return YearDT.EQUALS;
        return operator;
    }

    /**
     * Returns the default operators.
     *
     * @return property map (with multiple occurrences per key)
     */
    public static PropertyMap getDefaultOperators() {
        return defaultOperators;
    }

    /**
     * Returns the DDL document.
     *
     * @return DDL document
     */
    public Document getDDL() {
        return ddl;
    }

    /**
     * Sets the DDL document.
     *
     * @param ddl DDL document
     */
    public void setDDL(Document ddl) {
        this.ddl = ddl;
    }

    /**
     * Returns the regex used for filtering files in the directory.
     *
     * @return regex used for filtering
     */
    public String getFilterRegEx() {
        String filter = XMLUtilities.extract(ddl,
                "/hyrex/access/parameter[@name='expression']/@value");
        if (filter == null)
            return null;
        String[] f = filter.split("/");
        for (String s : f) {
            if (s.trim().length() > 0)
                filter = s.trim();
        }
        return filter;
    }

    /**
     * Returns a list containing the indexing directories.
     *
     * @return list containing File objects.
     */
    public List getIndexDirs() {
        List indexDirs = new LinkedList();
        try {
            NodeIterator nodeIt = XPathAPI.selectNodeIterator(getDDL(),
                    "/hyrex/access/parameter[@name=\"directories\"]/@value");
            for (Node node; (node = nodeIt.nextNode()) != null; ) {
                String indexDir = node.getNodeValue();
                indexDirs.add(new File(indexDir));
            }
        } catch (TransformerException e) {
            de.unidu.is.util.Log.error(e);
        }
        return indexDirs;
    }

    /**
     * Returns the base name from the DDL.
     *
     * @return base name
     */
    public String getBaseName() {
        return XMLUtilities.extract(ddl, "/hyrex/@base");
    }

    /**
     * Returns the base name from the DDL.
     *
     * @return base name
     */
    public String getClassName() {
        return XMLUtilities.extract(ddl, "/hyrex/@class");
    }

    /**
     * Adds default operators.
     */
    public void addDefaultOperators() {
        for (Object o : getXPaths()) {
            String xpath = (String) o;
            SchemaElement element = getElement(xpath);
            String datatype = convertDatatypeName(element.getDatatypeName());
            List operators = element.getOperators();
            List defs = defaultOperators.getAll(datatype);
            if (defs != null)
                operators.addAll(defs);
        }
    }

}
