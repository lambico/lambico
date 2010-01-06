/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico test.
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

package org.lambico.test.spring.hibernate;

import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.lambico.dao.spring.BusinessDao;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.generic.GenericDaoTypeSupport;

/**
 * Utils for the DAO tools.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public final class DaoUtils {

    /** The logger for this class. **/
    private static Logger logger = Logger.getLogger(DaoUtils.class);

    /**
     * Creates a new instance of DaoUtils.
     */
    private DaoUtils() {
    }

    /**
     * Return a map  of DAOs from a bean container.
     *
     * @param beanFactory The bean container.
     * @return A map of daos. The key is the id of the bean in the container.
     */
    public static Map<String, Object> getDaos(final ListableBeanFactory beanFactory) {
        Map<String, Object> result =
                new HashMap<String, Object>();
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            try {
                Object bean = beanFactory.getBean(beanNames[i]);
                if (DaoUtils.isDao(bean)) {
                    result.put(beanNames[i], bean);
                }
            } catch (BeanIsAbstractException ex) {
                // ignore it
                logger.debug("The requested bean is abstract.");
            }
        }
        return result;
    }

    /**
     * Check if an object is a DAO.
     *
     * @param o The object to check.
     * @return true if the object is recognized as a DAO.
     */
    @SuppressWarnings(value = "unchecked")
    public static boolean isDao(final Object o) {
        if (AnnotationUtils.findAnnotation(o.getClass(), BusinessDao.class) != null) {
            return true;
        }

        Class[] objInterfaces = o.getClass().getInterfaces();
        for (int i = 0; i < objInterfaces.length; i++) {
            if (objInterfaces[i].getAnnotation(Dao.class) != null) {
                return true;
            }
        }
        return false;
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
        for (int i = 0; i < objInterfaces.length; i++) {
            @SuppressWarnings(value = "unchecked")
            Dao daoAnnotation =
                    (Dao) objInterfaces[i].getAnnotation(Dao.class);
            if (daoAnnotation != null) {
                if (daoAnnotation.entity().getName().
                        equals(daoEntityType.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get the DAO for the specific entity class type.
     *
     * @param daoEntityType The entity class type.
     * @param beanFactory The context in wich searching for the DAO.
     * @return The DAO fot the specific entity class type.
     *         null if it can't be found.
     */
    public static GenericDaoBase getDaoFor(final Class daoEntityType,
                                       final ListableBeanFactory beanFactory) {
        GenericDaoBase result = null;
        Map<String, Object> daos =
                DaoUtils.getDaos(beanFactory);
        for (Object dao : daos.values()) {
            if (DaoUtils.isDaoFor(dao, daoEntityType)) {
                result = (GenericDaoBase) dao;
                break;
            }
        }
        return result;
    }
}
