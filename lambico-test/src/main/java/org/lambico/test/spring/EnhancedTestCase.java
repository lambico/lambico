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
package org.lambico.test.spring;

import java.util.Collection;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

/**
 * Adds useful assertions to the standard JUnit TestCase.
 */
public abstract class EnhancedTestCase extends AbstractAnnotationAwareTransactionalTests {

    /**
     * Assert equality between two arrays of bytes.
     *
     * @param expected The expected array.
     * @param actual The inspected array.
     */
    public static void assertEquals(final byte[] expected, final byte[] actual) {
        assertEquals("", expected, actual);
    }

    /**
     * Assert equality between two arrays of bytes.
     *
     * @param description The message in case of inequality.
     * @param expected The expected array.
     * @param actual The inspected array.
     */
    public static void assertEquals(final String description, final byte[] expected,
            final byte[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(description + ": byte[" + i + "] does not match", expected[i], actual[i]);
        }
    }

    /**
     * Assert equality between two arrays of doubles.
     *
     * @param expected The expected array.
     * @param actual The inspected array.
     */
    public static void assertEquals(final double[] expected, final double[] actual) {
        assertEquals("", expected, actual);
    }

    /**
     * Assert equality between two arrays of doubles.
     *
     * @param description The message in case of inequality.
     * @param expected The expected array.
     * @param actual The inspected array.
     */
    public static void assertEquals(final String description, final double[] expected,
            final double[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(description + ": double[" + i + "] does not match",
                    expected[i], actual[i]);
        }
    }

    /**
     * Assert equality of two strings, ignoring the case.
     *
     * @param expected The expected string.
     * @param actual The inspected string.
     */
    public static void assertEqualsIgnoreCase(final String expected, final String actual) {
        assertEquals(expected.toLowerCase(), actual.toLowerCase());
    }

    /**
     * Assert the emptyness of a Collection.
     *
     * @param c The collection to inspect.
     */
    public static void assertEmpty(final Collection c) {
        assertSize(0, c);
    }

    /**
     * Assert the not emptyness of a Collection.
     *
     * @param message The message in case the collection is empty.
     * @param c The collection to inspect.
     */
    public static void assertNotEmpty(final String message, final Collection c) {
        assertTrue(message, c.size() > 0);
    }

    /**
     * Assert the not emptyness of a Collection.
     *
     * @param c The collection to inspect.
     */
    public static void assertNotEmpty(final Collection c) {
        assertTrue(c.size() > 0);
    }

    /**
     * Assert the not emptyness of an array of Objects.
     *
     * @param message The message in case the array is empty.
     * @param array The collection to inspect.
     */
    public static void assertNotEmpty(final String message, final Object[] array) {
        assertTrue(message, array.length > 0);
    }

    /**
     * Assert the not emptyness of an array of Objects.
     *
     * @param array The collection to inspect.
     */
    public static void assertNotEmpty(final Object[] array) {
        assertTrue(array.length > 0);
    }

    /**
     * Assert the size of a Collection.
     *
     * @param size The expected size.
     * @param c The Collection to inspect.
     */
    public static void assertSize(final int size, final Collection c) {
        assertTrue("Size of the collection: expected " + size + ", got "
                + c.size(), c.size() == size);
    }
}
