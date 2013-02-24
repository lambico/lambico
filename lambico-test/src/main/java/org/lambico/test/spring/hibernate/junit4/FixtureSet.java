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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ContextConfiguration} defines class-level metadata which can be used to instruct client
 * code with regard to how to load and fixtures.
 *
 * @author michele franzin <michele at franzin.net>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface FixtureSet {

    public enum LoadMode {

        /**
         * put fixtures in DB once per class, before any test
         */
        CLASS,
        /**
         * put fixtures on DB before each test
         */
        TEST
    }

    public enum EvictMode {

        /**
         * don't cleanup fixtures
         */
        NONE,
        /**
         * clean fixtures once per class, after any test
         */
        CLASS,
        /**
         * clean fixtures after each test
         */
        TEST
    }

    /**
     * The resource locations to use for loading an fixtures.
     */
    String rootFolder() default "/fixtures";

    /**
     * The model classes to use for loading fixtures.
     */
    Class[] modelClasses() default {};

    /**
     * when db push will happend
     */
    LoadMode loadMode() default LoadMode.CLASS;
}
