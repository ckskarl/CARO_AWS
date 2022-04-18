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
        <link href="asset.css" rel="stylesheet" type="text/css" media="all" />
        <!--<script type="text/javascript" src="index.js"></script>-->
        <script type="text/javascript" src="functions/sorttable.js"></script>
        <script type="text/javascript" src="functions/asset_functions.js"></script>
        <script>
            var assetData =
                    [<c:forTokens var="asset" delims="," items="${assets}"><c:set var="assetDetails" value="${fn:split(asset, ';')}" />
                        ["${assetDetails[0]}"
                                , "${assetDetails[1]}"
                                , "${assetDetails[2]}"
                                , "${assetDetails[3]}"
                                , "${assetDetails[4]}"
                                , "${assetDetails[5]}"
                                , "${assetDetails[6]}"
                                , "${assetDetails[7]}"
                                , "${assetDetails[8]}"
                                , "${assetDetails[9]}"
                                , "${assetDetails[10]}"
                                , "${assetDetails[11]}"
                                , "${assetDetails[12]}"
                                , "${assetDetails[13]}"
                                , "${assetDetails[14]}"
                                , "${assetDetails[15]}"
                                , "${assetDetails[16]}"
                                , "${assetDetails[17]}"
                                , "${assetDetails[18]}"
                                , "${assetDetails[19]}"
                                , "${assetDetails[20]}"
                                , "${assetDetails[21]}"
                                , "${assetDetails[22]}"
                                , "${assetDetails[23]}"
                                , "${assetDetails[24]}"
                                , "${assetDetails[25]}"
                                , "${assetDetails[26]}"
                                , "${assetDetails[27]}", "${assetDetails[28]}", "${assetDetails[29]}"
                                , "${assetDetails[30]}", "${assetDetails[31]}", "${assetDetails[32]}"],
            </c:forTokens>];

            var assetCategoryData =
                    [<c:forTokens var="assetCategory" delims="," items="${assetCategories}"><c:set var="assetCategoryDetails" value="${fn:split(assetCategory, ';')}" />
                        ["${assetCategoryDetails[0]}"
                                , "${assetCategoryDetails[1]}"],
            </c:forTokens>];

            function selectAsset(assetId)
            {
                //alert("");
                for (var i = 0; i < assetData.length; i++) {
                    if (assetId === assetData[i][0])
                    {
                        //Set the edit asset form fields fields
                        document.getElementById("edit_asset_id").value = assetData[i][0];
                        document.getElementById("edit_asset_name").value = assetData[i][1];
                        document.getElementById("edit_asset_desc").value = assetData[i][2];
                        document.getElementById("edit_photo_link").value = assetData[i][3];
                        document.getElementById("edit_asset_category").value = assetData[i][29];
                        document.getElementsByClassName("OLD_asset_category")[0].innerHTML = assetData[i][4];
                        var pDate = assetData[i][5].toLocaleString().replace('a', '');
                        document.getElementById("edit_purchase_date").value = pDate;
                        document.getElementById("edit_cost").value = assetData[i][6];
                        document.getElementById("edit_depreciated_value").value = assetData[i][7];
                        document.getElementById("sold_price").value = assetData[i][7];
                        document.getElementById("edit_location").value = assetData[i][26];
                        document.getElementsByClassName("OLD_asset_location")[0].innerHTML = assetData[i][8];
                        document.getElementById("edit_added_by_email").value = assetData[i][11];
                        document.getElementById("edit_dimension").value = assetData[i][12];
                        document.getElementById("edit_IP").value = assetData[i][13];
                        document.getElementById("edit_MAC").value = assetData[i][14];
                        document.getElementById("edit_serial_number").value = assetData[i][15];
                        document.getElementById("edit_hostname").value = assetData[i][16];
                        document.getElementById("edit_service_tag").value = assetData[i][17];
                        document.getElementById("edit_comment_on_asset").value = assetData[i][18];
                        document.getElementById("edit_brand").value = assetData[i][27];
                        document.getElementsByClassName("OLD_asset_brand")[0].innerHTML = assetData[i][19];
                        document.getElementById("edit_vendor").value = assetData[i][28];
                        document.getElementsByClassName("OLD_asset_vendor")[0].innerHTML = assetData[i][20];
                        document.getElementById("edit_assigned_to_user_email").value = assetData[i][24];
                        document.getElementById("edit_assigned_to_OLD_personel_id").value = assetData[i][25];
                        document.getElementById("edit_deprec_info_id").value = assetData[i][30];
                        document.getElementsByClassName("OLD_deprec_info")[0].innerHTML = assetData[i][31];
                        document.getElementById("edit_OS").value = assetData[i][32];
                        //document.getElementsByClassName("edit_assigned_to_OLD_user_name")[0].innerHTML= assetData[i][23];
                        //document.getElementById("edit_assigned_to_asset_id").value = assetData[i][0];
                        //Set the delete asset target
                        document.getElementById("del_asset_id").value = assetData[i][0];
                        document.getElementById("del_assigned_to_id").value = assetData[i][25];
                        document.getElementById("del_asset_display").innerHTML = assetData[i][0];
                        break;
                    }
                }
            }
        </script>
        <title>Asset</title>
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
        <div class="asset">
            <div id="topBar" class="topbar">
                <a >Assets</a>
                <a href="/CARO/assetController?action=viewCategories">Categories</a>
                <a href="#" onclick="importFileButton()" >Import</a>
                <a href="#" onclick="addAssetButton()" >Add Asset</a>
                <!--<a href="/CARO/assetController?action=importFile">test import</a>
                <a href="/CARO/import_excel_test.jsp">generate_json</a>-->
            </div>

            <div class="undertopbar">
                &nbsp;<input id="searchInput" type="text" placeholder="Filter Assets..." oninput="searchAsset()" name="search" autofocus>
                <!-- DROP DOWN MENU -->
                <select id = "searchType" onchange = "dropdownList()" >
                    <option> Search by... </option>
                    <option> Asset ID </option>
                    <option> Name </option>
                    <option> Description </option>
                    <option> Category </option>
                    <option> Purchase Date </option>
                    <option> Cost </option>
                    <option> Depreciated Value </option>
                    <option> Location </option>
                    <option> Added By </option>
                    <option> Dimension </option>
                    <option> Serial Number </option>
                    <option> Comment </option>
                    <option> Brand </option>
                    <option> Vendor </option>
                    <option> Vendor Address </option>
                    <option> Vendor Phone </option>
                </select>

                <script>
                    function searchAsset() {
                        var searchMenu = document.getElementById('searchType');
                        var selection = searchMenu.options[searchMenu.selectedIndex];
                        var input = (document.getElementById("searchInput").value).toLowerCase();
                        var match = false;
                        for (var i = 0; i < assetData.length; i++)
                        {
                            switch (selection.text) {
                                case 'Asset ID':
                                    if (((assetData[i][0]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Name':
                                    if (((assetData[i][1]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Description':
                                    if (((assetData[i][2]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Category':
                                    if (((assetData[i][4]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Purchase Date':
                                    if (((assetData[i][5]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Cost':
                                    if (((assetData[i][6]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Depreciated Value':
                                    if (((assetData[i][7]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Location':
                                    if (((assetData[i][8]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Added By':
                                    if (((assetData[i][10]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Dimension':
                                    if (((assetData[i][12]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Serial Number':
                                    if (((assetData[i][15]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Comment':
                                    if (((assetData[i][18]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Brand':
                                    if (((assetData[i][19]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Vendor':
                                    if (((assetData[i][20]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Vendor Address':
                                    if (((assetData[i][21]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                case 'Vendor Phone':
                                    if (((assetData[i][22]).toLowerCase()).includes(input))
                                        match = true;
                                    break;
                                default:
                                    if (((assetData[i][0]).toLowerCase()).includes(input)) {  //asset id
                                        match = true;
                                    } else if (((assetData[i][1]).toLowerCase()).includes(input)) {//asset name
                                        match = true;
                                    } else if (((assetData[i][2]).toLowerCase()).includes(input)) {//Desc
                                        match = true;
                                    } else if (((assetData[i][4]).toLowerCase()).includes(input)) {//catag
                                        match = true;
                                    } else if (((assetData[i][5]).toLowerCase()).includes(input)) {//pur date
                                        match = true;
                                    } else if (((assetData[i][6]).toLowerCase()).includes(input)) {//cost
                                        match = true;
                                    } else if (((assetData[i][7]).toLowerCase()).includes(input)) {//depre
                                        match = true;
                                    } else if (((assetData[i][8]).toLowerCase()).includes(input)) {//Location
                                        match = true;
                                    } else if (((assetData[i][10]).toLowerCase()).includes(input)) {//add by
                                        match = true;
                                    } else if (((assetData[i][12]).toLowerCase()).includes(input)) {//dimension
                                        match = true;
                                    } else if (((assetData[i][15]).toLowerCase()).includes(input)) {//serial num
                                        match = true;
                                    } else if (((assetData[i][18]).toLowerCase()).includes(input)) {//comment
                                        match = true;
                                    } else if (((assetData[i][19]).toLowerCase()).includes(input)) {//brand
                                        match = true;
                                    } else if (((assetData[i][20]).toLowerCase()).includes(input)) {//vendor
                                        match = true;
                                    } else if (((assetData[i][21]).toLowerCase()).includes(input)) {//Vendor Address
                                        match = true;
                                    } else if (((assetData[i][22]).toLowerCase()).includes(input)) {//Vendor phone
                                        match = true;
                                    } else {
                                        match = false;
                                    }
                            }
                            if (match === false) {
                                document.getElementById(assetData[i][0]).hidden = true;
                            } else {
                                document.getElementById(assetData[i][0]).hidden = false;
                            }
                            match = false;
                        }
                    }
                </script>
            </div>
        </div>
        <div class="notes">
            <p style="color:red">${requestScope.archivemessage}<br></p>
            <p style="color:red">${requestScope.message}<br></p>
            Click on the header to sort ascending/descending<br>
            <c:choose>
                <c:when test="${sessionScope.clearanceLv==0}">
                    Click on the name of the asset to View/Edit<br>
                    Click on the depreciated value to update the value base on registered depreciation formula
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>

        </div>
        <div id="searchResult">
        </div>
        <br>
        <div class="assetlist">
            <table id='allAssetDisplay'  class="sortable">
                <thead>
                    <tr>
                        <c:choose>
                            <c:when test="${sessionScope.clearanceLv==0}">
                                <th></th>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        <th>Asset Id</th>
                        <th>Name</th>
                        <th>Description(Picture)</th>
                        <th>Category</th>
                        <th>Purchase Date</th>
                        <th>Cost</th>
                        <th>Depreciated Value</th>
                        <th>Location(Phone)</th>
                        <th>Added By </th>
                        <th>Dimension</th>
                        <th>IP</th>
                        <th>MAC</th>
                        <th class='serial_data'>Serial Number</th>
                        <th>Hostname</th>
                        <th>Service Tag</th>
                        <th>OS</th>
                        <th>Comment</th>
                        <th>Brand</th>
                        <th>Vendor</th>
                        <th>Vendor address</th>
                        <th>Vendor Phone</th>
                        <th>Assigned to </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forTokens var="asset" delims="," items="${assets}">
                        <c:set var="assetDetails" value="${fn:split(asset, ';')}" />
                        <tr id="${assetDetails[0]}" style="hidden">
                            <c:choose>
                                <c:when test="${sessionScope.clearanceLv==0}">
                                    <td><button onclick="selectAsset('${assetDetails[0]}');deleteAssetButton();">Delete</button></td>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>


                            <td>${assetDetails[0]}</td>
                            <c:choose>
                                <c:when test="${sessionScope.clearanceLv==0}">
                                <td><u style="cursor:pointer;color:blue" onclick="selectAsset('${assetDetails[0]}');
                                    editAssetButton();">${assetDetails[1]}</u></td></c:when>
                                <c:otherwise>
                                <td>${assetDetails[1]}</td>
                                </c:otherwise>
                            </c:choose>
                            
                            <td class='serial_data'><br>
                                <c:choose>
                                    <c:when test="${assetDetails[3]!='No Data'}">
                                        <c:if test="${assetDetails[3]!='N/A'}">
                                            <img class="asset_img" src="${assetDetails[3]}" width="50">
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                    </c:otherwise>
                                </c:choose><br>
                                ${assetDetails[2]}
                            </td>
                            <td>${assetDetails[4]}</td>
                            <td>${assetDetails[5]}</td>
                            <td>${assetDetails[6]}</td>
                            
                            <c:choose>
                                <c:when test="${sessionScope.clearanceLv==0}">
                                    <td><a href="DepreciationController?action=calDeprec&asset_id=${assetDetails[0]}&deprec_info_id=${assetDetails[30]}"> 
                                    ${assetDetails[7]}</a></td> 
                                </c:when>
                                <c:otherwise>
                                <td>${assetDetails[7]}</td>
                                </c:otherwise>
                            </c:choose>
                            
                            <td>${assetDetails[8]} (${assetDetails[9]})</td>
                            <td>${assetDetails[11]}</td>
                            <td>${assetDetails[12]}</td>
                            <td>${assetDetails[13]}</td>
                            <td>${assetDetails[14]}</td>
                            <!-- up to 31 -->
                            <td>${assetDetails[15]}</td>
                            <td>${assetDetails[16]}</td>

                            <td>${assetDetails[17]}</td>
                            <td>${assetDetails[32]}</td>
                            <td>${assetDetails[18]}</td>
                            <td>${assetDetails[19]}</td>
                            <td>${assetDetails[20]}</td>
                            <td>${assetDetails[21]}</td>
                            <td>${assetDetails[22]}</td>
                            <td>${assetDetails[24]}</td>
                        </tr>
                    </c:forTokens>
                </tbody>
            </table>
        </div>

        <!-- Add Asset Form -->
        <div id="addAsset-bg" class="addasset-bg">
            <div class="addasset">
                <form id="addAsset-form" action="assetController" method="POST">
                    <input type="hidden" name="action" value="addAsset">
                    <h1>Add Asset </h1><br>
                    <table class="addForm" style="border: none; box-shadow: none;">
                        <td>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Asset Id* <br>(at most 10 characters):</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="asset_id" required placeholder="2020AA001">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetName">Name*:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="asset_name" placeholder="Laptop" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetDesc">Description:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="asset_desc" placeholder="Finance Laptop #10">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="photo_link">Picture link</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="photo_link" placeholder="http://photo.jpg">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetCat">Category*:</label>
                                </div>
                                <div class="col-2">
                                    <select name="asset_category" id="asset_category">
                                        <c:forTokens var="assetCategory" delims="," items="${assetCategories}">
                                            <c:set var="assetCategoryDetails" value="${fn:split(assetCategory, ';')}" />
                                            <option value="${assetCategoryDetails[0]}">${assetCategoryDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="purchaseDate">Purchase Date:</label>
                                </div>
                                <div class="col-2">
                                    <input type="Date" id="today" name="purchase_date" value="">
                                </div>
                                <script>    var today = new Date();
                                    var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();

                                    document.getElementById("today").value = date;
                                </script>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="cost">Cost:</label>
                                </div>
                                <div class="col-2">
                                    <input type="number"  name="cost" step="0.01" value=0.00>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="cost">Depreciated Value:</label>
                                </div>
                                <div class="col-2">
                                    <input type="number" name="depreciated_value" step="0.01" value=0.00>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetCat">Depreciated Rate:</label>
                                </div>
                                <div class="col-2">
                                    <select name="deprec_info_id" id="deprec_info_id">
                                        <c:forTokens var="deprec" delims="," items="${deprecs}">
                                            <c:set var="deprecDetails" value="${fn:split(deprec, ';')}" />
                                            <option value="${deprecDetails[0]}">${deprecDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="location_id">Location</label>
                                </div>
                                <div class="col-2">
                                    <select name="location_id" id="location">
                                        <c:forTokens var="Location" delims="," items="${Locations}">
                                            <c:set var="LocationDetails" value="${fn:split(Location, ';')}" />
                                            <option value="${LocationDetails[0]}">${LocationDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="added_by">Added by:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="added_by_email" value="${sessionScope.userEmail}" readonly> 
                                    <input type="hidden"  name="added_by" value="${sessionScope.userId}" readonly> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="OS">OS</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="OS" name="OS" placeholder="Windows 11">
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="row">
                                <div class="col-1">
                                    <label for="dimension">Dimension</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="dimension" placeholder="15 inch (screen)">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="IP">IP</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="IP" placeholder="128.0.0.1" >
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="MAC">MAC</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="MAC" placeholder="00:00:5e:00:53:af">
                                </div>
                            </div>                        
                            <div class="row">
                                <div class="col-1">
                                    <label for="serialNum">Serial Number</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="serial_number" placeholder="GFHG2019SDGM">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="hostname">Host Name</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="hostname" placeholder="www.caro.ca">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="service_tag">Service Tag</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="service_tag" placeholder="ST1234">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="comment_on_asset">Comment</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="comment_on_asset" placeholder="Expected to use for 5 years (2022-2026)">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="brand">Brand*:</label>
                                </div>
                                <div class="col-2">
                                    <select name="brand_id" id="brand">
                                        <c:forTokens var="assetBrand" delims="," items="${assetBrands}">
                                            <c:set var="assetBrandDetails" value="${fn:split(assetBrand, ';')}" />
                                            <option value="${assetBrandDetails[0]}">${assetBrandDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="Vendor">Vendor*:</label>
                                </div>
                                <div class="col-2">
                                    <select name="vendor_id" id="vendor">
                                        <c:forTokens var="assetVendor" delims="," items="${assetVendors}">
                                            <c:set var="assetVendorDetails" value="${fn:split(assetVendor, ';')}" />
                                            <option value="${assetVendorDetails[0]}">${assetVendorDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="Assigned_to">Assigned to</label>
                                </div>
                                <div class="col-2">
                                    <select name="assigned_to_id" id="assigned_to">
                                        <option value="No one" >No one</option>
                                        <c:forTokens var="user" delims="," items="${users}">

                                            <c:set var="userDetails" value="${fn:split(user, ';')}" />
                                            <option value="${userDetails[0]}">${userDetails[4]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <br><br>*: required fields
                            <div class="row">
                                <button onclick="window.location.href = '/CARO/assetController?action=viewAssets'">Cancel</button>     
                                <input type="submit" value="Confirm">
                            </div>
                        </td>
                    </table>
                </form>
            </div>
        </div>


        <!-- Edit Asset Form -->
        <div id="editAsset-bg" class="editasset-bg">
            <div class="editasset">
                <form id="editAsset-form" action="assetController" method="POST">
                    <input type="hidden" name="action" value="editAsset">
                    <h1>Edit Asset </h1><br>
                    <table class="editForm">
                        <td>
                            <input type="hidden" name="action" value="editAsset">
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetId">Asset Id (CANNOT CHANGE):</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  id="edit_asset_id" name="edit_asset_id" readonly>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetName">Name:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  id="edit_asset_name" name="edit_asset_name">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetDesc">Description:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  id="edit_asset_desc" name="edit_asset_desc">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="photo_link">Picture link</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_photo_link" name="edit_photo_link">
                                </div>
                            </div>

                            <div class="row">

                                <div class="col-1">
                                    <label for="assetCat">Category:</label>
                                </div>
                                <div class="col-2">
                                    <select name="edit_asset_category" >
                                        <option id="edit_asset_category" class="OLD_asset_category">This is test</option>
                                        <c:forTokens var="assetCategory" delims="," items="${assetCategories}">
                                            <c:set var="assetCategoryDetails" value="${fn:split(assetCategory, ';')}" />
                                            <option value="${assetCategoryDetails[0]}">${assetCategoryDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="purchaseDate">Purchase Date:</label>
                                </div>
                                <div class="col-2">
                                    <input type="date"  id="edit_purchase_date" name="edit_purchase_date" >
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="cost">Cost:</label>
                                </div>
                                <div class="col-2">
                                    <input type="number" id="edit_cost" name="edit_cost" step="0.01" value=0.00>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="cost">Depreciated Value:</label>
                                </div>
                                <div class="col-2">
                                    <input type="number" id="edit_depreciated_value" name="edit_depreciated_value" step="0.01" value=0.00>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="assetCat">Depreciated Rate:</label>
                                </div>
                                <div class="col-2">
                                    <select name="deprec_info_id" id="deprec_info_id">
                                        <option id="edit_deprec_info_id" class="OLD_deprec_info" value="">This is test</option>
                                        <c:forTokens var="deprec" delims="," items="${deprecs}">
                                            <c:set var="deprecDetails" value="${fn:split(deprec, ';')}" />
                                            <option value="${deprecDetails[0]}">${deprecDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="location_id">Location</label>
                                </div>
                                <div class="col-2">
                                    <select  name="edit_location_id" id="location" value="test">
                                        <option id="edit_location" class="OLD_asset_location" value="">This is test</option>
                                        <c:forTokens var="Location" delims="," items="${Locations}">
                                            <c:set var="LocationDetails" value="${fn:split(Location, ';')}" />
                                            <option value="${LocationDetails[0]}">${LocationDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="added_by">Added by (CANNOT CHANGE):</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_added_by_email" name="edit_added_by" readonly> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="OS">OS</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_OS" name="edit_OS">
                                </div>
                            </div>
                        </td>         
                        <td>

                            <div class="row">
                                <div class="col-1">
                                    <label for="dimension">Dimension</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_dimension" name="edit_dimension">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="IP">IP</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_IP" name="edit_IP" >
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="MAC">MAC</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_MAC" name="edit_MAC" >
                                </div>
                            </div>                        
                            <div class="row">
                                <div class="col-1">
                                    <label for="serialNum">Serial Number</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_serial_number" name="edit_serial_number">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="hostname">Host Name</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_hostname" name="edit_hostname" >
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="service_tag">Service Tag</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_service_tag" name="edit_service_tag">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="comment_on_asset">Comment</label>
                                </div>
                                <div class="col-2">
                                    <input type="text" id="edit_comment_on_asset" name="edit_comment_on_asset">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="brand">Brand:</label>
                                </div>
                                <div class="col-2">
                                    <select name="edit_brand_id" id="brand">
                                        <option id="edit_brand" class="OLD_asset_brand">This is test</option>
                                        <c:forTokens var="assetBrand" delims="," items="${assetBrands}">
                                            <c:set var="assetBrandDetails" value="${fn:split(assetBrand, ';')}" />
                                            <option value="${assetBrandDetails[0]}">${assetBrandDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>
                            <!--<div class="row">
                                <div class="col-1">
                                    <label for="assetCat">Original Vendor:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  id="edit_vendor" name="edit_vendor" readonly>
                                </div>

                            </div>-->
                            <div class="row">
                                <div class="col-1">
                                    <label for="Vendor">Vendor:</label>
                                </div>
                                <div class="col-2">
                                    <select name="edit_vendor_id" id="vendor">
                                        <option id="edit_vendor"  class="OLD_asset_vendor">This is test</option>
                                        <c:forTokens var="assetVendor" delims="," items="${assetVendors}">
                                            <c:set var="assetVendorDetails" value="${fn:split(assetVendor, ';')}" />
                                            <option value="${assetVendorDetails[0]}">${assetVendorDetails[1]}</option>
                                        </c:forTokens>
                                    </select>
                                </div>
                            </div>


                            <div class="row">
                                <div class="col-1">
                                    <label for="assigned_to">Original Assigned To:</label>
                                </div>
                                <div class="col-2">
                                    <input type="hidden"  id="edit_assigned_to_OLD_personel_id" name="edit_assigned_to_OLD_personel_id" >
                                    <input type="text"  id="edit_assigned_to_user_email" name="edit_assigned_to" readonly>
                                </div>

                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="Assigned_to">Assign To Other User/ Remove:</label>
                                </div>
                                <div class="col-2">

                                    <select name="edit_assigned_to_NEW_personel_id" id="edit_assigned_to_NEW_personel_id">
                                        <option value="unchanged">No(unchanged)</option>
                                        <!---<option class="edit_assigned_to_OLD_user_name" name="edit_assigned_to_OLD_personel_id" >This is test</option>-->
                                        <c:forTokens var="user" delims="," items="${users}">
                                            <c:set var="userDetails" value="${fn:split(user, ';')}" />
                                            <option value="${userDetails[0]}">${userDetails[4]}</option>
                                        </c:forTokens>
                                        <option value="remove">Remove </option>    
                                    </select>

                                </div>
                            </div>

                            <br>
                            <div class="row">
                                <button type="submit" formaction="assetController?action=reload" class="cancel">Cancel</button>  
                                <!--<button onclick="history.back()">Cancel</button>-->
                                <input type="submit" value="Edit">
                            </div>
                        </td>
                    </table>
                </form>
            </div>
        </div>

        <!-- Delete Asset Form -->
        <div id="deleteasset-bg" class="deleteasset-bg">
            <div class="deleteasset">
                <h1>Delete Asset</h1><br>
                <form id="delAsset-form" action="assetController" method="POST">
                    <input type="hidden" name="action" value="deleteAsset">
                    <input type="hidden" id="del_asset_id" name="del_asset_id">
                    <input type="hidden" id="del_assigned_to_id" name="del_assigned_to_id">
                    <p>You are about to delete an asset (id: <a id="del_asset_display"></a>) <br/>
                        Do you want to proceed? 
                        <br>
                        
                        If the asset is sold, please fill in the sold price and select "Confirm and Send to Archive"</p><br>
                            <div class="row">
                                <div class="col-1">
                                    <label for="cost">Sold Price (default to be depreciated value):</label>
                                </div>
                                <div class="col-2">
                                    <input type="number" id="sold_price" name="sold_price" step="0.01" value=0.00>
                                </div>
                            </div>
                    
                    <br>Else select "Confirm"<br>
                    <button type="submit" formaction="assetController?action=reload" class="cancel">Cancel</button>  
                    <button type="submit" formaction="assetController?action=archive" class="cancel">Confirm and Send to Archive</button> 
                    <!--<button onclick="history.back()">Cancel</button>-->
                    <input type="submit" value="Confirm">
                    
                </form>
            </div>
        </div>

        <!--April 1st version-->
        <!-- Import file -->
        <div id="importFile-bg" class="importfile-bg" >
            <div class="importfile">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.8.0/jszip.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.8.0/xlsx.js"></script>
        <script>
            var ExcelToJSON = function () {
                this.parseExcel = function (file) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var data = e.target.result;
                        var workbook = XLSX.read(data, {
                            type: 'binary'
                        });
                        workbook.SheetNames.forEach(function (sheetName) {
                            var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
                            var json_object = JSON.stringify(XL_row_object);
                            console.log(JSON.parse(json_object));
                            jQuery('#xlx_json').val(json_object);
                        })
                    };

                    reader.onerror = function (ex) {
                        console.log(ex);
                    };
                    reader.readAsBinaryString(file);
                };
            };

            function handleFileSelect(evt) {
                var files = evt.target.files; // FileList object
                var xl2json = new ExcelToJSON();
                xl2json.parseExcel(files[0]);
            }
        </script>
                <form id="importFile-form" action="assetController" method="POST" >
                    <h1>Import from spreadsheet (accept xls OR xlsx file only):</h1><br>
                    <input id="upload" type=file  name="files[]" required="">
                    <input type="hidden" name="action" value="importFile">
                    Preview:<br>
                        <textarea name="json_parse" class="form-control" rows=20 cols=50 id="xlx_json"></textarea>
                        <div class="row">
                        <button onclick="window.location.href = '/CARO/assetController?action=viewAssets'">Cancel</button>     
                        <input type="submit" value="Import">
                    </div>
                </form>
            </div>
        <script>
            document.getElementById('upload').addEventListener('change', handleFileSelect, false);
        </script>
        </div>
    </body>
</html>
