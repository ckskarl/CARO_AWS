<%-- 
    Document   : login
    Created on : Feb. 5, 2022, 11:04:24 a.m.
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="login.css" rel="stylesheet" type="text/css" media="all" />
        <title>Login</title>
    </head>
    <body>
        <%@include file="jspf/header.jspf" %>
        <br>
        <div class="login">
            <form action="authenticateUser" method="POST">
                <h1>Login</h1>
                <div class="content">
                    <input type="hidden" name="action" value="login">
                    <div class="input-field">
                        <input type="text" placeholder="Username" name="username" autofocus >
                    </div>
                    <div class="input-field">
                        <input type="password" placeholder="Password" name="password">
                    </div>
                    <br>${message}<br>
                    <a href="placeholder" class="link">Forgot your password?</a>
                    <br>
                </div>
                <div class="action">
                    <button>SIGN IN</button>
                </div>
                    <br>
            </form>
        </div>
    </body>
</html>
