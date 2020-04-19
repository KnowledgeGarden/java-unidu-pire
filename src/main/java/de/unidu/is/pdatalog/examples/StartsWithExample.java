// $Id: StartsWithExample.java,v 1.2 2005/03/18 09:54:49 nottelma Exp $
package de.unidu.is.pdatalog.examples;

import de.unidu.is.pdatalog.EDBRelation;
import de.unidu.is.pdatalog.RelationBase;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.util.DB;
import de.unidu.is.util.HSQLDBEmbeddedDB;

/**
 * A simple example.
 *
 * @author nottelma
 * @version $Revision: 1.2 $, $Date: 2005/03/18 09:54:49 $
 * @since 19-Oct-2004
 */
public class StartsWithExample {

    public static void main(String[] args) {
        DB db = new HSQLDBEmbeddedDB();
        RelationBase base = new RelationBase(db);

        new EDBRelation(base, "s", 1, true);
        base.add(Parser.parseFact("s(abc)."));
        base.add(Parser.parseFact("s(ab)."));
        base.add(Parser.parseFact("s(xabc)."));
        base.add(Parser.parseFact("s(abd)."));

        System.out.println(base.query("?- s(X) & startswith(X,ab)."));
    }
}
