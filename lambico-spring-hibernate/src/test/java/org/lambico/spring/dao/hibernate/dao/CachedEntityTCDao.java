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
package org.lambico.spring.dao.hibernate.dao;

import java.util.List;
import org.lambico.dao.generic.CacheIt;
import org.lambico.dao.generic.Compare;
import org.lambico.dao.generic.CompareType;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDao;
import org.lambico.spring.dao.hibernate.po.CachedEntityTC;

/**
 *
 * @author Federico Russo <chiccorusso@gmail.com>
 */
@Dao(entity = CachedEntityTC.class)
@CacheIt
public interface CachedEntityTCDao extends GenericDao<CachedEntityTC, Long> {

    @CacheIt
    List<CachedEntityTC> findByFieldOne(@Compare(CompareType.LIKE) String value);

    @CacheIt
    List<CachedEntityTC> findCacheByFieldTwo(String value);

    List<CachedEntityTC> findNoCacheByFieldThree(String value);
}
