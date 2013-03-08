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

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * The handler for the lambico namespace.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class LambicoNamespaceHandler extends NamespaceHandlerSupport {

    /**
     * Initialization method.
     */
    public void init() {
        registerBeanDefinitionParser("dao", new DaoBeanDefinitionParser());
        registerBeanDefinitionParser("define-daos", new DefineDaosBeanDefinitionParser());
        registerBeanDefinitionParser("discover-persistent-classes",
                new DiscoverPersistentClassesBeanDefinitionParser());
    }
}
