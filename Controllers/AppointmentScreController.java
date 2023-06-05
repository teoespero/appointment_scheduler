package teoespero.jappointment.Controllers;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.DataAccessObjects.JContactDAO;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.Model.AppointmentDOM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>The <b>AppointmentScreController Class</b> provides the control logic functions needed by the Java Appointment
 * Application Project. From this screen the user is given the ability to:</p>
 * <ul>
 *     <li>Search for an appointment.</li>
 *     <li>Add a new appointment.</li>
 *     <li>Modify existing appointment.</li>
 *     <li>Delete an existing appointment.</li>
 *     <li>Maintain the customer database</li>
 *     <li>Run system generated reports.</li>
 * </ul>
 * @author Teodulfo Espero (BS Software Fevelopment, WGU)
 * @since 1.0.29.2023
 */
public class AppointmentScreController implements Initializable {

    //  Define the GUI elements that we need for the Appointments form. Most of the elements are
    //  defined as private, this is to define the elements for use only within the class.

    //  Defining the window
    @FXML Stage stage;
    @FXML Parent scene;

    //  Defining the Search elements
    @FXML private ToggleGroup grpAppointmentsView;
    @FXML private RadioButton rdoAllAppts;
    @FXML private RadioButton rdoApptByWeek;
    @FXML private RadioButton rdoApptByMonth;
    @FXML private TextField txtSearchBox;
    @FXML private Button btnClearSearchBox;
    @FXML private Button btnSearch;

    //  Defining the grid
    @FXML private static Appointment highlightedAppointment;
    @FXML private TableView<Appointment> tblViewAppointments;
    @FXML private TableColumn<Appointment, Integer> fldApptID;
    @FXML private TableColumn<Appointment, String> fldTitle;
    @FXML private TableColumn<Appointment, String> fldDescription;
    @FXML private TableColumn<Appointment, String> fldContact;
    @FXML private TableColumn<Appointment, String> fldType;
    @FXML private TableColumn<Appointment, String> fldLocation;
    @FXML private TableColumn<Appointment, LocalDateTime> fldApptStart;
    @FXML private TableColumn<Appointment, LocalDateTime> fldApptEnd;
    @FXML private TableColumn<Appointment, Integer> fldCustomerID;

    //  Defining the buttons
    @FXML private Button btnNewAppointment;
    @FXML private Button btnModifyAppointment;
    @FXML private Button btnDeleteAppointment;
    @FXML private Button btnCustomers;
    @FXML private Button btnReports;
    @FXML private Button btnExit;

    /**
     * <p>The <b>btnClearSearchBoxClicked Method</b> clears the textbox used by the Appointment screen's
     * search function.</p>
     * @param event <p>The event triggered when the Clear button is clicked.</p>
     * @throws SQLException <p>The error thrown by the JAppointment.DAO method (jApptSelectAll).</p>
     */
    @FXML public void btnClearSearchBoxClicked(ActionEvent event) throws SQLException {

        txtSearchBox.setText("");
        if (rdoAllAppts.isSelected()) showJAllAppointments();
        if (rdoApptByMonth.isSelected()) showJAppointmentsByMonth();
        if (rdoApptByWeek.isSelected()) showJAppointmentsByWeek();
    }

