/**
 * Copyright 2009 Vastra Gotalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */
package se.vgr.webbisar.types;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.NotNull;

@Embeddable
@Indexed
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BirthMultiplicityMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @Column(name = "sibling_id", nullable = false)
    @NotNull
    private Long siblingId;

    public BirthMultiplicityMapping() { /* empty */
    }

    public void setSiblingId(Long siblingId) {
        this.siblingId = siblingId;
    }

    public BirthMultiplicityMapping(Long siblingId) {
        this.siblingId = siblingId;
    }

    public Long getSiblingId() {
        return siblingId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((siblingId == null) ? 0 : siblingId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BirthMultiplicityMapping other = (BirthMultiplicityMapping) obj;
        if (siblingId == null) {
            if (other.siblingId != null) {
                return false;
            }
        } else if (!siblingId.equals(other.siblingId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("siblingId=").append(siblingId);
        return sb.toString();
    }

}
