/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of lambico-spring.
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

import org.lambico.spring.dao.DaoUtils;
import java.util.Map;
import org.lambico.test.spring.hibernate.DBTest;

/**
 * Tests for the DaoProvider classes.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class DaoProviderTest extends DBTest {
    
    /**
     * Test the retrieval of the DAO map.
     */
    public void testGetDaoMap() {
        Map daoMap = (Map) getApplicationContext().getBean("daoMap");
        assertNotNull(daoMap);
        assertTrue("Test DAO map shouldn't be empty", !daoMap.isEmpty());
    }

    /**
     * Test the retrieval of a DAO from the DAO map.
     */
    public void testGetDaoFromMap() {
        Map daoMap = (Map) getApplicationContext().getBean("daoMap");
        assertNotNull(daoMap);
        Object dao = daoMap.get("entityTCDao");
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of the DAO map from the generic dao provider.
     */
    public void testGetDaoMapFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) getApplicationContext().getBean("daos");
        assertNotNull(baseDaoProvider);
        Map daoMap = baseDaoProvider.getDaoMap();
        assertNotNull(daoMap);
        assertTrue("Test DAO map shouldn't be empty", !daoMap.isEmpty());
    }

    /**
     * Test the retrieval of a DAO from the generic dao provider.
     */
    public void testGetDaoFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) getApplicationContext().getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getDao("entityTCDao");
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of a DAO for an entity from the generic dao provider.
     */
    public void testGetDaoByEntityFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) getApplicationContext().getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getDao(EntityTC.class);
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of a DAO for an entity from the generic dao provider.
     */
    public void testGetDaoByMethodFromProvider() {
        TestDaos baseDaoProvider = (TestDaos) getApplicationContext().getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getEntityTCDao();
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }
    
}
