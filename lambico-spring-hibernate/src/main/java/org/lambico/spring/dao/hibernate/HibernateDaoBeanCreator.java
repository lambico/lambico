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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import org.lambico.dao.AutomaticDao;
import org.lambico.dao.generic.Dao;
import org.lambico.spring.xml.ContextUtils;
import org.lambico.spring.xml.DaoBeanCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * A creator for the DAO beans. It automagically builds DAO beans from
 * persistent classes.
 *
 * Based on an idea (and code) of Chris Richardson:
 *
 * CHECKSTYLE:OFF
 * <a href="http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm">http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm</a>
 * CHECKSTYLE:ON
 */
public class HibernateDaoBeanCreator implements DaoBeanCreator {

    private static final Logger logger = LoggerFactory.getLogger(HibernateDaoBeanCreator.class);
    /** The ResourcePatternResolver. */
    private final ResourcePatternResolver rl;
    /** The BeanDefinitionRegistry. */
    private final BeanDefinitionRegistry registry;
    /** The BeanDefinitionParserDelegate. */
    private final BeanDefinitionParserDelegate delegate;
    /** The ReaderContext. */
    private final ReaderContext readerContext;

    /**
     * Creates the DaoBeanCreator.
     *
     * @param parserContext The ParserContext.
     */
    public HibernateDaoBeanCreator(final ParserContext parserContext) {
        this.readerContext = parserContext.getReaderContext();
        this.rl = (ResourcePatternResolver) parserContext.getReaderContext().getReader().
                getResourceLoader();
        this.registry = parserContext.getReaderContext().getRegistry();
        this.delegate = parserContext.getDelegate();
    }

    /**
     * Creates the DaoBeanCreator.
     *
     * @param rl The resource pattern resolver.
     * @param registry The registry.
     * @param delegate The delegate.
     * @param readerContext The reader context.
     */
    public HibernateDaoBeanCreator(final ResourcePatternResolver rl,
            final BeanDefinitionRegistry registry, final BeanDefinitionParserDelegate delegate,
            final ReaderContext readerContext) {
        this.rl = rl;
        this.registry = registry;
        this.delegate = delegate;
        this.readerContext = readerContext;
    }

    /**
     * Create the DAO beans.
     *
     * @param element The DOM of the DAO definition XML fragment.
     * @param interfacePackageName The base package of the DAO interfaces.
     * @param entityPackageName  The base package of the persistent entities.
     * @param genericDaoName The DAO name.
     * @param sessionFactoryName The session factory bean name.
     * @throws ClassNotFoundException If the class of the base abstract generic DAO can't be found.
     */
    @Override
    public void createBeans(final Element element, final String interfacePackageName,
            final String entityPackageName,
            final String genericDaoName, final String sessionFactoryName)
            throws ClassNotFoundException {
        Set<Class> daoInterfaces = getDaoInterfaces(interfacePackageName);
        Set<Class> persistentClasses = getPersistentClasses(entityPackageName, sessionFactoryName,
                daoInterfaces);
        createBeanDefinitions(persistentClasses, daoInterfaces, genericDaoName);
    }

    /**
     * Build the array of interfaces that will be implemented and/or satisfied by the
     * specific DAO. They are the specific DAO interface and all the interfaces implemented
     * by the base abstra generic DAO.
     *
     * @param genericDaoName The name of the bean defining the base abstract generic DAO.
     * @param daoInterface The interface of the constructing DAO.
     * @return The array of interfaces.
     * @throws ClassNotFoundException If the class of the base abstract generic DAO can't be found.
     * @throws NoSuchBeanDefinitionException
     */
    private Class<?>[] extractDaoInterfaces(final String genericDaoName, final Class daoInterface)
            throws ClassNotFoundException {
        BeanDefinition baseDaoBD = registry.getBeanDefinition(genericDaoName);
        Class<?>[] genericDaoInterfaces =
                Class.forName(baseDaoBD.getBeanClassName()).getInterfaces();
        List<Class<?>> newGenericDaoInterfaces =
                new ArrayList<Class<?>>(genericDaoInterfaces.length + 2);
        newGenericDaoInterfaces.addAll(Arrays.asList(genericDaoInterfaces));
        if (daoInterface != null) {
            newGenericDaoInterfaces.add(daoInterface);
        }
        newGenericDaoInterfaces.add(AutomaticDao.class);
        return newGenericDaoInterfaces.toArray(new Class<?>[newGenericDaoInterfaces.size()]);
    }

    /**
     * Retrieve the list of persistent classes.
     *
     * It search classes in the provided package, in the DAO interface definitions,
     * and in other possibly places in the Spring context (i.e. in the Hibernate session factory).
     *
     * @param entityPackageName The base package of the entity classes.
     * @param sessionFactoryName The session factory bean name.
     * @param daoInterfaces The set of DAO interfaces.
     * @return The set of persistent classes.
     */
    Set<Class> getPersistentClasses(final String entityPackageName,
            final String sessionFactoryName, final Set<Class> daoInterfaces) {
        final List<Class> classes = ContextUtils.getAllClasses(rl, readerContext,
                entityPackageName);
        final List<Class> annotatedClasses = ContextUtils.getClassesByAnnotation(classes,
                Entity.class);
        List<Class> classesFromHibernateSessionFactory =
                searchInTheHibernateSessionFactory(sessionFactoryName);
        List<Class> classesFromTheDaoDefinitions = searchInTheDaoDefinitions(daoInterfaces);
        Set<Class> result = new HashSet<Class>();
        result.addAll(annotatedClasses);
        result.addAll(classesFromHibernateSessionFactory);
        result.addAll(classesFromTheDaoDefinitions);
        return result;
    }

