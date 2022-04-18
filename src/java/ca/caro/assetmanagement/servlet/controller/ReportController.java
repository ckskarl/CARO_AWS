/*
 * Copyright (C) 2022 CARO Analytical Services
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.caro.assetmanagement.servlet.controller;

import ca.caro.assetmanagement.database.AssetCategoryHandler;
import ca.caro.assetmanagement.database.AssetHandler;
import ca.caro.assetmanagement.database.LogHandler;
import ca.caro.assetmanagement.database.ReportHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>Report Controller Class</b>
 * <p>Class that handles what data is sent to the <b>reports_only.jsp</b> web page. 
 * Also handles action sent from the <b>reports_only.jsp</b> web page if applicable.
 * Forward to different page mainly base on action attribute</p>
 * 
 * @author (ADD NAME HERE)
 * @version 1.0
 */
@WebServlet(name = "ReportController", urlPatterns = {"/ReportController"})
public class ReportController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        //String action = request.getParameter("action");
        //System.out.println(action);
        StringBuffer selectClause = new StringBuffer("SELECT a.asset_id \"Asset ID\", a.asset_name \"Name\"");
        StringBuffer whereClause = new StringBuffer("WHERE a.asset_id IS NOT NULL ");
               
         int clearanceLv =Integer.parseInt(session.getAttribute("clearanceLv").toString());
         
        String title = "";
        //if (request.getParameter("asset_category") != null) {    //asset_category is selected
        //    System.out.println(request.getParameter("asset_category_input"));
        //}
        if (request.getParameter("action") != null) {
            if (clearanceLv==0){
                ReportHandler reportH = new ReportHandler();
                switch (request.getParameter("action")) {
                    case "dynamic":
                        String level_of_detail="";
                        if (request.getParameter("level_of_detail")!=null){
                            level_of_detail = request.getParameter("level_of_detail");
                            if (level_of_detail.equals("detail")){
                                title+="Detail Report on ";
                                selectClause.append(", a.service_tag \"Service Tag\", a.comment_on_asset \"Comment\"");
                            }else {
                                title+="Basic Report on ";
                            }
                        }
                        //System.out.println(leve_of_detail);
                        if (request.getParameter("asset_added_by_checkbox") != null) {    
                            title+="Added by, ";
                            selectClause.append(", su.user_first_name+' '+su.user_last_name \"Added by\" ");
                            if (level_of_detail.equals("detail")){
                                selectClause.append(", su.user_email \"Email of added by user\" , su.user_phone_number \"Phone of added by user\"  ");
                            }
                            if (!request.getParameter("asset_added_by_selection").equals("all")) {
                                whereClause.append(" AND a.added_by = '" + request.getParameter("asset_added_by_selection") + "'");
                            }
                        }
                        if (request.getParameter("asset_assigned_to_checkbox") != null) {    
                            title+="Assigned To, ";
                            selectClause.append(", su2.user_first_name+' '+su2.user_last_name \"Assigned to\"");
                            if (level_of_detail.equals("detail")){
                                selectClause.append(", su2.user_email \"Email of assigned user\" , su2.user_phone_number \"Phone of assigned user\" ");
                            }
                            if (request.getParameter("asset_assigned_to_selection").equals("all")) {
                            } else if (request.getParameter("asset_assigned_to_selection").equals("none")) { 
                                    whereClause.append(" AND a_t.personel_id IS NULL");
                            } else if (request.getParameter("asset_assigned_to_selection").equals("anyone")){
                                    whereClause.append(" AND a_t.personel_id IS NOT NULL");
                            } else{
                                 whereClause.append(" AND a_t.personel_id = '" + request.getParameter("asset_assigned_to_selection") + "'");
                            }
                        }

                        if (request.getParameter("asset_category_checkbox") != null) {    //asset_category is selected
                            title+="Category, ";
                            selectClause.append(", ac.asset_category_description \"Category\" ");
                            if (level_of_detail.equals("detail")){
                                switch(request.getParameter("asset_category_selection")){
                                    case "H":
                                        selectClause.append(", a.dimension \"Dimension\" , a.IP, a.MAC, a.serial_number \"Serial Number\" , a.hostname \"Host Name\", OS " );
                                        break;
                                    case "F":
                                        selectClause.append(", a.dimension \"Dimension\" ");
                                        break;
                                    case "S":
                                        selectClause.append(", a.hostname \"Host Name\", OS ");
                                        break;
                                    default:
                                        selectClause.append(", a.dimension \"Dimension\" , a.IP, a.MAC, a.serial_number \"Serial Number\" , a.hostname \"Host Name\", OS " );
                                        break;
                                }
                            }
                            if (!request.getParameter("asset_category_selection").equals("all")) {
                                whereClause.append(" AND a.asset_category = '" + request.getParameter("asset_category_selection") + "'");
                            }
                            //whereClause.append(" AND a.asset_category LIKE '%"+request.getParameter("asset_category_input")+"%' ");
                        }

                        if (request.getParameter("asset_cost_checkbox") != null) {    //asset_category is selected
                            title+="Cost, ";
                            selectClause.append(", a.cost \"Cost\" ");
                            if (level_of_detail.equals("detail")){
                                selectClause.append(", a.depreciated_value \"Depreciated Value\", (a.depreciated_value - a.cost) \"Net Change\", di.deprec_desc \"Depreciation Rate\" ");
                            }
                            if (request.getParameter("from_cost") != null && request.getParameter("to_cost") != null) {
                                whereClause.append(" AND a.cost BETWEEN '" + Double.parseDouble(request.getParameter("from_cost")) + "' AND '" + Double.parseDouble(request.getParameter("to_cost")) + "'");
                            }
                        }                   


                        if (request.getParameter("asset_brand_checkbox") != null) {    //asset_category is selected
                            title+="Brand, ";
                            selectClause.append(", b.brand_name \"Brand\" ");
                            if (!request.getParameter("asset_brand_selection").equals("all")) {
                                whereClause.append(" AND b.brand_id = '" + request.getParameter("asset_brand_selection") + "'");
                            }
                        }
                        if (request.getParameter("asset_vendor_checkbox") != null) {    //asset_category is selected
                            title+="Vendor, ";
                            selectClause.append(", v.vendor_name \"Vendor\" ");
                            if (level_of_detail.equals("detail")){
                                selectClause.append(", v.vendor_phone_number \"Vendor Phone number\" ");
                            }
                            if (!request.getParameter("asset_vendor_selection").equals("all")) {
                                whereClause.append(" AND v.vendor_id = '" + request.getParameter("asset_vendor_selection") + "'");
                            }
                        }

                        if (request.getParameter("asset_date_checkbox") != null) {    //asset_category is selected
                            title+="Date of Purchase, ";
                            selectClause.append(", a.purchase_date \"Purchase Date\" ");
                            if (request.getParameter("from_date") != null && request.getParameter("to_date") != null) {
                                whereClause.append(" AND a.purchase_date BETWEEN '" + request.getParameter("from_date") + "' AND '" + request.getParameter("to_date") + "'");
                            }
                        }

                        if (title.endsWith(", ")){
                            title=title.substring(0, title.length()-2);
                        }else if (title.endsWith(" on ")){
                            title=title.substring(0, title.length()-4);
                        }
                        request.setAttribute("title",title);
                        request.setAttribute("resultInCSV", reportH.getFullAssetsDynamicInCSV(selectClause, whereClause, request, response));
                        //sendToReports(request, response, "Report Generated");
                        sendToReports(request, response, "Report Generated");
                        break;
                    case "loadFullAssetInCSV":
                        request.setAttribute("resultInCSV", reportH.getFullAssetsInCSV());
                        sendToReports(request, response, "Full Loaded");
                        break;
                    case "loadTestAssetInCSV":
                        request.setAttribute("resultInCSV", reportH.getTestAssetsInCSV());
                        sendToReports(request, response, "Test Loaded");
                        break;
                    case "loadLocation":
                        request.setAttribute("resultInCSV", reportH.getLocationReport());
                        sendToReports(request, response, "Location Loaded");
                        //sendToReports(request, response, "Location Loaded");
                        break;
                    case "loadBrand":
                        request.setAttribute("resultInCSV", reportH.getBrandReport());
                        sendToReports(request, response, "Brand Loaded");
                        break;
                    case "loadVendor":
                        request.setAttribute("resultInCSV", reportH.getVendorReport());
                        sendToReports(request, response, "Vendor Loaded");
                        break;
                    default:
                        sendToReports(request, response, "Default Loaded");
                        break;
                }
            }
        } else {
            sendToReports(request, response, "");
        }
    }

    /**
     *  This method is used to help servlet send the user to the reports page with the needed parameters.
     *  Retrieves all data needed by the reports page to display required reports information.
     *  Forward to different page mainly base on action attribute
     * 
     *  @param request HttpServletRequest servlet request
     *  @param response HttpServletResponse servlet response
     *  @param message String servlet Message of the message box
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    private void sendToReports(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        HttpSession session  =request.getSession();
        if (session.getAttribute("reportLoaded")==null){
            AssetHandler assetHandler = new AssetHandler();
            //String assetList = "";
            String assetCategoryList = "";
            String assetBrandList = "";
            String assetVendorList = "";
            String LocationList = "";
            String userList = "";   //personel_id,username,firstname,lastname, email


            ArrayList<String> minmaxDate = assetHandler.getMinMaxDate();
            String maxDateInAssets = minmaxDate.remove(1);
            String minDateInAssets = minmaxDate.remove(0);
            //String minDateInAssets = assetHandler.getMinMaxDate().get(0);
            //String maxDateInAssets = assetHandler.getMinMaxDate().get(1);

            ArrayList<Double> minmaxCost = assetHandler.getMinMaxCost();
            Double maxCost = minmaxCost.remove(1);
            Double minCost = minmaxCost.remove(0);

            //Double minCost = assetHandler.getMinMaxCost().get(0);
            //Double maxCost = assetHandler.getMinMaxCost().get(1);

            //System.out.println(maxCost);
            //new get asset with assigned to
            /*for (String asset : assetHandler.getAssets_with_assigned_to()) {
                if (!asset.equals(assetHandler.getAssets_with_assigned_to().get(0))) {
                    assetList += ",";
                }

                assetList += asset;
            }*/

            if ( session.getAttribute("assetLoaded")==null){
                ArrayList<String> assetCategories = assetHandler.getAssetCategories();
                for (String assetCategory : assetCategories) {
                    if (!assetCategory.equals(assetCategories.get(0))) {
                        assetCategoryList += ",";
                    }

                    assetCategoryList += assetCategory;
                }

                ArrayList<String> brands = assetHandler.getAssetBrands();
                for (String assetBrand : brands) {
                    if (!assetBrand.equals(brands.get(0))) {
                        assetBrandList += ",";
                    }

                    assetBrandList += assetBrand;
                }


                ArrayList<String> vendors = assetHandler.getAssetVendors();
                for (String assetVendor : vendors) {
                    if (!assetVendor.equals(vendors.get(0))) {
                        assetVendorList += ",";
                    }

                    assetVendorList += assetVendor;
                }

                ArrayList<String> locations = assetHandler.getLocations();
                for (String Location : locations ) {
                    if (!Location.equals(locations.get(0))) {
                        LocationList += ",";
                    }

                    LocationList += Location;
                    //System.out.println(LocationList + "TEST");
                }

                ArrayList<String> users = assetHandler.getUsers();
                for (String user : users) {
                    if (!user.equals(users.get(0))) {
                        userList += ",";
                    }
                    userList += user;
                }
                session.setAttribute("assetCategories", assetCategoryList);
                 session.setAttribute("assetBrands", assetBrandList);
                session.setAttribute("assetVendors", assetVendorList);
                session.setAttribute("Locations", LocationList);
                session.setAttribute("users", userList);
            }
            //request.setAttribute("assets", assetList);

            session.setAttribute("message", message);
            session.setAttribute("minDateInAssets", minDateInAssets);
            session.setAttribute("maxDateInAssets", maxDateInAssets);
            session.setAttribute("minCost", minCost);
            session.setAttribute("maxCost", maxCost);
            session.setAttribute("reportLoaded", true);
        }
        getServletContext().getRequestDispatcher("/WEB-INF/reports_only.jsp").forward(request, response);
    }

         
    
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
