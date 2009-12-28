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
 * <a href="http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm">http://chris-richardson.blog-city.com/simpler_xml_configuration_files_for_spring_dependency_inject.htm</a>
 */
public class DaoBeanCreator {
    
    private ResourcePatternResolver rl;
    
    private BeanDefinitionRegistry registry;
    
    private BeanDefinitionParserDelegate delegate;
    
    private final ReaderContext readerContext;
    
    public DaoBeanCreator(ResourcePatternResolver resourcePatternResolver,
            BeanDefinitionRegistry beanDefinitionRegistry,
            BeanDefinitionParserDelegate parserDelegate, ReaderContext readerContext) {
        this.readerContext = readerContext;
        this.rl = resourcePatternResolver;
        this.registry = beanDefinitionRegistry;
        this.delegate = parserDelegate;
    }
    
    void createBeans(Element element, String packageName, String genericDaoName) {
        List<Class> allClasses = getAllClasses(packageName);
        List<Class> persistentClasses = getPersistentClasses(allClasses);
        List<Class> daoInterfaces = getDaoInterfaces(allClasses);
        createBeanDefinitions(persistentClasses, daoInterfaces, genericDaoName);
    }    
    
    private void fatal(Throwable e) {
        readerContext.fatal(e.getMessage(), null, e);
    }
    
    /**
     * Return all classes in the package subtree.
     *
     * @param packageName The base package
     * @return The listo of all classes
     */
    List<Class> getAllClasses(String packageName) {
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
                        fileName.length() - ".class".length())
                        .replace('/', '.');
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
    
    boolean isConcreteClass(Class<?> type) {
        return !type.isInterface() && !isAbstract(type);
    }
    
    boolean isAbstract(Class<?> type) {
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
    List<Class> getClassesByAnnotation(List<Class> classes, Class<? extends Annotation> annotationType) {
        List<Class> result = new ArrayList<Class>();
        AnnotationClassFilter filter = new AnnotationClassFilter(annotationType);
        for (Class type : classes) {
            if (filter.matches(type))
                result.add(type);
        }
        return result;
    }
    
    /**
     * Filter the list of classes extracting only the DAO interfaces
     *
     * @param classes The full list of classes
     * @return The filtered list
     */
    List<Class> getPersistentClasses(List<Class> classes) {
        return getClassesByAnnotation(classes, Entity.class);
    }
    
    /**
     * Filter the list of classes extracting only the DAO interfaces
     *
     * @param classes The full list of classes
     * @return The filtered list
     */
    List<Class> getDaoInterfaces(List<Class> classes) {
        return getClassesByAnnotation(classes, Dao.class);
    }
    
    void createBeanDefinitions(List<Class> persistentClasses, List<Class> daoInterfaces, String genericDaoName) {
        for (Class pClass : persistentClasses) {
            Class daoInterface = findDaoInterface(pClass, daoInterfaces);
            createBeanDefinition(pClass, daoInterface, genericDaoName);
        }
    }
    
    void createBeanDefinition(Class persistentClass, Class daoInterface, String genericDaoName) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(ProxyFactoryBean.class);
        if (daoInterface != null) {
            beanDefinitionBuilder.addPropertyValue("proxyInterfaces", daoInterface);
        }
        BeanDefinitionBuilder genericDaoBDB = BeanDefinitionBuilder.childBeanDefinition(genericDaoName);
        genericDaoBDB.addPropertyValue("type", persistentClass);
        beanDefinitionBuilder.addPropertyValue("target", genericDaoBDB.getBeanDefinition());
        String id = StringUtils.uncapitalize(StringUtils.unqualify(persistentClass.getName()))+"Dao";
        registry.registerBeanDefinition(id, beanDefinitionBuilder.getBeanDefinition());
    }
    
    /**
     * Find the defined dao interface for the persistent class.
     */
    private Class findDaoInterface(Class persistentClass, List<Class> daoInterfaces) {
        Class result = null;
        for (Class dao : daoInterfaces) {
            Dao daoAnnotation = (Dao)dao.getAnnotation(Dao.class);
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
