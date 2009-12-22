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

package org.lambico.spring.dao.hibernate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.lambico.dao.generic.Compare;
import org.lambico.dao.generic.CompareType;
import org.lambico.dao.generic.FirstResult;
import org.lambico.dao.generic.GenericDao;
import org.lambico.dao.generic.GenericDaoTypeSupport;
import org.lambico.dao.generic.MaxResults;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

/**
 * An aspect for instrumenting a class with dao interfaces and methods.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
@Aspect
public class HibernateDaoInstrumentation {

    private static final Logger logger = Logger.getLogger(HibernateDaoInstrumentation.class);

//    @Around(value = "execution(* *(..)) && target(org.lambico.dao.generic.AutoGenericDao)")
    @Around(value = "execution(* *(..)) && target(org.lambico.dao.generic.GenericDao)")
    public Object executeFinder(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        final GenericDao target = (GenericDao) pjp.getTarget();
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        final Object[] args = pjp.getArgs();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final StringBuffer errorMessages = new StringBuffer();
        final Integer firstResult = getFirstResultValue(args, parameterTypes, parameterAnnotations);
        final Integer maxResults = getMaxResultsValue(args, parameterTypes, parameterAnnotations);
        logger.debug("target: " + target);
        logger.debug("method: " + method);
        logger.debug("args: " + args);

        // query using a named query from the method name
        result = ((HibernateTemplate)target.getSupport()).executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                String queryName = queryNameFromMethod(target, method);
                Query namedQuery = null;
                try {
                    namedQuery = session.getNamedQuery(queryName);
                } catch (MappingException e) {
                // No such named query
                }
                if (namedQuery != null) {
                    for (int i = 0; i < args.length; i++) {
                        if (isQueryParameter(i, parameterTypes, parameterAnnotations)) {
                            Object arg = args[i];
                            namedQuery.setParameter(i, arg);
                        }
                    }
                    if (firstResult != null) {
                        namedQuery.setFirstResult(firstResult.intValue());
                    }
                    if (maxResults != null && maxResults.intValue() >= 0) {
                        namedQuery.setMaxResults(maxResults.intValue());
                    }
                    return namedQuery.list();
                } else {
                    errorMessages.append("Named query not found: ").append(queryName).append(". ");
                }
                return null;
            }
        });
        if (result == null) {
            // No named query found
            if (method.getName().startsWith("findBy")) {
                // Query evicting condition from the method name
                result = ((HibernateTemplate)target.getSupport()).executeFind(new HibernateCallback() {

                    public Object doInHibernate(Session session) throws HibernateException {
                        DetachedCriteria criteria = criteriaFromMethod(target, method, args);
                        Criteria executableCriteria = criteria.getExecutableCriteria(session);
                        if (firstResult != null) {
                            executableCriteria.setFirstResult(firstResult.intValue());
                        }
                        if (maxResults != null && maxResults.intValue() >= 0) {
                            executableCriteria.setMaxResults(maxResults.intValue());
                        }
                        return criteria.getExecutableCriteria(session).list();
                    }
                });
            } else {
                // Call an instance method
                try {
                    result = pjp.proceed(args);
                } catch (Throwable throwable) {
                    // TODO: enhance the exception management. For example: capture the AOP-related exceptions, and rethrows the DAO implementation-related ones (for example Hibernate/SQL exceptions)
                    errorMessages.append("Method not starting with \"findBy\".").append("Trying to call ").append(method.getName()).append(" method, but the method doesn't exist in the object (").append(target.getClass().getName()).append(").");
                    logger.error(errorMessages.toString(), throwable);
                }
            }
        }
        if (result != null && List.class.isAssignableFrom(result.getClass()) && !List.class.isAssignableFrom(method.getReturnType())) {
            // The return type is not a List, so I return the first result
            // of the list, or null if the list is empty
            List listResult = (List) result;
            if (!listResult.isEmpty()) {
                result = listResult.get(0);
            } else {
                result = null;
            }
        }
        return result;
    }

    private String queryNameFromMethod(GenericDao target, Method finderMethod) {
        return target.getType().getSimpleName() + "." + finderMethod.getName();
    }

    private DetachedCriteria criteriaFromMethod(GenericDao target, Method finderMethod, Object[] args) {
        Class<?>[] parameterTypes = finderMethod.getParameterTypes();
        Annotation[][] parameterAnnotations = finderMethod.getParameterAnnotations();
        DetachedCriteria criteria = DetachedCriteria.forClass(((GenericDaoTypeSupport)target).getType());
        int orderByIdx = finderMethod.getName().indexOf("OrderBy");
        String[] parameters = null;
        String[] orderParameters = null;
        if (orderByIdx == -1) {
            // no orderBy
            parameters = finderMethod.getName().substring(6).split("And");
        } else {
            if (orderByIdx - 1 > 6) {
                parameters = finderMethod.getName().substring(6, orderByIdx).split("And");
            }
            orderParameters = finderMethod.getName().substring(orderByIdx + 7).split("And");
        }
        if (parameters != null) {
            int argIndex = 0;
            for (int i = 0; i < parameters.length; i++) {
                while (!isQueryParameter(argIndex, parameterTypes, parameterAnnotations)) {
                    // skip not query parameters
                    argIndex++;
                }
                addComparison(criteria, StringUtils.uncapitalize(parameters[i]), args[argIndex], parameterAnnotations[argIndex]);
//                criteria.add(Restrictions.eq(StringUtils.uncapitalize(parameters[i]), args[argIndex]));
                argIndex++;
            }
        }
        if (orderParameters != null) {
            for (String oPar : orderParameters) {
                criteria.addOrder(Order.asc(StringUtils.uncapitalize(oPar)));
            }
        }
        return criteria;
    }

    /**
     * Check if a parameter is for the FirstResult value for the query.
     * 
     * @param parameterIndex index of the parameter as declared in the method
     * @param parameterTypes types of parameters of the method
     * @param parameterAnnotations annotations of parameters of the method
     * @return true if the parameter is for the FirstResult value
     */
    private boolean isFirstResultParameter(int parameterIndex, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        boolean result = false;
        for (Annotation annotation : parameterAnnotations[parameterIndex]) {
            if (annotation instanceof FirstResult && int.class.isAssignableFrom(parameterTypes[parameterIndex])) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Check if a parameter is for the MaxResults value for the query.
     * 
     * @param parameterIndex index of the parameter as declared in the method
     * @param parameterTypes types of parameters of the method
     * @param parameterAnnotations annotations of parameters of the method
     * @return true if the parameter is for the MaxResults value
     */
    private boolean isMaxResultsParameter(int parameterIndex, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        boolean result = false;
        for (Annotation annotation : parameterAnnotations[parameterIndex]) {
            if (annotation instanceof MaxResults && int.class.isAssignableFrom(parameterTypes[parameterIndex])) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Check if a parameter is a parameter for the query
     * 
     * @param parameterIndex index of the parameter as declared in the method
     * @param parameterTypes types of parameters of the method
     * @param parameterAnnotations annotations of parameters of the method
     * @return true if the parameter is a parameter for the query
     */
    private boolean isQueryParameter(int parameterIndex, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        return !isMaxResultsParameter(parameterIndex, parameterTypes, parameterAnnotations) && !isFirstResultParameter(parameterIndex, parameterTypes, parameterAnnotations);
    }

    /**
     * Get the value of the parameter marked as @FirstRecord
     * 
     * @param args parameters values
     * @param parameterTypes types of parameters of the method
     * @param parameterAnnotations annotations of parameters of the method
     * @return The value if it's found. null otherwise.
     */
    private Integer getFirstResultValue(Object args[], Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        Integer result = null;
        for (int i = 0; i < args.length; i++) {
            if (isFirstResultParameter(i, parameterTypes, parameterAnnotations)) {
                result = (Integer) args[i];
                break;
            }
        }
        return result;
    }

    /**
     * Get the value of the parameter marked as @MaxResults
     * 
     * @param args parameters values
     * @param parameterTypes types of parameters of the method
     * @param parameterAnnotations annotations of parameters of the method
     * @return The value if it's found. null otherwise.
     */
    private Integer getMaxResultsValue(Object args[], Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        Integer result = null;
        for (int i = 0; i < args.length; i++) {
            if (isMaxResultsParameter(i, parameterTypes, parameterAnnotations)) {
                result = (Integer) args[i];
                break;
            }
        }
        return result;
    }

    /**
     * Add a comparison to criteria.
     * 
     * @param criteria
     * @param string the parameter name
     * @param object the parameter value to compare
     * @param annotations method parameter annotations
     */
    private void addComparison(DetachedCriteria criteria, String parameter, Object value, Annotation[] annotations) {
        Compare compareAnnotation = getCompareAnnotation(annotations);
        CompareType compareType = compareAnnotation != null?compareAnnotation.value():CompareType.EQUAL;
        switch (compareType) {
            case LIKE:
                criteria.add(Restrictions.like(parameter, value));
                break;
            case ILIKE:
                criteria.add(Restrictions.ilike(parameter, value));
                break;
            case GE:
                criteria.add(Restrictions.ge(parameter, value));
                break;
            case GT:
                criteria.add(Restrictions.gt(parameter, value));
                break;
            case LE:
                criteria.add(Restrictions.le(parameter, value));
                break;
            case LT:
                criteria.add(Restrictions.lt(parameter, value));
                break;
            case NE:
                criteria.add(Restrictions.ne(parameter, value));
                break;
            case EQUAL:
            default:
                criteria.add(Restrictions.eq(parameter, value));                
        }
    }

    private Compare getCompareAnnotation(Annotation[] annotations) {
        Compare result = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Compare) {
                result = (Compare) annotation;
                break;
            }
        }
        return result;
    }
}
