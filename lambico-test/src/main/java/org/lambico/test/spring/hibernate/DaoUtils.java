/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Test.
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
import java.util.HashMap;
import java.util.Map;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.util.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils for the DAO tools. NB: COPIED FROM LAMBICO SPRING BEACAUSE OF CIRCULAR DEPENDENCY
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public final class DaoUtils {

    private static final Logger logger = LoggerFactory.getLogger(DaoUtils.class);

    /**
     * Creates a new instance of DaoUtils.
     */
    private DaoUtils() {
    }

    /**
     * Return a map of DAOs from a bean container.
     *
     * @param beanFactory The bean container.
     * @return A map of daos. The key is the id of the bean in the container.
     */
    public static Map<String, Object> getDaos(final ListableBeanFactory beanFactory) {
        Map<String, Object> result = new HashMap<String, Object>();
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            try {
                Object bean = beanFactory.getBean(beanName);
                if (DaoHelper.isDao(bean)) {
                    result.put(beanName, bean);
                }
            } catch (BeanIsAbstractException ex) {
                // ignore it
                logger.debug("The requested bean is abstract.");
            }
        }
        return result;
    }

    /**
     * Get the DAO for the specific entity class type.
     *
     * @param daoEntityType The entity class type.
     * @param beanFactory The context in wich searching for the DAO.
     * @return The DAO fot the specific entity class type. null if it can't be found.
     */
    public static GenericDaoBase getDaoFor(final Class daoEntityType,
            final ListableBeanFactory beanFactory) {
        GenericDaoBase result = null;
        for (Object dao : getDaos(beanFactory).values()) {
            if (DaoHelper.isDaoFor(dao, daoEntityType)) {
                result = (GenericDaoBase) dao;
                break;
            }
        }
        return result;
    }
}
