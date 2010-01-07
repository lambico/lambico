/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Example - Console Spring Hibernate.
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

package org.lambico.example.consolespringhibernate.util;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Some utility methods
 * 
 * @author Lucio Benfante
 */
public class ApplicationContextHolder {

    private static GenericApplicationContext context;
    

    static {
        context = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context).loadBeanDefinitions(
                new String[]{
                    "classpath:org/lambico/spring/dao/generic/genericDao.xml",
                    "classpath:org/lambico/spring/applicationContextBase.xml",
                    "classpath:database.xml",
                    "classpath:applicationContext.xml"
                });
        context.refresh();
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void autowireBeanProperties(Object o) {
        context.getBeanFactory().autowireBeanProperties(o,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }
}
