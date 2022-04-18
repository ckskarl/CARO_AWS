<%-- 
    Document   : landing
    Created on : Feb. 10, 2022, 10:34:14 a.m.
    Author     : Administrator
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="index.css" rel="stylesheet" type="text/css" media="all" />
       
        <script type="text/javascript" src="functions/sorttable.js"></script>
        <script type="text/javascript" src="functions/asset_functions.js"></script>
        
        <title>Home</title>
    </head>
    <body>
        <%@include file="jspf/header.jspf" %>
        <c:choose>
                        <c:when test="${sessionScope.clearanceLv==0}">
                <%@include file="jspf/sideBar.jspf" %>
            </c:when>
            <c:otherwise>
                <%@include file="jspf/sideBarOperationalUser.jspf" %>
            </c:otherwise>
        </c:choose>
        <br>
        
        
        <div class="homePage">
            <h1>Welcome, ${sessionScope.greetUser}!</h1>
            <div class="bord"></div>
        </div>
        
        
                                        
    </body>
</html>
