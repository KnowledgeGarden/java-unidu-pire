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

 
// $Id: OAIException.java,v 1.5 2005/02/21 17:29:20 huesselbeck Exp $

/*
 * $Log: OAIException.java,v $
 * Revision 1.5  2005/02/21 17:29:20  huesselbeck
 * fixed copyright disclaimer
 *
 * Revision 1.4  2005/02/21 16:23:33  huesselbeck
 * added copyright disclaimer
 *
 * Revision 1.3  2005/02/21 15:10:38  huesselbeck
 * removes status line(s)
 *
 * Revision 1.2  2005/02/21 13:11:47  huesselbeck
 * added "$Id: OAIException.java,v 1.5 2005/02/21 17:29:20 huesselbeck Exp $"
 *
 * Revision 1.1  2004/07/12 12:13:19  fischer
 * Open Archives client classes
 *
 * Missing:
 * - treatment of sets in GetRecord and ListRecords
 *
 * Revision 1.1  2004/06/02 16:03:32  fischer
 * a quick shot at OAI harvesting
 *
 */
package de.unidu.is.oai;

import java.io.IOException;

/**
 * @author fischer
 * @version $Revision: 1.5 $
 *
 */
public class OAIException extends IOException {

	/**
	 * 
	 */
	public OAIException() {
		super();
	}

	/**
	 * @param s
	 */
	public OAIException(String s) {
		super(s);
	}

}
