/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of lambico-test.
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

package org.lambico.test.spring;

import java.util.Collection;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

/**
 * Adds useful assertions to the standard JUnit TestCase
 */
public abstract class EnhancedTestCase extends AbstractAnnotationAwareTransactionalTests {

    public static void assertEquals(byte[] expected, byte[] actual) {
        assertEquals("", expected, actual);
    }

    public static void assertEquals(String description, byte[] expected, byte[] actual) {
        for (int i = 0; i < expected.length; i++) {
            byte expected_b = expected[i];
            byte actual_b = actual[i];
            assertEquals(description + ": byte[" + i + "] does not match", expected_b, actual_b);
        }
    }

    public static void assertEquals(double[] expected, double[] actual) {
        assertEquals("", expected, actual);
    }

    public static void assertEquals(String description, double[] expected, double[] actual) {
        for (int i = 0; i < expected.length; i++) {
            double expected_b = expected[i];
            double actual_b = actual[i];
            assertEquals(description + ": double[" + i + "] does not match", expected_b, actual_b);
        }
    }

    public static void assertEqualsIgnoreCase(String expected, String actual) {
        assertEquals(expected.toLowerCase(), actual.toLowerCase());
    }

    public static void assertEmpty(Collection c) {
        assertSize(0, c);
    }

    public static void assertNotEmpty(String message, Collection c) {
        assertTrue(message, c.size() > 0);
    }

    public static void assertNotEmpty(Collection c) {
        assertTrue(c.size() > 0);
    }

    public static void assertNotEmpty(String message, Object[] array) {
        assertTrue(message, array.length > 0);
    }

    public static void assertNotEmpty(Object[] array) {
        assertTrue(array.length > 0);
    }

    public static void assertSize(int size, Collection c) {
        assertTrue("Size of the collection: expected " + size + ", got " + c.size(), c.size() == size);
    }
}