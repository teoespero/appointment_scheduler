package teoespero.jappointment.DBHelper;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import teoespero.jappointment.Model.Customer;
import javafx.scene.control.Alert;

import java.util.Optional;

/**
 * <p>The <b>DBCheckCustomer Class</b> provides mechanisms for validating data entered in the Customer forms.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class DBCheckCustomer {

    /**
     * <p>The <b>fieldsEmpty</b> checks if any of the fields for the customer form is blank or empty.</p>
     * @param customer <p>The Customer object that contains the details of the Customer.</p>
     * @return <p>TRUE or FALSE if any of the field are blank or empty.</p>
     */
    public static boolean fieldsEmpty(Customer customer) {

        boolean fieldsEmpty = false;

        if ("".equals(customer.getCustomerName())) {
            fieldsEmpty = true;
        } else if ("".equals(customer.getAddress())) {
            fieldsEmpty = true;
        } else if ("".equals(customer.getPostalCode())) {
            fieldsEmpty = true;
        } else if ("".equals(customer.getPhone())) {
            fieldsEmpty = true;
        }

        if (!fieldsEmpty) {
            return fieldsEmpty;
        }
        displayAlert("Error", "Error", "All fields are required, please make sure that " +
                "all information needed are entered.");
        return fieldsEmpty;
    }

    /**
     * <p>The <b>displayAlert Method</b> provides the logic needed to display custom-defined messages to the user
     * when there is an error, or a confirmation is needed.</p>
     * @param alertType <p>Defines the type of alert (ERROR, CONFIRMATION).</p>
     * @param alertTitle <p>The alert window title.</p>
     * @param alertHeader <p>The message to be displayed.</p>
     * @return <p>An optional return value (button type).</p>
     */
    @FXML public static Optional<ButtonType> displayAlert(String alertType, String alertTitle, String alertHeader){
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
}
