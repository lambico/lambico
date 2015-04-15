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

import org.hibernate.Session;
import org.lambico.dao.generic.GenericDaoTypeSupport;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.HibernateTemplate;

/**
 * Methods of a generic DAO with support for hibernate.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
public interface GenericDaoHibernateSupport extends GenericDaoTypeSupport {
    /**
     * Gets a Spring's Hibernate Template.
     *
     * @return A Spring's Hibernate template.
     */
    HibernateTemplate getHibernateTemplate();

    /**
     * Gets a customized Spring's Hibernate Template, using the DAO
     * configuration.
     * 
     * @deprecated Simply use the {@link #getHibernateTemplate()} method.
     *
     * @return A Spring's Hibernate template.
     */
    @Deprecated
    HibernateTemplate getCustomizedHibernateTemplate();

    /**
     * Set one or more names of Hibernate filters to be activated for all
     * Sessions that this DAO works with.
     * 
     * @param filterNames The list of filters that must be activated.
     */
    public void setFilterNames(String... filterNames);

    /**
     * Return the names of Hibernate filters to be activated, if any.
     * 
     * @return the names of Hibernate filters to be activated, if any.
     */
    public String[] getFilterNames();

    /**
     * Conveniently obtain and customize the current Hibernate Session.
     * @return the Hibernate Session
     * @throws DataAccessResourceFailureException if the Session couldn't be created
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    Session currentCustomizedSession() throws DataAccessResourceFailureException;
    
}
