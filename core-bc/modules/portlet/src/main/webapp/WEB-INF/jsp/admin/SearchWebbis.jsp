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
    <div class="yui-g">
      <div class="yui-u first" style="text-align: left">
        <h2>Inaktivera/aktivera webbis</h2>
      </div>
      <div class="yui-u">
      </div>
    </div>
	</div>
	<div>
		<div id="yui-main">
			<form action="<portlet:actionURL/>" method="post" id="search_webbis_form">			
				<div class="yui-b addwebbis">
					<c:if test="${WebbisChanged != null}"><h3>Webbisen har uppdaterats</h3></c:if>
				
					<div class="yui-g">
						<div class="yui-u first" style="margin-left: 0.4em">
							Sök webbis: <input type="text" name="searchField"/>&nbsp;<input name="searchButton" id="searchButton" value="&nbsp;Sök&nbsp;" type="submit"/>
						</div>
						<div class="yui-u" style="text-align: right">
						</div>
					</div>
          <br/>
          <div class="yui-g">
            <div class="yui-u first" style="margin-left: 0.4em">
              <input name="reindex" id="reindex" value="&nbsp;Indexera om&nbsp;" type="submit"/>
              <br/>
              <span style="white-space:nowrap">Att indexera om kan ta ett antal minuter, så hav tålamod och tryck inte flera gånger...</span>
            </div>
            <div class="yui-u" style="text-align: right">
            </div>
          </div>
				</div>
			</form>
		</div>
	</div>
	<div id="ft"></div>
</div>
