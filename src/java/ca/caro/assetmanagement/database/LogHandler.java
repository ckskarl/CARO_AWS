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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <b>Log Handler Class</b>
 * <p>Class that handles interaction of the web application to the MS Server. 
 * Contains methods for handling the Log Related tables with operations such as adding, editing, and deleting log
 * as well as methods for getting log info.</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
public class LogHandler {
    private static final String USERID_SESSIONPARAMNAME = "userId";
    
    /**
     *  This method is used to record a log.
     *  @param request HttpServletRequest request
     *  @param mssg String Message beside the log.
     */
    public static void recordLog(HttpServletRequest request, String mssg)
    {
        HttpSession session = request.getSession();
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "INSERT INTO "+MSSQLConnection.getDBName()+".dbo.CARO_activity_log "
                    + "(personel_id, time_stamp, description) "
                    + "VALUES (?, GETDATE(), ?);";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, Integer.parseInt(session.getAttribute(USERID_SESSIONPARAMNAME).toString()));
            st.setString(2, mssg);
            st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try{conn.close();} catch(Exception e){e.printStackTrace();}
    }
    
    /**
     *  This method is used to record a log.
     *  @param mssg String Message beside the log.
     */
    public static void recordLog(String mssg)
    {
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            String query = "INSERT INTO "+MSSQLConnection.getDBName()+".dbo.CARO_activity_log "
                    + "(time_stamp, description) "
                    + "VALUES (GETDATE(), ?);";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, mssg);
            st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try{conn.close();} catch(Exception e){e.printStackTrace();}
    }
}
