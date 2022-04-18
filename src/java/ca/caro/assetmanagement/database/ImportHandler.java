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

import ca.caro.assetmanagement.entity.Asset_Entity_From_Import;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * <b>Import Handler Class</b>
 * <p>Class that handles interaction between database and the imported spreadsheet data. It involves processing the parsed json string to Asset object,
 * and perform SQL base on the object attributes. 
 * </p>
 * 
 * @author Kin Shing Chong
 * @author Nagi Nabal
 * @version 1.0
 */
public class ImportHandler {
    static AssetHandler assetH = new AssetHandler();
    
    /**
     * Convert the string that is in json format into Asset_Entity_From_Import object. 
     * the object is processed outside of the method, and the result is passed to method in Asset Handler class to perform SQL.
     * the SQL would INSERT new entry in asset table.
     * The method will return the total number of entry inserted to the asset table.
     * @param json_string the string in json format
     * @return the total number of entry inserted to the asset table.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied,
     */
    public static int json_to_object(String json_string) throws InterruptedException{
            int num_of_import = 0;
            Gson gson = new Gson();

            String sample_json_array = json_string;
            Asset_Entity_From_Import [] assetArray = gson.fromJson(sample_json_array, Asset_Entity_From_Import[].class); 
            for (Asset_Entity_From_Import asset : assetArray )    {
               String asset_id="";
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ssSSS");  
                    LocalDateTime now = LocalDateTime.now();  
                    asset_id = "IM" + dtf.format(now);
                    TimeUnit.MILLISECONDS.sleep(7); 
                asset.setAsset_id(asset_id);
               if (assetH.addAsset(setAssetParams(asset))){
                   num_of_import++;
               }
               if (asset.getAssigned_to_email()!=null){
                   String[] assetAssignedToParams;
                   assetAssignedToParams = new String[2];
                   assetAssignedToParams[0] = getAssigned_to(asset.getAssigned_to_email());
                   assetAssignedToParams[1] = asset.getAsset_id();
                   assetH.addAssigned_to(assetAssignedToParams);
               }
            }
            return num_of_import;
    }
    
    /**
     * Read from an Asset_Entity_From_Import object and extract information from it, 
     * the information together with extra parameters is returned as an array of String, 
     * each item in the array contains one of the details of the asset
     * @param asset asset
     * @return array of String containing necessarily information for INSERT into asset table
     */
    private static String[] setAssetParams(Asset_Entity_From_Import asset){
        String[] assetParams;
        assetParams = new String[21];
        assetParams[0] = asset.getAsset_id(); 
        assetParams[1] = asset.getAsset_name();
        assetParams[2] = asset.getAsset_desc();
        assetParams[3] = null;
        assetParams[4] = getAssetCategory(asset.getAsset_category_description());
        assetParams[5] = asset.getPurchase_date();
        assetParams[6] = String.valueOf(asset.getCost());
        assetParams[7] = getLocation(asset.getLocation_name());
        assetParams[8] = getAdded_by(asset.getAdded_by_email());
        assetParams[9] = null;
        assetParams[10] = null;
        assetParams[11] = asset.getMAC();
        assetParams[12] = asset.getSerial_number();
        assetParams[13] = asset.getHostname();
        assetParams[14] = asset.getService_tag();
        assetParams[15] = asset.getComment_on_asset();
        assetParams[16] = getAssetBrand(asset.getBrand_name());
        assetParams[17] = getAssetVendor(asset.getVendor_name());
        assetParams[18] = asset.getOs();
        assetParams[19] = null;
        assetParams[20] = null;
        return assetParams;
    }
    
    /**
     * Get the Brand id base on given brand name, the value is retrieved by connecting to database and performing SQL
     * @param brand_name the name of brand which the method used to look for brand id
     * @return the brand id.
     */
    private static String getAssetBrand(String brand_name) {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "SELECT brand_id FROM "+MSSQLConnection.getDBName()+".dbo.CARO_brand WHERE brand_name = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, brand_name);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return rs.getString(1);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get the Vendor id base on given vendor name, the value is retrieved by connecting to database and performing SQL
     * @param vendor_name the name of vendor which the method used to look for vendor id
     * @return the vendor id.
     */
    private static String getAssetVendor(String vendor_name) {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "SELECT vendor_id FROM "+MSSQLConnection.getDBName()+".dbo.CARO_vendor WHERE vendor_name = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, vendor_name);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return rs.getString(1);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get the asset category identifier base on given Asset Category description, the value is retrieved by connecting to database and performing SQL
     * @param asset_category_description the description of the category which the method used to look for asset category identifier
     * @return the asset category identifier.
     */
    private static String getAssetCategory(String asset_category_description) {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "SELECT asset_category FROM "+MSSQLConnection.getDBName()+".dbo.CARO_asset_category WHERE asset_category_description = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, asset_category_description);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return rs.getString(1);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get the location id base on given location name, the value is retrieved by connecting to database and performing SQL
     * @param location_name the location name which the method used to look for location id
     * @return the location id.
     */
    private static String getLocation(String location_name) {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "SELECT location_id  FROM "+MSSQLConnection.getDBName()+".dbo.CARO_location WHERE location_name = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, location_name);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return rs.getString(1);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
        
    /**
     * Get the personel id base on given user email, the value is retrieved by connecting to database and performing SQL
     * @param added_by_email the email of the user who added the asset data.
     * @return personel id
     */
    private static String getAdded_by(String added_by_email) {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "SELECT personel_id  FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user WHERE user_email = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, added_by_email);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return rs.getString(1);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }    
    
    /**
     * Get the personel id base on given user email, the value is retrieved by connecting to database and performing SQL
     * @param assigned_to_email the email of the user who is assigned to the asset
     * @return personel id
     */
    private static String getAssigned_to(String assigned_to_email) {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "SELECT personel_id  FROM "+MSSQLConnection.getDBName()+".dbo.CARO_system_user WHERE user_email = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, assigned_to_email);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return rs.getString(1);
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }       
    
}
