/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Test.
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
package org.lambico.test;

import java.util.Collection;
import static org.junit.Assert.*;

/**
 * A set of assertion methods useful for writing tests.
 * 
 * @author Lucio Benfante
 * @since 4.0
 */
public class ExtraAssert {
    
    protected ExtraAssert() {
    }
    
    /**
     * Assers that a collection is not empty. It fails if the collection is null.
     * 
     * @param c the collection
     */
    static public void assertNotEmpty(Collection c) {
        assertNotNull(c);
        assertNotEquals(0, c.size());
    }
    
    /**
     * Asserts that a collection contains the expected number of elements. It fails if the collection
     * is null.
     * 
     * @param expected expected value
     * @param c the collection
     */
    static public void assertSize(int expected, Collection c) {
        assertNotNull(c);
        assertEquals(expected, c.size());
    }
    
}
