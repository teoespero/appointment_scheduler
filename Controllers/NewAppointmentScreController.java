package teoespero.jappointment.Controllers;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.DataAccessObjects.JContactDAO;
import teoespero.jappointment.DataAccessObjects.JCustomerDAO;
import teoespero.jappointment.DataAccessObjects.JUserDAO;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.Model.Contact;
import teoespero.jappointment.Model.Customer;
import teoespero.jappointment.Model.User;
import teoespero.jappointment.DBHelper.DBCheckAppointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>The <b>NewAppointmentScreController Method</b> provides the needed logic and control for adding new
 * appointments to the database.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class NewAppointmentScreController implements Initializable {

    //  Define the GUI elements that we need for the Appointments form. Most of the elements are
    //  defined as private, this is to define the elements for use only within the class.

    //  Define the window
    @FXML Stage stage;
    @FXML Parent scene;

    //  The fields where the user can enter the appointment details
    @FXML private TextField txtAppointmentTitle;
    @FXML private TextField txtAppointmentDescription;
    @FXML private TextField txtAppointmentLocation;
    @FXML private TextField txtAppointmentType;
    @FXML private DatePicker pkrAppointmentStartDate;
    @FXML private DatePicker pkrAppointmentEndDate;
    @FXML private TextField txtAppointmentCustomerID;
    @FXML private TextField txtAppointmentUserID;
    @FXML private ComboBox<Contact> cboAppointmentContact;
    @FXML private ComboBox<Customer> cboAppointmentCustomer;
    @FXML private ComboBox<User> cboAppointmentUser;
    @FXML private Spinner<LocalTime> spnrAppointmentStartTime;
    @FXML private Spinner<LocalTime> spntAppointmentEndTime;

    //  vars that will hold data essential to the forms function
    private final DateTimeFormatter patternDateTime = DateTimeFormatter.ofPattern("HH:mm");
    private User currentUser = LoginScreController.getCurrentJAppointmentUser();
    private ObservableList<Contact> lstContact = FXCollections.observableArrayList();
    private ObservableList<Customer> lstCustomer = FXCollections.observableArrayList();
    private ObservableList<User> lstUser = FXCollections.observableArrayList();
    private Contact contactSelection;
    private Customer customerSelection;
    private User userSelection;

    /**
     * <p>The <b>btnCancelClicked Method</b> brings the user back to the Appointments Screen.</p>
     * @param event <p>The event triggered when the user clicks on the Cancel button.</p>
     * @throws IOException <p>The error thrown when JavaFX encounters an error.</p>
     */
    @FXML public void btnCancelClicked(ActionEvent event) throws IOException {
        Optional<ButtonType> result = displayAlert("Confirmation", "Alert",
                "Click OK to cancel changes and return to the Appointments screen?");

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml",
                "Appointment Management System (AMS) - Appointments");
    }

    /**
     * <p>The <b>cboCustomerClicked Method</b> displays the Customer ID when a user selects on the Customer name
     * Combo Box Dropdown.</p>
     * @param event <p>The event triggered when the user makes a combo box selection.</p>
     */
    @FXML public void cboCustomerClicked(ActionEvent event) {

        customerSelection = cboAppointmentCustomer.getSelectionModel().getSelectedItem();
        txtAppointmentCustomerID.setText(String.valueOf(customerSelection.getJAppointmentCustomerID()));
    }

    /**
     * <p>The <b>cboUserClicked Method</b> displays the User ID when a user selects on the Username Combo
     * Dropbox Dropdown.</p>
     * @param event <p>The event triggered when the user makes a combo box selection.</p>
     */
    @FXML public void cboUserClicked(ActionEvent event) {

        userSelection = cboAppointmentUser.getSelectionModel().getSelectedItem();
        txtAppointmentUserID.setText(String.valueOf(userSelection.getJAppointmentUserID()));
    }

    /**
     * <p>The <b>btnSaveClicked Method</b> provides the logic and controls needed to save the information provided
     * by the user about the appointment.</p>
     * @param event <p>The event triggered when the user clicks on the Save button.</p>
     * @throws IOException <p>The error thrown by the JAppointmentDAO Class (jApptCreate).</p>
     */
    @FXML public void btnSaveClicked(ActionEvent event) throws IOException {

        try {
            String title = txtAppointmentTitle.getText();
            String description = txtAppointmentDescription.getText();
            String location = txtAppointmentLocation.getText();
            String type = txtAppointmentType.getText();
            LocalDateTime startTime=null;
            try{
                startTime = getStartDateTime();
            } catch (Exception e) {
                displayAlert("Error", "Error", "Start Date is invalid");
            }
            LocalDateTime endTime=null;
            try{
                endTime = getEndDateTime();
            } catch (Exception e) {
                displayAlert("Error", "Error", "End Date is invalid");
            }
            String createdBy = currentUser.getJAppointmentUserName();
            String lastUpdatedBy = currentUser.getJAppointmentUserName();
            //  int customerID = customerSelection.getJAppointmentCustomerID();
            int customerID = Integer.parseInt(txtAppointmentCustomerID.getText());
            //  int userID = userSelection.getJAppointmentUserID();
            int userID = Integer.parseInt(txtAppointmentUserID.getText());
            //  int contactID = cboAppointmentContact.getSelectionModel().getSelectedItem().getJAppointmentContactID();
            int contactID = cboAppointmentContact.getValue().getJAppointmentContactID();

            //  the code below validates the form entries if they are empty
            if (title.isEmpty()){
                displayAlert("Error", "Error", "The Appointment Title is required.");
            } else if (description.isEmpty()) {
                displayAlert("Error", "Error", "The Appointment Description is required.");
            } else if (location.isEmpty()){
                displayAlert("Error", "Error", "The Appointment Location is required.");
            } else if (createdBy.isEmpty() || lastUpdatedBy.isEmpty()){
                displayAlert("Error", "Error", "The User is required.");
            } else if (type.isEmpty()){
                displayAlert("Error", "Error", "The Appointment Type is required.");
            }else{

                //  note that the database has a 50-character limit
                //  which is why we are only getting the first 50 characters
                //  of the information entered, otherwise the application crashes
                title = title.substring(0, Math.min(title.length(), 50));
                description = description.substring(0, Math.min(description.length(), 50));
                location = location.substring(0, Math.min(location.length(), 50));
                type = type.substring(0, Math.min(type.length(), 50));
            }


            Appointment newAppointment = new Appointment(title, description, location, type,
                    jConvertToUtc(startTime), jConvertToUtc(endTime), createdBy, lastUpdatedBy, customerID, userID, contactID);

            if (!DBCheckAppointment.appointmentOverlapChecks(newAppointment)) {
            } else {
                displayAlert("Error", "Error", "There is an overlaps with another appointment.");
            }

            if (DBCheckAppointment.fieldsBlank(newAppointment) ||
                    DBCheckAppointment.startTimePastEndTime(newAppointment) ||
                    DBCheckAppointment.appointmentSetAfterWorkHours(newAppointment) ||
                    DBCheckAppointment.appointmentOverlapChecks(newAppointment)) {
            } else {

        JAppointmentDAO.jApptCreate(newAppointment);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml",
                "Appointment Management System (AMS) - Appointments");
    }

        }
        catch(NullPointerException e) {
            //displayAlert("Error", "Error", "All fields are required and cannot be left blank");
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }


    /**
     * <p>The <b>displayWindow Method</b> is a user-defined method that is used within the application to load the
     * different windows used.</p>
     * @param stage <p>The top-level JavaFX container in our JAppointment application.</p>
     * @param windowName <p>The relative location of the FXML file to be used.</p>
     * @param windowTitle <p>The title of the FXML file.</p>
     * @throws IOException <p>The error thrown by JavaFX if it encounters an error when loading the form.</p>
     */
    @FXML public  void displayWindow(Stage stage, String windowName, String windowTitle) throws IOException {
        scene = FXMLLoader.load(getClass().getResource(windowName));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
        Rectangle2D screBoundary = Screen.getPrimary().getVisualBounds();
        stage.setX((screBoundary.getWidth() - stage.getWidth()) / 2);
        stage.setY((screBoundary.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * <p>The <b>displayAlert Method</b> provides the logic needed to display custom-defined messages to the user
     * when there is an error, or a confirmation is needed.</p>
     * @param alertType <p>Defines the type of alert (ERROR, CONFIRMATION).</p>
     * @param alertTitle <p>The alert window title.</p>
     * @param alertHeader <p>The message to be displayed.</p>
     * @return <p>An optional return value (button type).</p>
     */
    @FXML public Optional<ButtonType> displayAlert(String alertType, String alertTitle, String alertHeader){
        Alert alert = null;
        if (alertType == "Information"){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        if (alertType == "Error"){
            alert = new Alert(Alert.AlertType.ERROR);
        }
        if (alertType == "Confirmation"){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        }
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * <p><b>JAppointmentStartSVF</b> creates a time spinner with 15-minutes intervals.</p>
     */
    SpinnerValueFactory JAppointmentStartSVF = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(patternDateTime,null));
        }
        @Override public void decrement(int steps) {
            LocalTime localTime = (LocalTime) getValue();
            setValue(localTime.plusHours(steps));
            setValue(localTime.plusMinutes(steps + 14));
        }
        @Override public void increment(int steps) {
            LocalTime localTime = (LocalTime) getValue();
            setValue(localTime.minusHours(steps));
            setValue(localTime.minusMinutes(16 - steps));
        }
    };

    /**
     * <p><b>JAppointmentEndSVF</b> creates a time spinner with 15-minutes intervals.</p>
     */
    SpinnerValueFactory JAppointmentEndSVF = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(patternDateTime,null));
        }
        @Override public void decrement(int steps) {
            LocalTime localTime = (LocalTime) getValue();
            setValue(localTime.plusHours(steps));
            setValue(localTime.plusMinutes(steps + 14));
        }
        @Override public void increment(int steps) {
            LocalTime localTime = (LocalTime) getValue();
            setValue(localTime.minusHours(steps));
            setValue(localTime.minusMinutes(16 - steps));
        }
    };

    /**
     * <p>The <b>getStartDateTime Method</b> makes a LocalDateTime object from the values defined in the date picker
     * and time spinner</p>
     * @return <p>Returns the LocalDateTime created.</p>
     */
    public  LocalDateTime getStartDateTime() {

        LocalDate startDate = pkrAppointmentStartDate.getValue();
        LocalTime startTime = spnrAppointmentStartTime.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        return startDateTime;
    }

    /**
     * <p>The <b>getEndDateTime Method</b> makes a LocalDateTime object from the values defined in the date picker
     * and time spinner</p>
     * @return <p>Returns the LocalDateTime created.</p>
     */
    public LocalDateTime getEndDateTime() {

        LocalDate endDate = pkrAppointmentEndDate.getValue();
        LocalTime endTime = spntAppointmentEndTime.getValue();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return endDateTime;
    }




    /**
     * <p>The <b>setApptScreDefaultValues Method</b> provides logic and controls that defines the default values of
     * certain fields.</p>
     * <ul>
     *     <li>Date pickers</li>
     *     <li>Spinners</li>
     *     <li>Contacts</li>
     *     <li>Customers</li>
     *     <li>Users</li>
     * </ul>
     */
    @FXML public  void setApptScreDefaultValues() {

        JAppointmentStartSVF.setValue(LocalTime.of(8, 00));
        spnrAppointmentStartTime.setValueFactory(JAppointmentStartSVF);
        JAppointmentEndSVF.setValue(LocalTime.of(8, 15));
        spntAppointmentEndTime.setValueFactory(JAppointmentEndSVF);

        try {
            lstContact = JContactDAO.jContactSelectAll();
            cboAppointmentContact.setItems(lstContact);
            cboAppointmentContact.getSelectionModel().selectFirst();
            cboAppointmentContact.getSelectionModel().selectFirst();

            lstCustomer = JCustomerDAO.jCustomerSelectAll();
            cboAppointmentCustomer.setItems(lstCustomer);
            cboAppointmentCustomer.getSelectionModel().selectFirst();
            txtAppointmentCustomerID.setText(String.valueOf(cboAppointmentCustomer.getValue().getJAppointmentCustomerID()));

            lstUser = JUserDAO.jUserSelectAll();
            cboAppointmentUser.setItems(lstUser);
            cboAppointmentUser.getSelectionModel().selectFirst();
            txtAppointmentUserID.setText(String.valueOf(cboAppointmentUser.getValue().getJAppointmentUserID()));

            //pkrAppointmentEndDate.setValue(LocalDate.now());
            //pkrAppointmentStartDate.setValue(LocalDate.now());
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
     * This method converts LocalDateTime to UTC before saving it to the database.
     * @param dateTime The Dat/Time to be converted.
     * @return The UTC value of the Date/Time.
     */
    public LocalDateTime jConvertToUtc(LocalDateTime dateTime) {
        ZonedDateTime dateTimeInMyZone = ZonedDateTime.of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

    }

    /**
     * This method converts Date/Time from UTC to the system time zone.
     * @param utcDateTime The UTC Date/Time to convert.
     * @return The Date/Time in the local time zone.
     */
    public LocalDateTime jConvertFromUtc(LocalDateTime utcDateTime){
        return ZonedDateTime.of(utcDateTime, ZoneId.of("UTC")).toOffsetDateTime().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * <p>The <b>initialize method</b> initializes the AppointmentScreController Class by pre-populating
     * the appointments grid.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @FXML @Override public void initialize(URL location, ResourceBundle resources) {
        setApptScreDefaultValues();}
}