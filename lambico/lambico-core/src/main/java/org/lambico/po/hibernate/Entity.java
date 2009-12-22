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

/**
 * Common interface for a persistent entity.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public interface Entity {
    /**
     * Return the entity id
     * 
     * @return The entity id
     */
    Long getId();

    /**
     * Return the version of this entity
     * 
     * @return The version of this entity
     */
//    Integer getVersion();

    /**
     * Set the entity id
     * 
     * @param id The entity id
     */
    void setId(Long id);

    /**
     * Set the version of this entity
     * 
     * @param version The version
//     */
//    void setVersion(Integer version);
    
}
