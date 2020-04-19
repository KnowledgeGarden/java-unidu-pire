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

// $Id: Schema.java,v 1.17 2005/03/02 10:42:42 nottelma Exp $
package de.unidu.is.retrieval;

import de.unidu.is.util.StringUtilities;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import javax.xml.transform.TransformerException;
import java.util.*;

/**
 * A schema definition. A schema consists of a list of attributes, which are
 * extracted from the XML document using XPath expressions (similar to the HyREX
 * DDL file).
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.17 $, $Date: 2005/03/02 10:42:42 $
 * @since 2004-01-03
 */
public class Schema {

    /**
     * A mapping from XPath expressions (as strings) onto attribute names (as
     * strings). This is only used if <code>useXPathForQuery<code>
     * equals false.
     */
    private final Map xpathToAliases;
    /**
     * A mapping from an attribute names onto XPath expressions (as strings) (as
     * strings). This is only used if <code>useXPathForQuery<code>
     * equals false.
     */
    private final Map aliasesToXPaths;
    /**
     * The schema name.
     */
    protected String schemaName;
    /**
     * The root element of the schema.
     */
    protected SchemaElement rootElement;
    /**
     * A flag specifying if an XPath expression is used in the query, or if
     * attributes are used.
     */
    protected boolean useXPathForQuery;

    /**
     * Creates a new, empty schema instance.
     */
    public Schema() {
        this(null);
    }

    /**
     * Creates a new, empty schema instance.
     *
     * @param schemaName schema name
     */
    public Schema(String schemaName) {
        this.schemaName = schemaName;
        xpathToAliases = new HashMap();
        aliasesToXPaths = new HashMap();
        useXPathForQuery = true;
    }

    /**
     * Returns the schema name.
     *
     * @return schema name
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Sets the schema name.
     *
     * @param schemaName schema name
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Returns the root schema element.
     *
     * @return root schema element
     */
    public SchemaElement getRootElement() {
        return rootElement;
    }

    /**
     * Sets the root schema element.
     *
     * @param element new root schema element
     */
    public void setRootElement(SchemaElement element) {
        rootElement = element;
    }

    /**
     * Returns true iff XPath expressions are used in the query instead of
     * attributes.
     *
     * @return true iff XPath expressions are used in the query instead of
     * attributes
     */
    public boolean usesXPathForQuery() {
        return useXPathForQuery;
    }

    /**
     * Specifies if XPath expressions are used in the query instead of
     * attributes.
     *
     * @param b if true, XPath expressions are used in the query
     */
    public void setUseXPathForQuery(boolean b) {
        useXPathForQuery = b;
    }

    /**
     * Returns the schema element for the specified path (XPath expression or
     * alias).
     *
     * @param path path (XPath expression or alias)
     * @return corresponding schema element
     */
    public SchemaElement getElement(String path) {
        if (rootElement == null)
            return null;
        return rootElement.getElement(getXPath(path));
    }

    /**
     * Returns a list with the XPath expressions.
     *
     * @return list with the XPath expressions (as strings)
     */
    public List getXPaths() {
        return rootElement == null ? null : rootElement.getXPaths();
    }

    /**
     * Returns a list with all aliases.
     *
     * @return list with the aliases (as strings)
     */
    public List getAliases() {
        List l = new ArrayList(aliasesToXPaths.keySet());
        Collections.sort(l);
        return l;
    }

    /**
     * Returns the unified path for queries. If XPath expressions are used in
     * queries, then it is an XPath expression, otherwise it is an alias.
     *
     * @param path path (XPath expression or alias)
     * @return unified path
     */
    public String getPath(String path) {
        return useXPathForQuery ? getXPath(path) : getAlias(path);
    }

    /**
     * Unifies the path in the specified query condition. If XPath expressions
     * are used in queries, then it is an XPath expression, otherwise it is an
     * alias.
     *
     * @param condition query condition
     */
    public void unifyPathForCondition(QueryCondition condition) {
        condition.setPath(getPath(condition.getPath()));
    }

    /**
     * Returns the alias for the specified path.
     *
     * @param path path (XPath expression or alias)
     * @return alias
     */
    public String getAlias(String path) {
        if (!path.startsWith("/"))
            return path;
        List l = getAliasesForXPath(path);
        if (l == null || l.isEmpty())
            return null;
        return (String) l.get(0);
    }

    /**
     * Returns the aliases for the specified path.
     *
     * @param path XPath expression)
     * @return aliases
     */
    public List getAliasesForXPath(String path) {
        return (List) xpathToAliases.get(path);
    }

    /**
     * Returns the XPath expression for the specified path.
     *
     * @param alias path (XPath expression or alias)
     * @return XPath expression
     */
    public String getXPath(String alias) {
        if (alias.startsWith("/"))
            return alias;
        List l = getXPathsForAlias(alias);
        if (l == null || l.isEmpty())
            return null;
        return (String) l.get(0);
    }

    /**
     * Returns the XPath expressions for the specified path.
     *
     * @param alias alias
     * @return XPath expressions
     */
    public List getXPathsForAlias(String alias) {
        return (List) aliasesToXPaths.get(alias);
    }

    /**
     * Adds an alias.
     *
     * @param xpath XPath expression
     * @param alias alias for the specified XPath expression
     */
    public void addAlias(String xpath, String alias) {
        List l1 = getXPathsForAlias(xpath);
        if (l1 == null) {
            l1 = new ArrayList();
            xpathToAliases.put(xpath, l1);
        }
        l1.add(alias);
        List l2 = getXPathsForAlias(alias);
        if (l2 == null) {
            l2 = new ArrayList();
            aliasesToXPaths.put(alias, l2);
        }
        l2.add(xpath);
    }

    /**
     * Adds standard aliases (use converted XPaths).
     */
    public void addAliases() {
        if (useXPathForQuery) {
            for (Object o : getXPaths()) {
                String xpath = (String) o;
                String alias = convPath(xpath);
                addAlias(xpath, alias);
            }
        }
    }

    /**
     * Removes all aliases.
     */
    public void clearAliases() {
        aliasesToXPaths.clear();
        xpathToAliases.clear();
    }

    /**
     * Converts a XPath path by removing the first element name and "/text()",
     * transforming "/" into "__", "-" into "_" and "@" into "at":
     *
     * @param path XPath
     * @return converted path
     */
    public static String convPath(String path) {
        int h = path.indexOf('/', 1);
        if (h != -1) {
            path = path.substring(h + 1);
        }
        path = StringUtilities.replace(path, "-", "_");
        path = StringUtilities.replace(path, "/text()", "");
        path = StringUtilities.replace(path, "/", "__");
        path = StringUtilities.replace(path, "@", "at");
        return path;
    }

    /**
     * Extract all values from the document for the given attribute.
     *
     * @param document XML document
     * @param att      attribute (XPath or alias)
     * @return list with all values (as strings)
     */
    public List extractValues(Document document, String att) {
        String xpath = getXPath(att);
        List list = new LinkedList();
        try {
            NodeIterator nodeIt = XPathAPI.selectNodeIterator(document, xpath);
            for (Node node; (node = nodeIt.nextNode()) != null; )
                list.add(node.getNodeValue());
        } catch (TransformerException e) {
            de.unidu.is.util.Log.error(e);
        }
        return list;
    }

    /**
     * Returns a hierarchical view of this schema.
     *
     * @return hierarchical view of this schema
     */
    public String getHierarchicalView() {
        StringBuffer buf = new StringBuffer(1000);
        buf.append("Schema: ").append(getSchemaName()).append("\n\n");
        getRootElement().appendHierarchicalView(buf, 0);
        return buf.toString();
    }

}