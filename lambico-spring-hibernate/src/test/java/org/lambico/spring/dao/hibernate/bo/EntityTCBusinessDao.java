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

package org.lambico.spring.dao.hibernate.bo;

import java.util.Collection;
import java.util.List;
import org.lambico.spring.dao.hibernate.po.EntityTC;
import org.lambico.dao.spring.BusinessDao;
import org.lambico.spring.dao.hibernate.HibernateGenericBusinessDao;
import org.lambico.spring.dao.hibernate.dao.EntityTCDao;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 */
@BusinessDao
@Qualifier("entityTCBusinessDao")
public class EntityTCBusinessDao extends HibernateGenericBusinessDao<EntityTC, Long> implements EntityTCDao {

    @Override
    public List<EntityTC> findByFieldOne(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByFieldOneAndFieldTwoAndFieldThreeWithName(String one, String two,
            String three) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByFieldTwo(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByFieldThree(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByFieldOneAndFieldTwo(String one, String two) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByOrderByFieldOne() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByOrderByFieldTwo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByOrderByFieldOneAndFieldTwo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EntityTC findByFieldOneOrderByFieldTwo(String one) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> findByOrderByFieldOne(int firstResult, int maxResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> searchAllOrderByFieldOne(int firstResult, int maxResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long countByFieldOne(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer countByFieldTwo(String two) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer countByFieldOneAndFieldThree(String one, String three) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long maxByFieldOne(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> searchByFieldOneCollection(Collection<String> values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<EntityTC> searchByFieldOneArray(String[] values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
