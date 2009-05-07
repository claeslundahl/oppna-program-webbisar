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
	  		"link" : "http://vard.vgregion.se/sv/Webbisar2/Sok-webbisar/?_flowId=Webbisar.view-flow&webbisId=<c:out value="${w.id}"/>",
	  		"linkall" : "http://vard.vgregion.se/sv/Webbisar2"
	 	}<c:if test="${not w.last}">,</c:if>      	
      	</c:forEach>
   ]	
    }
)