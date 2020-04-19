// $Id: PIREExampleUtils.java,v 1.1 2005/03/01 09:21:33 nottelma Exp $
package de.unidu.is.retrieval.pire.examples;

import de.unidu.is.retrieval.*;
import de.unidu.is.retrieval.hyrex.HyREXSchema;
import de.unidu.is.retrieval.pire.PDatalogIR;
import de.unidu.is.retrieval.pire.PIRE;
import de.unidu.is.retrieval.pire.dt.NameDT;
import de.unidu.is.retrieval.pire.dt.TextDT;
import de.unidu.is.retrieval.pire.dt.YearDT;
import de.unidu.is.util.DB;
import de.unidu.is.util.HSQLDBEmbeddedDB;
import de.unidu.is.util.SystemUtilities;
import de.unidu.is.util.XMLUtilities;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods for PIRE examples.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/03/01 09:21:33 $
 * @since 01-Mar-2005
 */
public class PIREExampleUtils {

    /**
     * Creates and returns a new DB object.
     *
     * @return DB object
     */
    public static DB createDB() {
        return new HSQLDBEmbeddedDB();
    }

    /**
     * Creates an PIRE object and sets the attributes "ti" (Text), "au" (Name)
     * and "py" (Year).
     *
     * @return PIRE object
     */
    public static PIRE createPIRE() {
        PIRE ir = new PIRE(createDB(), "Test");
        List textPredicates = new ArrayList();
        textPredicates.add(TextDT.STEMEN);
        textPredicates.add(TextDT.NOSTEM);
        List yearPredicates = new ArrayList();
        yearPredicates.add(YearDT.LT);
        yearPredicates.add(YearDT.LE);
        yearPredicates.add(YearDT.GT);
        yearPredicates.add(YearDT.LE);
        yearPredicates.add(YearDT.EQ);
        yearPredicates.add(YearDT.VLT);
        yearPredicates.add(YearDT.VGT);
        yearPredicates.add(YearDT.VEQ);
        ir.registerAttribute("ti", TextDT.NAME, textPredicates);
        ir.registerAttribute("py", YearDT.NAME, yearPredicates);
        ir.registerAttribute("au", NameDT.NAME, Collections
                .singletonList(NameDT.PLAINNAME));
        return ir;
    }

    /**
     * Adds example documens to the PIRE index.
     *
     * @param ir PIRE object
     */
    public static void indexDocuments(PIRE ir) {
        ir.initIndex();
        ir.addToIndex("doc1");
        ir.addToIndex("doc1", "ti", "how do we know this");
        ir.addToIndex("doc1", "au", "Henrik Nottelmann");
        ir.addToIndex("doc2");
        ir.addToIndex("doc2", "ti", "quick quick quick");
        ir.addToIndex("doc3");
        ir.addToIndex("doc3", "ti", "we");
        ir.addToIndex("doc4");
        ir.addToIndex("doc4", "py", "1998");
        ir.addToIndex(
                "doc4",
                "ti",
                "The quick brown fox jumps over the fox fence");
        ir.addToIndex("doc4", "au", "Henrik Nottelmann");
        ir.addToIndex("doc5");
        ir.addToIndex("doc5", "py", "1999");
        ir.addToIndex("doc5", "ti", "quick and dirty is quick");
        ir.computeIndex();
    }

    /**
     * Creates an IR object and sets the <code>examples/ddl.xml</code> schema.
     *
     * @return IR object
     */
    public static IR createIR() {
        IR ir = new PDatalogIR(createDB(), "XMLTEST", "/tmp");
        Schema schema = new HyREXSchema(XMLUtilities.parse(SystemUtilities.getConfAsStream("examples/ddl.xml"), null));
        ir.registerSchema(schema);
        return ir;
    }

    /**
     * Adds example documens to the IR index.
     *
     * @param ir IR object
     * @throws IndexException
     * @throws DocumentNotStorableException
     * @throws DocumentMismatchException
     */
    public static void indexXMLDocuments(IR ir) throws IndexException,
            DocumentNotStorableException, DocumentMismatchException {
        ir.initIndex();
        ir
                .addToIndex(
                        "doc1",
                        XMLUtilities
                                .parseText("<doc><title value=\"Henrik\"/><text>Hallo</text></doc>"));
        ir
                .addToIndex(
                        "doc2",
                        XMLUtilities
                                .parseText("<doc><title value=\"Norbert\"/><text>Hallo</text></doc>"));
        ir.computeIndex();
    }

    /**
     * Performs retrieval w.r.t. the specified query, and prints the results to
     * STDOUT.
     *
     * @param retriever retrieval object
     * @param query     query
     * @throws UnsupportedQueryException
     * @throws IndexException
     * @throws DocumentNotFoundException
     */
    public static void retrieveAndPrint(Retriever retriever, Query query)
            throws UnsupportedQueryException, IndexException,
            DocumentNotFoundException {
        List result = retriever.getResult(query);
        printResult(retriever, result);
    }

    /**
     * Prints the results to STDOUT.
     *
     * @param retriever retrieval object
     * @param result    result list (items are ProbDoc instances)
     * @throws DocumentNotFoundException
     */
    public static void printResult(Retriever retriever, List result)
            throws DocumentNotFoundException {
        System.out.println(result);
        for (Object o : result) {
            ProbDoc doc = (ProbDoc) o;
            Document document = retriever.getDocument(doc);
            System.out.println(XMLUtilities.toString(document));
        }
    }

    /**
     * Returns an example weighted sum query.
     *
     * @return example weighted sum query
     */
    public static Query getWSumQuery() {
        return new WSumQuery(
                "42",
                "wsum(0.2,/doc/title/@value $stemen$ \"Henrik\",0.5,/#PCDATA $title:stemen$ \"Henrik\",0.3,/#PCDATA $text:stemen$ \"Hallo\")",
                2);
    }

    /**
     * Returns an example Boolean-style query.
     *
     * @return example Boolean-style query
     */
    public static Query getBooleanQuery() {
        QueryCondition c1 = new QueryCondition("/doc/title/@value", "stemen",
                "Henrik");
        QueryCondition c2 = new QueryCondition("/doc/text/text()", "stemen",
                "Hallo");
        QueryCondition c3 = new QueryCondition("text", "stemen", "Hallo");
        AndQueryNode a = new AndQueryNode();
        a.add(c1);
        a.add(c2);
        OrQueryNode o = new OrQueryNode();
        o.add(a);
        o.add(c3);
        return new StructuredQuery("42", o, 2);
    }
}