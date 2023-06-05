package teoespero.jappointment.DataAccessObjects;

import teoespero.jappointment.DBHelper.DBSQL;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.DBHelper.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * <p>The <b>JAppointmentDAO Class</b> provides the logic that interfaces with with database and tables (Appointments).
 * This acts as the back-end of the JAppointment application.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class JAppointmentDAO {

    private static Connection jAppDBConnection = null;

    static {
        try {
            jAppDBConnection = DBConnection.startConnection();
        } catch (ClassNotFoundException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>jApptCreate Method</b> defines the logic that creates new appointments in the database.</p>
     * @param appointment <p>The DOM that defines the details of the appointment to be created.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptCreate Method).</p>
     */
    public static void jApptCreate(Appointment appointment) throws SQLException {

        try {
            String sqlStatement = "insert into appointments(" +
                        "Title, " +
                        "Description, " +
                        "Location, " +
                        "Type, " +
                        "Start, " +
                        "End, " +
                        "Create_Date, " +
                        "Created_By, " +
                        "Last_Update, " +
                        "Last_Updated_By, " +
                        "Customer_ID, " +
                        "User_ID, " +
                        "Contact_ID) " +
                    "VALUES(" +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "NOW(), " +
                        "?, " +
                        "NOW(), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?" +
                    ")";


            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, appointment.getJAppointmentTitle());
            ps.setString(2,appointment.getJAppointmentDescription());
            ps.setString(3, appointment.getJAppointmentLocation());
            ps.setString(4, appointment.getJAppointmentType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getJClientAppointmentStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getJAppointmentEndTime()));
            ps.setString(7, appointment.getJAppointmentCreatedBy());
            ps.setString(8, appointment.getJAppointmentLastUpdatedBy());
            ps.setInt(9, appointment.getJAppointmentCustomerID());
            ps.setInt(10, appointment.getJAppointmentUserID());
            ps.setInt(11, appointment.getJAppointmentContactID());

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }



    /**
     * <p>The <b>jApptSelectAll Method</b> returns all the appointments in the database.</p>
     * @return <p>All the appointments.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptSelectAll Method).</p>
     */
    public static ObservableList<Appointment> jApptSelectAll() throws SQLException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String sqlStatement = "select * " +
                    "from appointments " +
                    "order " +
                    "   by Start";

            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                Appointment appointment = new Appointment();

                appointment.setJAppointmentID(rs.getInt("Appointment_ID"));
                appointment.setJAppointmentTitle(rs.getString("Title"));
                appointment.setJAppointmentDescription(rs.getString("Description"));
                appointment.setJAppointmentLocation(rs.getString("Location"));
                appointment.setJAppointmentType(rs.getString("Type"));
                appointment.setJAppointmentStartTime(rs.getTimestamp("Start").toLocalDateTime());
                appointment.setJAppointmentEndTime(rs.getTimestamp("End").toLocalDateTime());
                appointment.setJAppointmentCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                appointment.setCreatedBy(rs.getString("Created_By"));
                appointment.setJAppointmentLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                appointment.setJAppointmentCustomerID(rs.getInt("Customer_ID"));
                appointment.setUserID(rs.getInt("User_ID"));
                appointment.setJAppointmentContactID(rs.getInt("Contact_ID"));

                appointments.add(appointment);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return appointments;
    }

    /**
     * <p>The <b>jApptSelectAllOrderByContact Method</b> returns all the appointments ordered by their contact id</p>
     * @return <p>All the sorted appointments.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptSelectAllOrderByContact Method).</p>
     */
    public static ObservableList<Appointment> jApptSelectAllOrderByContact() throws SQLException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String sqlStatement = "select * " +
                    "from appointments " +
                    "order by " +
                    "   Contact_ID, " +
                    "   Start";

            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                Appointment appointment = new Appointment();

                appointment.setJAppointmentID(rs.getInt("Appointment_ID"));
                appointment.setJAppointmentTitle(rs.getString("Title"));
                appointment.setJAppointmentDescription(rs.getString("Description"));
                appointment.setJAppointmentLocation(rs.getString("Location"));
                appointment.setJAppointmentType(rs.getString("Type"));
                appointment.setJAppointmentStartTime(rs.getTimestamp("Start").toLocalDateTime());
                appointment.setJAppointmentEndTime(rs.getTimestamp("End").toLocalDateTime());
                appointment.setJAppointmentCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                appointment.setCreatedBy(rs.getString("Created_By"));
                appointment.setJAppointmentLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                appointment.setJAppointmentCustomerID(rs.getInt("Customer_ID"));
                appointment.setUserID(rs.getInt("User_ID"));
                appointment.setJAppointmentContactID(rs.getInt("Contact_ID"));

                appointments.add(appointment);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return appointments;
    }

    /**
     * <p>The <b>jApptSelectAllOrderByLocation Method</b> returns all the appointments sorted by their locations.</p>
     * @return <p>All the sorted appointments.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptSelectAllOrderByLocation Method).</p>
     */
    public static ObservableList<Appointment> jApptSelectAllOrderByLocation() throws SQLException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String sqlStatement = "select * " +
                    "from appointments " +
                    "order by " +
                    "   Location, " +
                    "   Start";

            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                Appointment appointment = new Appointment();

                appointment.setJAppointmentID(rs.getInt("Appointment_ID"));
                appointment.setJAppointmentTitle(rs.getString("Title"));
                appointment.setJAppointmentDescription(rs.getString("Description"));
                appointment.setJAppointmentLocation(rs.getString("Location"));
                appointment.setJAppointmentType(rs.getString("Type"));
                appointment.setJAppointmentStartTime(rs.getTimestamp("Start").toLocalDateTime());
                appointment.setJAppointmentEndTime(rs.getTimestamp("End").toLocalDateTime());
                appointment.setJAppointmentCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
                appointment.setCreatedBy(rs.getString("Created_By"));
                appointment.setJAppointmentLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
                appointment.setJAppointmentCustomerID(rs.getInt("Customer_ID"));
                appointment.setUserID(rs.getInt("User_ID"));
                appointment.setJAppointmentContactID(rs.getInt("Contact_ID"));

                appointments.add(appointment);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return appointments;
    }

    /**
     * <p>The <b>jApptUpdate Method</b> provides the logic needed to update an appointment using the appointment
     * id as the parameter</p>
     * @param appointment <p>The appointment object that defines the new details to replace the
     *                    old appointment details.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptUpdate Method).</p>
     */
    public static void jApptUpdate(Appointment appointment) throws SQLException {

        try {
            String sqlStatement = "update appointments " +
                    "   set " +
                    "       Title = ?, " +
                    "       Description = ?, " +
                    "       Location = ?, " +
                    "       Type = ?, " +
                    "       Start = ?, " +
                    "       End = ?, " +
                    "       Last_update = NOW(), " +
                    "       Last_Updated_By = ?, " +
                    "       Customer_ID = ?, " +
                    "       User_ID = ?, " +
                    "       Contact_ID = ? " +
                    "where " +
                    "       Appointment_ID = ?";

            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setString(1, appointment.getJAppointmentTitle());
            ps.setString(2, appointment.getJAppointmentDescription());
            ps.setString(3, appointment.getJAppointmentLocation());
            ps.setString(4, appointment.getJAppointmentType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getJClientAppointmentStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getJAppointmentEndTime()));
            ps.setString(7, appointment.getJAppointmentLastUpdatedBy());
            ps.setInt(8, appointment.getJAppointmentCustomerID());
            ps.setInt(9, appointment.getJAppointmentUserID());
            ps.setInt(10, appointment.getJAppointmentContactID());
            ps.setInt(11, appointment.getJAppointmentID());

            ps.execute();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>jApptDeleteByID Method</b> provides the logic needed to delete an existing appointment using
     * the appointment id as the filter.</p>
     * @param dbID <p>The appointment ID to be deleted.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptDeleteByID Method).</p>
     */
    public static void jApptDeleteByID(int dbID) throws SQLException {

        try{
            String sqlStatement = "delete from " +
                    "appointments " +
                    "where " +
                    "   Appointment_ID = ?";

            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

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

    /**
     * <p>The <b>jApptSelectByID Method (unused)</b> locates an appointment using the appointment ID
     * as the search key.</p>
     * @param dbID <p>The appointment ID</p>
     * @return <p>The appointment based on the appointment ID passed as a parameter.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptSelectByID Method).</p>
     */
    public static Appointment jApptSelectByID(int dbID) throws SQLException {

        Appointment appointment = new Appointment();

        try {
            String sqlStatement = "select * " +
                    "from appointments " +
                    "where " +
                    "   Appointment_ID = ?";

            DBSQL.setPreparedStatement(jAppDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();

            appointment.setJAppointmentID(rs.getInt("Appointment_ID"));
            appointment.setJAppointmentTitle(rs.getString("Title"));
            appointment.setJAppointmentDescription(rs.getString("Description"));
            appointment.setJAppointmentLocation(rs.getString("Location"));
            appointment.setJAppointmentType(rs.getString("Type"));
            appointment.setJAppointmentStartTime(rs.getTimestamp("Start").toLocalDateTime());
            appointment.setJAppointmentEndTime(rs.getTimestamp("End").toLocalDateTime());
            appointment.setJAppointmentCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
            appointment.setCreatedBy(rs.getString("Created_By"));
            appointment.setJAppointmentLastUpdateTime(rs.getTimestamp("Last_Update").toLocalDateTime());
            appointment.setJAppointmentCustomerID(rs.getInt("Customer_ID"));
            appointment.setUserID(rs.getInt("User_ID"));
            appointment.setJAppointmentContactID(rs.getInt("Contact_ID"));
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return appointment;
    }
}