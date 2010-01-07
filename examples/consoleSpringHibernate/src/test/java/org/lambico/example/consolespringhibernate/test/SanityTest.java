/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Example - Console Spring Hibernate.
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

package org.lambico.example.consolespringhibernate.test;

import javax.annotation.Resource;
import org.lambico.example.consolespringhibernate.bo.PersonBO;
import org.lambico.example.consolespringhibernate.dao.PersonDao;

/**
 * Sanity tests
 * 
 * @author Lucio Benfante
 */
public class SanityTest extends BaseTest {

    @Resource
    private PersonDao personDao;
    @Resource
    private PersonBO personBO;

    /**
     * Test the wiring of BO resources.
     */
    public void testBoResources() {
        assertNotNull(personBO);
    }

    /**
     * Test the wiring of DAO resources.
     */
    public void testDaoResources() {
        assertNotNull(personDao);
    }
}
