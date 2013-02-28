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

import java.util.List;

/**
 * The interface tham must be implemented by an entity with versioned localized data.
 *
 * @param <T> The versioned data class.
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public interface VersionedEntity<T extends VersionedData> extends Entity {

    /**
     * Return the collection of versioned localized data.
     * @return The collection of versioned localized data
     */
    List<T> getVersionedData();

    /**
     * Set the collection of versioned localized data.
     * @param versionedData The collection of versioned localized data
     */
    void setVersionedData(List<T> versionedData);

    /**
     * Return the default locale for this entity.
     * @return The default locale
     */
    String getDefaultLocale();

    /**
     * Set the default locale for this entity.
     * @param locale The default locale
     */
    void setDefaultLocale(String locale);
}
