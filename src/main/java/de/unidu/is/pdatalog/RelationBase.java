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

//$Id: RelationBase.java,v 1.28 2005/03/18 22:01:39 nottelma Exp $
package de.unidu.is.pdatalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import de.unidu.is.expressions.Arg2Expression;
import de.unidu.is.expressions.DifferenceExpression;
import de.unidu.is.expressions.EqualsExpression;
import de.unidu.is.expressions.Expression;
import de.unidu.is.expressions.FractionExpression;
import de.unidu.is.expressions.FunctionExpression;
import de.unidu.is.expressions.PlainExpression;
import de.unidu.is.expressions.ProductExpression;
import de.unidu.is.expressions.ProductNExpression;
import de.unidu.is.expressions.Str2NumFunctionExpression;
import de.unidu.is.expressions.StringExpression;
import de.unidu.is.expressions.SumExpression;
import de.unidu.is.expressions.VariableExpression;
import de.unidu.is.pdatalog.ds.Constant;
import de.unidu.is.pdatalog.ds.DBColExpression;
import de.unidu.is.pdatalog.ds.DBProbExpression;
import de.unidu.is.pdatalog.ds.Fact;
import de.unidu.is.pdatalog.ds.Literal;
import de.unidu.is.pdatalog.ds.LiteralExpression;
import de.unidu.is.pdatalog.ds.ProbExpression;
import de.unidu.is.pdatalog.ds.Rule;
import de.unidu.is.pdatalog.ds.SubSet;
import de.unidu.is.pdatalog.parser.Parser;
import de.unidu.is.sql.SQL;
import de.unidu.is.util.DB;
import de.unidu.is.util.FilePropertyMap;
import de.unidu.is.util.Log;
import de.unidu.is.util.PropertyMap;
import de.unidu.is.util.StringUtilities;
import de.unidu.is.util.SystemUtilities;

/**
 * A collection of pDatalog++ relations together with methods for inserting
 * facts and applying rules, using a RDBMS system.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-07
 * @version $Revision: 1.28 $, $Date: 2005/03/18 22:01:39 $
 */
public class RelationBase {

	/**
	 * PDatalog++ configuration.
	 */
	protected PropertyMap config;

	/**
	 * Logger for rules.
	 */
	protected Logger rulesLogger = Log.getLogger("unidu.pdatalog.rules");

	/**
	 * Logger for facts.
	 */
	protected Logger factsLogger = Log.getLogger("unidu.pdatalog.facts");

	/**
	 * A map with all registered relations. The keys are the relation names; the
	 * values are the relation objects.
	 */
	private Map relations;

	/**
	 * The object which is responsible for formatting SQL commands. This allows
	 * for adapting the SQL command to the underlying database.
	 */
	private PDatalogSQLFormatter formatter;

	/**
	 * Random number for generating prefixes.
	 */
	private static Random random = new Random();

	/**
	 * Counter for generating prefixes.
	 */
	private static int ids = 0;

	/**
	 * Creates a new, empty relation base.
	 * 
	 * @param db
	 *                   DB parameters
	 */
	public RelationBase(DB db) {
		relations = new HashMap();
		formatter = new PDatalogSQLFormatter(db);
		config = new FilePropertyMap(SystemUtilities.getConfURL("pdatalog"),
				true);
		Class[] constructorTypes = new Class[]{getClass()};
		Object[] constructorObjects = new Object[]{this};
		for (Iterator iter = config.getAll("pdatalog.edbcomputed").iterator(); iter
				.hasNext();) {
			String relationClassName = (String) iter.next();
			try {
				Class.forName(relationClassName).getConstructor(
						constructorTypes).newInstance(constructorObjects);
			} catch (Exception e) {
				Log.error(e, "Could not load EDB computed relation");
			}
		}
	}

	/**
	 * Returns a new id as unique prefix for temporary table names.
	 * 
	 * @return new id
	 */
	private static String getID() {
		String id = Long.toHexString(System.currentTimeMillis()) + "_"
				+ Integer.toHexString(random.nextInt()) + "_";
		synchronized (RelationBase.class) {
			id += Integer.toHexString(ids++);
		}
		return id;
	}

