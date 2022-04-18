<%-- 
 The whole jsp should be integraded with asset to reduce work load
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="assetCategory.css" rel="stylesheet" type="text/css" media="all" />
        <script type="text/javascript" src="index.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="functions/sorttable.js"></script>
        <script>
            var assetCategoryData =
                    [<c:forTokens var="assetCategory" delims="," items="${assetCategories}"><c:set var="assetCategoryDetails" value="${fn:split(assetCategory, ';')}" />
                        ["${assetCategoryDetails[0]}", "${assetCategoryDetails[1]}"],
            </c:forTokens>];

            function selectCategory(assetId)
            {
                for (var i = 0; i < assetCategoryData.length; i++) {
                    if (assetId === assetCategoryData[i][0])
                    {
                        document.getElementById("edit_cat_id").value = assetCategoryData[i][0];
                        document.getElementById("edit_cat_desc").value = assetCategoryData[i][1];
                        document.getElementById("edit_new_cat_id").value = assetCategoryData[i][0];
                        break;
                    }
                }
            }

        </script>                   
        <title>Category</title>
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
        <div class="depreciation">
            <div id="topBar" class="topbar">
                <a href="/CARO/assetController?action=viewAssets">Assets</a>
                <a href="/CARO/assetController?action=viewCategories">Categories</a>
            </div>

            <div class="undertopbarLeft">
                <p style="color:red">${requestScope.message}<br></p>
                Click on the header to sort ascending/descending<br>
                <p style="color:red"> There are currently ${sessionScope.numOfCategory} Category.<br></p>

                <c:choose>
                    <c:when test="${sessionScope.clearanceLv==0}">
                        Select to View/Edit/Delete
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>

                <br>
                <table id='id_of_table' class="sortable">
                    <thead>
                        <tr>
                            <c:choose>
                                <c:when test="${sessionScope.clearanceLv==0}">
                                    <th>Select to Edit/Delete</th>
                                    </c:when>
                                    <c:otherwise>

                                </c:otherwise>
                            </c:choose>

                            <th>Asset Category ID</th>
                            <th>Category Description</th>
                            <th>Number of assets in this category</th>
                        </tr>
                    </thead>
                    <c:forTokens var="assetCategory" delims="," items="${assetCategories}">
                        <c:set var="assetCategoryDetails" value="${fn:split(assetCategory, ';')}" />
                        <tr>
                            <c:choose>
                                <c:when test="${sessionScope.clearanceLv==0}">
                                    <td><input type="radio" id="cursor" name="assetCatId" onclick="selectCategory('${assetCategoryDetails[0]}');"></td>

                                </c:when>
                                <c:otherwise>

                                </c:otherwise>
                            </c:choose>
                            <td>${assetCategoryDetails[0]}</td>
                            <td>${assetCategoryDetails[1]}</td>
                            <td>${assetCategoryDetails[2]}</td>        
                        </tr>
                    </c:forTokens>
                </table>
            </div>



            <div class="undertopbarRight">

                <c:choose>
                    <c:when test="${sessionScope.clearanceLv==0}">
                        <form id="editCat-form" action="AssetCategoryController" method="POST" class="editCat-Form" >
                            <h3>Edit Category Details / Delete Category</h3><br>
                            <div class="row">
                                <div class="col-1">
                                    <label for="catId">Asset Category ID:</label>
                                </div>
                                <div class="col-2">
                                    <input id="edit_cat_id" type="text" name="edit_cat_id" readonly>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="catDesc">Description<br> (Display to User):</label>
                                </div>
                                <div class="col-2">
                                    <input id="edit_cat_desc" type="text" name="edit_cat_desc" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="newAssCat">NEW Asset Category ID (Can only be changed if there are no asset in this category):</label>
                                </div>
                                <div class="col-2">
                                    <input id="edit_new_cat_id" type="text" name="edit_new_cat_id" required>
                                </div>
                            </div>
                            <div class="row">
                                <button type="submit" formaction="AssetCategoryController?action=deleteCad" class="delete" onclick="return confirm('Are you sure you want to delete this item?');">Delete</button>
                                <button type="submit" formaction="AssetCategoryController?action=editCad" class="edit">Edit</button>  
                            </div>
                        </form>

                        <br><br>

                        <form id="addCat-form" action="AssetCategoryController" method="POST" class="addCat-Form">
                            <h3>Add New Category</h3><br>
                            <input type="hidden" name="action" value="addCategory">
                            <div class="row">
                                <div class="col-1">
                                    <label for="catId">Asset Category ID:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="add_cat_id" required> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <label for="catId">Category Description:</label>
                                </div>
                                <div class="col-2">
                                    <input type="text"  name="add_cat_desc" required> 
                                </div>
                            </div>
                            <div class="row">
                                <button  type="cancel">Cancel</button>
                                <input type="submit" value="Confirm">
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Add Depreciation Form -->
                <div id="addDepre-bg" class="addDepre-bg">
                    <div class="addDepre">
                        <h1>Add Depreciation</h1>

                        <form id="addDepre-form" action="" method="POST">
                            <input type="hidden" name="action" value="addAsset">
                            Depreciation Id: <input type="text"  name="depre_id"> <br>

                            <button  type="cancel">Cancel</button>
                            <input type="submit" value="Confirm">
                        </form>
                    </div>
                </div>
            </c:when>
            <c:otherwise>

            </c:otherwise>
        </c:choose>


    </body>
</html>
