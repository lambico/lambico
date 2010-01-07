/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico test.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lambico.test.spring.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import org.lambico.test.spring.Utils;

public class UtilsTest extends TestCase {

    public void testConvertToNameValueList() {
        Map<String, String> input = new HashMap<String, String>();
        input.put("A", "B");
        input.put("C", "D");
        input.put("E", "F");
        List<String> expected = new ArrayList<String>();
        // changed add order in the list
        expected.add("E=F");
        expected.add("A=B");
        expected.add("C=D");

        assertTrue(expected.containsAll(Utils.convertToNameValueList(input)));
    }

    public void testBinaryStripUTF8preamble() {
        byte[] stringWithPreamble = Utils.loadBinary("testdata/UTF8WithPreamble.txt");
        byte[] stringWithoutPreamble = Utils.loadBinary("testdata/UTF8WithoutPreamble.txt");

        assertTrue(Utils.hasUTF8preamble(stringWithPreamble));
        assertFalse(Utils.hasUTF8preamble(stringWithoutPreamble));

        byte[] stripped = Utils.stripUTF8preamble(stringWithPreamble);
        assertFalse(Utils.hasUTF8preamble(stripped));
        assertEquals(stringWithPreamble.length, stripped.length + 3);
    }

    public void testStringStripUTF8preamble() throws Exception {
        String stringWithPreamble = Utils.loadString("testdata/UTF8WithPreamble.txt");
        String stringWithoutPreamble = Utils.loadString("testdata/UTF8WithoutPreamble.txt");

        assertFalse(Utils.hasUTF8preamble(stringWithPreamble));
        assertFalse(Utils.hasUTF8preamble(stringWithoutPreamble));
    }
}
