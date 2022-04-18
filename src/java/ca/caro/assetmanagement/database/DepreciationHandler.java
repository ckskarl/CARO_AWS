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
import java.sql.Types;
import java.util.ArrayList;

/**
 * <b>Depreciation Handler Class</b>
 * <p>Class that handles interaction of the web application to the MS Server. 
 * Contains methods for handling the Depreciation Related tables with operations such as adding, editing, and deleting depreciation
 * as well as methods for getting depreciation info.</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
public class DepreciationHandler {
    private static MSSQLConnection connention = new MSSQLConnection();
    private static final String DBNAME = connention.getDBName();
   
    
    /**
     *  This method is used to get all the the basic depreciation information.
     *  <b>Row structure:</b> [Depreciation Info ID]; [Depreciation Description]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getDepreciations()
    {
        Connection conn = connention.getConnection();
        ArrayList<String> depreciation = new ArrayList<String>();
        try
        {
            String query = "SELECT deprec_info_id, deprec_desc "
                    + "FROM "+DBNAME +".dbo.CARO_depreciation_information";
            
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()){
                String result;
                result = rs.getString("deprec_info_id")+";"+
                        rs.getString("deprec_desc");
                depreciation.add(result);
            }
                      
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return depreciation;
    }
    
    /**
     *  This method is used to get more info for a specified depreciation ID.
     *  <b>Info structure:</b> 
     *      [Depreciation Info ID]; [Depreciation Description]; [Updated]; [Categories Applied];
     *      [Depreciation Formula]; [Custom Mode]; [Depreciation Rate]; [Time Interval ID]
     *  @param deprec_info_id String the ID of the depreciation that information will be retrieved
     *  @return String This returns a compiled information delimited by ';'
     */
    public String getDepreciationInfo(String deprec_info_id)
    {
        Connection conn = connention.getConnection();
        String depreciationInfo = "";
        try
        {
            String query = "SELECT * "
                    + "FROM "+DBNAME+".dbo.CARO_depreciation_information "
                    + "WHERE deprec_info_id = ?";
            
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, deprec_info_id);
            
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                depreciationInfo = rs.getString("deprec_info_id")+";"+
                        rs.getString("deprec_desc")+";"+
                        rs.getString("deprec_updated")+";"+
                        rs.getString("categories_applied")+";"+
                        rs.getString("deprec_formula")+";"+
                        rs.getBoolean("deprec_custom_mode")+";"+
                        rs.getString("deprec_rate")+";"+
                        rs.getString("time_interval_id");
            }
            System.out.println("DEPREC: "+depreciationInfo);
                      
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return depreciationInfo;
    }

 public String getDetailedDepreciationInfo()
    {
        Connection conn = connention.getConnection();
        String depreciationInfo = "";
        try
        {
            String query = "SELECT * "
                    + "FROM "+DBNAME+".dbo.CARO_depreciation_information ";
            
            PreparedStatement st = conn.prepareStatement(query);
            
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                depreciationInfo += rs.getString("deprec_info_id")+";"+
                        rs.getString("deprec_desc")+";"+
                        rs.getString("deprec_updated")+";"+
                        rs.getString("categories_applied")+";"+
                        rs.getString("deprec_formula")+";"+
                        rs.getBoolean("deprec_custom_mode")+";"+
                        rs.getString("deprec_rate")+";"+
                        rs.getString("time_interval_id")+"&&";
            }
                      
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return depreciationInfo;
    }
 
    /**
     *  This method is used to get all the time interval information.
     *  <b>Row structure:</b> [Time Interval ID]; [Time Interval Desc]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getTimeIntervals()
    {
        Connection conn = connention.getConnection();
        ArrayList<String> timeIntervals = new ArrayList<String>();
        try
        {
            String query = "SELECT * "
                    + "FROM "+DBNAME +".dbo.CARO_depreciation_time_interval";
            
            PreparedStatement st = conn.prepareStatement(query);
            
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                timeIntervals.add(rs.getString("time_interval_id")+";"+
                        rs.getString("time_interval_desc"));
            }
                      
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return timeIntervals;
    }
    
    /**
     *  This method is used to attempt to add Depreciation into the database.
     *  <b>Parameter String[] structure:</b> 
     *      [Depreciation Info ID], [Depreciation Description]
     *  @param params String[] parameters for adding a depreciation.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean addDepreciation(String[] params)
    {
        Connection conn = connention.getConnection();
        int result = 0;
        try
        {
            String query = "INSERT INTO "+DBNAME +".dbo.CARO_depreciation_information "
                    + "(deprec_info_id,deprec_desc,deprec_updated) "
                    + "VALUES (?, ?, GETDATE())";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[0]);
            st.setString(2, params[1]);
            
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
     *  This method is used to attempt to delete a Depreciation in the database.
     *  @param deprec_info_id String Depreciation ID for the depreciation to delete.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean deleteDepreciation(String deprec_info_id)
    {
        Connection conn = connention.getConnection();
        int result = 0;
        try
        {
            String query = "DELETE FROM "+DBNAME +".dbo.CARO_depreciation_information "
                    + "WHERE deprec_info_id=?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, deprec_info_id);
            
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
     *  This method is used to attempt to edit a Depreciation in the database.
     *  <b>Parameter String[] structure:</b> 
     *      [Depreciation Info ID]; [Depreciation Description]; [Updated]; [Categories Applied];
     *      [Depreciation Formula]; [Custom Mode]; [Depreciation Rate]; [Time Interval ID]
     *  @param params String[] parameters for editing a depreciation.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean updateDepreciationInfo(String[] params)
    {
        Connection conn = connention.getConnection();
        int result = 0;
        try
        {
            String query = "UPDATE "+DBNAME +".dbo.CARO_depreciation_information "
                    + "SET deprec_desc = ?, deprec_updated = GETDATE(), categories_applied = ?, deprec_formula = ?, "
                    + "     deprec_custom_mode = ?, deprec_rate = ?, time_interval_id = ? "
                    + "WHERE deprec_info_id =?;";
            PreparedStatement st = conn.prepareStatement(query);
            
            if( !(params[1].equals("") || params[1].equals("null")) ){
                st.setString(1, params[1]);
            } else {
                st.setNull(1, Types.VARCHAR);
            }
            
            if( !(params[2].equals("") || params[2].equals("null")) ){
                st.setString(2, params[2]);
            } else {
                st.setNull(2, Types.VARCHAR);
            }
            
            if( !(params[3].equals("") || params[3].equals("null")) ){
                st.setString(3, params[3]);
            } else {
                st.setNull(3, Types.VARCHAR);
            }
            
            if(params[4]!=null)
            {
                if( !(params[4].equals("") || params[4].equals("null")) ){
                    st.setBoolean(4, true);
                } else {
                    st.setBoolean(4, false);
                }
            } else { st.setBoolean(4, false); }
            
            //EDITED
            st.setDouble(5,  Double.parseDouble(String.format("%.2f", Double.parseDouble(params[5]))));
            
            if( !(params[6].equals("") || params[6].equals("null")) ){
                st.setString(6, params[6]);
            } else {
                st.setNull(6, Types.VARCHAR);
            }
            
            
            st.setString(7, params[0]);
            
            result=st.executeUpdate();
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
}
