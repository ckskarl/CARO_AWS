<%-- 
    Document   : report
    Created on : Mar. 11, 2022, 9:26:30 p.m.
    Author     : Administrator
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="report.css" rel="stylesheet" type="text/css" media="all" />
        <script type="text/javascript" src="index.js"></script>

        <script type="text/javascript" src="functions/FileSaver.js"></script>
        <script type="text/javascript" src="functions/report_functions.js" async=""></script>
        <title>Reports</title>
    </head>
    <body>
        <%@include file="jspf/header.jspf" %>
        <%@include file="jspf/sideBar.jspf" %>
        <!--<button onclick="testfunction();">test function</button>-->
        <div id="filter_form" class="filter-Form">
            <form action="ReportController" method="post" class="report-form">
                <div style="border-bottom: 1px solid dimgray; width: 90%;">
                    <h2><b>Select Type of Report: </b></h2><br>
                    ${requestScope.message}<br>
                    Other Report Available: &nbsp;&nbsp;
                    <button type="submit" formaction="ReportController?action=loadLocation" class="load">List all location details</button>&nbsp;&nbsp;|&nbsp;&nbsp;
                    <button type="submit" formaction="ReportController?action=loadBrand" class="load">List all brand details</button>&nbsp;&nbsp;|&nbsp;&nbsp;
                    <button type="submit" formaction="ReportController?action=loadVendor" class="load">List all vendor details</button>
                    <br><br>
                </div>
                
                <input type="hidden" name="action" value="dynamic">
                <br style="font-weight: bold;">
                <h2><b>Asset Report:</b></h2>
                
                Level of detail:&nbsp;&nbsp;
                <input type="radio" name="level_of_detail" value="basic" checked="checked"> &nbsp;Basic Report&nbsp;&nbsp;&nbsp;
                <input type="radio" name="level_of_detail" value="detail"> &nbsp;Detail Report (Table with more fields will be displayed)
                <br><br>
                <table>
                    <script language="JavaScript">
                        function toggle(source) {
                            // Get all input elements
                            var inputs = document.getElementsByTagName('input');
                            // Loop over inputs to find the checkboxes whose name starts with `orders`
                            for (var i = 0; i < inputs.length; i++) {
                                if (inputs[i].type == 'checkbox') {
                                    inputs[i].checked = source.checked;
                                }
                            }
                        }
                    </script>
                    
                    &nbsp;<input type='checkbox' onClick='toggle(this)' /> &nbsp;Select/Include All:
                    <thead>
                        <tr>
                            <th>Select</th>
                            <th>Field</th>
                            <th>Show data contain selected value</th>
                        </tr>
                    </thead>
                    <tr>
                        <td><input type="checkbox" id="asset_added_by_selection_checkbox" name="asset_added_by_checkbox"/></td>
                        <td>Added by</td>
                        <td>
                            <select id="asset_added_by_selection" name="asset_added_by_selection" onchange="makeChecked(this);">
                                <option value="all">Anyone</option>
                                <c:forTokens var="user" delims="," items="${sessionScope.users}">
                                    <c:set var="userDetails" value="${fn:split(user, ';')}" />
                                    <option value="${userDetails[0]}">${userDetails[2]} ${userDetails[3]}</option>
                                </c:forTokens>  
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="asset_assigned_to_selection_checkbox" name="asset_assigned_to_checkbox"/></td>
                        <td>Assigned to</td>
                        <td>
                            <select name="asset_assigned_to_selection" onchange="makeChecked(this);">
                                <option value="all">Anyone OR No one</option>
                                <option value="anyone">Anyone</option>
                                <option value="none">No one</option>   
                                <c:forTokens var="user" delims="," items="${users}">
                                    <c:set var="userDetails" value="${fn:split(user, ';')}" />
                                    <option value="${userDetails[0]}">${userDetails[2]} ${userDetails[3]}</option>
                                </c:forTokens>
                                 
                            </select>
                        </td>
                    </tr>                
                    <tr>
                        <td><input type="checkbox" id="asset_category_selection_checkbox" name="asset_category_checkbox" /></td>
                        <td>Category</td>
                        <td>
                            <select id="asset_category_selection" name="asset_category_selection" onchange="makeChecked(this);">
                                <option value="all">All Category</option>
                                <c:forTokens var="assetCategory" delims="," items="${assetCategories}">
                                    <c:set var="assetCategoryDetails" value="${fn:split(assetCategory, ';')}" />
                                    <option value="${assetCategoryDetails[0]}">${assetCategoryDetails[1]}</option>
                                </c:forTokens>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="asset_brand_selection_checkbox" name="asset_brand_checkbox" /></td>
                        <td>Brand</td>
                        <td>
                            <select name="asset_brand_selection" onchange="makeChecked(this);">
                                <option value="all">All Brand</option>
                                <c:forTokens var="assetBrand" delims="," items="${assetBrands}">
                                    <c:set var="assetBrandDetails" value="${fn:split(assetBrand, ';')}" />
                                    <option value="${assetBrandDetails[0]}">${assetBrandDetails[1]}</option>
                                </c:forTokens>
                            </select>
                        </td>
                    </tr>    
                    <tr>
                        <td><input type="checkbox" id="asset_vendor_selection_checkbox" name="asset_vendor_checkbox" /></td>
                        <td>Vendor</td>
                        <td> 
                            <select name="asset_vendor_selection" onchange="makeChecked(this);">
                                <option value="all">All Vendor</option>
                                <c:forTokens var="assetVendor" delims="," items="${assetVendors}">
                                    <c:set var="assetVendorDetails" value="${fn:split(assetVendor, ';')}" />
                                    <option value="${assetVendorDetails[0]}">${assetVendorDetails[1]}</option>
                                </c:forTokens>
                            </select>
                        </td>
                    </tr>    
                    <tr>
                        <td><input type="checkbox" id="cost_selection_checkbox"  name="asset_cost_checkbox" /></td>
                        <td>Cost</td>
                        <td>From&nbsp; $ <input type="number"  name="from_cost" value="${minCost}" step="0.01" onchange="makeChecked(this);"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $ <input type="number"  name="to_cost" value="${maxCost}" step="0.01" onchange="makeChecked(this);"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="date_selection_checkbox"  name="asset_date_checkbox" /></td>
                        <td>Date of Purchase</td>
                        <td>From &nbsp;&nbsp;&nbsp;<input type="Date"  name="from_date" value="${minDateInAssets}" onchange="makeChecked(this);"></td>
                    </tr>                
                    <tr>
                        <td></td>
                        <td></td>
                        <td>To &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="Date"  name="to_date" value="${maxDateInAssets}" onchange="makeChecked(this);"></td>
                    </tr>
                </table><br>
                <input type="submit">
            </form>
            
            <div id="csv_display" style="display: none;">
                <p id="theResult">${requestScope.resultInCSV}</p><br>
                <button onclick="ParseResultTable();">display in table form</button> 
            </div>
            <br>    
            <div id="Import Report in CSV format" style="border-bottom: 1px solid dimgray; width: 40%; border-top: 1px solid dimgray; width: 40%;">
                <br>
                <input type="file" id="fileUpload" class="file"/>&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" id="upload" value="Import" onclick="Upload()" class="import" />&nbsp;&nbsp;|&nbsp;&nbsp;
                <input type="button" id="upload" value="Export Report in CSV format" onclick="exportCSV('${resultInCSV}');" class="export" />
                <br><br>
            </div>
            <br><br>
            
            <div id="report_title"><h3>${requestScope.title}</h3>
                <h5>${requestScope.rowCount}</h5><div><br> 
            <div id="dvCSV"> <!--<!-- to display report in table format --> </div>
            
            <br><br><br>
            <div id="report_export"></div>
        </div>

                
        <script type="text/javascript">
            function ParseResultTable() {
                var result = "${requestScope.resultInCSV}";
                console.log(result);
                var table = document.createElement("table");
                table.setAttribute('class', 'sortable');
                var thead = document.createElement("thead");
                table.appendChild(thead);
                var tbody = document.createElement("tbody");
                table.appendChild(tbody);
                var rows = result.split("\n");
                for (var i = 0; i < rows.length; i++) {
                    var cells = rows[i].split(",");
                    if (cells.length > 1) {


                        if (i == 0) {
                            var headrow = thead.insertRow(-1);
                            for (var j = 0; j < cells.length; j++) {
                                var headercell = document.createElement("th");
                                //headercell = row.insertCell(-1);
                                headercell.innerHTML = cells[j];
                                headrow.appendChild(headercell);
                            }
                        } else {
                            var row = tbody.insertRow(-1);
                            for (var j = 0; j < cells.length; j++) {
                                var cell = row.insertCell(-1);
                                cell.innerHTML = cells[j];
                            }
                        }
                    }

                }
                var dvCSV = document.getElementById("dvCSV");
                dvCSV.innerHTML = "";
                dvCSV.appendChild(table);

                var script = document.createElement('script');
                script.src = "functions/sorttable.js";
                dvCSV.appendChild(script);
            }

            function Upload() {
                var fileUpload = document.getElementById("fileUpload");
                var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)$/;
                if (regex.test(fileUpload.value.toLowerCase())) {
                    if (typeof (FileReader) != "undefined") {
                        var reader = new FileReader();
                        reader.onload = function (e) {
                var table = document.createElement("table");
                table.setAttribute('class', 'sortable');
                var thead = document.createElement("thead");
                table.appendChild(thead);
                var tbody = document.createElement("tbody");
                table.appendChild(tbody);
                            var rows = e.target.result.split("\n");
                            for (var i = 0; i < rows.length; i++) {
                    var cells = rows[i].split(",");
                    if (cells.length > 1) {


                        if (i == 0) {
                            var headrow = thead.insertRow(-1);
                            for (var j = 0; j < cells.length; j++) {
                                var headercell = document.createElement("th");
                                //headercell = row.insertCell(-1);
                                headercell.innerHTML = cells[j];
                                headrow.appendChild(headercell);
                            }
                        } else {
                            var row = tbody.insertRow(-1);
                            for (var j = 0; j < cells.length; j++) {
                                var cell = row.insertCell(-1);
                                cell.innerHTML = cells[j];
                            }
                        }
                    }

                }
                            var dvCSV = document.getElementById("dvCSV");
                            dvCSV.innerHTML = "";
                            dvCSV.appendChild(table);
                            
                            var script = document.createElement('script');
                            script.src = "functions/sorttable.js";
                            dvCSV.appendChild(script);
                        }
                        reader.readAsText(fileUpload.files[0]);
                    } else {
                        alert("This browser does not support HTML5.");
                    }
                } else {
                    alert("Please upload a valid CSV file.");
                }
            }

            window.addEventListener('load', event => {
                ParseResultTable();
            });
            
            

        </script>
        
</body>
</html>
