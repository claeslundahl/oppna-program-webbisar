package se.vgr.webbisar.presentation;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.util.Date;

import se.vgr.webbisar.types.Image;
import se.vgr.webbisar.types.Sex;
import se.vgr.webbisar.types.Webbis;

public class WebbisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String smartTime;
	private String parentsShort;
	private String header;
	private String truncHeader;
	private String date;
	private String time;
	private String weight;
	private String length;
	private String parent1;
	private String parent2;
	private String siblings;
	private String home;
	private String hospital;
	private String [] imageUrls;
	private String message;
	private String homePage;
	private int selectedImage;
	private String selectedImageComment;
	private String [] styles = new String [] {"selected", "notselected", "notselected", "notselected"};
	private String imageBaseUrl;

	public WebbisBean(String imageBaseUrl, Webbis webbis, int selectedImage) {
		
		this.id = webbis.getId();
		this.name = generateName(webbis);
		this.smartTime = generateSmartTime(webbis);
		this.parentsShort = getParentsNames(webbis);
		this.header = generateHeader(webbis);
		this.truncHeader = generateTruncHeader(webbis);
		this.time = webbis.getBirthTime().getTime();
		this.date = webbis.getBirthTime().getSmartTime(new Date());
		this.weight = webbis.getWeight() + "g";
		this.length = webbis.getLength() + "cm";
		this.parent1 = webbis.getParents().size() > 0 ? webbis.getParents().get(0).getFullName() : "";
		this.parent2 = webbis.getParents().size() > 1 ? webbis.getParents().get(1).getFullName() : "";
		this.siblings = webbis.getSiblings();
		this.home = webbis.getHome();
		this.hospital = webbis.getHospital().toLongString();
		this.imageUrls = new String[webbis.getImages().size()];
		int cnt = 0;
		for(Image image : webbis.getImages()) {
			this.imageUrls[cnt++] = imageBaseUrl + "/"+ image.getLocation();
		}
		this.selectedImage = setSelectedImage(selectedImage);
		if(webbis.getImages().size() > 0) {
			this.selectedImageComment = webbis.getImages().get(selectedImage).getText();
		}
		this.homePage = webbis.getHomePage();
		this.message = webbis.getMessage();
		this.imageBaseUrl = imageBaseUrl;
	}

	public WebbisBean(String imageBaseUrl, Webbis webbis) {
		this(imageBaseUrl, webbis, 0);
	}
	
	private int setSelectedImage(int selectedImage) {
		styles = new String [] {"notselected", "notselected", "notselected", "notselected"};
		styles[selectedImage] = "selected";
		return selectedImage;
	}

	private String generateName(Webbis webbis) {
		return isNotEmpty(webbis.getName()) ? webbis.getName() : webbis
				.getSex() == Sex.Female ? "Flicka" : "Pojke";
	}
	
	private String generateSmartTime(Webbis webbis) {
		return "Född " + webbis.getBirthTime().getSmartTime(new Date());
	}
			
	private String generateHeader(Webbis webbis) {
		String name = generateName(webbis);

		String date = webbis.getBirthTime().getSmartTime(new Date());

		String parents = getParentsNames(webbis);

		StringBuffer sb = new StringBuffer().append(name).append(" född ").append(date);
		if(isNotEmpty(parents)) {
			sb.append(", ").append(parents);
		}
		return sb.toString();
	}

	private String generateTruncHeader(Webbis webbis) {
		String name = generateName(webbis);

		String date = webbis.getBirthTime().getSmartTime(new Date());

		String parents = getParentsNames(webbis);

		StringBuffer sb = new StringBuffer().append(name).append(" född ").append(date);
		if(isNotEmpty(parents) && parents.length() < 25) {
			sb.append(", ").append(parents);
		}
		return sb.toString();
		
	}
	
	private String getParentsNames(Webbis webbis) {
		switch (webbis.getParentsFirstNames().size()) {
		case 1:
			return webbis.getParentsFirstNames().get(0);
		case 2:
			return webbis.getParentsFirstNames().get(0) + " och "
					+ webbis.getParentsFirstNames().get(1);
		default:
			return "";
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSmartTime() {
		return smartTime;
	}

	public String getParentsShort() {
		return parentsShort;
	}

	public String getHeader() {
		return header;
	}

	public String getTruncHeader() {
		return truncHeader;
	}
	
	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}

	public String getLongDate() {
		return date + " " + time;
	}

	public String getWeight() {
		return weight;
	}

	public String getLength() {
		return length;
	}

	public String getParent1() {
		return parent1;
	}

	public String getParent2() {
		return parent2;
	}
	
	public String getTruncParent1() {
		return parent1 != null ?
			parent1.length() > 25 ?
				parent1.substring(0, 25) + "..." :
				parent1
			: "";
	}

	public String getTruncParent2() {
		return parent2 != null ?
				parent2.length() > 25 ?
					parent2.substring(0, 25) + "..." :
					parent2
				: "";
	}

	public String getSiblings() {
		return siblings;
	}
	
	public String getHome() {
		return home;
	}

	public String getHospital() {
		return hospital;
	}

	public String getMessage() {
		return message;
	}

	public String getHomePage() {
		return homePage;
	}

	public String getSelectedImageUrl() {
		if(imageUrls.length == 0) return imageBaseUrl + "/no-image.jpg";
		return imageUrls[selectedImage];
	}
	
	public String getSelectedImageComment() {
		return selectedImageComment;
	}
	
	public String [] getStyle() {
		return styles;
	}
	
	public String [] getImageUrls() {
		return imageUrls;
	}

}
