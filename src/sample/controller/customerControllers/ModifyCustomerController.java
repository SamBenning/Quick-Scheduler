package sample.controller.customerControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.dao.CountryDao;
import sample.model.Country;
import sample.model.Customer;
import sample.util.JavaFXUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ModifyCustomerController extends CustomerController implements Initializable {
   /* public TextField customerNameField;
    public TextField customerAddressField;
    public TextField customerPostalField;
    public TextField customerPhoneField;
    public ComboBox customerDivisionCombo;
    public ComboBox customerCountryCombo;
    public Button cancelButton;
    public Button saveButton;*/

    private Customer selectedCustomer;//Will need to be passed in from MainController

    public ModifyCustomerController(Customer selectedCustomer, ObservableList<Customer> customers) {
        super(customers);
        this.selectedCustomer = selectedCustomer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCountryCombo.getItems().addAll(countryNames);

        customerNameField.setText(selectedCustomer.getCustomerName());
        customerAddressField.setText(selectedCustomer.getAddress());
        customerPostalField.setText(selectedCustomer.getPostalCode());
        customerPhoneField.setText(selectedCustomer.getPhone());
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    public void saveButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }
}