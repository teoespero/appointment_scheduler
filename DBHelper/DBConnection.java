package teoespero.jappointment.DBHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>The <b>DBConnection Class</b> provides the JAppointment Application the mechanism to connect to the MySQL
 * Database.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05272023
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcURL = protocol + vendorName + ipAddress + databaseName;
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;
    private static final String username = "sqlUser"; // Username
    private static final String password = "Passw0rd!"; // Password

    /**
     * <p>The <b>startConnection Method</b> initiates the connection to the database.</p>
     * @return TRUE or FALSE if the connection is successful.
     * @throws ClassNotFoundException <p>The error thrown if an issue is encountered.</p>
     */
    public static Connection startConnection() throws ClassNotFoundException {
        System.out.println("Connecting to the database...");
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

}
