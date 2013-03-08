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
package org.lambico.dao;

import java.util.Map;

/**
 * The interface for the basic Dao provider.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public interface DaoProvider {

    /**
     * Get a Map containing the DAOs. The keys are the dao names.
     *
     * @return A map of the DAOs.
     */
    Map<String, ?> getDaoMap();

    /**
     * Get a dao with a specified name.
     *
     * @param daoName The name of the DAO
     * @return The dao. null if the dao doesn't exist.
     */
    Object getDao(String daoName);

    /**
     * Get a dao for an entity type.
     *
     * @param daoEntityType The DAO entity type
     * @return The dao. null if the dao doesn't exist.
     */
    Object getDao(Class daoEntityType);
}
