<%--

    Copyright 2010 Västra Götalandsregionen

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

<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<portlet:defineObjects />

<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.6.0/build/reset-fonts-grids/reset-fonts-grids.css"/>
<style type="text/css">
  <%@ include file="/style/style.css" %>
</style>  
 
<div id="custom-doc">
	<div>
		<div class="yui-b" style="text-align: left">
      <h2>Webbisar</h2>
    </div>
	</div>
	<div>
		<div id="yui-main">		
			<div class="yui-b addwebbis">
				<div class="yui-g">
					<h3 class="webbis">Välj webbis att uppdatera:</h3>
					<div class="yui-u first" style="margin-left: 0.4em">
						<c:forEach var="webbis" items="${requestScope.webbisar}">
							<p>
                <a href="<portlet:actionURL><portlet:param name="editWebbisId" value="${webbis.mainWebbis.id}"/></portlet:actionURL>">
                  <c:choose>
                    <c:when test="${webbis.hasMultipleBirthSiblings}">Redigera webbisar</c:when>
                    <c:otherwise>Redigera webbis</c:otherwise>
                  </c:choose>
                  <c:if test="${webbis.allWebbisNamesStringFromMain != null}">
                    <c:out value="${webbis.allWebbisNamesStringFromMain}"/> 
                  </c:if>
                  <c:if test="${webbis.birthDateFromMain != null}">
                   (<c:out value="${webbis.birthDateFromMain}"/>)
                  </c:if>
                </a>
              </p>
						</c:forEach>
					</div>
					<div class="yui-u"></div>
				</div>
				<div class="yui-g">
					<h3 class="webbis">Lägg till ny webbis:</h3>		
					<div class="yui-u first"  style="margin-left: 0.4em">
						<p>
              <a href="<portlet:renderURL><portlet:param name="VIEW" value="MAIN_VIEW"/></portlet:renderURL>">
                Lägg till ny
              </a>
            </p>
					</div>
					<div class="yui-u"></div>
				</div>
			</div>	
		</div>
	</div>
	<div id="ft"></div>
</div>
