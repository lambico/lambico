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
import org.springframework.stereotype.Service;

/**
 * A BO to be used for the tests of the versioned entity.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @version $Revision$
 */
@Service
public class EntityTCBO {
    
    @Resource
    private EntityTCDao entityTCDao;
    
    /** Creates a new instance of EntityTCBO */
    public EntityTCBO() {
    }
    
    public void createEntity(EntityTC entity) {
        entityTCDao.create(entity);
    }
    
    public EntityTC retrieveEntity(Long id) {
        EntityTC retrievedEntity = entityTCDao.read(id);
        return retrievedEntity;
    }

    List<EntityTC> retrieveEntityByFieldOne(String value) {
        return entityTCDao.findByFieldOne(value);
    }

    List<EntityTC> retrieveEntityByFieldTwo(String value) {
        return entityTCDao.findByFieldTwo(value);
    }
    
    List<EntityTC> retrieveEntityByFieldOneAndFieldTwo(String one, String two) {
        return entityTCDao.findByFieldOneAndFieldTwo(one, two);
    }
    
}
