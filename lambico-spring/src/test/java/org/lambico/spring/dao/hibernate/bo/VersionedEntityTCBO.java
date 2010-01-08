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

package org.lambico.spring.dao.hibernate.bo;

import org.lambico.spring.dao.hibernate.dao.VersionedEntityTCDao;
import org.lambico.spring.dao.hibernate.po.VersionedEntityTC;
import org.lambico.spring.dao.hibernate.po.VersionedEntityDataTC;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A BO to be used for the tests of the versioned entity.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @version $Revision$
 */
@Service
public class VersionedEntityTCBO {
    
    @Resource
    public VersionedEntityTCDao versionedEntityTCDao;
    
    /** Creates a new instance of VersionedEntityTCBO */
    public VersionedEntityTCBO() {
    }
    
    @Transactional()
    public void createEntity(VersionedEntityTC entity) {
        versionedEntityTCDao.create(entity);
    }
    
    @Transactional(readOnly=true)
    public VersionedEntityTC retrieveEntity(Long id) {
        VersionedEntityTC retrievedEntity = this.versionedEntityTCDao.read(id);
        retrievedEntity.getVersionedData().size(); // for initializing lazy collection
        return retrievedEntity;
    }
    
    @Transactional()
    public VersionedEntityTC updateVersionedData(Long id, VersionedEntityDataTC versionedData) {
        VersionedEntityTC retrievedEntity = this.versionedEntityTCDao.read(id);
        retrievedEntity.updateVersionedData(versionedData);
        this.versionedEntityTCDao.store(retrievedEntity);
        return retrievedEntity;
    }
    
}
