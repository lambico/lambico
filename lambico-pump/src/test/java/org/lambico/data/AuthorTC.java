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

import org.lambico.po.hibernate.EntityBase;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

/**
 * An persistent object to be used for the tests M:N relationships.
 *
 * @author michele franzin <michele at franzin.net>
 */
@javax.persistence.Entity
@NamedQuery(name = "AuthorTC.findByName", query = "from AuthorTC where name like ?")
public class AuthorTC extends EntityBase {

    private static final long serialVersionUID = 832363948575562242L;

    private String name = null;

    private List<BookTC> books = new ArrayList<BookTC>();

    public AuthorTC() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(targetEntity = BookTC.class, cascade = { CascadeType.ALL },  fetch = FetchType.EAGER)
    @JoinTable(name = "AUTHOR_BOOK", joinColumns = { @JoinColumn(name = "author_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
    public List<BookTC> getBooks() {
        return books;
    }

    public void setBooks(List<BookTC> books) {
        this.books = books;
    }
}
