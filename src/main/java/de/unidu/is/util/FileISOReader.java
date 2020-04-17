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

 
// $Id: FileISOReader.java,v 1.6 2005/03/14 17:33:13 nottelma Exp $
package de.unidu.is.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * A file reader using using the ISO-8859-1 encoding.
 * 
 * @author Henrik Nottelmann
 * @since 2004-01-30
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:13 $
 */
public class FileISOReader extends ISOReader {

	/**
	 * Creates a new reader for the given file using the ISO-8859-1 encoding.
	 * 
	 * @param filename file name
	 * @throws java.io.UnsupportedEncodingException
	 * @throws java.io.FileNotFoundException
	 */
	public FileISOReader(String filename)
		throws UnsupportedEncodingException, FileNotFoundException {
		this(new File(filename));
	}

	/**
	 * Creates a new reader for the given file using the ISO-8859-1 encoding.
	 * 
	 * @param file file
	 * @throws java.io.UnsupportedEncodingException
	 * @throws java.io.FileNotFoundException
	 */
	public FileISOReader(File file)
		throws UnsupportedEncodingException, FileNotFoundException {
		super(new FileInputStream(file));
	}

}
