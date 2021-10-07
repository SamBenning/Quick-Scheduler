package sample.controller.customerControllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import sample.dao.CustomerDao;
import sample.model.Customer;
import sample.util.JavaFXUtil;
import sample.util.ValidationUtil;

import java.net.URL;
import java.util.ResourceBundle;


public class AddCustomerController extends CustomerController implements Initializable  {

    public AddCustomerController (ObservableList<Customer> customers) {
        super(customers);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCountryCombo.getItems().addAll(countryNames);
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    public void saveButtonHandler(ActionEvent actionEvent) {
        if (ValidationUtil.validateCustomer(this)) {
            addCustomerToDb();
            JavaFXUtil.closeWindow(actionEvent);
        }
    }

    private void addCustomerToDb() {
        if(CustomerDao.addCustomer(instantiateCustomer())) {
            customers.setAll(CustomerDao.getAllCustomers());
        }
    }
}
