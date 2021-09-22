package sample.controller.customerControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.dao.CountryDao;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Country;
import sample.model.Customer;
import sample.model.FirstLevelDivision;
import sample.util.JavaFXUtil;

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

    private HashMap<String, Integer> idNameMap = new HashMap<String, Integer>();
    private ObservableList<Country> countries = CountryDao.getAllCountries();
    private ObservableList<String> countryNames = FXCollections.observableArrayList();
    private ObservableList<String> divisionNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        for (Country country : countries) {
            String name = country.getCountryName();
            Integer id = country.getCountryId();
            countryNames.add(name);
            idNameMap.put(name, id);
        }

        customerCountryCombo.getItems().addAll(countryNames);
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    public void saveButtonHandler(ActionEvent actionEvent) {

        Customer customer = new Customer (
                customerNameField.getText(),
                customerAddressField.getText(),
                Integer.parseInt(customerPostalField.getText()),
                customerPhoneField.getText(),
                Integer.parseInt(customerDivisionCombo.getButtonCell().getText())
        );

        JavaFXUtil.closeWindow(actionEvent);
    }

    public void selectCountryHandler(ActionEvent actionEvent) {
        customerDivisionCombo.getItems().clear();
        divisionNames.clear();
        String selection = customerCountryCombo.getSelectionModel().getSelectedItem().toString();
        int selectionId = idNameMap.get(selection);

        for (FirstLevelDivision division : FirstLevelDivisionDao.getDivisionsByCountry(selectionId)) {
            divisionNames.add(division.getDivisionName());
        }
        customerDivisionCombo.getItems().addAll(divisionNames);
    }
}
