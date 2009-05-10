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
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import static org.apache.commons.lang.StringUtils.*;

@Embeddable
@Indexed
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Name implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Field(index=Index.TOKENIZED, store=Store.YES)
	@Column(nullable=false, length=255)
	@NotNull
	@NotEmpty
	@Length(max=15)
	private String firstName;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	@Column(nullable=true, length=255)
	@Length(max=25)
	private String lastName;
	
	public Name() { /*empty*/ }

	public void setFirstName(String firstName) {
		this.firstName = capitalize(firstName);
	}

	public void setLastName(String lastName) {
		this.lastName = capitalize(lastName);
	}

	public Name(String firstName, String lastName) {
		this.firstName = capitalize(firstName);
		this.lastName = capitalize(lastName);
	}

	public Name(Name name) {
		this(name.firstName, name.lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return new StringBuilder().append(firstName).append(' ').append(lastName).toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("firstName=").append(firstName).append(",");
		sb.append("lastName=").append(lastName);
		return sb.toString();
	}
	
}
