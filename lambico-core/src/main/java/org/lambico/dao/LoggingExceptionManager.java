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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An exception manager that simply log its exception.
 *
 * @author Federico Russo <chiccorusso@gmail.com>
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class LoggingExceptionManager extends DaoExceptionManagerBase {

    /** The logger for this class. */
    private final Logger logger = LoggerFactory.getLogger(LoggingExceptionManager.class);

    /**
     * {@inheritDoc}
     *
     * @param throwable {@inheritDoc}
     * @param properties {@inheritDoc}
     */
    @Override
    public void process(final Throwable throwable, final Properties properties) {
        final StringBuilder errorMessages = new StringBuilder();
        final String methodName = properties.getProperty(METHOD_NAME);
        final String targetClassName = properties.getProperty(DAO_CLASS_NAME);

        errorMessages.append("Method not starting with \"findBy\".").append(
                "Trying to call ").append(methodName).
                append(" method, but the method doesn't exist in the object (").
                append(targetClassName).append(").");
        logger.error(errorMessages.toString(), throwable);
    }
}
