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

import org.hibernate.Transaction;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.criterion.Projections;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.util.Assert;

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
    private Class<T> type;
    /**
     * The flag for activating the cache at class level.
     */
    private boolean classLevelCacheQueries;
    /**
     * The names of the filters that must be activated.
     */
    private String[] filterNames;

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    @Override
    public final void create(final T o) {
        doExecute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                session.persist(o);
                return null;
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    @Override
    public final void store(final T o) {
        doExecute(new HibernateCallback<T>() {
            @Override
            public T doInHibernate(Session session) throws HibernateException {
                return (T) session.merge(o);
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final T read(final PK id) {
        return doExecute(new HibernateCallback<T>() {
            @Override
            public T doInHibernate(Session session) throws HibernateException {
                return (T) session.load(getType(), id);
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final T get(final PK id) {
        return doExecute(new HibernateCallback<T>() {
            @Override
            public T doInHibernate(Session session) throws HibernateException {
                return (T) session.get(getType(), id);
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     */
    @Override
    public final void delete(final T o) {
        doExecute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                session.delete(o);
                return null;
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final List<T> findAll() {
        return doExecute(new HibernateCallback<List<T>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<T> doInHibernate(Session session) throws HibernateException {
                    Query queryObject = session.createQuery("from "+ getType().getName()+" x");
                    prepare(queryObject);
                    return queryObject.list();
            }

        });
    }
    
    /**
     * {@inheritDoc}
     *
     * @param criterion {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final List<T> searchByCriteria(final Criterion... criterion) {
        Criteria crit = currentCustomizedSession().createCriteria(getType());
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
    @Override
    @SuppressWarnings("unchecked")
    public final List<T> searchByCriteria(final DetachedCriteria criteria) {
        return searchByCriteria(criteria, -1, -1);
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @param firstResult {@inheritDoc}
     * @param maxResults {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final List<T> searchByCriteria(final DetachedCriteria criteria,
            final int firstResult, final int maxResults) {
        List<T> result = doExecute(new HibernateCallback<List<T>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<T> doInHibernate(Session session) throws HibernateException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);
                prepare(executableCriteria);
                if (firstResult >= 0) {
                    executableCriteria.setFirstResult(firstResult);
                }
                if (maxResults > 0) {
                    executableCriteria.setMaxResults(maxResults);
                }
                return executableCriteria.list();
            }
        });
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param page {@inheritDoc}
     * @param pageSize {@inheritDoc}
     * @param criterion {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final Page<T> searchPaginatedByCriteria(final int page,
            final int pageSize, final Criterion... criterion) {
        Criteria crit = currentCustomizedSession().createCriteria(getType());
        Criteria count = currentCustomizedSession().createCriteria(getType());
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
    @Override
    public final Page<T> searchPaginatedByCriteria(final int page,
            final int pageSize, final DetachedCriteria criteria) {
        // Row count
        criteria.setProjection(Projections.rowCount());
        int rowCount = ((Number) searchByCriteria(criteria).get(0)).intValue();
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        return searchPaginatedByCriteria(page, pageSize, rowCount, criteria);
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
    @Override
    public Page<T> searchPaginatedByCriteria(final int page, final int pageSize,
            final int totalRecords, final DetachedCriteria criteria) {
        @SuppressWarnings("unchecked")
        List<T> result = searchByCriteria(criteria, (page - 1) * pageSize, pageSize);
        return new PageDefaultImpl<T>(result, page, pageSize, totalRecords);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int deleteAll() {
        final List<T> rows = findAll();
        doExecute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                for (Object row : rows) {
                    session.delete(row);
                }
                return null;
            }
        });
        return rows.size();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final long count() {
        return DataAccessUtils.intResult(doExecute(new HibernateCallback<List<T>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<T> doInHibernate(Session session) throws HibernateException {
                    Query queryObject = session.createQuery("select count(*) from " + getType().getSimpleName());
                    prepare(queryObject);
                    return queryObject.list();
            }
        }));
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final long countByCriteria(final DetachedCriteria criteria) {
        DetachedCriteria countCriteria = (DetachedCriteria) SerializationUtils.clone(criteria);
        countCriteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(searchByCriteria(countCriteria));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void rollBackTransaction() {
        final Session currentSession = currentSession();
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
    @Override
    public final Class getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     *
     * @param newType {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
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
        getHibernateTemplate().setCacheQueries(isClassLevelCacheQueries());
    }

    /**
     * Return an Hibernate template with a customized configuration, usually
     * obtained from the DAO definition.
     *
     * @return A customized hibernate template.
     */
    @Override
    @Deprecated
    public HibernateTemplate getCustomizedHibernateTemplate() {
        return this.getHibernateTemplate();
    }

    @Override
    public void setFilterNames(String... filterNames) {
        if (this.filterNames != null && this.filterNames.length > 0) {
            disableFilters(currentSession());
        }
        this.filterNames = filterNames;
    }

    @Override
    public String[] getFilterNames() {
        return this.filterNames;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isClassLevelCacheQueries() {
        return classLevelCacheQueries;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setClassLevelCacheQueries(boolean classLevelCacheQueries) {
        this.classLevelCacheQueries = classLevelCacheQueries;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc }
     * @throws DataAccessResourceFailureException {@inheritDoc }
     * @see {@inheritDoc }
     */
    @Override
    public final Session currentCustomizedSession() throws DataAccessResourceFailureException {
        Session session = currentSession();
        enableFilters(session);
        return session;
    }

    private void enableFilters(Session session) {
        if (getFilterNames() != null) {
            for (String filterName : getFilterNames()) {
                session.enableFilter(filterName);
            }
        }
    }

    private void disableFilters(Session session) {
        if (getFilterNames() != null) {
            for (String filterName : getFilterNames()) {
                session.disableFilter(filterName);
            }
        }
    }

    @Override
    public <T> T doExecute(HibernateCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");

        Session session = null;
        boolean isNew = false;
        try {
                session = currentCustomizedSession();
        } catch (HibernateException ex) {
            logger.debug("Could not retrieve pre-bound Hibernate session", ex);
        }
        if (session == null) {
            session = getSessionFactory().openSession();
            session.setFlushMode(FlushMode.MANUAL);
            enableFilters(session);
            isNew = true;
        }

        try {
            return action.doInHibernate(session);
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        } catch (RuntimeException ex) {
            // Callback code threw application exception...
            throw ex;
        } finally {
            if (isNew) {
                SessionFactoryUtils.closeSession(session);
            }
	}
    }

    private void prepare(Query queryObject) {
        if (this.isClassLevelCacheQueries()) {
            queryObject.setCacheable(true);
        }
    }
    
    protected void prepare(Criteria criteria) {
        if (this.isClassLevelCacheQueries()) {
            criteria.setCacheable(true);
        }
    }
    
}
