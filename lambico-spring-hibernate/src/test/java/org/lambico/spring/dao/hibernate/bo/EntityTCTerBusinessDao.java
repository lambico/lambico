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

import java.util.LinkedList;
import java.util.List;
import org.lambico.spring.dao.hibernate.HibernateGenericBusinessDao;
import org.lambico.spring.dao.hibernate.daoter.EntityTCTerDao;
import org.lambico.spring.dao.hibernate.poter.EntityTCTer;

/**
 * A DAO overriding a method of the base DAO.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class EntityTCTerBusinessDao extends HibernateGenericBusinessDao<EntityTCTer, Long> implements EntityTCTerDao {

    @Override
    public List<EntityTCTer> findAll() {
        final LinkedList<EntityTCTer> result =
                new LinkedList<EntityTCTer>();
        result.add(new EntityTCTer());
        return result;
    }

    @Override
    public List<EntityTCTer> findByFieldOne(String fieldOne) {
        final LinkedList<EntityTCTer> result =
                new LinkedList<EntityTCTer>();
        return result;
    }

}
