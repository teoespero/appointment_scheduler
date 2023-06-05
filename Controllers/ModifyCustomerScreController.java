package teoespero.jappointment.Controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.stage.Screen;
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
 * <p>The <b>ModifyCustomerScreController Method</b> provides the logic needed to modify an existing customer record.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 1.05292023
 */
public class ModifyCustomerScreController implements Initializable {

    //  Define the GUI elements that we need for the Modify Customer Screen form.
    //  Most of the elements are defined as private, this is to define the elements
    //  for use only within the class.
    @FXML Stage stage;
    @FXML Parent scene;

    //  this field is auto-generated and cannot be edited
    //  this is displayed but cannot be traversed
    @FXML private TextField txtCustomerID;

    //  fields that the customer can edit
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtCustomerAddress;
    @FXML private TextField txtCustomerZipCode;
    @FXML private TextField txtCustomerTelNo;
    @FXML private ComboBox<Country> cboCustomerCountry;
    @FXML private ComboBox<Division> cboCustomerDivision;

    //  the buttons on the form
    @FXML public Button btnCancel;
    @FXML public Button btnSave;

    //  Define the following vars to hold data that we will need for the form
    private ObservableList<Country> lstCountry = FXCollections.observableArrayList(); // List for country for the country combo box.
    private User currentSessionUser = LoginScreController.getCurrentJAppointmentUser(); // Saves the currently logged-in user
    private Customer customerSelection; // The customer highlighted in the grid


    /**
     * <p>The <b>btnCancelClicked Method</b> brings the user back to the Customer screen.</p>
     * @param event <p>The event triggered when the Cancel button is clicked.</p>
     * @throws IOException <p>The error thrown by JavaFX when an error is encountered.</p>
     */
    @FXML public void btnCancelClicked(ActionEvent event) throws IOException {
        Optional<ButtonType> result = displayAlert("Confirmation", "Alert",
                "Click OK to cancel the changes and to go back to the Customers screen?");

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/CustomersScre.fxml",
                "JAppointment-Customers");
    }

    /**
     * <p>The <b>displayWindow Method</b> is a user-defined method that is used within the application to load the
     * different windows used.</p>
     * @param stage <p>The top-level JavaFX container in our JAppointment application.</p>
     * @param windowName <p>The relative location of the FXML file to be used.</p>
     * @param windowTitle <p>The title of the FXML file.</p>
     * @throws IOException <p>The error thrown by JavaFX if it encounters an error when loading the form.</p>
     */
    public void displayWindow(Stage stage, String windowName, String windowTitle) throws IOException {
        scene = FXMLLoader.load(getClass().getResource(windowName));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();

        //  the section below positions the window in the middle of the screen
        Rectangle2D screBoundary = Screen.getPrimary().getVisualBounds();
        stage.setX((screBoundary.getWidth() - stage.getWidth()) / 2);
        stage.setY((screBoundary.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * <p>The <b>cboCountryEvent Method</b> allows the user to choose the country where the customer is from. The value
     * defined in this field defines the Division.</p>
     * @param event <p>The event triggered when the user selects the drop-down combo.</p>
     */
    @FXML public void cboCountryEvent(ActionEvent event) {

        try {
            Country selectedCountry = cboCustomerCountry.getSelectionModel().getSelectedItem();
            ObservableList<Division> dbDivisions = JDivisionDAO.JDivisionSelectAll();
            ObservableList<Division> divisionByCountry = FXCollections.observableArrayList();

            for (int i = 0, dbDivisionsSize = dbDivisions.size(); i < dbDivisionsSize; i++) {
                Division division = dbDivisions.get(i);

                if (division.getCountryID() != selectedCountry.getCountryID()) {
                    continue;
                }
                divisionByCountry.add(division);
            }

            cboCustomerDivision.setItems(divisionByCountry);
            cboCustomerDivision.setValue(null);
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>btnSaveClicked Method</b> provides the logic needed to save any changes made to a customer record.
     * Note that any field that is left blank is flagged as an error.</p>
     * @param event <p>The event triggered when the Save button is clicked by the user.</p>
     * @throws IOException <p>The error thrown by the JAppointmentDAO Class (jCustomerUpdate).</p>
     */
    @FXML public void btnSaveClicked(ActionEvent event) throws IOException {

        try {
            String customerName = txtCustomerName.getText();
            String address = txtCustomerAddress.getText();
            String postalCode = txtCustomerZipCode.getText();
            String phone = txtCustomerTelNo.getText();
            String createdBy = customerSelection.getCreatedBy();
            String lastUpdatedBy = currentSessionUser.getJAppointmentUserName();
            int divisionID = cboCustomerDivision.getSelectionModel().getSelectedItem().getDivisionID();

            Customer newCustomer = new Customer(customerName, address, postalCode, phone, createdBy,
                    lastUpdatedBy, divisionID);

            newCustomer.setCustomerID(customerSelection.getJAppointmentCustomerID());

            if (DBCheckCustomer.fieldsEmpty(newCustomer)) {
            } else {

                JCustomerDAO.jCustomerUpdate(newCustomer);

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                displayWindow(stage, "/teoespero/jappointment/Screens/CustomersScre.fxml",
                        "JAppointment-Customers");
            }
        }
        catch(NullPointerException e) {
            displayAlert("Error", "Error", "Some fields are left blank.");
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
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * <p>The <b>setFormValues Method</b> captures the selected customer from the Customer Screen grid
     * and puts the data into the Modify Customer Screen.</p>
     */
    private void setFormValues() {

        customerSelection = CustomersScreController.getCustomerSelection();
        txtCustomerID.setText(String.valueOf(customerSelection.getJAppointmentCustomerID()));
        txtCustomerName.setText(customerSelection.getCustomerName());
        txtCustomerAddress.setText(customerSelection.getAddress());
        txtCustomerZipCode.setText(customerSelection.getPostalCode());
        txtCustomerTelNo.setText(customerSelection.getPhone());

        try {
            //Get selected division then country
            Division selectedDivision = JDivisionDAO.JDivisionSelectByID(customerSelection.getDivisionID());
            Country country = JCountryDAO.jCountrySelectByID(selectedDivision.getCountryID());

            //Set country list
            lstCountry = JCountryDAO.jCountrySelectAll();
            cboCustomerCountry.setItems(lstCountry);
            cboCustomerCountry.setValue(country);

            //Set division list
            ObservableList<Division> dbDivisions = JDivisionDAO.JDivisionSelectAll();
            ObservableList<Division> divisionByCountry = FXCollections.observableArrayList();

            for (int i = 0, dbDivisionsSize = dbDivisions.size(); i < dbDivisionsSize; i++) {
                Division division = dbDivisions.get(i);

                if (division.getCountryID() == country.getCountryID()) {
                    divisionByCountry.add(division);
                }
            }

            cboCustomerDivision.setItems(divisionByCountry);
            cboCustomerDivision.setValue(selectedDivision);
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
     * <p>The <b>initialize method</b> initializes the ModifyCustomerScreController Class by pre-populating
     * the form.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @Override @FXML public void initialize(URL location, ResourceBundle resources) {setFormValues();}
}