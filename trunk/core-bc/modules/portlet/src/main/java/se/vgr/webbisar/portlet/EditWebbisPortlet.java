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
package se.vgr.webbisar.portlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import se.vgr.webbisar.helpers.FileHandler;
import se.vgr.webbisar.helpers.WebbisPortletHelper;
import se.vgr.webbisar.helpers.WebbisServiceProxy;
import se.vgr.webbisar.helpers.WebbisPortletHelper.WebbisValidationException;
import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Webbis;
import se.vgr.webbisar.util.CallContext;
import se.vgr.webbisar.util.CallContextUtil;

/**
 * EditWebbis Portlet Class
 * 
 * @author sofiajonsson
 */
public class EditWebbisPortlet extends GenericPortlet {

	
	// these parameters are set in the action phase. They help decide which view
	// to redirect to in the render phase
	private static final String VIEW = "VIEW";
	private static final String MAIN_VIEW = "MAIN_VIEW";
	private static final String ADD_IMAGES_VIEW = "ADD_IMAGES_VIEW";
	private static final String CONFIRMATION_VIEW = "CONFIRMATION_VIEW";
	private static final String SHOW_WEBBIS_LIST_VIEW = "SHOW_WEBBIS_LIST_VIEW";
	private static final String CONFIRM_DELETE_WEBBIS_VIEW = "CONFIRM_DELETE_WEBBIS_VIEW";
	private static final String PREVIEW_VIEW = "PREVIEW_VIEW";

	private WebbisPortletHelper helper;
	private WebbisServiceProxy webbisServiceProxy;
	
	

