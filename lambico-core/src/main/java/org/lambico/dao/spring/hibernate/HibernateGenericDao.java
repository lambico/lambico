/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Core.
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
package org.lambico.dao.spring.hibernate;

import java.io.Serializable;
import org.lambico.dao.generic.GenericDao;
import org.lambico.dao.hibernate.GenericDaoHibernateCriteriaSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;

/**
 * The interface for the Spring Hibernate generic DAO.
 *
 * @param <T> The entity class type of the DAO.
 * @param <PK> The type of the primary key of the entity.
 * @author Lucio Benfante
 */
public interface HibernateGenericDao<T, PK extends Serializable>
        extends GenericDao<T, PK>, GenericDaoHibernateCriteriaSupport<T>,
        GenericDaoHibernateSupport {
    
    <T> T doExecute(HibernateCallback<T> action) throws DataAccessException;
    
}
