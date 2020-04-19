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


// $Id: NameDT.java,v 1.7 2005/02/21 17:29:26 huesselbeck Exp $
package de.unidu.is.retrieval.pire.dt;

import de.unidu.is.text.*;

/**
 * A class for the IR datatype "name", containing the two deterministic
 * operators "soundex" (soundex similarity) and "plainname" (equality). No
 * indexing weights are used, the mapping function is the identity one.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.7 $, $Date: 2005/02/21 17:29:26 $
 * @since 2003-09-22
 */
public class NameDT extends AbstractDT {

    /**
     * The name of this datatype.
     */
    public static final String NAME = "Name";

    /**
     * Operator name "plainname".
     */
    public static final String PLAINNAME = "plainname";

    /**
     * Operator name "soundex".
     */
    public static final String SOUNDEX = "soundex";

    /**
     * The index filter for the operator "soundex".
     */
    private static final Filter soundexFilter;

    /**
     * The index filter for the operator "plainname".
     */
    private static final Filter plainnameFilter;

    /**
     * The index filter for the operator "soundex".
     */
    private static final Filter soundexIndexFilter;

    /**
     * The query filter for the operator "soundex".
     */
    private static final Filter soundexQueryFilter;

    /**
     * The index filter for the operator "plainname".
     */
    private static final Filter plainnameIndexFilter;

    /**
     * The query filter for the operator "plainname".
     */
    private static final Filter plainnameQueryFilter;

    static {
        // operator "soundex"
        soundexFilter = new SoundexFilter(new LowercaseFilter(
                new WordSplitterFilter(null)));
        soundexQueryFilter = new WordSetConcatenatorFilter(soundexFilter);
        soundexIndexFilter = new CounterFilter(soundexFilter);

        // operator "plainname"
        plainnameFilter = new LowercaseFilter(new WordSplitterFilter(null));
        plainnameQueryFilter = new WordSetConcatenatorFilter(plainnameFilter);
        plainnameIndexFilter = new CounterFilter(plainnameFilter);
    }

    /**
     * Returns a filter for converting a document value into tokens/token
     * frequency tuples.
     *
     * @param operator operator name
     * @return filter
     */
    protected Filter getFilter(String operator) {
        return operator.equals("soundex")
                ? soundexIndexFilter
                : plainnameIndexFilter;
    }

    /**
     * Returns a filter for converting a condition comparison value into
     * tokens/token frequency tuples.
     *
     * @param operator operator name
     * @return filter
     */
    protected Filter getQueryFilter(String operator) {
        return operator.equals("soundex")
                ? soundexQueryFilter
                : plainnameQueryFilter;
    }

}
