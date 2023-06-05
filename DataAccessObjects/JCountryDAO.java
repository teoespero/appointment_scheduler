package teoespero.jappointment.DataAccessObjects;

import teoespero.jappointment.Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teoespero.jappointment.Main;
import teoespero.jappointment.DBHelper.DBSQL;

import java.sql.*;

/**
 * <p>The <b>JCountryDAO Class</b> provides the logic that interfaces with with database and tables (Country).
 * This acts as the back-end of the JAppointment application.</p>
 * @author Teodulfo Espero (BS Software Deveopment, WGU)
 * @since 01.05302023
 */
public class JCountryDAO {

    private final static Connection conn = Main.mysqlConn;


    /**
     * <p>The <b>jCountrySelectByID Method</b> returns the country name using the Country ID as the filter in
     * the WHERE clause.</p>
     * @param dbID <p>The Country ID</p>
     * @return <p>The Country record that matches the Country ID.</p>
     * @throws SQLException <p>The error thrown by the JCountryDAO Class (jCountrySelectByID Method).</p>
     */
    public static Country jCountrySelectByID(int dbID) throws SQLException {

        Country country = new Country();

        try{
            String sqlStatement = "select * from " +
                    "countries " +
                    "where " +
                    "   Country_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();

            country.setCountryID(rs.getInt("Country_ID"));
            country.setCountryName(rs.getString("Country"));
            country.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
            country.setCreatedBy(rs.getString("Created_By"));
            country.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
            country.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        }
        catch(SQLException e){
            errorDBConn(e.getMessage());
        }

        return country;
    }

    /**
     * <p>The <b>jCountrySelectAll Method</b> returns all of the country records in the database.</p>
     * @return <p>All the country records.</p>
     * @throws SQLException <p>The error thrown by the JCountryDAO Class (jCountrySelectAll Method).</p>
     */
    public static ObservableList<Country> jCountrySelectAll() throws SQLException {

        ObservableList<Country> countries = FXCollections.observableArrayList();

        try{
            String sqlStatement = "select * from " +
                    "countries";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {

                Country country = new Country();

                country.setCountryID(rs.getInt("Country_ID"));
                country.setCountryName(rs.getString("Country"));
                country.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                country.setCreatedBy(rs.getString("Created_By"));
                country.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                country.setLastUpdatedBy(rs.getString("Last_Updated_By"));

                countries.add(country);
            }

        }
        catch(SQLException e){
            errorDBConn(e.getMessage());
        }

        return countries;
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

    /**
     * <p>The <b>jCountryCreate Method (unused)</b> </p>
     * @param country
     * @throws SQLException
     */
    public static void jCountryCreate(Country country) throws SQLException {

        try {
            String sqlStatement = "insert into " +
                    "countries(" +
                    "   Country, " +
                    "   Create_Date, " +
                    "   Created_By, " +
                    "   Last_Update, " +
                    "   Last_Updated_by" +
                    ") " +
                    "values(" +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?" +
                    ")";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, country.getCountryName());
            ps.setString(2, country.getCreatedBy());
            ps.setString(3, country.getLastUpdatedBy());

            ps.execute();
        }
        catch(SQLException e){
            errorDBConn(e.getMessage());
        }
    }

    public static void jCountryUpdate(Country country) throws SQLException {

        try{
            String sqlStatement = "update countries " +
                    "set " +
                    "   Country = ?, " +
                    "   Last_Update = NOW(), " +
                    "   Last_Updated_By = ? " +
                    "where " +
                    "   Country_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, country.getCountryName());
            ps.setString(2, country.getLastUpdatedBy());
            ps.setInt(3, country.getCountryID());

            ps.execute();
        }
        catch(SQLException e){
            errorDBConn(e.getMessage());
        }
    }

    public static void jCountryDeleteByID(int dbID) throws SQLException {

        try{
            String sqlStatement = "delete from countries " +
                    "where " +
                    "   Country_ID = ?";

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