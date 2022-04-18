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
package ca.caro.assetmanagement.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <b>Account Class</b>
 * <p>Class that handles interaction of the web application to the MS Server. 
 * Contains methods for handling basic Personal Account; methods include authenticating user, getting Account info, and 
 * editing user info.</p>
 * 
 * @author Neil Gilbert (761581)
 * 
 * @version 1.0
 */
public class Account {
    private static final String LOGGEDIN_SESSIONPARAMNAME = "loggedIn";
    private static final String USERID_SESSIONPARAMNAME = "userId";
    private static final String NAME_SESSIONPARAMNAME = "name";
    //private static final String CLEARANCE_SESSIONPARAMNAME = "accessLevel";
    
    /**
     *  This method is used to get all the the basic depreciation information.
     *  @param request HttpServletRequest servlet request
     *  @param username String Input username from login page
     *  @param password String Input password from login page
     *  @return boolean Returns <b>true</b> if authentication is a success, <b>false</b> if authentication failed
     */
    public static boolean authenticate(HttpServletRequest request, String username, String password)
    {
        HttpSession session = request.getSession();
        Connection conn = MSSQLConnection.getConnection();
        boolean result = false;
        try
        {
            String queryA = "SELECT count(*) FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "WHERE user_name = ? AND user_password = ?;";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setString(1, username);
            st.setString(2, password);
            
            ResultSet rs = st.executeQuery();
            
            while (rs.next())
            {
                int matches = rs.getInt(1);
                if(matches > 0)
                    result = true;
            }
            
            if(result) {
                String queryB = "SELECT user_first_name, user_last_name, personel_id, clearance_level FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "WHERE user_name = ? AND user_password = ?;";
                st = conn.prepareStatement(queryB);
                st.setString(1, username);
                st.setString(2, password);
                rs = st.executeQuery();
                
                while (rs.next())
                {
                    session.setAttribute(LOGGEDIN_SESSIONPARAMNAME, "true");
                    session.setAttribute(USERID_SESSIONPARAMNAME, rs.getInt("personel_id")+"");
                    session.setAttribute(NAME_SESSIONPARAMNAME, rs.getString("user_first_name") + " " + rs.getString("user_last_name"));
                }
                request.setAttribute("greetUser", username);
                LogHandler.recordLog(request, "[LOGIN_SUCCESS] User("+session.getAttribute(USERID_SESSIONPARAMNAME)+") successfully logged in.");
            } else 
                LogHandler.recordLog("[LOGIN_FAILED] Login attempt failed");
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     *  This method is used to get the Clearance Level.
     *  @param request HttpServletRequest servlet request
     *  @return int Returns the clearance level for the logged in user.
     */
    public static int getClearanceLevel(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        Connection conn = MSSQLConnection.getConnection();
        
        int result = 0;
        try
        {
            String queryA = "SELECT clearance_level FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "WHERE personel_id = ?";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setInt(1, Integer.parseInt(session.getAttribute(USERID_SESSIONPARAMNAME).toString()));
            
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                result = rs.getInt(1);
            }
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     *  This method is used to get the User Email.
     *  @param personel_id String user Personel ID
     *  @return String Returns the current user's email.
     */
    public static String getUserEmail(String personel_id)
    {
        Connection conn = MSSQLConnection.getConnection();
        
        String result = "";
        try
        {
            String queryA = "SELECT user_email FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "WHERE personel_id = ?";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setInt(1, Integer.parseInt(personel_id));
            
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                result=rs.getString("user_email");
            }
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }   
    
    /**
     * This method is used to get the status of the User with connection to database, the status is either active or inactive
     * @param request HttpServletRequest servlet request
     * @return true if the account is active, false if the account is inactive
     */
    public static boolean isActive(HttpServletRequest request)
    {
        Connection conn = MSSQLConnection.getConnection();
        HttpSession session = request.getSession();
        int result = -1;
        try
        {
            String queryA = "SELECT isActive FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "WHERE personel_id = ?";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setInt(1, Integer.parseInt(session.getAttribute(USERID_SESSIONPARAMNAME).toString()));
            
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                result=rs.getInt("isActive");
            }
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return (result==1);
    }
    
    
    
    /**
     *  This method is used to attempt to add Depreciation into the database.
     *  <b>Row structure:</b> 
     *      [First Name]; [Last Name]; [Date of Hire]; [Phone Number];
     *      [Email]; [Username]; [Clearance Level]
     *  @param request HttpServletRequest servlet request
     *  @return ArrayList of String  This returns a collection of the rows in an arraylist.
     */
    public static ArrayList<String> getAccountDetails(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        Connection conn = MSSQLConnection.getConnection();
        
        ArrayList<String> result = new ArrayList<String>();
        try
        {
            String queryA = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "WHERE personel_id = ?";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setInt(1, Integer.parseInt(session.getAttribute(USERID_SESSIONPARAMNAME).toString()));
            
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                result.add(rs.getString("user_first_name"));
                result.add(rs.getString("user_last_name"));
                result.add(rs.getString("date_of_hire"));
                result.add(rs.getString("user_phone_number"));
                result.add(rs.getString("user_email"));
                
                result.add(rs.getString("user_name"));
                result.add(rs.getInt("clearance_level")+"");
            }
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     *  This method is used to attempt to edit Account Personal Info.
     *  <b>Parameter String[] structure:</b> 
     *      [First Name]; [Last Name]; [Date of Hire]; 
     *      [Phone Number]; [Email]
     *  @param request HttpServletRequest servlet request
     *  @param params String[] parameters for editing an account.
     *  @return boolean Returns <b>true</b> if edit operation is success, <b>false</b> edit is failed
     */
    public static boolean editAccountPersonalInfo(HttpServletRequest request, String[] params)
    {
        HttpSession session = request.getSession();
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try
        {
            String query = "UPDATE "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "SET user_first_name = ?, user_last_name = ?, date_of_hire = ?, "
                    + "     user_phone_number = ?, user_email = ? "
                    + "WHERE personel_id =?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[0]);
            st.setString(2, params[1]);
            st.setString(3, params[2]);
            st.setString(4, params[3]);
            st.setString(5, params[4]);
            st.setInt(6, Integer.parseInt(session.getAttribute(USERID_SESSIONPARAMNAME).toString()));
            
            result = st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        try{conn.close();} catch(Exception e){e.printStackTrace();}
        if(result>0) {
            LogHandler.recordLog(request, "[ACCCHANGE_SUCCESS] User("+session.getAttribute(USERID_SESSIONPARAMNAME)+") changed personal info");
            return true;
        }
        else {
            LogHandler.recordLog(request, "[ACCCHANGE_FAILED] User("+session.getAttribute(USERID_SESSIONPARAMNAME)+") failed changed personal info");
            return false;
        }
    }
    
    /**
     *  This method is used to attempt to edit Account Info.
     *  <b>Parameter String[] structure:</b> 
     *      [Username]; [Password]
     *  @param request HttpServletRequest servlet request
     *  @param params String[] parameters for editing an account.
     *  @return boolean Returns <b>true</b> if edit operation is success, <b>false</b> edit is failed
     */
    public static boolean editAccountInfo(HttpServletRequest request, String[] params)
    {
        HttpSession session = request.getSession();
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try
        {
            String query = "UPDATE "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "SET user_name = ?, user_password = ? "
                    + "WHERE personel_id =?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[0]);
            st.setString(2, params[1]);
            st.setInt(3, Integer.parseInt(session.getAttribute(USERID_SESSIONPARAMNAME).toString()));
            
            result = st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        try{conn.close();} catch(Exception e){e.printStackTrace();}
        if(result>0) {
            LogHandler.recordLog(request, "[ACCCHANGE_S] User("+session.getAttribute(USERID_SESSIONPARAMNAME)+") changed account info");
            return true;
        }
        else {
            LogHandler.recordLog(request, "[ACCCHANGE_FAILED] User("+session.getAttribute(USERID_SESSIONPARAMNAME)+") failed changed account info");
            return false;
        }
    }
    
    /**
     *  This method to clear the session.
     *  @param request HttpServletRequest servlet request
     */
    public static void clear(HttpServletRequest request)
    {
        request.getSession().invalidate();
    }
    
    /**
     *  Method to check if user is logged in.
     *  @param request HttpServletRequest servlet request
     *  @return boolean Returns <b>true</b> if user is logged in, <b>false</b> isn't
     */
    public static boolean isLoggedIn(HttpServletRequest request)
    {
        return request.getSession().getAttribute(LOGGEDIN_SESSIONPARAMNAME)!=null;
    }
}
