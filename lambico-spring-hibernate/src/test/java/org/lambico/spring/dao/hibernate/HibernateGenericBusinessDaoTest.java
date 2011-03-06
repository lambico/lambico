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

import org.lambico.spring.dao.hibernate.po.EntityTC;
import java.util.List;
import javax.annotation.Resource;
import org.lambico.spring.dao.hibernate.dao.EntityTCDao;

/**
 *
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 */
public class HibernateGenericBusinessDaoTest  extends BaseTest {
    
    @Resource(name="entityTCBusinessDao")
    private EntityTCDao entityTCBusinessDao;
    
    public void testStoreRetrieve() {
        EntityTC entity = new EntityTC();
        entityTCBusinessDao.store(entity);
        assert(entity.getId() > 0);
        entity.setFieldOne("pippo");
        entityTCBusinessDao.store(entity);
        entity = entityTCBusinessDao.read(entity.getId());
        assertEquals("pippo", entity.getFieldOne());
    }

    public void testFindAll(){
        List<EntityTC> list = entityTCBusinessDao.findAll();
        assertNotNull(list);
    }
    
}