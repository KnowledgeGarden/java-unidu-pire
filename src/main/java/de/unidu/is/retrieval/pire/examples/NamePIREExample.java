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

// $Id: NamePIREExample.java,v 1.6 2005/03/01 09:21:33 nottelma Exp $
package de.unidu.is.retrieval.pire.examples;

import de.unidu.is.retrieval.pire.PIRE;
import de.unidu.is.retrieval.pire.dt.NameDT;

/**
 * An example for using names in PIRE.
 * 
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/03/01 09:21:33 $
 */
public class NamePIREExample {

	public static void main(String[] args) {
		PIRE ir = PIREExampleUtils.createPIRE();

		ir.initIndex();
		ir.addToIndex("doc1");
		ir.addToIndex("doc1", "name", "nottelmann henrik nottelmann henrik");
		ir.computeIndex();

		final String QUERYID = "42";
		ir.initQuery(QUERYID);
		ir.addCondition(QUERYID, "name", NameDT.PLAINNAME, 1, "nottelmann");
		ir.computeProbs(QUERYID);
		System.out.println(ir.getResult(QUERYID, 10));
		ir.closeQuery(QUERYID);
		
		ir.removeIndex();
		System.exit(0);
	}

}