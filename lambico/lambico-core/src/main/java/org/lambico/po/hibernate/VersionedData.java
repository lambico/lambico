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

import java.util.Date;

/**
 * The interface tham must be implemented by a set of versioned localized data.
 *
 * @param <T> The versioned entity class.
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public interface VersionedData<T extends VersionedEntity> extends Entity {

    /**
     * Return the entity of this versioned data.
     * @return The entity
     */
    T getEntity();

    /**
     * Set the entity of this versioned data.
     * @param entity The entity
     */
    void setEntity(T entity);

    /**
     * Return the date of starting validity of this versioned data.
     * @return The start date
     */
    Date getDateFrom();

    /**
     * Set the date of starting validity of this versioned data.
     * @param dateFrom The start date
     */
    void setDateFrom(Date dateFrom);

    /**
     * Return the date of end validity of this versioned data.
     * @return The start date. null for no end of validity.
     */
    Date getDateTo();

    /**
     * Set the date of starting validity of this versioned data.
     * @param dateTo The end date. null for no end of validity.
     */
    void setDateTo(Date dateTo);

    /**
     * Return the locale of this version of data.
     * @return The locale
     */
    String getLocale();

    /**
     * Set the locale of this version of data.
     * @param locale The locale
     */
    void setLocale(String locale);
}
