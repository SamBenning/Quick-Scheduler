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

/**
 * Controller for the add customer form.*/
public class AddCustomerController extends CustomerController implements Initializable  {

    public AddCustomerController (ObservableList<Customer> customers) {
        super(customers);
    }

    /**Sets up the country combo box.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCountryCombo.getItems().addAll(countryNames);
    }

    /**
     * Closes the window and returns to the main menu.*/
    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    /**
     * If ValidationUtil.validateCustomer returns true, calls addCustomertoDb.*/
    public void saveButtonHandler(ActionEvent actionEvent) {
        if (ValidationUtil.validateCustomer(this)) {
            addCustomerToDb();
            JavaFXUtil.closeWindow(actionEvent);
        }
    }

    /**
     * Calls CustomerDao.addCustomer with customer generated from user input to add to the database. Updates customers
     * list so that changes are reflected when returning to the table view in the main menu.*/
    private void addCustomerToDb() {
        if(CustomerDao.addCustomer(instantiateCustomer())) {
            customers.setAll(CustomerDao.getAllCustomers());
        }
    }
}
