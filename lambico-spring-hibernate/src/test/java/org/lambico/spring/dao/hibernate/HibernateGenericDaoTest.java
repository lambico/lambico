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

import org.lambico.spring.dao.hibernate.bo.EntityTCBO;
import org.lambico.spring.dao.hibernate.dao.EntityTCDao;
import org.lambico.spring.dao.hibernate.po.EntityTC;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.lambico.test.ExtraAssert.*;

/**
 * Test case for the generic DAO.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @version $Revision$
 */
public class HibernateGenericDaoTest extends BaseTest {

    @Resource
    private EntityTCBO entityTCBO;

    @Resource
    private EntityTCDao entityTCDao;

    @Test
    public void storeRetrieve() {
        EntityTC entity = new EntityTC();
        this.entityTCBO.createEntity(entity);
        EntityTC retrievedEntity = this.entityTCBO.retrieveEntity(entity.getId());
        assertEquals(entity, retrievedEntity);
    }

    @Test
    public void findAll(){
        List<EntityTC> list = entityTCDao.findAll();
        assertNotNull(list);
        assertSize(5, list);
    }

    @Test
    public void getByFieldOne() {
        EntityTC entity = new EntityTC();
        entity.setFieldOne("ONE");
        this.entityTCBO.createEntity(entity);
        List<EntityTC> result = this.entityTCBO.retrieveEntityByFieldOne("ONE");
        assertTrue(result.size() > 0);
        Iterator<EntityTC> enIt = result.iterator();
        while (enIt.hasNext()) {
            EntityTC elem = enIt.next();
            assertEquals(elem.getFieldOne(), "ONE");
        }
    }

    @Test
    public void getByFieldTwo() {
        EntityTC entity = new EntityTC();
        entity.setFieldTwo("TWO");
        this.entityTCBO.createEntity(entity);
        List<EntityTC> result = this.entityTCBO.retrieveEntityByFieldTwo("TWO");
        assertTrue(result.size() > 0);
        Iterator<EntityTC> enIt = result.iterator();
        while (enIt.hasNext()) {
            EntityTC elem = enIt.next();
            assertEquals(elem.getFieldTwo(), "TWO");
        }
    }

    @Test
    public void getByFieldOneAndFieldTwo() {
        EntityTC entity = new EntityTC();
        entity.setFieldOne("ONEONE");
        entity.setFieldTwo("TWOTWO");
        this.entityTCBO.createEntity(entity);
        List<EntityTC> result = this.entityTCBO.retrieveEntityByFieldOneAndFieldTwo("ONEONE", "TWOTWO");
        assertTrue(result.size() > 0);
        Iterator<EntityTC> enIt = result.iterator();
        while (enIt.hasNext()) {
            EntityTC elem = enIt.next();
            assertEquals(elem.getFieldOne(), "ONEONE");
            assertEquals(elem.getFieldTwo(), "TWOTWO");
        }
    }

    @Test
    public void countByNamedQuery() {
        Long result = this.entityTCDao.countByFieldOne("one3");
        assertEquals(Long.valueOf(2), result);
    }

    @Test
    public void maxByNamedQuery() {
        Long result = this.entityTCDao.maxByFieldOne("one3");
        assertEquals(Long.valueOf(5), result);
    }

    @Test
    public void countByFieldTwo() {
        Long result = entityTCDao.countByFieldTwo("two4");
        assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void countByFieldOneAndFieldThree() {
        Long result = this.entityTCDao.countByFieldOneAndFieldThree("one3", "7hree5");
        assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void countByCriteria() {
        long counted =
                ((HibernateGenericDao) this.entityTCDao).countByCriteria(DetachedCriteria.forClass(EntityTC.class));
        assertEquals(5, counted);
    }

    @Test
    public void countByCriteriaReusingCriteria() {
        final DetachedCriteria crit = DetachedCriteria.forClass(EntityTC.class);
        long counted = ((HibernateGenericDao) this.entityTCDao).countByCriteria(crit);
        assertEquals(5, counted);
        List<EntityTC> items  = ((HibernateGenericDao) this.entityTCDao).searchByCriteria(crit);
        assertSize(5, items);
    }
    
}
