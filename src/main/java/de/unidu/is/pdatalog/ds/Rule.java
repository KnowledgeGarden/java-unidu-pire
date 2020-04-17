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

 
// $Id: Rule.java,v 1.7 2005/03/14 17:33:14 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.unidu.is.expressions.*;

/**
 * The representation of a datalog probabilistic rule. A rule has a head
 * (an <code>Literal</code>), a body (one or more <code>Literal</code>s, stored
 * in a <code>List</code>) and a mapping function (which can be a linear
 * one, which then can be defined by a probability).<p>
 *
 * @author Henrik Nottelmann
 * @since 2003-10-27
 * @version $Revision: 1.7 $, $Date: 2005/03/14 17:33:14 $
 */
public class Rule {

	/**
	 * The head of the rule.
	 */
	protected Literal head;

	/**
	 * The body of the rule. The list elements are instances of
	 * Literal.
	 */
	protected List body;

	/**
	 * Mapping function.
	 */
	private Expression mapping;

	/**
	 * Rule can be optimised, i.e. that it only produces each tuple once.
	 */
	private boolean optimizable;

	/**
	 * Creates a new deterministic rule (probability is one).
	 * 
	 * @param head head of the rule
	 * @param body body of the rule
	 */
	public Rule(Literal head, Literal body) {
		this(head,body,null);
	}

	/**
	 * Creates a new probabilistic rule.
	 * 
	 * @param prob the probability of the rule
	 * @param head head of the rule
	 * @param body body of the rule
	 */
	public Rule(double prob, Literal head, Literal body) {
		this(head, body, getMapping(prob));
	}
	/**
	 * Creates a new deterministic rule (probability is one).
	 * 
	 * @param head head of the rule
	 * @param body body of the rule
	 */
	public Rule(Literal head, Literal body,Expression mapping) {
		this.head = head;
		this.body = new ArrayList();
		this.body.add(body);
		this.mapping = mapping;
	}

	/**
	 * Creates a new deterministic rule (probability is one).
	 * 
	 * @param head head of the rule
	 * @param body body of the rule
	 */
	public Rule(Literal head, List body) {
		this(head, body, null);
	}

	/**
	 * Creates a new probabilistic rule.
	 * 
	 * @param prob the probability of the rule
	 * @param head head of the rule
	 * @param body body of the rule
	 */
	public Rule(double prob, Literal head, List body) {
		this(head, body, getMapping(prob));
	}

	/**
	 * Creates a new probabilistic rule.
	 * 
	 * @param head head of the rule
	 * @param body body of the rule
	 * @param mapping mapping function
	 */
	public Rule(Literal head, List body, Expression mapping) {
		this.head = head;
		this.body = body;
		this.mapping = mapping;
	}

	/**
	 * Creates a new rule as a copy of the specified rule.
	 * 
	 * @param rule to be copied
	 */
	public Rule(Rule rule) {
		this.head = rule.head == null ? null : new Literal(rule.head);
		this.body = new ArrayList();
		for (int i = 0; i < rule.body.size(); i++)
			body.add(new Literal((Literal) rule.body.get(i)));
		this.mapping = rule.mapping;
	}


	/**
	 * Returns the head of this rule.
	 * 
	 * @return head of the rule
	 */
	public Literal getHead() {
		return head;
	}

	/**
	 * Sets the head of this rule.
	 * 
	 * @param head head of the rule
	 */
	public void setHead(Literal head) {
		this.head = head;
	}

	/**
	 * Returns the body of this rule.
	 * 
	 * @return body of the rule
	 */
	public List getBody() {
		return body;
	}

	/**
	 * Returns the body literal at the specified index.
	 * 
	 * @param num the index
	 * @return body literal at the specified index or <code>null</code>,
	 * 	       if this literal does not exist
	 */
	public Literal literalAt(int num) {
		return (Literal) body.get(num);
	}

	/**
	 * Sets (replaces) the body literal at the specified index.
	 * 
	 * @param num the index
	 * @param literal body literal at the specified index
	 */
	public void setLiteral(int num, Literal literal) {
		body.set(num, literal);
	}

	/**
	 * Returns the number of body literals of this Rule.
	 * 
	 * @return number of body literal of this Rule
	 */
	public int literalCount() {
		return body.size();
	}

	/**
	 * Tests if this rule contains the specified literal as the head or in the
	 * body without considering if the literal is positive or negative. <p>
	 * 
	 * This method can be used to test if a literal should be added to be
	 * body for spezialization.
	 * 
	 * @return true if this rule contains the specified literal
	 */
	public boolean contains(Literal literal) {
		if (head != null && head.equals(literal))
			return true;
		for (int i = 0; i < literalCount(); i++)
			if (literalAt(i).equals(literal))
				return true;
		return false;
	}

	/**
	 * Tests if this rule contains exactly the specified literal as the head or
	 * in the body considering if the literal is positive or negative. <p>
	 * 
	 * @return true if this rule contains exactly the specified literal
	 */
	public boolean containsExactly(Literal literal) {
		if (head != null && head.toString().equals(literal.toString()))
			return true;
		return containsExactlyInBody(literal);
	}

