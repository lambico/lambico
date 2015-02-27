/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring - Hibernate.
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
package org.lambico.spring.dao.hibernate;

import java.util.Set;
import junit.framework.TestCase;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Tests on the methods of the DaoBeanCreator class.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class HibernateDaoBeanCreatorTest extends TestCase {

    public HibernateDaoBeanCreatorTest(String testName) {
        super(testName);
    }

    private HibernateDaoBeanCreator createInstance() {
        return new HibernateDaoBeanCreator(new PathMatchingResourcePatternResolver(), null, null,
                null);
    }

    /**
     * Test of getDaoInterfaces method, of class DaoBeanCreator.
     */
    public void testGetDaoInterfaces() {
        HibernateDaoBeanCreator instance = createInstance();
        String interfacePackageName = "org.lambico.spring.dao.hibernate";
        Set result = instance.getDaoInterfaces(interfacePackageName);
        assertEquals(7, result.size());
    }
}