    /**
     * Search entity classes, looking in the Hibernate session factory configuration.
     *
     * @param sessionFactoryName The session factory bean name.
     * @return The list of persistent classes configurated for the session factory.
     */
    private List<Class> searchInTheHibernateSessionFactory(final String sessionFactoryName) {
        final List<Class> result = new LinkedList<Class>();
        if (registry.containsBeanDefinition(sessionFactoryName)) {
            BeanDefinition sessionFactoryBeanDefinition =
                    registry.getBeanDefinition(sessionFactoryName);
            PropertyValue annotatedClassesProperty =
                    sessionFactoryBeanDefinition.getPropertyValues().
                    getPropertyValue("annotatedClasses");
            if (annotatedClassesProperty != null) {
                @SuppressWarnings("unchecked")
                List<TypedStringValue> entitiesFromSession =
                        (List<TypedStringValue>) annotatedClassesProperty.getValue();
                for (TypedStringValue className : entitiesFromSession) {
                    try {
                        final Class pClass = Class.forName(className.getValue());
                        result.add(pClass);
                    } catch (ClassNotFoundException ex) {
                        // ignore it
                        logger.debug("Entity class not found.", ex);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Retrieves the set of DAO interfaces.
     *
     * @param interfacePackageName The base package of the DAO interfaces.
     * @return The filtered list
     */
    Set<Class> getDaoInterfaces(final String interfacePackageName) {
        final List<Class> allClasses = ContextUtils.getAllClasses(rl, readerContext,
                interfacePackageName);
        final List<Class> daoClasses = ContextUtils.getClassesByAnnotation(allClasses, Dao.class);
        return new HashSet<Class>(daoClasses);
    }

    /**
     * Create the DAO bean definitions for a set of persistent classes.
     *
     * @param persistentClasses The types of the DAOs.
     * @param daoInterfaces The DAO interfaces.
     * @param genericDaoName The parent DAO name.
     * @throws ClassNotFoundException If the class of the base abstract generic DAO can't be found.
     */
    void createBeanDefinitions(final Set<Class> persistentClasses,
            final Set<Class> daoInterfaces,
            final String genericDaoName) throws ClassNotFoundException {
        for (Class pClass : persistentClasses) {
            Class daoInterface = findDaoInterface(pClass, daoInterfaces);
            createBeanDefinition(pClass, daoInterface, genericDaoName);
        }
    }

    /**
     * Create the DAO bean definition for a persistent class.
     *
     * @param persistentClass The type of the DAO.
     * @param daoInterface The DAO interface.
     * @param genericDaoName The parent DAO name.
     * @throws ClassNotFoundException If the class of the base abstract generic DAO can't be found.
     */
    void createBeanDefinition(final Class persistentClass,
            final Class daoInterface,
            final String genericDaoName) throws ClassNotFoundException {
        String id =
                StringUtils.uncapitalize(StringUtils.unqualify(persistentClass.getName())) + "Dao";
        if (daoInterface == null && registry.containsBeanDefinition(id)) {
            // don't register a generic DAO (without daoInterface) if it already exists.
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.rootBeanDefinition(ProxyFactoryBean.class);
        BeanDefinitionBuilder genericDaoBDB =
                BeanDefinitionBuilder.childBeanDefinition(genericDaoName);
        Class<?>[] genericDaoInterfaces = extractDaoInterfaces(genericDaoName, daoInterface);
        beanDefinitionBuilder.addPropertyValue("proxyInterfaces", genericDaoInterfaces);
        genericDaoBDB.addPropertyValue("type", persistentClass);
        beanDefinitionBuilder.addPropertyValue("target", genericDaoBDB.getBeanDefinition());
        registry.registerBeanDefinition(id, beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * Find the defined dao interface for the persistent class.
     *
     * @param persistentClass The persistent class.
     * @param daoInterfaces The defined DAO interfaces.
     * @return The DAO interface for the specified persistent class.
     */
    private Class findDaoInterface(final Class persistentClass, final Set<Class> daoInterfaces) {
        Class result = null;
        for (Class dao : daoInterfaces) {
            @SuppressWarnings("unchecked")
            Dao daoAnnotation = (Dao) dao.getAnnotation(Dao.class);
            if (daoAnnotation != null) {
                if (daoAnnotation.entity().getName().equals(persistentClass.getName())) {
                    result = dao;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Search the persistence classes in the DAO definitions.
     *
     * @param daoInterfaces The set of DAO interfaces.
     * @return The set of persistence classes.
     */
    private List<Class> searchInTheDaoDefinitions(final Set<Class> daoInterfaces) {
        final List<Class> result = new LinkedList<Class>();
        for (Class daoInterface : daoInterfaces) {
            @SuppressWarnings("unchecked")
            Dao annotation = (Dao) daoInterface.getAnnotation(Dao.class);
            Class entity = annotation.entity();
            if (entity != null) {
                result.add(entity);
            }
        }
        return result;
    }
}
