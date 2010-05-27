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

package se.vgregion.webbisar.presentation;

import static org.apache.commons.lang.StringUtils.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.vgregion.webbisar.types.Image;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;

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
    private String[] imageUrls;
    private String message;
    private String homePage;
    private int selectedImage;
    private String selectedImageComment;
    private String[] styles = new String[] { "selected", "notselected", "notselected", "notselected" };
    private String imageBaseUrl;
    private Webbis mainMultipleBirthWebbis;
    private List<Webbis> multipleBirthSiblings;

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
        for (Image image : webbis.getImages()) {
            this.imageUrls[cnt++] = imageBaseUrl + "/" + image.getLocation();
        }
        this.selectedImage = setSelectedImage(selectedImage);
        if (webbis.getImages().size() > 0) {
            this.selectedImageComment = webbis.getImages().get(selectedImage).getText();
        }
        this.homePage = webbis.getHomePage();
        this.message = webbis.getMessage();
        this.imageBaseUrl = imageBaseUrl;
        this.mainMultipleBirthWebbis = webbis.getMainMultipleBirthWebbis();
        this.multipleBirthSiblings = webbis.getMultipleBirthSiblings();
    }

    public WebbisBean(String imageBaseUrl, Webbis webbis) {
        this(imageBaseUrl, webbis, 0);
    }

    private int setSelectedImage(int selectedImage) {
        styles = new String[] { "notselected", "notselected", "notselected", "notselected" };
        styles[selectedImage] = "selected";
        return selectedImage;
    }

    private String generateName(Webbis webbis) {
        return isNotEmpty(webbis.getName()) ? webbis.getName() : webbis.getSex() == Sex.Female ? "Flicka"
                : "Pojke";
    }

    private String generateSmartTime(Webbis webbis) {
        return "Född " + webbis.getBirthTime().getSmartTime(new Date());
    }

    private String generateHeader(Webbis webbis) {
        String name = generateName(webbis);

        String date = webbis.getBirthTime().getSmartTime(new Date());

        String parents = getParentsNames(webbis);

        StringBuffer sb = new StringBuffer().append(name).append(" född ").append(date);
        if (isNotEmpty(parents)) {
            sb.append(", ").append(parents);
        }
        return sb.toString();
    }

    private String generateTruncHeader(Webbis webbis) {
        String name = generateName(webbis);

        String date = webbis.getBirthTime().getSmartTime(new Date());

        String parents = getParentsNames(webbis);

        StringBuffer sb = new StringBuffer().append(name).append(" född ").append(date);
        if (isNotEmpty(parents) && parents.length() < 25) {
            sb.append(", ").append(parents);
        }
        return sb.toString();

    }

    private String getParentsNames(Webbis webbis) {
        switch (webbis.getParentsFirstNames().size()) {
            case 1:
                return webbis.getParentsFirstNames().get(0);
            case 2:
                return webbis.getParentsFirstNames().get(0) + " och " + webbis.getParentsFirstNames().get(1);
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
        return parent1 != null ? parent1.length() > 25 ? parent1.substring(0, 25) + "..." : parent1 : "";
    }

    public String getTruncParent2() {
        return parent2 != null ? parent2.length() > 25 ? parent2.substring(0, 25) + "..." : parent2 : "";
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
        if (imageUrls.length == 0) {
            return imageBaseUrl + "/no-image.jpg";
        }
        return imageUrls[selectedImage];
    }

    public String getSelectedImageComment() {
        return selectedImageComment;
    }

    public String[] getStyle() {
        return styles;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public int getMultipleBirthSiblingCount() {
        if (mainMultipleBirthWebbis != null) {
            return mainMultipleBirthWebbis.getMultipleBirthSiblings().size();
        } else if (multipleBirthSiblings != null) {
            return multipleBirthSiblings.size();
        }
        return 0;
    }

    public String getMultipleBirthSiblingIdString() {
        StringBuilder sb = new StringBuilder();
        List<Webbis> siblingList = null;
        if (mainMultipleBirthWebbis != null) {
            siblingList = mainMultipleBirthWebbis.getMultipleBirthSiblings();
        } else if (multipleBirthSiblings != null) {
            siblingList = multipleBirthSiblings;
        }
        if (siblingList != null) {
            for (int i = 0; i < siblingList.size(); i++) {
                if (siblingList.get(i).getId() != id) {
                    sb.append(",");
                    sb.append(siblingList.get(i).getId());
                }
            }
        }
        return sb.toString();
    }

    public String getAllWebbisIdString() {
        String siblingsIds = getMultipleBirthSiblingIdString();
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        if (mainMultipleBirthWebbis != null && id != mainMultipleBirthWebbis.getId()) {
            sb.append(",");
            sb.append(mainMultipleBirthWebbis.getId());
        }
        if (siblingsIds.length() > 0) {
            sb.append(siblingsIds);
        }
        return sb.toString();
    }

    public Map<Long, String> getMultipleBirthSiblingIdsAndNames() {
        Map<Long, String> retMap = new HashMap<Long, String>();

        if (mainMultipleBirthWebbis != null) {
            // If it has a main webbis, this is a sibling

            retMap.put(mainMultipleBirthWebbis.getId(), mainMultipleBirthWebbis.getName());

            // It might have another sibling as well (triplet)
            if (mainMultipleBirthWebbis.getMultipleBirthSiblings() != null) {
                Webbis webbis = null;
                for (int i = 0; i < mainMultipleBirthWebbis.getMultipleBirthSiblings().size(); i++) {
                    if (mainMultipleBirthWebbis.getMultipleBirthSiblings().get(i).getId() != id) {
                        webbis = mainMultipleBirthWebbis.getMultipleBirthSiblings().get(i);
                        retMap.put(webbis.getId(), webbis.getName());
                    }
                }
            }
        } else if (multipleBirthSiblings != null) {
            // If this is a main webbis, it might still have siblings
            Webbis webbis = null;
            for (int i = 0; i < multipleBirthSiblings.size(); i++) {
                if (multipleBirthSiblings.get(i).getId() != id) {
                    webbis = multipleBirthSiblings.get(i);
                    retMap.put(webbis.getId(), webbis.getName());
                }
            }
        }

        return retMap;
    }
}
