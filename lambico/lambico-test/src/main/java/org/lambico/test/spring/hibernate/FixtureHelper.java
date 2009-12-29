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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.parancoe.yaml.Yaml;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lambico.dao.generic.GenericDaoBase;
import org.lambico.test.spring.Utils;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author Paolo Dona paolo.dona@seesaw.it
 * @author Michele Franzin michele.franzin@seesaw.it
 */
public class FixtureHelper {

    /** The logger for this class. */
    private static Logger logger = Logger.getLogger(FixtureHelper.class);
    /** The header match. */
    private static final Pattern HEADER_MATCH = Pattern.compile(
            "^-\\s??(\\S*)\\s*?$", Pattern.MULTILINE);
    /** The line match. */
    private static final Pattern LINE_MATCH = Pattern.compile("^(.+)$",
            Pattern.MULTILINE);

    /**
     * A default protected constructor.
     */
    protected FixtureHelper() {
    }

    /**
     * Gets file name of a fixture fragment related to a model.
     *
     * @param model The class of a model.
     * @return The fixture file name.
     */
    public static String getFixtureFileName(final Class model) {
        return model.getSimpleName() + ".yml";
    }

    /**
     * Gets id of dao related to a model.
     * <p/>
     * *WARNING*: strong condition on the DAO id (entity_name+Dao) For retriving
     * a dao for an entity it's better something like: <code>
     * DaoProvider daos = (DaoProvider)ctx.getBean(DAO_PROVIDER_ID);
     * GenericDao dao = (GenericDao)daos.getDao(clazz);
     * </code>
     * <p/>
     * The DAO_PROVIDER_ID usually is "daos".
     *
     * @param model The class of a model.
     * @return The DAO id.
     */
    public static String getFixtureDaoId(final Class model) {
        return StringUtils.uncapitalize(model.getSimpleName()) + "Dao";
    }

    /**
     * Gets id of dao related to a model.
     * <p/>
     * *WARNING*: strong condition on the DAO id (entity_name+Dao) For retriving
     * a dao for an entity it's better something like: <code>
     * DaoProvider daos = (DaoProvider)ctx.getBean(DAO_PROVIDER_ID);
     * GenericDao dao = (GenericDao)daos.getDao(clazz);
     * </code>
     * <p/>
     * The DAO_PROVIDER_ID usually is "daos".
     *
     * @param model The class of a model.
     * @return The business DAO id.
     */
    public static String getFixtureBusinessDaoId(final Class model) {
        return StringUtils.uncapitalize(model.getSimpleName()) + "BusinessDao";
    }

    /**
     * Gets human readable name of a model.
     *
     * @param model The class of a model.
     * @return The model name.
     */
    public static String getModelName(final Class model) {
        return model.getSimpleName();
    }

    /**
     * Load the fixtures.
     *
     * @param classpathResource dir relative path
     * @param models The set of models.
     * @return The model->values map of fixtures.
     */
    public static Map<Class, Object[]> loadFixturesFromResource(
            final String classpathResource, final Set<Class> models) {
        return loadFixturesFromResource(
                new ClassPathResource(classpathResource), models);
    }

