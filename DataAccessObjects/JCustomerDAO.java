package teoespero.jappointment.DataAccessObjects;

import teoespero.jappointment.DBHelper.DBSQL;
import teoespero.jappointment.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teoespero.jappointment.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>The <b>JCustomerDAO Class</b> provides the logic that interfaces with with database and tables (Customer).
 *  This acts as the back-end of the JAppointment application.</p>
 *  @author Teodulfo Espero (BS Software Deveopment, WGU)
 *  @since 01.05302023
 */
public class JCustomerDAO {

    private final static Connection conn = Main.mysqlConn;

    /**
     * <p>The <b>jCustomerCreate Method</b> creates a new customer record in the database.</p>
     * @param customer <p>The Customer object that contains the details on the customer record.</p>
     * @throws SQLException <p>The error thrown by the JCustomerDAO Class (jCustomerCreate Method).</p>
     */
    public static void jCustomerCreate(Customer customer) throws SQLException {

        try {
            String sqlStatement = "insert into customers(" +
                    "   Customer_Name, " +
                    "   Address, " +
                    "   Postal_Code, " +
                    "   Phone, " +
                    "   Create_Date, " +
                    "   Created_By, " +
                    "   Last_Update, " +
                    "   Last_Updated_By, " +
                    "   Division_ID" +
                    ") " +
                    "values(" +
                    "   ?, " +
                    "   ?, " +
                    "   ?, " +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?, " +
                    "   NOW(), " +
                    "   ?, " +
                    "   ?" +
                    ")";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getCreatedBy());
            ps.setString(6, customer.getLastUpdatedBy());
            ps.setInt(7, customer.getDivisionID());

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>jCustomerSelectByID Method</b> returns the Customer name using the Customer ID as the filter in
     * the WHERE clause.</p>
     * @param dbID <p>The Customer ID</p>
     * @return <p>The Customer record that matches the Customer ID.</p>
     * @throws SQLException <p>The error thrown by the JCustomerDAO Class (jCustomerSelectByID Method).</p>
     */
    public static Customer jCustomerSelectByID(int dbID) throws SQLException {

        Customer customer = new Customer();

        try {
            String sqlStatement = "select * from " +
                    "customers " +
                    "where " +
                    "   Customer_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();

            customer.setCustomerID(rs.getInt("Customer_ID"));
            customer.setCustomerName(rs.getString("Customer_Name"));
            customer.setAddress(rs.getString("Address"));
            customer.setPostalCode(rs.getString("Postal_Code"));
            customer.setPhone(rs.getString("Phone"));
            customer.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
            customer.setCreatedBy(rs.getString("Created_By"));
            customer.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
            customer.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            customer.setDivisionID(rs.getInt("Division_ID"));
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return customer;
    }

    /**
     * <p>The <b>jCustomerSelectAll Method</b> returns all of the Customer records in the database.</p>
     * @return <p>All the Customer records.</p>
     * @throws SQLException <p>The error thrown by the JCustomerDAO Class (jCustomerSelectAll Method).</p>
     */
    public static ObservableList<Customer> jCustomerSelectAll() throws SQLException {

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {
            String sqlStatement = "select * " +
                    "from customers";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                Customer customer = new Customer();

                customer.setCustomerID(rs.getInt("Customer_ID"));
                customer.setCustomerName(rs.getString("Customer_Name"));
                customer.setAddress(rs.getString("Address"));
                customer.setPostalCode(rs.getString("Postal_Code"));
                customer.setPhone(rs.getString("Phone"));
                customer.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                customer.setCreatedBy(rs.getString("Created_By"));
                customer.setLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                customer.setLastUpdatedBy(rs.getString("Last_Updated_By"));
                customer.setDivisionID(rs.getInt("Division_ID"));

                customers.add(customer);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return customers;
    }

    /**
     * <p>The <b>jCustomerUpdate Method</b> provides the logic needed to update an Customer using the Customer
     * ID as the parameter</p>
     * @param customer <p>The customer object that defines the new details to replace the
     *                    old customer details.</p>
     * @throws SQLException <p>The error thrown by the JCustomerDAO Class (jCustomerUpdate Method).</p>
     */
    public static void jCustomerUpdate(Customer customer) throws  SQLException {

        try {
            String sqlStatement = "update customers " +
                    "set " +
                    "   Customer_Name = ?, " +
                    "   Address = ?, " +
                    "   Postal_Code = ?, " +
                    "   Phone = ?, " +
                    "   Last_Update = NOW(), " +
                    "   Last_Updated_By = ?, " +
                    "   Division_ID = ? " +
                    "where" +
                    "   Customer_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getLastUpdatedBy());
            ps.setInt(6, customer.getDivisionID());
            ps.setInt(7, customer.getJAppointmentCustomerID());

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>jCustomerDeleteByID Method</b> provides the logic needed to delete an existing Customer using
     * the Customer id as the filter.</p>
     * @param dbID <p>The Customer ID to be deleted.</p>
     * @throws SQLException <p>The error thrown by the JCustomerDAO Class (jCustomerDeleteByID Method).</p>
     */
    public static void jCustomerDeleteByID(int dbID) throws SQLException {

        try {
            String sqlStatement = "delete from customers " +
                    "where " +
                    "   Customer_ID = ?";

            DBSQL.setPreparedStatement(conn, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
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
}