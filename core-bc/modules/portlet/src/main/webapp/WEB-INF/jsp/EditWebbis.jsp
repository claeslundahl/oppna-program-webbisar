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
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<portlet:defineObjects />	
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.6.0/build/reset-fonts-grids/reset-fonts-grids.css"/>
<link rel="stylesheet" type="text/css" href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/style/style.css")%>'/>

<div id="custom-doc">
	<div id="hd">
		<div class="yui-b" style="text-align: left"><h2>Webbisar (BETA)</h2></div>
	</div>
	<div id="bd">
		<div id="yui-main">
			<form action="<portlet:actionURL/>" method="post" name="webbis_main_form" id="webbis_main_form">
				<c:if test="${portletSessionScope['webbis.mainform.webbisId'] != null}">
					<input type="hidden" name="webbisId" value="${portletSessionScope['webbis.mainform.webbisId']}"/>
				</c:if>
				<div class="yui-b addwebbis">
					
					<div class="yui-g">
						<c:if test="${portletSessionScope['webbis.mainform.webbisId'] == null}"><h3 class="webbis">Lägg till webbis</h3></c:if>
						<c:if test="${portletSessionScope['webbis.mainform.webbisId'] != null}"><h3 class="webbis">Uppdatera webbis</h3></c:if>
						<div class="yui-u first" style="margin-left: 0.4em">
							
							<div class="yui-g">
								<div class="yui-u first" style="width:99%">
									<c:if test="${empty validationMessages}">
										<p>Fyll i fälten nedan och klicka på ”Förhandsgranska”.</p>
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
							<div class="yui-g">
								<div class="yui-u first">
									<p>
										<label for="webbisname">Webbisens namn:</label>
								    	<input type="text" name="webbisname" id="webbisname" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.webbisname']}" default="skriv namn" />" maxlength="15"/>
									</p>
								</div>
								<div class="yui-u" style="padding-top: 0.8em">
									<p><input type="radio" name="gender" value="Male" <c:if test="${portletSessionScope['webbis.mainform.gender'] eq 'Male'}" > checked</c:if> />Pojke</p>
									<p><input type="radio" name="gender" value="Female" <c:if test="${portletSessionScope['webbis.mainform.gender'] eq 'Female'}" > checked</c:if> />Flicka</p>		  								 	
								</div>
							</div>
							<!-- Datum och tid -->
							<div class="yui-g addrow">
								<div class="yui-u first" style="width: 60%">
											<p>
												<label for="day">Födelsedatum: *</label><br/>
			         							<select name="day"><!-- TODO use js to change the number of days depending on the selected month  -->
													<c:forEach begin="1" end="31" var="day">
														<c:set var="dayFromSession" value="${portletSessionScope['webbis.mainform.day']}"/>
														<c:if test="${dayFromSession != null}">
															<c:set var="dayFromSession"><fmt:parseNumber value="${dayFromSession}"/></c:set>
														</c:if>
														<option <c:if test="${dayFromSession eq day}"> selected</c:if>><c:out value="${day}"/></option>
													</c:forEach>
												</select>
												<select name="month">
													<option value="1" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '1'}"> selected</c:if>>jan</option>
													<option value="2" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '2'}"> selected</c:if>>feb</option>
													<option value="3" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '3'}"> selected</c:if>>mar</option>
													<option value="4" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '4'}"> selected</c:if>>apr</option>
													<option value="5" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '5'}"> selected</c:if>>maj</option>
													<option value="6" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '6'}"> selected</c:if>>jun</option>
													<option value="7" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '7'}"> selected</c:if>>jul</option>
													<option value="8" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '8'}"> selected</c:if>>aug</option>
													<option value="9" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '9'}"> selected</c:if>>sep</option>
													<option value="10" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '10'}"> selected</c:if>>okt</option>
													<option value="11" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '11'}"> selected</c:if>>nov</option>
													<option value="12" <c:if test="${portletSessionScope['webbis.mainform.month'] eq '12'}"> selected</c:if>>dec</option>
												</select>
												<select name="year">
													<c:forEach begin="${requestScope.currentYear -1}" end="${requestScope.currentYear + 1}" var="year">
														<c:set var="yearFromSession" value="${portletSessionScope['webbis.mainform.year']}"/>
														<c:if test="${yearFromSession != null}">
															<c:set var="yearFromSession"><fmt:parseNumber value="${yearFromSession}"/></c:set>
														</c:if>
														<option <c:if test="${yearFromSession eq year}"> selected</c:if>><c:out value="${year}"/></option>
													</c:forEach>
												</select>
											</p>
							
	         							</div> 
	         							<div class="yui-u"  style="width: 40%">
	         								<p>
	         									<label for="hour">Klockslag: *</label><br/>
		         								<select name="hour">
													<c:forEach begin="0" end="23" var="hour">
														<c:set var="hourFromSession" value="${portletSessionScope['webbis.mainform.hour']}"/>
														<c:if test="${hourFromSession != null}">
															<c:set var="hourFromSession"><fmt:parseNumber value="${hourFromSession}"/></c:set>
														</c:if>
														<option <c:if test="${hourFromSession eq hour}"> selected</c:if>><fmt:formatNumber value="${hour}" minIntegerDigits="2"/></option>
													</c:forEach>
												</select>
												<select name="min">
													<c:forEach begin="0" end="59" var="min">
														<c:set var="minFromSession" value="${portletSessionScope['webbis.mainform.min']}"/>
														<c:if test="${minFromSession != null}">
															<c:set var="minFromSession"><fmt:parseNumber value="${minFromSession}"/></c:set>
														</c:if>
														<option <c:if test="${minFromSession eq min}"> selected</c:if>><fmt:formatNumber value="${min}" minIntegerDigits="2"/></option>
													</c:forEach>
												</select>
											</p>
									
								<!-- </div>
								<div class="yui-u"> -->
									
								</div>
							</div>
							<!-- Vikt och längd -->
							<div class="yui-g addrow">
								<div class="yui-u first">
									<p>
										<label for="vikt">
								    	Vikt (gram):
								    	</label>
								    	<input type="text" name="weight" id="weight" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.weight']}" />"/>
								  	</p>
								</div>
								<div class="yui-u">
									<p>
										<label for="langd">
								    	Längd (cm):
								    	</label>
								    	<input type="text" name="length" id="length" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.length']}" />"/>
								  	</p>							
								</div>
							</div>
							<!-- Förälder 1 -->
							<div class="yui-g addrow">
								<div class="yui-u first">
									<p>
										<label for="parentfname1">
								    	Förälder: *
								    	</label>
								    	<input type="text" name="parentfname1" id="parentfname1" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.parentfname1']}" default="förnamn" />" maxlength="15"/>
								  	</p>
								</div>
								<div class="yui-u">
									<p>
										<label for="parentlname1">
								    	<br/>
								    	</label>
								    	<input type="text" name="parentlname1" id="parentlname1" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.parentlname1']}" default="efternamn" />" maxlength="25"/>
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
								    	<input type="text" name="parentfname2" id="parentfname2" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.parentfname2']}" default="förnamn" />" maxlength="15"/>
								  	</p>
								</div>
								<div class="yui-u">
									<p>
										<label for="parentlname2">
								    	<br/>
								    	</label>
								    	<input type="text" name="parentlname2" id="parentlname2" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.parentlname2']}" default="efternamn" />" maxlenght="25"/>
								  	</p>							
								</div>
							</div>
							<!-- Syskon -->
							<div class="yui-g addrow">
								<div class="yui-u first">
									
									<p><label for="siblings">
								    	Syskon:
								    	</label>
								    	<input type="text" name="siblings" id="siblings" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.siblings']}" />"/>
								  	</p>
								</div>
							</div>
							<div class="yui-g addrow">
								<div class="yui-u first">
									<p>
										<label for="hemort">
										Hemort:
										</label>
										<input type="text" name="hometown" id="hometown" class="text" value="<c:out value="${portletSessionScope['webbis.mainform.hometown']}" />" maxlength="25"/>
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
											<option value="${hospital.longName}" <c:if test="${portletSessionScope['webbis.mainform.hospital'] eq hospital.longName}"> selected</c:if>><c:out value="${hospital.longName}"/></option>
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
										<textarea cols="40" rows="3" name="message" style="overflow:hidden"><c:out value="${portletSessionScope['webbis.mainform.message']}" /></textarea>
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
										<input type="text" name="webpage" value="<c:out value="${portletSessionScope['webbis.mainform.webpage']}" />"></input>
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
										<input type="text" name="e-mail" value="<c:out value="${portletSessionScope['webbis.mainform.e-mail']}" />"></input>
									</p>
								</div>
								<div class="yui-u"></div>
							</div>	
												
						</div>
						<div class="yui-u">
							<!-- 	<img class="icon" src="../img/add-32x32.png"><a id="addPic" href="#">Lägg till bild</a><br/> -->
							<input name="addImages" value="Lägg till bilder" type="submit"/><br/><br/>
							<input type="hidden" name="main-image" value="${portletSessionScope['webbis.mainform.main-image']}"/>
						
							<c:if test="${portletSessionScope['webbis.mainform.image1'] != null}" >
								<input type="hidden" name="image1" value="${portletSessionScope['webbis.mainform.image1']}"/>
								<div class="yui-g">	
									<div class="yui-u first">
										<img width="155" src="${portletSessionScope['webbis.mainform.image1']}"/><br/><br/>
									</div>
									<div class="yui-u">
										<input type="submit" name="remove-image1" value="Radera"/>
										<p>
											<a href="${portletSessionScope['webbis.mainform.image1']}" target="_new">Se stor bild</a>
										</p>
										<p>
											<c:if test="${'image1' eq portletSessionScope['webbis.mainform.main-image']}">Denna bild är förstabild</c:if>
											<c:if test="${'image1' ne portletSessionScope['webbis.mainform.main-image']}"><input type="submit" name="image1-main-image" value="Sätt som förstabild"/></c:if>
										</p>
										<p>
											<label for="image1-text">Bildtext:<br/></label>
											<input type="text" name="image1-text" class="text" value="${portletSessionScope['webbis.mainform.image1-text']}"  maxlength="80"></input><br/>
										</p>						
									</div>		
								</div>
											
							</c:if>
							<c:if test="${portletSessionScope['webbis.mainform.image2'] != null}" >
								<input type="hidden" name="image2" value="${portletSessionScope['webbis.mainform.image2']}"/>
								<div class="yui-g">	
									<div class="yui-u first">
										<img width="155" src="${portletSessionScope['webbis.mainform.image2']}"/><br/><br/>
									</div>
									<div class="yui-u">
										<input type="submit" name="remove-image2" value="Radera"/>
										<p>
											<a href="${portletSessionScope['webbis.mainform.image2']}" target="_new">Se stor bild</a>
										</p>
										<p>
											<c:if test="${'image2' eq portletSessionScope['webbis.mainform.main-image']}">Denna bild är förstabild</c:if>
											<c:if test="${'image2' ne portletSessionScope['webbis.mainform.main-image']}"><input type="submit" name="image2-main-image" value="Sätt som förstabild"/></c:if>
										</p>
										<p>
											<label for="image2-text">Bildtext:<br/></label>
											<input type="text" name="image2-text" class="text" value="${portletSessionScope['webbis.mainform.image2-text']}"  maxlength="80"></input><br/>
										</p>						
									</div>	
								</div>
							</c:if>
							<c:if test="${portletSessionScope['webbis.mainform.image3'] != null}" >
								<input type="hidden" name="image3" value="${portletSessionScope['webbis.mainform.image3']}"/>
								<div class="yui-g">	
									<div class="yui-u first">
										<img width="155" src="${portletSessionScope['webbis.mainform.image3']}"/><br/><br/>
									</div>
									<div class="yui-u">
										<input type="submit" name="remove-image3" value="Radera"/>
										<p>
											<a href="${portletSessionScope['webbis.mainform.image3']}" target="_new">Se stor bild</a>
										</p>
										<p>
											<c:if test="${'image3' eq portletSessionScope['webbis.mainform.main-image']}">Denna bild är förstabild</c:if>
											<c:if test="${'image3' ne portletSessionScope['webbis.mainform.main-image']}"><input type="submit" name="image3-main-image" value="Sätt som förstabild"/></c:if>
										</p>
										<p>
											<label for="image3-text">Bildtext:<br/></label>
											<input type="text" name="image3-text" class="text" value="${portletSessionScope['webbis.mainform.image3-text']}" maxlength="80"></input><br/>
										</p>							
									</div>	
								</div>
								
							</c:if>
							<c:if test="${portletSessionScope['webbis.mainform.image4'] != null}" >
								<input type="hidden" name="image4" value="${portletSessionScope['webbis.mainform.image4']}"/>
								<div class="yui-g">	
									<div class="yui-u first">
										<img width="155" src="${portletSessionScope['webbis.mainform.image4']}"/><br/><br/>
									</div>
									<div class="yui-u">
										<input type="submit" name="remove-image4" value="Radera"/>
										<p>
											<a href="${portletSessionScope['webbis.mainform.image4']}" target="_new">Se stor bild</a>
										</p>
										<p>
											<c:if test="${'image4' eq portletSessionScope['webbis.mainform.main-image']}">Denna bild är förstabild</c:if>
											<c:if test="${'image4' ne portletSessionScope['webbis.mainform.main-image']}"><input type="submit" name="image4-main-image" value="Sätt som förstabild"/></c:if>
										</p>
										<p>
											<label for="image4-text">Bildtext:<br/></label>
											<input type="text" name="image4-text" class="text" value="${portletSessionScope['webbis.mainform.image4-text']}"  maxlength="80"></input>
										</p>							
									</div>
								</div>
							</c:if>
						</div> 
					</div>
					<div class="yui-g">
						<div class="yui-u first"></div>
						<div class="yui-u" style="text-align: right">
							<c:if test="${portletSessionScope['webbis.mainform.webbisId'] != null}"><input name="deleteWebbis" value="Radera webbis" type="submit"/></c:if>
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