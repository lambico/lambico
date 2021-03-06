/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Core.
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

package org.lambico.dao.generic;

/**
 * Enum used with the {@link Compare) annotation, for defining type
 * of comparison for a finder parameter used as query parameter.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision$
 */
public enum CompareType {
    /** Compare by equality. */
    EQUAL,
    /** Compare by like. */
    LIKE,
    /** Compare by ilike. */
    ILIKE,
    /** Compare by greather than or equal. */
    GE,
    /** Compare by greather than. */
    GT,
    /** Compare by less than or equal. */
    LE,
    /** Compare by less than. */
    LT,
    /** Compare by no equal. */
    NE
}
