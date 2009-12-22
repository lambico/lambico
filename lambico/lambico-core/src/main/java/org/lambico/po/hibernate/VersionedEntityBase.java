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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * The standard abstract implementation for an entity with versioned localized data.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
@MappedSuperclass
public abstract class VersionedEntityBase<T extends VersionedData> extends EntityBase implements VersionedEntity<T> {
    
    /**
     * The collection of versioned data
     */
    protected List<T> versionedData;
    /**
     * The default locale for the entity
     */
    protected String defaultLocale;
    
    /**
     * Creates a new instance of VersionedEntityBase
     */
    public VersionedEntityBase() {
    }
    
    @OneToMany(mappedBy="entity", cascade=CascadeType.ALL)
    @OrderBy(value="dateFrom")
    public List<T> getVersionedData() {
        return this.versionedData;
    }
    
    public void setVersionedData(List<T> versionedData) {
        this.versionedData = versionedData;
    }
    
    public String getDefaultLocale() {
        return defaultLocale;
    }
    
    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
    
    /**
     * Find the last versioned data, using the default locale.
     * @return The last versioned data
     */
    public T findLastVersionedData() {
        return findLastVersionedData(this.getDefaultLocale());
    }
    
    /**
     * TODO to complete
     *
     * Find the last versioned data, using the passed locale.
     * @return The last versioned data
     */
    public T findLastVersionedData(String locale) {
        T result = null;
        if (this.getVersionedData() != null && !this.getVersionedData().isEmpty()) {
            for (int i = this.getVersionedData().size()-1; i >= 0; i--) {
                T current = this.getVersionedData().get(i);
                if (locale == null) {
                    if (current.getLocale() == null || current.getLocale().equals(this.getDefaultLocale())) {
                        result = current;
                        break;
                    }
                } else {
                    if (locale.equals(current.getLocale())) {
                        result = current;
                        break;
                    } else if (current.getLocale() == null && locale.equals(this.getDefaultLocale())) {
                        result = current;
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Update the versioned data.
     * @param newVersionData The new version of data of this entity
     */
    @SuppressWarnings("unchecked")
    public void updateVersionedData(T newVersionData) {
        newVersionData.setEntity(this);
        if (this.getVersionedData() == null) {
            this.setVersionedData(new ArrayList<T>());
        }
        Date current = new Date();
        T last = findLastVersionedData(newVersionData.getLocale());
        if (last != null) {
            if (!current.after(last.getDateFrom())) {
                // sometimes it happens with fast machines
                current.setTime(last.getDateFrom().getTime()+1000);
            }
            last.setDateTo(current);
        }
        newVersionData.setDateFrom(current);
        this.getVersionedData().add(newVersionData);
    }
    
    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return this.getId().hashCode();
        } else {
            return super.hashCode();
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VersionedEntityBase)) {
            return false;
        }
        VersionedEntityBase q = (VersionedEntityBase)obj;
        return (this.getId() == null ? q.getId() == null : this.getId().equals(q.getId()));
    }
}
