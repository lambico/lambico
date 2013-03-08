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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
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
public class DefineDaosBeanDefinitionParser implements BeanDefinitionParser {

    private static Logger logger = LoggerFactory.getLogger(DefineDaosBeanDefinitionParser.class);
    /** The baseEntityPackage attribute. */
    public static final String BASE_ENTITY_PACKAGE_ATTRIBUTE = "baseEntityPackage";
    /** The baseInterfacePackage attribute. */
    public static final String BASE_INTERFACE_PACKAGE_ATTRIBUTE = "baseInterfacePackage";
    /** The genericDao attribute. */
    public static final String GENERIC_DAO_ATTRIBUTE = "genericDao";
    /** The sessionFactoryName attribute. */
    public static final String SESSION_FACTORY_NAME_ATTRIBUTE = "sessionFactoryName";

    /**
     * {@inheritDoc}
     *
     * @param element {@inheritDoc}
     * @param parserContext {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BeanDefinition parse(final Element element, final ParserContext parserContext) {
        String interfacePackageName = element.getAttribute(BASE_INTERFACE_PACKAGE_ATTRIBUTE);
        String entityPackageName = element.getAttribute(BASE_ENTITY_PACKAGE_ATTRIBUTE);
        String genericDaoName = element.getAttribute(GENERIC_DAO_ATTRIBUTE);
        String sessionFactoryName = element.getAttribute(SESSION_FACTORY_NAME_ATTRIBUTE);
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        ResourcePatternResolver resourceLoader =
                (ResourcePatternResolver) parserContext.getReaderContext().getReader().
                getResourceLoader();
        BeanDefinitionRegistry registry = parserContext.getReaderContext().getRegistry();
        DaoBeanCreator daoBeanCreator = createDaoBeanCreator(parserContext);
        try {
            daoBeanCreator.createBeans(element, interfacePackageName, entityPackageName,
                    genericDaoName, sessionFactoryName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Can't create DAO beans.", ex);
        }
        return null;
    }

    /**
     * Creates an instance of the DaoBeanCreator, getting the type form the Spring context.
     *
     * @param parserContext The parseContext.
     * @return The dao bean creator.
     */
    private DaoBeanCreator createDaoBeanCreator(final ParserContext parserContext) {
        String daoBeanCreatorClassName =
                parserContext.getRegistry().getBeanDefinition("lambico.daoBeanCreatorClass").
                getBeanClassName();
        DaoBeanCreator daoBeanCreator = null;
        try {
            daoBeanCreator =
                    (DaoBeanCreator) ClassUtils.getDefaultClassLoader().loadClass(
                    daoBeanCreatorClassName).getConstructor(ParserContext.class).newInstance(
                    parserContext);
        } catch (Exception ex) {
            logger.error("Can't instantiate the DaoBeanCreator: " + daoBeanCreatorClassName, ex);
        }
        return daoBeanCreator;
    }
}
