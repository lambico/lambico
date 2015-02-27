/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Spring - Hibernate.
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
// Copyright 2006-2010 The Parancoe Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.lambico.spring.dao.hibernate.po;

import java.util.Arrays;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lambico.po.hibernate.EntityBase;

/**
 * A persistent object to be used for the tests of the CacheIt annotation.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @author Federico Russo <chiccorusso@gmail.com>
 * @version $Revision$
 */
@Entity()
@NamedQueries({
    @NamedQuery(name = "CachedEntityTC.findCacheByFieldTwo",
    query = "from CachedEntityTC where fieldTwo LIKE ?"),
    @NamedQuery(name = "CachedEntityTC.findNoCacheByFieldThree",
    query = "from CachedEntityTC where fieldThree LIKE ?")
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CachedEntityTC extends EntityBase {

    private String fieldOne;
    private String fieldTwo;
    private String fieldThree;
    private Long numericField;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private Byte[] blobOne;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private Byte[] blobTwo;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private Byte[] blobThree;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private Byte[] blobFour;

    /**
     * Creates a new instance of EntityTC
     */
    public CachedEntityTC() {
    }

    public CachedEntityTC(String fieldOne, String fieldTwo, String fieldThree,
            Long numericField) {
        this.fieldOne = fieldOne;
        this.fieldTwo = fieldTwo;
        this.fieldThree = fieldThree;
        this.numericField = numericField;
        blobOne = new Byte[1000];
        blobTwo = new Byte[1000];
        blobThree = new Byte[1000];
        blobFour = new Byte[1000];
        Arrays.fill(blobFour, new Byte((byte) 4));
        Arrays.fill(blobThree, new Byte((byte) 3));
        Arrays.fill(blobTwo, new Byte((byte) 2));
        Arrays.fill(blobOne, new Byte((byte) 1));
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
