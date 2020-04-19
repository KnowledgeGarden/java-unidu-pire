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

// $Id: SoundexFilter.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.text;

/**
 * This filter converts a specified string into its soundex representation. This
 * is useful for testing whether strings sound the same.
 * <p>
 * <p>
 * This implementation uses the nonfree version (not packaged here) if available
 * (for backwards compatibility), and the Apache Jakarta Commons Codec
 * implementation otherwise.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 * @since 2003-07-04
 */
public class SoundexFilter extends AbstractSingleItemFilter {

    /**
     * Soundex filter to which calls are delegated.
     */
    protected SingleItemFilter soundexFilter;

    /**
     * Creates a new instance and sets the next filter in the chain.
     *
     * @param nextFilter next filter in the filter chain
     */
    public SoundexFilter(Filter nextFilter) {
        super(nextFilter);
        try {
            // test for nonfree version
            soundexFilter = (SingleItemFilter) Class.forName(
                    "de.unidu.is.text.NonFreeSoundexFilter").getConstructor(
                    new Class[]{Filter.class}).newInstance(new Object[]{null});
        } catch (Exception e) {
            // use Codec instead
            soundexFilter = new CodecSoundexFilter(null);
        }
    }

    /**
     * Converts the specified object into its soundex value.
     *
     * @param value string to be converted
     * @return soundex representation of the specified value
     */
    public Object run(Object value) {
        return soundexFilter.run(value);
    }

}