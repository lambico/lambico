/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of lambico-spring.
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

import org.lambico.dao.*;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;


/**
 * An aspect for instrumenting a dao provider.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
@Aspect()
public class DaoProviderInstrumentation {
    private static final Logger logger = Logger.getLogger(DaoProviderInstrumentation.class);
    
    @Around("execution(* *(..)) && target(org.lambico.dao.DaoProvider)")
    public Object executeFinder(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        final DaoProvider target = (DaoProvider)pjp.getTarget();
        final Method method = ((MethodSignature)pjp.getSignature()).getMethod();
        final Object[] args = pjp.getArgs();
        logger.debug("target: "+target);
        logger.debug("method: "+method);
        logger.debug("args: "+args);
        
        if (method.getName().startsWith("get")) {
            result = target.getDaoMap().get(daoNameFromMethod(method));
            if (result == null) {
                try {
                    result = pjp.proceed(args);
                } catch (Throwable throwable) {
                    logger.error("No "+daoNameFromMethod(method)+ " DAO in the daoMap. " +
                            "Trying to call "+method.getName()+" method, but the method doesn't exist in the object."
                            , throwable);
                    throw throwable;
                }
            }
        } else {
            // Call an instance method
            try {
                result = pjp.proceed(args);
            } catch (Throwable throwable) {
                    logger.error("Method not starting with \"get\". " +
                            "Trying to call "+method.getName()+" method, but the method doesn't exist in the object ("+target.getClass().getName()+")."
                            , throwable);
                throw throwable;
            }
        }
        return result;
    }
    
    private String daoNameFromMethod(Method getterMethod) {
        return StringUtils.uncapitalize(getterMethod.getName().substring(3));
    }
}
