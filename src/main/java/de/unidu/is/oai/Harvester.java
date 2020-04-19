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


// $Id: Harvester.java,v 1.6 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: Harvester.java,v $
 * Revision 1.6  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.5  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.4  2005/02/21 15:10:38  huesselbeck
 * removes status line(s)
 *
 * Revision 1.3  2005/02/21 13:11:47  huesselbeck
 * added "$Id: Harvester.java,v 1.6 2005/02/21 17:29:20 huesselbeck Exp $"
 *
 * Revision 1.2  2005/01/24 11:44:27  huesselbeck
 * replaced e.printStackTrace by de.unidu.is.util.Log.error()
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.8  2004/07/12 12:08:22  fischer
 * undid previous tries at quick parsing
 *
 * Revision 1.7  2004/07/12 12:04:02  fischer
 * ListRecords with resumption token: chunks only
 * Sets: not supported with ListRecords and GetRecord
 *
 * Revision 1.6  2004/07/07 12:50:40  fischer
 * kdm greeting
 *
 * Revision 1.5  2004/06/08 20:57:27  fischer
 * *** empty log message ***
 *
 * Revision 1.4  2004/06/08 14:07:01  fischer
 * added ListRecords
 *
 * Revision 1.3  2004/06/04 08:56:53  fischer
 * *** empty log message ***
 *
 * Revision 1.2  2004/06/03 16:07:11  fischer
 * added ListSets, fromId, toId
 *
 * Revision 1.1  2004/06/02 16:03:31  fischer
 * a quick shot at OAI harvesting
 *
 */
package de.unidu.is.oai;

import org.apache.log4j.Category;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * @author fischer
 * @version $Revision: 1.6 $
 * <p>
 * An OAI Harvester
 */
public class Harvester {

    private static final Category logger =
            Category.getInstance(Harvester.class.getName());

    private String url = null;
    private String userAgent =
            "Java OAI Harvester, by University of Duisburg-Essen, Germany";
    private String fromEmail = "insert your e-mail here";

    private String directory = ".";
    private Collection sets = null;
    private Collection formats = null;

    private boolean harvestSingles = false;
    private boolean harvestSets = false;
    private boolean chunkList = false;

    private String fromId = null;
    private String toId = null;

    private Identify identify = null;

    public Harvester(String url, String userAgent, String fromEmail)
            throws OAIException {
        setUserAgent(userAgent);
        setFromEmail(fromEmail);
        setUrl(url);
    }

    public static void main(String[] args) {

        Map params = new Hashtable();
        String destination = ".";
        String url = null;

        List testing = Collections.synchronizedList(new LinkedList());
        //testing.add("http://www.aim25.ac.uk/cgi-bin/oai/OAI2.0");
        testing.add("http://digital.library.upenn.edu/webbin/OAI-celebration");
        //testing.add("http://hal.ccsd.cnrs.fr/oai/oai.php");
        //testing.add("http://rocky.dlib.vt.edu/~jcdlpix/cgi-bin/OAI2.0/test/oai.pl");
        //testing.add("http://cs1.ist.psu.edu/cgi-bin/oai.cgi");
        testing.add("-destination=/home/fischer/tmp/harvester");
        testing.add("-fromEmail=fischer@is.informatik.uni-duisburg.de");
        // testing.add("-sets");
        // testing.add("-singles");
        testing.add("-chunk");
        //testing.add("-fromId=oai:celebration:alcott/rose");
        //testing.add("-toId=oai:celebration:alcott/rose");
        testing.add("-schema=oai_dc");
        args = (String[]) testing.toArray(new String[0]);

        if (args.length > 0) {
            for (String s : args) {
                String arg = s;
                if (arg.startsWith("-")) { // option
                    arg = arg.substring(1);
                    int index = arg.indexOf('=');
                    if (index > 0) {
                        String what = arg.substring(0, index);
                        String how = arg.substring(index + 1);
                        params.put(what.trim().toLowerCase(), how);
                    } else {
                        params.put(arg.trim().toLowerCase(), "yes");
                    }
                } else {
                    url = arg.trim();
                }
            }
        } else {
            logger.error("No URL specified.");
            System.exit(1);
        }

        if ((url == null) || (url.isEmpty())) {
            logger.error("No URL specified.");
            System.exit(1);
        }

        // for testing: echo parameters
        logger.debug("url=" + url);
        for (Object o : params.keySet()) {
            String what = (String) o;
            String how = (String) params.get(what);
            logger.debug(what + "='" + how + "'");
        }

        // analyze parameters
        String dir = (String) params.get("destination");
        if (dir != null) {
            destination = dir;
        }
        String userAgent = (String) params.get("useragent");
        if (userAgent == null)
            userAgent = "OAI Harvester by UniDuE";
        String fromEmail = (String) params.get("fromemail");
        if (fromEmail == null)
            fromEmail = "anonymous@somewhere";
        boolean withSets = (params.get("sets") != null);
        boolean singles = (params.get("singles") != null);
        boolean chunks = (params.get("chunk") != null);
        String setSpec = (String) params.get("set");
        String schemaName = (String) params.get("schema");
        String fromId = (String) params.get("fromid");
        String toId = (String) params.get("toid");

        Harvester harvester;
        try {
            harvester = new Harvester(url, userAgent, fromEmail);
            harvester.setDirectory(destination);
            if (schemaName != null) {
                harvester.setSchemaName(schemaName);
            }
            if (setSpec != null) {
                withSets = true;
                harvester.setSetSpec(setSpec);
            }
            harvester.setFromId(fromId);
            harvester.setToId(toId);
            harvester.setHarvestSets(withSets);
            harvester.setHarvestSingles(singles);
            harvester.setHarvestChunks(chunks);
        } catch (Exception e) {
            harvester = null;
            logger.error(e.getMessage());
            de.unidu.is.util.Log.error(e);
        }

        if (harvester != null) {
            logger.debug("Harvester initialised");
            try {
                harvester.harvest();
            } catch (OAIException e) {
                logger.error(e.getMessage());
                de.unidu.is.util.Log.error(e);
            }
            logger.info("Harvester finished");
        }
        System.exit(0);
    }

