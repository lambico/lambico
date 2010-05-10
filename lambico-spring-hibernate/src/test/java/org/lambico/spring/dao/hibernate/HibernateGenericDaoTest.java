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

    public void testStoreRetrieve() {
        EntityTC entity = new EntityTC();
        this.entityTCBO.createEntity(entity);
        EntityTC retrievedEntity = this.entityTCBO.retrieveEntity(entity.getId());
        assertEquals(entity, retrievedEntity);
    }

    public void testFindAll(){
        List<EntityTC> list = entityTCDao.findAll();
        assertNotNull(list);
        assertSize(5, list);
    }

    public void testGetByFieldOne() {
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

    public void testGetByFieldTwo() {
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

    public void testGetByFieldOneAndFieldTwo() {
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

    public void testCountByNamedQuery() {
        Long result = this.entityTCDao.countByFieldOne("one3");
        assertEquals(Long.valueOf(2), result);
    }

    public void testMaxByNamedQuery() {
        Long result = this.entityTCDao.maxByFieldOne("one3");
        assertEquals(Long.valueOf(5), result);
    }

    public void testCountByFieldTwo() {
        Integer result = entityTCDao.countByFieldTwo("two4");
        assertEquals(Integer.valueOf(1), result);
    }

    public void testCountByFieldOneAndFieldThree() {
        Integer result = this.entityTCDao.countByFieldOneAndFieldThree("one3", "7hree5");
        assertEquals(Integer.valueOf(1), result);
    }
}
