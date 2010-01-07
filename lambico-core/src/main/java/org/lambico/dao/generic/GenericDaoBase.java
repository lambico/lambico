/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Core.
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
package org.lambico.dao.generic;

import java.io.Serializable;
import java.util.List;

/**
 * Common methods for Dao.
 *
 * @param <T> The entity class type of the DAO.
 * @param <PK> The type of the primary key of the entity.
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 */
public interface GenericDaoBase<T, PK extends Serializable> {

    /**
     * Read an entity instance with a specified primary key value.
     *
     * @param id The value of the primary key.
     * @return The entity.
     */
    T read(PK id);

    /**
     * Read an entity instance with a specified primary key value.
     *
     * @param id The value of the primary key.
     * @return The entity.
     */
    T get(PK id);

    /**
     * Create or update an object.
     *
     * @param transientObject The object to persist.
     */
    void create(T transientObject);

    /**
     * Create or update an object.
     *
     * @param transientObject The object to persist.
     */
    void store(T transientObject);

    /**
     * Remove an object from persistent storage in the database.
     *
     * @param  persistentObject The persistent object to delete.
     */
    void delete(T persistentObject);

    /**
     * Retrieve all the instances of the DAO entity.
     *
     * @return All the instances of the DAO entity.
     */
    List<T> findAll();

    /**
     * Delete all the instances of the DAO entity.
     *
     * @return The count of the deleted instances.
     */
    int deleteAll();

    /**
     * Count all the instances of the DAO entity.
     *
     * @return The count of all the instances of the DAO entity.
     */
    long count();

    /**
     * Rollback the current transaction.
     */
    void rollBackTransaction();

}
