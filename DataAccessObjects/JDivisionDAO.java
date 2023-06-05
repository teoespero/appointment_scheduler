package teoespero.jappointment.DataAccessObjects;

import teoespero.jappointment.Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teoespero.jappointment.Main;
import teoespero.jappointment.DBHelper.DBSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>The <b>JDivisionDAO Class</b> provides the logic that interfaces with with database and tables (Division).
 *  This acts as the back-end of the JAppointment application.</p>
 *  @author Teodulfo Espero (BS Software Deveopment, WGU)
 *  @since 01.05302023
 */
public class JDivisionDAO {

    private final static Connection conn = Main.mysqlConn;

    /**
     * <p>The <b>JDivisionSelectByID Method</b> returns the Division name using the Division ID as the filter in
     * the WHERE clause.</p>
     * @param dbID <p>The Division ID</p>
     * @return <p>The Division record that matches the Division ID.</p>
     * @throws SQLException <p>The error thrown by the JDivisionDAO Class (JDivisionSelectByID Method).</p>
     */
    public static Division JDivisionSelectByID(int dbID) throws SQLException {

        Division division = new Division();

        try {
            String sqlStatement = "select * " +
                    "from first_level_divisions " +
                    "where " +
                    "   Division_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();

            division.setDivisionID(rs.getInt("Division_ID"));
            division.setDivisionName(rs.getString("Division"));
            division.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
            division.setCreatedBy(rs.getString("Created_By"));
            division.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
            division.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            division.setCountryID(rs.getInt("COUNTRY_ID"));
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return division;
    }

    /**
     * <p>The <b>JDivisionSelectAll Method</b> returns all of the Division records in the database.</p>
     * @return <p>All the Division records.</p>
     * @throws SQLException <p>The error thrown by the JDivisionDAO Class (JDivisionSelectAll Method).</p>
     */
    public static ObservableList<Division> JDivisionSelectAll() throws SQLException {

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        try{
            String sqlStatement = "select * " +
                    "from first_level_divisions";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {

                Division division = new Division();

                division.setDivisionID(rs.getInt("Division_ID"));
                division.setDivisionName(rs.getString("Division"));
                division.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                division.setCreatedBy(rs.getString("Created_By"));
                division.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                division.setLastUpdatedBy(rs.getString("Last_Updated_By"));
                division.setCountryID(rs.getInt("COUNTRY_ID"));

                divisions.add(division);
            }

        }
        catch(SQLException e){
            errorDBConn(e.getMessage());
        }

        return divisions;
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

    public static void create(Division division) throws SQLException {

        try {
            String sqlStatement = "insert into " +
                    "first_level_divisions(" +
                    "   Division, Create_Date, " +
                    "   Created_By, " +
                    "   Last_Update, " +
                    "   Last_Updated_By, " +
                    "   COUNTRY_ID" +
                    ") " +
                    "values(" +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?, " +
                    "   ?" +
                    ")";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, division.getDivisionName());
            ps.setString(2, division.getCreatedBy());
            ps.setString(3, division.getLastUpdatedBy());
            ps.setInt(4, division.getCountryID());

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    public static void update(Division division) throws SQLException {

        try{
            String sqlStatement = "update first_level_divisions " +
                    "set " +
                    "   Division = ?, " +
                    "   Last_Update = NOW(), " +
                    "   Last_Updated_By = ?, " +
                    "   COUNTRY_ID = ? " +
                    "where " +
                    "   Division_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, division.getDivisionName());
            ps.setString(2, division.getLastUpdatedBy());
            ps.setInt(3, division.getCountryID());
            ps.setInt(4, division.getDivisionID());

            ps.execute();
        }
        catch(SQLException e){
            errorDBConn(e.getMessage());
        }
    }

    public static void deleteByID(int dbID) throws SQLException {

        try{
            String sqlStatement = "delete from first_level_divisions " +
                    "where " +
                    "   Division_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }
}
