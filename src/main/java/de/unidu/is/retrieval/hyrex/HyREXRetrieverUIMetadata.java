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


// $Id: HyREXRetrieverUIMetadata.java,v 1.4 2005/02/21 17:29:23 huesselbeck Exp $
package de.unidu.is.retrieval.hyrex;

import de.unidu.is.retrieval.AbstractRetrieverUIMetadata;
import de.unidu.is.retrieval.Retriever;

/**
 * A class describing metadata for a MIND retriever which can be used by
 * UIs for a reasonable handling of queries and documents.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:23 $
 * @since 2004-03-18
 */
public class HyREXRetrieverUIMetadata extends AbstractRetrieverUIMetadata {

    /**
     * Creates a new instance.
     *
     * @param retriever associated retriever
     */
    public HyREXRetrieverUIMetadata(Retriever retriever) {
        super(retriever);
    }

    /**
     * Returns all collections supported by the retriever.<p>
     * <p>
     * The collections are queried from the MIND dispatcher.
     *
     * @return attray of collection names, can be empty
     */
    public String[] getCollections() {
        if (collections == null)
            collections = new String[]{"HyREX"};
        return collections;
    }

    /**
     * Returns the attributes names with could be used for sorting.<p>
     * <p>
     * This implementation only uses "title", "author", "year", "yearperiod".
     *
     * @return attributes names with could be used for sorting
     * @see de.unidu.is.retrieval.RetrieverUIMetadata#getSortModel()
     */
    public String[] getSortModel() {
        if (sortModel == null) {
            sortModel =
                    new String[]{"title", "author", "year", "source"};
        }
        return sortModel;
    }

    /**
     * Returns all attribute names related to a potential title field.<p>
     * <p>
     * This can be used by a GUI for displaying the document title.
     *
     * @return attribute names related to a potential title field
     */
    public String[] getTitleAttributeNames() {
        return new String[]{"title"};
    }

    /**
     * Returns all attribute names related to a potential author field.<p>
     * <p>
     * This can be used by a GUI for displaying the document authors.
     *
     * @return attribute names related to a potential author field
     */
    public String[] getAuthorAttributeNames() {
        return new String[]{"author"};
    }

    /**
     * Returns all attribute names related to a potential year field.<p>
     * <p>
     * This can be used by a GUI for displaying years associated
     * with a document.
     *
     * @return attribute names related to a potential year field
     */
    public String[] getYearAttributeNames() {
        return new String[]{"year"};
    }

}
