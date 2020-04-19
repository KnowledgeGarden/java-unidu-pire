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

// $Id: Log.java,v 1.14 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;

/**
 * Provides utiliy methods for tje LOG4J framework.
 * <p>
 * <p>
 * This class expects the LOG4J configuration file in <code>conf/log4j</code>.
 * Additional configuration files can be loaded.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.14 $, $Date: 2005/03/14 17:33:13 $
 * @since 2001-08-27
 */
public class Log {

    /**
     * Semaphore.
     */
    private static final Object sem = new Object();

    /**
     * Flag specifies whether this class was initialised (lazy initialisation of
     * the log4j configuration).
     */
    private static boolean wasInit = false;

    /**
     * The LOG4J config file.
     */
    private static File confFile;

    /**
     * Returns a logger for the specified class.
     *
     * @param c class
     * @return logger
     */
    public static Logger getLogger(Class c) {
        return getLogger(c.getName());
    }

    /**
     * Returns a logger for the specified name.
     *
     * @param name logger name
     * @return logger
     */
    public static Logger getLogger(String name) {
        init();
        return org.apache.log4j.Logger.getLogger(name);
    }

    /**
     * Initialises this class.
     * <p>
     * <p>
     * This method loads the log4j configuration (if not already done) from
     * <code>conf/log4j</code>.
     */
    public static void addConfigFile(URL url) {
        synchronized (sem) {
            org.apache.log4j.PropertyConfigurator.configure(url);
            wasInit = true;
        }
    }

    /**
     * Initialises this class.
     * <p>
     * <p>
     * This method loads the log4j configuration (if not already done) from
     * <code>conf/log4j</code>.
     */
    private static void init() {
        if (!wasInit) {
            synchronized (sem) {
                if (!wasInit) {
                    wasInit = true;
                    Logger.getRootLogger().removeAllAppenders();
                    try {
                        if (confFile != null) {
                            addConfigFile(SystemUtilities
                                    .getResourceURL(confFile.toString()));
                        } else {
                            URL[] urls = SystemUtilities.getConfURLs("log4j");
                            for (int i = urls.length - 1; i >= 0; i--)
                                addConfigFile(urls[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.err); // cannot
                        // log
                        // yet
                    }
                }
            }
        }
    }

    /**
     * Logs an exception to the unidu.error logger.
     *
     * @param ex exception to be logged
     */
    public static void error(Throwable ex) {
        error(ex, "");
    }

    /**
     * Logs a message to the unidu.error logger.
     *
     * @param msg message to be logged
     */
    public static void error(String msg) {
        if (msg != null && msg.length() > 0)
            getLogger("unidu.error").error(msg);
    }

    /**
     * Logs an exception to the unidu.error logger.
     *
     * @param ex exception to be logged
     */
    public static void error(Throwable ex, String msg) {
        if (msg != null && msg.length() > 0)
            System.err.println(msg);
        ex.printStackTrace(System.err);
        getLogger("unidu.error").error(msg, ex);
    }

    /**
     * Sets the LOG4J config file.
     *
     * @param file LOG4J config file
     */
    public static void setConfFile(File file) {
        confFile = file;
    }

}