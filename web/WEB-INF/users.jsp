<%-- 
    Document   : asset
    Created on : Feb. 14, 2022, 3:29:05 p.m.
    Author     : Administrator
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="user.css" rel="stylesheet" type="text/css" media="all" />
        <!--<script type="text/javascript" src="index.js"></script>-->
        <script type="text/javascript" src="functions/sorttable.js"></script>
        <script type="text/javascript" src="functions/user_functions.js"></script>
        <script type="text/javascript" src="functions/b_r_functions.js"></script>
        <script>
            var userData =
            [<c:forTokens var="user" delims="," items="${users}">
                <c:set var="userDetails" value="${fn:split(user, ';')}" />
                ["${userDetails[0]}"            //Personnel Id
                    , "${userDetails[1]}" //Job ID
                    , "${userDetails[2]}" //Username
                    , "${userDetails[3]}" //Password
                    , "${userDetails[4]}" //First Name
                    , "${userDetails[5]}" //Last Name
                    , "${userDetails[6]}" //Date Hired
                    , "${userDetails[7]}" //Phone Num
                    , "${userDetails[8]}" //Email
                    , "${userDetails[9]}"],  //Clearance
            </c:forTokens>];
            
            function deleteUser(userId){
                document.getElementById("del_user_display").innerHTML = userId;
                document.getElementById("del_user_id").value = userId;
            }
            
            function editUser(userId){
                for (var i = 0; i < userData.length; i++) {
                    if (userId == userData[i][0])
                    {
                        //Set the edit user form fields fields
                        document.getElementById("edit_personel_id").value = userData[i][0];
                        document.getElementById("edit_job_category_id").value = userData[i][1];
                        document.getElementById("edit_user_name").value = userData[i][2];
                        document.getElementById("edit_user_password").value = userData[i][3];
                        document.getElementById("edit_user_first_name").value = userData[i][4];
                        document.getElementById("edit_user_last_name").value = userData[i][5];
                        document.getElementById("edit_date_of_hire").value = userData[i][6];
                        document.getElementById("edit_phone_number").value = userData[i][7];
                        document.getElementById("edit_user_email").value = userData[i][8];
                        document.getElementById("edit_clearance_level").value = userData[i][9];
                        break;
                    }
                }
            }
            
            function selectUser(userId)
            {
                for (var i = 0; i < userData.length; i++) {
                    if (userId === userData[i][0])
                    {
                        //Set the edit user form fields fields
                        document.getElementById("edit_personel_id").value = userData[i][0];
                        document.getElementById("edit_job_category_id").value = userData[i][1];
                        document.getElementById("edit_user_name").value = userData[i][2];
                        document.getElementById("edit_user_password").value = userData[i][3];
                        document.getElementById("edit_user_first_name").value = userData[i][4];
                        document.getElementById("edit_user_last_name").value = userData[i][5];
                        document.getElementById("edit_date_of_hire").value = userData[i][6];
                        document.getElementById("edit_phone_number").value = userData[i][7];
                        document.getElementById("edit_user_email").value = userData[i][8];
                        document.getElementById("edit_clearance_level").value = userData[i][9];
                        break;
                    }
                }
            }
            function searchUser() {
                var searchMenu = document.getElementById('searchType');
                var selection = searchMenu.options[searchMenu.selectedIndex];
                var input = (document.getElementById("searchInput").value).toLowerCase();
                var match = false;
                for (var i = 0; i < userData.length; i++)
                {
                    switch (selection.text) {
                        case 'Personnel ID':
                            if (((userData[i][0]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Job Category ID':
                            if (((userData[i][1]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Username':
                            if (((userData[i][2]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Password':
                            if (((userData[i][3]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'First Name':
                            if (((userData[i][4]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Last Name':
                            if (((userData[i][5]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Date of Hire':
                            if (((userData[i][6]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Phone Number':
                            if (((userData[i][7]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Email':
                            if (((userData[i][8]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        case 'Clearance Level':
                            if (((userData[i][9]).toLowerCase()).includes(input))
                                match = true;
                            break;
                        default:
                            if (((userData[i][0]).toLowerCase()).includes(input)) {  //pers id
                                match = true;
                            } else if (((userData[i][1]).toLowerCase()).includes(input)) {//job cat
                                match = true;
                            } else if (((userData[i][2]).toLowerCase()).includes(input)) {//username
                                match = true;
                            } else if (((userData[i][3]).toLowerCase()).includes(input)) {//passw
                                match = true;
                            } else if (((userData[i][4]).toLowerCase()).includes(input)) {//fname
                                match = true;
                            } else if (((userData[i][5]).toLowerCase()).includes(input)) {//lastn
                                match = true;
                            } else if (((userData[i][6]).toLowerCase()).includes(input)) {//DateOfHire
                                match = true;
                            } else if (((userData[i][7]).toLowerCase()).includes(input)) {//Phone
                                match = true;
                            } else if (((userData[i][8]).toLowerCase()).includes(input)) {//Email
                                match = true;
                            } else if (((userData[i][9]).toLowerCase()).includes(input)) {//clearance
                                match = true;
                            } else {
                                match = false;
                            }
                    }
                    if (match === false) {
                        document.getElementById(userData[i][0]).hidden = true;
                    } else {
                        document.getElementById(userData[i][0]).hidden = false;
                    }
                    match = false;
                }
            }

            function showMessage() {
                document.getElementById('showMessage-bg').style.visibility = 'visible';
                document.querySelector('.user').style.opacity = '0.3';
                document.querySelector('.userlist').style.opacity = '0.3';
            }
            function cancelMessage() {
                document.getElementById('showMessage-bg').style.visibility = 'hidden';
                document.querySelector('.user').style.opacity = '1';
                document.querySelector('.userlist').style.opacity = '1';
            }
        </script>
        <title>Users</title>
    </head>
    <body>
        <%@include file="jspf/header.jspf" %>
        <%@include file="jspf/sideBar.jspf" %>
        <div class="user">
            <div id="topBar" class="topbar">
                <a >USER</a>
                <a href="#" onClick="addUserButton()" >Add User</a>
                <a onclick="backupButton()" href="#" >Backup</a>
                <a onclick="restoreButton()" href="#">Restore</a>
                <a onclick="clearArchiveButton()" href="#">Clear Archive</a>
                <a style="color:red">${requestScope.message}<br></a>
            </div>

            <div class="undertopbar">
                <input id="searchInput" type="text" placeholder="Filter Users..." oninput="searchUser()" name="search" autofocus>
                <!-- DROP DOWN MENU -->
                <select id = "searchType" onchange = "dropdownList()" >
                    <option> Search by... </option>
                    <option> Personnel ID </option>
                    <option> Job Category ID </option>
                    <option> Username </option>
                    <option> Password </option>
                    <option> First Name </option>
                    <option> Last Name </option>
                    <option> Date of Hire </option>
                    <option> Phone Number </option>
                    <option> Email </option>
                    <option> Clearance Level </option>
                </select>
                <!--<a href="#">Delete Bulk</a>
                <a href="#" onClick="addDeleteButton()">Delete...</a>-->

            </div>
        </div>

        <div class="notes">
            <br>
            Click on the header to sort ascending/descending
            <form class="developer">
                Check to display the specific column<br>
                <input id='SerialNum' type="checkbox" name="SerialNum" value=4 checked onChange='javascript:show_hide_column(col_no.value, true)'><label for="SerialNum"> Serial Number</label><br>
                Enter column no: <input type='text' name=col_no><br>

                <input type='button' onClick='javascript:show_hide_column(col_no.value, true);' value='show'>
                <input type='button' onClick='javascript:show_hide_column(col_no.value, false);' value='hide'>
            </form>
            <br><br>
        </div>
            
        <!<!-- listing all the users -->
        <div class="userlist">
            <table id='id_of_table'  class="sortable">
                <thead>
                    <tr>
                        <th>Personnel ID</th>
                        <th>Job Category ID</th>
                        <th>Username</th>
                        <th>Password</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Date of Hire</th>
                        <th>Phone Number</th>
                        <th>Email</th>
                        <th>Clearance Level</th>
                        <th>Edit</th>
                        <th>Current Status(Click to toggle)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forTokens var="user" delims="," items="${users}">
                        <c:set var="userDetails" value="${fn:split(user, ';')}" />
                        <tr id="${userDetails[0]}" style="hidden">
                            <td>${userDetails[0]}</td>
                            <td>${userDetails[1]}</td>
                            <td>${userDetails[2]}</td>
                            <td>
                                ********
                            </td>
                            <td>${userDetails[4]}</td>
                            <td>${userDetails[5]}</td>
                            <td>${userDetails[6]}</td>
                            <td>${userDetails[7]}</td>
                            <td>${userDetails[8]}</td>
                            <td>${userDetails[9]}</td>
                            <td style="width: 100px;"><center><button onclick="editUser(${userDetails[0]});addEditButton()" class="edit">Edit</button></center></td>
                            <c:choose>
                                <c:when test="${userDetails[10]==1}">
                                    <td><center><button onclick="deleteUser(${userDetails[0]});addDeleteButton()" class="edit">
                                            Enabled
                                    </button></center></td>
                                </c:when>
                                <c:otherwise>
                                    <td><center><button onclick="deleteUser(${userDetails[0]});addDeleteButton()" class="delete">
                                            Disabled
                                        </button></center></td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forTokens>
                </tbody>
            </table>
        </div>
            <br><br><br>
        <!-- Add User Form -->
        <div id="adduser-bg" class="adduser-bg">
            <div class="adduser">
                <form id="addUser-form" action="UserController" method="POST">
                    <h1>Add User: </h1><br>
                            <input type="hidden" name="action" value="addUser">
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Personnel ID: </label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="personel_id" required placeholder="Number Only">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Job ID: </label>
                                </div>
                                <div class="col-2">
                                    <select name="job_category_id" id="job_category_id" required>
                                            <c:forTokens var="tJobCat" delims="," items="${jobCat}">
                                                <c:set var="jCat" value="${fn:split(tJobCat, ';')}" />
                                                <option value="${jCat[0]}">${jCat[1]}</option>
                                            </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Username: </label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="user_name" required placeholder="20 Characters at most">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Password: </label>
                                </div>
                                <div class="col-2">
                                    <input type="password" name="user_password" required placeholder="20 Characters at most">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">First Name: </label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="user_first_name" required placeholder="First Name">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Last Name: </label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="user_last_name" required placeholder="Last Name">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Date Hired: </label>
                                </div>
                                <div class="col-2">
                                    <input type="date"  name="date_of_hire" required> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Phone Number: </label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="user_phone_number" required placeholder="10 digits e.g.1234567890"> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Email: </label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="user_email" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Clearance Level: </label>
                                </div>
                                <div class="col-2">
                                    <select name="clearance_level" id="clearance_level" required>
                                            <c:forTokens var="tClrnce" delims="," items="${clearances}">
                                                <c:set var="clr" value="${fn:split(tClrnce, ';')}" />
                                                <option value="${clr[0]}">${clr[1]}</option>
                                            </c:forTokens>
                                    </select>
                                </div>
                            </div><br>
                            <div class="row">
                                <button onClick="window.location.reload();" type="button">Cancel</button>
                                <input type="submit" value="Confirm">  
                            </div>
                </form>
            </div>
        </div>

        <!-- Edit user Form -->
        <div id="editUser-bg" class="editUser-bg">
            <div class="editUser">
                <form id="editUser-form" action="UserController" method="POST">
                    <h1>Edit User: </h1><br>
                        <input type="hidden" name="action" value="editUser">
                        <div class="row">
                            <div class="col-1">
                                <label>Personnel ID: </label>
                            </div>
                            <div class="col-2">
                                <input type="text" id="edit_personel_id" name="personel_id" readonly>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Job ID: </label>
                            </div>
                            <div class="col-2">
                                <select name="job_category_id" id="edit_job_category_id">
                                    <option value=""></option>
                                    <c:forTokens var="tJobCat" delims="," items="${jobCat}">
                                        <c:set var="jCat" value="${fn:split(tJobCat, ';')}" />
                                        <option value="${jCat[0]}">${jCat[1]}</option>
                                    </c:forTokens>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Username: </label>
                            </div>
                            <div class="col-2">
                                <input type="text"  id="edit_user_name" name="user_name">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Password: </label>
                            </div>
                            <div class="col-2">
                                <input type="password"  id="edit_user_password" name="user_password">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>First Name: </label>
                            </div>
                            <div class="col-2">
                                <input type="text"  id="edit_user_first_name" name="user_first_name">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Last Name: </label>
                            </div>
                            <div class="col-2">
                                <input type="text"  id="edit_user_last_name" name="user_last_name">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Date Hired: </label>
                            </div>
                            <div class="col-2">
                                <input type="date"  id="edit_date_of_hire" name="date_of_hire">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Phone Number: </label>
                            </div>
                            <div class="col-2">
                                <input type="text"  id="edit_phone_number" name="user_phone_number">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Email: </label>
                            </div>
                            <div class="col-2">
                                <input type="text"  id="edit_user_email" name="user_email">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label>Clearance Level: </label>
                            </div>
                            <div class="col-2">
                                <select name="clearance_level" id="edit_clearance_level">
                                    <option value=""></option>
                                    <c:forTokens var="tClrnce" delims="," items="${clearances}">
                                        <c:set var="clr" value="${fn:split(tClrnce, ';')}" />
                                        <option value="${clr[0]}">${clr[1]}</option>
                                    </c:forTokens>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <input type="reset" onclick="cancelEditUser()" value="Cancel">
                            <input type="submit" value="Confirm">
                        </div>
                </form>
            </div>
        </div>

        <!-- Delete user Form -->
        <div id="deleteUser-bg" class="deleteUser-bg">
            <div class="deleteUser">
                <h1>Toggle User Status: </h1><br>

                <form id="delUser-form" action="UserController" method="POST">
                    <input type="hidden" name="action" value="deleteUser">
                    <input type="hidden" id="del_user_id" name="del_user_id">
                    <input type="hidden" id="del_user_state" name="del_user_state">
                    <p>You are about to toggle User (User id: <a id="del_user_display"></a>).<br/>
                        Do you want to proceed?</p><br>
                    <input type="reset" onclick="cancelDelete()" value="Cancel"> <input type="submit" value="Confirm">
                </form>
            </div>
        </div>

        <!-- Show Message -->
        <div id="showMessage-bg" class="deleteUser-bg">
            <div class="row">
                <h1>${requestScope.messageHeader}</h1><br>
                ${requestScope.message}<br>
                <input type="reset" onclick="cancelMessage()" value="OK">
            </div>
        </div>
        <script>
            if(${requestScope.showMessage}){
                showMessage();
            }
        </script>
        
        <!-- Backup Form -->
        <div id="Backup-bg" class="backup-bg">
            <div class="backup">
                <form id="backup-form" action="BackupRestoreController" method="POST">
                    <h1>Backup </h1><br>
                    <table class="addForm">
                        <td>
                            <input type="hidden" name="backupOrRestore" value="backup">
                            <div class="row">
                                <div class="col-1">
                                    <label for="backupName">Name of backup:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="backupName" placeholder="ex. back.bak">
                                </div>
                            </div>

                            <br>
                            <div class="row">
                                <button onClick="window.location.reload();" type="button">Cancel</button>
                                <input onClick="return backupConfirm();
                                       " type="submit" value="Confirm">
                            </div>   
                        </td>         
                    </table>
                </form>
            </div>
        </div>


        <!-- Restore Form -->
        <div id="Restore-bg" class="restore-bg">
            <div class="restore">
                <form id="restore-form" action="BackupRestoreController" method="POST">
                    <h1>Restore </h1><br>
                    <table class="addForm">
                        <td>

                            <input type="hidden" name="backupOrRestore" value="restore">

                            <div class="row">
                                <div class="col-1">
                                    <label for="backupName">Pick a backup file to restore:</label>
                                </div>
                                <div class="col-2">
                                    <input type="file"  name="restoreName" >
                                </div>
                            </div>


                            <br>
                            <div class="row">
                                <button onClick="window.location.reload();" type="button">Cancel</button>
                                <input onClick="return restoreConfirm();
                                       " type="submit" value="Confirm">
                            </div>   

                        </td>         
                    </table>
                </form>
            </div>
        </div>

        
        <!-- Clear Archive Form -->
        <div id="ClearArchive-bg" class="clearArchive-bg">
            <div class="clearArchive">
                <form id="clearArchive-form" action="BackupRestoreController" method="POST">
                    <h1>By clicking Confirm all archive data older than 5 years will be removed!</h1><br>
                    <table class="addForm">
                        <td>
                            <input type="hidden" name="backupOrRestore" value="clearArchive">
                            <div class="row">
                                <button onClick="window.location.reload();" type="button">Cancel</button>
                                <input type="submit" value="Confirm">
                            </div>   

                        </td>         
                    </table>
                </form>
            </div>
        </div>
    </body>
</html>
