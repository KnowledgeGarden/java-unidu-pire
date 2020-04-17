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

 
// $Id: MomentsIR.java,v 1.4 2005/02/21 17:29:22 huesselbeck Exp $
package de.unidu.is.retrieval;


/**
 * An interface for abstracting from different XML-based IR engines (e.g. the 
 * XML PIRE extension, HyREX). This interface supports both creating an index
 * as well as querying and computing the statistical moments of the RSVs.
 * 
 * @author Henrik Nottelmann
 * @since 2005-01-03
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:22 $
 */
public interface MomentsIR extends IR {

	/**
	 * Computes the moments of the indexing weights.
	 * 
	 */
	public abstract void computeMoments() throws IndexException;

}
