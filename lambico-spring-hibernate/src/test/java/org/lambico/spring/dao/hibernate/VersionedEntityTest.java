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

import org.lambico.spring.dao.hibernate.bo.VersionedEntityTCBO;
import org.lambico.spring.dao.hibernate.po.VersionedEntityTC;
import org.lambico.spring.dao.hibernate.po.VersionedEntityDataTC;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * A test case for a versioned entity.
 * 
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class VersionedEntityTest extends BaseTest {
    
    @Resource
    private VersionedEntityTCBO versionedEntityTCBO;

    @Test
    public void storeRetrieve() {
        VersionedEntityTC versionedEntity = new VersionedEntityTC();
        versionedEntity.setName("Test name");
        VersionedEntityDataTC versionedEntityData = new VersionedEntityDataTC();
        versionedEntityData.setBalance(new BigDecimal("100.00"));
        versionedEntityData.setDescription("Test description");
        versionedEntity.updateVersionedData(versionedEntityData);
        this.versionedEntityTCBO.createEntity(versionedEntity);
        VersionedEntityTC retrievedEntity = this.versionedEntityTCBO.retrieveEntity(versionedEntity.getId());
        assertEquals(versionedEntity, retrievedEntity);
    }

    @Test
    public void updateNewVersionedData() {
        VersionedEntityTC versionedEntity = new VersionedEntityTC();
        versionedEntity.setName("Test name");
        VersionedEntityDataTC versionedEntityData = new VersionedEntityDataTC();
        versionedEntityData.setBalance(new BigDecimal("100.00"));
        versionedEntityData.setDescription("Test description");
        versionedEntity.updateVersionedData(versionedEntityData);
        this.versionedEntityTCBO.createEntity(versionedEntity);
        VersionedEntityTC retrievedEntity = this.versionedEntityTCBO.retrieveEntity(versionedEntity.getId());
        List<VersionedEntityDataTC> entityDataVersions = retrievedEntity.getVersionedData();
        assertTrue(!entityDataVersions.isEmpty());
        assertEquals(1, entityDataVersions.size());
        assertTrue(versionedEntityData.getId().equals(versionedEntity.findLastVersionedData().getId()));
        assertNotNull(versionedEntity.findLastVersionedData().getDateFrom());
        assertNull(versionedEntity.findLastVersionedData().getDateTo());
    }

    @Test
    public void updateExistentVersionedData() throws InterruptedException {
        VersionedEntityTC versionedEntity = new VersionedEntityTC();
        versionedEntity.setName("Test name");
        VersionedEntityDataTC versionedEntityData = new VersionedEntityDataTC();
        versionedEntityData.setBalance(new BigDecimal("100.00"));
        versionedEntityData.setDescription("Test description");
        versionedEntity.updateVersionedData(versionedEntityData);
        this.versionedEntityTCBO.createEntity(versionedEntity);
        versionedEntityData = new VersionedEntityDataTC();
        versionedEntityData.setBalance(new BigDecimal("200.00"));
        versionedEntityData.setDescription("Test description modified");
        Thread.sleep(300);
        VersionedEntityTC updatedEntity = this.versionedEntityTCBO.updateVersionedData(versionedEntity.getId(), versionedEntityData);
        VersionedEntityTC retrievedEntity = this.versionedEntityTCBO.retrieveEntity(versionedEntity.getId());
        List<VersionedEntityDataTC> entityDataVersions = retrievedEntity.getVersionedData();
        assertTrue(!entityDataVersions.isEmpty());
        assertEquals(2, entityDataVersions.size());
        assertTrue(versionedEntityData.getDateFrom().equals(retrievedEntity.findLastVersionedData().getDateFrom()));
        assertTrue(entityDataVersions.get(entityDataVersions.size() - 1).getDateFrom().compareTo(
                entityDataVersions.get(entityDataVersions.size() - 2).getDateFrom()) > 0);
    }

    @Test
    public void updateExistentVersionedDataWithLocales() throws InterruptedException {
        VersionedEntityTC versionedEntity = new VersionedEntityTC();
        versionedEntity.setDefaultLocale(Locale.ITALIAN.getLanguage());
        versionedEntity.setName("Test name");
        VersionedEntityDataTC italianVersionedEntityData = new VersionedEntityDataTC();
        italianVersionedEntityData.setLocale(Locale.ITALIAN.getLanguage());
        italianVersionedEntityData.setBalance(new BigDecimal("100.00"));
        italianVersionedEntityData.setDescription("Descrizione in italiano");
        versionedEntity.updateVersionedData(italianVersionedEntityData);
        VersionedEntityDataTC englishVersionedEntityData = new VersionedEntityDataTC();
        englishVersionedEntityData.setLocale(Locale.ENGLISH.getLanguage());
        englishVersionedEntityData.setBalance(new BigDecimal("100.00"));
        englishVersionedEntityData.setDescription("English description");
        versionedEntity.updateVersionedData(englishVersionedEntityData);
        this.versionedEntityTCBO.createEntity(versionedEntity);
        italianVersionedEntityData = new VersionedEntityDataTC();
        italianVersionedEntityData.setLocale(Locale.ITALIAN.getLanguage());
        italianVersionedEntityData.setBalance(new BigDecimal("200.00"));
        italianVersionedEntityData.setDescription("Descrizione in italiano modificata");
        Thread.sleep(3000);
        VersionedEntityTC updatedEntity = this.versionedEntityTCBO.updateVersionedData(versionedEntity.getId(), italianVersionedEntityData);
        englishVersionedEntityData = new VersionedEntityDataTC();
        englishVersionedEntityData.setLocale(Locale.ENGLISH.getLanguage());
        englishVersionedEntityData.setBalance(new BigDecimal("200.00"));
        englishVersionedEntityData.setDescription("Updated english description");
        Thread.sleep(3000);
        updatedEntity = this.versionedEntityTCBO.updateVersionedData(versionedEntity.getId(), englishVersionedEntityData);
        VersionedEntityTC retrievedEntity = this.versionedEntityTCBO.retrieveEntity(versionedEntity.getId());
        List<VersionedEntityDataTC> entityDataVersions = retrievedEntity.getVersionedData();
        assertTrue(!entityDataVersions.isEmpty());
        assertEquals(4, entityDataVersions.size());
        assertTrue(italianVersionedEntityData.getDateFrom().equals(retrievedEntity.findLastVersionedData().getDateFrom()));
        assertTrue(englishVersionedEntityData.getDateFrom().equals(
                retrievedEntity.findLastVersionedData(Locale.ENGLISH.getLanguage()).getDateFrom()));
    }
}
