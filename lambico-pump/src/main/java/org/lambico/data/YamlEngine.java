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

import java.io.InputStream;
import java.util.Iterator;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Yaml data pump engine
 *
 * @author michele franzin <michele at franzin.net>
 */
class YamlEngine implements Pump {

    @Override
    public Iterable<Object> load(InputStream resource) {
        Yaml yaml = new Yaml();
        return yaml.loadAll(resource);
    }

    @Override
    public Iterable<Object> load(String text) {
        Yaml yaml = new Yaml();
        return yaml.loadAll(text);
    }

    @Override
    public String dump(Iterator<Object> objects) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        return yaml.dumpAll(objects);
    }

    /**
     * Gets file name of a fixture fragment related to a model.
     *
     * @param model The class of a model.
     * @return The fixture file name.
     */
    @Override
    public String getFixtureFileName(final String rootPath, final Class model) {
        return rootPath + model.getSimpleName() + ".yml";
    }
}