    public void setFromEmail(String fromEmail) {
        logger.debug("fromEmail=" + fromEmail);
        this.fromEmail = fromEmail;
    }

    public void setUserAgent(String userAgent) {
        logger.debug("userAgent=" + userAgent);
        this.userAgent = userAgent;
    }

    public String getId() {
        String myId = identify.getId();
        if (myId == null) {
            myId = identify.getUrl();
        }
        return myId;
    }

    public void initialiseSets() {
        sets = new ArrayList();
        ListSets ls;
        String resumptionToken;
        try {
            ls = new ListSets(url, userAgent, fromEmail);
        } catch (OAIException e) {
            ls = null;
        }
        while (ls != null) {
            sets.addAll(ls.getSetSpecs());
            resumptionToken = ls.getResumptionToken();
            if (resumptionToken != null) {
                try {
                    ls =
                            new ListSets(
                                    url,
                                    userAgent,
                                    fromEmail,
                                    resumptionToken);
                } catch (OAIException e) {
                    ls = null;
                }
            } else {
                ls = null;
            }
        }
    }

    public void initialiseMetadataFormats() {
        formats = new ArrayList();
        ListMetadataFormats ls;
        String resumptionToken;
        try {
            ls = new ListMetadataFormats(url, userAgent, fromEmail);
        } catch (OAIException e) {
            ls = null;
        }
        while (ls != null) {
            formats.addAll(ls.getMetadataPrefixes());
            resumptionToken = ls.getResumptionToken();
            if (resumptionToken != null) {
                try {
                    ls =
                            new ListMetadataFormats(
                                    url,
                                    userAgent,
                                    fromEmail,
                                    resumptionToken);
                } catch (OAIException e) {
                    ls = null;
                }
            } else {
                ls = null;
            }
        }
    }

    public void setSchemaName(String schemaName) {
        formats = new LinkedList();
        formats.add(schemaName); // just one schema to harvest
    }

    public void setSetSpec(String setSpec) {
        sets = new LinkedList();
        sets.add(setSpec); // just one set to harvest
    }

    public void setDirectory(String directory) throws OAIException {
        // create directory, if necessary
        try {
            File dir = new File(directory);
            if (dir.mkdirs()) {
                logger.info(
                        "harvest(" + getId() + "): Created directory " + directory);
            }
        } catch (Exception e) {
            throw new OAIException(
                    "setDirectory("
                            + getId()
                            + "): Could not create directory "
                            + directory
                            + ", got: "
                            + e.getMessage());
        }
        this.directory = directory;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
        if ((fromId != null)
                && (toId != null)
                && (fromId.compareTo(toId) > 0)) {
            logger.warn("fromId=" + fromId + " is greater than toId=" + toId);
        }
    }

    public void setToId(String toId) {
        this.toId = toId;
        if ((fromId != null)
                && (toId != null)
                && (fromId.compareTo(toId) > 0)) {
            logger.warn("fromId=" + fromId + " is greater than toId=" + toId);
        }
    }

