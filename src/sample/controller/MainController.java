package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sample.dao.AppointmentDao;
import sample.dao.CustomerDao;
import sample.model.Appointment;
import sample.model.Customer;
import sample.util.AlertUtil;
import sample.util.JavaFXUtil;
import sample.util.report.Report;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

import static java.time.LocalDate.now;

/**
 * Controller for the main menu of the application.*/
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
    public Label appStatusLabel;
    public Label customerStatusLabel;

    private ObservableList<Appointment> appointments;
    private ObservableList<Customer> customers;

    /**
     * Initializes table views for appointments and customers. Initializes appointment view toggle. Populates
     * reportTypeCombo, and does some setup for the Report abstract class so that the reports can work properly.*/
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

        checkUpcomingAppointments();

    }

    /**
     * Grabs the the local system time and queries the database for appointments within 15 minutes of that time.
     * If no results, displays an alert informing user there are no upcoming appointments. If results are found, loops
     * through all results, displaying a custom alert for each one.*/
    private void checkUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        ObservableList<Appointment> upcoming = AppointmentDao.getUpcomingAppointments(now);
        if (upcoming.isEmpty()) {
            AlertUtil.showUpcomingAppointment();
        } else {
            for (Appointment appointment : upcoming) {
                AlertUtil.showUpcomingAppointment(appointment);
            }
        }
    }

    /**
     * Displays the add appointment form.*/
    public void addAppHandler(ActionEvent actionEvent) {
        //AddAppointmentController addAppointmentController = new AddAppointmentController(appointments);
        JavaFXUtil.showAddAppWindow(actionEvent,
                "/sample/view/appointmentViews/addAppointmentForm.fxml",
                appointments);
        System.out.println("made it here");
        refreshAppView();
        //appTableView.setItems(appointments);
    }

    /** Displays the modify appointment form. Uses try/catch in case user clicks modify without selecting an appointment
     * in the table view.*/
    public void modifyAppHandler(ActionEvent actionEvent) {
        Appointment appointment;
        try {
            appointment = appTableView.getSelectionModel().getSelectedItem();
            JavaFXUtil.showModifyAppWindow(actionEvent,
                    "/sample/view/appointmentViews/modifyAppointmentForm.fxml",
                    appointments, appointment);
            System.out.println("Made it here");
            refreshAppView();
        } catch (Exception e) {
            System.out.println("No appointment selected.");
        }
    }

    /**
     * Checks which view toggle is selected, and calls the appropriate filter function to update the tableview.*/
    private void refreshAppView() {
        if (appViewGroup.getSelectedToggle().equals(appViewWeekRadio)) {
            filterWeek();
        } else if (appViewGroup.getSelectedToggle().equals(appViewMonthRadio)) {
            filterMonth();
        } else {
            filterAll();
        }
    }

    /**
     * First prompts user to confirm deletion. Then, passes the selected appointment into AppointmentDao.deleteAppointment
     * to perform deletion. On successful deletion, displays custom message in appStatusLabel to notify user deletion
     * was successful.*/
    public void deleteAppHandler() {
        if (AlertUtil.confirmDeletion()){
            Appointment appointment;
            try {
                appointment = appTableView.getSelectionModel().getSelectedItem();
                if(AppointmentDao.deleteAppointment(appointment)) {
                    filter();
                    appStatusLabel.setText(appointment.toString() + " was successfully deleted.");
                    appStatusLabel.setTextFill(Color.color(0,1,0));
                }
            } catch (Exception e) {
                System.out.println("No appointment selected.");
            }
        }
    }

    /**
     * Displays the add customer form.*/
    public void addCustomerHandler(ActionEvent actionEvent) {
        //AddCustomerController addCustomerController = new AddCustomerController(customers);
        JavaFXUtil.showAddCustomerWindow(actionEvent,
                "/sample/view/customerViews/addCustomerForm.fxml",
                customers);
    }

    /**
     * Displays the modify customer form. Uses try/catch in case user clicks modify button without having selected a
     * customer in the table view.*/
    public void modifyCustomerHandler(ActionEvent actionEvent) {
        Customer customer;
        try {
            customer = customerTableView.getSelectionModel().getSelectedItem();
            JavaFXUtil.showModifyCustomerWindow(actionEvent,
                    "/sample/view/customerViews/modifyCustomerForm.fxml", customers, customer);
        } catch (Exception e) {
            System.out.println("No customer selected.");
        }
    }

    /**
     * First prompts user to confirm deletion. Then, checks whether the customer has any associated appointments. If so,
     * A warning is displayed informing the user that these appointments must first be deleted to proceed. If the user
     * clicks yes, all of the associated appointments are deleted, then the customer is deleted. Custom message is
     * displayed in customerStatusLabel informing user that the deletion was successful.*/
    public void deleteCustomerHandler() {
        if(AlertUtil.confirmDeletion()) {
            Customer customer;
            try {
                customer = customerTableView.getSelectionModel().getSelectedItem();
                ObservableList<Appointment> associatedAppointments = AppointmentDao.getAppointmentsByCustomer(customer);
                if (!associatedAppointments.isEmpty()) {
                    if (AlertUtil.warnDeleteAssociatedAppointments()) {
                        for (Appointment appointment : associatedAppointments) {
                            AppointmentDao.deleteAppointment(appointment);
                        }
                        CustomerDao.deleteCustomer(customer);
                        customerTableView.setItems(CustomerDao.getAllCustomers());
                        customerStatusLabel.setText(customer.toString() + " was successfully deleted.");
                        customerStatusLabel.setTextFill(Color.color(0,1,0));
                    }
                } else {
                    CustomerDao.deleteCustomer(customer);
                    customerTableView.setItems(CustomerDao.getAllCustomers());
                    customerStatusLabel.setText(customer.toString() + " was successfully deleted.");
                    customerStatusLabel.setTextFill(Color.color(0,1,0));
                }

                //CustomerDao.deleteCustomer(customer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls appropriate filter method on toggle selection.*/
    public void viewAllHandler() {
        filterAll();
    }

    /**
     * Calls appropriate filter method on toggle selection.*/
    public void viewWeekHandler() {
        filterWeek();
    }

    /**
     * Calls appropriate filter method on toggle selection.*/
    public void viewMonthHandler() {
       filterMonth();
    }

    /**
     * Checks which appointment view toggle is selected, then calls the appropriate filter function.*/
    private void filter() {
        RadioButton selectedView = (RadioButton)appViewGroup.getSelectedToggle();
        if (selectedView.equals(appViewAllRadio)) {
            filterAll();
        } else if (selectedView.equals(appViewWeekRadio)) {
            filterWeek();
        } else {
            filterMonth();
        }
    }

    /**
     * Displays all appointments from the database in the table view.*/
    private void filterAll() {
        appointments = AppointmentDao.getAllAppointments();
        appTableView.setItems(appointments);
    }

    /**
     * Displays only appointments from Sunday to Saturday of the current week.*/
    private void filterWeek() {
        DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY;
        DayOfWeek lastDayOfWeek = DayOfWeek.SATURDAY;
        LocalDate firstDateOfWeek = now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        LocalDate lastDateOfWeek = now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
        appTableView.setItems(AppointmentDao.getAppointmentsInDateRange(firstDateOfWeek, lastDateOfWeek));
    }

    /**
     * Displays only appointments from the first day of the month to the last day of the month of the current month.*/
    private void filterMonth() {
        LocalDate date = now();
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        appTableView.setItems(AppointmentDao.getAppointmentsInDateRange(firstDayOfMonth, lastDayOfMonth));
    }

    /**
     * Calls appropriate onSelectReport method for selected Report subclass via ReportListener functional interface.
     * Each report subclass needs to perform different functions on selection.*/
    public void selectReportHandler() {
        Report selection = reportTypeCombo.getSelectionModel().getSelectedItem();
        selection.getOnSelection().onSelectReport();
    }
}
