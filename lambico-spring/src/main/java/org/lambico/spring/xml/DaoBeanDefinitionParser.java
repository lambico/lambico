/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring.
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
package org.lambico.spring.xml;

import org.lambico.dao.generic.CacheIt;
import org.lambico.dao.generic.Dao;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * The parser for the DAO bean definition.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public class DaoBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /** The interface attribute. */
    public static final String INTERFACE_ATTRIBUTE = "interface";
    /** The genericDao attribute. */
    public static final String GENERIC_DAO_ATTRIBUTE = "genericDao";

    /** Creates a new instance of DaoBeanDefinitionParser. */
    public DaoBeanDefinitionParser() {
    }

    /**
     * {@inheritDoc}
     *
     * In this case it always return a {@link ProxyFactoryBean} class definition.
     *
     * @param element {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected Class getBeanClass(final Element element) {
        return ProxyFactoryBean.class;
    }

    /**
     * {@inheritDoc}
     *
     * @param element {@inheritDoc}
     * @param bean {@inheritDoc}
     */
    @Override
    protected void doParse(final Element element, final BeanDefinitionBuilder bean) {
        String daoInterface = element.getAttribute(INTERFACE_ATTRIBUTE);
        bean.addPropertyValue("proxyInterfaces", daoInterface);
        Class entityType = null;
        boolean cacheQueries = false;
        try {
            Class daoInterfaceClass =
                    Class.forName(daoInterface, true, this.getClass().getClassLoader());
            @SuppressWarnings("unchecked")
            Dao daoAnnotation = (Dao) daoInterfaceClass.getAnnotation(Dao.class);
            if (daoAnnotation != null) {
                entityType = daoAnnotation.entity();
            }
            if (entityType == null) {
                throw new IllegalArgumentException(
                        "target Dao interface not annotated with Dao annotation");
            }
            cacheQueries = (daoInterfaceClass.getAnnotation(CacheIt.class) != null);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Dao interface not found", ex);
        }
        String genericDao = element.getAttribute(GENERIC_DAO_ATTRIBUTE);
        BeanDefinitionBuilder genericDaoBDB = BeanDefinitionBuilder.childBeanDefinition(genericDao);
        genericDaoBDB.addPropertyValue("type", entityType);
        genericDaoBDB.addPropertyValue("classLevelCacheQueries", cacheQueries);
        bean.addPropertyValue("target", genericDaoBDB.getBeanDefinition());
    }

    /**
     * {@inheritDoc}
     *
     * In this case it always returns true.
     *
     * @return true
     */
    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param element {@inheritDoc}
     * @param definition {@inheritDoc}
     * @param parserContext {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected String resolveId(final Element element, final  AbstractBeanDefinition definition,
            final ParserContext parserContext) {
        if (shouldGenerateId()) {
            return super.resolveId(element, definition, parserContext);
        } else {
            String id = element.getAttribute(ID_ATTRIBUTE);
            if (!StringUtils.hasText(id) && shouldGenerateIdAsFallback()) {
                String daoInterface = element.getAttribute(INTERFACE_ATTRIBUTE);
                id = StringUtils.uncapitalize(StringUtils.unqualify(daoInterface));
            }
            return id;
        }
    }
}
