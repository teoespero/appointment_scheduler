package teoespero.jappointment.Controllers;

import javafx.scene.control.*;
import teoespero.jappointment.DataAccessObjects.JCountryDAO;
import teoespero.jappointment.DataAccessObjects.JCustomerDAO;
import teoespero.jappointment.DataAccessObjects.JDivisionDAO;
import teoespero.jappointment.Model.Country;
import teoespero.jappointment.Model.Customer;
import teoespero.jappointment.Model.Division;
import teoespero.jappointment.Model.User;
import teoespero.jappointment.DBHelper.DBCheckCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>The <b>NewCustomerScreController Class</b> provides the logic and controls needed to add new customers
 * into the database.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class NewCustomerScreController implements Initializable {

    //  Define the GUI elements that we need for the Appointments form. Most of the elements are
    //  defined as private, this is to define the elements for use only within the class.

    //  Define the window
    @FXML Stage stage;
    @FXML Parent scene;

    //  The fields where the user can enter the customer details
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtCustomerAddress;
    @FXML private TextField txtCustomerZipCode;
    @FXML private TextField txtCustomerTelNo;
    @FXML private ComboBox<Country> cboCustomerCountry;
    @FXML private ComboBox<Division> cboCustomerDivision;

    //  vars that will hold data essential to the forms function
    private ObservableList<Country> lstCountry = FXCollections.observableArrayList();
    private User currentSessionUser = LoginScreController.getCurrentJAppointmentUser();

    /**
     * <p>The <b>btnSaveClicked Method</b> provides the logic and functions needed to save the information
     * entered by the user about a customer.</p>
     * @param event <p>The event triggered when the user clicks on the Save button.</p>
     * @throws IOException <p>The error thrown by the JAppointmentDAO Class (jCustomerCreate).</p>
     */
    @FXML public void btnSaveClicked(ActionEvent event) throws IOException {

        try {
            String customerName = txtCustomerName.getText();
            String address = txtCustomerAddress.getText();
            String postalCode = txtCustomerZipCode.getText();
            String phone = txtCustomerTelNo.getText();
            String createdBy = currentSessionUser.getJAppointmentUserName();
            String lastUpdatedBy = currentSessionUser.getJAppointmentUserName();
            int divisionID = cboCustomerDivision.getSelectionModel().getSelectedItem().getDivisionID();

            Customer newCustomer = new Customer(customerName, address, postalCode, phone, createdBy,
                    lastUpdatedBy, divisionID);

            if (DBCheckCustomer.fieldsEmpty(newCustomer)) {
            } else {

                JCustomerDAO.jCustomerCreate(newCustomer);

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                displayWindow(stage, "/teoespero/jappointment/Screens/CustomersScre.fxml",
                        "JAppointment-Customers");
            }
        }
        catch(NullPointerException e) {
            displayAlert("Error","Error", "One or more fields are not selected.");
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>btnCancelClicked Method</b> returns the user back to the Customer screen.</p>
     * @param event <p>The event triggered when the user clicks on the Cancel button.</p>
     * @throws IOException <p>The error thrown by JavaFX when it encounters an error.</p>
     */
    @FXML public void btnCancelClicked(ActionEvent event) throws IOException {
        Optional<ButtonType> result = displayAlert("Confirmation", "Alert",
                "Click OK to cancel changes and to the Customers screen?");

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/CustomersScre.fxml",
                "JAppointment-Customers");
    }

    /**
     * <p>The <b>cboCountryClicked Method</b> allows the user to choose the country where the customer is from. The value
     * defined in this field defines the Division.</p>
     * @param event <p>The event triggered when the user selects the drop-down combo.</p>
     */
    @FXML public void cboCountryClicked(ActionEvent event) {

        try {
            Country selectedCountry = cboCustomerCountry.getSelectionModel().getSelectedItem();
            ObservableList<Division> dbDivisions = JDivisionDAO.JDivisionSelectAll();
            ObservableList<Division> divisionByCountry = FXCollections.observableArrayList();

            for (int i = 0, dbDivisionsSize = dbDivisions.size(); i < dbDivisionsSize; i++) {
                Division division = dbDivisions.get(i);
                if (division.getCountryID() == selectedCountry.getCountryID()) {
                    divisionByCountry.add(division);
                }
            }

            cboCustomerDivision.setItems(divisionByCountry);
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
    @FXML public void displayWindow(Stage stage, String windowName, String windowTitle) throws IOException {
        scene = FXMLLoader.load(getClass().getResource(windowName));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
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
        if (alertType.equals("Information")){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        if (alertType.equals("Error")){
            alert = new Alert(Alert.AlertType.ERROR);
        }
        if (alertType.equals("Confirmation")){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        }
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }


    /**
     * <p>The <b>setNewCustDefaultValues Method</b> provides logic and controls that defines the default values of
     * certain fields.
     * <ul>
     *     <li>Country</li>
     * </ul></p>
     */
    @FXML private void setNewCustDefaultValues() {

        try {
            lstCountry = JCountryDAO.jCountrySelectAll();
            cboCustomerCountry.setItems(lstCountry);
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
     * <p>The <b>initialize method</b> initializes the AppointmentScreController Class by pre-populating
     * the appointments grid.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @FXML @Override public void initialize(URL location, ResourceBundle resources) {setNewCustDefaultValues();}
}