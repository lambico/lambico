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

package org.lambico.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.lambico.dao.generic.Page;

/**
 * Methods with the use of criteria for DAOs on Hibernate.
 * 
 * @author Lucio Benfante <lucio.benfante at gmail.com>
 */
public interface GenericDaoHibernateCriteriaSupport<T, PK extends Serializable> {

    List<T> searchByCriteria(Criterion... criterion);

    List<T> searchByCriteria(DetachedCriteria criteria);

    List<T> searchByCriteria(DetachedCriteria criteria, int firstResult, int maxResults);

    Page<T> searchPaginatedByCriteria(int page, int pageSize, Criterion... criterion);

    Page<T> searchPaginatedByCriteria(int page, int pageSize, DetachedCriteria criteria);
    
    long countByCriteria(DetachedCriteria criteria);

}
