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

import org.lambico.example.consolespringhibernate.dao.PersonDao;
import java.util.List;
import javax.annotation.Resource;
import org.lambico.example.consolespringhibernate.po.Person;
import org.lambico.example.consolespringhibernate.test.BaseTest;

/**
 *
 * @author lucio
 */
public class PersonDaoTest extends BaseTest {
    
    @Resource
    private PersonDao personDao;
    
    /**
     * Test of findByLastName method, of class PersonDao.
     */
    public void testFindByLastName() {
        List<Person> people = personDao.findByLastName("Benfante");
        assertEquals(8, people.size());
        for (Person p: people) {
            assertEquals("Benfante", p.getLastName());
        }
    }

    public void testFindByFirstName() {
        List<Person> people = personDao.findByFirstName("Ugo");
        assertEquals(2, people.size());
        for (Person p: people) {
            assertEquals("Ugo", p.getFirstName());
            System.out.println(p);
        }
    }
    
}
