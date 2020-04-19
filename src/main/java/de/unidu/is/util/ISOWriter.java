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


// $Id: ISOWriter.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * A writer using using the ISO-8859-1 encoding.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 * @since 2004-01-30
 */
public class ISOWriter extends OutputStreamWriter {

    /**
     * Creates a new writer using the ISO-8859-1 encoding.
     *
     * @param out output stream
     * @throws java.io.UnsupportedEncodingException
     */
    public ISOWriter(OutputStream out) throws UnsupportedEncodingException {
        super(out, ISOReader.ISO88591);
    }

}
