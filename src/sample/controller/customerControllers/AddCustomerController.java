package sample.controller.customerControllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Customer;
import sample.model.FirstLevelDivision;
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
        if (ValidationUtil.validateAddCustomer(this)) {
            addCustomerToDb(actionEvent);
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
