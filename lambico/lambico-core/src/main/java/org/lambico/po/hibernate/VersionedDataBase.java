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
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
@MappedSuperclass
public class VersionedDataBase<T extends VersionedEntity> extends EntityBase
        implements Serializable, VersionedData<T> {
    
    protected T entity;
    protected Date dateFrom;
    protected Date dateTo;
    protected String locale;
    
    /** Creates a new instance of VersionedDataBase */
    public VersionedDataBase() {
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateFrom() {
        return this.dateFrom;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateTo() {
        return this.dateTo;
    }
    
    @ManyToOne(cascade = {CascadeType.ALL})
    public T getEntity() {
        return this.entity;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
    public void setEntity(T entity) {
        this.entity = entity;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
}
