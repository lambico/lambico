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
package org.lambico.dao.generic;

/**
 * Support for cache configuration in the DAOs.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @since 4.0
 * @version $Revision$
 */
public interface GenericDaoCacheSupport {
    /**
     * Returns true if the cache is active at class level.
     *
     * @return true if the cache is active at class level.
     */
    boolean isClassLevelCacheQueries();
    /**
     * Sets the flag for activating the cache at class level.
     *
     * @param classLevelCacheQueries true id active.
     */
    void setClassLevelCacheQueries(boolean classLevelCacheQueries);
}
