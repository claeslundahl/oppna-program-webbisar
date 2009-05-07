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
          				<div class="yui-u first">				
          					
          				</div>
          				<div class="yui-u">
		                   
          				</div>
          				
          			</div>
          		</div>	
          	</div>
          	<div id="bd">
          		<div id="yui-main">
          		<h3><c:out value="${SearchResultPage.numberOfHits}"/> träffar <c:if test="${SearchResultPage.numberOfHits > 20}">- visar de första 20.</c:if><br/><br/>
                  </h3>
          			<div class="yui-u first">
                      <div class="search_item">
  			  		      <c:out value="${SearchResultPage.searchResults[0].header}"/>
                      </div>
                      <div class="search_info">
                        <c:out value="${SearchResultPage.searchResults[0].info}"/>
                      </div>
                      <div>
                      <a href="<portlet:actionURL><portlet:param name="webbisId" value="${SearchResultPage.searchResults[0].id}"/></portlet:actionURL>">
                      <c:if test="${SearchResultPage.searchResults[0].disabled}">Aktivera webbis</c:if>
                      <c:if test="${not SearchResultPage.searchResults[0].disabled}">Inaktivera webbis</c:if>
                      </a>
                      <br/><br/>
                      </div>
                      
                    </div>
                    <c:forEach var="sr" items="${SearchResultPage.searchResults}" begin="1">
          			<div class="yui-u">
                      <div class="search_item">
  			  		      <c:out value="${sr.header}"/>
                 
                      </div>
                      <div class="search_info">
                        <c:out value="${sr.info}"/>
                      </div>
                      <div>
                      <a href="<portlet:actionURL><portlet:param name="webbisId" value="${sr.id}"/></portlet:actionURL>">
                      <c:if test="${sr.disabled}">Aktivera webbis</c:if>
                      <c:if test="${not sr.disabled}">Inaktivera webbis</c:if>
                      </a>
                      <br/><br/>
                      </div>
                    </div>
                    </c:forEach>
          		</div>
          	</div>
          	<div id="ft">
              <div class="yui-b">
               
              </div>     
            </div>
          </div>
