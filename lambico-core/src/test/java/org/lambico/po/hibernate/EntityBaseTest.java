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
package org.lambico.po.hibernate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests on the EntityBase class.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class EntityBaseTest {

    /**
     * Test of getId method, of class EntityBase.
     */
    @Test
    public void getId() {
        EntityBase instance = new EntityBaseImpl();
        assertNull("A newly created entity should have its id equalt to null.", instance.getId());
    }

    /**
     * Test of setId method, of class EntityBase.
     */
    @Test
    public void setId() {
        // Not a really useful test
        Long id = 1L;
        EntityBase instance = new EntityBaseImpl();
        instance.setId(id);
        assertNotNull(instance.getId());
        assertEquals(id, instance.getId());
        assertTrue(id == instance.getId());
    }

    /**
     * Test of equals method, of class EntityBase, using null ids.
     */
    @Test
    public void equalsWithNullId() {
        EntityBase instanceA = new EntityBaseImpl();
        EntityBase instanceB = new EntityBaseImpl();
        assertFalse(instanceA.equals(instanceB));
    }

    /**
     * Test of equals method, of class EntityBase, using the same not null id.
     */
    @Test
    public void equalsWithTheSameId() {
        Long idA = 1L;
        Long idB = 1L;
        EntityBase instanceA = new EntityBaseImpl();
        instanceA.setId(idA);
        EntityBase instanceB = new EntityBaseImpl();
        instanceB.setId(idB);
        assertTrue(instanceA.equals(instanceB));
    }

    /**
     * Test of equals method, of class EntityBase, using two different not null ids.
     */
    @Test
    public void equalsWithDifferentIds() {
        Long idA = 1L;
        Long idB = 2L;
        EntityBase instanceA = new EntityBaseImpl();
        instanceA.setId(idA);
        EntityBase instanceB = new EntityBaseImpl();
        instanceB.setId(idB);
        assertFalse(instanceA.equals(instanceB));
    }

    /**
     * Test of equals method, of class EntityBase, using a null id, and a not null id.
     */
    @Test
    public void equalsWithANullId() {
        Long idA = 1L;
        Long idB = null;
        EntityBase instanceA = new EntityBaseImpl();
        instanceA.setId(idA);
        EntityBase instanceB = new EntityBaseImpl();
        instanceB.setId(idB);
        assertFalse(instanceA.equals(instanceB));
    }

    /**
     * Test of hashCode method, of class EntityBase.
     */
    @Test
    public void hashCodeMethod() {
        Long idA = 1L;
        Long idB = 1L;
        EntityBase instanceA = new EntityBaseImpl();
        instanceA.setId(idA);
        EntityBase instanceB = new EntityBaseImpl();
        instanceB.setId(idB);
        assertTrue(instanceA.hashCode() == instanceB.hashCode());
    }

    /**
     * Test of hashCode method, of class EntityBase.
     */
    @Test
    public void hashCodeWithNullId() {
        EntityBase instanceA = new EntityBaseImpl();
        EntityBase instanceB = new EntityBaseImpl();
        assertTrue(instanceA.hashCode() != instanceB.hashCode());
    }

    /**
     * Test of toString method, of class EntityBase.
     */
    @Test
    public void toStringMethod() {
        Long id = 1L;
        EntityBase instance = new EntityBaseImpl();
        instance.setId(id);
        assertEquals(instance.getClass().getName() + "[id=1]", instance.toString());
    }

    /**
     * A class that extends EntityBase.
     */
    public class EntityBaseImpl extends EntityBase {
    }
}
