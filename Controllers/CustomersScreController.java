package teoespero.jappointment.Controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.DataAccessObjects.JCustomerDAO;
import teoespero.jappointment.Model.Appointment;
import teoespero.jappointment.Model.Customer;
import teoespero.jappointment.Model.CustomerDisplay;
import javafx.beans.property.SimpleStringProperty;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>The <b>CustomersScreController Class</b> provides logic and functionality for the maintaining the customer
 * tables in the appointment system. The functions include:</p>
 * <ul>
 *     <li>Find a customer.</li>
 *     <li>Add new customers.</li>
 *     <li>Modify existing customer records.</li>
 *     <li>Delete customer records, associated appointments will also be deleted.</li>
 * </ul>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 1.05292023
 */
public class CustomersScreController implements Initializable {

    //  Define the GUI elements that we need for the Customer form. Most of the elements are
    //  defined as private, this is to define the elements for use only within the class.
    @FXML Stage stage;
    @FXML Parent scene;

    //  Defining the Search elements
    @FXML private Button btnClear;
    @FXML private Button btnSearch;
    @FXML private TextField txtSearchBox;

    //  Defining the grid
    private static Customer customerSelection;
    @FXML private TableView<CustomerDisplay> tblCustomersGrid;
    @FXML private TableColumn<CustomerDisplay, String> fldCustomerName;
    @FXML private TableColumn<CustomerDisplay, String> fldCustomerAddress;
    @FXML private TableColumn<CustomerDisplay, String> fldCustomerZipCode;
    @FXML private TableColumn<CustomerDisplay, String> fldCustomerTelNo;
    @FXML private TableColumn<CustomerDisplay, String> fldCustomerDivision;

    //  define the buttons
    @FXML private Button btnNewCustomer;
    @FXML private Button btnModifyCustomer;
    @FXML private Button btnDeleteCustomer;
    @FXML private Button btnBack;


    /**
     * <p>The <b>btnBackClicked Method</b> returns the user back to the Appointment screen.</p>
     * @param event <p>The event triggered when the user clicks on the Back button.</p>
     * @throws IOException <p>The error thrown by JavaFX when it encounters an issue.</p>
     */
    @FXML public void btnBackClicked(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/AppointmentScre.fxml", "JAppointment");
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
     * <p>The <b>btnClearClicked Method</b> clear the Customer Search Box field and refreshes the Customer grid
     * by displaying all the customers in the Customers table.</p>
     * @param event <p>The event triggered when the user clicks on the Clear button.</p>
     * @throws SQLException <p>The error thrown by JAppointmentDAO Class (showJCustomersAll).</p>
     */
    @FXML public void btnClearClicked(ActionEvent event) throws SQLException {
        txtSearchBox.setText("");
        showJCustomersAll();
    }

    /**
     * <p>The <b>btnDeleteCustomerClicked Method</b> allows the user to delete an existing customer from the table.
     * This can be done by selecting a customer from the grid and clicking on the Delete Customer button. The user
     * is asked to confirm the process before deletion, this process cannot be undone.</p>
     * @param event <p>The event triggered when the Delete Custome button is clicked.</p>
     * @throws SQLException <p>The error thrown by JAppointmentDAO Class (getSelectedItem).</p>
     */
    @FXML public void btnDeleteCustomerClicked(ActionEvent event) throws SQLException {

        //  get the highlighted customer record
        customerSelection = tblCustomersGrid.getSelectionModel().getSelectedItem();

        //  did the user select a customer from the grid?
        if (customerSelection != null) {

            //  Confirm if the user would like to delete the customer
            Optional<ButtonType> result = displayAlert("Confirmation", "Confirmation",
                    "Are you sure you want to delete this customer?\n" +
                    "Deleting a customer will cancel all their appointments!");

            //  delete the record
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }

            try {
                ObservableList<Appointment> dbAppointments = JAppointmentDAO.jApptSelectAll();

                //  loop through the table
                for (int i = 0, dbAppointmentsSize = dbAppointments.size(); i < dbAppointmentsSize; i++) {
                    Appointment appointment = dbAppointments.get(i);

                    //  record found, delete it
                    if (appointment.getJAppointmentCustomerID() == customerSelection.getJAppointmentCustomerID()) {
                        JAppointmentDAO.jApptDeleteByID(appointment.getJAppointmentID());
                    }
                }

                JCustomerDAO.jCustomerDeleteByID(customerSelection.getJAppointmentCustomerID());
            } catch (SQLException e) {
                errorDBConn(e.getMessage());
            }
            displayAlert("Information", "Information",
                    customerSelection.getCustomerName() +
                    " and all associated appointments has been cancelled.");

            showJCustomersAll();

        } else {

            //  if not, fire up an error message
            displayAlert("Error", "Error", "You must select a customer from the list.");
        }
    }

