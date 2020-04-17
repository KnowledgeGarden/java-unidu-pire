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

// $Id: MomentsPIREExample.java,v 1.2 2005/03/19 16:12:57 nottelma Exp $
package de.unidu.is.retrieval.pire.examples;

import de.unidu.is.retrieval.pire.PIRE;
import de.unidu.is.statistics.Moments;

/**
 * An example for computing moments with PIRE.
 * 
 * @author Henrik Nottelmann
 * @since 01-Mar-2005
 * @version $Revision: 1.2 $, $Date: 2005/03/19 16:12:57 $
 */
public class MomentsPIREExample {

	public static void main(String[] args) {
		PIRE ir = PIREExampleUtils.createPIRE();
		PIREExampleUtils.indexDocuments(ir);
		ir.computeMoments();
		
		String QUERYID = "m42";
		ir.initQuery(QUERYID);
		ir.addMomentsCondition(QUERYID, "ti", "stemen", 0.7, "quick");
		ir.addMomentsCondition(QUERYID, "ti", "stemen", 0.3, "fox");
		Moments moments = ir.getMoments(QUERYID);
		System.out.println("expectation=" + moments.getExpectation());
		System.out.println("variance=" + moments.getVariance());
		ir.closeQuery(QUERYID);

		ir.removeIndex();
		System.exit(0);
	}

}