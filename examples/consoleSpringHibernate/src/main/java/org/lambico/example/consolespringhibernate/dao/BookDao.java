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
package org.lambico.example.consolespringhibernate.dao;

import java.util.List;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDao;
import org.lambico.example.consolespringhibernate.po.Book;

/**
 * Represents Book DAO.
 *
 * @author <a href="mailto:enricogiurin@gmail.com">Enrico Giurin</a>
 *
 */
@Dao(entity = Book.class)
public interface BookDao extends GenericDao<Book, Long> {

    List<Book> findByAuthor(String author);

    List<Book> findByAuthorAndTitle(String author, String title);

    List<Book> findByTitle(String title);

    /**
     * Retrieves all the books lent to the person identified by
     * borrowerFirstName and borrowerLastName.
     * @param borrowerFirstName The borrower's first name.
     * @param borrowerLastName The borrower's last name.
     * @return The list of all book by borrower.
     */
    List<Book> allBooksByBorrower(String borrowerFirstName, String borrowerLastName);
}
