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
				<div class="yui-b addwebbis">
					<div class="yui-g">
						<h3 class="webbis">Dina ändringar har sparats.</h3>
						<div class="yui-u first"  style="margin-left: 0.4em">
							<p>Vill du gå tillbaks och lägga till, redigera eller ta bort en webbis trycker du på tillbaka knappen.
							Är du klar med dina ändringar, kan du logga ut i övre högra hörnet. 
							</p>
              <br/>
              <p>Notera att det kan ta en stund innan ändringen syns i sökresultat och liknande.</p>
						</div>
						<div class="yui-u" style="text-align: right">
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
