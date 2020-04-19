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


// $Id: AndQueryNode.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.*;

/**
 * A query node representing an AND.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 * @since 2004-05-22
 */
public class AndQueryNode extends BooleanQueryNode {

    /**
     * Creates a new object.
     */
    public AndQueryNode() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param children children children nodes
     */
    public AndQueryNode(Set children) {
        super(children);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.BooleanQueryNode#getConnector()
     */
    protected String getConnector() {
        return "$and$";
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.BooleanQueryNode#sameClass(de.unidu.is.retrieval.QueryNode)
     */
    protected boolean sameClass(QueryNode node) {
        return node instanceof AndQueryNode;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.QueryNode#toDF()
     */
    public QueryNode toDF() {
        if (isDF())
            return this;
        // c contains OR nodes or conditions, all already in DF
        List c = new LinkedList();
        for (Iterator it = iterator(); it.hasNext(); ) {
            QueryNode node = (QueryNode) it.next();
            QueryNode dfNode = node.toDF();
            c.add(dfNode);
        }
        // convert in array for multiplication
        QueryNode[][] nodes = new QueryNode[c.size()][];
        int i = 0;
        for (Iterator it = c.iterator(); it.hasNext(); i++) {
            QueryNode node = (QueryNode) it.next();
            if (node instanceof OrQueryNode) {
                int num = 0;
                for (Iterator iter = node.iterator(); iter.hasNext(); num++)
                    iter.next();
                nodes[i] = new QueryNode[num];
                num = 0;
                for (Iterator iter = node.iterator(); iter.hasNext(); num++)
                    nodes[i][num] = (QueryNode) iter.next();
            }
            if (node instanceof QueryCondition) {
                nodes[i] = new QueryNode[1];
                nodes[i][0] = node;
            }
        }
        // multiplication (distributivity law)
        Set cc = new LinkedHashSet();
        int[] nums = new int[nodes.length];
        L:
        while (true) {
            Set l = new LinkedHashSet();
            for (int j = 0; j < nums.length; j++)
                l.add(nodes[j][nums[j]]);
            cc.add(new AndQueryNode(l));
            for (int j = 0; j < nums.length; j++) {
                nums[j]++;
                if (nums[j] < nodes[j].length)
                    break;
                if (j == nums.length - 1)
                    break L;
                nums[j] = 0;
            }
        }
        return new OrQueryNode(cc);
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
            isDF &= (node instanceof QueryCondition)
                    || ((node instanceof AndQueryNode) && node.isDF());
        }
        return isDF;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.unidu.is.retrieval.BooleanQueryNode#newNode()
     */
    protected BooleanQueryNode newNode() {
        return new AndQueryNode();
    }

}
