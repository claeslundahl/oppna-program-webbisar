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

package se.vgregion.webbisar.helpers;

import static org.apache.commons.lang.StringUtils.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import se.vgregion.webbisar.beans.MainWebbisBean;
import se.vgregion.webbisar.beans.PreviewWebbisBean;
import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.types.MultimediaFile.MediaType;

@SuppressWarnings("unchecked")
public class WebbisPortletHelper {

    private ClassValidator webbisValidator = new ClassValidator(Webbis.class, ResourceBundle.getBundle(
            "se.vgregion.webbisar.types.ValidatorMessages", new Locale("sv")));

    public static final String SESSION_ATTRIB_KEY_PREFIX = "webbisForm.";
    public static final String SESSION_ATTRIB_KEY_WEBBIS = "webbisForm.webbis";
    public static final String SESSION_ATTRIB_KEY_WEBBIS_INDEX = "webbisForm.webbisIndex";
    public static final String SESSION_ATTRIB_KEY_MAINWEBBISBEAN = "webbisForm.mainWebbisBean";
    public static final String SESSION_ATTRIB_KEY_AVAILABLE_IMAGEIDS = "webbisForm.availableImageIds";
    public static final String SESSION_ATTRIB_KEY_AVAILABLE_VIDEOIDS = "webbisForm.availableVideoIds";
    public static final String WEBBIS_INDEX_PREFIX = "w";

    public static final int MAX_NO_OF_MEDIAFILES = 4;
    public static final int MAX_NO_OF_VIDEOS = 1;
    public static final int MAX_NO_OF_MULTIPLE_BIRTH_SIBLINGS = 3; // Handle twins and triplets

    private String baseUrl;
    private DiskFileItemFactory diskFileItemFactory;

    private FileHandler fileHandler;

    private Boolean testMode;

    private int maxVideoFileSize;

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
     * @param baseUrl
     *            the base url to the image directory when accessing the files via http
     * @param fileHandler
     *            utility handling file (transfer) operations
     * @param testMode
     *            flagging testmode, will affect e.g. validation of userId/authorId
     * @param maxVideoFileSize
     *            max allowed size for video file upload
     * 
     */
    public WebbisPortletHelper(String baseUrl, FileHandler fileHandler, Boolean testMode, int maxVideoFileSize) {

        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + '/';
        this.diskFileItemFactory = new DiskFileItemFactory();
        this.fileHandler = fileHandler;
        this.testMode = testMode;
        this.maxVideoFileSize = maxVideoFileSize;
    }

