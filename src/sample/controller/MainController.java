package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.controller.appointmentControllers.AddAppointmentController;
import sample.controller.appointmentControllers.ModifyAppointmentController;
import sample.controller.customerControllers.AddCustomerController;
import sample.dao.AppointmentDao;
import sample.dao.CustomerDao;
import sample.model.Appointment;
import sample.model.Customer;
import sample.util.JavaFXUtil;
import sample.util.report.Report;
import sample.util.report.TypeMonthReport;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.ResourceBundle;

import static java.time.LocalDate.now;

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
    public RadioButton appViewAllRadio;
    public ToggleGroup appViewGroup;
    public RadioButton appViewWeekRadio;
    public RadioButton appViewMonthRadio;
    public ComboBox reportTypeCombo;
    public HBox reportComboArea;

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

        appViewGroup.selectToggle(appViewAllRadio);
        reportTypeCombo.setItems(Report.getReports());
        Report.sethBox(reportComboArea);
    }

    public void addAppHandler(ActionEvent actionEvent) {
        AddAppointmentController addAppointmentController = new AddAppointmentController(appointments);
        JavaFXUtil.showAddAppWindow(actionEvent,
                "/sample/view/appointmentViews/addAppointmentForm.fxml",
                appointments);
        System.out.println("made it here");
        refreshAppView();
        //appTableView.setItems(appointments);
    }

    public void modifyAppHandler(ActionEvent actionEvent) {
        Appointment appointment;
        try {
            appointment = (Appointment) appTableView.getSelectionModel().getSelectedItem();
            JavaFXUtil.showModifyAppWindow(actionEvent,
                    "/sample/view/appointmentViews/modifyAppointmentForm.fxml",
                    appointments, appointment);
            System.out.println("Made it here");
            refreshAppView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAppView() {
        if (appViewGroup.getSelectedToggle().equals(appViewWeekRadio)) {
            filterWeek();
        } else if (appViewGroup.getSelectedToggle().equals(appViewMonthRadio)) {
            filterMonth();
        } else {
            filterAll();
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

    public void viewAllHandler(ActionEvent actionEvent) {
        filterAll();
    }

    public void viewWeekHandler(ActionEvent actionEvent) {
        filterWeek();
    }

    public void viewMonthHandler(ActionEvent actionEvent) {
       filterMonth();
    }

    private void filterAll() {
        appointments = AppointmentDao.getAllAppointments();
        appTableView.setItems(appointments);
    }

    private void filterWeek() {
        DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY;
        DayOfWeek lastDayOfWeek = DayOfWeek.SATURDAY;
        LocalDate firstDateOfWeek = now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        LocalDate lastDateOfWeek = now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
        appTableView.setItems(AppointmentDao.getAppointmentsInDateRange(firstDateOfWeek, lastDateOfWeek));
    }

    private void filterMonth() {
        LocalDate date = now();
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        appTableView.setItems(AppointmentDao.getAppointmentsInDateRange(firstDayOfMonth, lastDayOfMonth));
    }

    public void selectReportHandler(ActionEvent actionEvent) {
        Report selection = ((ComboBox<Report>) actionEvent.getSource()).getSelectionModel().getSelectedItem();
        selection.getOnSelection().onSelectReport();
    }
}
