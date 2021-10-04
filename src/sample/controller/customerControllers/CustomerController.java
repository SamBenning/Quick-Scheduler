package sample.controller.customerControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.dao.CountryDao;
import sample.dao.CustomerDao;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Country;
import sample.model.Customer;
import sample.util.JavaFXUtil;
import java.util.HashMap;

public abstract class CustomerController {
    public TextField customerNameField;
    public TextField customerAddressField;
    public TextField customerPostalField;
    public TextField customerPhoneField;
    public ComboBox customerDivisionCombo;
    public ComboBox customerCountryCombo;
    protected HashMap<String, Integer> countryNameIdMap;
    protected ObservableList<Customer> customers;
    protected ObservableList<Country> countries;
    protected ObservableList<String> countryNames;
    protected ObservableList<String> divisionNames;

    public CustomerController(ObservableList<Customer> customers) {
        this.countryNameIdMap = new HashMap<>();
        this.countries = CountryDao.getAllCountries();
        this.countryNames = FXCollections.observableArrayList();
        this.divisionNames = FXCollections.observableArrayList();
        this.customers = customers;
        initCountries();
    }

    private void initCountries() {
        for (Country country : countries) {
            String name = country.getCountryName();
            Integer id = country.getCountryId();
            countryNames.add(name);
            countryNameIdMap.put(name, id);
        }
    }

    protected void addCustomerToDb(ActionEvent actionEvent) {
        int selectedDivisionId = FirstLevelDivisionDao.getDivisionId(
                customerDivisionCombo.getSelectionModel().getSelectedItem().toString());

        Customer newCustomer = new Customer (
                -1,
                customerNameField.getText(),
                customerAddressField.getText(),
                customerPostalField.getText(),
                customerPhoneField.getText(),
                selectedDivisionId
        );

        if(CustomerDao.addCustomer(newCustomer)) {
            customers.setAll(CustomerDao.getAllCustomers());
            JavaFXUtil.closeWindow(actionEvent);
        }
    }
}
