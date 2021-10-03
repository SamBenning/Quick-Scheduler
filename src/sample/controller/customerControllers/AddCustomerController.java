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
import sample.model.FirstLevelDivision;
import sample.util.JavaFXUtil;
import sample.util.ValidationUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


public class AddCustomerController implements Initializable {
    public TextField customerNameField;
    public TextField customerAddressField;
    public TextField customerPostalField;
    public TextField customerPhoneField;
    public ComboBox customerDivisionCombo;
    public ComboBox customerCountryCombo;

    private HashMap<String, Integer> countryNameIdMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> divisionNameIdMap = new HashMap<String, Integer>();
    private ObservableList<Country> countries = CountryDao.getAllCountries();
    private ObservableList<String> countryNames = FXCollections.observableArrayList();
    private ObservableList<String> divisionNames = FXCollections.observableArrayList();
    private ObservableList<Customer> customers;

    public AddCustomerController (ObservableList<Customer> customers) {this.customers = customers;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        for (Country country : countries) {
            String name = country.getCountryName();
            Integer id = country.getCountryId();
            countryNames.add(name);
            countryNameIdMap.put(name, id);
        }

        customerCountryCombo.getItems().addAll(countryNames);
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    public void saveButtonHandler(ActionEvent actionEvent) {

        if (ValidationUtil.validateAddCustomer(this));

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

    public void selectCountryHandler(ActionEvent actionEvent) {
        customerDivisionCombo.getItems().clear();
        divisionNames.clear();
        String selection = customerCountryCombo.getSelectionModel().getSelectedItem().toString();
        int selectionId = countryNameIdMap.get(selection);

        for (FirstLevelDivision division : FirstLevelDivisionDao.getDivisionsByCountry(selectionId)) {
            divisionNames.add(division.getDivisionName());
        }
        customerDivisionCombo.getItems().addAll(divisionNames);
    }
}
