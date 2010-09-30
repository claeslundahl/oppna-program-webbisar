/**
 * Copyright 2010 Västra Götalandsregionen
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
 *
 */

package se.vgregion.webbisar.types;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

@Embeddable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MultimediaFile implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum MediaType {
        IMAGE, VIDEO;
    }

    @Column(nullable = false, length = 512)
    @NotNull
    @NotEmpty
    private String location;

    @Column(nullable = true, length = 512)
    @Length(max = 80)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "mediaType", nullable = false, updatable = true, columnDefinition = "VARCHAR(512) default 'IMAGE'")
    @NotNull
    private MediaType mediaType;

    @Column(nullable = true, length = 512)
    @Length(max = 50)
    private String contentType;

    public MultimediaFile() {/* Empty */
    }

    public MultimediaFile(String location, String text, MediaType mediaType, String contentType) {
        this.location = location;
        this.text = text;
        this.mediaType = mediaType;
        this.contentType = contentType;
    }

    public MultimediaFile(MultimediaFile img) {
        this.location = img.location;
        this.text = img.text;
        this.mediaType = img.mediaType;
        this.contentType = img.contentType;
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

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
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
        MultimediaFile other = (MultimediaFile) obj;
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new StringBuffer().append("location=").append(location).append(",").append("text=").append(text)
                .append(",").append("mediaType=").append(mediaType).append(",").append("contentType=").append(
                        contentType).toString();
    }

}
