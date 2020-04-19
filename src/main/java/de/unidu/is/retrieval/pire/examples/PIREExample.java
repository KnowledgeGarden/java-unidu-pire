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

// $Id: PIREExample.java,v 1.6 2005/03/01 09:21:33 nottelma Exp $
package de.unidu.is.retrieval.pire.examples;

import de.unidu.is.retrieval.pire.PIRE;
import de.unidu.is.util.StopWatch;

/**
 * An example for using PIRE.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/01 09:21:33 $
 */
public class PIREExample {

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        PIRE ir = PIREExampleUtils.createPIRE();
        PIREExampleUtils.indexDocuments(ir);
        watch.stop();
        System.out.println("Indexing: " + watch);
        System.out.println();
        String QUERYID;

        // query #1
        watch.reset();
        watch.start();
        QUERYID = "1";
        ir.initQuery(QUERYID);
        ir.addCondition(QUERYID, "ti", "stemen", 0.7, "quick");
        ir.addCondition(QUERYID, "au", "plainname", 0.3, "nottelmann");
        ir.computeProbs(QUERYID);
        System.out.println(QUERYID + ":");
        System.out.println(ir.getResult(QUERYID, 10));
        ir.closeQuery(QUERYID);
        watch.stop();
        System.out.println(watch);
        System.out.println();

        // query #2
        watch.reset();
        watch.start();
        QUERYID = "2";
        ir.initQuery(QUERYID);
        ir.addCondition(QUERYID, "ti", "stemen", 0.5, "quick");
        ir.addCondition(QUERYID, "ti", "containsNoStem", 0.3, "fence");
        ir.addCondition(QUERYID, "py", "lt", 0.2, "1999");
        ir.computeProbs(QUERYID);
        System.out.println(QUERYID + ":");
        System.out.println(ir.getResult(QUERYID, 10));
        ir.closeQuery(QUERYID);
        watch.stop();
        System.out.println(watch);
        System.out.println();

        // query #3
        watch.reset();
        watch.start();
        QUERYID = "3";
        ir.initQuery(QUERYID);
        ir.addCondition(QUERYID, "ti", "stemen", 0.5, "quick");
        ir.addCondition(QUERYID, "ti", "stemen", 0.2, "fox");
        ir.addCondition(QUERYID, "ti", "nostem", 0.3, "fence");
        ir.computeProbs(QUERYID);
        System.out.println(QUERYID + ":");
        System.out.println(ir.getResult(QUERYID, 10));
        ir.closeQuery(QUERYID);
        watch.stop();
        System.out.println(watch);
        System.out.println();

        // vague operators do not work yet
        //		// query #4
        //		watch.reset();
        //		watch.start();
        //		QUERYID = "4";
        //		ir.initQuery(QUERYID);
        //		ir.addCondition(QUERYID, "py", "vgt", 1, "2002");
        //		ir.computeProbs(QUERYID);
        //		System.out.println(QUERYID + ":");
        //		System.out.println(ir.getResult(QUERYID, 10));
        //		ir.closeQuery(QUERYID);
        //		watch.stop();
        //		System.out.println(watch);
        //		System.out.println();

        ir.removeIndex();
        System.exit(0);

    }

}