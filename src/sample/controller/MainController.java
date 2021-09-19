package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public TableView appTableView;
    public TableColumn appIdCol;
    public TableColumn appDescriptionCol;
    public TableColumn appLocationCol;
    public TableColumn appTypeCol;
    public TableColumn appStartCol;
    public TableColumn appEndCol;
    public TableColumn appCustomerCol;
    public TableColumn appUserCol;
    public TableColumn appContactCol;
    public Button addAppButton;
    public Button modifyAppButton;
    public Button deleteAppButton;
    public TableView customerTableView;
    public TableColumn customerIdCol;
    public TableColumn customerNameCol;
    public TableColumn customerAddressCol;
    public TableColumn customerPostalCol;
    public TableColumn customerPhoneCol;
    public TableColumn customerDivisionCol;
    public Button addCustomerButton;
    public Button modifyCustomerButton;
    public Button deleteCustomerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addAppHandler(ActionEvent actionEvent) {
    }

    public void modifyAppHandler(ActionEvent actionEvent) {
    }

    public void deleteAppHandler(ActionEvent actionEvent) {
    }

    public void addCustomerHandler(ActionEvent actionEvent) {
        showAddCustomerForm(actionEvent);
    }

    public void modifyCustomerHandler(ActionEvent actionEvent) {
    }

    public void deleteCustomerHandler(ActionEvent actionEvent) {
    }

    public void showAddCustomerForm(ActionEvent actionEvent) {
        try {

            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/customerViews/addCustomerForm.fxml"));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
