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

// $Id: LearnableFunction.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

import java.io.File;

import de.unidu.is.learning.Learner;
import de.unidu.is.learning.LearnerFactory;

/**
 * A function whose parameters can be learned via regression.
 * 
 * @author Henrik Nottelmann
 * @since 2004-06-21
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 */
public abstract class LearnableFunction extends Function {

	/**
	 * Uses the specified file for learning parameters. The file should contain
	 * x and y values, separated by a space. The parameter names are specified
	 * by <code>getParameterNames()</code>, the textual serialisation of this
	 * function by <code>getFunction()</code> (including the function name),
	 * and the result overwrites the current parameters.
	 * 
	 * @param learnfile
	 *                   file used for learning
	 */
	public void learn(File learnfile) {
		Learner parameterLearner = LearnerFactory.newLearner();
		parameters = new DelegatedPropertyMap(parameterLearner.learn(learnfile
				.toString(), " ", getFunction(), getParameterNames()));
	}

	/**
	 * Returns the function in its textual representation.
	 * 
	 * @return textual representation of this function
	 */
	public abstract String getFunction();

	/**
	 * Returns the parameter names which can be learned.
	 * 
	 * @return parameter names
	 */
	public abstract String[] getParameterNames();

}