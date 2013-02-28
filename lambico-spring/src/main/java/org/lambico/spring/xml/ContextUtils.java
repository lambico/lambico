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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Utilities for managing beans and classes in the Spring context.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public final class ContextUtils {

    private static Logger logger = LoggerFactory.getLogger(ContextUtils.class);

    /**
     * The constructor.
     */
    private ContextUtils() {
    }

    /**
     * Return all classes in the package subtree.
     *
     * @param resourcePatternResolver The resource pattern resolver.
     * @param packageName The base package.
     * @param readerContext The reader context.
     *                      It can be null, if you don't need to signal failures to the context.
     * @return The listo of all classes.
     */
    public static List<Class> getAllClasses(final ResourcePatternResolver resourcePatternResolver,
            final ReaderContext readerContext, final String packageName) {
        List<Class> result = new ArrayList<Class>();
        if (StringUtils.hasText(packageName)) {
            try {
                String packagePart = packageName.replace('.', '/');
                String classPattern = "classpath*:/" + packagePart + "/**/*.class";
                Resource[] resources = resourcePatternResolver.getResources(classPattern);
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
                fatal(readerContext, e);
                return null;
            } catch (ClassNotFoundException e) {
                logger.error("Error loading classes from the base package " + packageName, e);
                fatal(readerContext, e);
                return null;
            }
        }
        return result;
    }

    /**
     * Send a fatal message to the context.
     *
     * @param e The cause.
     * @param readerContext The reader context.
     */
    private static void fatal(final ReaderContext readerContext, final Throwable e) {
        if (readerContext != null) {
            readerContext.fatal(e.getMessage(), null, e);
        }
    }

    /**
     * Checks if it's a concrete class.
     *
     * @param type The class to check.
     * @return true if it's a concrete class.
     */
    public static boolean isConcreteClass(final Class<?> type) {
        return !type.isInterface() && !isAbstract(type);
    }

    /**
     * Checks if it's an abstract class.
     *
     * @param type The class to check.
     * @return true if the class is abstract.
     */
    public static boolean isAbstract(final Class<?> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    /**
     * Filter the list of classes extracting only the classes annotated with a
     * specific annotation.
     *
     * @param classes The full list of classes
     * @param annotationType The annotation
     * @return The filtered list
     */
    public static List<Class> getClassesByAnnotation(final List<Class> classes,
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

}
