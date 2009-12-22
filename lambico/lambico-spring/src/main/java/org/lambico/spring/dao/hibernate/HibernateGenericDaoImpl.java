/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of lambico-spring.
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
 * Derived from http://www-128.ibm.com/developerworks/java/library/j-genericdao.html
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @version $Revision$
 */
public class HibernateGenericDaoImpl <T, PK extends Serializable>
        extends HibernateDaoSupport
        implements HibernateGenericDao<T, PK> {
    private Class type;
            
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
        return (T) getHibernateTemplate().load(getType(), id);
    }

    @SuppressWarnings("unchecked")
    public T get(PK id) {
        return (T) getHibernateTemplate().get(getType(), id);
    }
    
    public void delete(T o) {
        getHibernateTemplate().delete(o);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getHibernateTemplate().find("from "+getType().getName()+" x");
    }

    @SuppressWarnings("unchecked")
    public List<T> searchByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getType());
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
        Criteria crit = getSession().createCriteria(getType());
        Criteria count = getSession().createCriteria(getType());
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

    public Class getType() {
        return type;
    }
    
    public void setType(Class type) {
        this.type = type;
    }

    public Object getSupport() {
        return this.getHibernateTemplate();
    }
}
