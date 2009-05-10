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
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;


@Embeddable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Image implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, length=512)
	@NotNull
	@NotEmpty
	private String location;
	
	@Column(nullable=true, length=512)
	@Length(max=80)
	private String text;
	
	public Image() {/*Empty*/}
	
	public Image(String location, String text) {
		this.location = location;
		this.text = text;
	}

	public Image(Image img) {
		this.location = img.location;
		this.text = img.text;		
	}

	public String getLocation() {
		return location;
	}

	public String getText() {
		return text;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
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
		Image other = (Image) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append("location=").append(location).append(",").append("text=").append(text).toString();
	}
		
}
