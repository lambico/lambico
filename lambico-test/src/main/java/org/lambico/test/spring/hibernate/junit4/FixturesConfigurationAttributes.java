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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.lambico.test.spring.hibernate.junit4.FixtureSet.LoadMode;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Configuration attributes for configuring fixture enable tests.
 *
 * @author michele franzin <michele at franzin.net>
 */
public class FixturesConfigurationAttributes {

    private static final Class[] defaultModelClasses = (Class[]) AnnotationUtils.getDefaultValue(
            FixtureSet.class, "modelClasses");
    private static final String defaultRootFolder = (String) AnnotationUtils.getDefaultValue(
            FixtureSet.class, "rootFolder");
    private static final LoadMode defaultloadMode = (LoadMode) AnnotationUtils.getDefaultValue(
            FixtureSet.class, "loadMode");
    private Class[] modelClasses;
    private String rootFolder;
    private LoadMode loadMode;

    public FixturesConfigurationAttributes() {
        this.modelClasses = defaultModelClasses;
        this.rootFolder = defaultRootFolder;
        this.loadMode = defaultloadMode;
    }

    public FixturesConfigurationAttributes(Class[] modelClasses, String rootFolder,
            LoadMode loadMode) {
        this.modelClasses = modelClasses;
        this.rootFolder = rootFolder;
        this.loadMode = loadMode;
    }

    public Class[] getModelClasses() {
        return modelClasses;
    }

    public void setModelClasses(Class[] modelClasses) {
        this.modelClasses = modelClasses;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public LoadMode getLoadMode() {
        return loadMode;
    }

    public void setLoadMode(LoadMode loadMode) {
        this.loadMode = loadMode;
    }

    /**
     * Returns a <strong>reverse-ordered</strong> array of the models that need to be loaded.
     *
     * @return The array of class models.
     */
    public Class[] getReversedModelClasses() {
        Class[] reverseClasses = modelClasses.clone();
        CollectionUtils.reverseArray(reverseClasses);
        return reverseClasses;
    }

    /**
     * Returns a set of the models that need to be loaded.
     *
     * @return The set of class models.
     */
    public Set<Class> getModelClassesSet() {
        Set<Class> result = new LinkedHashSet<Class>(modelClasses.length);
        CollectionUtils.addAll(result, modelClasses);
        return result;
    }

    public void merge(Annotation annotation) {
        Class[] models = (Class[]) AnnotationUtils.getValue(annotation, "modelClasses");
        if (!Arrays.equals(defaultModelClasses, models)) {
            this.modelClasses = models;
        }
        String folder = (String) AnnotationUtils.getValue(annotation, "rootFolder");
        if (!defaultRootFolder.equals(folder)) {
            this.rootFolder = folder;
        }
        LoadMode mode = (LoadMode) AnnotationUtils.getValue(annotation, "loadMode");
        if (!defaultloadMode.equals(mode)) {
            this.loadMode = mode;
        }
    }
}
