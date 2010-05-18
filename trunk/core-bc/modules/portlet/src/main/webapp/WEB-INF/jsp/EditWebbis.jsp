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

<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0"  prefix="portlet" %>

<portlet:defineObjects />	
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.6.0/build/reset-fonts-grids/reset-fonts-grids.css"/>

<style type="text/css">
  <%@ include file="/style/style.css" %>
</style>

<div id="custom-doc">
	<div id="hd">
    <div class="yui-g">
  		<div class="yui-u first" style="text-align: left">
        <h2>Webbisar</h2>
      </div>
      <div class="yui-u" style="text-align: right; padding-top: 4px;">
        <a target = "_blank" href="http://tycktill.vgregion.se/tyck-till-core-bc-module-web/xforms-jsp/index.jsp?context=webbisar&reportMethod=pivotaltracker&reportEmail=tycktill%40vgregion.se">Förbättringsförslag och synpunkter</a>
      </div>
    </div>
	</div>
	<div id="bd">
		<div id="yui-main">
			<form action="<portlet:actionURL/>" method="post" name="webbis_main_form" id="webbis_main_form">
      
				<c:if test="${portletSessionScope['webbisForm.mainWebbisBean'] != null and portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis != null}">
					<input type="hidden" name="w0_webbisId" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.id}"/>
				</c:if>
				<div class="yui-b addwebbis">
					
					<div class="yui-g">
						<c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.id == null}"><h3 class="webbis">Lägg till webbis / webbisar</h3></c:if>
						<c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.id != null}"><h3 class="webbis">Uppdatera webbis / webbisar</h3></c:if>
						<div class="yui-u first" style="margin-left: 0.4em">
							
							<div class="yui-g">
								<div class="yui-u first" style="width:99%">
									<c:if test="${empty validationMessages}">
										<p>Fyll i fälten nedan och klicka på ”Förhandsgranska”.<br/>
                      <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis == null or portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.id == null}">
                        Har ni fått <a href="<portlet:renderURL><portlet:param name="VIEW" value="MAIN_VIEW"/><portlet:param name="noOfSiblings" value="1"/></portlet:renderURL>">tvillingar</a> eller <a href="<portlet:renderURL><portlet:param name="VIEW" value="MAIN_VIEW"/><portlet:param name="noOfSiblings" value="2"/></portlet:renderURL>">trillingar</a>?
                      </c:if>
                    </p>
									</c:if>
									<c:if test="${not empty validationMessages}">
										<p style="color: red">Följande fält är inte korrekt ifyllda:</p>
										<ul style="color: red">
									</c:if>
									<c:forEach items="${validationMessages}" var="validationMessage">
										<li><c:out value="${validationMessage}"/></li>
									</c:forEach>
									<c:if test="${not empty validationMessages}">
										</ul>
									</c:if>
								</div>
								<div class="yui-u">
								</div>
							</div>

							<!-- Namn och kön -->
							<div class="yui-g" style="padding-top: 20px;">
                <h4 class="webbis">Webbis 1</h4>
								<div class="yui-u first">
									<p>
										<label for="w0_webbisname">Webbisens namn:</label>
								    	<input type="text" name="w0_webbisname" id="w0_webbisname" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.name}" default="skriv namn" />" maxlength="15"/>
									</p>
								</div>
								<div class="yui-u" style="padding-top: 0.8em">
									<p><input type="radio" name="w0_gender" value="Male" <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.sex eq 'Male'}" > checked</c:if> />Pojke</p>
									<p><input type="radio" name="w0_gender" value="Female" <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.sex eq 'Female'}" > checked</c:if> />Flicka</p>		  								 	
								</div>
							</div>
							<!-- Datum och tid -->
							<div class="yui-g addrow">
								<div class="yui-u first" style="width: 52%">
									<p>
										<label for="w0_day">Födelsedatum: *</label><br/>
	         							<select name="w0_day"><!-- TODO use js to change the number of days depending on the selected month  -->
											<c:forEach begin="1" end="31" var="day">
												<c:set var="dayFromSession" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.birthTime.day}"/>
												<c:if test="${dayFromSession != null}">
													<c:set var="dayFromSession"><fmt:parseNumber value="${dayFromSession}"/></c:set>
												</c:if>
												<option <c:if test="${dayFromSession eq day}"> selected</c:if>><c:out value="${day}"/></option>
											</c:forEach>
										</select>
                    <c:set var="w0_webbisMonth" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.birthTime.month}"/>
										<select name="w0_month">
											<option value="1" <c:if test="${w0_webbisMonth eq '1'}"> selected</c:if>>jan</option>
											<option value="2" <c:if test="${w0_webbisMonth eq '2'}"> selected</c:if>>feb</option>
											<option value="3" <c:if test="${w0_webbisMonth eq '3'}"> selected</c:if>>mar</option>
											<option value="4" <c:if test="${w0_webbisMonth eq '4'}"> selected</c:if>>apr</option>
											<option value="5" <c:if test="${w0_webbisMonth eq '5'}"> selected</c:if>>maj</option>
											<option value="6" <c:if test="${w0_webbisMonth eq '6'}"> selected</c:if>>jun</option>
											<option value="7" <c:if test="${w0_webbisMonth eq '7'}"> selected</c:if>>jul</option>
											<option value="8" <c:if test="${w0_webbisMonth eq '8'}"> selected</c:if>>aug</option>
											<option value="9" <c:if test="${w0_webbisMonth eq '9'}"> selected</c:if>>sep</option>
											<option value="10" <c:if test="${w0_webbisMonth eq '10'}"> selected</c:if>>okt</option>
											<option value="11" <c:if test="${w0_webbisMonth eq '11'}"> selected</c:if>>nov</option>
											<option value="12" <c:if test="${w0_webbisMonth eq '12'}"> selected</c:if>>dec</option>
										</select>
										<select name="w0_year">
											<c:forEach begin="${requestScope.currentYear -1}" end="${requestScope.currentYear + 1}" var="year">
												<c:set var="yearFromSession" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.birthTime.year}"/>
												<c:if test="${yearFromSession != null}">
													<c:set var="yearFromSession"><fmt:parseNumber value="${yearFromSession}"/></c:set>
												</c:if>
												<option <c:if test="${yearFromSession eq year}"> selected</c:if>><c:out value="${year}"/></option>
											</c:forEach>
										</select>
									</p>
   							</div> 
   							<div class="yui-u"  style="width: 48%">
   								<p>
   									<label for="w0_hour">Klockslag: *</label><br/>
     								<select name="w0_hour">
    									<c:forEach begin="0" end="23" var="hour">
    										<c:set var="hourFromSession" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.birthTime.hour}"/>
    										<c:if test="${hourFromSession != null}">
    											<c:set var="hourFromSession"><fmt:parseNumber value="${hourFromSession}"/></c:set>
    										</c:if>
    										<option <c:if test="${hourFromSession eq hour}"> selected</c:if>><fmt:formatNumber value="${hour}" minIntegerDigits="2"/></option>
    									</c:forEach>
    								</select>
    								<select name="w0_min">
    									<c:forEach begin="0" end="59" var="min">
    										<c:set var="minFromSession" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.birthTime.minutes}"/>
    										<c:if test="${minFromSession != null}">
    											<c:set var="minFromSession"><fmt:parseNumber value="${minFromSession}"/></c:set>
    										</c:if>
    										<option <c:if test="${minFromSession eq min}"> selected</c:if>><fmt:formatNumber value="${min}" minIntegerDigits="2"/></option>
    									</c:forEach>
								    </select>
							   </p>
								</div>
							</div>
							<!-- Vikt och längd -->
							<div class="yui-g addrow">
								<div class="yui-u first">
									<p>
										<label for="w0_weight">
								    	Vikt (gram):
								    	</label>
								    	<input type="text" name="w0_weight" id="w0_weight" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.weight}" />"/>
								  	</p>
								</div>
								<div class="yui-u">
									<p>
										<label for="w0_length">
								    	Längd (cm):
								    	</label>
								    	<input type="text" name="w0_length" id="w0_length" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.length}" />"/>
								  	</p>							
								</div>
							</div>
						</div>
            
						<div class="yui-u" style="padding-top: 20px;">
            
              <div class="yui-g">
                <div class="yui-u first">
                  <br/>
                  <br/>
                </div>
                <div class="yui-u">
                </div>
              </div>
            
              <input name="addImages_w0" value="Lägg till bilder" type="submit"/>
              <span style="padding-left: 20px;">Bilder på webbis 1</span>                
              <br/><br/>
              <!--  <img class="icon" src="../img/add-32x32.png"><a id="addPic" href="#">Lägg till bild</a><br/> -->
              
              <!-- Any images object? -->
              <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.images != null}" >
              
                <!-- First in list is always the main image when populated from DB, but it may change in session -->
                <input type="hidden" name="w0_main-image" value="${portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[0]}"/>
              
                <c:forEach items="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.images}" step="2" varStatus="refRow" var="image">
                  <div class="yui-g"> 
                    <div class="yui-u first">
                      <input type="hidden" name="w0_image${refRow.index}" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${image.location}"/>"/>
                      <img style="width: 150px; margin-bottom: 2px;" src="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${image.location}"/>" />
                      <br/>
                      <input type="submit" name="w0_remove-image${refRow.index}" value="Radera"/>
                      <div style="float: right; margin-right: 5px;">
                        <a href="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${image.location}"/>" target="_new">Se stor bild</a>
                      </div>
                      <div style="height: 27px; margin-top: 2px;">
                        <c:set var="imageId" value="w0_image${refRow.index}"/>
                        <c:if test="${imageId eq portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[0]}">
                          <p style="padding-top: 7px;">Denna bild är förstabild</p>
                        </c:if>
                        <c:if test="${imageId ne portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[0]}">
                          <input type="submit" name="w0_image${refRow.index}-main-image" value="Sätt som förstabild"/>
                        </c:if>
                      </div>
                      <p style="margin-bottom: 10px;">
                        <label for="w0_image${refRow.index+1}-text">Bildtext:<br/></label>
                        <input type="text" name="w0_image${refRow.index}-text" class="text" value="${image.text}"  maxlength="80"></input><br/>
                      </p>    
                    </div>
                    <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.images[refRow.index+1] != null}" >
                      <c:set var="imageSecondIdx" value="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.images[refRow.index+1]}"/>
                      <div class="yui-u">
                        <input type="hidden" name="w0_image${refRow.index+1}" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${imageSecondIdx.location}"/>"/>
                        <img style="width: 150px; margin-bottom: 2px;" src="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${imageSecondIdx.location}"/>"/>
                        <br/>
                        <input type="submit" name="w0_remove-image${refRow.index+1}" value="Radera"/>
                        <div style="float: right; margin-right: 5px;">
                          <a href="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${imageSecondIdx.location}"/>" target="_new">Se stor bild</a>
                        </div>
                        <div style="height: 27px; margin-top: 2px;">
                          <c:set var="imageId2" value="w0_image${refRow.index+1}"/>
                          <c:if test="${imageId2 eq portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[0]}">
                            <p style="padding-top: 7px;">Denna bild är förstabild</p>
                          </c:if>
                          <c:if test="${imageId2 ne portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[0]}">
                            <input type="submit" name="w0_image${refRow.index+1}-main-image" value="Sätt som förstabild"/>
                          </c:if>
                        </div>
                        <p style="margin-bottom: 10px;">
                          <label for="w0_image${refRow.index+1}-text">Bildtext:<br/></label>
                          <input type="text" name="w0_image${refRow.index+1}-text" class="text" value="${imageSecondIdx.text}"  maxlength="80"></input><br/>
                        </p>  
                      </div>  
                    </c:if> 
                  </div>
                </c:forEach>
              </c:if>
						</div> 
					</div>
          
          <!-- Listing multiple birth siblings, if any -->
          <c:forEach items="${portletSessionScope['webbisForm.mainWebbisBean'].multipleBirthWebbisSiblings}" varStatus="refRow" var="multipleBirthSibling">
            <div class="yui-g">
              <div class="yui-u first" style="margin-left: 0.4em">
                
                <input type="hidden" name="w${refRow.index+1}_webbisId" value="${multipleBirthSibling.id}"/>
                
                <div class="yui-g">
                  <div class="yui-u first" style="width:99%">
                  </div>
                  <div class="yui-u">
                  </div>
                </div>
  
                <!-- Namn och kön -->
                <div class="yui-g" style="padding-top: 20px;">
                  <h4 class="webbis">Webbis ${refRow.index+2}</h4>
                  <div class="yui-u first">
                    <p>
                      <label for="w${refRow.index+1}_webbisname">Webbisens namn:</label>
                        <input type="text" name="w${refRow.index+1}_webbisname" id="w${refRow.index+1}_webbisname" class="text" value="<c:out value="${multipleBirthSibling.name}" default="skriv namn" />" maxlength="15"/>
                    </p>
                  </div>
                  <div class="yui-u" style="padding-top: 0.8em">
                    <p><input type="radio" name="w${refRow.index+1}_gender" value="Male" <c:if test="${multipleBirthSibling.sex eq 'Male'}" > checked</c:if> />Pojke</p>
                    <p><input type="radio" name="w${refRow.index+1}_gender" value="Female" <c:if test="${multipleBirthSibling.sex eq 'Female'}" > checked</c:if> />Flicka</p>                       
                  </div>
                </div>
                <!-- Datum och tid -->
                <div class="yui-g addrow">
                  <div class="yui-u first" style="width: 52%">
                    <p>
                      <label for="w${refRow.index+1}_day">Födelsedatum: *</label><br/>
                          <select name="w${refRow.index+1}_day"><!-- TODO use js to change the number of days depending on the selected month  -->
                        <c:forEach begin="1" end="31" var="day">
                          <c:set var="dayFromSession" value="${multipleBirthSibling.birthTime.day}"/>
                          <c:if test="${dayFromSession != null}">
                            <c:set var="dayFromSession"><fmt:parseNumber value="${dayFromSession}"/></c:set>
                          </c:if>
                          <option <c:if test="${dayFromSession eq day}"> selected</c:if>><c:out value="${day}"/></option>
                        </c:forEach>
                      </select>
                      <select name="w${refRow.index+1}_month">
                        <option value="1" <c:if test="${multipleBirthSibling.birthTime.month eq '1'}"> selected</c:if>>jan</option>
                        <option value="2" <c:if test="${multipleBirthSibling.birthTime.month eq '2'}"> selected</c:if>>feb</option>
                        <option value="3" <c:if test="${multipleBirthSibling.birthTime.month eq '3'}"> selected</c:if>>mar</option>
                        <option value="4" <c:if test="${multipleBirthSibling.birthTime.month eq '4'}"> selected</c:if>>apr</option>
                        <option value="5" <c:if test="${multipleBirthSibling.birthTime.month eq '5'}"> selected</c:if>>maj</option>
                        <option value="6" <c:if test="${multipleBirthSibling.birthTime.month eq '6'}"> selected</c:if>>jun</option>
                        <option value="7" <c:if test="${multipleBirthSibling.birthTime.month eq '7'}"> selected</c:if>>jul</option>
                        <option value="8" <c:if test="${multipleBirthSibling.birthTime.month eq '8'}"> selected</c:if>>aug</option>
                        <option value="9" <c:if test="${multipleBirthSibling.birthTime.month eq '9'}"> selected</c:if>>sep</option>
                        <option value="10" <c:if test="${multipleBirthSibling.birthTime.month eq '10'}"> selected</c:if>>okt</option>
                        <option value="11" <c:if test="${multipleBirthSibling.birthTime.month eq '11'}"> selected</c:if>>nov</option>
                        <option value="12" <c:if test="${multipleBirthSibling.birthTime.month eq '12'}"> selected</c:if>>dec</option>
                      </select>
                      <select name="w${refRow.index+1}_year">
                        <c:forEach begin="${requestScope.currentYear -1}" end="${requestScope.currentYear + 1}" var="year">
                          <c:set var="yearFromSession" value="${multipleBirthSibling.birthTime.year}"/>
                          <c:if test="${yearFromSession != null}">
                            <c:set var="yearFromSession"><fmt:parseNumber value="${yearFromSession}"/></c:set>
                          </c:if>
                          <option <c:if test="${yearFromSession eq year}"> selected</c:if>><c:out value="${year}"/></option>
                        </c:forEach>
                      </select>
                    </p>
                  </div> 
                  <div class="yui-u"  style="width: 48%">
                    <p>
                      <label for="w${refRow.index+1}_hour">Klockslag: *</label><br/>
                      <select name="w${refRow.index+1}_hour">
                        <c:forEach begin="0" end="23" var="hour">
                          <c:set var="hourFromSession" value="${multipleBirthSibling.birthTime.hour}"/>
                          <c:if test="${hourFromSession != null}">
                            <c:set var="hourFromSession"><fmt:parseNumber value="${hourFromSession}"/></c:set>
                          </c:if>
                          <option <c:if test="${hourFromSession eq hour}"> selected</c:if>><fmt:formatNumber value="${hour}" minIntegerDigits="2"/></option>
                        </c:forEach>
                      </select>
                      <select name="w${refRow.index+1}_min">
                        <c:forEach begin="0" end="59" var="min">
                          <c:set var="minFromSession" value="${multipleBirthSibling.birthTime.minutes}"/>
                          <c:if test="${minFromSession != null}">
                            <c:set var="minFromSession"><fmt:parseNumber value="${minFromSession}"/></c:set>
                          </c:if>
                          <option <c:if test="${minFromSession eq min}"> selected</c:if>><fmt:formatNumber value="${min}" minIntegerDigits="2"/></option>
                        </c:forEach>
                      </select>
                   </p>
                  </div>
                </div>
                <!-- Vikt och längd -->
                <div class="yui-g addrow">
                  <div class="yui-u first">
                    <p>
                      <label for="w${refRow.index+1}_vikt">
                        Vikt (gram):
                        </label>
                        <input type="text" name="w${refRow.index+1}_weight" id="w${refRow.index+1}_weight" class="text" value="<c:out value="${multipleBirthSibling.weight}" />"/>
                      </p>
                  </div>
                  <div class="yui-u">
                    <p>
                      <label for="w${refRow.index+1}_langd">
                        Längd (cm):
                        </label>
                        <input type="text" name="w${refRow.index+1}_length" id="w${refRow.index+1}_length" class="text" value="<c:out value="${multipleBirthSibling.length}" />"/>
                      </p>              
                  </div>
                </div>
              </div>
              
              <div class="yui-u" style="padding-top: 20px;">
                <input name="addImages_w${refRow.index+1}" value="Lägg till bilder" type="submit"/>                
                <span style="padding-left: 20px;">Bilder på webbis ${refRow.index+2}</span>                
                <br/><br/>
                <!--  <img class="icon" src="../img/add-32x32.png"><a id="addPic" href="#">Lägg till bild</a><br/> -->
                
                <!-- Any images object? -->
                <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.images != null}" >
                
                  <!-- First in list is always the main image when populated from DB, but it may change in session -->
                  <input type="hidden" name="w${refRow.index+1}_main-image" value="${portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[refRow.index+1]}"/>
                
                  <c:forEach items="${multipleBirthSibling.images}" step="2" varStatus="refImgRow" var="image">
                    <div class="yui-g"> 
                      <div class="yui-u first">
                        <input type="hidden" name="w${refRow.index+1}_image${refImgRow.index}" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${image.location}"/>"/>
                        <img style="width: 150px; margin-bottom: 2px;" src="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${image.location}"/>" />
                        <br/>
                        <input type="submit" name="w${refRow.index+1}_remove-image${refImgRow.index}" value="Radera"/>
                        <div style="float: right; margin-right: 5px;">
                          <a href="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${image.location}"/>" target="_new">Se stor bild</a>
                        </div>
                        <div style="height: 27px; margin-top: 2px;">
                          <c:set var="imageId" value="w${refRow.index+1}_image${refImgRow.index}"/>
                          <c:if test="${imageId eq portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[refRow.index+1]}">
                            <p style="padding-top: 7px;">Denna bild är förstabild</p>
                          </c:if>
                          <c:if test="${imageId ne portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[refRow.index+1]}">
                            <input type="submit" name="w${refRow.index+1}_image${refImgRow.index}-main-image" value="Sätt som förstabild"/>
                          </c:if>
                        </div>
                        <p style="margin-bottom: 10px;">
                          <label for="w${refRow.index+1}_image${refRow.index+1}-text">Bildtext:<br/></label>
                          <input type="text" name="w${refRow.index+1}_image${refImgRow.index}-text" class="text" value="${image.text}"  maxlength="80"></input><br/>
                        </p>    
                      </div>
                      <c:if test="${multipleBirthSibling.images[refImgRow.index+1] != null}" >
                        <c:set var="imageSecondIdx" value="${multipleBirthSibling.images[refImgRow.index+1]}"/>
                        <div class="yui-u">
                          <input type="hidden" name="w${refRow.index+1}_image${refImgRow.index+1}" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${imageSecondIdx.location}"/>"/>
                          <img style="width: 150px; margin-bottom: 2px;" src="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${imageSecondIdx.location}"/>"/>
                          <br/>
                          <input type="submit" name="w${refRow.index+1}_remove-image${refImgRow.index+1}" value="Radera"/>
                          <div style="float: right; margin-right: 5px;">
                            <a href="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].imageBaseUrl}"/><c:out value="${imageSecondIdx.location}"/>" target="_new">Se stor bild</a>
                          </div>
                          <div style="height: 27px; margin-top: 2px;">
                            <c:set var="imageId2" value="w${refRow.index+1}_image${refImgRow.index+1}"/>
                            <c:if test="${imageId2 eq portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[refRow.index+1]}">
                              <p style="padding-top: 7px;">Denna bild är förstabild</p>
                            </c:if>
                            <c:if test="${imageId2 ne portletSessionScope['webbisForm.mainWebbisBean'].selectedMainImages[refRow.index+1]}">
                              <input type="submit" name="w${refRow.index+1}_image${refImgRow.index+1}-main-image" value="Sätt som förstabild"/>
                            </c:if>
                          </div>
                          <p style="margin-bottom: 10px;">
                            <label for="w${refRow.index+1}_image${refRow.index+1}-text">Bildtext:<br/></label>
                            <input type="text" name="w${refRow.index+1}_image${refImgRow.index+1}-text" class="text" value="${imageSecondIdx.text}"  maxlength="80"></input><br/>
                          </p>  
                        </div>  
                      </c:if> 
                    </div>
                  </c:forEach>
                </c:if>
              </div> 
              
            </div>
          </c:forEach>
          
          <div class="yui-g">
            <div class="yui-u first" style="margin-left: 0.4em">      
              <!-- Förälder 1 -->
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="parentfname1">
                      Förälder: *
                      </label>
                      <input type="text" name="parentfname1" id="parentfname1" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].parent1.firstName}" default="förnamn" />" maxlength="15"/>
                    </p>
                </div>
                <div class="yui-u">
                  <p>
                    <label for="parentlname1">
                      <br/>
                      </label>
                      <input type="text" name="parentlname1" id="parentlname1" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].parent1.lastName}" default="efternamn" />" maxlength="25"/>
                    </p>              
                </div>
              </div>
              <!-- Förälder 2 -->
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="parentfname2">
                      Förälder:
                      </label>
                      <input type="text" name="parentfname2" id="parentfname2" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].parent2.firstName}" default="förnamn" />" maxlength="15"/>
                    </p>
                </div>
                <div class="yui-u">
                  <p>
                    <label for="parentlname2">
                      <br/>
                      </label>
                      <input type="text" name="parentlname2" id="parentlname2" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].parent2.lastName}" default="efternamn" />" maxlenght="25"/>
                    </p>              
                </div>
              </div>
              <!-- Syskon -->
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p><label for="siblings">
                      Syskon:
                      </label>
                      <input type="text" name="siblings" id="siblings" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].siblings}" />"/>
                    </p>
                </div>
              </div>
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="hemort">
                    Hemort:
                    </label>
                    <input type="text" name="hometown" id="hometown" class="text" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].homeTown}" />" maxlength="25"/>
                  </p>
                </div>
                <div class="yui-u"></div>
              </div>
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="hospital">
                    Sjukhus: *
                    </label>
                    <select name="hospital">
                    <option value="Valj">Välj sjukhus</option>
                    <c:forEach items="${requestScope.hospitals}" var="hospital"><!-- KSS, MOLNDAL, NAL, SAS, OSTRA, OVRIGT -->
                      <option value="${hospital.longName}" <c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].hospital eq hospital}"> selected</c:if>><c:out value="${hospital.longName}"/></option>
                    </c:forEach>
                    </select>
                  </p>
                </div>
                <div class="yui-u"></div>
              </div>
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="message">
                      Meddelande till besökarna:
                    </label>
                    <textarea cols="40" rows="3" name="message" style="overflow:hidden"><c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].message}" /></textarea>
                  </p>
                </div>
                <div class="yui-u"></div>
              </div>
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="webpage">
                      Webbsida, blogg eller liknande (ex.http://www.hemma.se/min):
                    </label>
                    <input type="text" name="webpage" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].webPage}" />"></input>
                  </p>
                </div>
                <div class="yui-u"></div>
              </div>
              <div class="yui-g addrow">
                <div class="yui-u first">
                  <p>
                    <label for="e-mail">
                      E-post (visas ej för andra): *
                    </label>
                    <input type="text" name="e-mail" value="<c:out value="${portletSessionScope['webbisForm.mainWebbisBean'].email}" />"></input>
                  </p>
                </div>
                <div class="yui-u"></div>
              </div>  
            </div>
            
            <div class="yui-u" style="padding-top: 20px;">
            </div> 
          </div>
          
					<div class="yui-g">
						<div class="yui-u first"></div>
						<div class="yui-u" style="text-align: right">
							<c:if test="${portletSessionScope['webbisForm.mainWebbisBean'].mainWebbis.id != null}"><input name="deleteWebbis" value="Radera webbis" type="submit"/></c:if>
							<input name="cancel" value="Avbryt" type="submit"/>
							<input name="preview" value="Förhandsgranska" type="submit"/>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="ft"></div>
</div>