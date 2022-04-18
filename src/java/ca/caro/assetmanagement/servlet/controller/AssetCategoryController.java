package ca.caro.assetmanagement.servlet.controller;

import ca.caro.assetmanagement.database.AssetCategoryHandler;
import ca.caro.assetmanagement.database.AssetHandler;
import ca.caro.assetmanagement.database.LogHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>Report Controller Class</b>
 * <p>Class that handles what data is sent to the <b>asset_category_page.jsp</b> web page. 
 * Also handles action sent from the <b>asset_category_page.jsp</b> web page if applicable.
 * Most methods are auto-generated from being a generated servlet, additional methods created are meant to help the servlet</p>
 * 
 * @author Kin Shing Chong
 * @version 1.0
 */
@WebServlet(name = "AssetCategoryController", urlPatterns = {"/AssetCategoryController"})
public class AssetCategoryController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Forward to different page mainly base on action attribute
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if target is not found
     * @throws IOException if target path illegal
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int clearanceLv =Integer.parseInt(session.getAttribute("clearanceLv").toString());
            
    
        if(session.getAttribute("loggedIn")!=null){
            if(request.getParameter("action")!=null)
            {
                AssetCategoryHandler assetCH = new AssetCategoryHandler();
                
                switch(request.getParameter("action"))
                {
                    case "viewCategories":
                        sendToAssetCategory(request, response, "");;
                        break;
                    case "viewAssets":
                        sendToAssets(request, response, "");;
                        break;   
                    case "addCategory": 
                        if (clearanceLv==0){
                        String[] addAssetCatParams = 
                            {
                                request.getParameter("add_cat_id"),
                                request.getParameter("add_cat_desc"),
                            };
                        if(assetCH.addAssetCategory(addAssetCatParams)){
                            LogHandler.recordLog(request, "[ADD_ASSET_CATEGORY_SUCCESS] User("+session.getAttribute("userId")+") succesfully added asset category("+addAssetCatParams[0]+").");
                            request.setAttribute("message", "Asset category successfully added");
                        }
                        else{
                            LogHandler.recordLog(request, "[ADD_ASSET_CATEGORY_FAILED] User("+session.getAttribute("userId")+") failed to add asset category("+addAssetCatParams[0]+").");
                            request.setAttribute("message", "Adding asset category failed");
                        }
                        sendToAssetCategory(request, response, "Added");
                        }
                        break;
                        
                    case "editCad":
                        if (clearanceLv==0){
                        //System.out.print(request.getParameter("edit_cat_id"));
                        String[] editCategoryParams = 
                            {
                                request.getParameter("edit_cat_id"),
                                request.getParameter("edit_cat_desc"),
                                request.getParameter("edit_new_cat_id"),
                            };
                        System.out.println(editCategoryParams[0]+editCategoryParams[1]);
                        if(assetCH.editAssetCad(editCategoryParams)) { 
                            LogHandler.recordLog(request, "[EDIT_ASSET_CATEGORY_SUCCESS] User("+session.getAttribute("userId")+") succesfully edited asset category("+editCategoryParams[0]+").");
                            request.setAttribute("message", "Asset Category successfully edited");
                        } else {
                            LogHandler.recordLog(request, "[EDIT_ASSET_CATEGORY_FAILED] User("+session.getAttribute("userId")+") failed to edited asset category("+editCategoryParams[0]+").");
                            request.setAttribute("message", "Editing asset category failed"); 
                        }
                        sendToAssetCategory(request, response, "Edited");
                        }
                        break;
                    case "deleteCad":
                        if (clearanceLv==0){
                        System.out.print(request.getParameter("edit_cat_id"));
                        if(assetCH.deleteAssetCad(request.getParameter("edit_cat_id"))){
                            LogHandler.recordLog(request, "[DELETE_ASSET_CATEGORY_SUCCESS] User("+session.getAttribute("userId")+") succesfully deleted asset category("+request.getParameter("edit_cat_id")+").");
                            request.setAttribute("message", "Asset category successfully deleted");
                        } else {
                            LogHandler.recordLog(request, "[DELETE_ASSET_CATEGORY_FAILED] User("+session.getAttribute("userId")+") failed to delete asset category.");
                            request.setAttribute("message", "Deleting asset category failed");
                        }
                        sendToAssetCategory(request, response, "Deleted");
                        }
                        break;
                    default:
                        sendToAssetCategory(request, response, "");
                }
            } else {
                sendToAssets(request, response, "");
            }
        }
        else
        {
            sendToLogin(request, response, "");
        }
    
    }

     /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Get information of assets by SQL and Forward the result to /WEB-INF/asset.jsp
     *
     * @param request servlet request
     * @param response servlet response
     * @param message
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void sendToAssets(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException
    {
        AssetHandler assetHandler = new AssetHandler();
        HttpSession session = request.getSession();
        if (session.getAttribute("assetLoaded")==null){
        
            String assetList = "";
            for(String asset : assetHandler.getAssets_with_assigned_to())
            {
                if(!asset.equals(assetHandler.getAssets_with_assigned_to().get(0)))
                    assetList+=",";

                assetList+=asset;
            }
            session.setAttribute("assets", assetList);
        }
        getServletContext().getRequestDispatcher("/WEB-INF/asset.jsp").forward(request, response);
    }
    
    
    
     /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Forward the result to /authenticateUser
     *
     * @param request servlet request
     * @param response servlet response
     * @param message message
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */    
    private void sendToLogin(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException
    {
        getServletContext().getRequestDispatcher("/authenticateUser").forward(request, response);
    }
    
    
     /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Get information of asset category by SQL and Forward the result to /WEB-INF/asset_category_page.js
     *
     * @param request servlet request
     * @param response servlet response
     * @param message message
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */    
    private void sendToAssetCategory(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        
        HttpSession session = request.getSession();
         AssetCategoryHandler assetCH = new AssetCategoryHandler();
        //if (session.getAttribute("assetCadLoaded")==null){
           // AssetCategoryHandler assetCH = new AssetCategoryHandler();
            String assetCategoryList = "";
            

            for (String assetCategory : assetCH.getAssetCategories_with_count()) {
                if (!assetCategory.equals(assetCH.getAssetCategories_with_count().get(0))) {
                    assetCategoryList += ",";
                }
                assetCategoryList += assetCategory;
            }

            System.out.println(assetCategoryList);
            request.setAttribute("assetCategories", assetCategoryList);
            
//            /session.setAttribute("assetCadLoaded", true);
        //}
        int numOfCategory=0;
       
            numOfCategory = assetCH.getNumOfCategory();
            session.setAttribute("numOfCategory", numOfCategory);
        getServletContext().getRequestDispatcher("/WEB-INF/asset_category_page.jsp").forward(request, response);
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
