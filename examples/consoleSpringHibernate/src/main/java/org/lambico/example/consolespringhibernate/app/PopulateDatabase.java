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
package org.lambico.example.consolespringhibernate.app;

import org.lambico.example.consolespringhibernate.bo.PersonBO;
import org.lambico.example.consolespringhibernate.util.ApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A simple example of an application using Parancoe persistence module.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
public class PopulateDatabase {

    @Autowired
    private PersonBO personBO;

    public PopulateDatabase() {
        ApplicationContextHolder.autowireBeanProperties(this);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        PopulateDatabase app = new PopulateDatabase();
        // Popoulating the database
        app.personBO.populateArchive();
        // Print the person with id=1
        app.personBO.printPerson(1L);
    }
}
