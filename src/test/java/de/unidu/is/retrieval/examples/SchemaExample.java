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


// $Id: SchemaExample.java,v 1.1 2005/02/25 14:33:29 nottelma Exp $
package de.unidu.is.retrieval.examples;

import de.unidu.is.retrieval.Schema;
import de.unidu.is.retrieval.SchemaElement;
import junit.framework.TestCase;

import java.util.Collections;

/**
 * A schema example.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/02/25 14:33:29 $
 * @since 2005-02-25
 */
public class SchemaExample extends TestCase {

    public void test1() {
        SchemaElement a = new SchemaElement("a");
        SchemaElement b = new SchemaElement("b");
        SchemaElement c = new SchemaElement("c");
        SchemaElement x = new SchemaElement("@x", "Name", Collections
                .singletonList("plainname"));
        SchemaElement t1 = new SchemaElement("text()", "Text", Collections
                .singletonList("stemen"));
        SchemaElement t2 = new SchemaElement("text()", "Name", Collections
                .singletonList("plainname"));
        a.add(b);
        a.add(c);
        b.add(c);
        b.add(t1);
        c.add(t2);
        c.add(x);

        Schema schema = new Schema("S");
        schema.setRootElement(a);
        System.out.println(a);

        System.out.println(a.getXPaths());
        System.out.println(a.getElement("/a/c/text()"));
    }

}
