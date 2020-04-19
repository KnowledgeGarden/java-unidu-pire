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


// $Id: SyntaxCheck.java,v 1.1 2005/02/28 22:27:55 nottelma Exp $

package de.unidu.is.pdatalog.examples;

import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.parser.Parser;

/**
 * A simple class for testing the syntax.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/02/28 22:27:55 $
 * @since 2003-10-21
 */
public class SyntaxCheck {

    public static void main(String[] args) {
        String str;

        str = "dl(D,DL) :- docid(D) & sum(DL,TF,{tf(D,X,TF)}).";

        Rule rule = Parser.parseRule(str);
        System.out.println(str);
        System.out.println(rule);
    }

}
