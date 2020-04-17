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

 
// $Id: NormalDistributionSourceTest.java,v 1.6 2005/03/18 22:49:31 nottelma Exp $
package test.de.unidu.is.gnuplot;

import java.io.BufferedReader;
import java.io.IOException;

import de.unidu.is.gnuplot.GnuplotCall;
import de.unidu.is.gnuplot.NormalDistributionSource;
import de.unidu.is.statistics.NormalDistribution;

/**
 * @author nottelma
 * @since Aug 2, 2003
 * @version $Revision: 1.6 $, $Date: 2005/03/18 22:49:31 $
 */
public class NormalDistributionSourceTest extends SourceTest {

	private NormalDistributionSource normalSource;
	private BufferedReader reader;
	private final double EXP = 1;
	private final double VAR = Math.pow(2,2);

	/**
	 * Constructor for NormalDistributionSourceTest.
	 * @param arg0
	 */
	public NormalDistributionSourceTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		NormalDistribution dist = new NormalDistribution(EXP, VAR);
		normalSource = new NormalDistributionSource(TITLE, dist);
		source = normalSource; // for testing Source
	}

	public void testGetCommand() throws IOException,NumberFormatException {
		String command = normalSource.getCommand();
		reader = new BufferedReader(GnuplotCall.call("f(x)=" + command + "\nprint f(1)\nprint f(2)\n"));
		assertEquals(Double.parseDouble(reader.readLine()),0.1994711,0.00001);
		assertEquals(Double.parseDouble(reader.readLine()),0.1760326,0.00001);
		assertEquals(reader.readLine(),"");
		reader.close();
		reader = null;
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		if(reader!=null)
			reader.close();
	}

}
