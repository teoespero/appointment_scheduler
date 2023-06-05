package teoespero.jappointment.DataAccessObjects;

import teoespero.jappointment.DBHelper.DBSQL;
import teoespero.jappointment.Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teoespero.jappointment.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>The <b>JContactDAO Class</b> provides the logic that interfaces with with database and tables (Contact).
 * This acts as the back-end of the JAppointment application.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class JContactDAO {

    private final static Connection jApptDBConnection = Main.mysqlConn;



    /**
     * <p>The <b>jContactSelectByID Method</b> locates a Contact record based on the contact id.</p>
     * @param dbID <p>The contact id used as a WHERE clause.</p>
     * @return <p>The contact record that matches the contact ID filter.</p>
     * @throws SQLException <p>The error thrown by the JContactDAO Class (jContactSelectByID Method).</p>
     */
    public static Contact jContactSelectByID(int dbID) throws SQLException {

        Contact contact = new Contact();

        try {
            String sqlStatement = "select * " +
                    "from contacts " +
                    "where " +
                    "   Contact_ID = ?";

            DBSQL.setPreparedStatement(jApptDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.setInt(1, dbID);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();

            contact.setContactID(rs.getInt("Contact_ID"));
            contact.setContactName(rs.getString("Contact_Name"));
            contact.setEmail(rs.getString("Email"));
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return contact;
    }

    /**
     * <p>The <b>jContactSelectAll Method</b> returns all the contact records in the database.</p>
     * @return <p>All the contacts.</p>
     * @throws SQLException <p>The error thrown by the JContactDAO Class (jContactSelectAll Method).</p>
     */
    public static ObservableList<Contact> jContactSelectAll() throws SQLException {

        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        try {
            String sqlStatement = "select * " +
                    "from contacts";

            DBSQL.setPreparedStatement(jApptDBConnection, sqlStatement);

            PreparedStatement ps = DBSQL.getPreparedStatement();

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                Contact contact = new Contact();

                contact.setContactID(rs.getInt("Contact_ID"));
                contact.setContactName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));

                contacts.add(contact);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        return contacts;
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
