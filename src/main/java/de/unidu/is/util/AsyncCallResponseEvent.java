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


// $Id: AsyncCallResponseEvent.java,v 1.6 2005/03/14 17:33:13 nottelma Exp $

package de.unidu.is.util;


import java.util.EventObject;


/**
 * @author Alexej
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:13 $
 */
public class AsyncCallResponseEvent extends EventObject {
    private Object response = null;

    public AsyncCallResponseEvent(Object source) {
        super(source);
    }

    /**
     * @param source   the Name of the Class of the calling Object
     * @param response
     */
    public AsyncCallResponseEvent(Object source, Object response) {
        super(source);
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }
}
