package teoespero.jappointment.Controllers;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.DataAccessObjects.JContactDAO;
import teoespero.jappointment.DataAccessObjects.JCustomerDAO;
import teoespero.jappointment.DataAccessObjects.JUserDAO;
import teoespero.jappointment.Model.*;
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
 * <p>The <b>ModifyAppointmentScreController Class</b> provides the logic for modifying an existing appointment in the
 * system.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05292023
 */
public class ModifyAppointmentScreController implements Initializable {

    //  define the form window
    @FXML Stage stage;
    @FXML Parent scene;

    //  define the window elements
    @FXML private TextField txtAppointmentID;
    @FXML private TextField txtAppointmentTitle;
    @FXML private TextField txtAppointmentDescription;
    @FXML private TextField txtAppointmentLocation;
    @FXML private TextField txtAppointmentType;
    @FXML private DatePicker pickerStartDate;
    @FXML private DatePicker pickerEndDate;
    @FXML private TextField txtAppointmentCustomerID;
    @FXML private TextField txtAppointmentUserID;
    @FXML private ComboBox<Contact> cboAppointmentContact;
    @FXML private ComboBox<Customer> cboAppointmentCustomer;
    @FXML private ComboBox<User> cboAppointmentUser;
    @FXML private Spinner<LocalTime> spnrAppointmentStartTime;
    @FXML private Spinner<LocalTime> spnrAppointmentEndTimeSpinner;
    @FXML public Button btnSave;
    @FXML public Button btnCancel;

    //  define the vars that will hold the data
    private final DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("HH:mm");
    private User currentSessionUser = LoginScreController.getCurrentJAppointmentUser();
    private ObservableList<Contact> lstJContacts = FXCollections.observableArrayList();
    private ObservableList<Customer> lstJCustomers = FXCollections.observableArrayList();
    private ObservableList<User> lstJUsers = FXCollections.observableArrayList();
    private Appointment appointmentSelection;
    private Contact contactSelection;
    private Customer customerSelection;
    private User userSelection;

    /**
     * <p>The <b>btnCancelClicked Method</b> cancels any changes made to the appointment inside the Modify Screen.</p>
     * @param event <p>The event triggered when the user clicks on the Cancel button.</p>
     * @throws IOException <p>The error thrown by JavaFX when there is an error closing the form.</p>
     */
    @FXML public void btnCancelClicked(ActionEvent event) throws IOException {
        Optional<ButtonType> result = displayAlert("Information", "Alert",
                "Click OK to cancel the changes.");

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml",
                "Appointment Management System (AMS) - Appointments");
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
     * <p>the <b>cboCustomerEvent Method</b> displays the customer ID when the customer name is selected.</p>
     * @param event <p>The event triggered when a value in the combo is selected.</p>
     */
    @FXML public void cboCustomerEvent(ActionEvent event) {
        customerSelection = cboAppointmentCustomer.getSelectionModel().getSelectedItem();
        txtAppointmentCustomerID.setText(String.valueOf(customerSelection.getJAppointmentCustomerID()));
    }

    /**
     * <p>The <b>userComboBoxAction Method</b>  displays the user ID when the User name is selected.</p>
     * @param event <p>The event triggered when a value in the combo is selected.</p>
     */
    @FXML public void userComboBoxAction(ActionEvent event) {
        userSelection = cboAppointmentUser.getSelectionModel().getSelectedItem();
        txtAppointmentUserID.setText(String.valueOf(userSelection.getJAppointmentUserID()));
    }

