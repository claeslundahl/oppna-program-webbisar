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
		<div class="yui-b" style="text-align: left"><h2>Webbisar</h2></div>
		<div class="yui-b">
			<div class="yui-gb header">
				
			</div>
		</div>	
	</div>
	<div>
		<div id="yui-main">
			<form action="<portlet:actionURL secure="true"/>" enctype="multipart/form-data" method="post"
                  id="webbis_images_form" onsubmit="document.body.style.cursor = 'wait';">
			
				<div class="yui-b addwebbis">
					<div class="yui-g">
						<h3 class="webbis">Lägg till bilder / film</h3>
            <div style="margin-left: 0.4em">
              <c:choose>
                <c:when test="${empty portletSessionScope['webbisForm.availableImageIds']}">
                  <p>Det är endast tillåtet att lägga till fyra objekt, varav en film. Ta bort något befintligt objekt för att kunna ladda upp en annan bild/film.</p>
                </c:when>
                <c:otherwise>
                  <p>Det är tillåtet att lägga till fyra objekt, varav en film. Maximalt kan fyra bilder, alternativt tre bilder och en film, laddas upp. Maximal tillåten storlek på filmen är ${portletSessionScope['webbisForm.maxVideoMb']}MB.</p>
                </c:otherwise>
              </c:choose>
					  </div>
            
						<div class="yui-u first" style="margin-left: 0.4em">
							<c:if test="${not empty validationMessages}">
                <br/>
								<p style="color: red">
									<c:out value="${validationMessages[0]}"/>
								</p>
							</c:if>
							<c:forEach var="current" items="${portletSessionScope['webbisForm.availableImageIds']}">
								<br/>
								<p>
									<label for="<c:out value="${current}"/>">Välj mediafil: </label>
									<input name="<c:out value="${current}"/>" type="file" class="text" />	
								</p>
							</c:forEach>
              <br/>
              <br/>
						</div>
					</div>
					<div class="yui-g">
						<div class="yui-u first"></div>
						<div class="yui-u" style="text-align: right">
							<c:if test="${not empty portletSessionScope['webbisForm.availableImageIds']}">
								<input name="cancelAddImages" value="&nbsp;Avbryt&nbsp;" type="submit"/>
								<input name="saveImages" value="&nbsp;Lägg till&nbsp;" type="submit"/>
							</c:if>
							<c:if test="${empty portletSessionScope['webbisForm.availableImageIds']}">
								<input name="cancelAddImages" value="&nbsp;Tillbaka&nbsp;" type="submit"/>
							</c:if>
						</div>
					</div>	
				</div>
			</form>
		</div>
	</div>
	<div id="ft"></div>
</div>
