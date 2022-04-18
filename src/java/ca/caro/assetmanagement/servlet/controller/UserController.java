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

import ca.caro.assetmanagement.database.LogHandler;
import ca.caro.assetmanagement.database.UserHandler;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>User Controller Class</b>
 * <p>Class that handles what data is sent to the web page. Forward to different page mainly base on action attribute</p>
 * 
 * @author Neil Gilbert (761581)
 * @author Christian Lay
 * @version 1.0
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {
    

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
        int clearanceLv =Integer.parseInt(session.getAttribute("clearanceLv").toString());
          
        if (session.getAttribute("loggedIn") != null) {
            
            if (request.getParameter("action") != null) {
                if (clearanceLv==0){
                //Asset params:
                //"asset_id";"asset_desc";"serial_number";"asset_category";"cost";"brand_id";"purchase_date";
                UserHandler userH = new UserHandler();

                switch (request.getParameter("action")) {
                    case "viewCategories":

                    case "importFile":

                        break;
                        
                    case "addUser": 
                        String[] addUserParams = 
                            {
                                request.getParameter("personel_id"),
                                request.getParameter("job_category_id"),
                                request.getParameter("user_name"),
                                request.getParameter("user_password"),
                                request.getParameter("user_first_name"),
                                request.getParameter("user_last_name"),
                                request.getParameter("date_of_hire"),
                                request.getParameter("user_phone_number"),
                                request.getParameter("user_email"),
                                request.getParameter("clearance_level")
                            };
                        if(userH.addUser(addUserParams)){
                            LogHandler.recordLog(request, "[ADDUSER_SUCCESS] User("+session.getAttribute("userId")+") succesfully added asset("+addUserParams[0]+").");
                            request.setAttribute("messageHeader", "User Added");
                            request.setAttribute("message", "<p style='color:green'>User successfully added!</p>");
                        } else {
                            LogHandler.recordLog(request, "[ADDUSER_FAILED] User(" + session.getAttribute("userId") + ") failed to add user.");
                            request.setAttribute("messageHeader", "User Adding Fail");
                            request.setAttribute("message", "<p style='color:red'>Adding user failed!</p>");
                        }
                        sendToUsers(request, response, true);
                        break;

                    case "editUser":
                        String[] editUserParams = 
                            {
                                request.getParameter("personel_id"),
                                request.getParameter("job_category_id"),
                                request.getParameter("user_name"),
                                request.getParameter("user_password"),
                                request.getParameter("user_first_name"),
                                request.getParameter("user_last_name"),
                                request.getParameter("date_of_hire"),
                                request.getParameter("user_phone_number"),
                                request.getParameter("user_email"),
                                request.getParameter("clearance_level")
                            };
                        if(userH.editUser(editUserParams)) { 
                            LogHandler.recordLog(request, "[EDITUSER_SUCCESS] User("+session.getAttribute("userId")+") succesfully edited user("+editUserParams[0]+").");
                            request.setAttribute("messageHeader", "User Edited");
                            request.setAttribute("message", "<p style='color:green'>User successfully edited!</p>");
                        } else {
                            LogHandler.recordLog(request, "[EDITUSER_FAILED] User(" + session.getAttribute("userId") + ") failed to edited user.");
                            request.setAttribute("messageHeader", "User Editing Fail");
                            request.setAttribute("message", "<p style='color:red'>Editing user failed!</p>");
                        }
                        sendToUsers(request, response, true);
                        break;

                    case "deleteUser":
                        if (userH.toggleUser(request.getParameter("del_user_id"))) {
                            LogHandler.recordLog(request, "[DISABLEUSER_SUCCESS] User(" + session.getAttribute("userId") + ") succesfully deleted user(" + request.getParameter("del_user_id") + ").");
                            if (userH.getUserState(request.getParameter("del_user_id")) == 0) {
                                request.setAttribute("messageHeader", "User Disabled");
                                request.setAttribute("message", "<p style='font-size:200%;color:red'>User successfully disabled!</p>");
                            } else if (userH.getUserState(request.getParameter("del_user_id")) == 1) {
                                request.setAttribute("messageHeader", "User Enabled");
                                request.setAttribute("message", "<p style='font-size:200%;color:green'>User successfully enabled!</p>");
                            }
                        } else {
                            LogHandler.recordLog(request, "[DISABLEUSER_FAILED] User(" + session.getAttribute("userId") + ") failed to succesfully user.");
                            request.setAttribute("messageHeader", "User Status");
                            request.setAttribute("message", "<p style='font-size:200%;color:red'>User status failed toggle!</p>");
                        }
                        sendToUsers(request, response, true);
                        break;
                    default:
                        sendToUsers(request, response, false);
                }
                }
            } else {
                sendToUsers(request, response, false);
            }
        } else {
            sendToLogin(request, response, "");
        }

    }

    /**
     *  This method is used to help servlet send the user to the users page with the needed parameters.
     *  Retrieves all data needed by the asset page to display required asset information
     * 
     *  @param request HttpServletRequest servlet request
     *  @param response HttpServletResponse servlet response
     *  @param showMssg String servlet Message of the message box
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    private void sendToUsers(HttpServletRequest request, HttpServletResponse response, boolean showMssg) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String userList = "";
        UserHandler userHandler = new UserHandler();
        if (session.getAttribute("jobCat")==null){
            

            
            String jobCat = "";
            String clearances = "";
            

            ArrayList<String> jobCategories= userHandler.getJobCategories();
            for (String jC : jobCategories) {
                if (!jC.equals(jobCategories.get(0))) {
                    jobCat += ",";
                }

                jobCat += jC;
            }
            ArrayList<String> clearanceLevels = userHandler.getClearanceLevels();
            for (String clr : clearanceLevels) {
                if (!clr.equals(clearanceLevels.get(0))) {
                    clearances += ",";
                }

                clearances += clr;
            }
            
            session.setAttribute("jobCat", jobCat);
            session.setAttribute("clearances", clearances);
            
            ArrayList<String> users = userHandler.getUsers();
            for (String user : users) {
                if (!user.equals(users.get(0))) {
                    userList += ",";
                }

                userList += user;
            }
        session.setAttribute("users", userList);       
       }
                    
        request.setAttribute("showMessage", showMssg);
        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
    }

    /**
     *  This method is used to help servlet send the user to the Login page.
     * 
     *  @param request HttpServletRequest servlet request
     *  @param response HttpServletResponse servlet response
     *  @param message String servlet Message of the message box
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    private void sendToLogin(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        getServletContext().getRequestDispatcher("/authenticateUser").forward(request, response);
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
