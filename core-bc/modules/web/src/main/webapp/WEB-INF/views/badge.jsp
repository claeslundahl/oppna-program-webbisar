<%--

    Copyright 2009 Vastra Gotalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA

--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page import="se.vgr.webbisar.web.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%=request.getAttribute("callback") %>(
    { 
      webbisar : [
      	<c:forEach var="w" items="${webbisar}" >
		{ 
			"img" : "<c:out value="${w.selectedImageUrl}"/>",
	  		"name" : "<c:out value="${w.name}"/>",
	  		"time" : "<c:out value="${w.smartTime}"/>",
	  		"parents" : "<c:out value="${w.parentsShort}"/>",
	  		"home" : "<c:out value="${w.home}"/>",
	  		"link" : "http://vard.vgregion.se/sv/Webbisar/Sok-webbisar/?_flowId=Webbisar.view-flow&webbisId=<c:out value="${w.id}"/>",
	  		"linkall" : "http://vard.vgregion.se/sv/Webbisar"
	 	}<c:if test="${not w.last}">,</c:if>      	
      	</c:forEach>
   ]	
    }
)