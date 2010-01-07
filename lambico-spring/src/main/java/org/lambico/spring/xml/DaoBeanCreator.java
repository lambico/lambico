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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import org.lambico.dao.generic.Dao;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.io.Resource;
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
public class DaoBeanCreator {

    /** The ResourcePatternResolver. */
    private ResourcePatternResolver rl;
    /** The BeanDefinitionRegistry. */
    private BeanDefinitionRegistry registry;
    /** The BeanDefinitionParserDelegate. */
    private BeanDefinitionParserDelegate delegate;
    /** The ReaderContext. */
    private final ReaderContext readerContext;

    /**
     * Creates the DaoBeanCreator.
     *
     * @param resourcePatternResolver The ResourcePatternResolver.
     * @param beanDefinitionRegistry The BeanDefinitionRegistry.
     * @param parserDelegate The BeanDefinitionParserDelegate.
     * @param readerContext The ReaderContext.
     */
    public DaoBeanCreator(final ResourcePatternResolver resourcePatternResolver,
            final BeanDefinitionRegistry beanDefinitionRegistry,
            final BeanDefinitionParserDelegate parserDelegate,
            final ReaderContext readerContext) {
        this.readerContext = readerContext;
        this.rl = resourcePatternResolver;
        this.registry = beanDefinitionRegistry;
        this.delegate = parserDelegate;
    }

    /**
     * Create the DAO beans.
     *
     * @param element The DOM of the DAO definition XML fragment.
     * @param packageName The DAO package name.
     * @param genericDaoName The DAO name.
     * @throws ClassNotFoundException If the class of the base abstract generic DAO can't be found.
     */
    void createBeans(final Element element, final String packageName,
            final String genericDaoName) throws ClassNotFoundException {
        List<Class> allClasses = getAllClasses(packageName);
        List<Class> persistentClasses = getPersistentClasses(allClasses);
        List<Class> daoInterfaces = getDaoInterfaces(allClasses);
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
        if (daoInterface != null) {
            Class<?>[] newGenericDaoInterfaces = new Class<?>[genericDaoInterfaces.length + 1];
            System.arraycopy(genericDaoInterfaces, 0,
                    newGenericDaoInterfaces, 0, genericDaoInterfaces.length);
            newGenericDaoInterfaces[genericDaoInterfaces.length] = daoInterface;
            genericDaoInterfaces = newGenericDaoInterfaces;
        }
        return genericDaoInterfaces;
    }

    /**
     * Send a fatal message to the context.
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
                Class<?> type = Class.forName(className);
                result.add(type);
            }
        } catch (IOException e) {
            fatal(e);
            return null;
        } catch (ClassNotFoundException e) {
            fatal(e);
            return null;
        }
        return result;
    }

    /**
     * Checks if it's a concrete class.
     *
     * @param type The class to check.
     * @return true if it's a concrete class.
     */
    boolean isConcreteClass(final Class<?> type) {
        return !type.isInterface() && !isAbstract(type);
    }

    /**
     * Checks if it's an abstract class.
     *
     * @param type The class to check.
     * @return true if the class is abstract.
     */
    boolean isAbstract(final Class<?> type) {
        return (type.getModifiers() ^ Modifier.ABSTRACT) == 0;
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
     * Filter the list of classes extracting only the DAO interfaces.
     *
     * @param classes The full list of classes
     * @return The filtered list
     */
    List<Class> getDaoInterfaces(final List<Class> classes) {
        return getClassesByAnnotation(classes, Dao.class);
    }

    /**
     * Create the DAO bean definitions for a set of persistent classes.
     *
     * @param persistentClasses The types of the DAOs.
     * @param daoInterfaces The DAO interfaces.
     * @param genericDaoName The parent DAO name.
     * @throws ClassNotFoundException If the class of the base abstract generic DAO can't be found.
     */
    void createBeanDefinitions(final List<Class> persistentClasses,
            final List<Class> daoInterfaces,
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
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.rootBeanDefinition(ProxyFactoryBean.class);
        BeanDefinitionBuilder genericDaoBDB =
                BeanDefinitionBuilder.childBeanDefinition(genericDaoName);
        Class<?>[] genericDaoInterfaces = extractDaoInterfaces(genericDaoName, daoInterface);
        beanDefinitionBuilder.addPropertyValue("proxyInterfaces", genericDaoInterfaces);
        genericDaoBDB.addPropertyValue("type", persistentClass);
        beanDefinitionBuilder.addPropertyValue("target", genericDaoBDB.getBeanDefinition());
        String id =
                StringUtils.uncapitalize(StringUtils.unqualify(persistentClass.getName())) + "Dao";
        registry.registerBeanDefinition(id, beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * Find the defined dao interface for the persistent class.
     *
     * @param persistentClass The persistent class.
     * @param daoInterfaces The defined DAO interfaces.
     * @return The DAO interface for the specified persistent class.
     */
    private Class findDaoInterface(final Class persistentClass, final List<Class> daoInterfaces) {
        Class result = null;
        for (Class dao : daoInterfaces) {
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
}