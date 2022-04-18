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
import java.sql.ResultSetMetaData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>Report Handler Class</b>
 * <p>Class that handles the SQL related to request from Report page. Methods includes SELECT from database base on criteria sent to it.</p>
 * 
 * @author Kin Shing Chong
 * @version 1.0
 */
public class ReportHandler {
    private static MSSQLConnection connention = new MSSQLConnection();
    private static final String DBNAME = connention.getDBName();
    /**
     * It connects to database and performs SQL to select all asset, then parse the result into one String in CSV pattern 
     * @return String of result of all asset details in CSV pattern
     */
    public String getTestAssetsInCSV() {
        Connection conn = connention.getConnection();
        String resultInCSV = "";
        try {
            String query = "SELECT * FROM " + DBNAME + ".dbo.CARO_asset";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String allColName = "";
            for (int i = 1; i <= count; i++) {
                if (i == count) {
                    allColName += rsmd.getColumnName(i) +"\\r\\n";
                } else {
                    allColName += rsmd.getColumnName(i) + ",";
                }
            }
            resultInCSV+=allColName;
            while (rs.next()) {
                String eachRowData = "";
                for (int i = 1; i <= count; i++) {
                    if (i == count) {
                        eachRowData += rs.getString(i) +"\\r\\n";
                    } else {
                        eachRowData += rs.getString(i) + ",";
                    }
                }
                resultInCSV+=eachRowData;
            }
            System.out.println(resultInCSV);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultInCSV;
    }
    


    /**
     * It connects to database and performs SQL to select all asset with great details by joining multiple table,
     * then parse the result into one String in CSV pattern 
     * @return String of result of all asset with greater details in CSV pattern
     */    
    public String getFullAssetsInCSV() {
        //Connection conn = MSSQLConnection.getConnection();
        Connection conn = connention.getConnection();
        String resultInCSV = "";
        try {
            String query = "SELECT "
                    + "su.personel_id \"added_by_id\", a_t.personel_id \"assigned_to_id\", a.asset_id,* "
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
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            int rowCount=0;
            String allColName = "";
            for (int i = 1; i <= count; i++) {
                if (i == count) {
                    allColName += rsmd.getColumnName(i) +"\\r\\n";
                } else {
                    allColName += rsmd.getColumnName(i) + ",";
                }
            }
            //System.out.println(allColName);
            resultInCSV+=allColName;
            //assets.add(allColName);
            while (rs.next()) {
                rowCount++;
                String eachRowData = "";
                for (int i = 1; i <= count; i++) {
                    if (i == count) {
                        eachRowData += rs.getString(i) +"\\r\\n";
                    } else {
                        eachRowData += rs.getString(i) + ",";
                    }
                }
                //System.out.println(eachRowData);
                resultInCSV+=eachRowData;
            }
            System.out.println(resultInCSV);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultInCSV;
    }

    /**
     * It connects to database and performs SQL to select asset with specific details bases on input by joining multiple table,
     * then parse the result into one String in CSV pattern. 
     * @param selectClause the "SELECT" clause in SQL, it controls the number of columns being executed in SQL statement
     * @param whereClause the "WHERE" clause in SQL, it controls the condition executed in SQL statement.
     * @param request the HTTP request allowing the method to set attributes in requestScope
     * @param response the HTTP response
     * @return String of result of asset with specific details in CSV pattern
     */
    public String getFullAssetsDynamicInCSV(StringBuffer selectClause, StringBuffer whereClause, HttpServletRequest request, HttpServletResponse response) {
        //Connection conn = MSSQLConnection.getConnection();
        Connection conn = connention.getConnection();
        String resultInCSV = "";
        int rowCount =0;
        try {
            String query = selectClause.toString()
                    + "FROM " + DBNAME + ".dbo.CARO_asset a "
                    + "full join " + DBNAME + ".dbo.CARO_asset_category ac ON (a.asset_category=ac.asset_category)"
                    + "full join " + DBNAME + ".dbo.CARO_vendor v ON (a.vendor_id=v.vendor_id)"
                    + "full join " + DBNAME + ".dbo.CARO_system_user su ON (a.added_by=su.personel_id)"
                    + "full join " + DBNAME + ".dbo.CARO_brand b ON (a.brand_id=b.brand_id)"
                    + "full join " + DBNAME + ".dbo.CARO_location l ON (a.location_id=l.location_id) "
                    + "full join " + DBNAME + ".dbo.CARO_assigned_to a_t ON (a_t.asset_id = a.asset_id)"
                    + "full join " + DBNAME + ".dbo.CARO_system_user su2 ON (a_t.personel_id = su2.personel_id)"
                    + "full join " + DBNAME + ".dbo.CARO_depreciation_information di ON (a.deprec_info_id = di.deprec_info_id)"
                    + whereClause.toString();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            
            //System.out.println(count);
            String allColName = "";
            for (int i = 1; i <= count; i++) {
                if (i == count) {
                    allColName += rsmd.getColumnName(i) +"\\r\\n";
                } else {
                    allColName += rsmd.getColumnName(i) + ",";
                }
            }
            //System.out.println(allColName);
            resultInCSV+=allColName;
            //assets.add(allColName);
            while (rs.next()) {
                rowCount++;
                String eachRowData = "";
                for (int i = 1; i <= count; i++) {
                    if (i == count) {
                        eachRowData += (rs.getString(i)==null?"No Data":rs.getString(i)) +"\\r\\n";
                    } else {
                        eachRowData += (rs.getString(i)==null?"No Data":rs.getString(i)) + ",";
                    }
                }
                //System.out.println(eachRowData);
                resultInCSV+=eachRowData;
            }
            System.out.println(rowCount);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("rowCount","There are "+rowCount+" asset fit the criteria.");
        return resultInCSV;
    }

    
    /**
     * It connects to database and performs SQL to select all locations, then parse the result into one String in CSV pattern 
     * @return String of result of all location details in CSV pattern
     */
    public String getLocationReport() {
        //Connection conn = MSSQLConnection.getConnection();
        Connection conn = connention.getConnection();
        String resultInCSV = "";
        try {
            String query = "SELECT * FROM " + DBNAME + ".dbo.CARO_location l ";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String allColName = "";
            for (int i = 1; i <= count; i++) {
                if (i == count) {
                    allColName += rsmd.getColumnName(i) +"\\r\\n";
                } else {
                    allColName += rsmd.getColumnName(i) + ",";
                }
            }
            resultInCSV+=allColName;
            while (rs.next()) {
                String eachRowData = "";
                for (int i = 1; i <= count; i++) {
                    if (i == count) {
                        eachRowData += rs.getString(i) +"\\r\\n";
                    } else {
                        eachRowData += rs.getString(i) + ",";
                    }
                }
                resultInCSV+=eachRowData;
            }
            //System.out.println(resultInCSV);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultInCSV;
    }


    /**
     * It connects to database and performs SQL to select all brands, then parse the result into one String in CSV pattern 
     * @return String of result of all brand details in CSV pattern
     */    
    public String getBrandReport() {
        //Connection conn = MSSQLConnection.getConnection();
        Connection conn = connention.getConnection();
        String resultInCSV = "";
        try {
            String query = "SELECT * FROM " + DBNAME + ".dbo.CARO_brand b ";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String allColName = "";
            for (int i = 1; i <= count; i++) {
                if (i == count) {
                    allColName += rsmd.getColumnName(i) +"\\r\\n";
                } else {
                    allColName += rsmd.getColumnName(i) + ",";
                }
            }
            resultInCSV+=allColName;
            while (rs.next()) {
                String eachRowData = "";
                for (int i = 1; i <= count; i++) {
                    if (i == count) {
                        eachRowData += rs.getString(i) +"\\r\\n";
                    } else {
                        eachRowData += rs.getString(i) + ",";
                    }
                }
                resultInCSV+=eachRowData;
            }
            System.out.println(resultInCSV);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultInCSV;
    }


    /**
     * It connects to database and performs SQL to select all vendors, then parse the result into one String in CSV pattern 
     * @return String of result of all vendor details in CSV pattern
     */        
    public String getVendorReport() {
        //Connection conn = MSSQLConnection.getConnection();
        
        Connection conn = connention.getConnection();
        String resultInCSV = "";
        try {
            String query = "SELECT * FROM " + DBNAME + ".dbo.CARO_vendor v ";

            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String allColName = "";
            for (int i = 1; i <= count; i++) {
                if (i == count) {
                    allColName += rsmd.getColumnName(i) +"\\r\\n";
                } else {
                    allColName += rsmd.getColumnName(i) + ",";
                }
            }
            resultInCSV+=allColName;
            while (rs.next()) {
                String eachRowData = "";
                for (int i = 1; i <= count; i++) {
                    if (i == count) {
                        eachRowData += rs.getString(i) +"\\r\\n";
                    } else {
                        eachRowData += rs.getString(i) + ",";
                    }
                }
                resultInCSV+=eachRowData;
            }
            System.out.println(resultInCSV);
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultInCSV;
    }
    




}
