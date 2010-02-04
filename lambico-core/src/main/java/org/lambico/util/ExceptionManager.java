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

package org.lambico.util;

import java.util.Properties;

/**
 * Interface for classes that manage exceptions.
 *
 * @author Federico Russo <chiccorusso@gmail.com>
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public interface ExceptionManager {

// TODO: move them to the correct place.
//    String METHOD_NAME = "methodName";
//    String TARGET_CLASS_NAME = "targetClassName";

    /**
     * Process an exception.
     *
     * @param throwable The exception to process.
     * @param properties Properties useful in the exception processin.
     * @throws Throwable Any needed exception, or maybe a re-throw.
     */
    void process(final Throwable throwable, final Properties properties) throws Throwable;
}
