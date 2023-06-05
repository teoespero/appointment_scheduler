package teoespero.jappointment.DataAccessObjects;

import teoespero.jappointment.DBHelper.DBSQL;
import teoespero.jappointment.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teoespero.jappointment.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>The <b>JUserDAO Class</b> provides the logic that interfaces with with database and tables (User).
 *  This acts as the back-end of the JAppointment application.</p>
 *  @author Teodulfo Espero (BS Software Deveopment, WGU)
 *  @since 01.05302023
 */
public class JUserDAO {

    private final static Connection conn = Main.mysqlConn;

    /**
     * <p>The <b>jUserSelectByID Method</b> returns the User name using the User ID as the filter in
     * the WHERE clause.</p>
     * @param dbID <p>The User ID</p>
     * @return <p>The User record that matches the User ID.</p>
     * @throws SQLException <p>The error thrown by the JUserDAO Class (jUserSelectByID Method).</p>
     */
    public static User jUserSelectByID(int dbID) throws SQLException {

        User user = new User();

        try {
            String sqlStatement = "select * " +
                    "from users " +
                    "where " +
                    "   User_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();

            user.setUserID(rs.getInt("User_ID"));
            user.setUserName(rs.getString("User_Name"));
            user.setPassword(rs.getString("Password"));
            user.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
            user.setCreatedBy(rs.getString("Created_By"));
            user.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
            user.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return user;
    }

    /**
     * <p>The <b>jUserSelectByName Method</b> returns the User name using the User Name as the filter in the
     * WHERE clause.</p>
     * @param userName <p>The Username.</p>
     * @return <p>The User record that matches the Username.</p>
     * @throws SQLException <p>The error thrown by the JUserDAO Class (jUserSelectByName Method).</p>
     */
    public static User jUserSelectByName(String userName) throws SQLException {

        User user = new User();

        try {
            String sqlStatement = "select * " +
                    "from users " +
                    "where " +
                    "   User_Name = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, userName);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            if (rs.next()) {

                user.setUserID(rs.getInt("User_ID"));
                user.setUserName(rs.getString("User_Name"));
                user.setPassword(rs.getString("Password"));
                user.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                user.setCreatedBy(rs.getString("Created_By"));
                user.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                user.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return user;
    }

    /**
     * <p>The <b>jUserSelectAll Method</b> returns all of the User records in the database.</p>
     * @return <p>All the User records.</p>
     * @throws SQLException <p>The error thrown by the JUserDAO Class (jUserSelectAll Method).</p>
     */
    public static ObservableList<User> jUserSelectAll() throws SQLException {

        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            String sqlStatement = "select * " +
                    "from users";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                User user = new User();

                user.setUserID(rs.getInt("User_ID"));
                user.setUserName(rs.getString("User_Name"));
                user.setPassword(rs.getString("Password"));
                user.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                user.setCreatedBy(rs.getString("Created_By"));
                user.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                user.setLastUpdatedBy(rs.getString("Last_Updated_By"));

                users.add(user);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return users;
    }

    /**
     * <p>The <b>errorDBConn Method</b> provides a simple mechanism for handling DB connection errors.</p>
     * @param errorMsg <p>The error message returned connecting to the database.</p>
     */
    public static void errorDBConn(String errorMsg){
        System.out.println("DB Connection Error: " + errorMsg);
        System.out.println("JAppointment Application will now shutdown.");
        System.exit(0);
    }

    public static void create(User user) throws SQLException {

        try {
            String sqlStatement = "insert into users(" +
                    "   User_Name, " +
                    "   Password, " +
                    "   Create_Date, " +
                    "   Created_By, " +
                    "   Last_Update, " +
                    "   Last_Updated_By" +
                    ") " +
                    "values(" +
                    "   ?, " +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?" +
                    ")";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, user.getJAppointmentUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getCreatedBy());
            ps.setString(4, user.getLastUpdatedBy());

            ps.execute();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void update(User user) throws SQLException {

        try {
            String sqlStatement = "update users " +
                    "set " +
                    "   User_Name = ?, " +
                    "   Password = ?, " +
                    "   Last_Update = NOW(), " +
                    "   Last_Updated_By = ? " +
                    "where " +
                    "   User_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, user.getJAppointmentUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getLastUpdatedBy());
            ps.setInt(4, user.getJAppointmentUserID());

            ps.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteByID(int dbID) throws SQLException {

        try {
            String sqlStatement = "delete from users " +
                    "where" +
                    "   User_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
