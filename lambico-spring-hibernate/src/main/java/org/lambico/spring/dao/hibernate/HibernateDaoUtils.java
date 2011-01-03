/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring - Hibernate.
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

package org.lambico.spring.dao.hibernate;

import java.lang.reflect.Method;

/**
 * Utility methods for implementing the Hibernate DAO.
 *
 * @author Lucio Benfante <lucio.benfante@jugpadova.it>
 * @version $Revision$
 */
public class HibernateDaoUtils {

    /**
     * Check if a method of the DAO is a known native method, that can be invoked
     * directly.
     *
     * @param method The method to check for.
     * @return true if the method is known for being native.
     */
    public static boolean isAKnownNativeMethod(Method method) {
        boolean result = false;
        if (method.getName().equals("toString")) {
            result = true;
        } else if (method.getName().equals("getHibernateTemplate")) {
            result = true;
        } else if (method.getName().equals("getType")) {
            result = true;
        }
        return result;
    }
}
