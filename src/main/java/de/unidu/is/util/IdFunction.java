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

 
// $Id: IdFunction.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

/**
 * The identity function.
 * 
 * @author Henrik Nottelmann
 * @since 2004-06-21
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 */
public class IdFunction extends LearnableFunction{

	/* (non-Javadoc)
	 * @see de.unidu.is.util.Function#apply(double)
	 */
	public double apply(double x) {
		return x;
	}

	/* (non-Javadoc)
	 * @see de.unidu.is.util.Function#getParameterNames()
	 */
	public String[] getParameterNames() {
		return new String[0];
	}
	
	/* (non-Javadoc)
	 * @see de.unidu.is.util.LearnableFunction#getFunction()
	 */
	public String getFunction() {
		return "f(x)=x";
	}


}
