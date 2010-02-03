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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.lambico.dao.generic.Dao;
import org.lambico.spring.dao.hibernate.BaseTest;
import org.lambico.spring.dao.hibernate.dao.AuthorTCDao;
import org.lambico.spring.dao.hibernate.po.AuthorTC;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Tests on the methods of the DaoBeanCreator class.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class DaoBeanCreatorTest extends TestCase {

    public DaoBeanCreatorTest(String testName) {
        super(testName);
    }

    private DaoBeanCreator createInstance() {
        return new DaoBeanCreator(new PathMatchingResourcePatternResolver(), null, null, null);
    }

    /**
     * Test of getAllClasses method, of class DaoBeanCreator.
     */
    public void testGetAllClasses() {
        String packageName = "org.lambico.spring.dao.hibernate.dao";
        DaoBeanCreator instance = createInstance();
        List<Class> result = instance.getAllClasses(packageName);
        assertEquals(5, result.size());
    }

    /**
     * Test of isConcreteClass method, of class DaoBeanCreator.
     */
    public void testIsConcreteClass() {
        DaoBeanCreator instance = createInstance();
        assertTrue(instance.isConcreteClass(AuthorTC.class));
    }

    /**
     * Test of isConcreteClass method, of class DaoBeanCreator.
     */
    public void testIsConcreteClassWithInterface() {
        DaoBeanCreator instance = createInstance();
        assertFalse(instance.isConcreteClass(AuthorTCDao.class));
    }

    /**
     * Test of isAbstract method, of class DaoBeanCreator.
     */
    public void testIsAbstract() {
        DaoBeanCreator instance = createInstance();
        assertTrue(instance.isAbstract(BaseTest.class));
    }

    /**
     * Test of isAbstract method, of class DaoBeanCreator.
     */
    public void testIsAbstractWithInterface() {
        DaoBeanCreator instance = createInstance();
        assertTrue(instance.isAbstract(AuthorTCDao.class));
    }

    /**
     * Test of isAbstract method, of class DaoBeanCreator.
     */
    public void testIsAbstractWithConcrete() {
        DaoBeanCreator instance = createInstance();
        assertFalse(instance.isAbstract(AuthorTC.class));
    }

    /**
     * Test of getClassesByAnnotation method, of class DaoBeanCreator.
     */
    public void testGetClassesByAnnotation() {
        DaoBeanCreator instance = createInstance();
        List<Class> classes = instance.getAllClasses("org.lambico.spring.dao.hibernate");
        Class<? extends Annotation> annotationType = Dao.class;
        List result = instance.getClassesByAnnotation(classes, annotationType);
        assertEquals(5, result.size());
    }

    /**
     * Test of getDaoInterfaces method, of class DaoBeanCreator.
     */
    public void testGetDaoInterfaces() {
        DaoBeanCreator instance = createInstance();
        String interfacePackageName = "org.lambico.spring.dao.hibernate";
        Set result = instance.getDaoInterfaces(interfacePackageName);
        assertEquals(5, result.size());
    }

}
