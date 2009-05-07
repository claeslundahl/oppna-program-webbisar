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
