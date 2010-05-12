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
package se.vgr.webbisar.helpers;

import static org.apache.commons.lang.StringUtils.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import se.vgr.webbisar.beans.MainWebbisBean;
import se.vgr.webbisar.beans.PreviewWebbisBean;
import se.vgr.webbisar.types.BirthTime;
import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Image;
import se.vgr.webbisar.types.Name;
import se.vgr.webbisar.types.Sex;
import se.vgr.webbisar.types.Webbis;

@SuppressWarnings("unchecked")
public class WebbisPortletHelper {

    private ClassValidator webbisValidator = new ClassValidator(Webbis.class, ResourceBundle.getBundle(
            "se.vgr.webbisar.types.ValidatorMessages", new Locale("sv")));

    public static final String WEBBIS_MAINFORM_SESSION_PREFIX = "webbis.mainform.";
    public static final String WEBBIS_SESSION_PREFIX = "webbis.";

    private String baseUrl;
    private DiskFileItemFactory diskFileItemFactory;

    private FileHandler fileHandler;

    public class WebbisValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        private List<String> validationMessages;
        private Webbis webbis;

        WebbisValidationException(Webbis webbis, List<String> validationMessages) {
            this.webbis = webbis;
            this.validationMessages = validationMessages;

        }

        public WebbisValidationException(String string) {
            validationMessages = new ArrayList<String>();
            validationMessages.add(string);
        }

        public Webbis getWebbis() {
            return webbis;
        }

