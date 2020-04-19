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


// $Id: StemmerFilterTest.java,v 1.4 2005/02/21 17:29:29 huesselbeck Exp $
package de.unidu.is.text;

import de.unidu.is.text.StemmerFilter;
import junit.framework.TestCase;

/**
 * @author nottelma
 * @version $Revision: 1.4 $, $Date: 2005/02/21 17:29:29 $
 * @since Jul 8, 2003
 */
public class StemmerFilterTest extends TestCase {

    private StemmerFilter filter;

    /**
     * Constructor for SoundexFilterTest.
     *
     * @param arg0
     */
    public StemmerFilterTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() {
        filter = new StemmerFilter(null);
    }

    public void testRun() {
        assertEquals(filter.run("jumps"), filter.run("jump"));
    }

    public void testRunIdemPotency() {
        assertEquals(filter.run("jump"), filter.run("jump"));
    }

    public void testRunNotSame() {
        assertNotSame(filter.run("hello"), filter.run("world"));
    }

}
