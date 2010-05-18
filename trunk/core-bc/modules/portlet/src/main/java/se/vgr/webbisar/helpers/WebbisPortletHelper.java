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
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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

    public static final String WEBBIS_SESSION_PREFIX = "webbisForm.";
    public static final String WEBBIS_INDEX_PREFIX = "w";
    public static int MAX_NO_OF_MULTIPLE_BIRTH_SIBLINGS = 3; // Handle triplets

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

                        // Add temp image
                        String suffix = item.getName().substring(item.getName().indexOf('.'));
                        String filename = generateGUID() + suffix;
                        fileHandler.writeTempFile(filename, session.getId(), item.getInputStream());
                        imageFiles.add("temp/" + session.getId() + "/" + filename);

                        // Check if "main" or multiple birth sibling webbis
                        Integer webbisIndex = 0;
                        if (session.getAttribute(WEBBIS_SESSION_PREFIX + "webbisIndex") != null) {
                            webbisIndex = (Integer) session.getAttribute(WEBBIS_SESSION_PREFIX + "webbisIndex");
                        }

                        // Set temp path on webbis
                        setImageOnWebbisInSession(webbisIndex, session, filename);

                        // If main image not set
                        if (!isMainImageSet(webbisIndex, request)) {
                            setMainImage(webbisIndex, Integer.parseInt(item.getFieldName().substring(
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

    private void setImageOnWebbisInSession(Integer webbisIndex, PortletSession session, String filename) {
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(WEBBIS_SESSION_PREFIX
                + "mainWebbisBean");

        Webbis webbis = null;
        if (webbisIndex == 0) {
            webbis = mainWebbisBean.getMainWebbis();
        } else {
            webbis = mainWebbisBean.getMultipleBirthWebbisSiblings().get(webbisIndex - 1);
        }
        Image image = new Image();
        image.setLocation("temp/" + session.getId() + "/" + filename);
        webbis.getImages().add(image);

        session.setAttribute(WEBBIS_SESSION_PREFIX + "mainWebbisBean", mainWebbisBean);
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
    public void saveWebbisFormInSession(ActionRequest request, Integer webbisIndex) {
        PortletSession session = request.getPortletSession(true);

        // Cache webbis info
        Webbis webbis = parseWebbisInfo(request);
        putWebbisDataInSession(session, webbis);

        // Cache info about number of availabe images etc
        // Check if we got any sibling, if no index supplied we assume it is the "main" webbis
        ArrayList<String> availableImageIds = new ArrayList<String>();
        if (webbisIndex == null) {
            webbisIndex = 0;
        }
        for (int i = 0; i <= MAX_NO_OF_MULTIPLE_BIRTH_SIBLINGS; i++) {
            if (request.getParameter(WEBBIS_INDEX_PREFIX + webbisIndex + "_image" + i) == null) {
                availableImageIds.add(WEBBIS_INDEX_PREFIX + webbisIndex + "_image" + i);
            }
        }
        session.setAttribute(WEBBIS_SESSION_PREFIX + "availableImageIds", availableImageIds);
        session.setAttribute(WEBBIS_SESSION_PREFIX + "webbisIndex", webbisIndex);
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

        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(WEBBIS_SESSION_PREFIX
                + "mainWebbisBean");

        Webbis webbis = null;
        if (webbisIndex == 0) {
            webbis = mainWebbisBean.getMainWebbis();
        } else {
            webbis = mainWebbisBean.getMultipleBirthWebbisSiblings().get(webbisIndex - 1);
        }

        if (webbis.getImages() != null && webbis.getImages().size() > imageNumber) {
            int index = 0;
            Iterator itr = webbis.getImages().iterator();
            while (itr.hasNext()) {
                itr.next(); // We don't need the object...
                if (index == imageNumber) {
                    itr.remove();
                    break;
                }
                index++;
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
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(WEBBIS_SESSION_PREFIX
                + "mainWebbisBean");

        mainWebbisBean.getSelectedMainImages()[webbisIndex] = WEBBIS_INDEX_PREFIX + webbisIndex + "_image"
                + imageNumber;

        session.setAttribute(WEBBIS_SESSION_PREFIX + "mainWebbisBean", mainWebbisBean);
    }

    /**
     * Checks if the main image has been set in the session.
     * 
     * @param request
     *            the ActionRequest
     * @return true of the main image has been set in the session
     */
    private boolean isMainImageSet(int webbisIndex, ActionRequest request) {
        PortletSession session = request.getPortletSession(true);
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(WEBBIS_SESSION_PREFIX
                + "mainWebbisBean");
        Object mainImage = mainWebbisBean.getSelectedMainImages()[webbisIndex];
        return ((mainImage != null) && (mainImage.toString().startsWith(WEBBIS_INDEX_PREFIX + webbisIndex
                + "_image")));

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

        // Parse info from request
        Webbis mainWebbis = parseWebbisInfo(request);

        // Validate input
        if (mainWebbis.getHospital() == null) {
            validationMessages.add("Sjukhus måste väljas ur listan");
        }
        InvalidValue[] invalidValues = webbisValidator.getInvalidValues(mainWebbis);
        for (InvalidValue invalidValue : invalidValues) {
            validationMessages.add(createInvalidMessage(invalidValue));
        }
        // Throw if any validation failed
        if (validationMessages.size() != 0) {
            throw new WebbisValidationException(mainWebbis, validationMessages);
        }

        return mainWebbis;
    }

    private Webbis parseWebbisInfo(ActionRequest request) {

        // Common info first, shared between multiple birth siblings (if any)
        Hospital hospital = null;
        try {
            hospital = Hospital.fromLongString(request.getParameter("hospital"));
        } catch (ParseException e) {
        }

        String home = htmlStrip(request.getParameter("hometown"));
        List<Name> parents = new ArrayList<Name>();
        parseParentName(request, parents, "parentfname1", "parentlname1");
        parseParentName(request, parents, "parentfname2", "parentlname2");

        String siblings = htmlStrip(request.getParameter("siblings"));

        String email = htmlStrip(request.getParameter("e-mail"));
        String message = parseMessage(request.getParameter("message"));
        String webpage = request.getParameter("webpage");

        // Webbis multiple birth sibling loop, handling any twins/triplets as well
        Webbis mainWebbis = null;
        List<Webbis> multipleBirthSiblings = new ArrayList<Webbis>();
        for (int idx = 0; idx < MAX_NO_OF_MULTIPLE_BIRTH_SIBLINGS; idx++) {
            // Check if we got any sibling
            if (request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_webbisId") == null) {
                break;
            }

            String webbisId = htmlStrip(request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_webbisId"));
            String webbisName = parseName(request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_webbisname"));
            Sex webbisGender;
            if ("Female".equals(request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_gender"))) {
                webbisGender = Sex.Female;
            } else {
                webbisGender = Sex.Male;
            }
            int webbisYear = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_year");
            int webbisMonth = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_month");
            int webbisDay = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_day");
            int webbisHour = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_hour");
            int webbisMinute = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_min");
            BirthTime webbisBirthTime = new BirthTime(webbisYear, webbisMonth, webbisDay, webbisHour, webbisMinute);

            int webbisWeight = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_weight");
            int webbisLength = parseInt(request, WEBBIS_INDEX_PREFIX + idx + "_length");

            List<Image> webbisImages = new ArrayList<Image>();

            // Get index of main image
            int webbisMainImage = (request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_main-image") == null || ""
                    .equals(request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_main-image"))) ? 0 : Integer
                    .parseInt(request.getParameter(WEBBIS_INDEX_PREFIX + idx + "_main-image").substring(8));
            // Put the main image first in the list of images
            parseImage(request, webbisImages, WEBBIS_INDEX_PREFIX + idx + "_image" + webbisMainImage);
            // Place other images after main
            for (int i = 0; i <= MAX_NO_OF_MULTIPLE_BIRTH_SIBLINGS; i++) {
                if (i != webbisMainImage) {
                    parseImage(request, webbisImages, WEBBIS_INDEX_PREFIX + idx + "_image" + i);
                }
            }

            Webbis webbis;
            if (webbisId != null && !webbisId.equals("")) {
                webbis = new Webbis(Long.parseLong(webbisId), webbisName, getUserId(request), webbisGender,
                        webbisBirthTime, webbisWeight, webbisLength, hospital, home, parents, webbisImages,
                        siblings, message, email, webpage);
            } else {
                webbis = new Webbis(webbisName, getUserId(request), webbisGender, webbisBirthTime, webbisWeight,
                        webbisLength, hospital, home, parents, webbisImages, siblings, message, email, webpage);
            }

            // Set main webbis (always index == 0) or couple multiple birth sibling and main webbis
            if (idx == 0) {
                mainWebbis = webbis;
            } else {
                webbis.setMainMultipleBirthWebbis(mainWebbis);
                multipleBirthSiblings.add(webbis);
            }
        }

        // If we got any multiple birth siblings we need to add the list to "main" webbis
        if (multipleBirthSiblings.size() > 0) {
            mainWebbis.setMultipleBirthSiblings(multipleBirthSiblings);
        }

        // move all image files to the final dir (equals todays date)
        // this.copyWebbisImagesToDir(ImageUtil.getDirForTodaysDate(baseDir), webbis);

        return mainWebbis;
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

        session.setAttribute(WEBBIS_SESSION_PREFIX + "mainWebbisBean", mainWebbisBean);
    }

    public MainWebbisBean getWebbisDataFromSession(PortletSession session) {
        return (MainWebbisBean) session.getAttribute(WEBBIS_SESSION_PREFIX + "mainWebbisBean");
    }

    public void populateDefaults(PortletSession session, String noOfSiblings) {
        Webbis webbis = null;

        if (session.getAttribute(WEBBIS_SESSION_PREFIX + "mainWebbisBean") == null) {
            BirthTime birthTimeDefaultToday = new BirthTime(new Date());
            List<Name> parentsDefaultEmptyList = new ArrayList<Name>();
            List<Image> imagesDefaultEmptyList = new ArrayList<Image>();

            webbis = new Webbis(null, null, Sex.Male, birthTimeDefaultToday, 0, 0, null, null,
                    parentsDefaultEmptyList, imagesDefaultEmptyList, null, null, null, null);
        } else {
            MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(WEBBIS_SESSION_PREFIX
                    + "mainWebbisBean");
            webbis = mainWebbisBean.getMainWebbis();
        }

        if (webbis != null && noOfSiblings != null) {
            webbis = addDefaultSiblingWebbisar(webbis, Integer.valueOf(noOfSiblings));
        }

        putWebbisDataInSession(session, webbis);
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

    private Webbis addDefaultSiblingWebbisar(Webbis mainWebbis, Integer noOfSiblings) {

        if (noOfSiblings != null) {
            BirthTime birthTimeDefaultToday = new BirthTime(new Date());
            List<Name> parentsDefaultEmptyList = new ArrayList<Name>();
            List<Image> imagesDefaultEmptyList = new ArrayList<Image>();

            List<Webbis> multipleBirthSiblings = new ArrayList<Webbis>();
            for (int i = 0; i < noOfSiblings; i++) {
                Webbis webbis = new Webbis(null, null, Sex.Male, birthTimeDefaultToday, 0, 0, null, null,
                        parentsDefaultEmptyList, imagesDefaultEmptyList, null, null, null, null);

                webbis.setMainMultipleBirthWebbis(mainWebbis);
                multipleBirthSiblings.add(webbis);
            }

            mainWebbis.setMultipleBirthSiblings(multipleBirthSiblings);
        }

        return mainWebbis;
    }
}
