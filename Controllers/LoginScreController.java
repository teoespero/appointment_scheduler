package teoespero.jappointment.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import teoespero.jappointment.DBHelper.DBSessionLogger;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.DataAccessObjects.JUserDAO;
import teoespero.jappointment.Main;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * <p>The <b>LoginScreController Class</b> provides the logic for validating the username and password
 * that will be using the Appointment System. The login form can be displayed in either English or French
 * depending on the computer's language settings.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 1.05292023
 */
public class LoginScreController implements Initializable {

    //  Below are the JavaFX objects we are using in the form
    @FXML Stage stage;
    @FXML Parent scene;
    @FXML public Label lblUsername;
    @FXML public TextField txtUsername;
    @FXML public Label lblPassword;
    @FXML public PasswordField txtPassword;
    @FXML public Label lblZoneID;
    @FXML public Label lblTimeZoneSetting;
    @FXML public Button btnLogin;
    @FXML public Button btnCancel;

    //  Below are the object resources that we will be using to
    //  capture local machine settings

    //  capture the default language defined in the system
    Locale machineLocaleSettings = Locale.getDefault();

    //  capture the default time zone settings in the system
    private TimeZone machineTimeZoneSettings = Main.getUserTimeZone();

    //  define the language to be used in the application
    ResourceBundle resBundle = ResourceBundle.getBundle("/teoespero/jappointment/languages/lang", 
            machineLocaleSettings);

    //  store the username using the application
    public static User currentJAppointmentUser;

    //  pattern used by data for the date/time
    private final DateTimeFormatter patternDateTime = DateTimeFormatter.ofPattern("MM-dd-YYYY HH:mm");

    /**
     * <p>The <b>btnCancelClicked method</b> provides the logic when the cancel button in the login form is clicked. 
     * The application closes all the connections to the database and exits back to the operating system. </p>
     * @param actionEvent
     */
    @FXML public void btnCancelClicked(ActionEvent actionEvent) {
        System.out.println("cancelButton clicked...");
        System.exit(0);
    }

