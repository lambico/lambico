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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.lambico.dao.generic.CacheIt;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.generic.GenericDaoTypeSupport;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.lambico.dao.hibernate.GenericDaoHibernateCriteriaSupport;
import org.lambico.dao.spring.BusinessDao;
import org.lambico.dao.spring.hibernate.GenericDaoHibernateSupport;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate4.HibernateTemplate;

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
    private final Class<T> persistentClass;
    /**
     * A customized hibernate template.
     */
    private HibernateTemplate customizedHibernateTemplate;

    /**
     * Build the DAO.
     */
    @SuppressWarnings("unchecked")
    public HibernateGenericBusinessDao() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            genericSuperclass = ((Class)genericSuperclass).getGenericSuperclass();
        }
        this.persistentClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Class getType() {
        return this.persistentClass;
    }

    /**
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     */
    @Override
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
    @Override
    public void create(final T o) {
        getHibernateTemplate().persist(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void store(final T o) {
        getHibernateTemplate().saveOrUpdate(o);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
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
    @Override
    public T get(final PK id) {
        return (T) getHibernateTemplate().get(persistentClass, id);
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     */
    @Override
    public void delete(final T o) {
        getHibernateTemplate().delete(o);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return (List<T>) getHibernateTemplate().find("from " + persistentClass.getName() + " x");
    }

    /**
     * {@inheritDoc}
     *
     * @param criterion {@inheritDoc}
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> searchByCriteria(final Criterion... criterion) {
        Criteria crit = currentSession().createCriteria(persistentClass);
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
    @Override
    public List<T> searchByCriteria(final DetachedCriteria criteria) {
        return (List<T>) getHibernateTemplate().findByCriteria(criteria);
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
    @Override
    public List<T> searchByCriteria(final DetachedCriteria criteria, final int firstResult,
            final int maxResults) {
        return (List<T>) getHibernateTemplate().
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
    @Override
    public Page<T> searchPaginatedByCriteria(final int page, final int pageSize,
            final Criterion... criterion) {
        Criteria crit = currentSession().createCriteria(persistentClass);
        Criteria count = currentSession().createCriteria(persistentClass);
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
    @Override
    public Page<T> searchPaginatedByCriteria(final int page, final int pageSize,
            final DetachedCriteria criteria) {
        // Row count
        criteria.setProjection(Projections.rowCount());
        int rowCount = ((Integer) getHibernateTemplate().
                findByCriteria(criteria).get(0)).intValue();
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) getHibernateTemplate().
                findByCriteria(criteria, (page - 1) * pageSize, pageSize);

        return new PageDefaultImpl<T>(list, page, pageSize, rowCount);
    }

    @Override
    public Page<T> searchPaginatedByCriteria(int page, int pageSize, int totalRecords,
            DetachedCriteria criteria) {
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) getHibernateTemplate().
                findByCriteria(criteria, (page - 1) * pageSize, pageSize);

        return new PageDefaultImpl<T>(list, page, pageSize, totalRecords);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
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
    @Override
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
    @Override
    public long countByCriteria(final DetachedCriteria criteria) {
        criteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollBackTransaction() {
        final Transaction currTransaction =
                getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction();
        if (currTransaction != null && currTransaction.isActive()
                && !currTransaction.wasRolledBack()) {
            currTransaction.rollback();
        }
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
     * Return an Hibernate template with a customized configuration, obtained
     * from the DAO definition.
     *
     * If the DAO is annotated with the {@link CacheIt} annotation,
     * it activate the query cache for the returned template.
     *
     * @return A customized hibernate template.
     */
    @Override
    public HibernateTemplate getCustomizedHibernateTemplate() {
        HibernateTemplate result = this.customizedHibernateTemplate;
        Class<?>[] interfaces = getClass().getInterfaces();
        for (Class<?> iface : interfaces) {
            if (null != iface.getAnnotation(BusinessDao.class)
                    && null != iface.getAnnotation(CacheIt.class)) {
                result.setCacheQueries(true);
                break;
            }
        }
        return result;
    }

}
