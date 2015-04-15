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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

/**
 * @author michele franzin <michele at franzin.net>
 */
public class YamlFixtureHelperTest {

    private DemoBean[] expected;

    @Before
    public void setUp() throws Exception {
        expected = new DemoBean[5];
        expected[0] = new DemoBean("first one", (long) 356);
        expected[1] = new DemoBean("Demo 1", (long) 6789);
        expected[2] = new DemoBean("àèéìòù", (long) -980000);
        expected[3] = new DemoBean("Demo3", (long) 98000);
        expected[4] = new DemoBean("Demo#2", (long) -9800);
    }

    @Test
    public void shouldNotFailWrongFixtureDir() {
        Set<Class> models = getClassSet(DemoBean.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("nonExistant", models);
        assertTrue("Le fixture non sono vuote", objects.isEmpty());
    }

    @Test
    public void shouldNotFailIfMissingFixtureFile() {
        Set<Class> models = getClassSet(BigDecimal.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("fixtures", models);
        assertTrue("Le fixture non sono vuote", objects.isEmpty());
    }

    @Test
    public void shouldNotFailWhenTrailingSlashPresent() throws Exception {
        Set<Class> models = getClassSet(DemoBean.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("fixtures/", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
    }

    @Test
    public void shouldNotFailIfNoFile() {
        Set<Class> models = new LinkedHashSet<Class>();
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("emptyDir", models);
        assertTrue("Le fixture non sono vuote", objects.isEmpty());
    }

    @Test
    public void shouldNotFailIfEmptyFile() {
        Set<Class> models = getClassSet(DemoBean.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("fixtures/empty",
                models);
        assertEquals("Non carica tutti i beans", 1, objects.size());
        assertTrue("Le fixture non sono vuote", objects.get(DemoBean.class).isEmpty());
    }

    @Test
    public void beanLoading() {
        Set<Class> models = getClassSet(DemoBean.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("fixtures", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
        assertEquals("Non carica tutti i beans", 1, objects.size());
        assertTrue("Non crea istanze di " + DemoBean.class.getCanonicalName(), objects.containsKey(
                DemoBean.class));
        @SuppressWarnings("unchecked")
        List<DemoBean> result = objects.get(DemoBean.class);
        assertEquals("Non carica tutti i beans", 5, result.size());
        for (int i = 0; i < 5; i++) {
            assertEquals("Non ha caricato correttamente il bean numero " + i, expected[i], result.
                    get(i));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void bookLoading() {
        Set<Class> models = getClassSet(BookTC.class, AuthorTC.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("fixtures", models);
        assertNotNull("Non ha ritornato la mappa di fixtures", objects);
        assertEquals(2, objects.size());
        assertEquals(3, objects.get(BookTC.class).size());
        assertEquals(4, objects.get(AuthorTC.class).size());
        assertBookAuthorsAreIdentical(objects.get(BookTC.class), objects.get(AuthorTC.class));
    }

    @Test
    public void fixtureMerge() {
        Set<Class> models = getClassSet(BookTC.class, AuthorTC.class);
        Map<Class, List> objects = YamlFixtureHelper.loadFixturesFromResource("fixtures", models);
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

    @SuppressWarnings("unchecked")
    private LinkedHashSet<Class> getClassSet(final Class... models) {
        return new LinkedHashSet<Class>(CollectionUtils.arrayToList(models));
    }
}
