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

import org.lambico.dao.*;
import java.util.Iterator;
import java.util.Map;

/**
 * A class for retrieving DAOs from the context.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class BaseDaoProvider implements DaoProvider {
    private Map<String, ?> daoMap;    
    
    /** Creates a new instance of BaseDaoProvider */
    public BaseDaoProvider() {
    }

    public Map<String, ? extends Object> getDaoMap() {
        return daoMap;
    }

    public void setDaoMap(Map<String, ? extends Object> daoMap) {
        this.daoMap = daoMap;
    }
    
    public Object getDao(String daoName) {
        return daoMap.get(daoName);
    }

    public Object getDao(Class daoEntityType) {
        Object result = null;
        Iterator it = daoMap.values().iterator();
        while (it.hasNext()) {
            Object elem = (Object) it.next();
            if (DaoUtils.isDaoFor(elem, daoEntityType)) {
                result = elem;
                break;
            }
        }
        return result;
    }
    
}
