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
package org.lambico.test.spring.hibernate.junit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.dao.spring.hibernate.GenericDaoHibernateSupport;
import org.lambico.data.YamlFixtureHelper;
import org.lambico.po.hibernate.Entity;
import org.lambico.test.spring.hibernate.DaoUtils;
import org.lambico.test.spring.hibernate.junit4.FixtureSet.LoadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Spring test framework TestExecutionListener which looks for the
 * <code>FixtureSet</code> annotation and if found, attempts to load test fixture before the test is
 * run.
 *
 * @author michele franzin <michele at franzin.net>
 * @see FixtureSet
 */
public class FixturesTestExecutionListener extends AbstractTestExecutionListener {

    private static final Logger logger = LoggerFactory.
            getLogger(FixturesTestExecutionListener.class);
    private FixturesConfigurationAttributes configurationAttributes;
    // fixture instances
    private static Map<Class, List> fixtures = new LinkedHashMap<Class, List>();

    @Override
    public void beforeTestClass(final TestContext testContext) throws Exception {
        setupFixtures(testContext);
        LoadMode mode = retrieveConfigurationAttributes(testContext).getLoadMode();
        if (LoadMode.CLASS.equals(mode)) {
            // off transaction inserts (needs to be deleted)
            refreshDatabaseWithFixtures(testContext);
        }
    }

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        LoadMode mode = retrieveConfigurationAttributes(testContext).getLoadMode();
        if (LoadMode.TEST.equals(mode)) {
            // in transaction inserts (automatically deleted during rollback)
            refreshDatabaseWithFixtures(testContext);
        }
    }

    @Override
    public void afterTestClass(final TestContext testContext) {
        LoadMode mode = retrieveConfigurationAttributes(testContext).getLoadMode();
        if (LoadMode.CLASS.equals(mode)) {
            emptyDatabaseFixtures(testContext);
        }
    }

    /**
     * Reinsert all fixtures.
     */
    @SuppressWarnings("unchecked")
    private void refreshDatabaseWithFixtures(final TestContext testContext) throws Exception {
        LoadMode loadMode = retrieveConfigurationAttributes(testContext).getLoadMode();
        for (Class model : retrieveConfigurationAttributes(testContext).getModelClasses()) {
            GenericDaoBase dao = DaoUtils.getDaoFor(model, testContext.getApplicationContext());
            final HibernateTemplate template = ((GenericDaoHibernateSupport) dao).
                    getHibernateTemplate();
            List<Object> data = fixtures.get(model);
            if (data == null) {
                logger.warn("No fixtures stored for {} model", model.getSimpleName());
                continue;
            }
            for (Object entity : data) {
                if (LoadMode.CLASS.equals(loadMode)) {
                    // transaction will be rollbacked, but session cache lives
                    if (entity instanceof Entity) {
                        ((Entity) entity).setId(null);
                    } else {
                        // TODO: find an alternative for entities that don't have Entity as base class!
                    }
                }
                template.save(entity);
            }
        }
    }

    private void emptyDatabaseFixtures(final TestContext testContext) {
        for (Class model : retrieveConfigurationAttributes(testContext).getReversedModelClasses()) {
            GenericDaoBase dao = DaoUtils.getDaoFor(model, testContext.getApplicationContext());
            dao.deleteAll();
        }
    }

    /**
     * loads fixtures
     */
    @SuppressWarnings("unchecked")
    private void setupFixtures(final TestContext testContext) {
        fixtures.clear();
        FixturesConfigurationAttributes config = retrieveConfigurationAttributes(testContext);
        Set<Class> classes = config.getModelClassesSet();
        if (classes.isEmpty()) {
            logger.debug("No fixtures loaded for {}", testContext.getTestClass());
            return;
        }
        try {
            ClassPathResource path = (ClassPathResource) testContext.getApplicationContext().
                    getResource(ResourceLoader.CLASSPATH_URL_PREFIX + config.getRootFolder());
            fixtures.putAll(YamlFixtureHelper.loadFixturesFromResource(path, classes));
            if (logger.isInfoEnabled()) {
                HashMap<String, Integer> map = new HashMap<String, Integer>(fixtures.size());
                for (Class model : classes) {
                    map.put(model.getSimpleName(), fixtures.get(model).size());
                }
                logger.info("Loaded fixtures for models {}", map);
            }
        } catch (Exception e) {
            logger.error("I can't load all fixture for {}", classes, e);
        }
    }

    /**
     * Retrieves the {@link FixturesConfigurationAttributes} for the specified {@link Class class}
     * which may optionally declare or inherit a {@link FixtureSet @FixtureSet}.
     *
     * @param clazz the Class object corresponding to the test class for which the configuration
     * attributes should be retrieved
     * @return a new FixturesConfigurationAttributes instance
     */
    private FixturesConfigurationAttributes retrieveConfigurationAttributes(TestContext testContext) {
        if (this.configurationAttributes == null) {
            this.configurationAttributes = new FixturesConfigurationAttributes();

            Class<?>[] classes = getSuperClasses(testContext.getTestClass());
            ArrayUtils.reverse(classes);
            for (Class<?> c : classes) {
                FixtureSet annotation = c.getAnnotation(FixtureSet.class);
                if (annotation != null) {
                    configurationAttributes.merge(annotation);
                }
            }
        }
        return this.configurationAttributes;
    }

    /**
     * Gets all superclasses of the supplied {@link Class class}, including the class itself. The
     * ordering of the returned list will begin with the supplied class and continue up the class
     * hierarchy.
     * <p>Note: This code has been borrowed from
     * {@link org.junit.internal.runners.TestClass#getSuperClasses(Class)} and adapted.
     *
     * @param clazz the class for which to retrieve the superclasses.
     * @return all superclasses of the supplied class.
     */
    private Class<?>[] getSuperClasses(Class<?> clazz) {
        ArrayList<Class<?>> results = new ArrayList<Class<?>>();
        Class<?> current = clazz;
        while (current != null) {
            results.add(current);
            current = current.getSuperclass();
        }
        return results.toArray(new Class<?>[]{});
    }
}
