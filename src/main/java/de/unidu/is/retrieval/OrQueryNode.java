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


// $Id: OrQueryNode.java,v 1.7 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A query node representing an OR.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/28 22:27:55 $
 * @since 2004-05-22
 */
public class OrQueryNode extends BooleanQueryNode {

    /**
     * Creates a new object.
     */
    public OrQueryNode() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param children children children nodes
     */
    public OrQueryNode(Set children) {
        super(children);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.BooleanQueryNode#getConnector()
     */
    protected String getConnector() {
        return "$or$";
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.BooleanQueryNode#sameClass(de.unidu.is.retrieval.QueryNode)
     */
    protected boolean sameClass(QueryNode node) {
        return node instanceof OrQueryNode;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#toDF()
     */
    public QueryNode toDF() {
        if (isDF())
            return this;
        Set c = new LinkedHashSet();
        for (Iterator it = iterator(); it.hasNext(); ) {
            QueryNode node = (QueryNode) it.next();
            QueryNode dfNode = node.toDF();
            c.add(dfNode);
        }
        return new OrQueryNode(c);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#isDF()
     */
    public boolean isDF() {
        boolean isDF = true;
        for (Iterator it = iterator(); it.hasNext(); ) {
            QueryNode node = (QueryNode) it.next();
            isDF &= node.isDF();
        }
        return isDF;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.BooleanQueryNode#newNode()
     */
    protected BooleanQueryNode newNode() {
        return new OrQueryNode();
    }

}
