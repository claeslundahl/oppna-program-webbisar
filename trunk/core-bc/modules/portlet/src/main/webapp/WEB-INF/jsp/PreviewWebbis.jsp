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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<portlet:defineObjects />	

<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.6.0/build/reset-fonts-grids/reset-fonts-grids.css"/>
<style type="text/css">
  <%@ include file="/style/style.css" %>
</style>  

<div id="custom-doc">
	<div>
		<div class="yui-b" style="text-align: left"><h2>Webbisar</h2></div>
	</div>
	<div>
		<div id="yui-main">
			<form action="<portlet:actionURL/>" method="post" name="webbis_main_form" id="webbis_main_form">
				<div class="yui-b webbis">
        
          <c:forEach items="${previewWebbis.webbisPreviews}" varStatus="refRow" var="previewWebbis">
            <h3 class="webbis"><c:out value="${previewWebbis.header}"/></h3>
            <div class="yui-gc">
              <div class="yui-u first"><img class="full" src="${previewWebbis.selectedImageUrl}"></img>
              </div>
              
              <div class="yui-u" style="margin-left:0; margin-right:2px;">
                  <p>Född: <b><c:out value="${previewWebbis.longDate}"/></b></p>
                    <c:if test="${previewWebbis.weight != '0g'}">
                      <p>Vikt: <b><c:out value="${previewWebbis.weight}"/></b></p>
                    </c:if>
                    <c:if test="${previewWebbis.length != '0cm'}">
                      <p>Längd: <b><c:out value="${previewWebbis.length}"/></b></p>
                    </c:if>
                    <br/>
                    <c:if test="${not empty previewWebbis.parent1 or not empty previewWebbis.parent2}">
                      <p>Föräldrar:<br/>
                        <b><c:out value="${previewWebbis.parent1}"/></b><br/>
                        <b><c:out value="${previewWebbis.parent2}"/></b><br/>
                      </p>
                    </c:if>
                    <br/>
                    <p>
                    <c:if test="${not empty previewWebbis.siblings}">
                      Syskon:<br/> 
                      <b><c:out value="${previewWebbis.siblings}"/></b>                                    
                    </c:if>
                    </p>
                    <br/>
                    <c:if test="${not empty previewWebbis.home}">
                     <p>Från: <b><c:out value="${previewWebbis.home}"/></b></p>
                    </c:if>
                    <p>Sjukhus: <b><c:out value="${previewWebbis.hospital}"/></b></p>
                    <br/>
                    <p><b><c:out value="${previewWebbis.message}"/></b></p>
                    <br/>
                    <c:if test="${not empty previewWebbis.homePage}">
                     <p><b><a href="${previewWebbis.homePage}">Webbsida</a></b></p>
                    </c:if>
                    <br/>
                  </div>
                </div>
            
                <div class="yui-gb" style="margin-left: 0.8em; padding-bottom: 1.0em;">
                  <div class="yui-u first">
                    <p>
                      <c:choose>
                        <c:when test="${not empty previewWebbis.selectedImageComment}">
                          <b><c:out value="${previewWebbis.selectedImageComment}"/></b>
                        </c:when>
                        <c:otherwise>
                          &#160;
                        </c:otherwise>  
                      </c:choose>
                    </p>
                  </div>
                  <div class="yui-u">
                    <!-- <p style="text-align: right">
                      <a href="${previewWebbis.selectedImageUrl}" target="_new">Visa större</a>
                    </p> -->
                  </div>
                  <div class="yui-u"></div>
               </div>
               
               <div class="yui-g piclist">
                  <div class="yui-g first">
                    <div class="yui-u first">
                      <c:if test="${previewWebbis.imageUrls[0] != null}">
                        <img class="notselected" src="${previewWebbis.imageUrls[0]}"/>
                      </c:if>
                    </div>
                    <div class="yui-u">
                      <c:if test="${previewWebbis.imageUrls[1] != null}">
                        <img class="notselected" src="${previewWebbis.imageUrls[1]}"/>
                       </c:if>
                     </div>
                  </div>
                  <div class="yui-g">
                    <div class="yui-u first">
                      <c:if test="${previewWebbis.imageUrls[2] != null}">
                        <img class="notselected" src="${previewWebbis.imageUrls[2]}"/>
                       </c:if>
                    </div>
                    <div class="yui-u">
                      <c:if test="${previewWebbis.imageUrls[3] != null}">
                        <img class="notselected" src="${previewWebbis.imageUrls[3]}"/>
                       </c:if>
                    </div>
                  </div>
              </div>
          </c:forEach>
							
					<div class="yui-g">
						<div class="yui-u first">
							<c:if test="${not empty validationMessages}">
								<p style="color: red">Villkoren måste godkännas</p>
							</c:if>
							<p>
								<label for="accept"><!-- TODO fix the link to the terms -->
									Jag har tagit del av och godkänner <a href="http://webbisar.vgregion.se/images/villkor.html" target="_new">villkoren</a>: <input type="checkbox" name="accept" value="true" style="vertical-align: middle;"></input>
								</label>
							</p>
						</div>
						<div class="yui-u" style="text-align: right">
							<input name="cancelPreview" value="Tillbaka" type="submit"/>
							<input name="publish" value="Publicera" type="submit"/>
						</div>
					</div> 
				</div>	
			</form>
		</div>
	</div>
	<div id="ft"></div>
</div>