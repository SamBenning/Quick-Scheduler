package sample.controller.customerControllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import sample.dao.CustomerDao;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Customer;
import sample.util.JavaFXUtil;
import sample.util.ValidationUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the modify customer form.*/
public class ModifyCustomerController extends CustomerController implements Initializable {

    private final Customer selectedCustomer;//Will need to be passed in from MainController

    /**
     * Sets selectedCustomer passed in from the main menu.*/
    public ModifyCustomerController(Customer selectedCustomer, ObservableList<Customer> customers) {
        super(customers);
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * Sets up combo boxes and calls functions to read in data and get everything pre-filled/pre-selected.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCountryCombo.getItems().addAll(countryNames);
        int customerDivisionId = selectedCustomer.getDivisionId();
        int customerCountryID = FirstLevelDivisionDao.getCountryIdByDivisionId(customerDivisionId);
        customerNameField.setText(selectedCustomer.getCustomerName());
        customerAddressField.setText(selectedCustomer.getAddress());
        customerPostalField.setText(selectedCustomer.getPostalCode());
        customerPhoneField.setText(selectedCustomer.getPhone());
        customerIdField.setText(Integer.toString(selectedCustomer.getCustomerId()));
        customerCountryCombo.getSelectionModel().select(customerCountryID-1);
        setInitialDivision(customerDivisionId);
    }

    /**
     * Closes the window and returns to the main menu.*/
    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    /**
     * If ValidationUtil.validateCustomer returns true, calls updateCustomerInDb.*/
    public void saveButtonHandler(ActionEvent actionEvent) {
        if(ValidationUtil.validateCustomer(this)) {
            updateCustomerInDb();
            JavaFXUtil.closeWindow(actionEvent);
        }
    }

    /**
     * Calls instantiateCustomer. Passes that customer object and the customer id from selectedCustomer into
     * CustomerDao,updateCustomer so database entry can be properly updated. Updates customers so changes are refelcted
     * in the main menu.*/
    private void updateCustomerInDb() {
        Customer updatedCustomer = instantiateCustomer();
        updatedCustomer.setCustomerId(selectedCustomer.getCustomerId());
        if(CustomerDao.updateCustomer(updatedCustomer)) {
            customers.setAll(CustomerDao.getAllCustomers());
        }
    }

    /**
     * Handles selecting the initial division.*/
    private void setInitialDivision(int divisionId) {
        setDivisionList();
        String divisionName = FirstLevelDivisionDao.getDivisionName(divisionId);
        int i = 0;
        for (String name : divisionNames) {
            if (name.equals(divisionName)) {
                customerDivisionCombo.getSelectionModel().select(i);
            }
            i++;
        }
    }
}