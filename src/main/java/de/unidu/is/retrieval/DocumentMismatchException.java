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

 
// $Id: DocumentMismatchException.java,v 1.4 2005/02/21 17:29:22 huesselbeck Exp $
package de.unidu.is.retrieval;

/**
 * Signals that the document is not a valid document, or does not match 
 * its schema.
 * 
 * @author Henrik Nottelmann
 * @since 2004-03-17
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:22 $
 */
public class DocumentMismatchException extends Exception {

	/**
	 * Creates a new instance.
	 *  
	 */
	public DocumentMismatchException() {
		super();
	}

	/**
	 * Creates a new instance.
	 *  
	 * @param message further message
	 */
	public DocumentMismatchException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance.
	 *  
	 * @param message further message
	 * @param cause the cause for this exception
	 */
	public DocumentMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance.
	 *  
	 * @param cause the cause for this exception
	 */
	public DocumentMismatchException(Throwable cause) {
		super(cause);
	}

}