    /**
     * harvest all records, all specified formats
     */
    public void harvest() throws OAIException {
        if (harvestSets) {
            throw new RuntimeException("Sets are not yet supported");
			/*
			initialiseSets();
			if (sets.size()<=0) {
				logger.warn("No sets found at "+getUrl()+", won't harvest");
				return;
			}
			// harvest for all sets
			Iterator it=getSetsIterator();
			while (it.hasNext()) {
				String setSpec=(String) it.next();
				harvest(setSpec);
			}
			*/
        } else {
            harvest(null); // no sets
        }
    }

    public void harvest(String setSpec) throws OAIException {
        // harvest for all schemas
        Iterator it = getFormatsIterator();
        while (it.hasNext()) {
            String schemaName = (String) it.next();
            harvest(setSpec, schemaName);
        }
    }

    /**
     * @param directory
     * @param schema
     */
    private void harvest(String setSpec, String schemaName)
            throws OAIException {
        String parameters =
                ((setSpec != null) ? "set '" + setSpec + "', " : "")
                        + " schema "
                        + schemaName
                        + " from "
                        + getUrl()
                        + " to "
                        + directory;
        // create subdirectory, if necessary
        String subdir = makePath(setSpec, schemaName);
        makeDir(subdir);
        if (harvestSingles) {
            logger.info("Harvesting " + parameters + ", one file per record");
            harvestSinglesToDir(subdir, schemaName, setSpec);
        } else if (chunkList) {
            logger.info("Harvesting " + parameters + ", chunk the list according to the resumption tokens");
            harvestChunksToDir(subdir, schemaName, setSpec);
        } else {
            logger.info("Harvesting " + parameters + ", one file for all");
            harvestListToDir(subdir, schemaName, setSpec);
        }
        logger.info("Completed harvesting " + parameters);
    }

    /**
     * @param subdir
     * @param schemaName
     */
    private void harvestListToDir(
            String subdir,
            String schemaName,
            String setSpec) throws OAIException {
        logger.warn("harvestListToDir: Cannot combine chunks to one list yet, harvesting chunks instead.");
        harvestChunksToDir(subdir, schemaName, setSpec);
		/*
			String resumptionToken = null;
			ListRecords lr =
				new ListRecords(url, userAgent, fromEmail, schemaName);
			String header=lr.getHeader();
			String footer=lr.getFooter();
			String filename=makeFilename(getId())+".xml";
			if ((header==null) || (footer==null)) {
				throw new OAIException("harvestListToDir:"+
				((header==null)?" header is null":"")+
				((footer==null)?" footer is null":""));
			} else {
				logger.info(
					"Write header from "
						+ getUrl()
						+ " to "
						+ subdir+File.separator+filename);
				saveToFile(
					subdir,
					filename,
					header);
			}
			while (lr != null) {
				// get the records
				logger.info(
					"Write records from "
						+ getUrl()
						+ " to "
						+ subdir+File.separator+filename);
				appendToFile(subdir,filename,lr.getContent());
				resumptionToken = lr.getResumptionToken();
				// more?
				if (resumptionToken == null) {
					lr = null;
				} else {
					lr =
						new ListRecords(
							url,
							userAgent,
							fromEmail,
							schemaName,
							resumptionToken);
				}
			}
			logger.info(
				"Write footer from "
					+ getUrl()
					+ " to "
					+ subdir+File.separator+filename);
			appendToFile(
				subdir,
				filename,
				footer);
				*/
    }

    /**
     * harvest the records in list chunks, but write each list to a separate file
     *
     * @param subdir
     * @param schemaName
     */
    private void harvestChunksToDir(
            String subdir,
            String schemaName,
            String setSpec) throws OAIException {
        String resumptionToken;
        ListRecords lr =
                new ListRecords(url, userAgent, fromEmail, schemaName);
        int chunk = 0;
        while (lr != null) {
            String filename = makeFilename(getId()) + chunk + ".xml";
            // get the records
            logger.info(
                    "Write records from "
                            + getUrl()
                            + " to "
                            + subdir + File.separator + filename);
            saveToFile(subdir, filename, lr.getRequestResult());
            resumptionToken = lr.getResumptionToken();
            logger.debug("resumptionToken=" + resumptionToken);
            // more?
            if (resumptionToken == null) {
			} else {
                lr =
                        new ListRecords(
                                url,
                                userAgent,
                                fromEmail,
                                schemaName,
                                resumptionToken);
                chunk++;
            }
            lr = null;
        }
    }

