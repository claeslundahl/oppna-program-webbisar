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

import static org.apache.commons.lang.StringUtils.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Range;
import org.hibernate.validator.Valid;

@Entity
@Indexed
@Analyzer(impl = org.apache.lucene.analysis.standard.StandardAnalyzer.class)
@FullTextFilterDef(name = "enabledWebbis", impl = EnabledWebbisFilterFactory.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "Webbis")
public class Webbis implements Serializable {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @DocumentId
    private Long id;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @Column(length = 255, nullable = false)
    private String authorId;

    @Field(index = Index.TOKENIZED, store = Store.YES)
    @Length(max = 15)
    @Column(length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false, updatable = true)
    @NotNull
    private Sex sex;

    @Temporal(TemporalType.TIMESTAMP)
    @Fields( {
            @Field(name = "birthDate", index = Index.UN_TOKENIZED, store = Store.YES, bridge = @FieldBridge(impl = LocalDateBridge.class, params = @Parameter(name = "resolution", value = "DATE"))),
            @Field(name = "birthTime", index = Index.UN_TOKENIZED, store = Store.YES, bridge = @FieldBridge(impl = LocalDateBridge.class, params = @Parameter(name = "resolution", value = "DATETIME"))) })
    @Past
    private Date birthTime;

    @Column(nullable = false)
    @Range(min = 0, max = 10000)
    private int weight;

    @Column(nullable = false)
    @Range(min = 0, max = 100)
    private int length;

    @Fields( {
            @Field(name = "hospital", index = Index.TOKENIZED, store = Store.YES, bridge = @FieldBridge(impl = HospitalBridge.class)),
            @Field(name = "hospitalConstant", index = Index.UN_TOKENIZED, store = Store.YES) })
    @Enumerated(EnumType.STRING)
    private Hospital hospital;

    @Column(length = 255, nullable = true)
    @Field(index = Index.TOKENIZED, store = Store.YES)
    @Length(max = 25)
    private String home;

