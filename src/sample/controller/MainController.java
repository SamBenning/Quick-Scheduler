package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.controller.appointmentControllers.AddAppointmentController;
import sample.dao.AppointmentDao;
import sample.dao.CustomerDao;
import sample.model.Appointment;
import sample.model.Customer;
import sample.util.JavaFXUtil;
import sample.util.report.Report;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

import static java.time.LocalDate.now;

public class MainController implements Initializable {
    public TableView<Appointment> appTableView;
    public TableColumn<Appointment, Integer> appIdCol;
    public TableColumn<Appointment, String> appTitleCol;
    public TableColumn<Appointment, String> appDescriptionCol;
    public TableColumn<Appointment, String> appLocationCol;
    public TableColumn<Appointment, String> appTypeCol;
    public TableColumn<Appointment, LocalDateTime> appStartCol;
    public TableColumn<Appointment, LocalDateTime> appEndCol;
    public TableColumn<Appointment, String> appCustomerCol;
    public TableColumn<Appointment, String> appUserCol;
    public TableColumn<Appointment, String> appContactCol;
    public Button addAppButton;
    public Button modifyAppButton;
    public Button deleteAppButton;
    public TableView<Customer> customerTableView;
    public TableColumn<Customer, Integer> customerIdCol;
    public TableColumn<Customer, String> customerNameCol;
    public TableColumn<Customer, String> customerAddressCol;
    public TableColumn<Customer, String> customerPostalCol;
    public TableColumn<Customer, String> customerPhoneCol;
    public TableColumn<Customer, Integer> customerDivisionCol;
    public Button addCustomerButton;
    public Button modifyCustomerButton;
    public Button deleteCustomerButton;
    public RadioButton appViewAllRadio;
    public ToggleGroup appViewGroup;
    public RadioButton appViewWeekRadio;
    public RadioButton appViewMonthRadio;
    public ComboBox<Report> reportTypeCombo;
    public HBox reportComboArea;
    public HBox reportDynamicComboArea;
    public HBox reportDynamicTableArea;

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
        Report.setDynamicComboArea(reportDynamicComboArea);
        Report.setDynamicTableArea(reportDynamicTableArea);
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
