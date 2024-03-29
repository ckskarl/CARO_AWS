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

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>Page Navigation Class</b>
 * <p>Class that handles where to send the user if they clicked a link in the nav side bar.
 * Additional methods created are meant to help the servlet. Forward to different page mainly base on targetPage attribute</p>
 * 
 * @author Kin Shing Chong
 * @version 1.0
 */
@WebServlet(name = "pageNavigation", urlPatterns = {"/pageNavigation"})
public class PageNavigation extends HttpServlet {

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
        
            String targetPage = request.getParameter("targetPage");
            HttpSession session = request.getSession();
            int clearanceLv =Integer.parseInt(session.getAttribute("clearanceLv").toString());
            
            if (targetPage.equals("manageAsset")){
                
                getServletContext().getRequestDispatcher("/assetController").forward(request, response);
            }else if (targetPage.equals("manageDepreciation")){
                //if (clearanceLv==0){
                getServletContext().getRequestDispatcher("/DepreciationController").forward(request, response);
                //}
            }else if (targetPage.equals("reports")){
                //if (clearanceLv==0){
                getServletContext().getRequestDispatcher("/ReportController").forward(request, response);
                //}
            }else if (targetPage.equals("users")){
                //if (clearanceLv==0){
                getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                //}
            }else if (targetPage.equals("homePage")){
                getServletContext().getRequestDispatcher("/WEB-INF/main.jsp").forward(request, response);
            }
            
        
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
