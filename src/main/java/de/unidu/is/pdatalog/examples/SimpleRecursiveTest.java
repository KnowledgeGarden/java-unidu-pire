package de.unidu.is.pdatalog.examples;

import de.unidu.is.pdatalog.EDBComputedRelation;
import de.unidu.is.pdatalog.EDBRelation;
import de.unidu.is.pdatalog.IDBRelation;
import de.unidu.is.pdatalog.LessThanRelation;
import de.unidu.is.pdatalog.RelationBase;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.util.HSQLDBEmbeddedDB;

// $Id: SimpleRecursiveTest.java,v 1.3 2005/02/28 22:27:55 nottelma Exp $

/**
 * A simple test for recursive rules.
 * 
 * @author nottelma
 * @version $Revision: 1.3 $, $Date: 2005/02/28 22:27:55 $
 */
public class SimpleRecursiveTest {

	public static void main(String[] args) {
		RelationBase base = new RelationBase(new HSQLDBEmbeddedDB());

		EDBRelation bar = new EDBRelation(base, "bar", 2);
		EDBComputedRelation lt = new LessThanRelation(base);
		IDBRelation foo= new IDBRelation(base, "foo", 2);
		System.out.println(base);

		base.add(Parser.parseFact("0.3 bar('1','2')."));
		base.add(Parser.parseFact("0.5 bar('2','3')."));
		base.add(Parser.parseFact("0.7 bar('3','4')."));

		Rule rule =
			Parser.parseRule("foo(X,Y) :- bar(X,Y).");
		rule.setOptimizable(true); 
		base.compute(foo,rule);
		
		rule =
			Parser.parseRule("foo(X,Z) :- foo(X,Y) & bar(Y,Z).");
		rule.setOptimizable(true);
		base.computeRecursively(foo,rule);
	}
	
}