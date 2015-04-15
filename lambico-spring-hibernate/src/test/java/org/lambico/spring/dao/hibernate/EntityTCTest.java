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

import java.util.ArrayList;
import org.lambico.spring.dao.hibernate.dao.TestDaos;
import org.lambico.spring.dao.hibernate.dao.EntityTCDao;
import org.lambico.spring.dao.hibernate.po.EntityTC;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.lambico.dao.hibernate.GenericDaoHibernateCriteriaSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.lambico.test.ExtraAssert.*;

/**
 * Tests on generic DAO using EntityTC.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
public class EntityTCTest extends BaseTest {

    @Resource
    protected TestDaos daos;
    @Resource
    private EntityTCDao entityTCDao;

    @Test
    public void daoExists() {
        assertNotNull(this.daos);
        assertNotNull(this.daos.getEntityTCDao());
    }

    @Test
    public void allSize() {
        assertSize(5, this.daos.getEntityTCDao().findAll());
    }

    @Test
    public void orderByFieldOne() {
        List<EntityTC> entities = this.daos.getEntityTCDao().findByOrderByFieldOne();
        assertNotEmpty(entities);
        EntityTC old = entities.get(0);
        for (int i = 1; i < entities.size(); i++) {
            EntityTC curr = entities.get(i);
            assertTrue("Entities not orderd by fieldOne", old.getFieldOne().compareTo(curr.
                    getFieldOne()) <= 0);
            old = curr;
        }
    }

    @Test
    public void orderByFieldTwo() {
        List<EntityTC> entities = this.daos.getEntityTCDao().findByOrderByFieldTwo();
        assertNotEmpty(entities);
        EntityTC old = entities.get(0);
        for (int i = 1; i < entities.size(); i++) {
            EntityTC curr = entities.get(i);
            assertTrue("Entities not orderd by fieldTwo", old.getFieldTwo().compareTo(curr.
                    getFieldTwo()) <= 0);
            old = curr;
        }
    }

    @Test
    public void orderByFieldOneAndFieldTwo() {
        List<EntityTC> entities = this.daos.getEntityTCDao().findByOrderByFieldOneAndFieldTwo();
        assertNotEmpty(entities);
        EntityTC old = entities.get(0);
        for (int i = 1; i < entities.size(); i++) {
            EntityTC curr = entities.get(i);
            if (old.getFieldOne().equals(curr.getFieldOne())) {
                assertTrue("Entities ordered by fieldOne but not orderd by fieldTwo", old.
                        getFieldTwo().compareTo(curr.getFieldTwo()) <= 0);
            } else {
                assertTrue("Entities not orderd by fieldOne", old.getFieldOne().compareTo(curr.
                        getFieldOne()) <= 0);
            }
            old = curr;
        }
    }

    @Test
    public void findSingleRecord() {
        EntityTC entityTC = this.daos.getEntityTCDao().findByFieldOneOrderByFieldTwo("one1");
        assertNotNull(entityTC);
        assertEquals("one1", entityTC.getFieldOne());
    }

    @Test
    public void findOrderedSingleRecord() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByFieldOne("one3");
        assertSize(2, entities);
        EntityTC entityTC = this.daos.getEntityTCDao().findByFieldOneOrderByFieldTwo("one3");
        assertNotNull(entityTC);
        assertEquals("one3", entityTC.getFieldOne());
        assertEquals("two1", entityTC.getFieldTwo());
    }

    @Test
    public void findByOrderByFieldTwoWithFirstRecordAndMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByOrderByFieldOne(1, 2);
        assertSize(2, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    @Test
    public void findByOrderByFieldTwoWithFirstRecordAndNoMaxRecords() {
        EntityTCDao dao = entityTCDao;
        List<EntityTC> entities = dao.findByOrderByFieldOne(1, -1);
        assertSize(4, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    @Test
    public void searchAllOrderByFieldTwoWithFirstRecordAndMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.searchAllOrderByFieldOne(1, 2);
        assertSize(2, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    @Test
    public void searchAllOrderByFieldTwoWithFirstRecordAndNoMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.searchAllOrderByFieldOne(1, -1);
        assertSize(4, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    @Test
    public void findByFieldThree() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByFieldThree("t%");
        assertSize(4, entities);
    }

    @Test
    public void findAllByCriteria() {
        @SuppressWarnings("unchecked")
        List<EntityTC> results = ((GenericDaoHibernateCriteriaSupport<EntityTC>) entityTCDao).
                searchByCriteria(DetachedCriteria.forClass(EntityTC.class));
        assertSize(5, results);
    }

    @Test
    public void withNamedParameters() {
        List<EntityTC> results = entityTCDao.findByFieldOneAndFieldTwoAndFieldThreeWithName("one3",
                "two3", "Three3");
        assertSize(1, results);
    }

    @Test
    public void searchByFieldOneList() {
        List<String> values = new ArrayList<String>();
        values.add("one1");
        values.add("one3");
        List<EntityTC> result = entityTCDao.searchByFieldOneCollection(values);
        assertSize(3, result);
    }

    @Test
    public void searchByFieldOneArray() {
        String[] values = {"one1", "one3"};
        List<EntityTC> result = entityTCDao.searchByFieldOneArray(values);
        assertSize(3, result);
    }
}
