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
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <b>MSSQLConnection Class</b>
 * <p>Class that establishes connection to the MS SQL Database Server</p>
 * 
 * @author Neil Gilbert (761581)
 * @author Brandon Donkersloot
 * @version 1.0
 */
public class MSSQLConnection {
    //private static final String DBNAME="CAROAssetManagement";
    private static final String DBNAME="db_a85ae1_db1";
    private static String connectionString;
    
    /**
     *  This method attempts to connect to the MS SQL Server
     *  @return Connection Returns a connection using a driver
     */
    public static Connection getConnection() {
        Connection conn=null;
       
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Change "databasename" if you named your database differently
            // Change localhost: to correct port number if necessary
            /*connectionString="jdbc:sqlserver://localhost:1433;databasename="+DBNAME+";"
                    + "integratedSecurity=true;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";
            */
            connectionString="jdbc:sqlserver://SQL8001.site4now.net;databasename="+DBNAME+";"
                    //+ "integratedSecurity=true;"
                    //+ "encrypt=true;"
                    + "trustServerCertificate=true;"
                    + "user=db_a85ae1_db1_admin;"
                    + "password=toby0722;";
            
            conn = DriverManager.getConnection(connectionString);
                    
            if(conn!=null)
            {
                //System.out.println("CONNECTION WORKS");
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return conn;
    }
    
    /**
     *  This method attempts to connect to master database in the MS SQL Server, it is being used when doing restore
     *  @return Connection Returns a connection using a driver
     */
    public static Connection getConnectionMaster() {
        Connection conn=null;
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Change "databasename" if you named your database differently
            // Change localhost: to correct port number if necessary
            connectionString="jdbc:sqlserver://localhost:1433;databasename=master;"
                    + "integratedSecurity=true;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";
            
            conn = DriverManager.getConnection(connectionString);
                    
            if(conn!=null)
            {
                System.out.println("CONNECTION WORKS");
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return conn;
    }
    
    /**
     *  This method returns the database name
     *  @return String Returns the Database Name
     */
    public static String getDBName()
    {
        return DBNAME;
    }
}