	/**
	 * Replaces all variable arguments in the list uses the specified variable
	 * binding, and returns a new list.
	 * 
	 * @param list
	 *                   list containing arguments (
	 *                   <code>de.unidu.is.pdatalog.ds.Argument</code>)
	 * @param variable
	 *                   binding
	 * @return new list with modified arguments
	 */
	private List replaceAll(List list, Map binding) {
		List newList = new ArrayList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Expression expression = (Expression) it.next();
			newList.add(expression.substitute(binding));
		}
		return newList;
	}

	/**
	 * Adds the specified fact to the knowledge base.
	 * 
	 * @param fact
	 *                   fact to be added
	 */
	public void add(Fact fact) {
		if (factsLogger.isDebugEnabled())
			factsLogger.debug("add(" + fact + ")");
		SQL sql = transform(fact);
		formatter.perform(sql, fact.getHead().getPredicateName());
	}

	/**
	 * Creates an SQL statement from the rule.
	 * 
	 * @param rule
	 *                   rule to be transformed
	 * @return SQL statement created from the rule
	 */
	private SQL transform(Rule rule) {
		List select = new ArrayList();
		List prob = new ArrayList();
		List from = new ArrayList();
		Map binding = new HashMap();
		List where = new ArrayList();
		List group = new ArrayList();

		int i = 0;
		for (Iterator it = rule.getBody().iterator(); it.hasNext(); i++) {
			Literal l = (Literal) it.next();
			String name = l.getPredicateName();
			//			if (name.equals("compute")) {
			//				Expression arg = l.getArgument(0);
			//				List ll = (List) binding.get(arg);
			//				if (ll == null) {
			//					ll = new ArrayList();
			//					binding.put(arg, ll);
			//				}
			//				continue;
			//			}
			if (name.equals("add") || name.equals("mult") || name.equals("div")
					|| name.equals("sub") || name.equals("log")) {
				Expression arg = l.getArgument(0);
				List ll = (List) binding.get(arg);
				if (ll == null) {
					ll = new ArrayList();
					binding.put(arg, ll);
				}
				if (name.equals("log"))
					ll.add(new FunctionExpression("log", l.getArgument(1)));
				if (name.equals("add"))
					ll
							.add(new SumExpression(l.getArgument(1), l
									.getArgument(2)));
				if (name.equals("mult"))
					ll.add(new ProductExpression(l.getArgument(1), l
							.getArgument(2)));
				if (name.equals("div"))
					ll.add(new FractionExpression(l.getArgument(1), l
							.getArgument(2)));
				if (name.equals("sub"))
					ll.add(new DifferenceExpression(l.getArgument(1), l
							.getArgument(2)));
				continue;
			}
			if (name.equals("neq")) {
				where.add(new Arg2Expression("<>", l.getArgument(0), l
						.getArgument(1)));
				continue;
			}
			if (name.equals("sum") || name.equals("avg")
					|| name.equals("count") || name.equals("min")
					|| name.equals("max")) {
				Expression targetVar = l.getArgument(0);
				// aggregation literal and expression (the #)
				Literal aggrLiteral = ((LiteralExpression) l.getArgument(l
						.getArity() - 1)).getLiteral();
				VariableExpression hash = new VariableExpression("#");
				Expression aggrExpression = null;
				for (int j = 0; j < aggrLiteral.getArity(); j++) {
					Expression arg = aggrLiteral.getArgument(j);
					if (arg.equals(hash))
						aggrExpression = arg;
				}
				if (aggrExpression != null) {
					// group by these variables
					for (int j = 1; j < l.getArity() - 1; j++) {
						Expression arg = l.getArgument(j);
						group.add(arg);
					}
				} else { // old mode
					aggrExpression = l.getArgument(1);
					// group by these variables
					VariableExpression dummy2 = new VariableExpression("~");
					VariableExpression dummy = new VariableExpression("_");
					for (int j = 0; j < aggrLiteral.getArity(); j++) {
						Expression arg = aggrLiteral.getArgument(j);
						if (!arg.equals(dummy) && !arg.equals(dummy2)
								&& !arg.equals(aggrExpression))
							group.add(arg);
					}
				}
				String aggrRelationName = aggrLiteral.getPredicateName();
				String n = "rel" + i;
				from.add(aggrRelationName + " " + n);
				for (int j = 0; j < aggrLiteral.getArity(); j++) {
					Expression arg = aggrLiteral.getArgument(j);
					List ll = (List) binding.get(arg);
					if (ll == null) {
						ll = new ArrayList();
						binding.put(arg, ll);
					}
					ll.add(new DBColExpression(n, j));
				}
				List ll = (List) binding.get(targetVar);
				if (ll == null) {
					ll = new ArrayList();
					binding.put(targetVar, ll);
				}
				if (!name.equals("count"))
					aggrExpression = new Str2NumFunctionExpression(
							aggrExpression);
				ll.add(new FunctionExpression(name, aggrExpression));
				continue;
			}
			Relation rel = get(name);
			if (rel instanceof EDBComputedRelation) {
				EDBComputedRelation relation = (EDBComputedRelation) rel;
				relation.addProb(l, prob);
				relation.addWhere(l, where);
			} else {
				String n = "rel" + i;
				from.add(name + " " + n);
				for (int j = 0; j < l.getArity(); j++) {
					Expression arg = l.getArgument(j);
					List ll = (List) binding.get(arg);
					if (ll == null) {
						ll = new ArrayList();
						binding.put(arg, ll);
					}
					ll.add(new DBColExpression(n, j));
				}
				prob.add(new DBProbExpression(n));
			}
		}

		// create select statement from head
		Literal head = rule.getHead();
		for (int j = 0; j < head.getArity(); j++) {
			Expression arg = head.getArgument(j);
			select.add(arg);
		}

		// replace variables
		select = replaceAll(select, binding);
		prob = replaceAll(prob, binding);
		where = replaceAll(where, binding);
		group = replaceAll(group, binding);

		// create where statements from bindings
		for (Iterator it = binding.keySet().iterator(); it.hasNext();) {
			Expression arg = (Expression) it.next();
			List ll = (List) binding.get(arg);
			Expression last = null;
			for (Iterator it2 = ll.iterator(); it2.hasNext();) {
				Expression o = (Expression) it2.next();
				if (arg instanceof VariableExpression) {
					if (last != null)
						where.add(new EqualsExpression(last, o));
					last = o;
				} else
					where.add(new EqualsExpression(o, arg));
			}
		}

		// handle probability
		if (prob.isEmpty())
			prob.add(new PlainExpression("1"));
		Expression probArg = new ProductNExpression(prob);
		Expression probOperator = rule.getMapping();
		if (probOperator != null) { // use mapping function
			if (probOperator instanceof FunctionExpression) {
				FunctionExpression op = (FunctionExpression) probOperator;
				if (op.getName().equals("groupsum")) {
					group.addAll(select); // group by all variables
					// in head
					Expression exp = (Expression) op.getArgument();
					probOperator = new FunctionExpression("sum", exp);
				}
			}
			Map args = new HashMap();
			args.putAll(binding);
			args.put(new ProbExpression(), probArg);
			int ii = 1;
			for (Iterator it = prob.iterator(); it.hasNext(); ii++) {
				Expression exp = (Expression) it.next();
				args.put(new VariableExpression("PROB" + ii), Collections
						.singletonList(exp));
			}
			probArg = probOperator.substitute(args);
		}
		select.add(probArg);

		// create abstract SQL statement
		SQL sql = new SQL();
		sql.setInsertTable(rule.getPredicateName());
		sql.setSelect(select);
		sql.setFrom(from);
		sql.setWhere(where);
		sql.setGroup(group);
		return sql;
	}

	/**
	 * Computes the result of the specified rules (all corresponding to the
	 * specified derived relation).
	 * 
	 * @param relation
	 *                   target relation
	 * @param rules
	 *                   collection of rules for target relation
	 */
	public void compute(IDBRelation relation, Collection rules) {
		if (rulesLogger.isDebugEnabled())
			rulesLogger.debug("compute(" + relation + "," + rules + ")");
		String predicate = relation.getName();
		String target = computeTarget(relation, rules);
		combine(relation, target, predicate, true);
		formatter.remove(target);
	}

	/**
	 * Compute the relation by evaluating the single rule.
	 * 
	 * @param relation
	 *                   IDB relation
	 * @param rule
	 *                   rule to be evaluated
	 */
	public void compute(IDBRelation relation, Rule rule) {
		compute(relation, rule, true);
	}

	/**
	 * Compute the relation by evaluating the single rule.
	 * 
	 * @param relation
	 *                   IDB relation
	 * @param rule
	 *                   rule to be evaluated
	 * @param addIndex
	 *                   if true, an DB index is added
	 */
	public void compute(IDBRelation relation, Rule rule, boolean addIndex) {
		if (rulesLogger.isDebugEnabled())
			rulesLogger.debug("compute(" + relation + "," + rule + ")");
		String predicate = relation.getName();
		SQL sql = transform(rule);
		formatter.perform(sql, null);
		if (addIndex)
			addIndex(relation);
	}

	/**
	 * Computes the result of the specified rules (all corresponding to the
	 * specified derived relation). The results from the single rules are
	 * considered to be disjoint.
	 * 
	 * @param relation
	 *                   target relation
	 * @param rules
	 *                   collection of rules for target relation
	 */
	public void computeDisjoint(IDBRelation relation, Collection rules) {
		if (rulesLogger.isDebugEnabled())
			rulesLogger
					.debug("computeDisjoint(" + relation + "," + rules + ")");
		String predicate = relation.getName();
		String target = computeTarget(relation, rules);
		combineDisjoint(relation, target, predicate);
		formatter.remove(target);
	}

	/**
	 * Computes the temporary target relation. This relation may contain
	 * multiple occurrences of the same fact, which have to be combined
	 * afterwards.
	 * 
	 * @param relation
	 *                   target relation
	 * @param rules
	 *                   collection of rules for target relation
	 * @return name of the temporary target relation
	 */
	private String computeTarget(IDBRelation relation, Collection rules) {
		return computeTarget(relation, rules, false);
	}

	/**
	 * Computes the temporary target relation. This relation may contain
	 * multiple occurrences of the same fact, which have to be combined
	 * afterwards.
	 * 
	 * @param relation
	 *                   target relation
	 * @param rules
	 *                   collection of rules for target relation
	 * @return name of the temporary target relation
	 */
	private String computeTarget(IDBRelation relation, Collection rules,
			boolean alwaysOptimize) {
		String predicate = relation.getName();
		int arity = relation.getArity();
		String tmpName = "raw_tmp_" + predicate;
		String target = "tmp_" + predicate;
		boolean tmpCreated = false;
		formatter.create(arity, target); // also removes!
		for (Iterator it = rules.iterator(); it.hasNext();) {
			Rule rule = (Rule) it.next();
			Expression operator = rule.getMapping();
			SQL sql = transform(rule);
			if (alwaysOptimize || rule.isOptimizable()) {
				formatter.perform(sql, target);
			} else {
				if (tmpCreated)
					formatter.clear(tmpName);
				else {
					formatter.create(arity, tmpName); // also
					// removes!
					tmpCreated = true;
				}
				formatter.perform(sql, tmpName);
				combine(relation, tmpName, target, false);
			}
		}
		if (tmpCreated)
			formatter.remove(tmpName);
		return target;
	}

	/**
	 * Combines the facts in the given temporary source relation, and adds the
	 * new facts to the specified temporary target relation.
	 * 
	 * @param relation
	 *                   target relation
	 * @param source
	 *                   temporary source relation
	 * @param target
	 *                   temporary target relation
	 * @param createIndex
	 *                   if true, a DB index is created
	 *  
	 */
	private void combine(IDBRelation relation, String source, String target,
			boolean createIndex) {
		// recombine results
		SQL sql1 = new SQL();
		sql1.setDistinct(true);
		List l11 = new ArrayList();
		for (int i = 0; i < relation.getArity(); i++)
			l11.add(new DBColExpression(source, i));
		sql1.setSelect(l11);
		List l12 = new ArrayList();
		l12.add(source);
		sql1.setFrom(l12);
		try {
			boolean acted = false;
			ResultSet rs = formatter.getDB().executeQuery(
					formatter.getSelect(sql1));
			while (rs.next()) {
				acted = true;
				// get current arguments
				Expression[] args = new Expression[relation.getArity()];
				for (int i = 0; i < relation.getArity(); i++)
					args[i] = new StringExpression(rs.getString(i + 1));
				// get all occurrences
				SQL sql2 = new SQL();
				List l20 = new ArrayList();
				l20.add(new DBProbExpression(source));
				sql2.setSelect(l20);
				sql2.setFrom(l12);
				List l21 = new ArrayList();
				for (int i = 0; i < relation.getArity(); i++)
					l21.add(new EqualsExpression(
							new DBColExpression(source, i), args[i]));
				sql2.setWhere(l21);
				ResultSet rs2 = formatter.getDB().executeQuery(
						formatter.getSelect(sql2));
				List l = new ArrayList();
				while (rs2.next())
					l.add(new Double(rs2.getDouble(1)));
				formatter.getDB().close(rs2);
				// sieve formulae
				double prob = 0;
				for (SubSet subset = new SubSet(l.size(), 1); subset.getValue() > 0; subset
						.next()) {
					double p = 1;
					int count = 0;
					for (int i = 0; i < l.size(); i++)
						if (subset.contains(i)) {
							p *= ((Double) l.get(i)).doubleValue();
							count++;
						}
					prob += (count % 2 == 1 ? 1 : -1) * p;
				}
				// add new fact
				Fact fact = new Fact(prob, new Literal(relation.getName(),
						args, true));
				SQL sql = transform(fact);
				formatter.perform(sql, target);
			}
			formatter.getDB().close(rs);
			if (acted && createIndex)
				for (int i = 0; i < relation.getArity(); i++) {
					String arg = "arg" + i;
					formatter.addIndex(target, arg, new String[]{arg},
							new boolean[]{true});
				}
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		}
	}

	/**
	 * Combines the facts in the given temporary source relation (by assuming
	 * that they are derived with disjoint events), and adds the new facts to
	 * the specified temporary target relation.
	 * 
	 * @param relation
	 *                   target relation
	 * @param source
	 *                   temporary source relation
	 * @param target
	 *                   temporary target relation
	 */
	private void combineDisjoint(IDBRelation relation, String source,
			String target) {
		// recombine results
		SQL sql = new SQL();
		sql.setInsertTable(target);
		List l1 = new ArrayList();
		List l11 = new ArrayList();
		for (int i = 0; i < relation.getArity(); i++)
			l1.add(new DBColExpression(source, i));
		l11.addAll(l1);
		l1.add(new FunctionExpression("sum", new Str2NumFunctionExpression(
				new DBProbExpression(source))));
		sql.setSelect(l1);
		List l2 = new ArrayList();
		l2.add(source);
		sql.setFrom(l2);
		sql.setGroup(l11);
		formatter.perform(sql, null);
		for (int i = 0; i < relation.getArity(); i++) {
			String arg = "arg" + i;
			formatter.addIndex(relation.getName(), arg, new String[]{arg},
					new boolean[]{true});
		}
	}

	/**
	 * Dumps the content of the relation.
	 * 
	 * @param relation
	 *                   target relation
	 */
	public void dump(Relation relation) {
		formatter.dump(relation.getArity(), relation.getName());
	}

	/*
	 * Methods delegatin to the relation map.
	 */

	/**
	 * Removes all relations.
	 *  
	 */
	public void clear() {
		relations.clear();
	}

	/**
	 * Tests whether this relation base contains the relation defined by the
	 * specified name.
	 * 
	 * @param name
	 *                   relation name
	 * @return true iff the relation base contains the specified relation
	 */
	public boolean containsRelation(String name) {
		return relations.containsKey(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return relations.equals(obj);
	}

	/**
	 * Returns the relation defined by the specified name.
	 * 
	 * @param name
	 *                   relation name
	 * @return relation defined by the specified name
	 */
	public Relation get(String name) {
		return (Relation) relations.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return relations.hashCode();
	}

	/**
	 * Tests whether this relation base is empty, which means it does not
	 * contain any relation.
	 * 
	 * @return true iff this relation base is empty
	 */
	public boolean isEmpty() {
		return relations.isEmpty();
	}

	/**
	 * Returns an iterator over all relation names.
	 * 
	 * @return iterator over all relation names
	 */
	public Iterator names() {
		return relations.keySet().iterator();
	}

	/**
	 * Adds (and initialises) the specified relation to this relation base.
	 * 
	 * @param relation
	 *                   relation to be stored
	 */
	public void add(Relation relation) {
		if (!(relation instanceof EDBComputedRelation))
			formatter.create(relation.getArity(), relation.getName());
		relations.put(relation.getName(), relation);
	}

	/**
	 * Adds (and initialises, if specified by the Boolean parameter) the
	 * specified relation to this relation base.
	 * 
	 * @param relation
	 *                   relation to be stored
	 * @param create
	 *                   if true, the relation is physically created
	 */
	public void add(Relation relation, boolean create) {
		if (create && !(relation instanceof EDBComputedRelation))
			formatter.create(relation.getArity(), relation.getName());
		relations.put(relation.getName(), relation);
	}

	/**
	 * Removes the relation defined by the specified name.
	 * 
	 * @param name
	 *                   relation name
	 */
	public void remove(String name) {
		if (!(relations.get(name) instanceof EDBComputedRelation))
			formatter.remove(name);
		relations.remove(name);
	}

	/**
	 * Clears the relation, i.e. it removes all facts.
	 * 
	 * @param name
	 *                   relation name
	 */
	public void clear(String name) {
		if (!(relations.get(name) instanceof EDBComputedRelation))
			formatter.clear(name);
	}

	/**
	 * Returns the size of this relation base, which means the number of
	 * relations in it.
	 * 
	 * @return size of this relation base
	 */
	public int size() {
		return relations.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return relations.toString();
	}

	/**
	 * Returns a collection containing all relations.
	 * 
	 * @return collection containing all relations
	 */
	public Collection relations() {
		return relations.values();
	}

	/**
	 * Performs a SQL insert statement.
	 * 
	 * @param sql
	 *                   SQL statement
	 */
	public void perform(SQL sql) {
		formatter.perform(sql, null);
	}

	/**
	 * Performs a SQL insert statement.
	 * 
	 * @param sql
	 *                   SQL statement
	 */
	public ResultSet performQuery(SQL sql) throws SQLException {
		return formatter.performQuery(sql);
	}

	/**
	 * Closes the specified <code>ResultSet</code>.
	 * 
	 * @param rs
	 *                   result set
	 */
	public void close(ResultSet rs) {
		formatter.close(rs);
	}

	/**
	 * Adds indexes to the specified relation.
	 * 
	 * @param relation
	 *                   reation
	 */
	public void addIndex(Relation relation) {
		for (int i = 0; i < relation.getArity(); i++) {
			String arg = "arg" + i;
			formatter.addIndex(relation.getName(), arg, new String[]{arg},
					new boolean[]{true});
		}
	}

	/**
	 * Retutns true iff the relation physically exists.
	 * 
	 * @param relation
	 *                   relation name
	 * @return true iff the relation physically exists
	 */
	public boolean existsRelation(String relation) {
		return formatter.getDB().existsTable(relation);
	}

	/**
	 * Queries the relation base.
	 * 
	 * @param query
	 *                   pDatalog++ query
	 * @return list of Fact instances
	 */
	public List query(String query) {
		String relation = "tmp" + getID();
		// get variables in rule body
		Rule r = Parser.parseRule(query.replaceAll("\\?-", relation
				+ "(foo) :- "));
		Set variables = r.getAllArguments();
		variables.removeAll(r.getConstants());
		boolean containsNilVariable = variables.remove(new VariableExpression(
				"~"));
		containsNilVariable |= variables.remove(new VariableExpression("_"));
		int arity = variables.size();

		// create temporary relation and rule
		query = query.replaceAll("\\?-", relation + "("
				+ StringUtilities.implode(variables, ",") + ") :- ");
		r = Parser.parseRule(query);
		if (r.literalCount() == 1 && r.literalAt(0).getArity() == arity)
			return queryRelation(r.literalAt(0).getPredicateName(), arity);
		IDBRelation rel = new IDBRelation(this, relation, arity);
		// TODO: optimize?
		if (!containsNilVariable && r.literalCount() == 1) {
			r.setOptimizable(true);
			compute(rel, r);
		} else
			compute(rel, Collections.singleton(r));

		// query temporary relation
		List results = queryRelation(relation, arity);

		// remove temporary relation
		remove(relation);
		return results;
	}

	/**
	 * Returns all tuples in the specified relation.
	 * 
	 * @param relation
	 *                   relation
	 * @return all tuples in the specified relation
	 */
	public List queryRelation(Relation relation) {
		return queryRelation(relation.getName(), relation.getArity());
	}

	/**
	 * Returns all tuples in the specified relation.
	 * 
	 * @param relation
	 *                   relation name
	 * @param arity
	 *                   relation arity
	 * @return all tuples in the specified relation
	 */
	public List queryRelation(String relation, int arity) {
		List results = new ArrayList();
		SQL sql = new SQL();
		List select = new ArrayList();
		for (int i = 0; i < arity; i++)
			select.add(new DBColExpression(relation, i));
		select.add(new DBProbExpression(relation));
		sql.setSelect(select);
		sql.setFrom(Collections.singletonList(relation));
		ResultSet rs = null;
		try {
			rs = performQuery(sql);
			while (rs.next()) {
				Expression[] exp = new Expression[arity];
				for (int i = 0; i < arity; i++)
					exp[i] = new Constant(rs.getString(i + 1));
				double prob = rs.getDouble(arity + 1);
				results.add(new Fact(prob, new Literal("", exp, true)));
			}
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			if (rs != null)
				close(rs);
		}
		return results;
	}

	/**
	 * Compute the specified rule recursively, applying the naive evaluation.
	 * <p>
	 * 
	 * Future implementation will use a more efficient method.
	 * 
	 * @param rel
	 *                   relation name
	 * @param rule
	 *                   rule
	 */
	public void computeRecursively(IDBRelation rel, Rule rule) {
		// TODO: more efficient
		String id = getID();
		String relationa = "tmpa_" + id;
		String relationb = "tmpb_" + id;
		IDBRelation rela = new IDBRelation(this, relationa, rel.getArity(),
				true);
		IDBRelation relb = new IDBRelation(this, relationb, rel.getArity(),
				true);
		String p = rule.getHead().getPredicateName();
		Rule rule2 = new Rule(rule);
		rule2.getHead().setPredicateName(relationb);
		for (int i = 0; i < rule2.literalCount(); i++) {
			Literal l = rule2.literalAt(i);
			if (l.getPredicateName().equals(p))
				l.setPredicateName(relationa);
		}
		formatter.getDB().executeUpdate(
				"insert into " + relationa + " select distinct * from "
						+ rel.getName());
		int count = -1;
		int old = -1;
		do {
			old = count;
			formatter.getDB().executeUpdate("delete from " + relationb);
			compute(relb, rule2, false);
			formatter.getDB().executeUpdate(
					"insert into " + relationa + " select distinct * from "
							+ relationb);
			formatter.getDB().executeUpdate("delete from " + relationb);
			count = getTupleCount(rela);
		} while (old < count);
		addIndex(rela);

		clear(p);
		combine(rel, relationa, rel.getName(), true);
		remove(relationa);
		remove(relationb);
	}

	/**
	 * Returns the number of tuples in the relation.
	 * 
	 * @param rel
	 *                   relation
	 * @return number of tuples in the relation
	 */
	public int getTupleCount(Relation rel) {
		int count = -1;
		ResultSet rs = null;
		try {
			String s = "prob";
			for (int i = 0; i < rel.getArity(); i++)
				s = "concat(" + s + ",concat('@@@',arg" + i + "))";
			rs = formatter.getDB().executeQuery(
					"select count(distinct " + s + ") from " + rel.getName());
			if (rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) {
			de.unidu.is.util.Log.error(e);
		} finally {
			if (rs != null)
				close(rs);
		}
		return count;
	}

}