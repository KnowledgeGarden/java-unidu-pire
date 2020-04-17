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

 
// $Id: Literal.java,v 1.6 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unidu.is.expressions.*;

/**
 * The representation of an datalog literal. An literal is a string
 * <code>[!]p(t<sub>1</sub>,...,t<sub>n</sub>)</code> where is
 * <code>p</code> is a predicate name, the <code>t<sub>i</sub></code>s
 * are the arguments (variable names or constants, where constants are
 * conclosed on <code>''</code>) and <code>n</code> is the arity of
 * the predicate. It is also called the arity of the literal. A
 * literal can be positive (missing <code>!</code>) or negative (with
 * starting <code>!</code>). </p>
 *
 * This class is used by <code>Rule</code> for storing the head literal.
 *
 * @author Henrik Nottelmann
 * @since 2002-12-02 (predecessor 2000-07-27)
 * @version $Revision: 1.6 $, $Date: 2005/03/14 17:33:14 $
 */
public class Literal {

	/**
	 * The name of the predicate.
	 */
	protected String predicateName;

	/**
	 * The arguments.
	 */
	protected Expression[] arguments;

	/**
	 * Being true iff this Literal is positive.
	 */
	protected boolean positive;

	/**
	 * Creates a new literal.
	 *
	 * @param predicateName predicate name
	 * @param arguments arguments (variable names or constants)
	 * @param positive true iff this literal is positive
	 */
	public Literal(
		String predicateName,
		Expression[] arguments,
		boolean positive) {
		this.predicateName = predicateName;
		this.arguments = new Expression[arguments.length];
		for (int i = 0; i < arguments.length; i++)
			this.arguments[i] = arguments[i];
		this.positive = positive;
	}

	/**
	 * Creates a new literal.
	 *
	 * @param predicateName predicate name
	 * @param arguments list of arguments (variable names or constants)
	 * @param positive true iff this literal is positive
	 */
	public Literal(String predicateName, List arguments, boolean positive) {
		this.predicateName = predicateName;
		this.arguments =
			(Expression[]) arguments.toArray(new Expression[arguments.size()]);
		this.positive = positive;
	}

	/**
	 * Creates a new literal.
	 *
	 * @param literal to be copied
	 */
	public Literal(Literal literal) {
		this.predicateName = literal.predicateName;
		this.arguments = new Expression[literal.arguments.length];
		for (int i = 0; i < literal.arguments.length; i++)
			this.arguments[i] = literal.arguments[i];
		this.positive = literal.positive;
	}

	/**
	 * Returns the predicate name.
	 *
	 * @return predicate name
	 */
	public String getPredicateName() {
		return predicateName;
	}

	/**
	 * Sets the predicate name.
	 *
	 * @param predicateName predicate name
	 */
	public void setPredicateName(String predicateName) {
		this.predicateName = predicateName;
	}

	/**
	 * Returns the arity of this literal. 
	 *
	 * @return arity
	 */
	public int getArity() {
		return arguments.length;
	}

	/**
	 * Returns the arguments as a comma separated string.
	 *
	 * @return arguments
	 */
	public String getArguments() {
		String ret = "";
		for (int i = 0; i < arguments.length; i++)
			ret += arguments[i] + (i < arguments.length - 1 ? "," : "");
		return ret;
	}

	/**
	 * Returns the argument at the specified index.
	 *
	 * @param index index
	 * @return argument at the specified index
	 */
	public Expression getArgument(int index) {
		return arguments[index];
	}

	/**
	 * Returns whether the argument at the specified index is a
	 * variable name or a constant.
	 *
	 * @param index index
	 * @return flag indicating whether the argument at the specified
	 *         index is a variable name or a constant
	 */
	public boolean isVariable(int index) {
		return arguments[index] instanceof VariableExpression;
	}

	/**
	 * Sets the argument at the specified index.
	 *
	 * @param index specified index
	 * @param value new argument at the specified index
	 */
	public void setArgument(int index, Expression value) {
		arguments[index] = value;
	}

