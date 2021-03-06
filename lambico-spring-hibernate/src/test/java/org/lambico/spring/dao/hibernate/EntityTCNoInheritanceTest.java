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

import java.util.List;
import javax.annotation.Resource;
import org.lambico.dao.generic.GenericDao;
import org.lambico.spring.dao.hibernate.po.EntityTCNoInheritance;

/**
 * Tests on generic DAO without defining a specific DAO interface.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 * @version $Revision$
 */
public class EntityTCNoInheritanceTest extends BaseTest {

    @Resource
    private GenericDao<EntityTCNoInheritance, Short> entityTCNoInheritanceDao;

    public void testDaoExists() {
        assertNotNull(entityTCNoInheritanceDao);
    }

    public void testAllSize() {
        final List<EntityTCNoInheritance> result = entityTCNoInheritanceDao.findAll();
        assertSize(5, result);
        EntityTCNoInheritance testCast = result.get(0);
    }

    public void testEmpty() {}
}
