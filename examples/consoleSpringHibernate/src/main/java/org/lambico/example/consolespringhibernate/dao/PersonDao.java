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

import java.util.Date;
import java.util.List;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDao;
import org.lambico.example.consolespringhibernate.po.Person;

/**
 * The DAO interface for the Person entity.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
@Dao(entity = Person.class)
public interface PersonDao extends GenericDao<Person, Long> {

    List<Person> findByLastName(String lastName);

    List<Person> findByFirstNameAndLastName(String firstName, String lastName);

    List<Person> findByBirthDate(Date birthDate);

    List<Person> findByFirstName(String firstName);
}
