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

//$Id: Test.java,v 1.2 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.examples;

import de.unidu.is.pdatalog.*;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.util.HSQLDBEmbeddedDB;
import de.unidu.is.util.StopWatch;

/**
 * A simple example.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.2 $, $Date: 2005/02/28 22:27:55 $
 */
public class Test {

    public static void main(String[] args) {
        RelationBase base = new RelationBase(new HSQLDBEmbeddedDB());

        EDBRelation tf = new EDBRelation(base, "tf", 3);
        EDBComputedRelation lt = new LessThanRelation(base);
        IDBRelation docid = new IDBRelation(base, "docid", 1);
        IDBRelation text = new IDBRelation(base, "textindex", 2);
        IDBRelation dl = new IDBRelation(base, "dl", 2);

        IDBRelation dummy = new IDBRelation(base, "dummy", 1);
        System.out.println(base);

        StopWatch watch = new StopWatch();
        watch.start();

        base.add(Parser.parseFact("tf('d1','foo','42')."));
        base.add(Parser.parseFact("tf('d1','bar','10')."));
        base.add(Parser.parseFact("tf('d2','foo','3')."));
        base.dump(tf);

        Rule rule = Parser
                .parseRule("textindex(D,T) :- tf(D,T,TF) | (&exp(1)*TF) .");
        rule.setOptimizable(true);
        System.out.println(rule);
        text.compute(rule);
        base.dump(text);

        rule = Parser.parseRule("docid(D) :- tf(D).");
        System.out.println(rule);
        docid.compute(rule);
        base.dump(docid);

        rule = Parser
                .parseRule("dl(D,DL) :- docID(D) & sum(DL,D,{ tf(D,_,#) }).");
        rule.setOptimizable(true);
        System.out.println(rule);
        dl.compute(rule);
        base.dump(dl);

        rule = Parser.parseRule("dummy(D) :- dl(D,DL) & lt(DL,'6').");
        rule.setOptimizable(true);
        System.out.println(rule);
        dummy.compute(rule);
        base.dump(dummy);

        watch.stop();
        System.out.println(watch);

        System.out.println("THE END");
    }
}