    /**
     * <p>The <b>rdoAllApptsAction Method</b> provides logic that display all the appointments listed in the
     * database (showJAllAppointments)</p>
     * @param event <p>The error thrown by the JAppointment.DAO method (jApptSelectAll).</p>
     */
    @FXML public void rdoAllApptsAction(ActionEvent event) {

        try {
            showJAllAppointments();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>btnCustomersClicked Method</b> transfers control from the appointment screen to the customer screen.
     * This allows the user to maintain the customer database.</p>
     * @param event <p>The event triggered when the Customer button is clicked.</p>
     * @throws IOException <p>The error thrown by JavaFX when an error occurs.</p>
     */
    @FXML public void btnCustomersClicked(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/CustomersScre.fxml",
                "Appointment Management System (AMS) - Customers");

    }

    /**
     * <p>The <b>btnDeleteAppointmentClicked Method</b> provides the logic that allows the user to delete an
     * existing apppointment.</p>
     * <b>Conditions:</b>
     * <ul>
     *     <li>An appointment must first be selected before clicking on the Delete appointment button.</li>
     *     <li>A user can only delete one appointment at a time.</li>
     * </ul>
     * @param event <p>The event triggered when the Delete appointment button is clicked.</p>
     */
    @FXML public void btnDeleteAppointmentClicked(ActionEvent event) {

        //  capture the selected appointment
        highlightedAppointment = tblViewAppointments.getSelectionModel().getSelectedItem();

        //  if no appointment was selected prior to clicking the delete appointment button
        //  fire up an error message to inform the user of the error.
        if (highlightedAppointment == null) {
            displayAlert("Error", "Delete Error",
                    "You must select an appointment from the list");
        } else {

            //  confirm with the user if they would like to proceed in deleting the
            //  appointment from the database, this operation cannot be undone.
            Optional<ButtonType> result = displayAlert("Confirmation", "Alert",
                    "Click OK to confirm deleting the selected appointment?");

            if (result.isPresent() && result.get() == ButtonType.OK) {

                try {
                    JAppointmentDAO.jApptDeleteByID(highlightedAppointment.getJAppointmentID());
                }
                catch(SQLException e) {
                    errorDBConn(e.getMessage());
                }
                displayAlert("Information", "Information", "Appointment ID " +
                        highlightedAppointment.getJAppointmentID() +
                        " : " + highlightedAppointment.getJAppointmentTitle() +
                        " of type " + highlightedAppointment.getJAppointmentType() +
                        " has been cancelled from the system.");

                //  after the appointment has been deleted, bring the user back to the main appointment
                //  screen and refresh the appointment table grid.
                if (rdoAllAppts.isSelected()) {
                    try {
                        showJAllAppointments();
                    }
                    catch(SQLException e) {
                        errorDBConn(e.getMessage());
                    }
                } else if (rdoApptByMonth.isSelected()) {
                    try {
                        showJAppointmentsByMonth();
                    }
                    catch(SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (rdoApptByWeek.isSelected()) {
                    try {
                        showJAppointmentsByWeek();
                    }
                    catch(SQLException e) {
                        errorDBConn(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * <p>The <b>btnExitClicked Method</b> exits the application and returns back the user to the calling
     * operating system or IDE.</p>
     * @param event <p>This is the event triggered when the user clicks on the Exit button.</p>
     */
    @FXML public void btnExitClicked(ActionEvent event) {
        System.exit(0);
    }

    /**
     * <p>The <b>onDoubleClicked Method</b>  provides the logic when the user double-clicks on an Appointment record
     * inside the grid. The selected Appointment record will be loaded in the Modify Appointment Screen Form
     * for editing.</p>
     * @param mouseEvent <p>The mouse event triggered when the user double-clicks on an appointment record
     *                   inside the grid </p>
     * @throws IOException <p>The error thrown by JAppointmentDAO Class (getSelectedItem).</p>
     */
    @FXML public void onDoubleClicked(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                //  check if there is an appointment selected
                highlightedAppointment = tblViewAppointments.getSelectionModel().getSelectedItem();

                //  if there is no appointment selected, fire up an error message
                if (highlightedAppointment == null) {
                    displayAlert("Error", "Delete Error",
                            "You must select an appointment from the list");
                } else {

                    //  an appointment was selected, pass the selection to the modify appointment screen.
                    stage = (Stage)((TableView)mouseEvent.getSource()).getScene().getWindow();
                    displayWindow(stage, "/teoespero/jappointment/Screens/ModifyAppointmentScre.fxml",
                            "Appointment Management System (AMS) - Appointments");
                }
            }
        }
    }

    /**
     * <p>The <b>btnModifyAppointmentClicked Method</b> allows the user to modify an existing appointment from
     * the grid. The user must first select an appointment prior to clicking the Modify appointnment button.</p>
     * @param event <p>The event triggered when the user clicks on the Modify Appointment button.</p>
     * @throws IOException <p>The error thrown by the JAppointmentDAO (getSelectedItem) method.</p>>
     */
    @FXML public void btnModifyAppointmentClicked(ActionEvent event) throws IOException {

        //  check if there is an appointment selected
        highlightedAppointment = tblViewAppointments.getSelectionModel().getSelectedItem();

        //  if there is no appointment selected, fire up an error message
        if (highlightedAppointment == null) {
            displayAlert("Error", "Delete Error",
                    "You must select an appointment from the list");
        } else {

            //  an appointment was selected, pass the selection to the modify appointment screen.
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            displayWindow(stage, "/teoespero/jappointment/Screens/ModifyAppointmentScre.fxml",
                    "Appointment Management System (AMS) - Appointments");
        }
    }

    /**
     * <p>the <b>rdoApptByMonthAction Method</b> provides logic when the user selects the Search
     * Appointments by month</p>
     * @param event <p>The event triggered when the Month radio button option is selected by the user. </p>
     */
    @FXML public void rdoApptByMonthAction(ActionEvent event) {

        try {
            showJAppointmentsByMonth();
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
    }

    /**
     * <p>The <b>btnNewAppointmentClicked Method</b> allows the user to create new appointments within the system.</p>
     * @param event <p>This is the event triggered when the user clicks on the New Appointment button.</p>
     * @throws IOException <p>The JavaFX error thrown.</p>
     */
    @FXML public void btnNewAppointmentClicked(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/NewAppointmentScre.fxml",
                "Appointment Management System (AMS) - New Appointment");
    }

    /**
     * <p>The <b>btnReportsClicked Method</b> provides the user access to system-generated reports.</p>
     * @param event <p>The event triggered when the user clicks on the Reports button.</p>
     * @throws IOException <p>The error thrown by JavaFX when it encounters an error.</p>
     */
    @FXML public void btnReportsClicked(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/ReportsScre.fxml",
                "Appointment Management System (AMS) - Reports");
    }

    /**
     * <p>The <b>keyEnterPressed Method</b> provides the logic when the user presses the ENTER key while inside the
     * Appointments Search Box. This will trigger the Search Button (btnSearchClicked) Method.</p>
     * @param keyEvent <p>The event triggered when the user presses on the ENTER key while inside the Search Box.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentsDAO Class (showJAllAppointments).</p>
     */
    @FXML public void keyEnterPressed(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            if (rdoAllAppts.isSelected()) {
                showJAllAppointments();
            } else if (rdoApptByMonth.isSelected()) {
                showJAppointmentsByMonth();
            } else if (rdoApptByWeek.isSelected()) {
                showJAppointmentsByWeek();
            }
        }
    }

    /**
     * <p>The <b>btnSearchClicked Method</b> provides the logic when the Search button is clicked. Depending on which
     * radio button is selected, the Search button will act accordingly by calling the appropriate method.</p>
     * @param event <p>The event triggered when clicking on the Search button.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentsDAO Class (showJAllAppointments).</p>
     */
    @FXML public void btnSearchClicked(ActionEvent event) throws SQLException {

        if (rdoAllAppts.isSelected()) {
            showJAllAppointments();
        } else if (rdoApptByMonth.isSelected()) {
            showJAppointmentsByMonth();
        } else if (rdoApptByWeek.isSelected()) {
            showJAppointmentsByWeek();
        }
    }

    /**
     * <p>The <b>rdoApptByWeekAction Method</b> display appointments a week from the current system date.</p>
     * @param event <p>The event triggered when the Week radio button is selected.</p>
     */
    @FXML public void rdoApptByWeekAction(ActionEvent event) {

        try {
            showJAppointmentsByWeek();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>showJAllAppointments Method</b> provides the logic when the All radio button is selected.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentsDAO Class (showJAllAppointments).</p>
     */
    public void showJAllAppointments() throws SQLException {
        tblViewAppointments.refresh();
        try {
            ObservableList<Appointment> obsClientAppointments = JAppointmentDAO.jApptSelectAll();
            ObservableList<Appointment> JClientAppointments = FXCollections.observableArrayList();

            //  loop through the DB and copy the records
            for (int i = 0, obsClientAppointmentsSize = obsClientAppointments.size();
                 i < obsClientAppointmentsSize; i++) {
                Appointment appointment = obsClientAppointments.get(i);

                AppointmentDOM newAppointment = new AppointmentDOM(appointment);
                JClientAppointments.add(newAppointment);
            }

            //  base of the appointment search on the contents of the Search box
            String searchAppointmentKey = txtSearchBox.getText();

            if (!"".equals(searchAppointmentKey)) {

                //  define the vars that will hold the record's column values
                ObservableList<Appointment> JFilteredAppointments = FXCollections.observableArrayList();
                String appointmentTitle;
                String appointmentDescription;
                String appointmentLocation;
                String appointmentContact;
                String appointmentType;

                //  loop through each record and check if a match exist
                for (int i = 0, jClientAppointmentsSize = JClientAppointments.size();
                     i < jClientAppointmentsSize; i++) {

                    //  get the appointment record
                    Appointment appointment = JClientAppointments.get(i);

                    //  get the appointment title
                    appointmentTitle = appointment.getJAppointmentTitle();

                    //  get the appointment description
                    appointmentDescription = appointment.getJAppointmentDescription();

                    //  get the appointment location
                    appointmentLocation = appointment.getJAppointmentLocation();

                    //  get the appointment contact
                    appointmentContact = JContactDAO.jContactSelectByID(appointment.getJAppointmentContactID()).getJAppointmentContactName();

                    //  get the appointment type
                    appointmentType = appointment.getJAppointmentType();

                    //  check if there's a match in the appointment title
                    if ((appointmentTitle.contains(searchAppointmentKey)) || (appointmentDescription.contains(searchAppointmentKey)) || (appointmentLocation.contains(searchAppointmentKey)) ||  (appointmentContact.contains(searchAppointmentKey)) || (appointmentType.contains(searchAppointmentKey))) {
                        appointment.setJAppointmentStartTime(convertFromUtc(appointment.getJClientAppointmentStartTime()));
                        appointment.setJAppointmentEndTime(convertFromUtc(appointment.getJAppointmentEndTime()));
                        JFilteredAppointments.add(appointment);
                    }
                }
                tblViewAppointments.setItems(JFilteredAppointments);

                //  if there is only one (1) search result, highlight it for the user
                if (JFilteredAppointments.size() == 1){
                    tblViewAppointments.getSelectionModel().clearSelection();
                    tblViewAppointments.setItems(JFilteredAppointments);
                    tblViewAppointments.requestFocus();
                    tblViewAppointments.getSelectionModel().selectFirst();
                    tblViewAppointments.getFocusModel().focus(0);

                }
            } else {
                //  otherwise display all of it
                tblViewAppointments.setItems(JClientAppointments);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

    }

    /**
     * <p>The <b>showJAppointmentsByWeek Method</b> provides the logic when the Week radio button is selected.
     * This displays all the appointments one (1) week from the system date.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentsDAO Class (showJAppointmentsByWeek).</p>
     */
    public  void showJAppointmentsByWeek() throws SQLException {
        tblViewAppointments.refresh();
        try {
            ObservableList<Appointment> obsClientAppointments = JAppointmentDAO.jApptSelectAll();
            ObservableList<Appointment> JClientAppointments = FXCollections.observableArrayList();

            //  find the current system date
            LocalDate todayDate = LocalDate.now();
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDateTime todayMidnight = LocalDateTime.of(todayDate, midnight);

            //  determine next week
            LocalDateTime oneWeek = todayMidnight.plusWeeks(1);

            //  loop through the DB and copy the records if the record is within a week from the current system date
            for (int i = 0, obsClientAppointmentsSize = obsClientAppointments.size(); i < obsClientAppointmentsSize; i++) {
                Appointment appointment = obsClientAppointments.get(i);

                //  loop through each record and check if a match exist
                if (appointment.getJClientAppointmentStartTime().isAfter(todayMidnight)
                        && appointment.getJClientAppointmentStartTime().isBefore(oneWeek)) {
                    AppointmentDOM newAppointment = new AppointmentDOM(appointment);
                    JClientAppointments.add(newAppointment);
                }
            }

            //  base of the appointment search on the contents of the Search box
            String searchAppointmentKey = txtSearchBox.getText();

            if (!"".equals(searchAppointmentKey)) {

                //  define the vars that will hold the record's column values
                ObservableList<Appointment> JFilteredAppointments = FXCollections.observableArrayList();
                String appointmentTitle;
                String appointmentDescription;
                String appointmentLocation;
                String appointmentContact;
                String appType;

                //  loop through each record and check if a match exist
                for (int i = 0, jClientAppointmentsSize = JClientAppointments.size(); i < jClientAppointmentsSize; i++) {

                    //  get the appointment record
                    Appointment appointment = JClientAppointments.get(i);

                    //  get the appointment title
                    appointmentTitle = appointment.getJAppointmentTitle();

                    //  get the appointment description
                    appointmentDescription = appointment.getJAppointmentDescription();

                    //  get the appointment contact
                    appointmentLocation = appointment.getJAppointmentLocation();

                    //  get the appointment location
                    appointmentContact = JContactDAO.jContactSelectByID(appointment.getJAppointmentContactID()).getJAppointmentContactName();

                    //  get the appointment type
                    appType = appointment.getJAppointmentType();

                    //  check if there's a match in the appointment title
                    if ((appointmentTitle.contains(searchAppointmentKey)) || (appointmentDescription.contains(searchAppointmentKey)) || (appointmentLocation.contains(searchAppointmentKey)) ||  (appointmentContact.contains(searchAppointmentKey)) || (appType.contains(searchAppointmentKey))) {
                        appointment.setJAppointmentStartTime(convertFromUtc(appointment.getJClientAppointmentStartTime()));
                        appointment.setJAppointmentEndTime(convertFromUtc(appointment.getJAppointmentEndTime()));
                        JFilteredAppointments.add(appointment);
                    }
                }
                tblViewAppointments.setItems(JFilteredAppointments);

                //  if there is only one (1) search result, highlight it for the user
                if (JFilteredAppointments.size() == 1){
                    tblViewAppointments.getSelectionModel().clearSelection();
                    tblViewAppointments.setItems(JFilteredAppointments);
                    tblViewAppointments.requestFocus();
                    tblViewAppointments.getSelectionModel().selectFirst();
                    tblViewAppointments.getFocusModel().focus(0);

                }
            } else {
                tblViewAppointments.setItems(JClientAppointments);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>showJAppointmentsByMonth Method</b> provides the logic when the Month radio button is selected.
     * This displays all the appointments one (1) month from the system date.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentsDAO Class (showJAppointmentsByMonth).</p>
     */
    public void showJAppointmentsByMonth() throws SQLException {
        tblViewAppointments.refresh();
        try {
            ObservableList<Appointment> obsClientAppointments = JAppointmentDAO.jApptSelectAll();
            ObservableList<Appointment> JClientAppointment = FXCollections.observableArrayList();

            //  find the current system date
            LocalDate currentSystemDate = LocalDate.now();
            LocalTime localMidnight = LocalTime.MIDNIGHT;
            LocalDateTime todayMidnight = LocalDateTime.of(currentSystemDate, localMidnight);

            //  determine next month
            LocalDateTime oneMonth = todayMidnight.plusMonths(1);

            //  loop through the DB and copy the records if the record is within a month from the current system date
            for (int i = 0, JClientAppointmentsSize = obsClientAppointments.size(); i < JClientAppointmentsSize; i++) {
                Appointment appointment = obsClientAppointments.get(i);

                //  loop through each record and check if a match exist
                if (appointment.getJClientAppointmentStartTime().isAfter(todayMidnight)
                        && appointment.getJClientAppointmentStartTime().isBefore(oneMonth)) {
                    AppointmentDOM newAppointment = new AppointmentDOM(appointment);
                    JClientAppointment.add(newAppointment);
                }
            }

            //  base of the appointment search on the contents of the Search box
            String searchAppointmentKey = txtSearchBox.getText();

            if (!"".equals(searchAppointmentKey)) {

                //  define the vars that will hold the record's column values
                ObservableList<Appointment> JFilteredAppointments = FXCollections.observableArrayList();
                String appointmentTitle;
                String appointmentDescription;
                String appointmentLocation;
                String appointmentContact;
                String appointmentType;

                //  loop through each record and check if a match exist
                for (int i = 0, appointmentsSize = JClientAppointment.size(); i < appointmentsSize; i++) {

                    //  get the appointment record
                    Appointment appointment = JClientAppointment.get(i);

                    //  get the appointment title
                    appointmentTitle = appointment.getJAppointmentTitle();

                    //  get the appointment description
                    appointmentDescription = appointment.getJAppointmentDescription();

                    //  get the appointment location
                    appointmentLocation = appointment.getJAppointmentLocation();

                    //  get the appointment contact
                    appointmentContact = JContactDAO.jContactSelectByID(appointment.getJAppointmentContactID()).getJAppointmentContactName();

                    //  get the appointment type
                    appointmentType = appointment.getJAppointmentType();

                    //  check if there's a match in the appointment title
                    if ((appointmentTitle.contains(searchAppointmentKey))
                            || (appointmentDescription.contains(searchAppointmentKey))
                            || (appointmentLocation.contains(searchAppointmentKey))
                            ||  (appointmentContact.contains(searchAppointmentKey))
                            || (appointmentType.contains(searchAppointmentKey))) {
                        appointment.setJAppointmentStartTime(convertFromUtc(appointment.getJClientAppointmentStartTime()));
                        appointment.setJAppointmentEndTime(convertFromUtc(appointment.getJAppointmentEndTime()));
                        JFilteredAppointments.add(appointment);
                    }
                }
                tblViewAppointments.setItems(JFilteredAppointments);

                //  if there is only one (1) search result, highlight it for the user
                if (JFilteredAppointments.size() == 1){
                    tblViewAppointments.getSelectionModel().clearSelection();
                    tblViewAppointments.setItems(JFilteredAppointments);
                    tblViewAppointments.requestFocus();
                    tblViewAppointments.getSelectionModel().selectFirst();
                    tblViewAppointments.getFocusModel().focus(0);

                }
            } else {
                tblViewAppointments.setItems(JClientAppointment);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
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

        //  optional return value
        Optional<ButtonType> result = alert.showAndWait();
        return result;
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

    LocalDateTime convertToUtc(LocalDateTime dateTime) {
        ZonedDateTime dateTimeInMyZone = ZonedDateTime.
                of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

    }

    LocalDateTime convertFromUtc(LocalDateTime utcDateTime){
        return ZonedDateTime.
                of(utcDateTime, ZoneId.of("UTC"))
                .toOffsetDateTime()
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * <p>The <b>getHighlightedAppointment Method</b> returns the highlighted appointment in the grid.</p>
     * @return <p>The selected row in the appointment in the grid.</p>
     */
    public static Appointment getHighlightedAppointment() {return highlightedAppointment;}

    /**
     * <p>The <b>initialize method</b> initializes the AppointmentScreController Class by pre-populating
     * the appointments grid.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @Override @FXML public void initialize(URL location, ResourceBundle resources) {
        tblViewAppointments.refresh();
        fldApptID.setCellValueFactory(new PropertyValueFactory<>("JAppointmentID"));
        fldTitle.setCellValueFactory(new PropertyValueFactory<>("JAppointmentTitle"));
        fldDescription.setCellValueFactory(new PropertyValueFactory<>("JAppointmentDescription"));
        fldLocation.setCellValueFactory(new PropertyValueFactory<>("JAppointmentLocation"));
        fldContact.setCellValueFactory(new PropertyValueFactory<>("JAppointmentContactName"));
        fldType.setCellValueFactory(new PropertyValueFactory<>("JAppointmentType"));
        fldApptStart.setCellValueFactory(new PropertyValueFactory<>("JAppointmentStartTimeString"));
        fldApptEnd.setCellValueFactory(new PropertyValueFactory<>("JAppointmentEndTimeString"));
        fldCustomerID.setCellValueFactory(new PropertyValueFactory<>("JAppointmentCustomerID"));

        try {
            tblViewAppointments.refresh();
            showJAllAppointments();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }

        rdoAllAppts.setSelected(true);
    }
}