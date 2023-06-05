package teoespero.jappointment.Controllers;

import javafx.scene.control.Button;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.Model.MonthlyTypeReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
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
 * <p>The <b>MonthByTypeReportScreController Class</b> provides the logic needed in displaying a form for a system
 * generated report that provides information on appointments per month based on appointment type.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class MonthByTypeReportScreController implements Initializable {

    //  Define the GUI elements that we need for the Monthly Appointments By Type Report.
    //  Most of the elements are defined as private, this is to define the elements
    //  for use only within the class.
    @FXML Stage stage;
    @FXML Parent scene;

    //  the elements below are used to define the report's grid
    @FXML private TableView<MonthlyTypeReport> grdReport;
    @FXML private TableColumn<MonthlyTypeReport, Integer> fldApptYear;
    @FXML private TableColumn<MonthlyTypeReport, Month> fldApptMonth;
    @FXML private TableColumn<MonthlyTypeReport, String> fldApptType;
    @FXML private TableColumn<MonthlyTypeReport, Integer> fldApptCount;

    //  the data elements below are used to display information as to when the report was generated
    @FXML private Label timestampLabel;
    private final DateTimeFormatter patternDateTime = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    /**
     * <p>The <b>generateJReport Method</b> provides the logic and control needed to display information on the report
     * grid. The method makes of Lambda expressions which makes the code more compact and simpler in expressing
     * their logic.</p>
     */
    public void generateJReport() {

        try {
            ObservableList<Integer> years = FXCollections.observableArrayList();
            ObservableList<Month> months = FXCollections.observableArrayList();
            ObservableList<String> types = FXCollections.observableArrayList();
            ObservableList<Appointment> obsAppointments = JAppointmentDAO.jApptSelectAll();
            ObservableList<MonthlyTypeReport> reportList = FXCollections.observableArrayList();

            //  lambda expression -> get unique appointment years
            obsAppointments.forEach(appointment -> {
                        Integer year = appointment.getJClientAppointmentStartTime().getYear();
                        if (years.contains(year)) {
                            return;
                        }
                        years.add(year);
                    }
            );

            //  lambda expression -> get unique appointment months
            obsAppointments.forEach(appointment -> {
                        Month month = appointment.getJClientAppointmentStartTime().getMonth();
                        if (months.contains(month)) {
                            return;
                        }
                        months.add(month);
                    }
            );

            //  lambda expression -> get unique appointment type
            obsAppointments.forEach(appointment -> {
                        String type = appointment.getJAppointmentType();
                        if (types.contains(type)) {
                            return;
                        }
                        types.add(type);
                    }
            );

            Integer appointmentYear;
            Month appointmentMonth;
            String appointmentType;
            Appointment appointmentLooper;
            int appointmentTypeCount;

            int i = 0;
            while (i < years.size()) {
                appointmentYear = years.get(i);
                int j = 0;
                while (j < months.size()) {
                    appointmentMonth = months.get(j);
                    int k = 0;
                    while (k < types.size()) {
                        appointmentType = types.get(k);
                        appointmentTypeCount = 0;
                        int l = 0;
                        while (l < obsAppointments.size()) {
                            appointmentLooper = obsAppointments.get(l);
                            if (appointmentLooper.getJClientAppointmentStartTime().getYear() != appointmentYear
                                    || !appointmentLooper.getJClientAppointmentStartTime().getMonth().equals(appointmentMonth)
                                    || !appointmentLooper.getJAppointmentType().equals(appointmentType)) {
                            } else {

                                appointmentTypeCount++;
                            }

                            l++;
                        }
                        if (appointmentTypeCount <= 0) {
                        } else {
                            MonthlyTypeReport reportObject = new MonthlyTypeReport(appointmentYear,
                                    appointmentMonth, appointmentType, appointmentTypeCount);
                            reportList.add(reportObject);
                        }
                        k++;
                    }
                    j++;
                }
                i++;
            }
            grdReport.setItems(reportList);
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>btnCloseClicked Method</b> brings the user back to the Reports screen.</p>
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
        fldApptYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        fldApptMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        fldApptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        fldApptCount.setCellValueFactory(new PropertyValueFactory<>("total"));

        timestampLabel.setText(LocalDateTime.now().format(patternDateTime));

        generateJReport();
    }
}