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


// $Id: SystemUtilities.java,v 1.19 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;

/**
 * A collection of system utility methods.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.19 $, $Date: 2005/02/21 17:29:29 $
 * @since 2003-06-20
 */
public class SystemUtilities {

    /**
     * Returns a reader for the first file with the given name
     * (prefixed with <code>conf/</conf>).<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path, without "conf" prefix
     * @return corresponding reader
     */
    public static InputStream getConfAsStream(String relativePath) {
        return SystemUtilities.class.getResourceAsStream("/" + relativePath);
    }

    /**
     * Returns a URL for the first file with the given name
     * (prefixed with <code>conf/</conf>).<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     *
     * @param relativePath relative path, without "conf" prefix
     * @return corresponding URL
     */
    public static URL getConfURL(String relativePath) {
        String sep = System.getProperty("file.separator");
        System.out.println("System.Utils.getconf " + relativePath);
        return getResourceURL("conf" + sep + relativePath);
    }

    /**
     * Returns an array of URLs for all files with the given name
     * (prefixed with <code>conf/</conf>).<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path, without "conf" prefix
     * @return array of corresponding URLs
     */
    public static URL[] getConfURLs(String relativePath) {
        String sep = System.getProperty("file.separator");
        return getResourceURLs("conf" + sep + relativePath);
    }

    /**
     * Returns an array of readers for all files with the given name
     * (prefixed with <code>conf/</conf>).<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path, without "conf" prefix
     * @return array of corresponding readers
     */
    public static Reader[] getConfAsStreams(String relativePath) {
        String sep = System.getProperty("file.separator");
        return getResourceAsStreams("conf" + sep + relativePath);
    }

    /**
     * Returns a reader for the first file with the given name.<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path
     * @return corresponding reader
     */
    public static Reader getResourceAsStream(String relativePath) {
        System.out.println("SysUtil.getResourceAsStream " + relativePath);
        try {
            return new InputStreamReader(
                    getResourceURL(relativePath).openStream());
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return null;
    }

    /**
     * Returns a URL for the first file with the given name.<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path
     * @return corresponding URL
     */
    public static URL getResourceURL(String relativePath) {
        URL u = SystemUtilities.class.getResource("/" + relativePath);
        return u;
//		//relativePath = "/" + relativePath;
//		System.out.println("SysUtil.getResURL "+relativePath);
//		String x = "";
//		try {
//			File f = new File(".");
//			//System.out.println("TMP "+f.getAbsolutePath());
//			x = f.getAbsolutePath();
//			x = x.substring(0, (x.length()-1));
//			//MUST test for maven /target
//			//Because this codebase has been mavenized, it will now look in the wrong place
//			// for any resource.
//			//We have to perform surgery on the path it picks up
//			//All of this is a hack (park)
//			if (x.indexOf("target/classes") > -1) {
//				x = x.substring(0, (x.length() - "target/classes/".length()));
//			}
//			x += relativePath;
//			x = "file://"+x;
//			System.out.println("YYZ "+x);
//			URL url = new URL(x);
//			// for JARs and directories on the classpath
//			//URL url = SystemUtilities.class.getClassLoader().getResource(relativePath);
//			//url = ClassLoader.getSystemResource(relativePath);
//			System.out.println("System.Utils.getResURL "+relativePath+" | "+url);
//			if (url == null) {
//				// for parent directories
//				for (Enumeration enumer = ClassLoader.getSystemResources(".");
//					enumer.hasMoreElements();
//					) {
//					URL u = (URL) enumer.nextElement();
//					File dir = new File(u.getFile()).getParentFile();
//					File file = new File(dir, relativePath);
//					if (file.exists()) {
//						url = file.toURL();
//						break;
//					}
//				}
//			}
//			return url;
//		} catch (Exception ex) {
//			de.unidu.is.util.Log.error(ex);
//		}
//		return null;
    }

    /**
     * Returns an array of URLs for all files with the given name.<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path
     * @return array of corresponding URLs
     */
    public static URL[] getResourceURLs(String relativePath) {
        List urls = new ArrayList();
        try {
            // "de" necessary for working with Jars
            for (Enumeration enumer = ClassLoader.getSystemResources("de");
                 enumer.hasMoreElements();
            ) {
                String u = ((URL) enumer.nextElement()).toExternalForm();
                u = u.substring(0, u.lastIndexOf('/'));
                InputStream is = null;
                try {
                    URL url = new URL(u + "/" + relativePath);
                    is = url.openStream();
                    urls.add(url);
                } catch (Exception ex) {
                } finally {
                    if (is != null)
                        is.close();
                }
                u = u.substring(0, u.lastIndexOf('/'));
                is = null;
                try {
                    URL url = new URL(u + "/" + relativePath);
                    is = url.openStream();
                    urls.add(url);
                } catch (Exception ex) {
                } finally {
                    if (is != null)
                        is.close();
                }
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return (URL[]) urls.toArray(new URL[0]);
    }

    /**
     * Returns an array of readers for all files with the given name.<p>
     * <p>
     * This method scans the classpath. For each JAR, the file must be
     * located in the beginning (<code>jar:file://...!/file</code>). For
     * each directory, the file must be located in that directoy or in its
     * direct parent directory.
     *
     * @param relativePath relative path
     * @return array of corresponding readers
     */
    public static Reader[] getResourceAsStreams(String relativePath) {
        List streams = new ArrayList();
        try {
            // "de" necessary for working with Jars
            for (Enumeration enumer = ClassLoader.getSystemResources("de");
                 enumer.hasMoreElements();
            ) {
                String u = ((URL) enumer.nextElement()).toExternalForm();
                u = u.substring(0, u.lastIndexOf('/'));
                try {
                    URL url = new URL(u + "/" + relativePath);
                    streams.add(new InputStreamReader(url.openStream()));
                } catch (Exception ex) {
                }
                u = u.substring(0, u.lastIndexOf('/'));
                try {
                    URL url = new URL(u + "/" + relativePath);
                    streams.add(new InputStreamReader(url.openStream()));
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return (Reader[]) streams.toArray(new Reader[0]);
    }

    /**
     * Returns the current date in ISO format "yyyymmdd".
     *
     * @return current date
     */
    public static String currentISODate() {
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return StringUtilities.toString(year, 4)
                + StringUtilities.toString(month, 2)
                + StringUtilities.toString(day, 2);
    }

}