    /**
     * <p>The <b>btnSaveClicked Method</b> provides the logic needed in saving the changes made on an appointment
     * record.</p>
     * @param event <p>The event triggered when the Save Appointment button is clicked.</p>
     * @throws IOException
     */
    @FXML public void btnSaveClicked(ActionEvent event) throws IOException {

        try {

            //  get form contents into the vars
            String appointmentTitle = txtAppointmentTitle.getText();
            String appointmentDescription = txtAppointmentDescription.getText();
            String appointmentLocation = txtAppointmentLocation.getText();
            String appointmentType = txtAppointmentType.getText();
            LocalDateTime appointmentStartTime=null;
            try{
                appointmentStartTime = getJAppointmentStartDateTime();
            } catch (Exception e) {
                displayAlert("Error", "Error", "Start Date is invalid.");
            }
            LocalDateTime appointmentEndTime = null;
            try{
                appointmentEndTime = getJAppointmentEndDateTime();
            } catch (Exception e) {
                displayAlert("Error", "Error", "End Date is invalid.");
            }
            String appointmentCreatedBy = appointmentSelection.getJAppointmentCreatedBy();
            String appointmentLastUpdatedBy = currentSessionUser.getJAppointmentUserName();
            int appointmentCustomerID = customerSelection.getJAppointmentCustomerID();
            int appointmentUserID = userSelection.getJAppointmentUserID();
            int appointmentContactID = cboAppointmentContact.getSelectionModel().getSelectedItem().getJAppointmentContactID();

            //  the code below validates the form entries if they are empty
            if (appointmentTitle.isEmpty()){
                displayAlert("Error", "Error", "The Appointment Title is required.");
            } else if (appointmentDescription.isEmpty()) {
                displayAlert("Error", "Error", "The Appointment Description is required.");
            } else if (appointmentLocation.isEmpty()){
                displayAlert("Error", "Error", "The Appointment Location is required.");
            } else if (appointmentCreatedBy.isEmpty() || appointmentLastUpdatedBy.isEmpty()){
                displayAlert("Error", "Error", "The User is required.");
            } else if (appointmentType.isEmpty()){
                displayAlert("Error", "Error", "The Appointment Type is required.");
            }else{

                //  note that the database has a 50-character limit
                //  which is why we are only getting the first 50 characters
                //  of the information entered, otherwise the application crashes
                appointmentTitle = appointmentTitle.substring(0, Math.min(appointmentTitle.length(), 50));
                appointmentDescription = appointmentDescription.substring(0, Math.min(appointmentDescription.length(), 50));
                appointmentLocation = appointmentLocation.substring(0, Math.min(appointmentLocation.length(), 50));
                appointmentType = appointmentType.substring(0, Math.min(appointmentType.length(), 50));
            }

            //  create a new object defined by the textbox contents
            Appointment newAppointment = new Appointment(
                    appointmentTitle,
                    appointmentDescription,
                    appointmentLocation,
                    appointmentType,
                    jConvertToUtc(appointmentStartTime),
                    jConvertToUtc(appointmentEndTime),
                    appointmentCreatedBy,
                    appointmentLastUpdatedBy,
                    appointmentCustomerID,
                    appointmentUserID,
                    appointmentContactID
            );

            newAppointment.setJAppointmentID(appointmentSelection.getJAppointmentID());

            //  check for overlaps
            if (DBCheckAppointment.appointmentOverlapChecks(newAppointment)) {
                displayAlert("Error", "Error",
                        "Appointment is overlapping with another appointment.");
            }

            //  check if any of the fields are empty
            if (DBCheckAppointment.fieldsBlank(newAppointment) ||
                    DBCheckAppointment.startTimePastEndTime(newAppointment) ||
                    DBCheckAppointment.appointmentSetAfterWorkHours(newAppointment) ||
                    DBCheckAppointment.appointmentOverlapChecks(newAppointment)) {
            } else {

        JAppointmentDAO.jApptUpdate(newAppointment);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml",
                "Appointment Management System (AMS) - Appointments");
    }

        }
        catch(NullPointerException e) {
            displayAlert("Error", "Error",
                    "One or more fields are not selected.");
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

    }

    /**
     * <p>The <b>endTimeSpnrValueFact Method</b> provides the logic and control on the spinners for the
     * appointment's start time </p>
     */
    public LocalDateTime getJAppointmentStartDateTime() {

        LocalDate startDate = pickerStartDate.getValue();
        LocalTime startTime = spnrAppointmentStartTime.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        return startDateTime;
    }

    /**
     * <p>The <b>endTimeSpnrValueFact Method</b> provides the logic and control on the spinners for the
     * appointment's end date </p>
     */
    public LocalDateTime getJAppointmentEndDateTime() {

        LocalDate endDate = pickerEndDate.getValue();
        LocalTime endTime = spnrAppointmentEndTimeSpinner.getValue();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return endDateTime;
    }

