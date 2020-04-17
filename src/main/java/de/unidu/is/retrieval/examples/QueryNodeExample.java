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

 
// $Id: QueryNodeExample.java,v 1.1 2005/02/25 14:33:29 nottelma Exp $
package de.unidu.is.retrieval.examples;

import de.unidu.is.retrieval.AndQueryNode;
import de.unidu.is.retrieval.OrQueryNode;
import de.unidu.is.retrieval.QueryCondition;

/**
 * Example for using Boolean query nodes
 * @author nottelma
 * @since 2005-02-25
 * @version $Revision: 1.1 $, $Date: 2005/02/25 14:33:29 $
 */
public class QueryNodeExample {

	public static void main(String[] args) {
		// (X or Y) and (X or (Z AND ZZ))
		// -> X OR (X AND Y) OR (X AND Z AND ZZ) OR (Y AND Z AND ZZ)
		QueryCondition x = new QueryCondition("/a/b", "bla", "x");
		QueryCondition y = new QueryCondition("/a/b", "bla", "y");
		QueryCondition z = new QueryCondition("/a/b", "bla", "z");
		QueryCondition zz = new QueryCondition("/a/b", "bla", "zz");
		AndQueryNode aa = new AndQueryNode();
		aa.add(z);
		aa.add(zz);
		OrQueryNode o1 = new OrQueryNode();
		o1.add(x);
		o1.add(y);
		OrQueryNode o2 = new OrQueryNode();
		o2.add(x);
		o2.add(aa);
		AndQueryNode a = new AndQueryNode();
		a.add(o1);
		a.add(o2);
		a.add(aa);
		System.out.println(a);
		System.out.println("DF: " + a.simplifiedNode().toDF().simplifiedNode());

		System.out.println(o1.isDF());
		System.out.println(o2.isDF());
		System.out.println(a.isDF());
		System.out.println(a.simplifiedNode().toDF().simplifiedNode().isDF());

		AndQueryNode a1 = new AndQueryNode();
		AndQueryNode a2 = new AndQueryNode();
		AndQueryNode a3 = new AndQueryNode();
		a1.add(z);
		a2.add(a1);
		a2.add(x);
		a2.add(o1);
		a3.add(a2);
		System.out.println("=> " + a3.toPrefix());
		System.out.println(a3.simplifiedNode().toPrefix());
	}
}
