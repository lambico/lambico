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

import javax.sql.DataSource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Spring test framework TestExecutionListener which deactivate FK constraints before each test.
 *
 * @author michele franzin <michele at franzin.net>
 */
public class ForeignKeyDisablingListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(final TestContext testContext) throws Exception {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        dataSource.getConnection().prepareStatement("SET DATABASE REFERENTIAL INTEGRITY FALSE").
                execute();
    }
}
