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
package org.lambico.spring.xml;

import org.w3c.dom.Element;

/**
 * The interface for the discoverers of entity classes.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public interface EntityDiscoverer {

    /**
     * Populate the "session factory" with the persistent classes.
     *
     * @param element The DOM definition element.
     * @param packageName The base package name.
     * @param sessionFactoryName The session factory name.
     */
    void pupulateWithEntities(final Element element, final String packageName,
            final String sessionFactoryName);
}
