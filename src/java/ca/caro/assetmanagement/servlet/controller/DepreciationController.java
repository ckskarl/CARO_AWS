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

import ca.caro.assetmanagement.database.Account;
import ca.caro.assetmanagement.database.AssetHandler;
import ca.caro.assetmanagement.database.DepreciationHandler;
import ca.caro.assetmanagement.database.Report;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>Depreciation Controller Class</b>
 * <p>Class that handles what data is sent to the <b>depreciation.jsp</b> web page.
 * Also handles action sent from the <b>depreciation.jsp</b> web page if applicable.
 * Most methods are auto-generated from being a generated servlet, additional methods created are meant to help the servlet</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
@WebServlet(name = "DepreciationController", urlPatterns = {"/DepreciationController"})
public class DepreciationController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.Forward to different page mainly base on action attribute
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String asset_id = request.getParameter("asset_id");
        String deprec_info_id = request.getParameter("deprec_info_id");
        //System.out.println(asset_id);
        //System.out.println(deprec_info_id);
        HttpSession session = request.getSession();
        int clearanceLv =Integer.parseInt(session.getAttribute("clearanceLv").toString());
           
        DepreciationHandler depH = new DepreciationHandler();
        if(!Account.isLoggedIn(request))
        {
            response.sendRedirect("/CARO");
        } else {
            if(request.getParameter("action")!=null){
                if (clearanceLv==0){
                switch(request.getParameter("action"))
                {
                    case "addDeprecitation":
                        if( !request.getParameter("deprec_info_id").equals("") &&
                                !request.getParameter("deprec_desc").equals("")){
                            String[] depcParams = {
                                request.getParameter("deprec_info_id"), 
                                request.getParameter("deprec_desc")
                            };
                            if(depH.addDepreciation(depcParams)){
                                sendToDepreciation(request, response, true,
                                        "Depreciation Added", "<p style='color:green'>Adding depreciation success</p>");
                            } else {
                                sendToDepreciation(request, response, true,
                                        "Depreciation Add Failed", "<p style='color:red'>Failed to add depreciation info</p>");
                            }
                        } else {
                            sendToDepreciation(request, response, true,
                                    "Adding Depreciation Failed", "<p style='color:red'>Failed to add depreciation info</p>");
                        }
                        break;
                    case "editDeprecitation":
                        String[] updateParams = 
                            {
                                request.getParameter("deprec_info_id"),
                                request.getParameter("depreDescription"),
                                request.getParameter("depreCategories"),
                                request.getParameter("depreFormula"),
                                request.getParameter("depreCalcMode"),
                                request.getParameter("depreDeprecRate"),
                                request.getParameter("depreTimeInterval")
                            };
                        
                        if(depH.updateDepreciationInfo(updateParams)) { 
                            sendToDepreciation(request, response, true,
                                    "Depreciation Updated", "<p style='color:green'>Depreciation successfully updated!</p>");
                        } else {
                            sendToDepreciation(request, response, true,
                                    "Failed Depreciation Failed", "<p style='color:red'>Depreciation update failed!</p>");
                        }
                        break;
                    case "delDeprecitation":
                        if( !request.getParameter("deprec_info_id").equals("") ){
                            if(depH.deleteDepreciation(request.getParameter("deprec_info_id"))){
                                sendToDepreciation(request, response, true, 
                                        "Depreciation Deleted", "<p style='color:green'>Deleting depreciation success</p>");
                            } else {
                                sendToDepreciation(request, response, true, 
                                        "Delete Depreciation Failed", "<p style='color:red'>Failed to delete depreciation info</p>");
                            }
                        } else {
                            sendToDepreciation(request, response, true, 
                                    "Delete Depreciation Failed", "<p style='color:red'>Failed to delete depreciation info</p>");
                        }
                        break;
                    case "testDepreciation":// Part might need to be deleted
                        if(!request.getParameter("testdeprec_info_id").equals("") && !request.getParameter("assetID").equals("")){
                            String test_deprec_id = request.getParameter("testdeprec_info_id");
                            String test_asset_id = request.getParameter("assetID");
                            request.getSession().setAttribute("TEST_OUTPUT", Report.calcDepreciation(test_asset_id, test_deprec_id));
                            sendToDepreciation(request, response, false, "", "");
                        } else {
                            sendToDepreciation(request, response, true, 
                                    "Test Depreciation Failed", "<p style='color:red'>Depreciation needs to be selected and Asset ID needs to be specified...</p>");
                        }
                        break;
                    case "generateDepreciationReport":
                        String[] assetIDs = request.getParameterValues("assetID");
                        String deprecID = request.getParameter("depreciationID");
                        
                        String assetDepreciatedVals = "";
                        String applyAssetIDs = "";
                        if(assetIDs!=null && (deprecID!=null && !deprecID.equals(""))){
                            for(String s : assetIDs){
                                applyAssetIDs += s.split(";")[0] + ";";
                                assetDepreciatedVals += s.split(";")[0] + ";";
                                assetDepreciatedVals += s.split(";")[1] + ";";
                                assetDepreciatedVals += Report.calcDepreciation(s.split(";")[0], deprecID) + ",";
                            }
                            request.setAttribute("applyAssetIDs", applyAssetIDs);
                            request.setAttribute("applyDepreciationID", deprecID);
                            
                            request.setAttribute("assetDepreciatedVals", assetDepreciatedVals);
                            sendToDepreciation(request, response, false, "", "");
                        } else {
                            sendToDepreciation(request, response, true, 
                                    "Not enough Info selected", 
                                    "Please select a depreciation and at least one asset id");
                        }
                        break;
                    case "applyDepreciationReport":
                        String assetIDsToApply = request.getParameter("applyAssetIDs");
                        String deprecIDToApply = request.getParameter("applyDepreciationID");
                        
                        AssetHandler assetH = new AssetHandler();
                        if((assetIDsToApply!=null && !assetIDsToApply.equals("")) && 
                                (deprecIDToApply!=null && !deprecIDToApply.equals(""))){
                            for(String s : assetIDsToApply.split(";")){
                                assetH.editDeprec_value(s, deprecIDToApply);
                            }
                            sendToDepreciation(request, response, true, 
                                    "Depreciation Value applied", 
                                    "Depreciation Value applied to selected assets. Go to assets to confirm.");
                        } else {
                            sendToDepreciation(request, response, true, 
                                    "Depreciation Report not generated", 
                                    "Please generate a report first before trying to apply asset depreciation.");
                        }
                        break;
                    default:
                        sendToDepreciation(request, response, false, "", "");
                }
                }
            }
            else
            {
                sendToDepreciation(request, response, false, "", "");
            }
        }
    }
    
    /**
     *  This method is used to help servlet send the user to the depreciation page with the needed parameters.
     *  Retrieves all data needed by the depreciation page to display required depreciation information
     * 
     *  @param request HttpServletRequest servlet request
     *  @param response HttpServletResponse servlet response
     *  @param showMessage boolean If the message box shows up upon opening the page
     *  @param header String servlet Header of the message box
     *  @param message String servlet Message of the message box
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    private void sendToDepreciation(HttpServletRequest request, HttpServletResponse response, boolean showMessage, String header, String message) throws IOException, ServletException
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("depreciations")==null){
            DepreciationHandler deprecH = new DepreciationHandler();
            String deprecation = "";
            String deprecationInfo = "";
            String depTimeIntervals = "";
            String assetSelectVal = "";
            String oneStringdeprecationInfo = "";
            
            ArrayList<String> depreciations = deprecH.getDepreciations();
            //ArrayList<String> deprecInfo = new DepreciationHandler().getDepreciations();
            for(String d : depreciations)
            {
                deprecation += d + ",";
                //deprecationInfo += deprecH.getDepreciationInfo(d.split(";")[0])+"&&";
            }
            //oneStringdeprecationInfo= deprecH.getDetailedDepreciationInfo();
            System.out.println(oneStringdeprecationInfo);
            ArrayList<String> timeIntervals = deprecH.getTimeIntervals();
            for(String d : timeIntervals )
            {
                depTimeIntervals += d + ",";
            }
            
            ArrayList<String> assetSelectorList = new AssetHandler().getAssetsSelectorList();
            for(String a : assetSelectorList)
            {
                assetSelectVal += a + ",";
            }

            session.setAttribute("depreciations", deprecation);
session.setAttribute("depreciationInfos", oneStringdeprecationInfo);            
//session.setAttribute("depreciationInfos", deprecationInfo);
            session.setAttribute("depTimeIntervals", depTimeIntervals);
            session.setAttribute("assetSelector", assetSelectVal);
        }
        request.setAttribute("showMessage", showMessage);
        request.setAttribute("dep_messageHeader", header);
        request.setAttribute("dep_message", message);
        getServletContext().getRequestDispatcher("/WEB-INF/depreciation.jsp").forward(request, response);
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
