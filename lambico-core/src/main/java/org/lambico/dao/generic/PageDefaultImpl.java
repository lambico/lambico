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
import java.util.Collections;

/**
 * The default implementation fo the {@link Page} interface.
 *
 * @param <T> The type of the entity instance in the page.
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class PageDefaultImpl<T> implements Page<T> {

    /**
     * The content of the page.
     */
    private List<T> results;
    /**
     * The size of the page, i.e. the max number of rows in a single page.
     */
    private int pageSize;
    /**
     * The index of the current page.
     */
    private int page;
    /**
     * The total rows count.
     */
    private int rowCount;
    /**
     * An empty page.
     */
    @SuppressWarnings("unchecked")
    public static final PageDefaultImpl EMPTY =
            new PageDefaultImpl(Collections.EMPTY_LIST, 1, 1, 0);
    
    /**
     * The constructor of a page.
     *
     * @param list The page content.
     * @param page The index of the page.
     * @param pageSize The size of the page.
     * @param rowCount The total rows count.
     */
    public PageDefaultImpl(final List<T> list, final int page,
            final int pageSize, final int rowCount) {
        this.page = page;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        results = list;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getPage() {
        return page;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isNextPage() {
        return page < getLastPage();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isPreviousPage() {
        return page > 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public List<T> getList() {
        return results;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getLastPage() {
        int lastPage = rowCount / pageSize;
        if (rowCount % pageSize > 0) {
            lastPage++;
        }
        return lastPage;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return rowCount;
    }
}
