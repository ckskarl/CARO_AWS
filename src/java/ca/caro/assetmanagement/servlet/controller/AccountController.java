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
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>Account Controller Class</b>
 * <p>Class that handles what data is sent to the <b>account.jsp</b> web page. 
 * Also handles action sent from the <b>account.jsp</b> web page if applicable.
 * Most methods are auto-generated from being a generated servlet, additional methods created are meant to help the servlet</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
@WebServlet(name = "AccountController", urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Forward to different page mainly base on action attribute
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(!Account.isLoggedIn(request))
        {
            response.sendRedirect("/CARO");
        } else {
            if(request.getParameter("action")!=null){
                switch(request.getParameter("action"))
                {
                    case "personalinfo_edit":
                        String[] pInfoParams = {
                            request.getParameter("user_first_name"), 
                            request.getParameter("user_last_name"), 
                            request.getParameter("date_of_hire"), 
                            request.getParameter("user_phone_number"), 
                            request.getParameter("user_email")
                        };
                        
                        if(Account.editAccountPersonalInfo(request, pInfoParams)){
                            sendToAccount(request, response, "Personal information changed");
                        } else {
                            sendToAccount(request, response, "Failed to change personal info");
                        }
                        break;
                    case "accountinfo_edit":
                        if( !request.getParameter("user_name").equals("") &&
                            !request.getParameter("user_password1").equals("") &&
                            !request.getParameter("user_password2").equals("")){
                            if(request.getParameter("user_password1").equals(request.getParameter("user_password2"))) {
                                String[] aInfoParams = {
                                    request.getParameter("user_name"), 
                                    request.getParameter("user_password1")
                                };

                                if(Account.editAccountInfo(request, aInfoParams)){
                                    sendToAccount(request, response, "Login info changed");
                                } else {
                                    sendToAccount(request, response, "Failed to change login info");
                                }
                            } else {
                                sendToAccount(request, response, "Passwords do not match");
                            }
                        } else {
                            sendToAccount(request, response, "All fields need to be filled");
                        }
                        break;
                    default:
                        sendToAccount(request, response, "No action");
                }
            }
            else
            {
                sendToAccount(request, response, "");
            }
        }
    }
    
    /**
     *  This method is used to help servlet send the user to the account page with the needed parameters.
     *  Retrieves all data needed by the account page to display required account information
     * 
     *  @param request HttpServletRequest servlet request
     *  @param response HttpServletResponse servlet response
     *  @param message String servlet Message of the message box
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    private void sendToAccount(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException
    {  
        ArrayList<String> accP = Account.getAccountDetails(request);
        request.setAttribute("user_first_name", accP.get(0));
        request.setAttribute("user_last_name", accP.get(1));
        request.setAttribute("date_of_hire", accP.get(2));
        request.setAttribute("user_phone_number", accP.get(3));
        request.setAttribute("user_email", accP.get(4));
        request.setAttribute("user_name", accP.get(5));
        request.setAttribute("clearance_level", accP.get(6));
        
        request.setAttribute("acc_message", message);
        getServletContext().getRequestDispatcher("/WEB-INF/account.jsp").forward(request, response);
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
