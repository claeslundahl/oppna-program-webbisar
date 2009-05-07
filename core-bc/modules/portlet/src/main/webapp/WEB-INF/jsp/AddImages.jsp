<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<portlet:defineObjects />
 
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.6.0/build/reset-fonts-grids/reset-fonts-grids.css"/>
<link rel="stylesheet" type="text/css" href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/style/style.css")%>'/>

	

<div id="custom-doc">
	<div id="hd">
		<div class="yui-b" style="text-align: left"><h2>Webbisar (BETA)</h2></div>
		<div class="yui-b">
			<div class="yui-gb header">
				
			</div>
		</div>	
	</div>
	<div id="bd">
		<div id="yui-main">
			<form action="<portlet:actionURL/>" enctype="multipart/form-data" method="post" id="webbis_images_form">
			
				<div class="yui-b addwebbis">
					<div class="yui-g">
						<h3 class="webbis">Lägg till bilder</h3>
					
						<div class="yui-u first" style="margin-left: 0.4em">

							<c:if test="${empty portletSessionScope['webbis.availableImageIds']}">
								<p>Det är endast tillåtet att lägga till fyra bilder. Ta bort någon befintlig bild för att kunna ladda upp ytterligare bilder.</p>
							</c:if>
							<c:if test="${not empty validationMessages}">
								<p style="color: red">
									<c:out value="${validationMessages[0]}"/>
								</p>
							</c:if>
							<c:forEach var="current" items="${portletSessionScope['webbis.availableImageIds']}">
								<br/>
								<p>
									<label for="<c:out value="${current}"/>">Välj bild: </label>
									<input name="<c:out value="${current}"/>" type="file" class="text" />	
								</p>
							</c:forEach>
							<br/>
							<br/>
						</div>
						<div class="yui-u"></div>
					</div>
					<div class="yui-g">
						<div class="yui-u first"></div>
						<div class="yui-u" style="text-align: right">
							
							<c:if test="${not empty portletSessionScope['webbis.availableImageIds']}">
								<input name="cancelAddImages" value="Avbryt" type="submit"/>
								<input name="saveImages" value="Lägg till" type="submit"/>
							</c:if>
							<c:if test="${empty portletSessionScope['webbis.availableImageIds']}">
								<input name="cancelAddImages" value="Tillbaka" type="submit"/>
							</c:if>
						</div>
						
					
					</div>	
				</div>
			</form>
		</div>
	</div>
	<div id="ft"></div>
</div>
