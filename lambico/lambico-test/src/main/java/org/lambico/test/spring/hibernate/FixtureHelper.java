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

import org.lambico.test.spring.*;
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
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author Paolo Dona paolo.dona@seesaw.it
 * @author Michele Franzin michele.franzin@seesaw.it
 */
public class FixtureHelper {

    private final static Logger logger = Logger.getLogger(FixtureHelper.class);

    private final static Pattern headerMatch = Pattern.compile(
            "^-\\s??(\\S*)\\s*?$", Pattern.MULTILINE);

    private final static Pattern lineMatch = Pattern.compile("^(.+)$",
            Pattern.MULTILINE);

    /**
     * Gets file name of a fixture fragment related to a model.
     */
    public static String getFixtureFileName(Class model) {
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
     */
    public static String getFixtureDaoId(Class model) {
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
     */
    public static String getFixtureBusinessDaoId(Class model) {
        return StringUtils.uncapitalize(model.getSimpleName()) + "BusinessDao";
    }

    /**
     * Gets human readable name of a model.
     */
    public static String getModelName(Class model) {
        return model.getSimpleName();
    }

    /**
     * @param classpathResource dir relative path
     */
    public static Map<Class, Object[]> loadFixturesFromResource(
            final String classpathResource, final Set<Class> models) {
        return loadFixturesFromResource(
                new ClassPathResource(classpathResource), models);
    }

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
            logger
                    .debug("### Inizio Yaml generato dal merge delle fixtures ###\n"
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
     * ritorna il contenuto del file <Class>.yml
     */
    private static String loadFixtureStringForClass(Resource resource,
                                                    Class model) throws IOException {
        String fixtureString = Utils.loadString(resource);
        // Add fixture class into header
        Matcher matcher = headerMatch.matcher(fixtureString);
        // Il replace del $ serve alla regex nel caso di inner classes
        fixtureString = matcher.replaceAll("- $1 !"
                + model.getName().replace("$", "\\$"));
        // Indent lines
        matcher = lineMatch.matcher(fixtureString);
        fixtureString = matcher.replaceAll("  $1");
        // aggiungo l'header (Array di oggetti <Class>)
        return "- !" + model.getName() + "[]" + IOUtils.LINE_SEPARATOR
                + fixtureString;
    }

    @SuppressWarnings("unchecked")
    public static void populateDbForModel(Class model, Object[] fixtures,
                                          GenericDaoBase dao) {
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
                ((HibernateTemplate)dao.getSupport()).flush();
            }
        } catch (Exception e) {
            logger.error("Error populating rows in " + getModelName(model)
                    + " table", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void eraseDbForModel(Class model, GenericDaoBase dao) {
        logger.debug("Erasing table for " + getModelName(model));
        try {
            // List rows = dao.findAll();
            // for (Object o : rows) {
            // dao.delete(o);
            // }
            if (dao == null) {
                throw new IllegalArgumentException("Dao associated to " + model.getName() + " PO is null!");
            }

            int deleted = ((HibernateTemplate)dao.getSupport()).bulkUpdate("DELETE FROM " + org.hibernate.cfg.DefaultComponentSafeNamingStrategy.INSTANCE.tableName(model.getSimpleName()));
//                                deleteAll();
        } catch (Exception e) {
            logger.error("Error deleting rows in " + getModelName(model)
					+ " table", e);
		}
	}

}
