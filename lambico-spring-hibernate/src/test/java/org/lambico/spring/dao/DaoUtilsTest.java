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
package org.lambico.spring.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.generic.GenericDaoTypeSupport;
import org.lambico.spring.po.AnnotatedPO;
import org.lambico.spring.po.BusinessPO;
import org.lambico.spring.po.ExtendPO;
import org.lambico.test.spring.hibernate.junit4.AbstractBaseTest;

/**
 * DAO object identification tests.
 * 
 * @author michele franzin <michele.franzin at seesaw.it>
 */
// TODO move tests in core (or spring) module
public class DaoUtilsTest extends AbstractBaseTest {
 
    @Test
    public void getDaosContainsAnnotatedClass() {
        GenericDaoBase dao = DaoUtils.getDaoFor(AnnotatedPO.class, applicationContext);
        isCompatibleType(dao);
    }

    @Test
    public void getDaosContainsExtendedClass() {
        GenericDaoBase dao = DaoUtils.getDaoFor(ExtendPO.class, applicationContext);
        isCompatibleType(dao);
    }

    @Test
    public void getDaosContainsBusinessClass() {
        GenericDaoBase dao = DaoUtils.getDaoFor(BusinessPO.class, applicationContext);
        isCompatibleType(dao);
    }

    private void isCompatibleType(Object object) {
        assertThat(object, is(notNullValue()));
        assertTrue(GenericDaoTypeSupport.class.isAssignableFrom(object.getClass()));
    }
}
