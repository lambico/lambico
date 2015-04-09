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
package org.lambico.po.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Base class for classes mantaining versioned localized data.
 *
 * @param <T> The versioned entity class.
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
@MappedSuperclass
public class VersionedDataBase<T extends VersionedEntity> extends EntityBase
        implements Serializable, VersionedData<T> {

    /**
     * The main entity.
     */
    protected T entity;
    /**
     * The start validity date.
     */
    protected Date dateFrom;
    /**
     * The end validity date.
     */
    protected Date dateTo;
    /**
     * The locale for this version of the data.
     */
    protected String locale;

    /** Creates a new instance of VersionedDataBase. */
    public VersionedDataBase() {
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Override
    public Date getDateFrom() {
        return this.dateFrom;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Override
    public Date getDateTo() {
        return this.dateTo;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @ManyToOne(cascade = { CascadeType.ALL })
    @Override
    public T getEntity() {
        return this.entity;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String getLocale() {
        return this.locale;
    }

    /**
     * {@inheritDoc}
     *
     * @param dateFrom {@inheritDoc}
     */
    @Override
    public void setDateFrom(final Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * {@inheritDoc}
     *
     * @param dateTo {@inheritDoc}
     */
    @Override
    public void setDateTo(final Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * {@inheritDoc}
     *
     * @param entity {@inheritDoc}
     */
    @Override
    public void setEntity(final T entity) {
        this.entity = entity;
    }

    /**
     * {@inheritDoc}
     *
     * @param locale {@inheritDoc}
     */
    @Override
    public void setLocale(final String locale) {
        this.locale = locale;
    }
}
