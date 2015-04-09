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

import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.lambico.dao.generic.Dao;
import static org.lambico.test.ExtraAssert.assertSize;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Tests on the methods of the DaoBeanCreator class.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class ContextUtilsTest {

    private final ResourcePatternResolver resourcePatternResolver
            = new PathMatchingResourcePatternResolver();

    /**
     * Test of getAllClasses method, of class DaoBeanCreator.
     */
    @Test
    public void getAllClasses() {
        String packageName = "org.lambico.spring.xml.test";
        List<Class> result = ContextUtils.getAllClasses(resourcePatternResolver, null, packageName);
        assertSize(2, result);
    }

    /**
     * Test of isConcreteClass method, of class DaoBeanCreator.
     */
    @Test
    public void isConcreteClass() {
        assertTrue(ContextUtils.isConcreteClass(ContextUtilsTest.class));
    }

    /**
     * Test of isConcreteClass method, of class DaoBeanCreator.
     */
    @Test
    public void isConcreteClassWithInterface() {
        assertFalse(ContextUtils.isConcreteClass(Runnable.class));
    }

    /**
     * Test of isAbstract method, of class DaoBeanCreator.
     */
    @Test
    public void isAbstract() {
        assertTrue(ContextUtils.isAbstract(ClassLoader.class));
    }

    /**
     * Test of isAbstract method, of class DaoBeanCreator.
     */
    @Test
    public void isAbstractWithInterface() {
        assertTrue(ContextUtils.isAbstract(Runnable.class));
    }

    /**
     * Test of isAbstract method, of class DaoBeanCreator.
     */
    @Test
    public void isAbstractWithConcrete() {
        assertFalse(ContextUtils.isAbstract(ContextUtilsTest.class));
    }

    /**
     * Test of getClassesByAnnotation method, of class DaoBeanCreator.
     */
    @Test
    public void getClassesByAnnotation() {
        List<Class> classes = ContextUtils.getAllClasses(resourcePatternResolver, null,
                "org.lambico.spring.xml.test");
        List<Class> result = ContextUtils.getClassesByAnnotation(classes, Dao.class);
        assertSize(1, result);
    }
}
