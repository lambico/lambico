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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import org.apache.log4j.Logger;

import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
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
public class SessionFactoryPopulator {

    /** The logger for this class. */
    private static Logger logger = Logger.getLogger(DaoBeanCreator.class);
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
    void populateSessionFactory(final Element element, final String packageName,
            final String sessionFactoryName) {
        List<Class> allClasses = getAllClasses(packageName);
        List<Class> persistentClasses = getPersistentClasses(allClasses);
        BeanDefinition sessionFactoryBeanDefinition =
                registry.getBeanDefinition(sessionFactoryName);
        addPersistentClasses(persistentClasses, sessionFactoryBeanDefinition);
    }

    /**
     * Send a fatal messag to the context.
     *
     * @param e The cause.
     */
    private void fatal(final Throwable e) {
        readerContext.fatal(e.getMessage(), null, e);
    }

    /**
     * Return all classes in the package subtree.
     *
     * @param packageName The base package
     * @return The listo of all classes
     */
    List<Class> getAllClasses(final String packageName) {
        List<Class> result = new ArrayList<Class>();
        if (StringUtils.hasText(packageName)) {
            try {
                String packagePart = packageName.replace('.', '/');
                String classPattern = "classpath*:/" + packagePart + "/**/*.class";
                Resource[] resources = rl.getResources(classPattern);
                for (int i = 0; i < resources.length; i++) {
                    Resource resource = resources[i];
                    String fileName = resource.getURL().toString();
                    String className = fileName.substring(
                            fileName.indexOf(packagePart),
                            fileName.length() - ".class".length()).replace('/', '.');
                    Class<?> type = ClassUtils.getDefaultClassLoader().loadClass(className);
                    result.add(type);
                }
            } catch (IOException e) {
                logger.error("Error loading classes from the base package " + packageName, e);
                fatal(e);
                return null;
            } catch (ClassNotFoundException e) {
                logger.error("Error loading classes from the base package " + packageName, e);
                fatal(e);
                return null;
            }
        }
        return result;
    }

    /**
     * Filter the list of classes extracting only the classes annotated with a
     * specific annotation.
     *
     * @param classes The full list of classes
     * @param annotationType The annotation
     * @return The filtered list
     */
    List<Class> getClassesByAnnotation(final List<Class> classes,
            final Class<? extends Annotation> annotationType) {
        List<Class> result = new ArrayList<Class>();
        AnnotationClassFilter filter = new AnnotationClassFilter(annotationType);
        for (Class type : classes) {
            if (filter.matches(type)) {
                result.add(type);
            }
        }
        return result;
    }

    /**
     * Filter the list of classes extracting only the DAO interfaces.
     *
     * @param classes The full list of classes
     * @return The filtered list
     */
    List<Class> getPersistentClasses(final List<Class> classes) {
        return getClassesByAnnotation(classes, Entity.class);
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