	public void init(PortletConfig config) throws PortletException {
		webbisServiceProxy = new WebbisServiceProxy();		
		String baseUrl = webbisServiceProxy.getImageBaseUrl();
		String ftpCfg  = webbisServiceProxy.getFtpConfig();
		
		FileHandler fileHandler = new FileHandler(ftpCfg);
		
		helper = new WebbisPortletHelper(baseUrl, fileHandler);
		
		super.init(config);
	}

	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		try{
			response.setContentType("text/html");
			PortletRequestDispatcher dispatcher = null;
			Object view = request.getParameter(VIEW);
			
	
			if ((view == null) || (view.equals(SHOW_WEBBIS_LIST_VIEW))) {
				
				// CleanUp so the session is empty
				helper.cleanUp(request.getPortletSession(true));
				
				List<Webbis> webbisar = webbisServiceProxy.getWebbisarForAuthorId(helper.getUserId(request));
				if (webbisar != null && webbisar.size() > 0) {
					helper.storeMyWebbisarInSession(request, webbisar);
					request.setAttribute("webbisar", webbisar);
					dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ShowWebbisList.jsp");
	
				} else {
					// show an empty main edit webbis page - for adding a new webbis
					helper.populateDefaults(request.getPortletSession(true));
					request.setAttribute("currentYear", new GregorianCalendar().get(Calendar.YEAR)); // this is used in the jsp
					request.setAttribute("hospitals", Hospital.values());
					dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/EditWebbis.jsp");
				}
	
			} else if (view.equals(MAIN_VIEW)) {
				helper.populateDefaults(request.getPortletSession(true));
	 			// this is used in EditWebbis.jsp TODO find a smarter way to do this, preferably directly in the jsp
				request.setAttribute("currentYear", new GregorianCalendar().get(Calendar.YEAR));
				request.setAttribute("hospitals", Hospital.values());
				// show the main edit webbis page
				dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/EditWebbis.jsp");
			} else if (view.equals(ADD_IMAGES_VIEW)) {// show add picture page
				dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/AddImages.jsp");
			} else if (view.equals(PREVIEW_VIEW)) {// show preview page
				dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/PreviewWebbis.jsp");
			} else if (view.equals(CONFIRMATION_VIEW)) {// show confirmation page
				dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/Confirmation.jsp");
			} else if (view.equals(CONFIRM_DELETE_WEBBIS_VIEW)) {// show confirm delete page
				dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ConfirmDelete.jsp");
			}
			dispatcher.include(request, response);
			}
		finally {
			//request.removeAttribute(VIEW);
			//System.out.println("removed attribute View: " + request.getAttribute(VIEW));
		}
	}

	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

		try {
			CallContextUtil.setContext(new CallContext(helper.getUserId(request)));
			
			// Check if we have a file upload request
			if (PortletFileUpload.isMultipartContent(request)) {
				// Save the images using FTP.
				try{
					List<String> imageFiles = helper.parseAndSaveImages(request);
					// Now tell the server to resize them.
					webbisServiceProxy.resize(imageFiles);
					response.setRenderParameter(VIEW, MAIN_VIEW);
				}catch(WebbisValidationException e){
					request.setAttribute("validationMessages", e.getValidationMessages());
					response.setRenderParameter(VIEW, ADD_IMAGES_VIEW);
				}
			} else {
				// check which button was pressed and handle each request
				// accordingly
				if (request.getParameter("preview") != null) {
					helper.saveWebbisFormInSession(request);
					Webbis webbis;
					try{
						webbis = helper.createWebbis(request);
						helper.saveWebbisInSession(request.getPortletSession(), webbis);
						request.setAttribute("webbis", helper.createPreviewWebbisBean(request, webbis));
						response.setRenderParameter(VIEW, PREVIEW_VIEW);
					}catch(WebbisValidationException wve){
						request.setAttribute("validationMessages", wve.getValidationMessages());
						response.setRenderParameter(VIEW, MAIN_VIEW);
					}
					
				} else if (request.getParameter("publish") != null) {
					// has the user accepted the terms?
					if (request.getParameter("accept") == null){
						request.setAttribute("validationMessages", "Villkoren måste accepteras");
						request.setAttribute("webbis", helper.createPreviewWebbisBean(request, helper.getWebbisFromSession(request.getPortletSession())));
						response.setRenderParameter(VIEW, PREVIEW_VIEW);
					}
					else{
						webbisServiceProxy.saveWebbis(request.getPortletSession(
									true).getId(), helper.getWebbisFromSession(request.getPortletSession()));
						helper.cleanUp(request.getPortletSession(true));
						response.setRenderParameter(VIEW, CONFIRMATION_VIEW);
					}
				} else if (request.getParameter("cancelPreview") != null) {
					response.setRenderParameter(VIEW, MAIN_VIEW);
				} else if (request.getParameter("backFromConfirm") != null) {
					response.setRenderParameter(VIEW, SHOW_WEBBIS_LIST_VIEW);
				} else if (request.getParameter("editWebbisId") != null) {
					Webbis webbis = webbisServiceProxy.prepareForEditing(
							request.getPortletSession(true).getId(), request
									.getParameter("editWebbisId"));
					helper.putWebbisDataInSession(request
							.getPortletSession(true), webbis);
					request.setAttribute("editWebbisId", request.getParameter("editWebbisId"));
					response.setRenderParameter(VIEW, MAIN_VIEW);
				} else if (request.getParameter("deleteWebbis") != null) {
					// show the confirmDelete Page
					request.setAttribute("webbisId", request.getParameter("webbisId"));
					response.setRenderParameter(VIEW, CONFIRM_DELETE_WEBBIS_VIEW);
				} else if (request.getParameter("cancelDeleteWebbis") != null) {
					response.setRenderParameter(VIEW, MAIN_VIEW);
				} else if (request.getParameter("confirmDelete") != null) {
					webbisServiceProxy.deleteWebbis(request.getParameter("webbisId"));
					webbisServiceProxy.cleanUpTempDir(request.getPortletSession(true).getId());
					response.setRenderParameter(VIEW, CONFIRMATION_VIEW);
				} else if (request.getParameter("cancel") != null) {
					helper.cleanUp(request.getPortletSession(true));
					webbisServiceProxy.cleanUpTempDir(request
							.getPortletSession(true).getId());
					response.setRenderParameter(VIEW, SHOW_WEBBIS_LIST_VIEW);
				} else if (request.getParameter("addImages") != null) {
					helper.saveWebbisFormInSession(request);
					response.setRenderParameter(VIEW, ADD_IMAGES_VIEW);
				} else { // handle remove image and setting of main image Note: because we cannot 
					// rely on javascript we have to handle this on the server.
					helper.saveWebbisFormInSession(request);
					response.setRenderParameter(VIEW, MAIN_VIEW);
					if (request.getParameter("remove-image1") != null) {
						helper.removeImage(1, request);
					} else if (request.getParameter("remove-image2") != null) {
						helper.removeImage(2, request);
					} else if (request.getParameter("remove-image3") != null) {
						helper.removeImage(3, request);
					} else if (request.getParameter("remove-image4") != null) {
						helper.removeImage(4, request);
					} else if (request.getParameter("remove-image5") != null) {
						helper.removeImage(5, request);
					} else if (request.getParameter("image1-main-image") != null) {
						helper.setMainImage(1, request);
					} else if (request.getParameter("image2-main-image") != null) {
						helper.setMainImage(2, request);
					} else if (request.getParameter("image3-main-image") != null) {
						helper.setMainImage(3, request);
					} else if (request.getParameter("image4-main-image") != null) {
						helper.setMainImage(4, request);
					} else if (request.getParameter("image5-main-image") != null) {
						helper.setMainImage(5, request);
					}
				}
			}
		} finally {
			CallContextUtil.clear();
		} 
	}
}