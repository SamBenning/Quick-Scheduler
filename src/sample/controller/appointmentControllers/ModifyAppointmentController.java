package sample.controller.appointmentControllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.controller.customerControllers.ModifyCustomerController;
import sample.dao.AppointmentDao;
import sample.dao.CustomerDao;
import sample.model.Appointment;
import sample.model.AppointmentType;
import sample.model.Customer;
import sample.util.JavaFXUtil;
import sample.util.ValidationUtil;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller class for Modify Appointment form. Handles initializing combo boxes and reading in data from the selected
 * appointment to pre-populate all fields and combo boxes.*/
public class ModifyAppointmentController extends AppointmentController implements Initializable {

    private Appointment selectedAppointment;
    private HashMap<Integer, String> idToCustomerMap;
    private HashMap<Integer, String> idToUserMap;
    private HashMap<Integer, String> idToContactMap;

    /**
     * @param appointments List of all appointments from main menu
     * @param selectedAppointment Whichever appointment was selected in table view when user clicked modify.*/
    public ModifyAppointmentController(Appointment selectedAppointment, ObservableList<Appointment> appointments) {
        super(appointments);
        this.selectedAppointment = selectedAppointment;
    }

    /**
     * Initializes combo boxes, pre-selects appropriate option for each combo box, reads in data to text fields.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCustomers(customers);
        initUsers(users);
        initContacts(contacts);
        initTimeCombos();
        initTypes();
        idToCustomerMap = new HashMap<>();
        idToUserMap = new HashMap<>();
        idToContactMap = new HashMap<>();
        appTitleField.setText(selectedAppointment.getTitle());
        appDescriptionField.setText(selectedAppointment.getDescription());
        appLocationField.setText(selectedAppointment.getLocation());
        appTypeCombo.getSelectionModel().select(new AppointmentType(selectedAppointment.getType()));
        appIdField.setText(Integer.toString(selectedAppointment.getAppointmentId()));
        selectInitialCustomer();
        selectInitialUser();
        selectInitialContact();
        selectInitialDate();
        selectInitialTimes();
    }

    /**
     * Sets dates and times and calls ValidationUtil to validate data. If valid, updates appointment in the database
     * by calling updateAppInDb.*/
    public void saveButtonHandler(ActionEvent actionEvent) {
        setDate();
        setStartDateTime();
        setEndDateTime();
        if (ValidationUtil.validateAppointment(this)) {
            updateAppInDb();
            JavaFXUtil.closeWindow(actionEvent);
        }
    }

    /**
     * Closes the window and returns to main menu.*/
    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    /**
     * Returns the appointment that was passed in from the main menu.*/
    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    /**
     * Passes the appointment ID of the selectedAppointment from main menu and the new appointment object to
     * AppointmentDao,updateAppointment so that values can be properly updated.*/
    private void updateAppInDb() {
        if(AppointmentDao.updateAppointment(selectedAppointment.getAppointmentId(), instantiateAppointment())) {
            appointments.setAll(AppointmentDao.getAllAppointments());
        }
    }

    /**
     * Pre-selects proper customer option in customer combo box.*/
    private void selectInitialCustomer() {
        Customer customer = CustomerDao.getCustomer(selectedAppointment.getCustomerId());
        for (Map.Entry<String, Integer> entry : customerToIdMap.entrySet()) {
            idToCustomerMap.put(entry.getValue(), entry.getKey());
        }
        String selection = idToCustomerMap.get(selectedAppointment.getCustomerId());
        appCustomerCombo.getSelectionModel().select(selection);
    }

    /**
     * Pre-selects proper user option in user combo box.*/
    private void selectInitialUser() {
        for (Map.Entry<String, Integer> entry: userToIdMap.entrySet()) {
            idToUserMap.put(entry.getValue(), entry.getKey());
        }
        String selection = idToUserMap.get(selectedAppointment.getUserId());
        appUserCombo.getSelectionModel().select(selection);
    }

    /**
     * Pre-selects proper contact option in contact combo box.*/
    private void selectInitialContact() {
        for (Map.Entry<String, Integer> entry: contactToIdMap.entrySet()) {
            idToContactMap.put(entry.getValue(), entry.getKey());
        }
        String selection = idToContactMap.get(selectedAppointment.getContactId());
        appContactCombo.getSelectionModel().select(selection);
    }

    /**
     * Pre-selects proper date in the date picker.*/
    private void selectInitialDate() {
        LocalDate date = selectedAppointment.getStart().toLocalDate();
        appDate.setValue(date);
    }

    /**
     * Reads LocalTimes and pre-selects all of the time combos to represent to correct time for selected appointment.*/
    private void selectInitialTimes() {
        LocalTime startTime = selectedAppointment.getStart().toLocalTime();
        LocalTime endTime = selectedAppointment.getEnd().toLocalTime();
        appStartHourCombo.getSelectionModel().select(
                findMatchIndexInCombo(startTime.getHour(), appStartHourCombo, true));
        appStartMinuteCombo.getSelectionModel().select(
                findMatchIndexInCombo(startTime.getMinute(), appStartMinuteCombo, false));
        appEndHourCombo.getSelectionModel().select(
                findMatchIndexInCombo(endTime.getHour(), appEndHourCombo, true));
        appEndMinuteCombo.getSelectionModel().select(
                findMatchIndexInCombo(endTime.getMinute(), appStartMinuteCombo, false));
        if (startTime.getHour() > 12) {
            appStartAmPmCombo.getSelectionModel().select("PM");
        } else {
            appStartAmPmCombo.getSelectionModel().select("AM");
        }
        if (endTime.getHour() > 12) {
            appEndAmPmCombo.getSelectionModel().select("PM");
        } else {
            appEndAmPmCombo.getSelectionModel().select("AM");
        }
    }

    /**
     * Given an int value and a combo box, returns the index of the comboBox item matching that value. The selectInitialTimes
     * method passes hour and minute values, and this method then returns the index matching that value.
     * @param value The value being searched for.
     * @param comboBox The combo box to search.
     * @param convertTo12Hour Used to account for 12-hour times.*/
    private int findMatchIndexInCombo(int value, ComboBox<String> comboBox, boolean convertTo12Hour) {
        int index = 0;
        if (convertTo12Hour && value > 12) { value -= 12;}
        for (String item : comboBox.getItems()) {
            int itemInt = Integer.parseInt(item);
            if (itemInt == value) {
                return index;
            } else if (itemInt > value) {
                int diff = itemInt - value;
                if (diff < 3) {
                    return index;
                } else {
                    return index++;
                }
            }
            index++;
        }
        return -1;
    }
}
