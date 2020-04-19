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


// $Id: Constant.java,v 1.6 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import de.unidu.is.expressions.StringExpression;

/**
 * A pDatalog++ constant.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.6 $, $Date: 2005/02/28 22:27:55 $
 * @since 2003-11-03
 */
public class Constant extends StringExpression {

    /**
     * Creates a new instance.
     *
     * @param arg constant value
     */
    public Constant(Object arg) {
        super(arg.toString());
    }

}
