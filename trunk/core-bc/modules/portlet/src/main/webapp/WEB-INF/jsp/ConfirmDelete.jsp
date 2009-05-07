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
		<form action="<portlet:actionURL><portlet:param name="webbisId" value="${requestScope.webbisId}"/></portlet:actionURL>" method="post" id="webbis_delete_form">			
			<div class="yui-b addwebbis">
				<div class="yui-g">
					<h3 class="webbis">Ta bort webbis</h3>
					<div class="yui-u first"  style="margin-left: 0.4em">
						<p>Är du säker på att du vill ta bort webbisen?</p>
					</div>
					<div class="yui-u" style="text-align: right">
						<input type="submit" name="cancelDelete" value="Avbryt"/>
						<input type="submit" name="confirmDelete" value="Ta bort"/>
					</div>
				</div>
			</div>
		</form>		
	    </div>
	</div>
	<div id="ft"></div>
</div>
