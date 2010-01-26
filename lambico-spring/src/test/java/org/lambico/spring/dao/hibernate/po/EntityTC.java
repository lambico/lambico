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

package org.lambico.spring.dao.hibernate.po;

import org.lambico.po.hibernate.EntityBase;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * An persistent object to be used for the tests of the generic DAO.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision$
 */
@Entity
@NamedQueries({
@NamedQuery(name="EntityTC.findByFieldOne", query="from EntityTC where fieldOne = ?"),
@NamedQuery(name="EntityTC.findByFieldOneAndFieldTwoAndFieldThreeWithName", query="from EntityTC where fieldOne = ? and fieldThree = ? and fieldTwo = :a"),
@NamedQuery(name="EntityTC.searchAllOrderByFieldOne", query="from EntityTC order by fieldOne"),
@NamedQuery(name="EntityTC.countByFieldOne", query="select count(etc) from EntityTC etc where etc.fieldOne = ?"),
@NamedQuery(name="EntityTC.maxByFieldOne", query="select max(etc.numericField) from EntityTC etc where etc.fieldOne = ?")})
public class EntityTC extends EntityBase {
    private String fieldOne;
    private String fieldTwo;
    private String fieldThree;
    private Long numericField;
    
    /**
     * Creates a new instance of EntityTC
     */
    public EntityTC() {
    }

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }

    public String getFieldThree() {
        return fieldThree;
    }

    public void setFieldThree(String fieldThree) {
        this.fieldThree = fieldThree;
    }

    public Long getNumericField() {
        return numericField;
    }

    public void setNumericField(Long numericField) {
        this.numericField = numericField;
    }
    
}
