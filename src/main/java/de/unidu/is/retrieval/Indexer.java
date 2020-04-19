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


// $Id: Indexer.java,v 1.9 2005/02/25 14:33:29 nottelma Exp $
package de.unidu.is.retrieval;

import de.unidu.is.retrieval.hyrex.HyREXSchema;
import de.unidu.is.retrieval.pire.PDatalogIR;
import de.unidu.is.util.*;
import org.apache.commons.cli.*;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A class which can be called for indexing an XML collection.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.9 $, $Date: 2005/02/25 14:33:29 $
 * @since 20-Jan-2005
 */
public class Indexer {

    /**
     * The IR object.
     */
    protected IR ir;
    /**
     * If true, not output is given to STDOUT and STDERR.
     */
    protected boolean quiet;
    /**
     * If not null, this is the writer for the logfile.
     */
    protected PrintWriter out;
    /**
     * Directories to be scanned.
     */
    protected List dirs;
    /**
     * RegEx for files to be indexed.
     */
    protected String filter;

    /**
     * Creates a new object.
     *
     * @param args arguments
     * @throws IOException if an error occurs
     */
    public Indexer(String[] args) throws IOException {
        handleCommandLine(args);
    }

    public static void main(String[] args) {
        try {
            Indexer indexer = new Indexer(args);
            indexer.start();
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * Starts the indexing part.
     *
     * @throws IndexException if an exception occurs
     */
    public void start() throws IndexException {
        final List l = new LinkedList(); // #docs
        log("Loading documents");
        ir.initIndex();
        for (Object o : dirs) {
            File dir = (File) o;
            log("");
            log("Scanning directory " + dir);
            IOUtilities.doForAllFiles(dir, file -> {
                String fileName = file.getName();
                if (fileName.matches(filter)) {
                    l.add("");
                    String docID = fileName.replaceAll("\\.xml", "");
                    log(docID);
                    try {
                        Document doc = XMLUtilities.parse(file);
                        ir.addToIndex(docID, doc);
                    } catch (Exception e) {
                        log("-> Error");
                    }
                }
            });
        }
        log("");
        log("Complete index for " + l.size() + " documents");
        ir.computeIndex();
        log("Finished");
    }

    /**
     * Closes the log file.
     */
    public void close() {
        if (out != null)
            out.close();
    }

    /**
     * Display help message.
     *
     * @param options command-line options
     */
    private static void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("indexer", "Indexes an XML collection.", options,
                "", true);
    }

    /**
     * Create and return command-line options.
     *
     * @return command-line options
     */
    private static Options getOptions() {
        Options options = new Options();

        options.addOption("?", "help", false, "displays this help message");
        options.addOption("q", "quiet", false,
                "do not output anything to STDOUT and STDERR");
        {
            Option opt = new Option("l", "logfile", true,
                    "use the specified file for the output");
            opt.setArgName("file");
            options.addOption(opt);
        }
        //		{
        //			Option opt = new Option(
        //					"c",
        //					"class",
        //					true,
        //					"use specified class which extends de.unidu.is.retrieval.IR instead
        // of PDatalogIR");
        //			opt.setArgName("class name");
        //			options.addOption(opt);
        //		}
        {
            Option opt = new Option("n", "name", true,
                    "use specified collection name instead of combining base/class from HyREX DDL");
            opt.setArgName("collection name");
            options.addOption(opt);
        }
        {
            Option opt = new Option("u", "user", true,
                    "use specified user name for RDBMS");
            opt.setArgName("user");
            opt.setRequired(true);
            options.addOption(opt);
        }
        {
            Option opt = new Option("p", "password", true,
                    "use specified password for RDBMS");
            opt.setArgName("password");
            opt.setRequired(true);
            options.addOption(opt);
        }
        {
            Option opt = new Option("h", "host", true,
                    "use specified host for RDBMS");
            opt.setArgName("host");
            opt.setRequired(true);
            options.addOption(opt);
        }
        {
            Option opt = new Option("d", "db", true,
                    "use specified database for RDBMS");
            opt.setArgName("database");
            opt.setRequired(true);
            options.addOption(opt);
        }
        {
            Option opt = new Option(
                    "x",
                    "xml",
                    true,
                    "use specified directory for XML files (instead of extracting it from the HyREX DDL file)");
            opt.setArgName("dir");
            options.addOption(opt);
        }
        //		{
        //			Option opt = new Option(
        //					"u",
        //					"uri",
        //					true,
        //					"use specified uri for RDBMS (including host and db)");
        //			opt.setArgName("uri");
        //			options.addOption(opt);
        //		}
        {
            OptionGroup group = new OptionGroup();
            group.setRequired(true);
            {
                Option opt = new Option("1", "ddl", true,
                        "use specified HyREX DDL file");
                opt.setArgName("ddl file");
                group.addOption(opt);
            }
            {
                Option opt = new Option("2", "dtd", true, "use specified DTD");
                opt.setArgName("dtdl file");
                group.addOption(opt);
            }
            options.addOptionGroup(group);
        }

        return options;
    }

    /**
     * Handle command-line options
     *
     * @param args arguments
     * @throws IOException if an error occurs
     */
    private void handleCommandLine(String[] args) throws IOException {
        Options options = getOptions();
        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("?")) {
                showHelp(options);

            }
            quiet = line.hasOption("q");
            String logFile = line.getOptionValue("l");
            if (logFile != null) {
                try {
                    out = new PrintWriter(new FileWriter(logFile));
                } catch (IOException e) {
                    throw new IOException("Cannot write to logfile!");
                }
            }
            String user = line.getOptionValue("u");
            String password = line.getOptionValue("p");
            String host = line.getOptionValue("h");
            String database = line.getOptionValue("d");
            String collection = line.getOptionValue("n");
            //String className = line.getOptionValue("c");
            //			String uri = line.getOptionValue("u");
            //			if (uri != null) {
            //				// todo: split uri
            //			}
            String dirString = line.getOptionValue("x");
            if (dirString != null)
                dirs = Collections.singletonList(dirString.split(","));
            filter = "\\.xml$";
            String ddl = line.getOptionValue("1");
            String dtd = line.getOptionValue("2");
            Schema schema = null;
            if (ddl != null) {
                HyREXSchema hyrex = new HyREXSchema(new File(ddl));
                schema = hyrex;
                if (hyrex.usesXPathForQuery())
                    hyrex.addAliases();
                if (collection == null) {
                    Document doc = ((HyREXSchema) schema).getDDL();
                    collection = hyrex.getBaseName() + "_" + hyrex.getClassName();
                    filter = hyrex.getFilterRegEx();
                    dirs = hyrex.getIndexDirs();
                }
            }
            if (dtd != null) {
                try {
                    schema = new DTDSchema(collection, new File(dtd));
                    schema.addAliases();
                } catch (IOException e) {
                    throw new IOException("DTD file " + dtd
                            + " cannot be loaded!");
                }
            }

            // set default operators if no operator is set
            for (Object o : schema.getAliases()) {
                String alias = (String) o;
                SchemaElement element = schema.getElement(alias);
                String datatypeName = HyREXSchema.convertDatatypeName(element.getDatatypeName());
                List operators = element.getOperators();
                if (operators.isEmpty()) {
                    PropertyMap defOps = HyREXSchema.getDefaultOperators();
                    List def = defOps.getAll(datatypeName);
                    if (def != null) {
                        operators.addAll(def);
                        element.setOperators(operators);
                    }
                }
            }

            log("Indexing " + collection);
            log("DB: jdbc:mysql://" + host + "/" + database + ", user=" + user
                    + ", password=" + password);
            log("Schema attributes: "
                    + StringUtilities.implode(schema.getAliases(), ", "));
            log("Load XML files from " + StringUtilities.implode(dirs, ", "));
            log("Applying filter " + filter);
            log("");

            DB db = new MySQLDB(host, database, user, password);
            ir = new PDatalogIR(db, collection);
            ir.registerSchema(schema);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            showHelp(options);
            System.exit(1);
        }
    }

    /**
     * Logs the given object to STDOUT and/or a log file.
     *
     * @param o object
     */
    private void log(Object o) {
        if (!quiet)
            System.out.println(o);
        if (out != null)
            out.println(o);
    }

}