    /**
     * <p>The <b>btnLoginClicked Method</b> checks if the user has access to the system and displays an error
     * if they dont.</p>
     * @param event <p>The event triggered when the Login button is clicked.</p>
     * @throws IOException <p>The error thrown by JavaFX when an issue occurs opening the main appointment form.</p>
     * @throws SQLException <p>The error thrown by JAppointmentDAO Class (boolUserIsValid).</p>
     */
    @FXML public void btnLoginClicked(ActionEvent event) throws IOException, SQLException {
        String username=null;
        String password=null;
        try{
            username = txtUsername.getText();
            password = txtPassword.getText();
            if (username.isEmpty()){
                displayAlert("Error", resBundle.getString("lblError"),
                        resBundle.getString("txtErrorMessage2"));
            } else {
                currentJAppointmentUser = JUserDAO.jUserSelectByName(username);
                if (currentJAppointmentUser.getUserName()==null){
                    displayAlert("Error", resBundle.getString("lblError"),
                            resBundle.getString("txtErrorMessage4"));
                    DBSessionLogger.logJAppointmentSession(4, username);
                }else{
                    if (password.isEmpty()){
                        displayAlert("Error", resBundle.getString("lblError"),
                                resBundle.getString("txtErrorMessage3"));
                    }else {
                        if (currentJAppointmentUser.getPassword() != null && currentJAppointmentUser.getPassword().equals(password)){
                            DBSessionLogger.logJAppointmentSession(1, username);
                            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                            displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml",
                                    "Appointment Management System (AMS) - Appointments");
                            appointmentReminder();
                        }else{
                            displayAlert("Error", resBundle.getString("lblError"),
                                    resBundle.getString("txtErrorMessage4"));
                            DBSessionLogger.logJAppointmentSession(0, username);
                        }
                    }


                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * <p>The <b>boolUserIsValid Method</b> checks if the username/password entered is the same information
     * stored in the database.</p>
     * @return <p>boolUserIsOK, returns either TRUE or FALSE.</p>
     * @throws SQLException <p>The error thrown by JAppointmentDAO Class (boolUserIsValid).</p>
     */
    public Boolean boolUserIsValid() throws SQLException {

        Boolean boolUserIsOK = false;

        try {

            String username = txtUsername.getText();
            String password = txtPassword.getText();
            currentJAppointmentUser = JUserDAO.jUserSelectByName(username);

            if (currentJAppointmentUser.getPassword() != null) {
                if (currentJAppointmentUser.getPassword().equals(password)) {
                    boolUserIsOK = true;
                }
            } else {
                //  Do nothing, because there is nothing to execute
                //  and the default value returned will be false
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return boolUserIsOK;
    }

    /**
     * <p>The <b>defineMachineLanguageSettings Method</b> "translates" elements of the login page to a different
     * language (EN, FR) depending on the language settings defined on the local machine.</p>
     */
    public void defineMachineLanguageSettings() {

        lblUsername.setText(resBundle.getString("lblUsername"));
        lblPassword.setText(resBundle.getString("lblPassword"));
        lblTimeZoneSetting.setText(resBundle.getString("lblTimeZone"));
        btnCancel.setText(resBundle.getString("btnCancel"));
        btnLogin.setText(resBundle.getString("btnLogin"));
    }

    /**
     * <p>The <b>defineMachineTimeZoneSettings Method</b> defines the Time/Zone that the local machine is set to.</p>
     */
    public void defineMachineTimeZoneSettings() {lblZoneID.setText(machineTimeZoneSettings.getID());}

    /**
     * <p>The <b>getCurrentJAppointmentUser Method</b> returns the current session user and uses that information for
     * the activity log file.</p>
     * @return <p>Returns the current session user (currentJAppointmentUser).</p>
     */
    public static User getCurrentJAppointmentUser() {return currentJAppointmentUser;}

    /**
     * <p>The <b>appointmentReminder Method</b> looks ahead for any appointments that will start in the next
     * 15 minutes from the curremy system time.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class (jApptSelectAll).</p>
     */
    @FXML public void appointmentReminder() throws SQLException {

        try {
            boolean upcomingScheduledAppointment = false;
            ObservableList<Appointment> dbAppointments = JAppointmentDAO.jApptSelectAll();
            LocalDateTime now = LocalDateTime.now();
            int currentUserID = currentJAppointmentUser.getJAppointmentUserID();

            for (int i = 0, dbAppointmentsSize = dbAppointments.size(); i < dbAppointmentsSize; i++) {
                Appointment appointment = dbAppointments.get(i);

                LocalDateTime startTime = appointment.getJClientAppointmentStartTime();

                if ((appointment.getJAppointmentUserID() == currentUserID)
                        && startTime.isAfter(now)
                        && startTime.isBefore(now.plusMinutes(15))) {
                    displayAlert("Information",
                            "Information",
                            "Appointment ID "
                                    + appointment.getJAppointmentID()
                                    + " starts in " + startTime.format(patternDateTime) + ".");
                    upcomingScheduledAppointment = true;
                    break;
                }
            }

            if (!upcomingScheduledAppointment) {

                displayAlert("Information",
                        "Information",
                        "No appointments starting in the next 15 minutes.");
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
     */
    @FXML public void displayAlert(String alertType, String alertTitle, String alertHeader){
        Alert alert = null;
        if (alertType == "Information"){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        if (alertType == "Error"){
            alert = new Alert(Alert.AlertType.ERROR);
        }
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.showAndWait();

    }

    /**
     * <p>The <b>displayWindow Method</b> is a user-defined method that is used within the application to load the
     * different windows used.</p>
     * @param stage <p>The top-level JavaFX container in our JAppointment application.</p>
     * @param windowName <p>The relative location of the FXML file to be used.</p>
     * @param windowTitle <p>The title of the FXML file.</p>
     * @throws IOException <p>The error thrown by JavaFX if it encounters an error when loading the form.</p>
     */
    @FXML public void displayWindow(Stage stage, String windowName, String windowTitle) throws IOException {
        scene = FXMLLoader.load(getClass().getResource(windowName));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        Rectangle2D screBoundary = Screen.getPrimary().getVisualBounds();
        stage.setX((screBoundary.getWidth() - stage.getWidth()) / 2);
        stage.setY((screBoundary.getHeight() - stage.getHeight()) / 2);
        stage.show();
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
     * <p>The <b>initialize method</b> initializes the AppointmentScreController Class by pre-populating
     * the appointments grid.</p>
     * @param url <p>The location of the form that is to be initialized.</p>
     * @param resourceBundle <p>the relative location of the resources used by JavaFX.</p>
     */
    @Override @FXML public void initialize(URL url, ResourceBundle resourceBundle) {
        defineMachineLanguageSettings();
        defineMachineTimeZoneSettings();
    }
}