    /**
     * <p>The <b>onDoubleClicked Method</b> provides the logic when the user double-clicks on a customer record inside
     * the grid. The selected customer record will be loaded in the Modify Customer Screen Form for editing.</p>
     * @param mouseEvent <p>The mouse event triggered when the user double-clicks on a customer record inside
     *                   the grid.</p>
     * @throws IOException <p>The error thrown by JAppointmentDAO Class (getSelectedItem).</p>
     */
    @FXML public void onDoubleClicked(MouseEvent mouseEvent) throws IOException {

        //  check if the user double-clicks on customer record inside
        //  the customer grid
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

            if(mouseEvent.getClickCount() == 2){
                //  get the highlighted customer record
                customerSelection = tblCustomersGrid.getSelectionModel().getSelectedItem();

                //  did the user select a customer from the grid?
                if (customerSelection == null) {

                    //  if not, fire up an error message
                    displayAlert("Error", "Error",
                            "You must select a customer from the list.");
                } else {

                    //  yes, open the edit window and pass the selected item to that window
                    stage = (Stage)((TableView)mouseEvent.getSource()).getScene().getWindow();
                    displayWindow(stage, "/teoespero/jappointment/Screens/ModifyCustomerScre.fxml",
                            "JAppointment-Modify Customer");
                }
            }
        }
    }

    /**
     * <p>The <b>btnModifyCustomerClicked Method</b> allows the user to modify an existing customer record.</p>
     * @param event <p>The event triggered when the user clicks on the Modify Customer button.</p>
     * @throws IOException <p>The error thrown by JAppointmentDAO Class (getSelectedItem).</p>
     */
    @FXML public void btnModifyCustomerClicked(ActionEvent event) throws IOException {

        //  get the highlighted customer record
        customerSelection = tblCustomersGrid.getSelectionModel().getSelectedItem();

        //  did the user select a customer from the grid?
        if (customerSelection != null) {

            //  yes, open the edit window and pass the selected item to that window
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            displayWindow(stage, "/teoespero/jappointment/Screens/ModifyCustomerScre.fxml",
                    "JAppointment-Modify Customer");
        } else {

            //  if not, fire up an error message
            displayAlert("Error", "Error", "You must select a customer from the list.");
        }
    }

    /**
     * <p>The <b>btnNewCustomerClicked Method</b> allows the user to add new customers to the Customers table.
     * Control is transferred to the New Customers Screen.</p>
     * @param event <p>The event triggered when the New Customer button is clicked.</p>
     * @throws IOException <p>The error thrown when JavaFX encounters an issue opening the form.</p>
     */
    @FXML public void btnNewCustomerClicked(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        displayWindow(stage, "/teoespero/jappointment/Screens/NewCustomerScre.fxml",
                "JAppointment-New Customer");
    }

    /**
     * <p>The <b>keyOnEnterPressed Method</b> processes the ENTER key when it is pressed while the focus is in the
     * Customer Search Box. This is treated a Search Button click event.</p>
     * @param keyEvent <p>The event triggered when the user presses the ENTER key while the Customer Search Box
     *                 has focus (KeyCode.ENTER).</p>
     * @throws SQLException <p>The error thrown by JAppointmentDAO Class (showJCustomersAll()).</p>
     */
    @FXML public void keyOnEnterPressed(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            showJCustomersAll();
        }

    }

    /**
     * <p>The <b>btnSearchClicked Method</b> displays the customer records based on the contents of the Customer
     * Search Box. If the Search Box is empty, all the records are displayed, otherwise, its contents are used
     * as a filter to find customer records that will match it.</p>
     * @param event <p>The event triggered when the user clicks on the Search button.</p>
     * @throws SQLException <p>The error thrown by JAppointmentDAO Class (showJCustomersAll()).</p>
     */
    @FXML public void btnSearchClicked(ActionEvent event) throws SQLException {
        showJCustomersAll();
    }

    /**
     * <p>The <b>showJCustomersAll Method</b> provides the logic needed to display teh customer records based of
     * the contents of the Customer Search Box.</p>
     * @throws SQLException
     */
    public void showJCustomersAll() throws SQLException {

        try {

            //  define vars that will hold customer data from the database
            ObservableList<Customer> obsCustomersList = JCustomerDAO.jCustomerSelectAll();
            ObservableList<CustomerDisplay> tblCustomersDisplay = FXCollections.observableArrayList();

            //  loop through the table and copy the records
            for (int i = 0, dbCustomersSize = obsCustomersList.size(); i < dbCustomersSize; i++) {
                Customer customer = obsCustomersList.get(i);

                CustomerDisplay newCustomer = new CustomerDisplay(customer);
                tblCustomersDisplay.add(newCustomer);
            }

            //  check if the Customer Search Box is loaded
            String customerSearchKey = txtSearchBox.getText();

            //  if it is, then try to match rows
            if (!"".equals(customerSearchKey)) {

                //  define vars that will hold the match
                ObservableList<CustomerDisplay> JFilteredCustomers = FXCollections.observableArrayList();
                String customerName;
                String customerAddress;
                String customerZipCode;
                String customerTelNo;
                String customerDivision;

                //  loop through the data and try to find a match
                for (int i = 0, customersSize = tblCustomersDisplay.size(); i < customersSize; i++) {
                    CustomerDisplay customer = tblCustomersDisplay.get(i);
                    customerName = customer.getCustomerName();
                    customerAddress = customer.getAddress();
                    customerZipCode = customer.getPostalCode();
                    customerTelNo = customer.getPhone();
                    customerDivision = customer.getDivisionName();

                    //  check if there is a match in the customer name
                    if ((customerName.toLowerCase().contains(customerSearchKey.toLowerCase()))
                            || (customerAddress.toLowerCase().contains(customerSearchKey.toLowerCase()))
                            || (customerZipCode.toLowerCase().contains(customerSearchKey.toLowerCase()))
                            ||  (customerTelNo.toLowerCase().contains(customerSearchKey.toLowerCase()))
                            || (customerDivision.toLowerCase().contains(customerSearchKey.toLowerCase()))) {
                        JFilteredCustomers.add(customer);
                    }
                }

                //  there is a match
                tblCustomersGrid.setItems(JFilteredCustomers);

                //  if the match is equal to one (1), then highlight it
                if(JFilteredCustomers.size() == 1){
                    tblCustomersGrid.getSelectionModel().clearSelection();
                    tblCustomersGrid.setItems(JFilteredCustomers);
                    tblCustomersGrid.requestFocus();
                    tblCustomersGrid.getSelectionModel().selectFirst();
                    tblCustomersGrid.getFocusModel().focus(0);
                }
            } else {
                tblCustomersGrid.setItems(tblCustomersDisplay);
            }
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }

    /**
     * <p>The <b>displayAlert Method</b> is a general method that displays custom alert, error messages (ERROR,
     * CONFIRMATION).</p>
     * @param alertType <p>The type of alert message (ERROR, CONFIRMATION).</p>
     * @param alertTitle <p>The alert window title.</p>
     * @param alertHeader <p>The alert window mesage.</p>
     * @return <p>Optional return value (ButtonType)</p>
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

        // optional return value
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

    /**
     * <p>The <b>getCustomerSelection Method</b> returns the current customer selection in the grid.</p>
     * @return <p>The selected customer in the grid.</p>
     */
    public static Customer getCustomerSelection() {
        return customerSelection;
    }

    /**
     * <p>The <b>initialize method</b> initializes the CustomerScreController Class by pre-populating
     * the appointments grid. This method makes the use of Lambda Expressions.</p>
     * @param location <p>The location of the form that is to be initialized.</p>
     * @param resources <p>the relative location of the resources used by JavaFX.</p>
     */
    @Override @FXML public void initialize(URL location, ResourceBundle resources) {
        //  Lambda Expression are being used here to set the values in the grid
        fldCustomerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        fldCustomerAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        fldCustomerZipCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        fldCustomerTelNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        fldCustomerDivision.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDivisionName()));

        try {
            showJCustomersAll();
        }
        catch(SQLException e) {
            errorDBConn(e.getMessage());
        }
    }
}
