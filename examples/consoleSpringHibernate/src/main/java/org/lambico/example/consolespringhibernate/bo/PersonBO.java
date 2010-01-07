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
package org.lambico.example.consolespringhibernate.bo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.lambico.example.consolespringhibernate.dao.PersonDao;
import org.lambico.example.consolespringhibernate.po.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A BO to be used for the tests of the generic DAO.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
@Component
public class PersonBO {

    @Autowired
    private PersonDao personDao;

    @Transactional()
    public void populateArchive() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Person> searches = null;
        searches = personDao.findByFirstNameAndLastName("Mario", "Rossi");
        if (searches.isEmpty()) {
            Person p = new Person();
            p.setFirstName("Mario");
            p.setLastName("Rossi");
            p.setBirthDate(sdf.parse("25/04/1970"));
            personDao.create(p);
        }
        searches = personDao.findByFirstNameAndLastName("Francesca", "Verdi");
        if (searches.isEmpty()) {
            Person p = new Person();
            p.setFirstName("Francesca");
            p.setLastName("Verdi");
            p.setBirthDate(sdf.parse("30/08/1990"));
            personDao.create(p);
        }
        searches = personDao.findByFirstNameAndLastName("Giovanni", "Bianchi");
        if (searches.isEmpty()) {
            Person p = new Person();
            p.setFirstName("Giovanni");
            p.setLastName("Bianchi");
            p.setBirthDate(sdf.parse("15/03/1980"));
            personDao.create(p);
        }
    }

    @Transactional(readOnly = true)
    public void printPerson(Long id) {
        Person p = personDao.read(id);
        if (p != null) {
            System.out.println(p.getFirstName() + " " + p.getLastName() + " " +
                    p.getBirthDate());
        } else {
            System.out.println("Person ("+id+") not found");
        }
    }
}
