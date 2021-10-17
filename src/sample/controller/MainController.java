package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.controller.appointmentControllers.AddAppointmentController;
import sample.controller.appointmentControllers.ModifyAppointmentController;
import sample.controller.customerControllers.AddCustomerController;
import sample.dao.AppointmentDao;
import sample.dao.CustomerDao;
import sample.model.Appointment;
import sample.model.Customer;
import sample.util.JavaFXUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public TableView appTableView;
    public TableColumn appIdCol;
    public TableColumn appTitleCol;
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

    private ObservableList<Appointment> appointments;
    private ObservableList<Customer> customers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointments = AppointmentDao.getAllAppointments();
        appTableView.setItems(appointments);
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        appUserCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        customers = CustomerDao.getAllCustomers();
        customerTableView.setItems(customers);
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    }

    public void addAppHandler(ActionEvent actionEvent) {
        AddAppointmentController addAppointmentController = new AddAppointmentController(appointments);
        JavaFXUtil.showAddAppWindow(actionEvent,
                "/sample/view/appointmentViews/addAppointmentForm.fxml",
                appointments);
        //appTableView.setItems(appointments);
    }

    public void modifyAppHandler(ActionEvent actionEvent) {
        Appointment appointment;
        try {
            appointment = (Appointment) appTableView.getSelectionModel().getSelectedItem();
            JavaFXUtil.showModifyAppWindow(actionEvent,
                    "/sample/view/appointmentViews/modifyAppointmentForm.fxml",
                    appointments, appointment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAppHandler(ActionEvent actionEvent) {
    }

    public void addCustomerHandler(ActionEvent actionEvent) {
        //AddCustomerController addCustomerController = new AddCustomerController(customers);
        JavaFXUtil.showAddCustomerWindow(actionEvent,
                "/sample/view/customerViews/addCustomerForm.fxml",
                customers);
    }

    public void modifyCustomerHandler(ActionEvent actionEvent) {
        Customer customer;
        try {
            customer = (Customer)customerTableView.getSelectionModel().getSelectedItem();
            JavaFXUtil.showModifyCustomerWindow(actionEvent,
                    "/sample/view/customerViews/modifyCustomerForm.fxml", customers, customer);
        } catch (Exception e) {
            System.out.println("No customer selected.");;
        }

       /* JavaFXUtil.showNewWindow(actionEvent,
                "/sample/view/customerViews/modifyAppointmentForm.fxml");*/
    }

    public void deleteCustomerHandler(ActionEvent actionEvent) {
    }

}
