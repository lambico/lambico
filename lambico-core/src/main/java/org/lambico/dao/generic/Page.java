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

import java.util.List;

/**
 * A page in a paginated result.
 *
 * @param <T> The type of the entity instance in the page.
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public interface Page<T> {
    /**
     * Returns the index of the last page.
     *
     * @return The index of the last page.
     */
    int getLastPage();

    /**
     * Return the content of the page.
     *
     * @return The content, i.e. the list of entity instances.
     */
    List<T> getList();

    /**
     * Returns the index of the current page.
     *
     * @return The index of the current page.
     */
    int getPage();

    /**
     * The total number of entity instances.
     *
     * @return The total number of entity instances.
     */
    int getRowCount();

    /**
     * Checks if there exists a following page.
     *
     * @return true if there is a page after the current page.
     */
    boolean isNextPage();

    /**
     * Checks if there exists a previous page.
     *
     * @return true if there is a page before the current page.
     */
    boolean isPreviousPage();

}
