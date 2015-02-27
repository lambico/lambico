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

import java.util.Properties;

/**
 * A base implementation of a {@link DaoExceptionManager}.
 *
 * @author lucio
 */
public abstract class DaoExceptionManagerBase implements DaoExceptionManager {

    /**
     * {@inheritDoc}
     *
     * @param throwable {@inheritDoc}
     * @param method {@inheritDoc}
     * @param dao {@inheritDoc}
     * @throws Throwable {@inheritDoc}
     */
    @Override
    public void process(final Throwable throwable, final String method, final String dao)
            throws Throwable {
        process(throwable, method, dao, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param throwable {@inheritDoc}
     * @param method {@inheritDoc}
     * @param dao {@inheritDoc}
     * @param properties {@inheritDoc}
     * @throws Throwable {@inheritDoc}
     */
    @Override
    public void process(final Throwable throwable, final String method, final String dao,
            final Properties properties)
            throws Throwable {
        Properties p = new Properties();
        if (properties != null) {
            p.putAll(properties);
        }
        p.put(METHOD_NAME, method);
        p.put(DAO_CLASS_NAME, dao);
        process(throwable, p);
    }
}
