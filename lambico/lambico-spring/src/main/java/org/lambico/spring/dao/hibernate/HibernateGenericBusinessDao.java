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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.generic.GenericDaoTypeSupport;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.support.DataAccessUtils;

/**
 *
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 */
public class HibernateGenericBusinessDao<T, PK extends Serializable>
        extends HibernateDaoSupport
        implements GenericDaoBase<T, PK>, GenericDaoTypeSupport
         {


    private Class<T> persistentClass;

    public HibernateGenericBusinessDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class getType() {
        return this.persistentClass;
    }

    public void setType(Class type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Resource
    public void initDao(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    public void create(T o) {
       getHibernateTemplate().persist(o);
    }

    @SuppressWarnings("unchecked")
    public void store(T o) {
       getHibernateTemplate().merge(o);
    }

    @SuppressWarnings("unchecked")
    public T read(PK id) {
        return (T) getHibernateTemplate().load(persistentClass, id);
    }

    @SuppressWarnings("unchecked")
    public T get(PK id) {
        return (T) getHibernateTemplate().get(persistentClass, id);
    }
    
    public void delete(T o) {
        getHibernateTemplate().delete(o);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getHibernateTemplate().find("from "+persistentClass.getName()+" x");
    }

    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistentClass);
        for (Criterion c: criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(DetachedCriteria criteria) {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
        return getHibernateTemplate().
                findByCriteria(criteria, firstResult, maxResults);
    }

    @SuppressWarnings("unchecked")
    public Page<T> searchPaginatedByCriteria(int page, int pageSize, Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistentClass);
        Criteria count = getSession().createCriteria(persistentClass);
        for (Criterion c: criterion) {
            crit.add(c);
            count.add(c);
        }

        // row count
        count.setProjection(Projections.rowCount());
        int rowCount = ((Integer)count.list().get(0)).intValue();

        crit.setFirstResult((page-1)*pageSize);
        crit.setMaxResults(pageSize);
        return new PageDefaultImpl<T>(crit.list(), page, pageSize, rowCount);
    }

    @SuppressWarnings("unchecked")
    public Page<T> searchPaginatedByCriteria(int page, int pageSize, DetachedCriteria criteria) {
        // Row count
        criteria.setProjection(Projections.rowCount());
        int rowCount = ((Integer)getHibernateTemplate().
                findByCriteria(criteria).get(0)).intValue();
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        List<T> list = getHibernateTemplate().
                findByCriteria(criteria, (page-1)*pageSize, pageSize);

        return new PageDefaultImpl<T>(list, page, pageSize,rowCount);
    }

    public int deleteAll() {
        List<T> rows = findAll();

        getHibernateTemplate().deleteAll(rows);

        return rows.size();
    }

    public long count() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from " + getType().getSimpleName()));
    }

    public long countByCriteria(DetachedCriteria criteria){
       criteria.setProjection(Projections.rowCount());
       return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
    }

    public void rollBackTransaction() {
        if(getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction() != null 
                && getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().isActive() 
                && !getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().wasRolledBack()) 
            getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().rollback();
    }

    public Object getSupport() {
        return this.getHibernateTemplate();
    }

  }