    /**
     * harvest the records individually,
     * one file per record
     *
     * @param subdir
     * @param schemaName
     * @throws OAIException
     */
    private void harvestSinglesToDir(
            String subdir,
            String schemaName,
            String setSpec)
            throws OAIException {
        // alternate between listIdentifiers and getRecord
        String resumptionToken;
        ListIdentifiers li =
                new ListIdentifiers(url, userAgent, fromEmail, schemaName);
        while (li != null) {
            // get the IDs
            Iterator ids = li.getIdentifiersIterator();
            while (ids.hasNext()) {
                String identifier = (String) ids.next();
                if (isInRequestedRange(identifier)) {
                    GetRecord gr;
                    try {
                        gr =
                                new GetRecord(
                                        url,
                                        userAgent,
                                        fromEmail,
                                        identifier,
                                        schemaName);

                    } catch (OAIException e) {
                        logger.warn(
                                "Record "
                                        + identifier
                                        + " from "
                                        + getUrl()
                                        + " could not be harvested: "
                                        + e.getMessage());
                        gr = null;
                    }
                    if (gr != null) {
                        if (gr.isDeleted()) {
                            logger.debug(
                                    "Record "
                                            + gr.getId()
                                            + " from "
                                            + getUrl()
                                            + " has been deleted, it will not be saved");
                        } else {
                            logger.info(
                                    "Save record "
                                            + gr.getId()
                                            + " from "
                                            + getUrl()
                                            + " to "
                                            + subdir);
                            saveToFile(
                                    subdir,
                                    makeFilename(gr.getId()) + ".xml",
                                    gr.getRequestResult());
                        }
                    }
                }
            }
            resumptionToken = li.getResumptionToken();
            if (resumptionToken == null) {
                li = null;
            } else {
                li =
                        new ListIdentifiers(
                                url,
                                userAgent,
                                fromEmail,
                                schemaName,
                                resumptionToken);
            }
        }
    }

    private boolean isInRequestedRange(String identifier) {
        if ((fromId != null) && (fromId.compareTo(identifier) > 0)) {
            return false;
        }
        return (toId == null) || (toId.compareTo(identifier) >= 0);
    }

    private static void makeDir(String path) throws OAIException {
        try {
            File dir = new File(path);
            if (dir.mkdirs()) {
                logger.info("Created directory " + path);
            }
        } catch (Exception e) {
            throw new OAIException(
                    "Could not create directory "
                            + path
                            + ", got: "
                            + e.getMessage());
        }
    }

    private String makePath(String setSpec, String schemaName) {
        String subpath = "";
        if (setSpec != null) {
            // build a subpath by replacing all ":" in the setSpec by File.separator
            subpath = setSpec;
            subpath = subpath.replaceAll(":", File.separator);
            // add a leading separator
            subpath = File.separator + subpath;
        }
        return directory + File.separator + schemaName + subpath;
    }

    protected Iterator getFormatsIterator() {
        if (formats == null) {
            initialiseMetadataFormats();
        }
        return formats.iterator();
    }

    protected Iterator getSetsIterator() {
        if (sets == null) {
            initialiseSets();
        }
        return sets.iterator();
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) throws OAIException {
        this.url = url;
        logger.debug("url=" + url);
        identify = new Identify(url, userAgent, fromEmail);
    }

    private static void saveToFile(
			String directory,
			String filename,
			String content) {
        String fn = directory + File.separator + filename;

        try {
            FileWriter fw = new FileWriter(fn);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            logger.error(
                    "saveToFile("
                            + fn
                            + ") : "
                            + "Could not write to file "
                            + fn
                            + ": "
                            + e.getMessage());
        }
    }

    private static void appendToFile(
			String directory,
			String filename,
			String content) {
        String fn = directory + File.separator + filename;

        try {
            FileWriter fw = new FileWriter(fn, true);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            logger.error(
                    "appendToFile("
                            + fn
                            + ") : "
                            + "Could not append to file "
                            + fn
                            + ": "
                            + e.getMessage());
        }
    }

    /**
     * @param identifier
     * @return
     */
    private static String makeFilename(String identifier) {
        StringBuilder ret = new StringBuilder();
        String p = identifier.toLowerCase();
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if ((c == '.') || (File.separatorChar == c))
                c = '_';
            if (Character.isUnicodeIdentifierStart(c)
                    || Character.isDigit(c)
                    || (c == '_'))
                ret.append(c);
        }
        return ret.toString();
    }

    /**
     * @param singles
     */
    public void setHarvestSingles(boolean singles) {
        this.harvestSingles = singles;
    }

    /**
     * @param withSets
     */
    public void setHarvestSets(boolean withSets) {
        this.harvestSets = withSets;
    }

    /**
     * @param chunks
     */
    public void setHarvestChunks(boolean chunks) {
        this.chunkList = chunks;
    }

}
