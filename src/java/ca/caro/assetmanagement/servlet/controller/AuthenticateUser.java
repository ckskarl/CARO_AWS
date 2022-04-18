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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>Authenticate User Class</b>
 * <p>Class that handles what data and control in the <b>login.jsp</b> page. 
 * Also handles action sent from the <b>login.jsp</b> web page if applicable.
 * Calls methods to authenticate passed login info allows user to proceed if authentication succeeds or sends them back to the login form if authentication failed.
 * Most methods are auto-generated from being a generated servlet, additional methods created are meant to help the servlet</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
@WebServlet(name = "authenticateUser", urlPatterns = {"/authenticateUser"})
public class AuthenticateUser extends HttpServlet {

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
        if(request.getParameter("action")!=null){
            switch(request.getParameter("action"))
            {
                case "login":
                    String user = request.getParameter("username");
                    String pass = request.getParameter("password");
                    if(user!=null && pass!=null)
                    {
                        if(!user.equals("") && !pass.equals(""))
                        {
                            //User and password authentication
                            if(Account.authenticate(request, user, pass))
                            {
                                if (Account.isActive(request)){
                                  //System.out.println("Active account");
                                    HttpSession session = request.getSession();
                                  //System.out.println("USERID: "+request.getSession().getAttribute("userId"));
                                  //System.out.println("CLEARANCE: "+Account.getClearanceLevel(request));
                                  session.setAttribute("greetUser", user);
                                  session.setAttribute("clearanceLv", Account.getClearanceLevel(request));
                                  request.getRequestDispatcher("/WEB-INF/main.jsp").forward(request, response);
                                }else {
                                    //System.out.println("InActive account");
                                    sendToLogin(request, response, "Account is Inactive, Please contact Admin.");
                                }
                                
                            }
                            else
                            {
                                sendToLogin(request, response, "Invalid Username and Password");
                            }
                        }
                        else
                        {
                            sendToLogin(request, response, "Username and Password are required");
                        }
                    }
                    else
                    {
                        sendToLogin(request, response, "Username and Password are required");
                    }
                    break;
                
                case "logout":
                    Account.clear(request);
                    sendToLogin(request, response, "Logged out");
                    break;
            }
        }
        else
        {
            if(!Account.isLoggedIn(request))
            { 
                sendToLogin(request, response, "");
            }
            else{
                getServletContext().getRequestDispatcher("/assetController").forward(request, response);
            }
        }
        
    }
    
    /**
     *  This method is used to help servlet send the user to the login page with the needed parameters.
     *  Able to display message for login status purposes.
     * 
     *  @param request HttpServletRequest servlet request
     *  @param response HttpServletResponse servlet response
     *  @param message String servlet Message of the message box
     *  @throws ServletException if a servlet-specific error occurs
     *  @throws IOException if an I/O error occurs
     */
    private void sendToLogin(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException
    {
        request.setAttribute("message", message);
        
        
        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
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
