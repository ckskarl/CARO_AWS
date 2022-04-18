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
 * <b>AssetCategoryHandler Class</b>
 * <p>
 * Class that handles Asset Category database manipulation in MS SQL Server.
 * Contains methods for handling basic Asset Category Management.</p>
 *
 * @author Kin Shing Chong
 * @version 1.0
 */
public class AssetCategoryHandler {
    
    /**
     * Get the information of asset category by connecting to database and perform SQL, and return as arraylist.
     * @return a collection of the rows of asset category information in an arraylist.
     */
    public ArrayList<String> getAssetCategories_with_count() {
        Connection conn = MSSQLConnection.getConnection();
        ArrayList<String> assets = new ArrayList<String>();
        try {
            //String query = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_asset_category";
            String query = "SELECT ac.asset_category \"asset_category\",ac.asset_category_description \"asset_category_description\",COUNT(a.asset_category) \"count\""
                    + "FROM " + MSSQLConnection.getDBName() + ".[dbo].[CARO_asset] a "
                    + "RIGHT JOIN " + MSSQLConnection.getDBName() + ".[dbo].[CARO_asset_category] ac "
                    + "ON (a.asset_category=ac.asset_category) GROUP BY ac.asset_category,ac.asset_category_description;";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String result;
                result = rs.getString("asset_category") + ";"
                        + rs.getString("asset_category_description") + ";"
                        + rs.getInt("count");
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
     * Get the number of unique asset category by connecting to database and perform SQL 
     * @return the number of unique asset category
     */
    public int getNumOfCategory() {
        Connection conn = MSSQLConnection.getConnection();
        int count = 0;
        try {
            //String query = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_asset_category";
            String query = "SELECT COUNT(*) FROM " + MSSQLConnection.getDBName() + ".[dbo].[CARO_asset_category] ac ";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                count = rs.getInt(1);
            }

            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Insert new entry to asset category table by connecting to database and perform SQL 
     * @param params the parameter of new asset category
     * @return true if asset is added to database successfully, else false
     */
    public boolean addAssetCategory(String[] params) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "INSERT INTO " + MSSQLConnection.getDBName() + ".dbo.CARO_asset_category VALUES (?, ?)";
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
     * Delete an asset category base on input ID, by connecting with database and perform SQL 
     * @param assetCadID the asset_category_id
     * @return true if delete success, else false
     */
    public boolean deleteAssetCad(String assetCadID) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "DELETE FROM " + MSSQLConnection.getDBName() + ".dbo.CARO_asset_category "
                    + "WHERE asset_category=?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, assetCadID);

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
     * Edit details of an existing asset category by connecting with database and perform SQL 
     * @param params  the new parameter that is being edited
     * @return true if edit success, else false
     */
    public boolean editAssetCad(String[] params) {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "UPDATE " + MSSQLConnection.getDBName() + ".dbo.CARO_asset_category "
                    + "SET asset_category = ?, asset_category_description = ? WHERE asset_category = ?;";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, params[2]);
            st.setString(2, params[1]);
            st.setString(3, params[0]);

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

}
