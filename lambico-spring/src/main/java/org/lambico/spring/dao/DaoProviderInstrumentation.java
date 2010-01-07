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
package org.lambico.spring.dao;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lambico.dao.DaoProvider;
import org.springframework.util.StringUtils;

/**
 * An aspect for instrumenting a dao provider.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
@Aspect()
public class DaoProviderInstrumentation {

    /** Prefix of the get&lt;DAO&gt; method. */
    private static final String METHOD_PREFIX = "get";
    /** The logger for this class. */
    private static Logger logger = Logger.getLogger(DaoProviderInstrumentation.class);

    /**
     * Executes a get&lt;DAO&gt; method in the instrumented class.
     *
     * @param pjp The joint point.
     * @return The result of the execution of the get&lt;DAO&gt; method.
     * @throws Throwable In case of error.
     */
    @Around("execution(* *(..)) && target(org.lambico.dao.DaoProvider)")
    public Object executeFinder(final ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        final DaoProvider target = (DaoProvider) pjp.getTarget();
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        final Object[] args = pjp.getArgs();
        logger.debug("target: " + target);
        logger.debug("method: " + method);
        logger.debug("args: " + args);

        if (method.getName().startsWith(METHOD_PREFIX)) {
            result = target.getDaoMap().get(daoNameFromMethod(method));
            if (result == null) {
                try {
                    result = pjp.proceed(args);
                } catch (Throwable throwable) {
                    logger.error("No " + daoNameFromMethod(method) + " DAO in the daoMap. "
                            + "Trying to call " + method.getName()
                            + " method, but the method doesn't exist in the object.", throwable);
                    throw throwable;
                }
            }
        } else {
            // Call an instance method
            try {
                result = pjp.proceed(args);
            } catch (Throwable throwable) {
                logger.error("Method not starting with \"" + METHOD_PREFIX + "\". "
                        + "Trying to call " + method.getName()
                        + " method, but the method doesn't exist in the object ("
                        + target.getClass().getName() + ").", throwable);
                throw throwable;
            }
        }
        return result;
    }

    /**
     * Extract the name fo the DAO from the method.
     *
     * @param getterMethod The get&lt;DAO&gt; method name.
     * @return The name of the DAO.
     */
    private String daoNameFromMethod(final Method getterMethod) {
        return StringUtils.uncapitalize(getterMethod.getName().substring(METHOD_PREFIX.length()));
    }
}