    /**
     * <p>The <b>endTimeSpnrValueFact Method</b> provides the logic and control on the spinners for the
     * appointment's start time </p>
     */
    SpinnerValueFactory startTimeSpnrValueFact = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(timePattern,null));
        }
        @Override public void decrement(int steps) {
            LocalTime localTimeSettings = (LocalTime) getValue();
            setValue(localTimeSettings.plusHours(steps));
            setValue(localTimeSettings.plusMinutes(steps + 14));
        }
        @Override public void increment(int steps) {
            LocalTime localTimeSettings = (LocalTime) getValue();
            setValue(localTimeSettings.minusHours(steps));
            setValue(localTimeSettings.minusMinutes(16 - steps));
        }
    };

    /**
     * <p>The <b>endTimeSpnrValueFact Method</b> provides the logic and control on the spinners for the
     * appointment's end time </p>
     */
    SpinnerValueFactory endTimeSpnrValueFact = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(timePattern,null));
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
     * <p>The <b>setFormValues Method</b> captures the selected appointment from the main appointment screen
     * and puts the data into the Modify Appointment Screen.</p>
     */
    @FXML public void setFormValues() {

        appointmentSelection = AppointmentScreController.getHighlightedAppointment();

        //Set text fields
        txtAppointmentID.setText(String.valueOf(appointmentSelection.getJAppointmentID()));
        txtAppointmentTitle.setText(appointmentSelection.getJAppointmentTitle());
        txtAppointmentDescription.setText(appointmentSelection.getJAppointmentDescription());
        txtAppointmentLocation.setText(appointmentSelection.getJAppointmentLocation());
        txtAppointmentType.setText(appointmentSelection.getJAppointmentType());




        LocalTime startTime = appointmentSelection.getJClientAppointmentStartTime().toLocalTime();
        LocalDate startDate = appointmentSelection.getJClientAppointmentStartTime().toLocalDate();
        LocalDateTime localStartDateTime = startTime.atDate(startDate);
        LocalDateTime utcLocalStartDateTime = jConvertFromUtc(localStartDateTime);
        LocalDate localStartDate = utcLocalStartDateTime.toLocalDate();
        LocalTime localStartTime = utcLocalStartDateTime.toLocalTime();


        //Set start time spinner
        startTimeSpnrValueFact.setValue(localStartTime);
        spnrAppointmentStartTime.setValueFactory(startTimeSpnrValueFact);
        //Set start date picker
        pickerStartDate.setValue(localStartDate);


        LocalTime endTime = appointmentSelection.getJAppointmentEndTime().toLocalTime();
        LocalDate endDate = appointmentSelection.getJAppointmentEndTime().toLocalDate();
        LocalDateTime localEndtDateTime = endTime.atDate(endDate);
        LocalDateTime utcLocalEndDateTime = jConvertFromUtc(localEndtDateTime);
        LocalDate localEndDate = utcLocalEndDateTime.toLocalDate();
        LocalTime localEndTime = utcLocalEndDateTime.toLocalTime();

        //Set end time spinner
        endTimeSpnrValueFact.setValue(localEndTime);
        spnrAppointmentEndTimeSpinner.setValueFactory(endTimeSpnrValueFact);

        //Set end date picker
        pickerEndDate.setValue(localEndDate);

        try {
            //Set contact combo box
            lstJContacts = JContactDAO.jContactSelectAll();
            cboAppointmentContact.setItems(lstJContacts);
            contactSelection = JContactDAO.jContactSelectByID(appointmentSelection.getJAppointmentContactID());
            cboAppointmentContact.setValue(contactSelection);

            //Set customer combo box
            lstJCustomers = JCustomerDAO.jCustomerSelectAll();
            cboAppointmentCustomer.setItems(lstJCustomers);
            customerSelection = JCustomerDAO.jCustomerSelectByID(appointmentSelection.getJAppointmentCustomerID());
            cboAppointmentCustomer.setValue(customerSelection);
            txtAppointmentCustomerID.setText(String.valueOf(customerSelection.getJAppointmentCustomerID()));

            //Set user combo box
            lstJUsers = JUserDAO.jUserSelectAll();
            cboAppointmentUser.setItems(lstJUsers);
            userSelection = JUserDAO.jUserSelectByID(appointmentSelection.getJAppointmentUserID());
            cboAppointmentUser.setValue(userSelection);
            txtAppointmentUserID.setText(String.valueOf(userSelection.getJAppointmentUserID()));
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
    LocalDateTime jConvertToUtc(LocalDateTime dateTime) {
        ZonedDateTime dateTimeInMyZone = ZonedDateTime.of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

    }

    /**
     * This method converts Date/Time from UTC to the system time zone.
     * @param utcDateTime The UTC Date/Time to convert.
     * @return The Date/Time in the local time zone.
     */
    LocalDateTime jConvertFromUtc(LocalDateTime utcDateTime){
        return ZonedDateTime.of(utcDateTime, ZoneId.of("UTC")).toOffsetDateTime().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }


    /**
     * <p>The <b>initialize method</b> initializes the ModifyAppointmentScreController Class by pre-populating
     * the form.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @Override @FXML public void initialize(URL location, ResourceBundle resources) {setFormValues();}
}