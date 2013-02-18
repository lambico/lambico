/**
 * Copyright (C) 2013 Lambico Team <michele@franzin.net>
 *
 * This file is part of Lambico Data Pump.
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
package org.lambico.data;

import java.io.File;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lambico.data.EngineFactory.EngineType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fixture load / dump facility
 *
 * @author michele franzin <michele at franzin.net>
 */
public class FixtureHelper {

    private static Logger logger = LoggerFactory.getLogger(FixtureHelper.class);
    private static final Pattern HEADER_MATCH = Pattern.compile("^-\\s??(\\S*)\\s*?$",
            Pattern.MULTILINE);
    private static final Pattern LINE_MATCH = Pattern.compile("^(.+)$", Pattern.MULTILINE);

    private FixtureHelper() {
    }

//    /**
//     * Gets id of dao related to a model.
//     * <p/>
//     * *WARNING*: strong condition on the DAO id (entity_name+Dao) For retriving a dao for an
//     * entity it's better something like:
//     * <code>
//     * DaoProvider daos = (DaoProvider)ctx.getBean(DAO_PROVIDER_ID);
//     * GenericDao dao = (GenericDao)daos.getDao(clazz);
//     * </code>
//     * <p/>
//     * The DAO_PROVIDER_ID usually is "daos".
//     *
//     * @param model The class of a model.
//     * @return The DAO id.
//     */
//    public static String getFixtureDaoId(final Class model) {
//        return StringUtils.uncapitalize(model.getSimpleName()) + "Dao";
//    }
//
//    /**
//     * Gets id of dao related to a model.
//     * <p/>
//     * *WARNING*: strong condition on the DAO id (entity_name+Dao) For retriving a dao for an
//     * entity it's better something like:
//     * <code>
//     * DaoProvider daos = (DaoProvider)ctx.getBean(DAO_PROVIDER_ID);
//     * GenericDao dao = (GenericDao)daos.getDao(clazz);
//     * </code>
//     * <p/>
//     * The DAO_PROVIDER_ID usually is "daos".
//     *
//     * @param model The class of a model.
//     * @return The business DAO id.
//     */
//    public static String getFixtureBusinessDaoId(final Class model) {
//        return StringUtils.uncapitalize(model.getSimpleName()) + "BusinessDao";
//    }
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
    public static Map<Class, List> loadFixturesFromResource(final String classpathResource,
            final Set<Class> models) {
        return loadFixturesFromResource(new ClassPathResource(classpathResource), models);
    }

    /**
     * Load the fixtures. Compose a unique fixture document (aliases are document scoped)
     *
     * @param fixtureDir The directory where to search the fixtures.
     * @param models The set of models.
     * @return The model->values map of fixtures.
     */
    @SuppressWarnings("unchecked")
    public static Map<Class, List> loadFixturesFromResource(final ClassPathResource fixtureDir,
            final Set<Class> models) {
        // Mantiene l'ordine di inserimento
        LinkedHashMap<Class, List> fixtures = new LinkedHashMap<Class, List>(models.size());
        Pump engine = EngineFactory.getEngine(EngineType.YAML);
        StringBuilder sb = new StringBuilder();
        for (Class model : models) {
            InputStream stream = null;
            String path = fixtureDir.getPath();
            if (!path.endsWith(File.separator)) {
                path += File.separatorChar;
            }
            String fixtureFileName = engine.getFixtureFileName(path, model);
            try {
                ClassPathResource fixtureResource = new ClassPathResource(fixtureFileName);
                stream = fixtureResource.getInputStream();
                String content = loadFixtureTextForClass(stream, model);
                if (StringUtils.isNotBlank(content)) {
                    sb.append(content).append(IOUtils.LINE_SEPARATOR);
                    fixtures.put(model, new LinkedList());
                } else {
                    logger.warn("No fixtures for " + getModelName(model) + " in the file '"
                            + fixtureFileName + "' ?");
                }
            } catch (FileNotFoundException e) {
                logger.warn("Fixture file not found for " + getModelName(model)
                        + ", did you created the file '" + fixtureFileName + "' ?");
            } catch (IOException e) {
                logger.error("Loading of fixtures failed for " + getModelName(model), e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }

        logger.debug("### start of merged fixtures dump ###\n{}"
                + "\n### end of merged fixtures dump ###", sb);

        Iterable<Object> loaded = engine.load(sb.toString());
        if (loaded.iterator().hasNext()) {
            List objectsTree = (List) loaded.iterator().next();
            if (objectsTree != null) {
                for (Object modelFixtures : objectsTree) {
                    List objects = (List) modelFixtures;
                    if (objects != null && !objects.isEmpty()) {
                        fixtures.get(objects.get(0).getClass()).addAll(objects);
                    }
                }
            }
        }
        return fixtures;
    }

    /**
     * Load the content of a fixture file <Class>.yml, indenting the whole content
     *
     * @param stream The fixture file.
     * @param model The model to load.
     * @return The content of the fixture file.
     * @throws IOException In case of error.
     */
    private static String loadFixtureTextForClass(final InputStream stream,
            final Class model) throws IOException {
        String fixtureString = IOUtils.toString(stream, "UTF-8").trim();
        if (StringUtils.isBlank(fixtureString)) {
            return StringUtils.EMPTY;
        }
        // Add fixture class into header
        Matcher matcher = HEADER_MATCH.matcher(fixtureString);
        // Il replace del $ serve alla regex nel caso di inner classes
        fixtureString = matcher.replaceAll("- $1 !!" + model.getName().replace("$", "\\$"));
        // Indent lines
        matcher = LINE_MATCH.matcher(fixtureString);
        fixtureString = matcher.replaceAll("  $1");
        // aggiungo l'header per ottenere una lista di oggetti <Class>
        return "-" + IOUtils.LINE_SEPARATOR + fixtureString;
    }
}
