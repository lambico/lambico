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
package org.lambico.example.consolespringhibernate.po;

import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.lambico.po.hibernate.EntityBase;

/**
 * Represents a book entity.
 * @author <a href="mailto:enricogiurin@gmail.com">Enrico Giurin</a>
 *
 */
@javax.persistence.Entity()
@NamedQueries(value = {
    @NamedQuery(name = "Book.allBooksByBorrower", query =
    "from Book b where upper(b.borrower.firstName) = upper(?) and upper(b.borrower.lastName) = upper(?)")})
public class Book extends EntityBase {

    private int numPages = 0;
    private String author = null;
    private String title = null;
    private Person borrower = null;

    /**
     *
     */
    public Book() {
        // TODO Auto-generated constructor stub
    }

    public Book(String author, String title) {
        this(author, title, 0);
    }

    public Book(String author, String title, int numPages) {
        this.author = author;
        this.title = title;
        this.numPages = numPages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne
    public Person getBorrower() {
        return borrower;
    }

    public void setBorrower(Person borrower) {
        this.borrower = borrower;
    }
}
