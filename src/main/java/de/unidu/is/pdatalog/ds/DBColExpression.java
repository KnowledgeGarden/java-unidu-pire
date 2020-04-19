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

// $Id: DBColExpression.java,v 1.5 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.pdatalog.ds;

import de.unidu.is.sql.DBColumnExpression;

/**
 * An expression representing an argument column in a relational table.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.5 $, $Date: 2005/02/28 22:27:55 $
 */
public class DBColExpression extends DBColumnExpression {

    /**
     * Creates a new object.
     *
     * @param table table name
     * @param col   column name
     */
    public DBColExpression(String table, int col) {
        super(table, "arg" + col);
    }

}