    /**
     * This method will parse the multipart request and if the user has pressed cancel return without doing
     * anything else. If the user has pressed submit it will save uploaded images/video in the temp folder on disk.
     * It will also save references to the images/video in the session with the prefix 'webbis.mainform.'.
     * 
     * @param request
     *            the ActionRequest object
     * @return List of image references or NULL if video added or user pressed cancel
     */
    public List<String> parseAndSaveMultipartFiles(ActionRequest request) throws WebbisValidationException {
        try {
            PortletSession session = request.getPortletSession(true);
            PortletFileUpload upload = new PortletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = upload.parseRequest(request);
            // first check which button was clicked; submit or cancel
            for (FileItem item : fileItems) {
                if (item.isFormField()) {
                    if (item.getFieldName().equals("cancelAddImages")) {
                        // do nothing more - the user has pressed 'cancel'.
                        return null;
                    }
                }
            }
            List<String> imageFiles = null;

            // Check if "main" or multiple birth sibling webbis
            Integer webbisIndex = 0;
            if (session.getAttribute(SESSION_ATTRIB_KEY_WEBBIS_INDEX) != null) {
                webbisIndex = (Integer) session.getAttribute(SESSION_ATTRIB_KEY_WEBBIS_INDEX);
            }

            // Validate mediafiles, e.g. size and/or number of video files
            validateMediafiles(fileItems, webbisIndex, session);

            // parse and save the images on the temp area
            for (FileItem item : fileItems) {
                if (!item.isFormField()) {

                    if (item.getSize() > 0) {
                        MediaType mediaType = MediaType.IMAGE;
                        if (item.getContentType().startsWith("video")) {
                            mediaType = MediaType.VIDEO;
                        }

                        // Add temp image
                        String contentType = item.getContentType();
                        String suffix = item.getName().substring(item.getName().lastIndexOf('.'));
                        if (suffix != null) {
                            suffix = suffix.toLowerCase();
                        }
                        String filename = generateGUID() + suffix;
                        fileHandler.writeTempFile(filename, session.getId(), item.getInputStream());
                        if (MediaType.IMAGE.equals(mediaType)) {
                            // Add to list, we need to resize later...
                            imageFiles = new ArrayList<String>();
                            imageFiles.add("temp/" + session.getId() + "/" + filename);
                        }

                        // Set temp path on webbis
                        setMediaFileOnWebbisInSession(webbisIndex, session, filename, contentType, mediaType);

                        // If main image not set
                        if (MediaType.IMAGE.equals(mediaType) && !isMainImageSet(webbisIndex, request)) {
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

    private void setMediaFileOnWebbisInSession(Integer webbisIndex, PortletSession session, String filename,
            String contentType, MediaType mediaType) {
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);

        Webbis webbis = null;
        if (webbisIndex == 0) {
            webbis = mainWebbisBean.getMainWebbis();
        } else {
            webbis = mainWebbisBean.getMultipleBirthWebbisSiblings().get(webbisIndex - 1);
        }
        MultimediaFile image = new MultimediaFile();
        image.setLocation("temp/" + session.getId() + "/" + filename);
        image.setMediaType(mediaType);
        if (contentType != null) {
            // IE sets its own jpeg mime type when uploading (!?!)
            image.setContentType(contentType.replace("pjpeg", "jpeg"));
        }
        webbis.getMediaFiles().add(image);
    }

    private void validateMediafiles(List<FileItem> fileItems, Integer webbisIndex, PortletSession session)
            throws WebbisValidationException {
        int noOfUploadedVideos = 0;
        // Check what we got...
        for (FileItem item : fileItems) {
            if (!item.isFormField()) {
                if (item.getSize() > 0) {
                    if (!item.getContentType().startsWith("image")) {
                        if (item.getContentType().startsWith("video")) {
                            // Validate size
                            if (item.getSize() > maxVideoFileSize) {
                                throw new WebbisValidationException("Videofilen " + item.getName()
                                        + " är för stor, maximalt tillåten storlek är 10MB.");
                            }
                            noOfUploadedVideos++;
                        } else {
                            // File does not seem to be image nor video
                            throw new WebbisValidationException("Filen " + item.getName()
                                    + " verkar inte vara av bild- eller videoformat.");
                        }
                    }
                }
            }
        }

        // Validate that max videos has not been reached
        int newNoOfVideosForWebbis = currentNoOfVideosForWebbis(webbisIndex, session) + noOfUploadedVideos;
        if (newNoOfVideosForWebbis > MAX_NO_OF_VIDEOS) {
            throw new WebbisValidationException("Det är inte tillåtet att ladda upp mer än en videofil.");
        }

    }

    private int currentNoOfVideosForWebbis(Integer webbisIndex, PortletSession session) {
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);

        Webbis webbis = null;
        if (webbisIndex == 0) {
            webbis = mainWebbisBean.getMainWebbis();
        } else {
            webbis = mainWebbisBean.getMultipleBirthWebbisSiblings().get(webbisIndex - 1);
        }

        int noOfVideos = 0;
        for (MultimediaFile file : webbis.getMediaFiles()) {
            if (MediaType.VIDEO.equals(file.getMediaType())) {
                noOfVideos++;
            }
        }

        return noOfVideos;
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
            if (name.startsWith(SESSION_ATTRIB_KEY_PREFIX)) {
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
        putWebbisDataInSession(session, getMainWebbisBeanForWebbis(webbis));

        // Cache info about number of available images etc
        // Check if we got any sibling, if no index supplied we assume it is the "main" webbis
        ArrayList<String> availableImageIds = new ArrayList<String>();
        if (webbisIndex == null) {
            webbisIndex = 0;
        }
        for (int i = 0; i < MAX_NO_OF_MEDIAFILES; i++) {
            if (request.getParameter(WEBBIS_INDEX_PREFIX + webbisIndex + "_mediaFile" + i) == null) {
                availableImageIds.add(WEBBIS_INDEX_PREFIX + webbisIndex + "_mediaFile" + i);
            }
        }

        session.setAttribute(SESSION_ATTRIB_KEY_AVAILABLE_IMAGEIDS, availableImageIds);
        session.setAttribute(SESSION_ATTRIB_KEY_WEBBIS_INDEX, webbisIndex);
    }

    /**
     * This method will remove all info corresponding to a certain media file number from the session. Note: it
     * will not remove any files from the file system as this should be done when publishing the webbis.
     * 
     * @param webbisIndex
     *            index of webbis (if multiple birth siblings)
     * @param fileNumber
     *            - the index of file to remove
     */
    public void removeMediaFile(int webbisIndex, int fileNumber, ActionRequest request) {
        PortletSession session = request.getPortletSession(true);

        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);

        Webbis webbis = null;
        if (webbisIndex == 0) {
            webbis = mainWebbisBean.getMainWebbis();
        } else {
            webbis = mainWebbisBean.getMultipleBirthWebbisSiblings().get(webbisIndex - 1);
        }

        List<MultimediaFile> fileList = webbis.getMediaFiles();
        if (fileList != null && fileList.size() > fileNumber) {
            int index = 0;
            Iterator itr = fileList.iterator();
            while (itr.hasNext()) {
                itr.next(); // We don't need the object...
                if (index == fileNumber) {
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
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);

        mainWebbisBean.getSelectedMainImages()[webbisIndex] = WEBBIS_INDEX_PREFIX + webbisIndex + "_mediaFile"
                + imageNumber;
    }

    /**
     * Checks if the main image has been set in the session.
     * 
     * @param webbisIndex
     *            index of webbis, if multiple birth siblings (twins/triplets)
     * @param request
     *            the ActionRequest
     * @return true of the main image has been set in the session
     */
    private boolean isMainImageSet(int webbisIndex, ActionRequest request) {
        PortletSession session = request.getPortletSession(true);
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);
        // Ensure we had a bean in session
        if (mainWebbisBean == null) {
            return false;
        }
        String[] mainImage = mainWebbisBean.getSelectedMainImages();
        // Ensure we have an array, that it has the correct length and that target position is populated
        if (mainImage == null || mainImage.length <= webbisIndex || mainImage[webbisIndex] == null) {
            return false;
        }
        return (mainImage != null && mainImage.length > webbisIndex && mainImage[webbisIndex].toString()
                .startsWith(WEBBIS_INDEX_PREFIX + webbisIndex + "_mediaFile"));

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
        for (int webbisIdx = 0; webbisIdx < MAX_NO_OF_MULTIPLE_BIRTH_SIBLINGS; webbisIdx++) {
            // Check if we got any sibling
            if (request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_webbisId") == null) {
                break;
            }

            String webbisId = htmlStrip(request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_webbisId"));
            String webbisName = parseName(request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_webbisname"));
            Sex webbisGender;
            if ("Female".equals(request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_gender"))) {
                webbisGender = Sex.Female;
            } else {
                webbisGender = Sex.Male;
            }
            int webbisYear = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_year");
            int webbisMonth = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_month");
            int webbisDay = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_day");
            int webbisHour = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_hour");
            int webbisMinute = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_min");
            BirthTime webbisBirthTime = new BirthTime(webbisYear, webbisMonth, webbisDay, webbisHour, webbisMinute);

            int webbisWeight = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_weight");
            int webbisLength = parseInt(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_length");

            List<MultimediaFile> webbisMediaFiles = new ArrayList<MultimediaFile>();

            // Get index of main image
            int webbisMainImage = (request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_main-image") == null || ""
                    .equals(request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_main-image"))) ? 0 : Integer
                    .parseInt(request.getParameter(WEBBIS_INDEX_PREFIX + webbisIdx + "_main-image").substring(12));
            // Put the main image first in the list of images
            parseMultimediaFile(request, webbisMediaFiles, WEBBIS_INDEX_PREFIX + webbisIdx + "_mediaFile"
                    + webbisMainImage);
            // Place other images after main
            for (int i = 0; i < MAX_NO_OF_MEDIAFILES; i++) {
                if (i != webbisMainImage) {
                    parseMultimediaFile(request, webbisMediaFiles, WEBBIS_INDEX_PREFIX + webbisIdx + "_mediaFile"
                            + i);
                }
            }

            Webbis webbis;
            if (webbisId != null && !webbisId.equals("")) {
                Date created = null;
                Long dateLong = parseLong(request, WEBBIS_INDEX_PREFIX + webbisIdx + "_created");
                if (dateLong != null) {
                    created = new Date(dateLong);
                }
                webbis = new Webbis(Long.parseLong(webbisId), webbisName, getUserId(request), webbisGender,
                        webbisBirthTime, webbisWeight, webbisLength, hospital, home, parents, webbisMediaFiles,
                        siblings, message, email, webpage, created);
            } else {
                webbis = new Webbis(webbisName, getUserId(request), webbisGender, webbisBirthTime, webbisWeight,
                        webbisLength, hospital, home, parents, webbisMediaFiles, siblings, message, email, webpage);
            }

            // Set main webbis (always index == 0) or couple multiple birth sibling and main webbis
            if (webbisIdx == 0) {
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
            int splitIn = w.length() / 20;
            if (splitIn > 0) {
                for (int i = 0; i <= splitIn; i++) {
                    String s = substring(w, i * 20, (i + 1) * 20);
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
     * Returns the userId of the logged in user. If testMode==TRUE we return userId = 191212121212
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
            if (Boolean.TRUE.equals(testMode)) {
                userId = "191212121212";
            } else {
                throw new RuntimeException("User not logged in properly, no user principal found in request.");
            }
        } else {
            userId = request.getUserPrincipal().getName();
            if (userId == null) {
                throw new RuntimeException("User not logged in properly, no user principal name found in request.");
            }
        }
        return userId;
    }

    /**
     * This method parses an image from the input request data and adds it to the list of multimedia files. The
     * location of the file is set to a relative link (without baseDir/baseUrl).
     * 
     * @param request
     * @param multimediaFiles
     *            the list of multimedia files
     * @param fileParamName
     *            e.g. 'w0_IMAGE1'
     */
    private void parseMultimediaFile(ActionRequest request, List<MultimediaFile> multimediaFiles,
            String fileParamName) {
        if (request.getParameter(fileParamName) != null) {
            String contentType = request.getParameter(fileParamName + "_contentType");

            MediaType mediaType = MediaType.IMAGE;
            String mediaTypeStr = request.getParameter(fileParamName + "_mediaType");
            if (!StringUtils.isEmpty(mediaTypeStr)) {
                mediaType = MediaType.valueOf(mediaTypeStr);
            }

            multimediaFiles.add(new MultimediaFile(request.getParameter(fileParamName).replace(baseUrl, ""),
                    htmlStrip(request.getParameter(fileParamName + "-text")), mediaType, contentType));
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
     * Parses an long input field. Will return null if the request parameter is null or ''.
     * 
     * @param request
     * @param paramName
     * @return a Long
     */
    private Long parseLong(ActionRequest request, String paramName) {
        Long l = null;
        if (request.getParameter(paramName) != null && !request.getParameter(paramName).equals("")) {
            l = Long.parseLong(request.getParameter(paramName));
        }
        return l;
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
     * This method will put each individual field from the webbis in the session with the prefix 'webbis.mainform'.
     * This is used for easy showing on the main edit webbis page (which retrieves values from the session).
     * 
     * @param session
     * @param MainWebbisBean
     *            the webbisBean for which each individual value will be stored in the session with the prefix
     *            'webbis.mainform'
     */
    public void putWebbisDataInSession(PortletSession session, MainWebbisBean mainWebbisBean) {
        session.setAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN, mainWebbisBean);
    }

    public MainWebbisBean getWebbisDataFromSession(PortletSession session) {
        return (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);
    }

    public void populateDefaults(PortletSession session, String noOfSiblings) {
        MainWebbisBean mainWebbisBean = (MainWebbisBean) session.getAttribute(SESSION_ATTRIB_KEY_MAINWEBBISBEAN);

        if (mainWebbisBean == null) {
            BirthTime birthTimeDefaultToday = new BirthTime(new Date());
            List<Name> parentsDefaultEmptyList = new ArrayList<Name>();
            List<MultimediaFile> mediaFileDefaultEmptyList = new ArrayList<MultimediaFile>();

            Webbis webbis = new Webbis(null, null, Sex.Male, birthTimeDefaultToday, 0, 0, null, null,
                    parentsDefaultEmptyList, mediaFileDefaultEmptyList, null, null, null, null);

            mainWebbisBean = getMainWebbisBeanForWebbis(webbis);
        }

        // Check if we should add sibling objects
        if (noOfSiblings != null) {
            addDefaultSiblingWebbisarOnMain(mainWebbisBean, Integer.valueOf(noOfSiblings));
        }

        // Put mainWebbisBean (back) into session
        putWebbisDataInSession(session, mainWebbisBean);
    }

    public Webbis getWebbisFromSession(PortletSession session) {
        return (Webbis) session.getAttribute(SESSION_ATTRIB_KEY_WEBBIS);
    }

    public void saveWebbisInSession(PortletSession session, Webbis webbis) {
        session.setAttribute(SESSION_ATTRIB_KEY_WEBBIS, webbis);
    }

    public PreviewWebbisBean createPreviewWebbisBean(ActionRequest request, Webbis webbis) {
        return new PreviewWebbisBean(baseUrl, webbis, 0);
    }

    public MainWebbisBean getMainWebbisBeanForWebbis(Webbis webbis) {
        return new MainWebbisBean(webbis, baseUrl);
    }

    private void addDefaultSiblingWebbisarOnMain(MainWebbisBean mainWebbisBean, Integer noOfSiblings) {

        if (noOfSiblings != null) {
            BirthTime birthTimeDefaultToday = new BirthTime(new Date());
            List<Name> parentsDefaultEmptyList = new ArrayList<Name>();
            List<MultimediaFile> mediaFileDefaultEmptyList = new ArrayList<MultimediaFile>();

            List<Webbis> multipleBirthSiblings = new ArrayList<Webbis>();
            for (int i = 0; i < noOfSiblings; i++) {
                Webbis webbis = new Webbis(null, null, Sex.Male, birthTimeDefaultToday, 0, 0, null, null,
                        parentsDefaultEmptyList, mediaFileDefaultEmptyList, null, null, null, null);

                webbis.setMainMultipleBirthWebbis(mainWebbisBean.getMainWebbis());
                multipleBirthSiblings.add(webbis);
            }

            mainWebbisBean.setMultipleBirthWebbisSiblings(multipleBirthSiblings);
            mainWebbisBean.getMainWebbis().setMultipleBirthSiblings(multipleBirthSiblings);
        }
    }
}
