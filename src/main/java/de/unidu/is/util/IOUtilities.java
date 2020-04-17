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

// $Id: IOUtilities.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A class with utility methods for IO.
 * 
 * @author Henrik Nottelmann
 * @since 2004-05-19
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 */
public class IOUtilities {

	/**
	 * An interface whose instances can handle a file (for arbitrary usage).
	 * 
	 * @author Henrik Nottelmann
	 * @since 2004-05-19
	 */
	public static interface FileHandler {
		/**
		 * Will be called for a file.
		 * 
		 * @param file file to be handled.
		 */
		public void handle(File file);
	}

	/**
	 * Loads the content of the file and returns it as a string.
	 * 
	 * @param file
	 *                   file to be read
	 * @return string content of the file
	 * @throws FileNotFoundException
	 *                   if the file is not found
	 * @throws IOException
	 *                   if an IO error occurs
	 */
	public static String load(File file) throws FileNotFoundException,
			IOException {
		return load(new FileReader(file));
	}

	/**
	 * Loads the content of the URL and returns it as a string.
	 * 
	 * @param url
	 *                   URL whoose content is to be read
	 * @return string content of the URL
	 * @throws IOException
	 *                   if an IO error occurs
	 */
	public static String load(URL url) throws IOException {
		return load(new InputStreamReader(url.openStream()));
	}

	/**
	 * Loads the content of the reader and returns it as a string.
	 * 
	 * @param reader
	 *                   reader
	 * @return string content of the reader
	 * @throws IOException
	 *                   if an IO error occurs
	 */
	public static String load(Reader reader) throws IOException {
		StringBuffer buf = new StringBuffer(1000);
		BufferedReader in = new BufferedReader(reader);
		for (String line; (line = in.readLine()) != null;)
			buf.append(line).append("\n");
		in.close();
		return buf.toString();
	}

	/**
	 * Returns all files (recursively) in the specified directory. Might yield
	 * an OOME in very large directories, then it is better to use
	 * <code>doForAllFiles(File, FileHandler)</code>.
	 * 
	 * @param dir
	 *                   directory to search
	 * @return all files under this directory.
	 */
	public static File[] findFiles(File dir) {
		List files = new LinkedList();
		List dirQueue = new ArrayList();
		dirQueue.add(dir);
		while (!dirQueue.isEmpty()) {
			dir = (File) dirQueue.get(0);
			dirQueue.remove(0);
			File[] f = dir.listFiles();
			for (int i = 0; i < f.length; i++) {
				File file = f[i];
				if (file.isDirectory())
					dirQueue.add(file);
				else
					files.add(file);
			}
		}
		return (File[]) files.toArray(new File[files.size()]);
	}

	/**
	 * Calls the file handler for every file (recursively) in this directory
	 * 
	 * @param dir
	 *                   directory to search
	 * @param handler
	 *                   handler to be called for every file
	 */
	public static void doForAllFiles(File dir, FileHandler handler) {
		List dirQueue = new ArrayList();
		dirQueue.add(dir);
		while (!dirQueue.isEmpty()) {
			dir = (File) dirQueue.get(0);
			dirQueue.remove(0);
			String[] f = dir.list();
			if (f != null)
				for (int i = 0; i < f.length; i++) {
					File file = new File(dir, f[i]);
					if (file.isDirectory())
						dirQueue.add(file);
					else
						handler.handle(file);
				}
		}
	}

}