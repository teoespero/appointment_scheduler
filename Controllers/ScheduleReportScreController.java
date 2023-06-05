package teoespero.jappointment.Controllers;

import javafx.scene.control.Button;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.Model.AppointmentDOM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * <p>The <b>ScheduleReportScreController Class</b> is a system-generated report that shows information
 * about appointments sorted by Contact Name.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class ScheduleReportScreController implements Initializable {

    //  define the GUI elements of the reports screen. Most of the elements are
    //  defined as private, this is to define the elements for use only within the class.

    //  Defining the window
    @FXML Stage stage;
    @FXML Parent scene;

    //  Defining the report grid
    @FXML private TableView<Appointment> grdReport;
    @FXML private TableColumn<Appointment, Integer> fldApptID;
    @FXML private TableColumn<Appointment, String> fldApptTitle;
    @FXML private TableColumn<Appointment, String> fldApptType;
    @FXML private TableColumn<Appointment, String> fldApptDescription;
    @FXML private TableColumn<Appointment, String> fldApptContact;
    @FXML private TableColumn<Appointment, String> fldApptStart;
    @FXML private TableColumn<Appointment, String> fldApptEnd;
    @FXML private TableColumn<Appointment, Integer> fldApptCustomerID;

    //  define vars that make the form function
    @FXML private Label timestampLabel;
    @FXML private final DateTimeFormatter patternDateTime = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    /**
     * <p>The <b>generateJReport Method</b> provides logic on displaying the information on the appointments
     * by contact name</p>
     */
    @FXML public void generateJReport() {

        try {
            ObservableList<Appointment> obsAppointments = JAppointmentDAO.jApptSelectAllOrderByContact();
            ObservableList<Appointment> JClientAppointments = FXCollections.observableArrayList();

            int i = 0, totalAppointmentsSize = obsAppointments.size();
            while (i < totalAppointmentsSize) {
                Appointment appointment = obsAppointments.get(i);

                AppointmentDOM newAppointment = new AppointmentDOM(appointment);
                JClientAppointments.add(newAppointment);
                i++;
            }

            grdReport.setItems(JClientAppointments);
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>btnCloseClicked Method</b> brings the user back to the Reports Screen.</p>
     * @param event <p>The event triggered when the Close button is clicked.</p>
     * @throws IOException <p>The error thrown by JavaFX when an error is encountered.</p>
     */
    @FXML public void btnCloseClicked(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/ReportsScre.fxml",
                "Appointment Management System (AMS) - Reports Screen");
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
     * <p>The <b>errorDBConn Method</b> provides a simple mechanism for handling DB connection errors.</p>
     * @param errorMsg <p>The error message returned connecting to the database.</p>
     */
    public static void errorDBConn(String errorMsg){
        System.out.println("DB Connection Error: " + errorMsg);
        System.out.println("JAppointment Application will now shutdown.");
        System.exit(0);
    }


    /**
     * <p>The <b>initialize method</b> initializes the ModifyCustomerScreController Class by pre-populating
     * the form.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @Override @FXML public void initialize(URL location, ResourceBundle resources) {

        fldApptID.setCellValueFactory(new PropertyValueFactory<>("JAppointmentID"));
        fldApptTitle.setCellValueFactory(new PropertyValueFactory<>("JAppointmentTitle"));
        fldApptDescription.setCellValueFactory(new PropertyValueFactory<>("JAppointmentDescription"));
        fldApptContact.setCellValueFactory(new PropertyValueFactory<>("JAppointmentContactName"));
        fldApptType.setCellValueFactory(new PropertyValueFactory<>("JAppointmentType"));
        fldApptStart.setCellValueFactory(new PropertyValueFactory<>("JAppointmentStartTimeString"));
        fldApptEnd.setCellValueFactory(new PropertyValueFactory<>("JAppointmentEndTimeString"));
        fldApptCustomerID.setCellValueFactory(new PropertyValueFactory<>("JAppointmentCustomerID"));

        timestampLabel.setText(LocalDateTime.now().format(patternDateTime));

        generateJReport();
    }
}
