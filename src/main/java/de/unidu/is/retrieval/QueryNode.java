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

// $Id: QueryNode.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.retrieval;

import java.util.Iterator;

/**
 * A node in a query representation, as the root of a subtree.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 * @since 2004-07-16
 */
public interface QueryNode {

    /**
     * Converts the node and its subtree into disjunctive form.
     *
     * @return node in disjunctive form (or this node, if no simplication is
     * required)
     */
    QueryNode toDF();

    /**
     * Returns true iff the node is in disjunctive form.
     *
     * @return true iff the node is in disjunctive form
     */
    boolean isDF();

    /**
     * Returns the node in prefix notation.
     *
     * @return node in prefix notation
     */
    String toPrefix();

    /**
     * Simplifies this node.
     *
     * @return simplified node (or this node, if no simplication is required)
     */
    QueryNode simplifiedNode();

    /**
     * Clones this node.
     *
     * @return cloned node
     */
    QueryNode cloneNode();

    /**
     * Returns an iterator over the children of this node.
     *
     * @return iterator over children nodes
     */
    Iterator iterator();

}