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

import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.criterion.Projections;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.support.DataAccessUtils;

/**
 * Hibernate implementation of the generic DAO.
 *
 * Derived from
 * <a
 * href="http://www-128.ibm.com/developerworks/java/library/j-genericdao.html">
 * http://www-128.ibm.com/developerworks/java/library/j-genericdao.html</a>
 *
 * @param <T> The entity class type of the DAO.
 * @param <PK> The type of the primary key of the entity.
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @version $Revision$
 */
public class HibernateGenericDaoImpl<T, PK extends Serializable>
        extends HibernateDaoSupport
        implements HibernateGenericDao<T, PK> {

    /**
     * The DAO entity type.
     */
    private Class type;

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    public final void create(final T o) {
        getHibernateTemplate().persist(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    public final void store(final T o) {
        getHibernateTemplate().merge(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final T read(final PK id) {
        return (T) getHibernateTemplate().load(getType(), id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final T get(final PK id) {
        return (T) getHibernateTemplate().get(getType(), id);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    public final void delete(final T o) {
        getHibernateTemplate().delete(o);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final List<T> findAll() {
        return getHibernateTemplate().find(
                "from " + getType().getName() + " x");
    }

    /**
     * {@inheritDoc}
     *
     * @param criterion {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final List<T> searchByCriteria(final Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getType());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final List<T> searchByCriteria(final DetachedCriteria criteria) {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @param firstResult {@inheritDoc}
     * @param maxResults {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final List<T> searchByCriteria(final DetachedCriteria criteria,
            final int firstResult, final int maxResults) {
        return getHibernateTemplate().
                findByCriteria(criteria, firstResult, maxResults);
    }

    /**
     * {@inheritDoc}
     *
     * @param page {@inheritDoc}
     * @param pageSize {@inheritDoc}
     * @param criterion {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final Page<T> searchPaginatedByCriteria(final int page,
            final int pageSize, final Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getType());
        Criteria count = getSession().createCriteria(getType());
        for (Criterion c : criterion) {
            crit.add(c);
            count.add(c);
        }

        // row count
        count.setProjection(Projections.rowCount());
        int rowCount = ((Integer) count.list().get(0)).intValue();

        crit.setFirstResult((page - 1) * pageSize);
        crit.setMaxResults(pageSize);
        return new PageDefaultImpl<T>(crit.list(), page, pageSize, rowCount);
    }

    /**
     * {@inheritDoc}
     *
     * @param page {@inheritDoc}
     * @param pageSize {@inheritDoc}
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final Page<T> searchPaginatedByCriteria(final int page,
            final int pageSize, final DetachedCriteria criteria) {
        // Row count
        criteria.setProjection(Projections.rowCount());
        int rowCount = ((Integer) getHibernateTemplate().
                findByCriteria(criteria).get(0)).intValue();
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        List<T> list = getHibernateTemplate().
                findByCriteria(criteria, (page - 1) * pageSize, pageSize);

        return new PageDefaultImpl<T>(list, page, pageSize, rowCount);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public int deleteAll() {
        List<T> rows = findAll();

        getHibernateTemplate().deleteAll(rows);

        return rows.size();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final long count() {
        return DataAccessUtils.intResult(
                getHibernateTemplate().find(
                "select count(*) from " + getType().getSimpleName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final long countByCriteria(final DetachedCriteria criteria) {
        criteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(
                criteria));
    }

    /**
     * {@inheritDoc}
     */
    public final void rollBackTransaction() {
        if (getHibernateTemplate().getSessionFactory().getCurrentSession().
                getTransaction() != null
                && getHibernateTemplate().getSessionFactory().
                getCurrentSession().getTransaction().isActive()
                && !getHibernateTemplate().getSessionFactory().
                getCurrentSession().getTransaction().wasRolledBack()) {
            getHibernateTemplate().getSessionFactory().getCurrentSession().
                    getTransaction().rollback();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final Class getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     *
     * @param newType {@inheritDoc}
     */
    public final void setType(final Class newType) {
        this.type = newType;
    }

    /**
     * {@inheritDoc}
     *
     * This implementation returns an
     * {@link org.springframework.orm.hibernate3.HibernateTemplate} object.
     *
     * @return {@inheritDoc}
     */
    public final Object getSupport() {
        return this.getHibernateTemplate();
    }
}
