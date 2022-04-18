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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * <b>Backup Class</b>
 * <p>Class that handles functions for backup of database.
 * 
 * </p>
 * 
 * @author Brandon Donkersloot
 * @version 1.0
 */
public class Backup {

    /**
     * Method to perform database backup, it will connect to the database and execute SQL to perform BACKUP.
     * @param backupName name of backup file
     * @return true if backup success, else false
     * @throws IOException if file path is illegal
     */
    public static boolean Backupdb(String backupName) throws IOException  {

        //The backup will show up in this location which is the default C:\Program Files\Microsoft SQL Server\MSSQL15.MSSQLSERVER\MSSQL\Backup
        //The easiest way to back up files to a different location is to manually do it. Alternatively you can set the permissions in sql manager for that folder 
        //https://stackoverflow.com/questions/18286765/sql-server-operating-system-error-5-5access-is-denied 
        Connection conn = MSSQLConnection.getConnection();
        if (backupName.endsWith(".bak")) {
            try {
            
            String query =  
                    "BACKUP DATABASE CAROAssetManagement TO DISK = 'C:\\Program Files\\Microsoft SQL Server\\MSSQL15.MSSQLSERVER\\MSSQL\\Backup\\" + backupName + "'"
                    ;
            
            PreparedStatement st = conn.prepareStatement(query);

            st.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            
        }
            
        } else {
            System.out.println("Improper name of backcup");
            
        }
        
        
        
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }
}
