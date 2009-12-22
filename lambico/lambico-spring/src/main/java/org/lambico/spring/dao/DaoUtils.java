/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of lambico-spring.
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

package org.lambico.spring.dao;

import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import java.util.HashMap;
import java.util.Map;
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
public class DaoUtils {

    /**
     * Creates a new instance of DaoUtils
     */
    private DaoUtils() {
    }

    /**
     * Return a map  of DAOs from a bean container.
     *
     * @param beanFactory The bean container.
     * @return A map of daos. The key is the id of the bean in the container.
     */
    public static Map<String, Object> getDaos(ListableBeanFactory beanFactory) {
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
            }
        }
        return result;
    }

    /**
     * Check if an object is a DAO.
     *
     * @return true if the object is recognized as a DAO.
     */
    @SuppressWarnings(value = "unchecked")
    public static boolean isDao(Object o) {
        if (AnnotationUtils.findAnnotation(o.getClass(), BusinessDao.class) != null) return true;

        Class[] objInterfaces = o.getClass().getInterfaces();
        for (int i = 0; i < objInterfaces.length; i++) {
            if (objInterfaces[i].getAnnotation(Dao.class) != null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDaoFor(Object o, Class daoEntityType) {
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

    public static GenericDaoBase getDaoFor(Class daoEntityType,
                                       ListableBeanFactory beanFactory) {
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
