/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico test.
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

package org.lambico.test.spring.hibernate;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.springframework.util.CollectionUtils;

/**
 * @author <a href="mailto:michele.franzin@seesaw.it">Michele Franzin</a>
 * @author Lucio Benfante <lucio dot benfante at gmail.com>
 * @version $Revision$
 */
public class FixtureHelperTest extends TestCase {

    private DemoBean[] expected;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        expected = new DemoBean[5];
        expected[0] = new DemoBean("first one", new Long(356));
        expected[1] = new DemoBean("Demo 1", new Long(6789));
        expected[2] = new DemoBean("àèéìòù", new Long(-980000));
        expected[3] = new DemoBean("Demo3", new Long(98000));
        expected[4] = new DemoBean("Demo#2", new Long(-9800));
    }

    @SuppressWarnings("unchecked")
    public void testShouldNotFailWrongFixtureDir() throws Exception {
        Set<Class> models = new LinkedHashSet<Class>(
                CollectionUtils.arrayToList(new Class[]{DemoBean.class}));
        Map<Class, Object[]> result = FixtureHelper.loadFixturesFromResource(
                "nonExistant/", models);
        assertTrue("Le fixture non sono vuote", result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testShouldNotFailIfMissingFixtureFile() throws Exception {
        Set<Class> models = new LinkedHashSet<Class>(
                CollectionUtils.arrayToList(new Class[]{BigDecimal.class}));
        Map<Class, Object[]> result = FixtureHelper.loadFixturesFromResource(
                "fixtures/", models);
        assertTrue("Le fixture non sono vuote", result.isEmpty());
    }

    public void testShouldNotFailIfEmptyDocs() throws Exception {
        Map<Class, Object[]> result = FixtureHelper.loadFixturesFromResource(
                "emptyDir/", new LinkedHashSet<Class>());
        assertTrue("Le fixture non sono vuote", result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testBeanLoading() throws Exception {
        Set<Class> models = new LinkedHashSet<Class>(
                CollectionUtils.arrayToList(new Class[]{DemoBean.class}));
        Map<Class, Object[]> objects = FixtureHelper.loadFixturesFromResource(
                "fixtures/", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
        assertEquals("Non carica tutti i beans", 1, objects.size());
        assertTrue("Non crea istanze di " + DemoBean.class.getCanonicalName(),
                objects.containsKey(DemoBean.class));
        DemoBean[] result = (DemoBean[]) objects.get(DemoBean.class);
        assertEquals("Non carica tutti i beans", 5, result.length);
        for (int i = 0; i < 5; i++) {
            assertEquals("Non ha caricato correttamente il beans numero " + i,
                    expected[i], result[i]);
        }
    }

    public void testBookLoading() {
        Set<Class> models = new LinkedHashSet<Class>(
                CollectionUtils.arrayToList(new Class[]{AuthorTC.class,
                    BookTC.class
                }));
        Map<Class, Object[]> objects = FixtureHelper.loadFixturesFromResource(
                "fixtures/", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
        assertEquals(2, objects.size());
        assertEquals(3, objects.get(BookTC.class).length);
        assertEquals(4, objects.get(AuthorTC.class).length);
        assertBookAuthorsAreIdentical(objects.get(BookTC.class), objects.get(AuthorTC.class));
    }
    
    private void assertBookAuthorsAreIdentical(Object[] books, Object[] authors) {        
        for (int i=0; i < books.length; i++) {
            BookTC currentBook = (BookTC) books[i];
            for (AuthorTC author: currentBook.getAuthors()) {
                boolean found = false;
                for (int j=0; j < authors.length; j++) {
                    if (author == authors[j]) {
                        found = true;
                        break;
                    }
                }
                assertTrue("The book author is not in the author array", found);
            }
        }
    }
}
