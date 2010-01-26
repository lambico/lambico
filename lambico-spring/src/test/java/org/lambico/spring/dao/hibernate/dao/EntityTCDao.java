/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring.
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

import org.lambico.spring.dao.hibernate.po.EntityTC;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.MaxResults;
import org.lambico.dao.generic.FirstResult;
import org.lambico.dao.generic.Compare;
import org.lambico.dao.generic.CompareType;
import java.util.List;
import org.lambico.dao.generic.GenericDao;
import org.lambico.dao.generic.NamedParameter;

/**
 * A DAO to be used for the tests of the generic DAO.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
@Dao(entity=EntityTC.class)
public interface EntityTCDao extends GenericDao<EntityTC, Long> {
    List<EntityTC> findByFieldOne(String value);
    List<EntityTC> findByFieldOneAndFieldTwoAndFieldThreeWithName(String one, @NamedParameter("a") String two, String three);
    List<EntityTC> findByFieldTwo(String value);
    List<EntityTC> findByFieldThree(@Compare(CompareType.ILIKE) String value);
    List<EntityTC> findByFieldOneAndFieldTwo(String one, String two);
    List<EntityTC> findByOrderByFieldOne();
    List<EntityTC> findByOrderByFieldTwo();
    List<EntityTC> findByOrderByFieldOneAndFieldTwo();
    EntityTC findByFieldOneOrderByFieldTwo(String one);
    List<EntityTC> findByOrderByFieldOne(@FirstResult int firstResult, @MaxResults int maxResults);
    List<EntityTC> searchAllOrderByFieldOne(@FirstResult int firstResult, @MaxResults int maxResults);
    Long countByFieldOne(String value);
    Long maxByFieldOne(String value);
}
