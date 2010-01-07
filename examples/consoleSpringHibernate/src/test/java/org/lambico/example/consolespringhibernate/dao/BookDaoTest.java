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

package org.lambico.example.consolespringhibernate.dao;

import org.lambico.example.consolespringhibernate.dao.BookDao;
import java.util.List;

import javax.annotation.Resource;
import org.lambico.example.consolespringhibernate.po.Book;
import org.lambico.example.consolespringhibernate.po.Person;
import org.lambico.example.consolespringhibernate.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Enrico Giurin
 */
public class BookDaoTest extends BaseTest {
    
    @Resource
    private BookDao bookDao;
    
    
    public void testAllBooksByBorrower() {
    	List<Book> list = bookDao.allBooksByBorrower("Ugo", "Benfante");
    	assertEquals(2, list.size());      
    }
    
    public void testFindByAuthor() {
    	
        List<Book> books = bookDao.findByAuthor("Doug Lea");
        assertEquals(1, books.size());
        assertEquals(books.get(0).getTitle(), "Concurrent programming in java second edition");
        
    }

    
   
    
}
