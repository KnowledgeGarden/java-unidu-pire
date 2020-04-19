// $Id: StartsWithExample.java,v 1.2 2005/03/18 09:54:49 nottelma Exp $
package de.unidu.is.pdatalog.examples;

import de.unidu.is.pdatalog.EDBRelation;
import de.unidu.is.pdatalog.RelationBase;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.util.HSQLDBEmbeddedDB;
import junit.framework.TestCase;

/**
 * A simple example.
 *
 * @author nottelma
 * @version $Revision: 1.2 $, $Date: 2005/03/18 09:54:49 $
 * @since 19-Oct-2004
 */
public class StartsWithExample extends TestCase {

    public void test1() {
        RelationBase base = new RelationBase(new HSQLDBEmbeddedDB());

        new EDBRelation(base, "s", 1, true);
        base.add(Parser.parseFact("s(abc)."));
        base.add(Parser.parseFact("s(ab)."));
        base.add(Parser.parseFact("s(xabc)."));
        base.add(Parser.parseFact("s(abd)."));

        /* TODO check if this is actual correct result */
        assertEquals("[('ab') | (1.0*PROB)., ('abc') | (1.0*PROB)., ('abd') | (1.0*PROB).]",
                base.query("?- s(X) & startswith(X,ab).").toString());
    }
}
