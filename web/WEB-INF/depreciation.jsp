<%-- 
    Document   : depreciation
    Created on : Mar. 11, 2022, 4:40:29 p.m.
    Author     : Administrator
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="depreciation.css" rel="stylesheet" type="text/css" media="all" />
        <script type="text/javascript" src="functions/sorttable.js"></script>
        <script type="text/javascript" src="index.js"></script>
        <script>
        var depData =
                [<c:forTokens var="depc" delims="&&" items="${depreciationInfos}">
                    [
                        <c:set var="dpc" value="${fn:split(depc, ';')}" />
                        "${dpc[0]}",
                        "${dpc[1]}",
                        "${dpc[2]}",
                        "${dpc[3]}",
                        "${dpc[4]}",
                        ${dpc[5]},
                        "${dpc[6]}",
                        "${dpc[7]}"
                    ], 
                </c:forTokens>];
        function addDepreButtonShow(){
            document.querySelector('.addDepre-bg').style.visibility = 'visible';
            document.querySelector('.addDepre-bg').style.hidden = false;
            document.querySelector('.depreciation').style.opacity = '0.3';      
        }
        function addDepreButtonHide(){
            document.querySelector('.addDepre-bg').style.visibility = 'hidden';
            document.querySelector('.addDepre-bg').style.hidden = true;
            document.querySelector('.depreciation').style.opacity = '1';      
        }
        function delDepreButtonShow(){
            document.querySelector('.delDepre-bg').style.visibility = 'visible';
            document.querySelector('.delDepre-bg').style.hidden = false;
            document.querySelector('.depreciation').style.opacity = '0.3';      
        }
        function delDepreButtonHide(){
            document.querySelector('.delDepre-bg').style.visibility = 'hidden';
            document.querySelector('.delDepre-bg').style.hidden = true;
            document.querySelector('.depreciation').style.opacity = '1';      
        }
        function assetSelectorShow(){
            document.querySelector('.generateReport-bg').style.visibility = 'visible';
            document.querySelector('.generateReport-bg').style.hidden = false;
            document.querySelector('.depreciation').style.opacity = '0.3';      
        }
        function assetSelectorHide(){
            document.querySelector('.generateReport-bg').style.visibility = 'hidden';
            document.querySelector('.generateReport-bg').style.hidden = true;
            document.querySelector('.depreciation').style.opacity = '1';      
        }
        function selectDepc(depID){
            document.getElementById("editdeprec_info_id").value = depID;
            document.getElementById("deldeprec_info_id").value = depID;
            document.getElementById("del_ID").innerText = depID;
            
            for(var i=0; i<depData.length; i++)
            {
                if(depData[i][0] === depID)
                {
                    document.getElementById("editdeprec_info_id").value = depData[i][0];
                    document.getElementById("dateUpdated").innerText = depData[i][2];
                    document.getElementById("depreDescription").value = avoidNull(depData[i][1]);
                    
                    document.getElementById("depreCalcMode").checked = depData[i][5];
                    setDepCalcForm();
                    
                    document.getElementById("depreDeprecRate").value = avoidNull(depData[i][6]);
                    document.getElementById("depreTimeInterval").value = avoidNull(depData[i][7]);
                    
                    document.getElementById("depreCategories").value = avoidNull(depData[i][3]);
                    document.getElementById("depreFormula").value = avoidNull(depData[i][4]);
                    
                    
                    document.getElementById("testdeprec_info_id").value = depData[i][0];
                    document.getElementById("depreciationID").value = depData[i][0];
                    document.getElementById("depreciationIDDisplay").innerHTML = depData[i][0];
                    document.getElementById("depreciationIDDisplay").removeAttribute('style');
                }
            }
        }
        function avoidNull(s)
        {
            if(s.includes("null"))
                return "";
            else
                return s;
        }
        function setDepCalcForm()
        {
            if(document.getElementById("depreCalcMode").checked){
                document.getElementById("fixedCalcForm").hidden = true;
                document.getElementById("customCalcForm").hidden = false;
                
                //Change color or class style here
                document.getElementById("bDepButton").style.backgroundColor = "white";
                document.getElementById("cDepButton").style.backgroundColor = "gray";
            } else {
                document.getElementById("fixedCalcForm").hidden = false;
                document.getElementById("customCalcForm").hidden = true;
                
                //Change color or class style here
                document.getElementById("bDepButton").style.backgroundColor = "gray";
                document.getElementById("cDepButton").style.backgroundColor = "white";
            }
        }
        function setDepMode(bool)
        {
            document.getElementById("depreCalcMode").checked = bool;
            setDepCalcForm();
        }
        function formulaHelperSelect(selectId)
        {
            const formFld = document.getElementById("depreFormula");
            switch(document.getElementById(selectId).value)
            {
                case 'asset_cost':
                    formFld.value+="[cost]";
                    break;
                case 'asset_purDate_day':
                    formFld.value+="[purchase_date.daymonth]";
                    break;
                case 'asset_purDate_month':
                    formFld.value+="[purchase_date.month]";
                    break;
                case 'asset_purDate_year':
                    formFld.value+="[purchase_date.year]";
                    break;
                case 'currenttime_day':
                    formFld.value+="[currdate.daymonth]";
                    break;
                case 'currenttime_month':
                    formFld.value+="[currdate.month]";
                    break;
                case 'currenttime_year':
                    formFld.value+="[currdate.year]";
                    break;
                case 'addit':
                    formFld.value+="+";
                    break;
                case 'subtr':
                    formFld.value+="-";
                    break;
                case 'multi':
                    formFld.value+="*";
                    break;
                case 'divid':
                    formFld.value+="/";
                    break;
                case 'modul':
                    formFld.value+="%";
                    break;
                case 'round':
                    formFld.value+="round([CHANGE_THIS_VAL])";
                    break;
                case 'pow':
                    formFld.value+="pow([CHANGE_THIS_VAL],[CHANGE_THIS_VAL])";
                    break;
                case 'abs':
                    formFld.value+="abs([CHANGE_THIS_VAL])";
                    break;
            }
            document.getElementById(selectId).value = "";
        }
        function selectAllAsset(){
            var ele=document.getElementsByName('assetID');
            for(var i=0; i<ele.length; i++){  
                if(ele[i].type=='checkbox')
                    ele[i].checked=true;
            }
        }
        function deselectAllAsset(){
            var ele=document.getElementsByName('assetID');
            for(var i=0; i<ele.length; i++){  
                if(ele[i].type=='checkbox')
                    ele[i].checked=false;
            }
        }
        
        function showMessage() {;
            document.getElementById('showMessage-bg').style.visibility = 'visible';
            document.getElementById('showMessage-bg').style.hidden = false;
            document.querySelector('.depreciation').style.opacity = '0.3'; 
        }
        function cancelMessage() {
            document.getElementById('showMessage-bg').style.visibility = 'hidden';
            document.getElementById('showMessage-bg').style.hidden = true;
            document.querySelector('.depreciation').style.opacity = '1';  
        }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Depreciation</title>
    </head>
    <body>
        <%@include file="jspf/header.jspf" %>
        <%@include file="jspf/sideBar.jspf" %>
        <div class="depreciation">
            <div id="topBar" class="topbar">
                Depreciation
                <a href="#" onClick="delDepreButtonShow()" >Delete Depreciation</a>

                <a href="#" onClick="addDepreButtonShow()" >Add Depreciation</a>
            </div>



            <div class="undertopbarLeft">
                <table id='id_of_table' class="sortable">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Depreciation ID</th>
                            <th>Description</th>

                        </tr>
                    </thead>
                    <c:forTokens var="deprec" delims="," items="${depreciations}">
                        <c:set var="dep" value="${fn:split(deprec, ';')}" />
                        <tr>
                            <td><input type="radio" id="cursor${dep[0]}" name="assetId" onclick="selectDepc('${dep[0]}')"></td>
                            <td><label for="cursor${dep[0]}">${dep[0]}</label></td>
                            <td><label for="cursor${dep[0]}">${dep[1]}</label></td>
                        </tr>
                    </c:forTokens>
                </table>


            </div>
            <div class="undertopbarRight">
                <form action="DepreciationController" method="POST" class="depDetail-Form">
                    <h3>Depreciation Details: </h3><br>
                    <input type="hidden" name="action" value="editDeprecitation">
                    <input id="editdeprec_info_id" type="hidden" name="deprec_info_id" value="">
                    <div class="row">
                        <div class="col-1">
                            <label for="dateUpdated">Last Updated: </label>
                        </div>
                        <div class="col-2">
                            <a id="dateUpdated"></a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-1">
                            <label for="depreDescription">Description: </label>
                        </div>
                        <div class="col-2">
                            <input id="depreDescription" type="text" name="depreDescription">
                        </div>
                    </div>
                    <div class="row" hidden>
                        <div class="col-1">
                            <label for="depreCategories">Categories Applied: </label>
                        </div>
                        <div class="col-2">
                            <input id="depreCategories" type="text" name="depreCategories">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-1">
                            <label for="depType">Calculation Type: </label>
                        </div>
                        <div class="col-2">
                            <button id="bDepButton" type="button" onclick="setDepMode(false)">Basic</button>
                            <button id="cDepButton" type="button" onclick="setDepMode(true)">Custom</button>
                        </div>
                    </div>
                    <input id="depreCalcMode" type="checkbox" name="depreCalcMode" onclick="setDepCalcForm()" hidden>
                    <div id="fixedCalcForm" hidden>
                        <h3>Regular Calculation:</h3><br>
                        <div class="row">
                            <div class="col-1">
                                <label for="depreDeprecRate">Depreciation Rate: </label>
                            </div>
                            <div class="col-2">
                                <input id="depreDeprecRate" type="text" name="depreDeprecRate">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <label for="depreTimeInterval">Time Interval: </label>
                            </div>
                            <div class="col-2">
                                <select name="depreTimeInterval" id="depreTimeInterval">
                                        <option value=""></option>
                                        <c:forTokens var="tIntervals" delims="," items="${depTimeIntervals}">
                                            <c:set var="interv" value="${fn:split(tIntervals, ';')}" />
                                            <option value="${interv[0]}">${interv[1]}</option>
                                        </c:forTokens>
                                </select><br/>
                            </div>
                        </div>
                    </div>
                    <div id="customCalcForm" hidden>
                        <h3>Custom Calculation:</h3><br>
                        <div class="row">
                            <div class="col-1">
                                <label for="depreFormula">Depreciation Formula: </label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <select name="formulaHelperA" id="formulaHelperA" onchange="formulaHelperSelect('formulaHelperA')">
                                        <option value="">Select Variable</option>
                                        <option value="asset_cost">Asset - Cost</option>
                                        <option value="asset_purDate_day">Asset - Purchase Date - Day</option>
                                        <option value="asset_purDate_month">Asset - Purchase Date - Month</option>
                                        <option value="asset_purDate_year">Asset - Purchase Date - Year</option>
                                        <option value="currenttime_day">Current Time - Day</option>
                                        <option value="currenttime_month">Current Time - Month</option>
                                        <option value="currenttime_year">Current Time - Year</option>
                                </select>
                                <select name="formulaHelperB" id="formulaHelperB" onchange="formulaHelperSelect('formulaHelperB')">
                                        <option value="">Select Operation</option>
                                        <option value="addit">+</option>
                                        <option value="subtr">-</option>
                                        <option value="multi">*</option>
                                        <option value="divid">/</option>
                                        <option value="modul">%</option>
                                </select>
                                <select name="formulaHelperC" id="formulaHelperC" onchange="formulaHelperSelect('formulaHelperC')">
                                        <option value="">Select Operation</option>
                                        <option value="round">Round Off</option>
                                        <option value="pow">To the power off</option>
                                        <option value="abs">Absolute Value</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                <textarea id="depreFormula" type="text" name="depreFormula" 
                                          style="resize: vertical;" rows="5" cols="70%"></textarea><br/>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <input type="submit" value="Update">
                    </div>
                </form>
                
                <br><br>
                
                <div class="depTest-Form">
                    <h3>Generate Depreciation: </h3>
                    <div hidden>
                        <input type="hidden" name="action" value="testDepreciation">
                        <input id="testdeprec_info_id" type="hidden" name="testdeprec_info_id" value="">
                        <div class="row">
                            <div class="col-1">
                                <label for="assetID">Asset ID: </label>
                            </div>
                            <div class="col-2">
                                <input id="assetID" type="text" name="assetID">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-1">
                                Output: ${TEST_OUTPUT}
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <form action="DepreciationController" method="POST" >
                            <input type="hidden" name="action" value='applyDepreciationReport'/>
                            <input type="hidden" name='applyAssetIDs' value="${applyAssetIDs}">
                            <input type="hidden" name='applyDepreciationID' value="${applyDepreciationID}">
                            <input type="submit" value="Apply to Asset(s)"/>
                        </form>
                        <button type="button" onclick="assetSelectorShow()">Calculate Depreciation</button>
                    </div>
                    <div class="row">
                        <table id='id_of_table' class="sortable">
                            <thead>
                                <tr>
                                    <th>Asset ID</th>
                                    <th>Name</th>
                                    <th>Depreciated Value</th>
                                </tr>
                            </thead>
                            <c:forTokens var="depReport" delims="," items="${assetDepreciatedVals}">
                                <c:set var="depR" value="${fn:split(depReport, ';')}" />
                                <tr>
                                    <td>${depR[0]}</td>
                                    <td>${depR[1]}</td>
                                    <td>${depR[2]}</td>
                                </tr>
                            </c:forTokens>
                        </table>
                        <c:if test = "${empty assetDepreciatedVals}">
                           <p>(Report not generated)<p>
                        </c:if>
                    </div>
                </div>
                
            </div>
        </div>

        <!-- Add Depreciation Form -->
        <div id="addDepre-bg" class="addDepre-bg">
            <form id="addDepre-form" action="DepreciationController" method="POST" class="addDep-Form">
                <h2>Add Depreciation: </h2><br>
                    <input type="hidden" name="action" value="addDeprecitation">
                    <div class="row">
                        <div class="col-1">
                            <label for="depId">Depreciation Id: </label>
                        </div>
                        <div class="col-2">
                            <input type="text" name="deprec_info_id">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-1">
                            <label for="dep">Description: </label>
                        </div>
                        <div class="col-2">
                            <input type="text" name="deprec_desc">
                        </div>
                    </div><br>
                    <div class="row">
                        <button type="button" onclick="addDepreButtonHide()">Cancel</button>
                        <input type="submit" value="Confirm">
                    </div>
                </form>
        </div>
        
        
        <div id="generateReport-bg" class="generateReport-bg">
            <form action="DepreciationController" method="POST">
                <div class="row">
                    <h1>Select assets:</h1>
                    <input type="hidden" id="depreciationID" name="depreciationID" />
                    Target depreciation: <a id="depreciationIDDisplay" style="color: red;">SELECT A DEPRECIATION</a>
                </div>
                <div class="row">
                    <input type="hidden" name="action" value='generateDepreciationReport'/>
                    <input type="submit" value='Confirm'/>
                    <button type="button" onclick="assetSelectorHide()">Cancel</button>
                </div><br/>
                <div class="row">
                    <button type="button" onclick="deselectAllAsset()">Deselect All</button>
                    <button type="button" onclick="selectAllAsset()">Select All</button>
                </div>
                <table id='id_of_table' class="sortable">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Asset ID</th>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <c:forTokens var="aSelector" delims="," items="${assetSelector}">
                        <c:set var="astSel" value="${fn:split(aSelector, ';')}" />
                        <tr>
                            <td><input type="checkbox" id="astC${astSel[0]}" name="assetID" value="${astSel[0]};${astSel[1]}"></td>
                            <td><label for="astC${astSel[0]}">${astSel[0]}</label></td>
                            <td><label for="astC${astSel[0]}">${astSel[1]}</label></td>
                        </tr>
                    </c:forTokens>
                </table>
            </form>
        </div>
        
        <!-- Delete Depreciation Form -->
        <div id="delDepre-bg" class="delDepre-bg">
                <form id="delDepre-form" action="DepreciationController" method="POST" class="deleDep-Form">
                    <h2>Delete Depreciation:</h2><br>
                    <input type="hidden" name="action" value="delDeprecitation">
                    <div class="row">
                        <div class="col-1">
                            <input id="deldeprec_info_id" type="hidden" name="deprec_info_id" value="">
                            Delete selected depreciation (<a id="del_ID">[id]</a>)? <br>
                        </div>
                    </div><br>
                    <div class="row">
                        <button type="button" onclick="delDepreButtonHide()">Cancel</button>
                        <input type="submit" value="Confirm">
                    </div>
                </form>
        </div>
        
        <!-- Show Message -->
        <div id="showMessage-bg" class="showMessage-bg">
            <div>
                <h1>${requestScope.dep_messageHeader}</h1><br>
                <p>${requestScope.dep_message}</p><br>
                <button onclick="cancelMessage()">OK</button>
            </div>
        </div>
        <script>
            if(${requestScope.showMessage}){
                showMessage();
            }
        </script>
    </body>
</html>