        public List<String> getValidationMessages() {
            return this.validationMessages;
        }
    }

    /**
     * Constructor
     * 
     * @param baseDir
     *            the directory where images will be saved
     * @param baseUrl
     *            the base url to the image directory when accessing the files via http
     * @param imageSize
     *            the pixel-size (of the longest side) of the images are scaled to before saveing them on disk.
     * @param imageQuality
     *            the quality of the saved images. Should be a number between 0 and 100 where 100 gives the best
     *            quality.
     */
    public WebbisPortletHelper(String baseUrl, FileHandler fileHandler) {

        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + '/';
        this.diskFileItemFactory = new DiskFileItemFactory();
        this.fileHandler = fileHandler;
    }

    /**
     * This method will parse the multipart request and if the user has pressed cancel return without doing
     * anything else. If the user has pressed submit it will save all uploaded images in the temp folder on disk.
     * It will also save references to the images in the session with the prefix 'webbis.mainform.'.
     * 
     * @param request
     *            the ActionRequest object
     */
    public List<String> parseAndSaveImages(ActionRequest request) throws WebbisValidationException {
        try {
            PortletSession session = request.getPortletSession(true);
            PortletFileUpload upload = new PortletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = upload.parseRequest(request);
            // first check which button was clicked; submit or cancel
            for (FileItem item : fileItems) {
                if (item.isFormField()) {
                    if (item.getFieldName().equals("cancelAddImages")) {
                        // do nothing more - the user has pressed 'cancel'.
                        return Collections.EMPTY_LIST;
                    }
                }
            }
            List<String> imageFiles = new ArrayList<String>();
            // parse and save the images on the temp area
            for (FileItem item : fileItems) {
                if (!item.isFormField()) {

                    if (item.getSize() > 0) {
                        if (!item.getContentType().startsWith("image")) {
                            throw new WebbisValidationException(
                                    "Bildfilen måste vara av typen JPeg, GIF eller PNG");
                        }

                        String suffix = item.getName().substring(item.getName().indexOf('.'));
                        String filename = generateGUID() + suffix;
                        fileHandler.writeTempFile(filename, session.getId(), item.getInputStream());
                        imageFiles.add("temp/" + session.getId() + "/" + filename);

                        session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + item.getFieldName(), baseUrl
                                + "temp/" + session.getId() + "/" + filename);

                        if (!isMainImageSet(request)) {
                            // TODO: AndersB - Handle multiple sibling!
                            setMainImage(0, Integer.parseInt(item.getFieldName().substring(
                                    item.getFieldName().length() - 1)), request);
                        }
                    }

                }
            }
            return imageFiles;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FTPException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileUploadException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This will remove all webbis-specific data from the session (i.e. everything that starts with 'webbis').
     * 
     * @param session
     *            the portlet session
     */
    public void cleanUp(PortletSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            if (name.startsWith(WEBBIS_SESSION_PREFIX)) {
                session.removeAttribute(name);
            }
        }
    }

    /**
     * This method will save all main webbis form data in the session with the prefix 'webbis.mainform.'. It will
     * also store an arraylist called 'webbis.availableImageIds' with all image id form fields (i.e. 'image1'
     * through 'image5') that have not yet been "taken".
     * 
     * TODO find a better way to handle images (i.e. than availableImageIds..).
     * 
     * @param request
     *            the ActionRequest object
     */
    public void saveWebbisFormInSession(ActionRequest request) {
        PortletSession session = request.getPortletSession(true);
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String nextName = paramNames.nextElement();
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + nextName, request.getParameter(nextName));
        }
        ArrayList<String> availableImageIds = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            if (request.getParameter("image" + i) == null) {
                availableImageIds.add("image" + i);
            }
        }

        session.setAttribute(WEBBIS_SESSION_PREFIX + "availableImageIds", availableImageIds);
    }

    /**
     * This method will remove all info corresponding to a certain image number from the session. Note: it will not
     * remove any files from the file system as this should be done when publishing the webbis.
     * 
     * @param imageNumber
     *            - the image to remove
     */
    public void removeImage(int webbisIndex, int imageNumber, ActionRequest request) {
        PortletSession session = request.getPortletSession(true);

        session.removeAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "image" + imageNumber);
        session.removeAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "image" + imageNumber + "-text");
        ArrayList availableImageIds = (ArrayList) session.getAttribute(WEBBIS_SESSION_PREFIX + "w" + webbisIndex
                + "availableImageIds");
        availableImageIds.add("w" + webbisIndex + "image" + imageNumber);
        session.setAttribute(WEBBIS_SESSION_PREFIX + "w" + webbisIndex + "availableImageIds", availableImageIds);

        if (session.getAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "w" + webbisIndex + "main-image").equals(
                "w" + webbisIndex + "image" + imageNumber)) {
            // set the first available image as the main image
            for (int i = 1; i <= 5; i++) {
                if (session.getAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "w" + webbisIndex + "image" + i) != null) {
                    session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "w" + webbisIndex + "main-image", "w"
                            + webbisIndex + "image" + i);
                    break;
                }
            }
        }
    }

    /**
     * This method will set the main image in the session.
     * 
     * @param imageNumber
     *            the image to set as the main image
     */
    public void setMainImage(int webbisIndex, int imageNumber, ActionRequest request) {
        PortletSession session = request.getPortletSession(true);
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(WEBBIS_MAINFORM_SESSION_PREFIX
                + "mainWebbisBean");

        mainWebbisBean.getSelectedMainImages()[webbisIndex] = "w" + webbisIndex + "_image" + imageNumber;

        session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "mainWebbisBean", mainWebbisBean);
    }

    /**
     * Checks if the main image has been set in the session.
     * 
     * @param request
     *            the ActionRequest
     * @return true of the main image has been set in the session
     */
    private boolean isMainImageSet(ActionRequest request) {
        Object mainImage = request.getPortletSession(true).getAttribute(
                WEBBIS_MAINFORM_SESSION_PREFIX + "main-image");
        return ((mainImage != null) && (mainImage.toString().startsWith("image")));

    }

    private String htmlStrip(String strWithHtml) {
        if (strWithHtml == null) {
            return null;
        } else {
            return strWithHtml.replaceAll("<", "").replaceAll(">", "");// ta bort alla html-tecken
        }
    }

    /**
     * Creates a Webbis from the input request parameters. This method will also move the images from the temp area
     * to their final destination before setting the links to the images in the Webbis. All links in the webbis
     * will be saved relative to the baseUrl/baseDir, i.e. baseUrl/baseDir will not be included.
     * 
     * @param request
     *            the ActionRequest
     * @return a Webbis ready for saving in the database
     */
    public Webbis createWebbis(ActionRequest request) throws WebbisValidationException {
        List<String> validationMessages = new ArrayList<String>();

        String id = htmlStrip(request.getParameter("webbisId"));
        String name = parseName(request.getParameter("webbisname"));
        Sex sex;
        if ("Female".equals(request.getParameter("gender"))) {
            sex = Sex.Female;
        } else {
            sex = Sex.Male;
        }
        int year = parseInt(request, "year");
        int month = parseInt(request, "month");
        int day = parseInt(request, "day");
        int hour = parseInt(request, "hour");
        int minute = parseInt(request, "min");
        BirthTime birthTime = new BirthTime(year, month, day, hour, minute);

        int weight = parseInt(request, "weight");
        int length = parseInt(request, "length");

        Hospital hospital = null;
        try {
            hospital = Hospital.fromLongString(request.getParameter("hospital"));
        } catch (ParseException e) {
            validationMessages.add("Sjukhus måste väljas ur listan");
        }

        String home = htmlStrip(request.getParameter("hometown"));
        List<Name> parents = new ArrayList<Name>();
        parseParentName(request, parents, "parentfname1", "parentlname1");
        parseParentName(request, parents, "parentfname2", "parentlname2");

        List<Image> images = new ArrayList<Image>();

        // put the main image first in the list of images

        int mainImage = (request.getParameter("main-image") == null || "".equals(request
                .getParameter("main-image"))) ? 1 : Integer.parseInt(request.getParameter("main-image").substring(
                5));
        for (int i = mainImage; i <= 4; i++) {
            parseImage(request, images, "image" + i);
        }
        for (int i = 1; i < mainImage; i++) {
            parseImage(request, images, "image" + i);
        }

        String siblings = htmlStrip(request.getParameter("siblings"));

        String email = htmlStrip(request.getParameter("e-mail"));
        String message = parseMessage(request.getParameter("message"));
        String webpage = request.getParameter("webpage");

        // TODO: AndersB - Handle birth siblings!

        Webbis webbis;
        if (id != null && !id.equals("")) {
            webbis = new Webbis(Long.parseLong(id), name, getUserId(request), sex, birthTime, weight, length,
                    hospital, home, parents, images, siblings, message, email, webpage);
        } else {
            webbis = new Webbis(name, getUserId(request), sex, birthTime, weight, length, hospital, home, parents,
                    images, siblings, message, email, webpage);
        }

        // move all image files to the final dir (equals todays date)
        // this.copyWebbisImagesToDir(ImageUtil.getDirForTodaysDate(baseDir), webbis);

        InvalidValue[] invalidValues = webbisValidator.getInvalidValues(webbis);
        for (InvalidValue invalidValue : invalidValues) {
            validationMessages.add(createInvalidMessage(invalidValue));
        }
        if (validationMessages.size() != 0) {
            throw new WebbisValidationException(webbis, validationMessages);
        }
        return webbis;

    }

    static class FieldMap {
        static final Map<String, String> fieldMap = new HashMap<String, String>();
        static {
            fieldMap.put("name", "Webbisens namn");
            fieldMap.put("birthTime", "Födelsedatum");
            fieldMap.put("weight", "Vikt");
            fieldMap.put("length", "Längd");
            fieldMap.put("parents", "Förälder");
            fieldMap.put("parents[0].firstName", "Första förälderns förnamn");
            fieldMap.put("parents[0].lastName", "Första förälderns efternamn");
            fieldMap.put("parents[1].firstName", "Andra förälderns förnamn");
            fieldMap.put("parents[1].lastName", "Andra förälderns efternamn");
            fieldMap.put("siblings", "Syskon");
            fieldMap.put("home", "Hemort");
            fieldMap.put("hospital", "Sjukhus");
            fieldMap.put("message", "Meddelande");
            fieldMap.put("homePage", "Hemsida");
            fieldMap.put("email", "Epost");
            fieldMap.put("images[0].text", "Första bildens text");
            fieldMap.put("images[1].text", "Andra bildens text");
            fieldMap.put("images[2].text", "Tredje bildens text");
            fieldMap.put("images[3].text", "Fjärde bildens text");
        }

        static final String get(String key) {
            return fieldMap.get(key);
        }
    }

    private String createInvalidMessage(InvalidValue invalidValue) {
        String property = FieldMap.get(invalidValue.getPropertyPath());
        property = property != null ? property : invalidValue.getPropertyPath();
        return property + " " + invalidValue.getMessage();
    }

    private String parseName(String name) {
        name = htmlStrip(strip(name));
        if ("skriv namn".equalsIgnoreCase(name)) {
            name = "";
        }
        return name;
    }

    protected String parseMessage(String message) {
        message = htmlStrip(message);
        List<String> result = new ArrayList<String>();
        String[] words = split(message);
        words = stripAll(words);
        for (String w : words) {
            List<String> wl = new ArrayList<String>();
            int splitIn = w.length() / 17;
            if (splitIn > 0) {
                for (int i = 0; i <= splitIn; i++) {
                    String s = substring(w, i * 17, (i + 1) * 17);
                    if (i != splitIn) {
                        s += "-";
                    }
                    wl.add(s);
                }
            } else {
                wl.add(w);
            }
            result.addAll(wl);
        }
        return join(result, " ");
    }

    /**
     * Returns the userId of the logged in user.
     * 
     * TODO remove hard coding of userId!
     * 
     * @param request
     *            the PortletRequest
     * @return a String with the userId
     * @throws RuntimeException
     *             if the userId cannot be found.
     */
    public String getUserId(PortletRequest request) {
        String userId = null;
        if (request.getUserPrincipal() == null) {
            // throw new RuntimeException("User not logged in properly");
            userId = "9322496207"; // TODO: Remove!
        } else {
            userId = request.getUserPrincipal().getName();
            if (userId == null) {
                throw new RuntimeException("User not logged in properly");
            }
        }
        return userId;
    }

    /**
     * This method parses an image from the input request data and adds it to the list of images. The location of
     * the image is set to a relative link (without baseDir/baseUrl).
     * 
     * @param request
     * @param images
     *            the list of images
     * @param imageParamName
     *            e.g. 'image1'
     */
    private void parseImage(ActionRequest request, List<Image> images, String imageParamName) {
        if (request.getParameter(imageParamName) != null) {
            images.add(new Image(request.getParameter(imageParamName).replace(baseUrl, ""), htmlStrip(request
                    .getParameter(imageParamName + "-text"))));
        }
    }

    /**
     * This method parses a parents name from the input request data and adds it to the list of names.
     * 
     * @param request
     * @param parents
     *            the list of parent names
     * @param firstNameParam
     * @param lastNameParam
     */
    private void parseParentName(ActionRequest request, List<Name> parents, String firstNameParam,
            String lastNameParam) {
        String firstName = htmlStrip(request.getParameter(firstNameParam));
        String lastName = htmlStrip(request.getParameter(lastNameParam));

        // User has not edited fields
        if ("förnamn".equalsIgnoreCase(firstName)) {
            firstName = "";
        }
        if ("efternamn".equalsIgnoreCase(lastName)) {
            lastName = "";
        }

        // A first name is enough
        if (isNotBlank(firstName)) {
            parents.add(new Name(firstName, lastName));
        }
    }

    /**
     * Parses an integer input field. Will return 0 if the request parameter is null or ''.
     * 
     * @param request
     * @param paramName
     * @return an int
     */
    private int parseInt(ActionRequest request, String paramName) {
        int i = 0;
        if (request.getParameter(paramName) != null && !request.getParameter(paramName).equals("")) {
            i = Integer.parseInt(request.getParameter(paramName));
        }
        return i;
    }

    /**
     * This method will generate a unique id.
     * 
     * @return a unique id
     */
    private String generateGUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * This will store the user's webbisar in the session. This is used between showing the link-list of
     * "my webbisar" and the edit webbis main page (when the user has selected a webbis to edit). The list is also
     * used when deleting a webbis or upon update to retrieve the "old" webbis' images.
     * 
     * @param request
     * @param webbisar
     *            a List<Webbis> of webbisar
     */
    public void storeMyWebbisarInSession(PortletRequest request, List<Webbis> webbisar) {
        request.getPortletSession(true).setAttribute(WEBBIS_SESSION_PREFIX + "myWebbisar", webbisar);

    }

    /**
     * This method will put each individual field from the webbis in the session with the prefix 'webbis.mainform'.
     * This is used for easy showing on the main edit webbis page (which retrieves values from the session).
     * 
     * @param session
     * @param Webbis
     *            the webbis for which each individual value will be stored in the session with the prefix
     *            'webbis.mainform'
     */
    public void putWebbisDataInSession(PortletSession session, Webbis webbis) {

        MainWebbisBean mainWebbisBean = new MainWebbisBean(webbis, baseUrl);

        // TODO: AndersB - Handle main images!!!

        session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "mainWebbisBean", mainWebbisBean);

        // the first image in the list is always the main image (when read from database, can be changed in
        // session).

        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "webbisId", webbis.getId());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "webbisname", webbis.getName());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "gender", webbis.getSex().toString());
        //
        // BirthTime time = webbis.getBirthTime();
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "day", time.getDay());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "month", time.getMonth());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "year", time.getYear());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "hour", time.getHour());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "min", time.getMinutes());
        //
        // if (webbis.getWeight() != 0) {
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "weight", webbis.getWeight());
        // }
        // if (webbis.getLength() != 0) {
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "length", webbis.getLength());
        // }
        //
        // List<Name> parents = webbis.getParents();
        // int i = 1;
        // for (Name name : parents) {
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "parentfname" + i, name.getFirstName());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "parentlname" + i, name.getLastName());
        // i++;
        // }
        //
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "siblings", webbis.getSiblings());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "hospital", webbis.getHospital() != null ? webbis
        // .getHospital().getLongName() : "");
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "hometown", webbis.getHome());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "message", webbis.getMessage());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "webpage", webbis.getHomePage());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "e-mail", webbis.getEmail());
        //
        // List<Image> images = webbis.getImages();
        // i = 1;
        // for (Image image : images) {
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "image" + i, baseUrl + image.getLocation());
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "image" + i + "-text", image.getText());
        // i++;
        // }
        // // the first image in the list is always the main image.
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "main-image", "image1");
        //
        // // TODO: AndersB - Testing multipleBirthSiblings
        // session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "multipleBirthSiblings", webbis
        // .getMultipleBirthSiblings());
    }

    public void populateDefaults(PortletSession session) {
        Calendar cal = new GregorianCalendar();
        if (session.getAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "day") == null) {
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "day", cal.get(Calendar.DAY_OF_MONTH));
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "month", cal.get(Calendar.MONTH) + 1);
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "year", cal.get(Calendar.YEAR));
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "hour", cal.get(Calendar.HOUR_OF_DAY));
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "min", cal.get(Calendar.MINUTE));
        }
        if (session.getAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "gender") == null) {
            session.setAttribute(WEBBIS_MAINFORM_SESSION_PREFIX + "gender", "Male");
        }
    }

    public Webbis getWebbisFromSession(PortletSession session) {
        return (Webbis) session.getAttribute(WEBBIS_SESSION_PREFIX + "webbis");
    }

    public void saveWebbisInSession(PortletSession session, Webbis webbis) {
        session.setAttribute(WEBBIS_SESSION_PREFIX + "webbis", webbis);
    }

    public PreviewWebbisBean createPreviewWebbisBean(ActionRequest request, Webbis webbis) {
        return new PreviewWebbisBean(baseUrl, webbis, 0);
    }

}
