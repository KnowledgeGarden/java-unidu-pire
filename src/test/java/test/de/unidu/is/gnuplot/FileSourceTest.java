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


// $Id: FileSourceTest.java,v 1.5 2005/03/09 09:00:19 nottelma Exp $
package test.de.unidu.is.gnuplot;

import de.unidu.is.gnuplot.FileSource;

import java.io.File;

/**
 * @author nottelma
 * @version $Revision: 1.5 $, $Date: 2005/03/09 09:00:19 $
 * @since Aug 2, 2003
 */
public class FileSourceTest extends SourceTest {

    protected final String FILENAME = "tmp.SOURCETEST";
    protected FileSource fileSource;

    /**
     * Constructor for FileSourceTest.
     *
     * @param arg0
     */
    public FileSourceTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() {
        fileSource = new FileSource(TITLE, FILENAME);
        source = fileSource;
    }

    public void testGetCommand() {
        String command = fileSource.getCommand();
        assertTrue(command.contains("'" + FILENAME + "'"));
    }

    /*
     * Test for void FileSource(String, File)
     */
    public void testFileSourceStringFile() {
        File file = new File(FILENAME);
        assertEquals(new FileSource(TITLE, file).getFilename(), FILENAME);
    }

    public void testSetFilename() {
        final String FN = "FN";
        assertEquals(fileSource.getFilename(), FILENAME);
        fileSource.setFilename(FN);
        assertEquals(fileSource.getFilename(), FN);
    }

    public void testSetSmooth() {
        fileSource.setSmooth(true);
        assertTrue(fileSource.isSmooth());
        fileSource.setSmooth(true);
        assertTrue(fileSource.isSmooth());
        fileSource.setSmooth(false);
        assertFalse(fileSource.isSmooth());
        fileSource.setSmooth(true);
        assertTrue(fileSource.isSmooth());
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() {
        new File(FILENAME).delete();
    }

}
