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
 * <b>Asset Handler Class</b>
 * <p>Class that handles interaction of the web application to the MS SQL Server. 
 * Contains methods for handling the Asset Related tables with operations such as adding, editing, and deleting asset
 * as well as methods for getting asset info.</p>
 * 
 * @author Neil Gilbert (761581)
 * @author Kin Shing Chong 
 * @author Nagi Nabal
 * @author Alexander Fleury
 * @version 1.0
 */
public class AssetHandler {
    public static final String DBNAME = MSSQLConnection.getDBName();
    
    /**
     * Get the detailed asset information by connecting with database and perform SQL
     * The information is returned as ArrayList.
     * @return ArrayList  containing all detailed asset information
     */
    public ArrayList<String> getAssets_with_assigned_to() {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> assets = new ArrayList<String>();
        try {
            String query = "SELECT su.user_first_name+' '+su.user_last_name \"added_by_name\",su.user_email \"added_by_email\","
                    + "su2.user_first_name+' '+su2.user_last_name \"assigned_to_name\",su2.user_email \"assigned_to_email\", "
                    + "su.user_name, su.personel_id \"added by\", a_t.personel_id \"assigned_to_id\", a.asset_id,* "
                    + "FROM " + DBNAME + ".dbo.CARO_asset a "
                    + "full join " + DBNAME + ".dbo.CARO_asset_category ac ON (a.asset_category=ac.asset_category)"
                    + "full join " + DBNAME + ".dbo.CARO_vendor v ON (a.vendor_id=v.vendor_id)"
                    + "full join " + DBNAME + ".dbo.CARO_system_user su ON (a.added_by=su.personel_id)"
                    + "full join " + DBNAME + ".dbo.CARO_brand b ON (a.brand_id=b.brand_id)"
                    + "full join " + DBNAME + ".dbo.CARO_location l ON (a.location_id=l.location_id) "
                    + "full join " + DBNAME + ".dbo.CARO_assigned_to a_t ON (a_t.asset_id = a.asset_id)"
                    + "full join " + DBNAME + ".dbo.CARO_system_user su2 ON (a_t.personel_id = su2.personel_id)"
                    + "full join " + DBNAME + ".dbo.CARO_depreciation_information di ON (a.deprec_info_id = di.deprec_info_id)"
                    + "where a.asset_id is NOT NULL";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String result;
                result = rs.getString("asset_id") + ";"
                        + rs.getString("asset_name") + ";"
                        + (rs.getString("asset_desc")==null?"No Data":rs.getString("asset_desc")) + ";" //testing
                        + (rs.getString("photo_link")==null?"No Data": (rs.getString("photo_link").equals("")?"No Data":rs.getString("photo_link"))) + ";"
                        + (rs.getString("asset_category_description")==null?"No Data":rs.getString("asset_category_description")) + ";"
                        + (rs.getString("purchase_date")==null?"No Data":rs.getString("purchase_date")) + ";"
                        + rs.getFloat("cost") + ";"
                        + rs.getFloat("depreciated_value") + ";"
                        //no comma for location_name data otherwise wont read properly
                        + rs.getString("location_name")+";"
                        + rs.getString("location_phone") + ";"
                        + rs.getString("added_by_name") + ";"
                        + rs.getString("added_by_email") + ";"
                        + (rs.getString("dimension")==null?"No Data":rs.getString("dimension")) + ";"
                        + (rs.getString("IP")==null?"No Data":(rs.getString("IP"))) + ";"
                        + (rs.getString("MAC")==null?"No Data":rs.getString("MAC")) + ";"
                        + (rs.getString("serial_number")==null?"No Data":rs.getString("serial_number")) + ";"
                        + (rs.getString("Hostname")==null?"No Data":rs.getString("Hostname")) + ";"
                        + (rs.getString("service_tag")==null?"No Data":rs.getString("service_tag")) + ";"
                        + (rs.getString("comment_on_asset")==null?"No Data":rs.getString("comment_on_asset")) + ";"
                        + rs.getString("brand_name") + ";"
                        + rs.getString("vendor_name") + ";"
                        + (rs.getString("vendor_address")==null?"No Data":rs.getString("vendor_address")) + ";"
                        + (rs.getString("vendor_phone_number")==null?"No Data":rs.getString("vendor_phone_number")) + ";"
                        + (rs.getString("assigned_to_name")==null?"No Data":rs.getString("assigned_to_name")) + ";"
                        + (rs.getString("assigned_to_email")==null?"No Data":rs.getString("assigned_to_email")) + ";"
                        + (rs.getString("assigned_to_id")==null?"No Data":rs.getString("assigned_to_id")) + ";"
                        + rs.getString("location_id") + ";"
                        + (rs.getString("brand_id")==null?"No Data":rs.getString("brand_id")) + ";"
                        + (rs.getString("vendor_id")==null?"No Data":rs.getString("vendor_id")) + ";"
                        + rs.getString("asset_category") + ";"
                        + (rs.getString("deprec_info_id")==null?"No Data":rs.getString("deprec_info_id")) + ";"
                        + (rs.getString("deprec_desc")==null?"No Data":rs.getString("deprec_desc")) + ";"
                        + (rs.getString("OS")==null?"No Data":rs.getString("OS"));
                //System.out.println(rs.getString("OS"));
                assets.add(result);
            }
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assets;
    }    


