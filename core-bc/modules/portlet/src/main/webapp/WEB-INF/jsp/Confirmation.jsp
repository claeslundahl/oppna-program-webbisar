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
				<div class="yui-b addwebbis">
					<div class="yui-g">
						<h3 class="webbis">Dina ändringar har sparats.</h3>
						<div class="yui-u first"  style="margin-left: 0.4em">
							<p>Vill du gå tillbaks och lägga till, redigera eller ta bort en webbis trycker du på tillbaka knappen.
							Är du klar med dina ändringar, kan du logga ut i övre högra hörnet. 
							</p>
						</div>
						<div class="yui-u" style="text-align: right">
<!--							<a href="<portlet:renderURL><portlet:param name="VIEW" value="SHOW_WEBBIS_LIST_VIEW"/></portlet:renderURL>">Tillbaka</a>-->
							<form action="<portlet:actionURL/>" method="post" name="back_from_confirm" id="back_from_confirm">			
								<input type="submit" name="backFromConfirm" value="Tillbaka"/>
							</form>		
						</div>
					</div>
				</div>		
		</div>
	</div>
	<div id="ft"></div>
</div>
