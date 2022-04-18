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
import ca.caro.assetmanagement.systemadministration.Backup;
import ca.caro.assetmanagement.systemadministration.DataArchiver;
import ca.caro.assetmanagement.systemadministration.Restore;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <b>Backup Restore Controller Class</b>
 * <p>Class that's mainly used to get evoke methods to do a user backup action
 * Most methods are auto-generated from being a generated servlet, additional methods created are meant to help the servlet</p>
 * 
 * @author Brandon Donkersloot
 * @version 1.0
 */
@WebServlet(name = "BackupRestoreController", urlPatterns = {"/BackupRestoreController"})
public class BackupRestoreController extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.Forward to different page mainly base on action attribute, related to backup, restore and archive
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String backupOrRestore = request.getParameter("backupOrRestore");
        String backupName = request.getParameter("backupName");
        
        String restoreName = request.getParameter("restoreName");
        HttpSession session = request.getSession();
         int clearanceLv =Integer.parseInt(session.getAttribute("clearanceLv").toString());
         
        if (backupOrRestore == null) {
            getServletContext().getRequestDispatcher("/UserController").forward(request, response);
        } else {
            if (clearanceLv==0){
                if (backupOrRestore.equals("backup")) {
                    Backup backup = new Backup();
                    try {
                        if (backup.Backupdb(backupName)) {
                            request.setAttribute("message", "Backup Completed");
                            getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                        } else {
                            request.setAttribute("message", "Backup incomplete");
                            System.out.println("Backup could not be completed ");
                            getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                        }

                    } catch (Exception e) {
                        request.setAttribute("message", "Backup error");
                        System.out.println("Backup recieved an error ");
                        getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                    }
                } else if (backupOrRestore.equals("restore")) {
                    Restore restore = new Restore();   
                    try {
                        if (restore.Restoredb(restoreName)) {
                            request.setAttribute("message", "Restore Completed");
                        getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                        } else {
                            request.setAttribute("message", "Restore incomplete");
                            System.out.println("Something went wrong with the restore ");
                           getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                        }



                    } catch (Exception e) {
                        request.setAttribute("message", "Restore error");
                        System.out.println("Restore recieved an error");
                        getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                
                    }

                } else if (backupOrRestore.equals("clearArchive")) {  
                    try {
                        DataArchiver.deleteOldArchive();
                        request.setAttribute("message", "Old Archive has been removed");
                        getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                    } 
                    catch (Exception e) {
                        request.setAttribute("message", "Remove Archive error");
                        getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                    }

                } else {
                    getServletContext().getRequestDispatcher("/UserController").forward(request, response);
                }
            }
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
