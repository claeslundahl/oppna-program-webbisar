<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 

<portlet:defineObjects />	

<div id="custom-doc">
	<div id="hd">
		<div class="yui-b" style="text-align: left"><h2>Inaktivera/aktivera webbis</h2></div>
		<div class="yui-b">
			<div class="yui-gb header">
				
			</div>
		</div>	
	</div>
	<div id="bd">
		<div id="yui-main">
			<form action="<portlet:actionURL/>"  method="post" id="search_webbis_form">			
				<div class="yui-b addwebbis">
					<c:if test="${WebbisChanged != null}"><h3>Webbisen har uppdaterats</h3></c:if>
				
					<div class="yui-g">
						<div class="yui-u first">
							Sök webbis: <input type="text" name="searchField"/>&nbsp;<input name="search" id="search" value="Sök" type="submit"/>
						</div>
						<div class="yui-u" style="text-align: right">
						</div>
					</div>
				</div>
				<div class="yui-b">
					<input name="reindex" id="reindex" value="Indexera om" type="submit"/>Detta tar ett antal minuter, så hav tålamod och tryck inte flera gånger...				
				</div>	
			</form>
		</div>
	</div>
	<div id="ft"></div>
</div>
