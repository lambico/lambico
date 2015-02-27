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

import java.util.List;
import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.spring.hibernate.GenericDaoHibernateSupport;
import org.lambico.data.YamlFixtureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;

/**
 * Fixture utility class.
 *
 * @author michele franzin <michele at franzin.net>
 */
@Deprecated
public class FixtureUtils {

    private static Logger logger = LoggerFactory.getLogger(FixtureUtils.class);

    /**
     * Populate the DB with the fixtures data.
     *
     * @param model The model to populate.
     * @param fixtures The fixtures.
     * @param dao The DAO to use.
     */
    public static void populateDbForModel(final Class model, final List fixtures,
            final GenericDaoBase dao) {
        logger.debug("Populating table for {}", YamlFixtureHelper.getModelName(model));
        if (fixtures == null) {
            logger.warn("No data for {}, did you created the fixture file?",
                    YamlFixtureHelper.getModelName(model));
            return;
        }
        final HibernateTemplate template = ((GenericDaoHibernateSupport) dao).getHibernateTemplate();
        try {
            for (Object entity : fixtures) {
                template.saveOrUpdate(entity);
            }
            template.flush();
            template.clear();
        } catch (Exception e) {
            logger.error("Error populating rows in {} table", YamlFixtureHelper.getModelName(model), e);
        }
    }

    /**
     * Erase the DB for a model.
     *
     * @param model The model to erase.
     * @param dao The DAO tp use.
     */
    public static void eraseDbForModel(final Class model, final GenericDaoBase dao) {
        logger.debug("Erasing table for {}", YamlFixtureHelper.getModelName(model));
        try {
            if (dao == null) {
                throw new IllegalArgumentException("Dao associated to " + model.getName()
                        + " PO is null!");
            }

            final HibernateTemplate template = ((GenericDaoHibernateSupport) dao).
                    getHibernateTemplate();
            template.bulkUpdate("DELETE FROM "
                    + DefaultComponentSafeNamingStrategy.INSTANCE.tableName(model.getSimpleName()));
        } catch (Exception e) {
            logger.error("Error deleting rows in {} table", YamlFixtureHelper.getModelName(model), e);
        }
    }
}