    /**
     *  This method is used to get all the Asset Categories.
     *  <b>Row structure:</b> [Asset Categories]; [Category Description]
     *  @return ArrayList of String  This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getAssetCategories()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> categories = new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+DBNAME+".dbo.CARO_asset_category";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String result;
                result = rs.getString("asset_category")+";"+
                        rs.getString("asset_category_description");
                categories .add(result);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Get the information needed for inserting asset into archive table by connecting to database and performing SQL
     * return ArrayList containing the information.
     * @param asset_id the unique identifier of the asset
     * @return ArrayList containing the information needed for inserting asset into archive table
     */
    public ArrayList<String> getArchiveParams(String asset_id)
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> archiveParams = new ArrayList<String>();
        try
        {
            String query = "SELECT asset_id, purchase_date, cost ,"
                    + "asset_name, asset_desc, comment_on_asset, asset_category   FROM "+DBNAME+".dbo.CARO_asset "
                    + "WHERE asset_id = ? ";
            PreparedStatement st = conn.prepareStatement(query);
             st.setString(1, asset_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                archiveParams.add(rs.getString(1));
                archiveParams.add(rs.getString(2));
                archiveParams.add(String.valueOf(rs.getDouble(3)));
                archiveParams.add(rs.getString(4));
                archiveParams.add(rs.getString(5));
                archiveParams.add(rs.getString(6));
                archiveParams.add(rs.getString(7));
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return archiveParams;
    }

    
    /**
     *  This method is used to get all the Asset Brands.
     *  <b>Row structure:</b> [Brand ID]; [Brand Name]
     *  @return ArrayList of String  This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getAssetBrands()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> brands = new ArrayList<String>();
        try
        {
            String query = "SELECT brand_id,brand_name FROM "+DBNAME+".dbo.CARO_brand";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String result;
                result = rs.getString(1)+";"+
                        rs.getString(2);
                brands.add(result);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return brands;
    }
    
    /**
     *  This method is used to get all the Asset Vendors.
     *  <b>Row structure:</b> 
     *      [Vendor ID]; [Vendor Name];
     *      [Vendor Address]; [Phone Number]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getAssetVendors()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> vendors= new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+DBNAME+".dbo.CARO_vendor";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String result;
                result = rs.getString("vendor_id")+";"+
                        rs.getString("vendor_name")+";"+
                        rs.getString("vendor_address")+";"+
                        rs.getString("vendor_phone_number");
                vendors.add(result);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //System.out.println(vendors);
        return vendors;
    }
    
    /**
     *  This method is used to get all the Locations.
     *  <b>Row structure:</b> 
     *      [Location ID]; [Location Name];
     *      [Location Address]; [Location Phone];
     *      [Email]; [# of Staff];
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getLocations()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> locations= new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+DBNAME+".dbo.CARO_location";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String result;
                result = rs.getString("location_id")+";"+
                        rs.getString("location_name")+";"+
                        rs.getString("location_address")+";"+
                        rs.getString("location_phone")+";"+
                        rs.getString("location_email")+";"+
                        rs.getString("number_of_staff");
                locations.add(result);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //System.out.println(locations);
        return locations;
    }

    /**
     *  This method is used to get Min and Max date.
     *  <b>Row structure:</b> 
     *      [Minimum Date]; [Maximum Date]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getMinMaxDate()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> dates= new ArrayList<String>();
        try
        {
            String query = "SELECT MIN(purchase_date), MAX(purchase_date) FROM "+DBNAME+".dbo.CARO_asset";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                dates.add(rs.getString(1));
                //System.out.println(rs.getString(1));
                dates.add(rs.getString(2));
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return dates;
    }
    
    /**
     *  This method is used to get Min and Max Cost.
     *  <b>Row structure:</b> 
     *      [Minimum Cost]; [Maximum Cost]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<Double> getMinMaxCost()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<Double> costs= new ArrayList<Double>();
        try
        {
            String query = "SELECT MIN(cost), MAX(cost) FROM "+DBNAME+".dbo.CARO_asset";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                costs.add(rs.getDouble(1));
                //System.out.println(rs.getDouble(2));
                costs.add(rs.getDouble(2));
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return costs;
    }
    
    /**
     *  This method is used to get information of all Users.
     *  <b>Row structure:</b> 
     *      [Personnel ID]; [Username];
     *      [First name]; [Last name];
     *      [Email]
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getUsers()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> users= new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+DBNAME+".dbo.CARO_system_user";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String result;
                result = rs.getString("personel_id")+";"+
                        rs.getString("user_name")+";"+
                        rs.getString("user_first_name")+";"+
                        rs.getString("user_last_name")+";"+
                        rs.getString("user_email");
                users.add(result);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //System.out.println(users);
        return users;
    }

    /**
     *  This method is used to get information of all Active Users.
     *  Get 
     *  @return ArrayList of String This returns a collection of the rows in an arraylist.
     */
    public ArrayList<String> getActiveUsers()
    {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> users = new ArrayList<String>();
        try
        {
            String query = "SELECT * FROM "+DBNAME+".dbo.CARO_system_user WHERE isActive = 1 ;";
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
     * Get asset information for a selector list
     * The information is returned as ArrayList.
     * @return ArrayList  containing all asset information
     */
    public ArrayList<String> getAssetsSelectorList() {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> assets = new ArrayList<String>();
        try {
            String query = "SELECT * "
                    + "FROM " + DBNAME + ".dbo.CARO_asset "
                    + "where asset_id is NOT NULL";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String result;
                result = rs.getString("asset_id") + ";"
                        + rs.getString("asset_name");
                assets.add(result);
            }
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assets;
    }    
    
    
    /**
     *  This method is used to attempt to add Asset into the database.
     *  <b>Parameter String[] structure:</b> 
     *      [Asset ID], [Asset Description],
     *      [Serial #], [Asset Category],
     *      [Cost], [Brand ID],
     *      [Purchase Date]
     *  @param params String[] parameters for adding an asset.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean addAsset(String[] params) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "INSERT INTO " + DBNAME + ".dbo.CARO_asset "
                    + "(asset_id,asset_name,asset_desc,photo_link,asset_category,purchase_date,"
                    + "cost,location_id,added_by,dimension, IP, MAC, serial_number, hostname, "
                    + "service_tag, comment_on_asset, brand_id, vendor_id, OS, depreciated_value, deprec_info_id ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?,?);";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[0]);
            st.setString(2, params[1]);
            st.setString(3, params[2]);
            st.setString(4, params[3]);
            st.setString(5, params[4]);
            st.setString(6, params[5]);
            st.setDouble(7, Double.parseDouble(params[6]));
            st.setString(8, params[7]);
            st.setInt(9, Integer.parseInt(params[8]));
            st.setString(10, params[9]);
            st.setString(11, params[10]);
            st.setString(12, params[11]);
            st.setString(13, params[12]);
            st.setString(14, params[13]);
            st.setString(15, params[14]);
            st.setString(16, params[15]);
            st.setString(17, params[16]);
            st.setString(18, params[17]);
            st.setString(19, (params[18]==null?null:(params[18].equals("")?"test":params[18])));
            st.setString(20, params[19]);
            st.setString(21, params[20]);
            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process the input parameter and insert new entry to asset_sold_archive table by connection with database and performing SQL
     * @param archiveParams the parameters required for INSERT TO asset_sold_archive table
     * @return true if entry is successfully added, else return false
     */
    public boolean addToArchive(ArrayList<String> archiveParams) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "INSERT INTO " + DBNAME + ".dbo.CARO_asset_sold_archive "
                    + "(asset_id, purchase_date ,cost ,asset_name,asset_desc ,comment_on_asset ,"
                    + "asset_category,sold_price ,date_of_archive ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE());";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, archiveParams.remove(0));
            st.setString(2, archiveParams.remove(0));
            st.setDouble(3, Double.parseDouble(archiveParams.remove(0)));
            st.setString(4, archiveParams.remove(0));
            st.setString(5, archiveParams.remove(0));
            st.setString(6, archiveParams.remove(0));
            st.setString(7, archiveParams.remove(0));
            st.setString(8, archiveParams.remove(0));
            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    
    /**
     *  This method is used to attempt to assign to Assigned To.
     *  <b>Parameter String[] structure:</b> 
     *      [Personnel ID], [Asset ID]
     *  @param params String[] parameters for adding an asset.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean addAssigned_to(String[] params) {
        if(params[0].equals("No one")){
            return false;
        }
        
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "INSERT INTO " + DBNAME + ".dbo.CARO_assigned_to "
                    + "VALUES (?, ?);";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[0]);
            st.setString(2, params[1]);

            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }    
    
    /**
     *  This method is used to attempt to delete an Asset.
     *  @param assetID String ID for asset that will be deleted.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean deleteAsset(String assetID) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "DELETE FROM " + DBNAME + ".dbo.CARO_asset "
                    + "WHERE asset_id=?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, assetID);

            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * DELETE the entry in assigned_to table base on parameters. It connects to database and performs SQL.
     * @param assigned_to_personel_id the user id of the entry being deleted
     * @param assigned_to_asset_id the asset id of the entry being deleted
     * @return true if deleted success, else false
     */
    public boolean deleteAssigned_to(String assigned_to_personel_id, String assigned_to_asset_id) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "DELETE FROM " + DBNAME + ".dbo.CARO_assigned_to "
                    + "WHERE personel_id = ? AND asset_id = ? ;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, Integer.parseInt(assigned_to_personel_id));
            st.setString(2, assigned_to_asset_id);
            
            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *  This method is used to attempt to edit an Asset in the database.
     *  <b>Parameter String[] structure:</b> 
     *      []; []
     *  @param params String[] parameters for editing an Asset.
     *  @return boolean Returns <b>true</b> if add operation is success, <b>false</b> adding is failed
     */
    public boolean editAsset(String[] params) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        System.out.println(params[19]+params[20]+params[0]);
        
        
        try {
            String query = "UPDATE " + DBNAME + ".dbo.CARO_asset "
                    + "SET asset_name = ?,asset_desc = ?,photo_link = ?,"
                    + "asset_category = ?,purchase_date = ?,"
                    + "cost = ?, location_id = ?," //added by cannot change
                    + "dimension =?, IP = ?, MAC = ?, serial_number = ?, hostname = ?, "
                    + "service_tag = ?, comment_on_asset = ?, brand_id = ?, vendor_id = ?,"
                    + "depreciated_value = ? WHERE asset_id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[1]);
            st.setString(2, params[2]);
            st.setString(3, params[3]);
            st.setString(4, params[4]);
            st.setString(5, params[5]);
            st.setDouble(6, Double.parseDouble(params[6]));
            st.setString(7, params[7]);
            //st.setInt(8, Integer.parseInt(params[8]));
            st.setString(8, params[9]);
            st.setString(9, params[10]);
            st.setString(10, params[11]);
            st.setString(11, params[12]);
            st.setString(12, params[13]);
            st.setString(13, params[14]);
            st.setString(14, params[15]);
            st.setString(15, params[16]);
            st.setString(16, params[17]);
            st.setDouble(17, Double.parseDouble(params[18]));
            st.setString(18, params[0]);

            result = st.executeUpdate();
           
            if (params[20].equals("null")){
                String[] paramsInput = {params[19],params[0]};
                addAssigned_to(paramsInput);
            }
            else{
                if (params[19].equals("unchanged")){
                    System.out.println("assigned user unchanged.");
                }
                else if (params[19].equals("remove")){
                    System.out.println("assigned user removed.");
                    deleteAssigned_to(params[20],params[0]);
                }else{
                    System.out.println("assigned user edited.");
                    editAssigned_to(params[19],params[20],params[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Edit the assigned__to table, change the user that is assigned to the given asset, by connecting with database and performing SQL 
     * @param NEW_assigned_to_personel_id the unique id of the new user that is assigned to the asset
     * @param OLD_assigned_to_personel_id the original id of the user that WAS assigned to the asset
     * @param assigned_to_asset_id the unique id of the asset
     * @return true if the user assigned to the asset is edited, else return false
     */
    public boolean editAssigned_to(String NEW_assigned_to_personel_id, String OLD_assigned_to_personel_id, String assigned_to_asset_id) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "UPDATE " + DBNAME + ".dbo.CARO_assigned_to "
                    + "SET personel_id = ? "
                    + "WHERE personel_id = ? AND asset_id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, Integer.parseInt(NEW_assigned_to_personel_id));
            st.setInt(2, Integer.parseInt(OLD_assigned_to_personel_id));
            st.setString(3, assigned_to_asset_id);

            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            System.out.print("No change in assigned to");
            return false;
        }
    }
 
    /**
     * Edit the depreciated value of the given asset and update the table, it is done by connecting the database and performing SQL
     * @param asset_id the unique id of the asset
     * @param deprec_info_id the unique id of the applied depreciation formula
     * @return true the depreciation value of the asset is edited, else return false
     */
    public boolean editDeprec_value(String asset_id, String deprec_info_id) {
        double deprecVal = Report.calcDepreciation(asset_id, deprec_info_id);
        
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "UPDATE " + DBNAME + ".dbo.CARO_asset "
                    + "SET depreciated_value = ? "
                    + "WHERE asset_id = ? AND deprec_info_id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setDouble(1, deprecVal);
            st.setString(2, asset_id);
            st.setString(3, deprec_info_id);

            result = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            System.out.print("No change in assigned to");
            return false;
        }
    }

}
