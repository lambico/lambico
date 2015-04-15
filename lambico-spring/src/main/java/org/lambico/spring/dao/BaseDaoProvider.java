/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring.
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
package org.lambico.spring.dao;

import java.util.Map;
import org.lambico.dao.DaoProvider;
import org.lambico.util.DaoHelper;

/**
 * A class for retrieving DAOs from the context.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class BaseDaoProvider implements DaoProvider {

    /** The map of DAOs. */
    private Map<String, ?> daoMap;

    /** Creates a new instance of BaseDaoProvider. */
    public BaseDaoProvider() {
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Map<String, ? extends Object> getDaoMap() {
        return daoMap;
    }

    /**
     * Set the map of current DAOs.
     *
     * @param daoMap The map of current DAOs.
     */
    public void setDaoMap(final Map<String, ? extends Object> daoMap) {
        this.daoMap = daoMap;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Object getDao(final String daoName) {
        return daoMap.get(daoName);
    }

    /**
     * {@inheritDoc}
     *
     * @param daoEntityType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Object getDao(final Class daoEntityType) {
        Object result = null;
        for (Object elem : daoMap.values()) {
            if (DaoHelper.isDaoFor(elem, daoEntityType)) {
                result = elem;
                break;
            }
        }
        return result;
    }
}