	/**
	 * Puts all variables (no constants) into the specified set.
	 *
	 * @param set destination set for all variables
	 */
	public void putVariables(Set set) {
		for (int i = 0; i < arguments.length; i++)
			if (isVariable(i))
				set.add(arguments[i]);
	}

	/**
	 * Puts all constants (no variables) into the specified set.
	 *
	 * @param set destination set for all constants
	 */
	public void putConstants(Set set) {
		for (int i = 0; i < arguments.length; i++)
			if (!isVariable(i))
				set.add(arguments[i]);
	}

	/**
	 * Puts all arguments (variables and constants) into the specified
	 * set.
	 *
	 * @param set destination set for all arguments
	 */
	public void putAllArguments(Set set) {
		for (int i = 0; i < arguments.length; i++)
			set.add(arguments[i]);
	}

	/**
	 * Returns a set containing all variables (no constants).
	 *
	 * @return set containing all variables (no constants)
	 */
	public Set getVariables() {
		Set set = new HashSet();
		putVariables(set);
		return set;
	}

	/**
	 * Returns a set containing all constants (no variables).
	 *
	 * @return set containing all constants (no variables)
	 */
	public Set getConstants() {
		Set set = new HashSet();
		putConstants(set);
		return set;
	}

	/**
	 * Returns a set containing all arguments (variables and constants).
	 *
	 * @return set containing all arguments
	 */
	public Set getAllArguments() {
		Set set = new HashSet();
		putAllArguments(set);
		return set;
	}

	/**
	 * Returns if this literal is positive.
	 *
	 * @return true if this literal is positve
	 */
	public boolean isPositive() {
		return positive;
	}

	/**
	 * Sets if this literal is positive.
	 *
	 * @param positive flag indicating if this literal is positive or negative
	 */
	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	/**
	 * Makes this literal positive. This is equivalent to
	 * <code>setPositive(true)</code>.
	 * 
	 */
	public void setPositive() {
		this.positive = true;
	}

	/**
	 * Makes this literal negative. This is equivalent to
	 * <code>setPositive(false)</code>.
	 * 
	 */
	public void setNegative() {
		this.positive = false;
	}

	/**
	 * Negates this literal.
	 */
	public void negate() {
		this.positive = !this.positive;
	}

	/**
	 * Creates a positive copy of this literal.
	 *
	 * @return positive copy of this literal
	 */
	public Literal createLiteral() {
		return createLiteral(true);
	}

	/**
	 * Creates a copy of this literal.
	 *
	 * @param positive indicating whether the copy is positive or negative
	 * @return positive copy of this literal
	 */
	public Literal createLiteral(boolean positive) {
		return new Literal(this);
	}

	/**
	 * Returns a datalog representation of this literal.
	 *
	 * @return a datalog representation of this literal
	 */
	public String toString() {
		return (positive ? "" : "!")
			+ predicateName
			+ "("
			+ getArguments()
			+ ")";
	}

	/**
	 * Returns a string representation of this literal, used for
	 * <code>equals(Object)</code> and <code>hashCode()</code>.
	 *
	 * @return a string representation of this literal
	 */
	protected String toHashCodeString() {
		return predicateName + "(" + getArguments() + ")";
	}

	/**
	 * Returns the hash code value for this literal. The hash code is
	 * calculated based on <code>toHashCodeString()</code>.
	 *
	 * @return the hash code value for this literal
	 */
	public int hashCode() {
		return toHashCodeString().hashCode();
	}

	/**
	 * Compares the specified Object with this literal for
	 * equality. The comparasion is performed based on the equality of
	 * <code>toHashCodeString()</code>.
	 *
	 * @param o the Object to be compared for equality with this literal
	 * @return true if the specified Object is equal to this literal
	 */
	public boolean equals(Object o) {
		return ((Literal) o).toHashCodeString().equals(toHashCodeString());
	}

}
