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


// $Id: Schema.java,v 1.6 2005/02/21 17:29:21 huesselbeck Exp $

package de.unidu.is.oai;

/**
 * @author fischer
 * @version $Revision: 1.6 $, $Date: 2005/02/21 17:29:21 $
 */

public class Schema implements Comparable {

    /**
     * Constants
     */
    protected static String oaiPrefix = "oai:";
    protected static int oaiDelimiter = ':';


    /***************************************************
     * Members
     ***************************************************/

    protected final String prefix;

    /**
     * The url of a DTD or XSD for this schema
     */
    protected final String url;

    /**
     * The url of a namespace for this schema
     */
    protected final String namespace;

    /***************************************************
     * Constructors
     ***************************************************/
    public Schema(String prefix, String url, String namespace) {
        this.prefix = prefix;
        this.url = url;
        this.namespace = namespace;
    }

    /***************************************************
     * protected methods
     ***************************************************/

    /***************************************************
     * public methods
     ***************************************************/

    public String getUrl() // url
    {
        return url;
    }

    public String getNamespace() // namespace
    {
        return namespace;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean equals(Object o) {
        return ((o instanceof Schema) &&
                (this.getPrefix().equals(((Schema) o).getPrefix())));
    }

    public int compareTo(Object o) {
        Schema s = (Schema) o; // this throws a ClassCastException, if o is not a Schema
        return this.getPrefix().compareTo(s.getPrefix());
    }

}
