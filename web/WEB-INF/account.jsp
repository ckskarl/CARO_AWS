<%-- 
    Document   : landing
    Created on : Feb. 10, 2022, 10:34:14 a.m.
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="account.css" rel="stylesheet" type="text/css" media="all" />
        <!--<script type="text/javascript" src="index.js"></script>-->
        
        <title>Account</title>
        <script>
            var personalInfoEditMode = false;
            function editPersonalInfo()
            {
                if(personalInfoEditMode) {
                    personalInfoEditMode = false;
                    document.getElementById("personalInfoDisplay").hidden = false;
                    document.getElementById("personalInfoEditForm").hidden = true;
                } else {
                    personalInfoEditMode = true;
                    document.getElementById("personalInfoDisplay").hidden = true;
                    document.getElementById("personalInfoEditForm").hidden = false;
                }
            }
            var accountInfoEditMode = false;
            function editAccountInfo()
            {
                if(accountInfoEditMode) {
                    accountInfoEditMode = false;
                    document.getElementById("accountInfoDisplay").hidden = false;
                    document.getElementById("accountInfoEditForm").hidden = true;
                } else {
                    accountInfoEditMode = true;
                    document.getElementById("accountInfoDisplay").hidden = true;
                    document.getElementById("accountInfoEditForm").hidden = false;
                }
            }
        </script>
    </head>
    <body>
        <%@include file="jspf/header.jspf" %>
        <%@include file="jspf/sideBar.jspf" %>
        <div class="accountForm" style="width: 600px;">
            <h1>Account: </h1><br/>
            <div class="accountSubForm">
                
                <div id="personalInfoDisplay">
                    <br>
                    <div>${requestScope.user_first_name} ${requestScope.user_last_name}</div>
                    <div>Started: ${requestScope.date_of_hire}</div>
                    <div>Phone#: ${requestScope.user_phone_number}</div>
                    <div>Email#: ${requestScope.user_email}</div>
                </div>
                <div id="personalInfoEditForm" hidden="true">
                    <form action="AccountController" method="POST">
                        <div>
                            <input type="text" name="user_first_name" value="${requestScope.user_first_name}">
                            <input type="text" name="user_last_name" value="${requestScope.user_last_name}">
                        </div>
                        <div>
                             <label for="date_of_hire">Started: </label>
                             <input type="date" id="date_of_hire" name="date_of_hire" value="${requestScope.date_of_hire}">
                        </div>
                        <div>
                             <label for="user_phone_number">Phone#: </label>
                             <input type="text" id="user_phone_number" name="user_phone_number" value="${requestScope.user_phone_number}">
                        </div>
                        <div>
                             <label for="user_email">Email#: </label>
                             <input type="text" id="user_email" name="user_email" value="${requestScope.user_email}">
                        </div>
                        <div style="float: right;">
                            <input type="hidden" name="action" value="personalinfo_edit">
                            <input type="submit" value="Apply" style="margin-left: 7px; width: 60px; border: none; padding: 9px; border-radius: 4px; background-color: #ccc; color: black;">
                        </div>
                    </form>
                </div>
                <div class="accountSubFormHeader">
                    <button onclick="editPersonalInfo()">Edit</button>
                </div>
            </div><br/>
            <div class="accountSubForm">

                <div id="accountInfoDisplay">
                    <br>
                    <div>Username: ${requestScope.user_name}</div>
                    <div>Password: ******************</div>
                    <div>Clearance Level: ${requestScope.clearance_level}</div>
                </div>
                <div id="accountInfoEditForm" hidden="true">
                    <form action="AccountController" method="POST">
                        <div>
                             <label for="user_name">Username: </label>
                             <input type="text" id="user_name" name="user_name" value="${requestScope.user_name}">
                        </div>
                        <div>
                             <label for="user_password1">New Password: </label>
                             <input type="password" id="user_password1" name="user_password1">
                        </div>
                        <div>
                             <label for="user_password2">Confirm Password: </label>
                             <input type="password" id="user_password2" name="user_password2">
                        </div>
                        <div style="float: right;">
                            <input type="hidden" name="action" value="accountinfo_edit">
                            <input type="submit" value="Apply" style="margin-left: 7px; width: 60px; border: none; padding: 9px; border-radius: 4px; background-color: #ccc; color: black;">
                        </div>
                    </form>
                </div>
                <div class="accountSubFormHeader">
                    <button onclick="editAccountInfo()">Edit</button>
                </div>
            </div>
            <div class="accountSubForm">${requestScope.acc_message}</div>
        </div>
    </body>
</html>
