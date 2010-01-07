/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Example - Console Spring Hibernate.
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

// Copyright 2006-2007 The Parancoe Team
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.lambico.example.consolespringhibernate.app;

import java.util.List;

import org.lambico.example.consolespringhibernate.dao.BookDao;
import org.lambico.example.consolespringhibernate.po.Book;
import org.lambico.example.consolespringhibernate.util.ApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A simple example of an application using Parancoe persistence module for Book entity.
 * @author <a href="mailto:enricogiurin@gmail.com">Enrico Giurin</a>
 * @version $Revision$
 *
 */
public class UseBookDAO {

    @Autowired
    private BookDao bookDao;

    public UseBookDAO() {
        ApplicationContextHolder.autowireBeanProperties(this);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UseBookDAO app = new UseBookDAO();
        Book myBook = new Book("Parancoe Team", "Parancoe: the best framework");

        Book myBook2 = new Book("Mario Rossi", "My life");
        Book myBook3 = new Book("Parancoe Team", "Parancoe: tips & trick");
        Book myBook4 = new Book("The Hitchhiker's Guide to the Galaxy",
                "Douglas Adams", 320);

        app.bookDao.create(myBook);
        app.bookDao.create(myBook2);
        app.bookDao.create(myBook3);
        app.bookDao.create(myBook4);

        List<Book> myBooks = app.bookDao.findByAuthor("Parancoe Team");
        System.out.println(myBooks.size());
    }
}
