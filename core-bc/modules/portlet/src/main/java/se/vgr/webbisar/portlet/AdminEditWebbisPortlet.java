package se.vgr.webbisar.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import se.vgr.webbisar.beans.SearchResultBean;
import se.vgr.webbisar.beans.SearchResultPageBean;
import se.vgr.webbisar.helpers.WebbisServiceProxy;
import se.vgr.webbisar.types.Webbis;

/**
 * AdminEditWebbis Portlet Class
 * 
 * @author sofiajonsson
 */
public class AdminEditWebbisPortlet extends GenericPortlet {

	private WebbisServiceProxy webbisServiceProxy;

	public void init(PortletConfig config) throws PortletException {
		webbisServiceProxy = new WebbisServiceProxy();
		super.init(config);
	}

	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher = null;
		if (request.getAttribute("SearchResultPage") != null){
			dispatcher = getPortletContext().getRequestDispatcher(
			"/WEB-INF/jsp/admin/EnableDisableWebbis.jsp");
		}
		else {
			dispatcher = getPortletContext().getRequestDispatcher(
			"/WEB-INF/jsp/admin/SearchWebbis.jsp");
		}
		
		dispatcher.include(request, response);
	}

	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
	
		if (request.getParameter("search") != null){
			int numberOfHits = webbisServiceProxy.getNumberOfMatchesForIncludeDisabled(request.getParameter("searchField"));
			
			List<Webbis> resultList = webbisServiceProxy.searchWebbisIncludeDisabled(request.getParameter("searchField"), 0, 20);
			List<SearchResultBean> list = new ArrayList<SearchResultBean>();
			if (resultList != null){
				for(Webbis webbis : resultList) {
					list.add(new SearchResultBean(webbis));
				}
			}
			
			SearchResultPageBean searchResultPage = new SearchResultPageBean(numberOfHits, list);
			request.setAttribute("SearchResultPage", searchResultPage);
			
					
		} else if (request.getParameter("webbisId") != null){
			webbisServiceProxy.toggleEnableDisable(request.getParameter("webbisId"));
			request.setAttribute("WebbisChanged", "true");
			
		} else if (request.getParameter("reindex") != null) {
			webbisServiceProxy.reindex();
		}
	}
}
