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

import org.lambico.spring.dao.hibernate.dao.TestDaos;
import org.lambico.spring.dao.hibernate.po.EntityTC;
import org.lambico.spring.dao.DaoUtils;
import java.util.Map;
import org.junit.Test;
import org.lambico.test.spring.hibernate.junit4.AbstractBaseTest;
import static org.junit.Assert.*;

/**
 * Tests for the DaoProvider classes.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class DaoProviderTest extends AbstractBaseTest {

    /**
     * Test the retrieval of the DAO map.
     */
    @Test
    public void testGetDaoMap() {
        Map contextDaoMap = (Map) this.applicationContext.getBean("daoMap");
        assertNotNull(contextDaoMap);
        assertTrue("Test DAO map shouldn't be empty", !contextDaoMap.isEmpty());
    }

    /**
     * Test the retrieval of a DAO from the DAO map.
     */
    @Test
    public void testGetDaoFromMap() {
        Map contextDaoMap = (Map) this.applicationContext.getBean("daoMap");
        assertNotNull(contextDaoMap);
        Object dao = contextDaoMap.get("entityTCDao");
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of the DAO map from the generic dao provider.
     */
    @Test
    public void testGetDaoMapFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) this.applicationContext.getBean("daos");
        assertNotNull(baseDaoProvider);
        Map contextDaoMap = baseDaoProvider.getDaoMap();
        assertNotNull(contextDaoMap);
        assertTrue("Test DAO map shouldn't be empty", !contextDaoMap.isEmpty());
    }

    /**
     * Test the retrieval of a DAO from the generic dao provider.
     */
    @Test
    public void testGetDaoFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) this.applicationContext.getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getDao("entityTCDao");
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of a DAO for an entity from the generic dao provider.
     */
    @Test
    public void testGetDaoByEntityFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) this.applicationContext.getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getDao(EntityTC.class);
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of a DAO for an entity from the generic dao provider.
     */
    @Test
    public void testGetDaoByMethodFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) this.applicationContext.getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getEntityTCDao();
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

}
