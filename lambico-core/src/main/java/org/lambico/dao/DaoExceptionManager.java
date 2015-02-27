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
import org.lambico.util.ExceptionManager;

/**
 * An exception manager used for calls to the methods of DAOs.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public interface DaoExceptionManager extends ExceptionManager {
    /** The name of the property containing the method that caused the exception. */
    String METHOD_NAME = "methodName";
    /** The name of the property containing the DAO class that caused the exception. */
    String DAO_CLASS_NAME = "daoClassName";

    /**
     * Process an exception.
     *
     * @param throwable The exception to process.
     * @param method The method that caused the exception.
     * @param dao The DAO class that caused the exception.
     * @throws Throwable Any needed exception, or maybe a re-throw.
     */
    void process(final Throwable throwable, final String method, final String dao) throws Throwable;

    /**
     * Process an exception.
     *
     * @param throwable The exception to process.
     * @param method The method that caused the exception.
     * @param dao The DAO class that caused the exception.
     * @param properties Properties useful in the exception processing.
     * @throws Throwable Any needed exception, or maybe a re-throw.
     */
    void process(final Throwable throwable, final String method, final String dao,
            final Properties properties) throws Throwable;
}
