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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.generic.GenericDaoTypeSupport;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.lambico.dao.hibernate.GenericDaoHibernateCriteriaSupport;
import org.lambico.dao.spring.hibernate.GenericDaoHibernateSupport;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.support.DataAccessUtils;

/**
 * A generic DAO wich can be extended and personalized.
 *
 *
 * @param <T> The entity class type of the DAO.
 * @param <PK> The type of the primary key of the entity.
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @version $Revision$
 */
public class HibernateGenericBusinessDao<T, PK extends Serializable>
        extends HibernateDaoSupport
        implements GenericDaoBase<T, PK>, GenericDaoHibernateCriteriaSupport<T>,
        GenericDaoTypeSupport, GenericDaoHibernateSupport {

    /** The entity class type. */
    private Class<T> persistentClass;

    /**
     * Build the DAO.
     */
    public HibernateGenericBusinessDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().
                getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public Class getType() {
        return this.persistentClass;
    }

    /**
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     */
    public void setType(final Class type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Init the DAO with the session factory.
     *
     * @param sessionFactory The session factory.
     */
    @Resource
    public void initDao(final SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void create(final T o) {
        getHibernateTemplate().persist(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void store(final T o) {
        getHibernateTemplate().merge(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T read(final PK id) {
        return (T) getHibernateTemplate().load(persistentClass, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(final PK id) {
        return (T) getHibernateTemplate().get(persistentClass, id);
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     */
    public void delete(final T o) {
        getHibernateTemplate().delete(o);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getHibernateTemplate().find("from " + persistentClass.getName() + " x");
    }

    /**
     * {@inheritDoc}
     *
     * @param criterion {@inheritDoc}
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(final Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistentClass);
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
    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(final DetachedCriteria criteria) {
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
    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(final DetachedCriteria criteria, final int firstResult,
            final int maxResults) {
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
    @SuppressWarnings("unchecked")
    public Page<T> searchPaginatedByCriteria(final int page, final int pageSize,
            final Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistentClass);
        Criteria count = getSession().createCriteria(persistentClass);
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
    @SuppressWarnings("unchecked")
    public Page<T> searchPaginatedByCriteria(final int page, final int pageSize,
            final DetachedCriteria criteria) {
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
    public long count() {
        return DataAccessUtils.intResult(getHibernateTemplate().find(
                "select count(*) from " + getType().getSimpleName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    public long countByCriteria(final DetachedCriteria criteria) {
        criteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
    }

    /**
     * {@inheritDoc}
     */
    public void rollBackTransaction() {
        final Transaction currTransaction =
                getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction();
        if (currTransaction != null && currTransaction.isActive()
                && !currTransaction.wasRolledBack()) {
            currTransaction.rollback();
        }
    }
}