    @CollectionOfElements(fetch = FetchType.EAGER)
    @IndexColumn(name = "idx", base = 0)
    @IndexedEmbedded
    @JoinTable(name = "WebbisParent", joinColumns = @JoinColumn(name = "webbis_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Valid
    @NotEmpty
    private List<Name> parents = new ArrayList<Name>();

    @CollectionOfElements(fetch = FetchType.EAGER)
    @IndexColumn(name = "idx", base = 0)
    @IndexedEmbedded
    @JoinTable(name = "WebbisMediaFile", joinColumns = @JoinColumn(name = "webbis_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Valid
    private List<MultimediaFile> mediaFiles = new ArrayList<MultimediaFile>();

    @Column(length = 512, nullable = true)
    @Length(max = 25)
    private String siblings;

    @Column(length = 1024, nullable = true)
    @Length(max = 150)
    private String message;

    @Column(length = 512, nullable = false)
    @Email
    @NotEmpty
    private String email;

    @Column(length = 512, nullable = true)
    @Pattern(regex = "^((http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?){0,1}$", message = "har ogiltig URL")
    private String homePage;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private boolean disabled;

    @Column(updatable = false)
    private Date created;

    private Date lastModified;

    // Holding multiple birth sibling webbisar, if any. List will contain one or two other webbisar
    // in case of twin/triplet
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "multiple_birth_main_webbis_id")
    private List<Webbis> multipleBirthSiblings;

    // Holding main multiple birth webbis, if any. The main webbis will contain one or two other
    // webbisar in case of twin/triplet
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "multiple_birth_main_webbis_id")
    private Webbis multipleBirthMainWebbis;

    public Webbis() { /* empty */
    }

    public Webbis(Long id, String name, String authorId, Sex sex, BirthTime birthTime, int weight, int length,
            Hospital hospital, String home, List<Name> parents, List<MultimediaFile> mediaFiles, String siblings,
            String message, String email, String homePage, Date created) {

        this(name, authorId, sex, birthTime, weight, length, hospital, home, parents, mediaFiles, siblings,
                message, email, homePage);

        this.id = id;
        if (created == null) {
            this.created = new Date();
        } else {
            this.created = created;
        }
        this.lastModified = new Date();
    }

    public Webbis(String name, String authorId, Sex sex, BirthTime birthTime, int weight, int length,
            Hospital hospital, String home, List<Name> parents, List<MultimediaFile> mediaFiles, String siblings,
            String message, String email, String homePage) {
        this.name = capitalize(trimToEmpty(name));
        this.authorId = trimToEmpty(authorId);
        this.sex = sex;
        this.birthTime = birthTime.getTimeAsDate();
        this.weight = weight;
        this.length = length;
        this.hospital = hospital;
        this.home = capitalize(trimToEmpty(home));
        this.mediaFiles = mediaFiles;
        this.parents = parents;
        this.siblings = trimToEmpty(siblings);
        this.message = trimToEmpty(message);
        this.email = trimToEmpty(email);
        this.homePage = trimToEmpty(homePage);

        this.disabled = false;
        this.created = new Date();
        this.lastModified = this.created;
    }

    public List<MultimediaFile> getUnusedMediaFiles(Webbis previousWebbis) {
        List<MultimediaFile> unusedImages = new ArrayList<MultimediaFile>();
        for (MultimediaFile othersImage : previousWebbis.getMediaFiles()) {
            if (!getMediaFiles().contains(othersImage)) {
                unusedImages.add(othersImage);
            }
        }
        return unusedImages;
    }

    public boolean isPersisted() {
        return getId() != null;
    }

    public void toggleEnableDisable() {
        disabled = !disabled;
    }

    public void disable() {
        disabled = true;
    }

    public Long getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }

    public Sex getSex() {
        return sex;
    }

    public BirthTime getBirthTime() {
        return new BirthTime(birthTime);
    }

    public int getWeight() {
        return weight;
    }

    public int getLength() {
        return length;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public String getHome() {
        return home;
    }

    public String getSiblings() {
        return siblings;
    }

    public List<Name> getParents() {
        return parents;
    }

    public List<String> getParentsFirstNames() {
        List<String> fnames = new ArrayList<String>();
        for (Name n : parents) {
            fnames.add(n.getFirstName());
        }
        return fnames;
    }

    public List<MultimediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public MultimediaFile getSelectedImage() {
        if (mediaFiles.size() > 0) {
            return mediaFiles.get(0);
        } else {
            return null;
        }
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getHomePage() {
        return homePage;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public Date getCreated() {
        return created;
    }

    public Long getCreatedAsLong() {
        if (created != null) {
            return created.getTime();
        } else {
            return null;
        }
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Webbis getMainMultipleBirthWebbis() {
        return multipleBirthMainWebbis;
    }

    public void setMainMultipleBirthWebbis(Webbis mainWebbis) {
        this.multipleBirthMainWebbis = mainWebbis;
    }

    public List<Webbis> getMultipleBirthSiblings() {
        return multipleBirthSiblings;
    }

    public void setMultipleBirthSiblings(List<Webbis> siblingWebbisars) {
        this.multipleBirthSiblings = siblingWebbisars;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorId == null) ? 0 : authorId.hashCode());
        result = prime * result + ((birthTime == null) ? 0 : birthTime.hashCode());
        result = prime * result + ((home == null) ? 0 : home.hashCode());
        result = prime * result + ((hospital == null) ? 0 : hospital.hashCode());
        result = prime * result + length;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + weight;
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
        Webbis other = (Webbis) obj;
        if (authorId == null) {
            if (other.authorId != null) {
                return false;
            }
        } else if (!authorId.equals(other.authorId)) {
            return false;
        }
        if (birthTime == null) {
            if (other.birthTime != null) {
                return false;
            }
        } else if (!birthTime.equals(other.birthTime)) {
            return false;
        }
        if (home == null) {
            if (other.home != null) {
                return false;
            }
        } else if (!home.equals(other.home)) {
            return false;
        }
        if (hospital == null) {
            if (other.hospital != null) {
                return false;
            }
        } else if (!hospital.equals(other.hospital)) {
            return false;
        }
        if (length != other.length) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (sex == null) {
            if (other.sex != null) {
                return false;
            }
        } else if (!sex.equals(other.sex)) {
            return false;
        }
        if (weight != other.weight) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=").append(id).append(",");
        sb.append("authorId=").append(authorId).append(",");
        sb.append("name=").append(name).append(",");
        sb.append("sex=").append(sex).append(",");
        sb.append("birthTime=").append(sdf.format(birthTime)).append(",");
        sb.append("weight=").append(weight).append(",");
        sb.append("length=").append(length).append(",");
        sb.append("mediaFiles=[");
        for (MultimediaFile i : mediaFiles) {
            sb.append("mediaFile=[").append(i).append("]").append(",");
        }
        sb.append("]").append(",");
        if (multipleBirthSiblings != null && multipleBirthSiblings.size() > 0) {
            sb.append("multipleBirthSiblings=[");
            for (Webbis w : multipleBirthSiblings) {
                sb.append(w.toWebbisInfoOnlyString()).append(",");
            }
            sb.append("]").append(",");
        }
        sb.append("parents=[");
        for (Name p : parents) {
            sb.append("parent=[").append(p).append("]").append(",");
        }
        sb.append("]").append(",");
        sb.append("hospital=").append(hospital).append(",");
        sb.append("home=").append(home).append(",");
        sb.append("siblings=").append(siblings).append(",");
        sb.append("message=").append(message).append(",");
        sb.append("email=").append(email).append(",");
        sb.append("homePage=").append(homePage).append(",");
        sb.append("disabled=").append(disabled).append(",");
        sb.append("created=").append(sdf.format(created)).append(",");
        sb.append("lastModified=").append(sdf.format(lastModified));
        return sb.toString();
    }

    public String toWebbisInfoOnlyString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=").append(name).append(",");
        sb.append("sex=").append(sex).append(",");
        sb.append("birthTime=").append(sdf.format(birthTime)).append(",");
        sb.append("weight=").append(weight).append(",");
        sb.append("length=").append(length).append(",");
        sb.append("mediaFiles=[");
        for (MultimediaFile i : mediaFiles) {
            sb.append("mediaFile=[").append(i).append("]").append(",");
        }
        sb.append("]").append(",");
        sb.append("disabled=").append(disabled).append(",");
        sb.append("created=").append(sdf.format(created)).append(",");
        sb.append("lastModified=").append(sdf.format(lastModified));
        return sb.toString();
    }
}
