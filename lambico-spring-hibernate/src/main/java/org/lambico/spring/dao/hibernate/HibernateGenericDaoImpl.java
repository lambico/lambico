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

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.criterion.Projections;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.springframework.orm.hibernate3.HibernateTemplate;
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
     * A customized hibernate template.
     */
    private HibernateTemplate customizedHibernateTemplate;

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    public final void create(final T o) {
        getCustomizedHibernateTemplate().persist(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    public final void store(final T o) {
        getCustomizedHibernateTemplate().merge(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final T read(final PK id) {
        return (T) getCustomizedHibernateTemplate().load(getType(), id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final T get(final PK id) {
        return (T) getCustomizedHibernateTemplate().get(getType(), id);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    public final void delete(final T o) {
        getCustomizedHibernateTemplate().delete(o);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final List<T> findAll() {
        return getCustomizedHibernateTemplate().find(
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
        return getCustomizedHibernateTemplate().findByCriteria(criteria);
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
        return getCustomizedHibernateTemplate().
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
        int rowCount = ((Number) count.list().get(0)).intValue();

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
        int rowCount = ((Number) getHibernateTemplate().
                findByCriteria(criteria).get(0)).intValue();
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        List<T> list = getCustomizedHibernateTemplate().
                findByCriteria(criteria, (page - 1) * pageSize, pageSize);

        return new PageDefaultImpl<T>(list, page, pageSize, rowCount);
    }
    
    /**
     * {@inheritDoc}
     *
     * @param page {@inheritDoc}
     * @param pageSize {@inheritDoc}
     * @param totalRecords {@inheritDoc}
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Page<T> searchPaginatedByCriteria(int page, int pageSize, int totalRecords,
            DetachedCriteria criteria) {
        List<T> list = getCustomizedHibernateTemplate().
                findByCriteria(criteria, (page - 1) * pageSize, pageSize);

        return new PageDefaultImpl<T>(list, page, pageSize, totalRecords);
    }    

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public int deleteAll() {
        List<T> rows = findAll();

        getCustomizedHibernateTemplate().deleteAll(rows);

        return rows.size();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final long count() {
        return DataAccessUtils.intResult(
                getCustomizedHibernateTemplate().find(
                "select count(*) from " + getType().getSimpleName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    public final long countByCriteria(final DetachedCriteria criteria) {
        DetachedCriteria countCriteria = (DetachedCriteria) SerializationUtils.clone(criteria);
        countCriteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(getCustomizedHibernateTemplate().findByCriteria(
                countCriteria));
    }

    /**
     * {@inheritDoc}
     */
    public final void rollBackTransaction() {
        final SessionFactory currSessionFactory =
                getCustomizedHibernateTemplate().getSessionFactory();
        final Session currentSession = currSessionFactory.getCurrentSession();
        final Transaction transaction = currentSession.getTransaction();
        if (transaction != null
                && transaction.isActive()
                && !transaction.wasRolledBack()) {
            transaction.rollback();
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
     * It creates the instance of customized hibernate template.
     *
     * @throws Exception {@inheritDoc}
     */
    @Override
    protected void initDao() throws Exception {
        super.initDao();
        this.customizedHibernateTemplate = new HibernateTemplate(getSessionFactory());
    }

    /**
     * Return an Hibernate template with a customized configuration, usually
     * obtained from the DAO definition.
     *
     * @return A customized hibernate template.
     */
    @Override
    public HibernateTemplate getCustomizedHibernateTemplate() {
        return this.customizedHibernateTemplate;
    }
}
