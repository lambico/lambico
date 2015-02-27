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

import org.lambico.spring.dao.hibernate.dao.AuthorTCDao;
import org.lambico.spring.dao.hibernate.dao.BookTCDao;
import org.lambico.spring.dao.hibernate.po.AuthorTC;
import org.lambico.spring.dao.hibernate.po.BookTC;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import static org.lambico.test.ExtraAssert.*;

/**
 * Tests fixture load with M:N relationships
 *
 * @author michele franzin <michele at franzin.net>
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 */
public class ManyToManyFixturesTest extends BaseTest {

    @Resource
    protected AuthorTCDao authorTCDao = null;

    @Resource
    protected BookTCDao bookTCDao = null;

    @Test
    public void testDaoExists() {
        assertNotNull("Author dao variable not setted", authorTCDao);
        assertNotNull("Book dao variable not setted", bookTCDao);
    }

    @Test
    public void testAllSize() {
        assertSize(4, authorTCDao.findAll());
        assertSize(3, bookTCDao.findAll());
    }

    @Test
    public void testFixturesLoad() {
        List<AuthorTC> authors = authorTCDao.findByName("joe");
        assertSize(1, authors);
        assertSize(2, authors.get(0).getBooks());
        List<BookTC> books = bookTCDao.findByTitle("Java manual");
        assertSize(1, books);
        assertSize(3, books.get(0).getAuthors());
        books = bookTCDao.findByTitle("Mr. Bean");
        assertSize(1, books);
        assertSize(2, books.get(0).getAuthors());
    }

    @Test
    @Ignore
    public void testRelationSanity() {
        BookTC book1 = new BookTC();
        book1.setTitle("title1");
        bookTCDao.store(book1);
        BookTC book2 = new BookTC();
        book2.setTitle("title2");
        bookTCDao.store(book2);

        AuthorTC author1 = new AuthorTC();
        author1.setName("name1");
        List<BookTC> bookList = new ArrayList<BookTC>();
        bookList.add(book1);
        bookList.add(book2);
        author1.setBooks(bookList);
        authorTCDao.store(author1);

        AuthorTC author2 = new AuthorTC();
        author2.setName("name2");
        bookList.clear();
        bookList.add(book2);
        author2.setBooks(bookList);
        authorTCDao.store(author2);

        List<AuthorTC> authors = authorTCDao.findByName("name1");
        assertSize(1, authors);
        assertSize(2, authors.get(0).getBooks());
        List<BookTC> books = bookTCDao.findByTitle("title2");
        assertSize(1, books);
        // FIXME assertSize(2, books.get(0).getAuthors());

        authorTCDao.deleteAll();
        bookTCDao.deleteAll();
    }
}
