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
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 */
public class PageDefaultImpl<T> implements Page<T> {

    private List results;
    private int pageSize;
    private int page;
    private int rowCount;

    public static PageDefaultImpl EMPTY = new PageDefaultImpl(Collections.EMPTY_LIST, 1, 1, 0);

    public PageDefaultImpl(List<T> list, int page, int pageSize, int rowCount) {
        this.page = page;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        results = list;
    }
    
    public int getPage() {
        return page;
    }

    public boolean isNextPage() {
        return page < getLastPage();
    }

    public boolean isPreviousPage() {
        return page > 1;
    }

    public List<T> getList() {
        return results;
    }
    
    public int getLastPage() {
        int lastPage = rowCount/pageSize;
        if (rowCount%pageSize > 0) lastPage++;
        return lastPage;
    }

    public int getRowCount() {
        return rowCount;
    }

}
