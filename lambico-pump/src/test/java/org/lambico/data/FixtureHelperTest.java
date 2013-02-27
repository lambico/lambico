/**
 * Copyright (C) 2013 Lambico Team <michele@franzin.net>
 *
 * This file is part of Lambico Data Pump.
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
package org.lambico.data;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import org.springframework.util.CollectionUtils;

/**
 * @author michele franzin <michele at franzin.net>
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

    public void testShouldNotFailWrongFixtureDir() {
        Set<Class> models = new LinkedHashSet<Class>(CollectionUtils.arrayToList(new Class[]{
            DemoBean.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("nonExistant", models);
        assertTrue("Le fixture non sono vuote", objects.isEmpty());
    }

    public void testShouldNotFailIfMissingFixtureFile() {
        Set<Class> models = new LinkedHashSet<Class>(CollectionUtils.arrayToList(new Class[]{
            BigDecimal.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("fixtures", models);
        assertTrue("Le fixture non sono vuote", objects.isEmpty());
    }

    public void testShouldNotFailWhenTrailingSlashPresent() throws Exception {
        Set<Class> models = new LinkedHashSet<Class>(
                CollectionUtils.arrayToList(new Class[]{DemoBean.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("fixtures/", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
    }

    public void testShouldNotFailIfNoFile() {
        Set<Class> models = new LinkedHashSet<Class>();
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("emptyDir", models);
        assertTrue("Le fixture non sono vuote", objects.isEmpty());
    }

    public void testShouldNotFailIfEmptyFile() {
        Set<Class> models = new LinkedHashSet<Class>(CollectionUtils.arrayToList(new Class[]{
            DemoBean.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("fixtures/empty", models);
        assertEquals("Non carica tutti i beans", 1, objects.size());
        assertTrue("Le fixture non sono vuote", objects.get(DemoBean.class).isEmpty());
    }

    public void testBeanLoading() {
        Set<Class> models = new LinkedHashSet<Class>(CollectionUtils.arrayToList(new Class[]{
            DemoBean.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("fixtures", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
        assertEquals("Non carica tutti i beans", 1, objects.size());
        assertTrue("Non crea istanze di " + DemoBean.class.getCanonicalName(), objects.containsKey(
                DemoBean.class));
        List<DemoBean> result = (List<DemoBean>) objects.get(DemoBean.class);
        assertEquals("Non carica tutti i beans", 5, result.size());
        for (int i = 0; i < 5; i++) {
            assertEquals("Non ha caricato correttamente il bean numero " + i, expected[i], result.
                    get(i));
        }
    }

    public void testBookLoading() {
        Set<Class> models = new LinkedHashSet<Class>(CollectionUtils.arrayToList(new Class[]{
            BookTC.class, AuthorTC.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("fixtures", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
        assertEquals(2, objects.size());
        assertEquals(3, objects.get(BookTC.class).size());
        assertEquals(4, objects.get(AuthorTC.class).size());
        assertBookAuthorsAreIdentical(objects.get(BookTC.class), objects.get(AuthorTC.class));
    }

    public void testFixtureMerge() {
        Set<Class> models = new LinkedHashSet<Class>(CollectionUtils.arrayToList(new Class[]{
            BookTC.class, AuthorTC.class}));
        Map<Class, List> objects = FixtureHelper.loadFixturesFromResource("fixtures", models);
        AuthorTC author = (AuthorTC) objects.get(AuthorTC.class).get(3);
        assertEquals("john", author.getName());
        assertEquals("Java manual", author.getBooks().get(0).getTitle());
    }

    private void assertBookAuthorsAreIdentical(List<BookTC> books, List<AuthorTC> authors) {
        for (BookTC book : books) {
            for (AuthorTC author : book.getAuthors()) {
                assertTrue("The book author is not in the author array", authors.contains(author));
            }
        }
    }
}
