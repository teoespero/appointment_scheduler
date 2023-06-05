package teoespero.jappointment.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * <p>The <b>ReportsScreController Method</b> provides an interface for the user to access the different
 * system-generated reports.</p>
 * <br><br>Types of reports:
 * <ul>
 *     <li>Appointment Count by month and type</li>
 *     <li>Appointment by contact schedule</li>
 *     <li>Appointments by location</li>
 * </ul>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class ReportsScreController {

    //  Define the GUI elements that we need for the Appointments form. Most of the elements are
    //  defined as private, this is to define the elements for use only within the class.

    //  Define the window
    @FXML Stage stage;
    @FXML Parent scene;

    //  Define elements of the reports group
    @FXML private RadioButton monthRadioButton;
    @FXML private ToggleGroup radioButtons;
    @FXML private RadioButton scheduleRadioButton;
    @FXML private RadioButton locationRadioButton;

    /**
     * <p>The <b>btnGenerateReportClicked Method</b> provides the logic that loads the different reports.</p>
     * @param actionEvent <p>The event triggered when the Generate Report button is clicked.</p>>
     * @throws IOException <p>The error thrown by JavaFX when an error is encountered.</p>
     */
    public void btnGenerateReportClicked(ActionEvent actionEvent) throws IOException {
        if (!monthRadioButton.isSelected()) {
            if (scheduleRadioButton.isSelected()) {
                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                displayWindow(stage, "/teoespero/jappointment/Screens/ScheduleReportScre.fxml",
                        "JAppointment-Contact Schedule Report");
            } else if (locationRadioButton.isSelected()) {
                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                displayWindow(stage, "/teoespero/jappointment/Screens/LocationReportScre.fxml",
                        "JAppointment-Appointments by Location Report");
            }
        } else {
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            displayWindow(stage, "/teoespero/jappointment/Screens/MonthByTypeReportScre.fxml",
                    "JAppointment-Appointment Count by Month and Type Report");
        }
    }

    /**
     * <p>The <b>btnBackClick Method</b> brings the user back to the Appointment Screen.</p>
     * @param event <p>The event triggered when the user clicks on the Back button.</p>
     * @throws IOException <p>The error thrown by JavaFX when an error is encountered.</p>
     */
    public void btnBackClick(ActionEvent event) throws IOException {
        System.out.println("backButton clicked...");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml",
                "JAppointment-Appointments");
    }

    /**
     * <p>The <b>displayWindow Method</b> is a user-defined method that is used within the application to load the
     * different windows used.</p>
     * @param stage <p>The top-level JavaFX container in our JAppointment application.</p>
     * @param windowName <p>The relative location of the FXML file to be used.</p>
     * @param windowTitle <p>The title of the FXML file.</p>
     * @throws IOException <p>The error thrown by JavaFX if it encounters an error when loading the form.</p>
     */
    private void displayWindow(Stage stage, String windowName, String windowTitle) throws IOException {
        scene = FXMLLoader.load(getClass().getResource(windowName));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }
}
