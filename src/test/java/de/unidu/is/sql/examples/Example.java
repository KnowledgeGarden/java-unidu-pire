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

// $Id: Example.java,v 1.1 2005/03/01 09:34:11 nottelma Exp $
package de.unidu.is.sql.examples;

import de.unidu.is.expressions.Arg2Expression;
import de.unidu.is.expressions.FunctionExpression;
import de.unidu.is.expressions.PlainExpression;
import de.unidu.is.expressions.StringExpression;
import de.unidu.is.sql.DBColumnExpression;
import de.unidu.is.sql.SQL;
import de.unidu.is.sql.SQLFormatter;
import de.unidu.is.sql.SQLFormatterFactory;
import de.unidu.is.util.DB;
import de.unidu.is.util.HSQLDBEmbeddedDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * An example about how to use de.unidu.is.sql.
 * <p>
 * <p>
 * The example runs with both HSQLDB and MySQL.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.1 $, $Date: 2005/03/01 09:34:11 $
 * @since 2003-11-26
 */
public class Example {

    public static void main(String[] args) {
        DB db = new HSQLDBEmbeddedDB();
        SQLFormatter formatter = SQLFormatterFactory.newFormatter(
                db);
        final String TABLE = "de_unidu_is_sql_example_"
                + System.currentTimeMillis();

        formatter.create(TABLE, new String[]{"a", "b"}, new String[]{
                "${type.text}", "${type.double}"});

        SQL sql = new SQL();
        List selects = new ArrayList();
        StringExpression a = new StringExpression("test");
        PlainExpression b = new PlainExpression("42");
        selects.add(a);
        selects.add(b);
        sql.setSelect(selects);
        sql.setInsertTable(TABLE);
        formatter.perform(sql, null);

        a.set("test");
        b.set("12345");
        formatter.perform(sql, null);

        a.set("abc");
        b.set("4711");
        formatter.perform(sql, null);
        System.out.println("\n");

        System.out
                .println("First columns of row where second colum is greater than 42:");
        sql = new SQL();
        selects.clear();
        selects.add(new DBColumnExpression(TABLE, "a"));
        sql.setSelect(selects);
        List froms = new ArrayList();
        froms.add(TABLE);
        sql.setFrom(froms);
        List wheres = new ArrayList();
        wheres.add(new Arg2Expression(">", new DBColumnExpression(TABLE, "b"),
                new PlainExpression("42")));
        sql.setWhere(wheres);
        ResultSet rs = null;
        try {
            rs = formatter.performQuery(sql);
            while (rs.next()) {
                System.out.println("b>42: \t\t" + rs.getString(1));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (rs != null)
                formatter.close(rs);
        }
        System.out.println("\n");

        System.out.println("Sum of second column:");
        sql = new SQL();
        selects.clear();
        selects.add(new FunctionExpression("sum", new DBColumnExpression(TABLE,
                "b")));
        sql.setSelect(selects);
        froms = new ArrayList();
        froms.add(TABLE);
        sql.setFrom(froms);
        List groups = new ArrayList();
        groups.add(new DBColumnExpression(TABLE, "a"));
        sql.setGroup(groups);
        rs = null;
        try {
            rs = formatter.performQuery(sql);
            while (rs.next()) {
                System.out.println("sum: \t\t" + rs.getString(1));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (rs != null)
                formatter.close(rs);
        }
        System.out.println("\n");

        System.out.println("Complete table:");
        sql = new SQL();
        selects.clear();
        selects.add(new DBColumnExpression(TABLE, "a"));
        selects.add(new DBColumnExpression(TABLE, "b"));
        sql.setSelect(selects);
        froms = new ArrayList();
        froms.add(TABLE);
        sql.setFrom(froms);
        rs = null;
        try {
            rs = formatter.performQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getDouble(2));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (rs != null)
                formatter.close(rs);
        }
        System.out.println("\n");

        System.out
                .println("Complete table, sorted by first and then by second colum:");
        sql = new SQL();
        selects.clear();
        selects.add(new DBColumnExpression(TABLE, "a"));
        selects.add(new DBColumnExpression(TABLE, "b"));
        sql.setSelect(selects);
        froms = new ArrayList();
        froms.add(TABLE);
        sql.setFrom(froms);
        List orders = new ArrayList();
        orders.add(new DBColumnExpression(TABLE, "a"));
        orders.add(new DBColumnExpression(TABLE, "b"));
        sql.setOrder(orders);
        rs = null;
        try {
            rs = formatter.performQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getDouble(2));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (rs != null)
                formatter.close(rs);
        }
        System.out.println("\n");

        System.out
                .println("Complete table, sorted descending by second column:");
        sql = new SQL();
        selects.clear();
        selects.add(new DBColumnExpression(TABLE, "a"));
        selects.add(new DBColumnExpression(TABLE, "b"));
        sql.setSelect(selects);
        froms = new ArrayList();
        froms.add(TABLE);
        sql.setFrom(froms);
        orders = new ArrayList();
        orders.add(new DBColumnExpression(TABLE, "b"));
        sql.setOrder(orders, true);
        rs = null;
        try {
            rs = formatter.performQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getDouble(2));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (rs != null)
                formatter.close(rs);
        }
        System.out.println("\n");

        System.out.println("First row:");
        sql = new SQL();
        selects.clear();
        selects.add(new DBColumnExpression(TABLE, "a"));
        selects.add(new DBColumnExpression(TABLE, "b"));
        sql.setSelect(selects);
        froms = new ArrayList();
        froms.add(TABLE);
        sql.setFrom(froms);
        sql.setLimit(1);
        rs = null;
        try {
            rs = formatter.performQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getDouble(2));
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        } finally {
            if (rs != null)
                formatter.close(rs);
        }
        System.out.println("\n");

        formatter.remove(TABLE);
        System.exit(0);
    }

}