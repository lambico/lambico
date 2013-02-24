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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.data.FixtureHelper;
import org.lambico.test.spring.EnhancedTestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * A base class for tests that populate the DB with the fixture data.
 *
 * @author Jacopo Murador <jacopo.murador at seesaw.it>
 *
 * @deprecated as of Spring 3.0, in favor of using the listener-based test context framework
 * ({@link org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests})
 * @see AbstractBaseTest
 * @see FixtureLoaderTestExecutionListener
 */
@Deprecated
public abstract class DBTest extends EnhancedTestCase {

    protected Map<Class, List> fixtures;
    @Resource
    protected SessionFactory sessionFactory;
    @Resource(name = "daoMap")
    protected HashMap daoMap;

    /**
     * Returns a <strong>reverse-ordered</strong> array of the models that need to be loaded.
     *
     * @return The array of class models.
     */
    public final Class[] getReverseOrderFixtureClasses() {
        Class[] models = getFixtureClasses();
        CollectionUtils.reverseArray(models);
        return models;
    }

    /**
     * Returns a <strong>reverse-ordered</strong> array of the models that need to be loaded.
     *
     * NB: It must always return a new object, not a reference to an already existent object.
     *
     * @return The array of class models.
     */
    public Class[] getFixtureClasses() {
        return new Class[]{};
    }

    /**
     * Returns a set of the models that need to be loaded.
     *
     * @return The set of class models.
     */
    public final Set<Class> getFixtureClassSet() {
        Class[] classes = getFixtureClasses();
        Set<Class> result = new LinkedHashSet<Class>(classes.length);
        CollectionUtils.addAll(result, classes);
        return result;
    }

    /**
     * Delete the rows from the tables, and reinsert all data.
     *
     * @throws Exception In case of error.
     */
    @Override
    public void onSetUpBeforeTransaction() throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //Attach transaction to thread
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        TransactionSynchronizationManager.initSynchronization();

        try {
            // erase everything
            for (Class model : getReverseOrderFixtureClasses()) {
                GenericDaoBase dao = DaoUtils.getDaoFor(model, applicationContext);
                FixtureUtils.eraseDbForModel(model, dao);
            }
            // repopulate
            for (Class model : getFixtureClasses()) {
                GenericDaoBase dao = DaoUtils.getDaoFor(model, applicationContext);
                FixtureUtils.populateDbForModel(model, fixtures.get(model), dao);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e);
            logger.debug("Rolling back the database transaction");
            session.getTransaction().rollback();
        } finally {
            try {
                session.close();
            } catch (Exception e) { /*do nothing*/
                logger.info("Can't close the session! (ignore it)");
            }
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            TransactionSynchronizationManager.clearSynchronization();
        }

    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
            "classpath:org/lambico/spring/dao/hibernate/genericDao.xml",
            "classpath:org/lambico/spring/dao/hibernate/applicationContextBase.xml",
            "classpath:applicationContext_test.xml"
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void prepareTestInstance() throws Exception {
        super.prepareTestInstance();
        daoMap.putAll(DaoUtils.getDaos(applicationContext));

        if (fixtures == null) {
            // carico le fixture se non sono già presenti
            Set<Class> fixtureClasses = getFixtureClassSet();
            if (fixtureClasses != null && fixtureClasses.size() > 0) {
                try {
                    fixtures = FixtureHelper.loadFixturesFromResource(
                            (ClassPathResource) applicationContext.getResource(
                            ResourceLoader.CLASSPATH_URL_PREFIX + "/fixtures/"), fixtureClasses);
                    logger.info("Loaded fixtures for classes "
                            + fixtures.keySet().toString());
                } catch (Exception e) {
                    logger.warn("I can't load all fixture for classes "
                            + fixtureClasses.toString(), e);
                }
            } else {
                logger.info("No fixtures to load");
            }
        }
    }

    /**
     * At the end of the test the method endTransaction call a rollback for the transaction although
     * a explicit rollback call is did. This raise a false exception.
     */
    @Override
    protected void endTransaction() {
        try {
            super.endTransaction();
        } catch (Exception ex) {
            //do nothing
            logger.info("Expected false exception when ending transaction! (ignore it)");
        }
    }
}
