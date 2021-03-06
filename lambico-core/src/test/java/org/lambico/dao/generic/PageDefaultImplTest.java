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

import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lambico.po.hibernate.EntityBase;
import static org.junit.Assert.*;

/**
 *
 * @author lucio
 */
public class PageDefaultImplTest {

    /** The full set of data. */
    private static List<EntityBase> allData = new LinkedList<EntityBase>();

    /**
     * Initialize the full set of data.
     */
    @BeforeClass
    public static void setUpClass() {
        allData.add(new EntityBaseImpl(Long.valueOf(1)));
        allData.add(new EntityBaseImpl(Long.valueOf(2)));
        allData.add(new EntityBaseImpl(Long.valueOf(3)));
        allData.add(new EntityBaseImpl(Long.valueOf(4)));
        allData.add(new EntityBaseImpl(Long.valueOf(5)));
    }

    /**
     * Test of getPage method, of class PageDefaultImpl.
     */
    @Test
    public void testGetPage() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(0, 2), 1, 2, 5);
        assertEquals(1, instance.getPage());
    }

    /**
     * Test of isNextPage method, of class PageDefaultImpl.
     */
    @Test
    public void testIsNextPage() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(0, 2), 1, 2, 5);
        assertTrue(instance.isNextPage());
    }

    /**
     * Test of isNextPage method, of class PageDefaultImpl.
     */
    @Test
    public void testIsNextPageOnTheLast() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(4, 5), 3, 2, 5);
        assertFalse(instance.isNextPage());
    }

    /**
     * Test of isPreviousPage method, of class PageDefaultImpl.
     */
    @Test
    public void testIsPreviousPage() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(0, 2), 1, 2, 5);
        assertFalse(instance.isPreviousPage());
    }

    /**
     * Test of isPreviousPage method, of class PageDefaultImpl.
     */
    @Test
    public void testIsPreviousPageOnTheLast() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(4, 5), 3, 2, 5);
        assertTrue(instance.isPreviousPage());
    }

    /**
     * Test of getList method, of class PageDefaultImpl.
     */
    @Test
    public void testGetList() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(0, 2), 1, 2, 5);
        assertEquals(2, instance.getList().size());
    }

    /**
     * Test of getLastPage method, of class PageDefaultImpl.
     */
    @Test
    public void testGetLastPage() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(0, 2), 1, 2, 5);
        assertEquals(3, instance.getLastPage());
    }

    /**
     * Test of getLastPage method, of class PageDefaultImpl.
     */
    @Test
    public void testGetLastPageOnTheLast() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(4, 5), 3, 2, 5);
        assertEquals(3, instance.getLastPage());
    }

    /**
     * Test of getRowCount method, of class PageDefaultImpl.
     */
    @Test
    public void testGetRowCount() {
        PageDefaultImpl<EntityBase> instance = new PageDefaultImpl<EntityBase>(allData.subList(0, 2), 1, 2, 5);
        assertEquals(5, instance.getRowCount());
    }

    /**
     * A class that extends EntityBase.
     */
    public static class EntityBaseImpl extends EntityBase {

        public EntityBaseImpl(Long id) {
            this.id = id;
        }

    }

}