	/**
	 * Tests if this rule contains exactly the specified literal 
	 * in the body considering if the literal is positive or negative. <p>
	 * 
	 * @return true if this rule contains exactly the specified literal
	 */
	public boolean containsExactlyInBody(Literal literal) {
		String l = literal.toString();
		for (int i = 0; i < literalCount(); i++)
			if (literalAt(i).toString().equals(l))
				return true;
		return false;
	}

	/**
	 * Adds the specified literal to the body of this rule. This method does
	 * not check for double occurrences of literals.
	 * 
	 * @param literal literal to be added to the body
	 */
	public void addLiteral(Literal literal) {
		body.add(literal);
	}

	/**
	 * Removes the specified literal fromo the body of this rule.
	 * 
	 * @param literal literal to be removed to the body
	 */
	public void removeLiteral(Literal literal) {
		body.remove(literal);
	}

	/**
	 * Removes the literal at the specified index from the body of
	 * this rule.
	 * 
	 * @param literal literal to be removed to the body
	 */
	public void removeLiteral(int literal) {
		body.remove(literal);
	}

	/**
	 * Puts all variables (no constants) of the head literal and all
	 * body literals into the specified set.
	 *
	 * @param set destination set for all variables
	 */
	public void putVariables(Set set) {
		if (head != null)
			head.putVariables(set);
		for (int i = 0; i < literalCount(); i++)
			literalAt(i).putVariables(set);
	}

	/**
	 * Puts all constants (no variables) of the head literal and all
	 * body literals into the specified set.
	 *
	 * @param set destination set for all constants
	 */
	public void putConstants(Set set) {
		if (head != null)
			head.putConstants(set);
		for (int i = 0; i < literalCount(); i++)
			literalAt(i).putConstants(set);
	}

	/**
	 * Puts all arguments (variables an constants) of the head literal
	 * and all body literals into the specified set.
	 *
	 * @param set destination set for all arguments
	 */
	public void putAllArguments(Set set) {
		if (head != null)
			head.putAllArguments(set);
		for (int i = 0; i < literalCount(); i++)
			literalAt(i).putAllArguments(set);
	}

	/**
	 * Returns a set containing all variables (no constants) of the
	 * head literal and all body literals.
	 *
	 * @return a set containing all variables (no constants)
	 */
	public Set getVariables() {
		Set set = new HashSet();
		putVariables(set);
		return set;
	}

	/**
	 * Returns a set containing all constants (no variables) of the
	 * head literal and all body literals.
	 *
	 * @return a set containing all constants (no variables)
	 */
	public Set getConstants() {
		Set set = new LinkedHashSet();
		putConstants(set);
		return set;
	}

	/**
	 * Returns a set containing all arguments (variables an constants)
	 * of the head literal and all body literals.
	 *
	 * @return a set containing all arguments
	 */
	public Set getAllArguments() {
		Set set = new LinkedHashSet();
		putAllArguments(set);
		return set;
	}

	/**
	 * Returns an array containing all arguments (variables an constants)
	 * of the head literal and all body literals.
	 *
	 * @return an array containing all arguments
	 */
	public Expression[] getAllArgumentsAsArray() {
		Set set = getAllArguments();
		return (Expression[]) set.toArray(new Expression[set.size()]);
	}

	/**
	 * Returns the heads predicate name.
	 *
	 * @return heads predicate name
	 */
	public String getName() {
		return head == null ? null : head.getPredicateName();
	}

	/**
	 * Returns the heads predicate name.
	 *
	 * @return heads predicate name
	 */
	public String getPredicateName() {
		return getName();
	}

	/**
	 * Returns a datalog representation of this rule.
	 *
	 * @return datalog representation of this rule
	 */
	public String toString() {
		String ret = (head != null ? head.toString() : "");
		if (literalCount() > 0)
			ret += " :- ";
		for (int i = 0; i < literalCount(); i++)
			ret += literalAt(i) + (i < literalCount() - 1 ? " & " : "");
		return ret + " | " + mapping + ".";
	}

	/**
	 * Returns the mapping function.
	 * 
	 * @return mapping function
	 */
	public Expression getMapping() {
		return mapping;
	}

	/**
	 * Sets the mapping function.
	 * 
	 * @param mapping mapping function
	 */
	public void setMapping(Expression mapping) {
		this.mapping = mapping;
	}

	/**
	 * Returns the optimisable flag.
	 * 
	 * @return optimisable flag
	 */
	public boolean isOptimizable() {
		return optimizable;
	}

	/**
	 * Sets the optimisable flag.
	 * 
	 * @param optimizable optimisable flag
	 */
	public void setOptimizable(boolean optimizable) {
		this.optimizable = optimizable;
	}

	/**
	 * Returns a linear mapping function.
	 * 
	 * @param prob probability factor of the mapping function
	 * @return corresponding mapping function
	 */
	public static Expression getMapping(double prob) {
		return new ProductExpression(
			new PlainExpression("" + prob),
			new ProbExpression());
	}

	/**
	 * Returns a linear mapping function.
	 * 
	 * @param prob probability factor of the mapping function
	 * @return corresponding mapping function
	 */
	public static Expression getMapping(String prob) {
		return new ProductExpression(new PlainExpression(prob), new ProbExpression());
	}

}