    /**
     * Load the fixtures.
     *
     * @param fixtureDir The directory where to search the fixtures.
     * @param models The set of models.
     * @return The model->values map of fixtures.
     */
    public static Map<Class, Object[]> loadFixturesFromResource(
            final ClassPathResource fixtureDir, final Set<Class> models) {
        LinkedHashMap<Class, Object[]> fixtures = new LinkedHashMap<Class, Object[]>(
                models.size());
        String fixtureFileName = null;
        StringBuffer sb = new StringBuffer();
        for (Class model : models) {
            fixtureFileName = fixtureDir.getPath() + getFixtureFileName(model);
            InputStream stream = null;
            try {
                Resource fixtureResource =
                        new ClassPathResource(fixtureFileName);
                stream = fixtureResource.getInputStream();
                String content = loadFixtureStringForClass(fixtureResource, model);
                if (StringUtils.isNotBlank(content)) {
                    sb.append(content);
                    sb.append(IOUtils.LINE_SEPARATOR);
                    // Mantiene l'ordine di inserimento
                    fixtures.put(model, null);
                } else {
                    logger.warn("Non ho trovato fixtures per "
                            + getModelName(model) + " dentro il file '"
                            + fixtureFileName + "' ?");
                }
            } catch (FileNotFoundException e) {
                logger.warn("Non ho trovato il file di fixtures per "
                        + getModelName(model) + ", hai creato il file '"
                        + fixtureFileName + "' ?");
            } catch (IOException e) {
                logger.error("Fallito il caricamento delle fixtures per "
                        + getModelName(model), e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }

        if (sb.length() == 0) {
            sb.append("--- !java.lang.Object[]  []" + IOUtils.LINE_SEPARATOR);
        } else {
            sb.insert(0, "--- !java.lang.Object[]" + IOUtils.LINE_SEPARATOR);
        }

        sb.append("...");
        sb.append(IOUtils.LINE_SEPARATOR);

        if (logger.isDebugEnabled()) {
            logger.debug("### Inizio Yaml generato dal merge delle fixtures ###\n"
                    + sb.toString()
                    + "\n### Fine Yaml generato dal merge delle fixtures ###");
        }

        // Debug file output
        // try {
        // FileUtils.writeStringToFile(new File("dump.yml"), sb.toString(),
        // "UTF-8");
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        // Se il documento è vuoto, yaml ritorna un'HashMap
        Object any = Yaml.load(sb.toString());

        if (any instanceof Object[]) {
            Object[] fixtureObjects = (Object[]) any;
            for (Object fixtureObject : fixtureObjects) {
                Object[] objects = (Object[]) fixtureObject;
                // null safe handling
                if (!ArrayUtils.isEmpty(objects)) {
                    fixtures.put(objects[0].getClass(), objects);
                }
            }
        }
        return fixtures;
    }

    /**
     * Load the content of a fixture file <Class>.yml.
     *
     * @param resource The fixture file.
     * @param model The model to load.
     * @return The content of the fixture file.
     * @throws IOException In case of error.
     */
    private static String loadFixtureStringForClass(final Resource resource,
            final Class model) throws IOException {
        String fixtureString = Utils.loadString(resource);
        // Add fixture class into header
        Matcher matcher = HEADER_MATCH.matcher(fixtureString);
        // Il replace del $ serve alla regex nel caso di inner classes
        fixtureString = matcher.replaceAll("- $1 !"
                + model.getName().replace("$", "\\$"));
        // Indent lines
        matcher = LINE_MATCH.matcher(fixtureString);
        fixtureString = matcher.replaceAll("  $1");
        // aggiungo l'header (Array di oggetti <Class>)
        return "- !" + model.getName() + "[]" + IOUtils.LINE_SEPARATOR
                + fixtureString;
    }

    /**
     * Populate the DB with the fixtures data.
     *
     * @param model The model to populate.
     * @param fixtures The fixtures.
     * @param dao The DAO to use.
     */
    @SuppressWarnings("unchecked")
    public static void populateDbForModel(final Class model, final Object[] fixtures,
            final GenericDaoBase dao) {
        logger.debug("Populating table for " + getModelName(model));
        if (fixtures == null) {
            logger.warn("Non ci sono fixtures per " + getModelName(model)
                    + ", hai creato il file '" + getFixtureFileName(model)
                    + "'?");
            return;
        }
        try {
            for (Object entity : fixtures) {
                dao.store(entity);
                ((HibernateTemplate) dao.getSupport()).flush();
            }
        } catch (Exception e) {
            logger.error("Error populating rows in " + getModelName(model)
                    + " table", e);
        }
    }

    /**
     * Erase the DB for a model.
     *
     * @param model The model to erase.
     * @param dao The DAO tp use.
     */
    @SuppressWarnings("unchecked")
    public static void eraseDbForModel(final Class model, final GenericDaoBase dao) {
        logger.debug("Erasing table for " + getModelName(model));
        try {
            // List rows = dao.findAll();
            // for (Object o : rows) {
            // dao.delete(o);
            // }
            if (dao == null) {
                throw new IllegalArgumentException("Dao associated to "
                        + model.getName() + " PO is null!");
            }

            int deleted = ((HibernateTemplate) dao.getSupport()).bulkUpdate("DELETE FROM "
                    + org.hibernate.cfg.DefaultComponentSafeNamingStrategy.INSTANCE.tableName(
                    model.getSimpleName()));
        } catch (Exception e) {
            logger.error("Error deleting rows in " + getModelName(model)
                    + " table", e);
        }
    }
}
