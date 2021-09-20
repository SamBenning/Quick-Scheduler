package sample.controller.customerControllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.model.Customer;
import sample.util.JavaFXUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    public TextField customerNameField;
    public TextField customerAddressField;
    public TextField customerPostalField;
    public TextField customerPhoneField;
    public ComboBox customerDivisionCombo;
    public ComboBox customerCountryCombo;
    public Button cancelButton;
    public Button saveButton;

    private Customer selectedCustomer; //Will need to be passed in from MainController

    /*public ModifyCustomerController(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Query the db and fill in fields with values
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    public void saveButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }
}