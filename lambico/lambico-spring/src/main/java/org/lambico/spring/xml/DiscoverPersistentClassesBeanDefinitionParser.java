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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Element;

/**
 * The parser for the define-daos definition.
 *
 * Based on an idea (and code) of Chris Richardson:
 *
 * CHECKSTYLE:OFF
 * <a href="http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm">http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm</a>
 * CHECKSTYLE:ON
 */
public class DiscoverPersistentClassesBeanDefinitionParser implements BeanDefinitionParser {

    /** The basePackage attribute. */
    public static final String BASE_PACKAGE_ATTRIBUTE = "basePackage";
    /** The sessionFactoryName attribute. */
    public static final String SESSION_FACTORY_NAME_ATTRIBUTE = "sessionFactoryName";

    /**
     * {@inheritDoc}
     *
     * @param element {@inheritDoc}
     * @param parserContext {@inheritDoc}
     * @return {@inheritDoc}
     */
    public BeanDefinition parse(final Element element, final ParserContext parserContext) {
        String packageName = element.getAttribute(BASE_PACKAGE_ATTRIBUTE);
        String sessionFactoryName = element.getAttribute(SESSION_FACTORY_NAME_ATTRIBUTE);
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        ResourcePatternResolver resourceLoader = (ResourcePatternResolver) parserContext.
                getReaderContext().getReader().getResourceLoader();
        BeanDefinitionRegistry registry = parserContext.getReaderContext().getRegistry();
        SessionFactoryPopulator sessionFactoryPopulator = new SessionFactoryPopulator(
                resourceLoader, registry, delegate, parserContext.getReaderContext());
        sessionFactoryPopulator.populateSessionFactory(element, packageName, sessionFactoryName);
        return null;
    }
}
