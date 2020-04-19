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


// $Id: AcceptFilter.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.util;


/**
 * A filter which can accept or reject values.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 * @since 2003-09-23
 */
public interface AcceptFilter {

    /**
     * Returns true if the specified object is accepted.
     *
     * @param object object to be testet
     * @return true if the specified object is accepted
     */
    boolean accept(Object object);

}
