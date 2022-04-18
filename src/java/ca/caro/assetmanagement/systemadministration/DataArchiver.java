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
 * <b>Data Archiver class</b>
 * <p>Class that handles functions for data archiving, 
 * the asset that is sold and no longer in the company will be archived.
 * 
 * </p>
 * 
 * @author Kin Shing Chong
 * @version 1.0
 */
public class DataArchiver {
    
    /**
     * This will look for the database and delete data in asset_sold_archive table that are older than 5 year.
     * The method will connect to database and execute SQL DELETE statement.
     * @return true if delete is success, else false
     */
    public static boolean deleteOldArchive() {
        Connection conn = MSSQLConnection.getConnection();
        int result = 0;
        try {
            String query = "DELETE FROM " + MSSQLConnection.getDBName() + ".dbo.CARO_asset_sold_archive "
                    + "WHERE date_of_archive < GETDATE() - 1825;";
            PreparedStatement st = conn.prepareStatement(query);
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

