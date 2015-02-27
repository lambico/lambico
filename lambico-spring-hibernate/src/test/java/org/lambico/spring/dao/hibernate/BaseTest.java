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

import org.lambico.spring.dao.hibernate.po.AuthorTC;
import org.lambico.spring.dao.hibernate.po.BookTC;
import org.lambico.spring.dao.hibernate.po.EntityTC;
import org.lambico.spring.dao.hibernate.po.EntityTCNoInheritance;
import org.lambico.spring.dao.hibernate.po.EntityTCWithoutDaoInterface;
import org.lambico.spring.dao.hibernate.pobis.EntityTCBis;
import org.lambico.spring.dao.hibernate.poter.EntityTCTer;
import org.lambico.test.spring.hibernate.junit4.AbstractBaseTest;
import org.lambico.test.spring.hibernate.junit4.FixtureSet;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * A base class for lambico tests.
 *
 * @author lucio
 * @author michele franzin <michele at franzin.net>
 */
@FixtureSet(modelClasses = {EntityTC.class, EntityTCBis.class, EntityTCTer.class,
    EntityTCWithoutDaoInterface.class, EntityTCNoInheritance.class, BookTC.class, AuthorTC.class})
public abstract class BaseTest extends AbstractBaseTest {

    @BeforeTransaction
    public void beforeTransaction() throws Exception {
        this.jdbcTemplate.execute("SET DATABASE REFERENTIAL INTEGRITY FALSE");
    }
    
    @AfterTransaction
    public void afterTransaction() throws Exception {
        this.jdbcTemplate.execute("SET DATABASE REFERENTIAL INTEGRITY TRUE");
    }
    
}
