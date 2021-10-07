package sample.controller.customerControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.dao.CountryDao;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Country;
import sample.model.Customer;
import sample.model.FirstLevelDivision;
import java.util.HashMap;

public abstract class CustomerController {
    public TextField customerNameField;
    public TextField customerAddressField;
    public TextField customerPostalField;
    public TextField customerPhoneField;
    public ComboBox<String> customerDivisionCombo;
    public ComboBox<String> customerCountryCombo;
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

    protected Customer instantiateCustomer() {
        int selectedDivisionId = FirstLevelDivisionDao.getDivisionId(
                customerDivisionCombo.getSelectionModel().getSelectedItem());

        return new Customer (
                -1,
                customerNameField.getText(),
                customerAddressField.getText(),
                customerPostalField.getText(),
                customerPhoneField.getText(),
                selectedDivisionId
        );
    }

    public void selectCountryHandler() {
        customerDivisionCombo.getItems().clear();
        divisionNames.clear();
        setDivisionList();
    }

    public void setDivisionList() {
        divisionNames.clear();
        String selection = customerCountryCombo.getSelectionModel().getSelectedItem();
        int selectionId = countryNameIdMap.get(selection);

        for (FirstLevelDivision division : FirstLevelDivisionDao.getDivisionsByCountry(selectionId)) {
            divisionNames.add(division.getDivisionName());
        }
        customerDivisionCombo.getItems().addAll(divisionNames);
    }

}
