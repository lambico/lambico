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
package org.lambico.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.lambico.dao.spring.BusinessDao;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDao;
import org.lambico.dao.generic.GenericDaoTypeSupport;

/**
 * DAO object identification helper methods.
 *
 * @author michele franzin <michele.franzin at seesaw.it>
 */
public final class DaoHelper {

    /**
     * Creates a new instance of DaoUtils.
     */
    private DaoHelper() {
    }

    /**
     * Check if an object is a DAO.
     *
     * @param o The object to check.
     * @return true if the object is recognized as a DAO.
     */
    @SuppressWarnings(value = "unchecked")
    public static boolean isDao(final Object o) {
        if (GenericDao.class.isAssignableFrom(o.getClass())) {
            return true;
        }

        Class[] objInterfaces = o.getClass().getInterfaces();
        for (Class objInterface : objInterfaces) {
            if (objInterface.getAnnotation(Dao.class) != null) {
                return true;
            }
        }
        return AnnotationUtils.findAnnotation(o.getClass(), BusinessDao.class) != null;
    }

    /**
     * Check if an object is a DAO for a specific entity class.
     *
     * @param o The object to check.
     * @param daoEntityType The entity class type.
     * @return true if the object is o DAO for the entity.
     */
    public static boolean isDaoFor(final Object o, final Class daoEntityType) {
        if (AnnotationUtils.findAnnotation(o.getClass(), BusinessDao.class) != null) {
            if (((GenericDaoTypeSupport) o).getType().getName().equals(daoEntityType.getName())) {
                return true;
            }
        }

        Class[] objInterfaces = o.getClass().getInterfaces();
        for (Class objInterface : objInterfaces) {
            @SuppressWarnings(value = "unchecked")
            Dao daoAnnotation = (Dao) objInterface.getAnnotation(Dao.class);
            if (daoAnnotation != null) {
                if (daoAnnotation.entity().getName().
                        equals(daoEntityType.getName())) {
                    return true;
                }
            }
        }

        if (GenericDaoTypeSupport.class.isAssignableFrom(o.getClass())) {
            if (((GenericDaoTypeSupport) o).getType().equals(daoEntityType)) {
                return true;
            }
        }

        return false;
    }
}
