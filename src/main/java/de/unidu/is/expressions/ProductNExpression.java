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

 
// $Id: ProductNExpression.java,v 1.4 2005/02/21 17:29:18 huesselbeck Exp $
package de.unidu.is.expressions;

import java.util.List;

/**
 * An expression for the product of a list of arguments, i.e.
 * arg1*arg2*arg3*...
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-27
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:18 $
 */
public class ProductNExpression extends ArgNExpression {

	/**
	 * Creates a new expression object.
	 * 
	 * @param args list of arguments of this expression
	 */
	public ProductNExpression(List args) {
		super("*",args);
	}

}
