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


// $Id: Example.java,v 1.9 2005/02/28 22:27:56 nottelma Exp $
package de.unidu.is.retrieval.hyrex;

import de.unidu.is.retrieval.*;
import de.unidu.is.util.StopWatch;
import de.unidu.is.util.XMLUtilities;
import junit.framework.TestCase;
import org.w3c.dom.Document;

import java.util.List;

/**
 * A simple example for the HyREX IR interface. It uses the BIBDB HyREX
 * server used by our web server.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.9 $, $Date: 2005/02/28 22:27:56 $
 * @since 2004-01-04
 */
public class Example extends TestCase {

    public void test1() throws IndexException, UnsupportedQueryException, DocumentNotFoundException {
        StopWatch watch = new StopWatch();
        Retriever ir =
                new HyREXRetriever(
                        "www.is.informatik.uni-duisburg.de",
                        10099,
                        "DB",
                        "bibdb");
        watch.start();
        XIRQLQuery query =
                new XIRQLStringQuery("42", "/#PCDATA $author:plainname$ \"Nottelmann\"", 22);
        List result = ir.getResult(query);
        System.out.println(result);
        for (Object o : result) {
            ProbDoc doc = (ProbDoc) o;
            Document document = ir.getDocument(doc);
            System.out.println(XMLUtilities.toString(document));
        }
        System.out.println(result.size() + " results");
        System.out.println(ir.getDocuments(result));
        watch.stop();
        System.out.println(watch);
        ir.close();

    }

}
