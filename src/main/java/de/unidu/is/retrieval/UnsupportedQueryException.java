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


// $Id: UnsupportedQueryException.java,v 1.4 2005/02/21 17:29:23 huesselbeck Exp $
package de.unidu.is.retrieval;

/**
 * Signals that the query type is not supported.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:23 $
 * @since 2004-03-17
 */
public class UnsupportedQueryException extends Exception {

    /**
     * Creates a new instance.
     */
    public UnsupportedQueryException() {
        super();
    }

    /**
     * Creates a new instance.
     *
     * @param message further message
     */
    public UnsupportedQueryException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     *
     * @param message further message
     * @param cause   the cause for this exception
     */
    public UnsupportedQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     *
     * @param cause the cause for this exception
     */
    public UnsupportedQueryException(Throwable cause) {
        super(cause);
    }

}
