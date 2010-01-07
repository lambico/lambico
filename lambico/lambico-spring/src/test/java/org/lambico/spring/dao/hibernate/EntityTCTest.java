/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring.
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
import org.hibernate.criterion.DetachedCriteria;
import org.lambico.dao.hibernate.GenericDaoHibernateCriteriaSupport;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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

    public void testDaoExists() {
        assertNotNull(this.daos);
        assertNotNull(this.daos.getEntityTCDao());
    }

    public void testAllSize() {
        assertSize(5, this.daos.getEntityTCDao().findAll());
    }

    public void testOrderByFieldOne() {
        List<EntityTC> entities = this.daos.getEntityTCDao().findByOrderByFieldOne();
        assertNotEmpty(entities);
        EntityTC old = entities.get(0);
        for (int i = 1; i < entities.size(); i++) {
            EntityTC curr = entities.get(i);
            assertTrue("Entities not orderd by fieldOne", old.getFieldOne().compareTo(curr.getFieldOne()) <= 0);
            old = curr;
        }
    }

    public void testOrderByFieldTwo() {
        List<EntityTC> entities = this.daos.getEntityTCDao().findByOrderByFieldTwo();
        assertNotEmpty(entities);
        EntityTC old = entities.get(0);
        for (int i = 1; i < entities.size(); i++) {
            EntityTC curr = entities.get(i);
            assertTrue("Entities not orderd by fieldTwo", old.getFieldTwo().compareTo(curr.getFieldTwo()) <= 0);
            old = curr;
        }
    }

    public void testOrderByFieldOneAndFieldTwo() {
        List<EntityTC> entities = this.daos.getEntityTCDao().findByOrderByFieldOneAndFieldTwo();
        assertNotEmpty(entities);
        EntityTC old = entities.get(0);
        for (int i = 1; i < entities.size(); i++) {
            EntityTC curr = entities.get(i);
            if (old.getFieldOne().equals(curr.getFieldOne())) {
                assertTrue("Entities ordered by fieldOne but not orderd by fieldTwo", old.getFieldTwo().compareTo(curr.getFieldTwo()) <= 0);
            } else {
                assertTrue("Entities not orderd by fieldOne", old.getFieldOne().compareTo(curr.getFieldOne()) <= 0);
            }
            old = curr;
        }
    }

    public void testFindSingleRecord() {
        EntityTC entityTC = this.daos.getEntityTCDao().findByFieldOneOrderByFieldTwo("one1");
        assertNotNull(entityTC);
        assertEquals("one1", entityTC.getFieldOne());
    }

    public void testFindOrderedSingleRecord() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByFieldOne("one3");
        assertSize(2, entities);
        EntityTC entityTC = this.daos.getEntityTCDao().findByFieldOneOrderByFieldTwo("one3");
        assertNotNull(entityTC);
        assertEquals("one3", entityTC.getFieldOne());
        assertEquals("two1", entityTC.getFieldTwo());
    }

    public void testFindByOrderByFieldTwoWithFirstRecordAndMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByOrderByFieldOne(1, 2);
        assertSize(2, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    public void testFindByOrderByFieldTwoWithFirstRecordAndNoMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByOrderByFieldOne(1, -1);
        assertSize(4, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    public void testSearchAllOrderByFieldTwoWithFirstRecordAndMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.searchAllOrderByFieldOne(1, 2);
        assertSize(2, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    public void testSearchAllOrderByFieldTwoWithFirstRecordAndNoMaxRecords() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.searchAllOrderByFieldOne(1, -1);
        assertSize(4, entities);
        assertEquals("one2", entities.get(0).getFieldOne());
    }

    public void testFindByFieldThree() {
        EntityTCDao dao = this.daos.getEntityTCDao();
        List<EntityTC> entities = dao.findByFieldThree("t%");
        assertSize(4, entities);
    }

    public void testFindAllByCriteria() {
        List<EntityTC> results = ((GenericDaoHibernateCriteriaSupport<EntityTC, Long>) entityTCDao)
                .searchByCriteria(DetachedCriteria.forClass(EntityTC.class));
        assertSize(5, results);
    }
}
