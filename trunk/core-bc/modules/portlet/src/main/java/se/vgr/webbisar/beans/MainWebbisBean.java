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
package se.vgr.webbisar.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Name;
import se.vgr.webbisar.types.Webbis;

public class MainWebbisBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Webbis mainWebbis;

    private List<Webbis> multipleBirthWebbisSiblings;

    private Name parent1;
    private Name parent2;
    private String siblings;
    private String homeTown;
    private Hospital hospital;
    private String message;
    private String webPage;
    private String email;

    private String[] selectedMainImages = new String[10];
    private String imageBaseUrl;
    private String userId;

    public MainWebbisBean(Webbis webbis, String imageBaseUrl) {
        this.mainWebbis = webbis;
        this.multipleBirthWebbisSiblings = webbis.getMultipleBirthSiblings();

        List<Name> parents = webbis.getParents();
        if (parents != null) {
            if (parents.size() > 0) {
                this.parent1 = parents.get(0);
            }
            if (parents.size() > 1) {
                this.parent2 = parents.get(1);
            }
        }
        this.siblings = webbis.getSiblings();
        this.homeTown = webbis.getHome();
        this.webPage = webbis.getHomePage();
        this.hospital = webbis.getHospital();
        this.email = webbis.getEmail();
        this.message = webbis.getMessage();

        // Set selected main image to first image for now. May be changed in session, but when saved the selection
        // will be sorted as first image in list. Named as image1 or (birth sibling) [index]image1 in jsp.
        if (mainWebbis.getImages() != null && mainWebbis.getImages().size() > 0) {
            this.selectedMainImages[0] = "w0_image0";
        }
        if (multipleBirthWebbisSiblings != null) {
            for (int i = 0; i < multipleBirthWebbisSiblings.size(); i++) {
                Webbis w = multipleBirthWebbisSiblings.get(i);
                if (w.getImages() != null && w.getImages().size() > 0) {
                    int relativeIndex = i + 1;
                    this.selectedMainImages[relativeIndex] = "w" + relativeIndex + "_image0";
                }
            }
        }

        this.imageBaseUrl = imageBaseUrl;
        this.userId = webbis.getAuthorId();
    }

    public Webbis getMainWebbis() {
        return mainWebbis;
    }

    public void setMainWebbis(Webbis mainWebbis) {
        this.mainWebbis = mainWebbis;
    }

    public List<Webbis> getMultipleBirthWebbisSiblings() {
        return multipleBirthWebbisSiblings;
    }

    public void setMultipleBirthWebbisSiblings(List<Webbis> multipleBirthWebbisSiblings) {
        this.multipleBirthWebbisSiblings = multipleBirthWebbisSiblings;
    }

    public Name getParent1() {
        return parent1;
    }

    public void setParent1(Name parent1) {
        this.parent1 = parent1;
    }

    public Name getParent2() {
        return parent2;
    }

    public void setParent2(Name parent2) {
        this.parent2 = parent2;
    }

    public String getSiblings() {
        return siblings;
    }

    public void setSiblings(String siblings) {
        this.siblings = siblings;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String home) {
        this.homeTown = home;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String homePage) {
        this.webPage = homePage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public String[] getSelectedMainImages() {
        return selectedMainImages;
    }

    public void setSelectedMainImages(String[] mainImages) {
        this.selectedMainImages = mainImages;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Name> getParents() {
        List<Name> parents = new ArrayList<Name>();
        if (parent1 != null) {
            parents.add(parent1);
        }
        if (parent2 != null) {
            parents.add(parent2);
        }
        return parents;
    }

    public boolean isHasMultipleBirthSiblings() {
        // Webbis is a sibling
        if (mainWebbis.getMainMultipleBirthWebbis() != null) {
            return true;
        }
        // Webbis is a main sibling
        if (multipleBirthWebbisSiblings != null && multipleBirthWebbisSiblings.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Helper function, only to be used on "main" webbis if multiple birth siblings.
     * 
     * @return String listing webbis names (Alfa, Beta)
     */
    public String getAllWebbisNamesStringFromMain() {
        StringBuilder sb = new StringBuilder();
        if (mainWebbis != null) {
            sb.append(mainWebbis.getName());
            if (multipleBirthWebbisSiblings != null) {
                for (Webbis w : multipleBirthWebbisSiblings) {
                    sb.append(", ");
                    sb.append(w.getName());
                }
            }
        }
        return sb.toString();
    }

    /**
     * Helper function, only to be used on "main" webbis if multiple birth siblings.
     * 
     * @return String Date of birth on format "19 april, 2009"
     */
    public String getBirthDateFromMain() {
        StringBuilder sb = new StringBuilder();
        if (mainWebbis != null && mainWebbis.getBirthTime() != null) {
            sb.append(mainWebbis.getBirthTime().getSmartTime(new Date()));
            sb.append(", ");
            sb.append(mainWebbis.getBirthTime().getYear());
        }
        return sb.toString();
    }
}
