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
package ca.caro.assetmanagement.systemadministration;

import ca.caro.assetmanagement.database.MSSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * <b>RestoreClass</b>
 * <p>Class that handles functions for restore of database.
 * 
 * </p>
 * 
 * @author Brandon Donkersloot
 * @version 1.0
 */
public class Restore {

    /**
     * Method to perform database restore, it will connect to the database and execute SQL to perform RESTORE.
     * @param restoreName name of restore file
     * @return true if backup success, else false
     */
    public static boolean Restoredb(String restoreName) {

        //***IMPORTANT*** -ServerInstance is the Server name: that is displayed when you login to SQL server management studio 
        //*** Important *** -Change the options in the database for this to work (Server management studio) - right click the database, choose properties -> option, change recovery model to simmple
        
       
        //RESTORE DATABASE CAROAssetManagement FROM DISK = 'C:\Program Files\Microsoft SQL Server\MSSQL15.MSSQLSERVER\MSSQL\Backup\back.bak' WITH REPLACE
        
        
        Connection conn = MSSQLConnection.getConnectionMaster();
        try {
            
            String query =  
                    "RESTORE DATABASE CAROAssetManagement FROM DISK = '" + restoreName + "' WITH REPLACE"
                    ;
            
            PreparedStatement st = conn.prepareStatement(query);

            st.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }
}
