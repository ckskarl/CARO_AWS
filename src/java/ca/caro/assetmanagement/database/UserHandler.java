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

/**
 * <b>User Handler Class</b>
 * <p>Class that handles interaction of the web application to the MS Server. 
 * Contains methods for handling the Users Related tables with operations such as adding, editing, and disabling users
 * as well as methods for getting user info.</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
public class UserHandler {
    
    /**
     *  This method is used to get all the the rows for users.
     *  <b>Row structure:</b> 
     *      [Personnel ID]; [Job Category]; [Username]; [Password];
     *      [First Name]; [Last Name]; [Date of Hire]; [Phone Number];
     *      [Email]; [Clearance Level]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getUsers()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> users = new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user;";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            
            while (rs.next())
            {
                int personel_id = rs.getInt("personel_id");
                String job_category_id = rs.getString("job_category_id");
                String user_name = rs.getString("user_name");
                String user_password = rs.getString("user_password");
                String user_first_name = rs.getString("user_first_name");
                String user_last_name = rs.getString("user_last_name");
                String date_of_hire = rs.getString("date_of_hire");
                String user_phone_number = rs.getString("user_phone_number");
                String user_email = rs.getString("user_email");
                int clearance_level = rs.getInt("clearance_level");
                int isActive = rs.getInt("isActive");
                
                users.add(personel_id+";"+job_category_id+";"+user_name+";"+user_password+";"+user_first_name+";"+
                        user_last_name+";"+date_of_hire+";"+user_phone_number+";"+user_email+";"+clearance_level+";"+
                        isActive);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return users;
    }

    /**
     *  This method is used to attempt to add a User into the database.
     *  <b>Parameter String[] structure:</b> 
     *      [Personnel ID], [Username], [Password],
     *      [First name], [Last name], [Date of Hire], 
     *      [Email], [Clearance Level]
     *  @param params String[] parameters for adding a User.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean addUser(String[] params)
    {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try
        {
            String query = "INSERT INTO "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "(personel_id, job_category_id, user_name, user_password, "
                    + "user_first_name, user_last_name, date_of_hire, user_phone_number, user_email, clearance_level, isActive) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1);";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, Integer.parseInt(params[0]));
            st.setString(2, params[1]);
            st.setString(3, params[2]);
            st.setString(4, params[3]);
            st.setString(5, params[4]);
            st.setString(6, params[5]);
            st.setString(7, params[6]);
            st.setString(8, params[7]);
            st.setString(9, params[8]);
            st.setInt(10, Integer.parseInt(params[9]));
            
            result = st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        try{conn.close();} catch(Exception e){e.printStackTrace();}
        if(result>0) {return true;}
        else {return false;}
    }
    
    /**
     *  This method is used to attempt to toggle a User enabled or disabled in the database.
     *  @param personel_id String User ID for the user to be deleted.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean toggleUser(String personel_id)
    {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try
        {
            String query = "UPDATE "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "SET isActive=(isActive+1)%2 "
                    + "WHERE personel_id=?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, Integer.parseInt(personel_id));
            
            result = st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        try{conn.close();} catch(Exception e){e.printStackTrace();}
        if(result>0) {return true;}
        else {return false;}
    }
    
    /**
     * This method is used to retrieve the isActive status for a specific user account in the database and will return that number, or a 3 if the query broke.
     * @param personel_id String User ID for the user to be queried
     * @return int Returns <b>1</b> or <b>0</b> valid numbers; or will return <b>3</b> if invalid query occurred.
     */
    public int getUserState(String personel_id)
    {
        Connection conn = MSSQLConnection.getConnection();
        int result = 3;
        try {
            String query = "SELECT isActive FROM " + MSSQLConnection.getDBName() + ".dbo.CARO_system_user "
                    + "WHERE personel_id=?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, Integer.parseInt(personel_id));
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                result = rs.getInt("isActive");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 3;
        }
        return result;
    }
    
    /**
     *  This method is used to attempt to edit a User into the database.
     *  <b>Parameter String[] structure:</b> 
     *      [Personnel ID], [Username], [Password],
     *      [First name], [Last name], [Date of Hire], 
     *      [Email], [Clearance Level]
     *  @param params String[] parameters for editing a User.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean editUser(String[] params)
    {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try
        {
            String query = "UPDATE "+MSSQLConnection.getDBName()+".dbo.CARO_system_user "
                    + "SET job_category_id = ?, user_name = ?, user_password=?, user_first_name=?"
                    + ", user_last_name=?, date_of_hire=?, user_phone_number=?, user_email=?, clearance_level=?"
                    + " WHERE personel_id =?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(10, Integer.parseInt(params[0]));
            st.setString(1, params[1]);
            st.setString(2, params[2]);
            st.setString(3, params[3]);
            st.setString(4, params[4]);
            st.setString(5, params[5]);
            st.setString(6, params[6]);
            st.setString(7, params[7]);
            st.setString(8, params[8]);
            st.setInt(9, Integer.parseInt(params[9]));
            
            
            result = st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        try{conn.close();} catch(Exception e){e.printStackTrace();}
        if(result>0) {return true;}
        else {return false;}
    }
    
    /**
     *  This method is used to get all the Job Category information.
     *  <b>Row structure:</b> 
     *      [Job Category ID]; [Job Title]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getJobCategories()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> jobCategories = new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_job_category;";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            
            while (rs.next())
            {
                String job_category_id = rs.getString("job_category_id");
                String job_title = rs.getString("job_title");
                
                jobCategories.add(job_category_id+";"+job_title);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return jobCategories;
    }
    
    /**
     *  This method is used to get all the Clearance Levels information.
     *  <b>Row structure:</b> 
     *      [Clearance Level]; [Level Description]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getClearanceLevels()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> clearanceLevels = new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_clearance_level;";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            
            while (rs.next())
            {
                int clearance_level = rs.getInt("clearance_level");
                String level_desc = rs.getString("level_desc");
                
                clearanceLevels.add(clearance_level+";"+level_desc);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return clearanceLevels;
    }
}
