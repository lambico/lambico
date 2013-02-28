/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring - Hibernate.
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
package org.lambico.spring.dao.hibernate;

import java.util.List;

import javax.persistence.Entity;
import org.lambico.spring.xml.ContextUtils;
import org.lambico.spring.xml.EntityDiscoverer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Element;

/**
 * A populator for the Lambico session factory.
 * It automagically populate the session factory with persistent classes.
 *
 * Based on an idea (and code) of Chris Richardson:
 *
 * CHECKSTYLE:OFF
 * <a href="http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm">http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm</a>
 * CHECKSTYLE:ON
 *
 * @author Lucio Benfante <lucio.benfante at gmail dot com>
 */
public class SessionFactoryPopulator implements EntityDiscoverer {

    private static Logger logger = LoggerFactory.getLogger(SessionFactoryPopulator.class);
    /** The ResourcePatternResolver. */
    private ResourcePatternResolver rl;
    /** The BeanDefinition registry. */
    private BeanDefinitionRegistry registry;
    /** The BeanDefinitionParserDelegate. */
    private BeanDefinitionParserDelegate delegate;
    /** The ReaderContext. */
    private final ReaderContext readerContext;

    /**
     * The constructor.
     *
     * @param parserContext The ParserContext.
     */
    public SessionFactoryPopulator(final ParserContext parserContext) {
        this.readerContext = parserContext.getReaderContext();
        this.rl = (ResourcePatternResolver) parserContext.getReaderContext().getReader().
                getResourceLoader();
        this.registry = parserContext.getReaderContext().getRegistry();
        this.delegate = parserContext.getDelegate();
    }

    /**
     * The constructor.
     *
     * @param resourcePatternResolver The ResourcePatternResolver.
     * @param beanDefinitionRegistry The BeanDefinitionRegistry.
     * @param parserDelegate The BeanDefinitionParserDelegate.
     * @param readerContext The ReaderContext.
     */
    public SessionFactoryPopulator(final ResourcePatternResolver resourcePatternResolver,
            final BeanDefinitionRegistry beanDefinitionRegistry,
            final BeanDefinitionParserDelegate parserDelegate,
            final ReaderContext readerContext) {
        this.readerContext = readerContext;
        this.rl = resourcePatternResolver;
        this.registry = beanDefinitionRegistry;
        this.delegate = parserDelegate;
    }

    /**
     * Populate the SessionFactory with the persistent classes.
     *
     * @param element The DOM definition element.
     * @param packageName The base package name.
     * @param sessionFactoryName The session factory name.
     */
    @Override
    public void pupulateWithEntities(final Element element, final String packageName,
            final String sessionFactoryName) {
        List<Class> allClasses = ContextUtils.getAllClasses(rl, readerContext, packageName);
        List<Class> persistentClasses = getPersistentClasses(allClasses);
        BeanDefinition sessionFactoryBeanDefinition =
                registry.getBeanDefinition(sessionFactoryName);
        addPersistentClasses(persistentClasses, sessionFactoryBeanDefinition);
    }

    /**
     * Filter the list of classes extracting only the DAO interfaces.
     *
     * @param classes The full list of classes
     * @return The filtered list
     */
    List<Class> getPersistentClasses(final List<Class> classes) {
        return ContextUtils.getClassesByAnnotation(classes, Entity.class);
    }

    /**
     * Add the persistent classes to the SessionFactory.
     *
     * @param persistentClasses The list of persistent classes.
     * @param sessionFactoryBeanDefinition The session factory bean definition.
     */
    private void addPersistentClasses(final List<Class> persistentClasses,
            final BeanDefinition sessionFactoryBeanDefinition) {
        List<TypedStringValue> result = null;
        PropertyValue annotatedClassesProperty = sessionFactoryBeanDefinition.getPropertyValues().
                getPropertyValue("annotatedClasses");
        if (annotatedClassesProperty == null) {
            result = new ManagedList();
        } else {
            result = (List) annotatedClassesProperty.getValue();
        }
        for (Class current : persistentClasses) {
            TypedStringValue currentValue = new TypedStringValue(current.getName());
            result.add(currentValue);
        }
        if (annotatedClassesProperty == null) {
            sessionFactoryBeanDefinition.getPropertyValues().
                    addPropertyValue(new PropertyValue("annotatedClasses", result));
        }
    }
}
