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

import java.util.List;

import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDao;

/**
 * A DAO to be used for the tests M:N relationships.
 * 
 * @author <a href="mailto:michele.franzin@seesaw.it">Michele Franzin</a>
 * @version $Revision$
 */
@Dao(entity = BookTC.class)
public interface BookTCDao extends GenericDao<BookTC, Long> {
    List<BookTC> findByTitle(String value);
}
