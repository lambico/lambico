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

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.lambico.dao.generic.Page;

/**
 * Methods with the use of criteria for DAOs on Hibernate.
 *
 * @param <T> The entity class type of the DAO.
 * @author Lucio Benfante <lucio.benfante at gmail.com>
 */
public interface GenericDaoHibernateCriteriaSupport<T> {

    /**
     * Search using a set of Criteria.
     *
     * @param criterion The set of Criteria.
     * @return The result of the search with the provided criteria.
     */
    List<T> searchByCriteria(Criterion... criterion);

    /**
     * Search using a criteria.
     *
     * @param criteria The Criteria.
     * @return The result of the search with the provided criteria.
     */
    List<T> searchByCriteria(DetachedCriteria criteria);

    /**
     * Search using a criteria with limits.
     *
     * @param criteria The Criteria.
     * @param firstResult The index of the first result.
     * @param maxResults The max number of returned results.
     * @return The result of the search with the provided criteria.
     */
    List<T> searchByCriteria(DetachedCriteria criteria, int firstResult, int maxResults);

    /**
     * Search using a criteria with limits, returning a Page.
     *
     * @param criteria The Criteria.
     * @param firstResult The index of the first result.
     * @param maxResults The max number of returned results.
     * @return The result of the search with the provided criteria.
     */
    Page<T> searchPaginatedByCriteria(DetachedCriteria criteria, int firstResult, int maxResults);
    
    /**
     * Search using a set of Criteria with pagination.
     *
     * @param page The requested page index.
     * @param pageSize The page width, i.e. the max number of elements on each page.
     * @param criterion The set of Criteria.
     * @return The result of the search with the provided criteria.
     */
    Page<T> searchPaginatedByCriteria(int page, int pageSize, Criterion... criterion);

    /**
     * Search using a criteria with pagination.
     *
     * @param page The requested page index.
     * @param pageSize The page width, i.e. the max number of elements on each page.
     * @param criteria The Criteria.
     * @return The result of the search with the provided criteria.
     */
    Page<T> searchPaginatedByCriteria(int page, int pageSize, DetachedCriteria criteria);

    /**
     * Search using a criteria with pagination without counting the total records.
     *
     * @param page The requested page index.
     * @param pageSize The page width, i.e. the max number of elements on each page.
     * @param totalRecords The (supposed or pre-calculated) total number of records from the execution of the query.
     * @param criteria The Criteria.
     * @return The result of the search with the provided criteria.
     */
    Page<T> searchPaginatedByCriteria(int page, int pageSize, int totalRecords, DetachedCriteria criteria);
    
    /**
     * Count on the result of a search using a Criteria.
     *
     * @param criteria The Criteria.
     * @return The number of instances in the result.
     */
    long countByCriteria(DetachedCriteria criteria);
}
