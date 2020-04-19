// $Id: Example.java,v 1.2 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.examples;

import de.unidu.is.pdatalog.EDBRelation;
import de.unidu.is.pdatalog.IDBRelation;
import de.unidu.is.pdatalog.RelationBase;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.util.DB;
import de.unidu.is.util.HSQLDBEmbeddedDB;

/**
 * A simple example.
 *
 * @author nottelma
 * @version $Revision: 1.2 $, $Date: 2005/02/28 22:27:55 $
 * @since 19-Oct-2004
 */
public class Example {

    public static void main(String[] args) {
        DB db = new HSQLDBEmbeddedDB();
        RelationBase base = new RelationBase(db);


        new EDBRelation(base, "xx", 2, true);
        IDBRelation rel = new IDBRelation(base, "xxx", 2, true);

        base.add(Parser.parseFact("xx(1,2)."));
        base.add(Parser.parseFact("xx(1,3)."));
        base.add(Parser.parseFact("xx(2,4)."));
        base.add(Parser.parseFact("xx(2,5)."));
        base.add(Parser.parseFact("xx(3,4)."));
        base.add(Parser.parseFact("xx(4,6)."));

        Rule r1 = Parser.parseRule("xxx(X,Z) :- xx(X,Z).");
        r1.setOptimizable(true);
        base.compute(rel, r1, false);
        Rule r2 = Parser.parseRule("xxx(X,Z) :- xxx(X,Y) & xxx(Y,Z).");
        base.computeRecursively(rel, r2);

        System.out.println(base.queryRelation("xxx", 2));
        System.out.println(base.query("?- xx(Y,X)."));
        System.out.println(base.query("?- xx(Y,~)."));

        System.out.println(base.query("?- log(Z,10) & xx(A,B)."));
    }
}
