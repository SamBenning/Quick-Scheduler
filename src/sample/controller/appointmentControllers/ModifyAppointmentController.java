package sample.controller.appointmentControllers;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import sample.controller.customerControllers.ModifyCustomerController;
import sample.dao.CustomerDao;
import sample.model.Appointment;
import sample.model.Customer;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ModifyAppointmentController extends AppointmentController implements Initializable {
    private Appointment selectedAppointment;
    private HashMap<Integer, String> idToCustomerMap;
    private HashMap<Integer, String> idToUserMap;
    private HashMap<Integer, String> idToContactMap;

    public ModifyAppointmentController(Appointment selectedAppointment, ObservableList<Appointment> appointments) {
        super(appointments);
        this.selectedAppointment = selectedAppointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCustomers(customers);
        initUsers(users);
        initContacts(contacts);
        initTimeCombos();
        idToCustomerMap = new HashMap<>();
        idToUserMap = new HashMap<>();
        idToContactMap = new HashMap<>();
        appTitleField.setText(selectedAppointment.getTitle());
        appDescriptionField.setText(selectedAppointment.getDescription());
        appLocationField.setText(selectedAppointment.getLocation());
        appTypeField.setText(selectedAppointment.getType());
        selectInitialCustomer();
        selectInitialUser();
        selectInitialContact();
        selectInitialDate();
        selectInitialTimes();
    }

    private void selectInitialCustomer() {
        Customer customer = CustomerDao.getCustomer(selectedAppointment.getCustomerId());
        for (Map.Entry<String, Integer> entry : customerToIdMap.entrySet()) {
            idToCustomerMap.put(entry.getValue(), entry.getKey());
        }
        String selection = idToCustomerMap.get(selectedAppointment.getCustomerId());
        appCustomerCombo.getSelectionModel().select(selection);
    }

    private void selectInitialUser() {
        for (Map.Entry<String, Integer> entry: userToIdMap.entrySet()) {
            idToUserMap.put(entry.getValue(), entry.getKey());
        }
        String selection = idToUserMap.get(selectedAppointment.getUserId());
        appUserCombo.getSelectionModel().select(selection);
    }

    private void selectInitialContact() {
        for (Map.Entry<String, Integer> entry: contactToIdMap.entrySet()) {
            idToContactMap.put(entry.getValue(), entry.getKey());
        }
        String selection = idToContactMap.get(selectedAppointment.getContactId());
        appContactCombo.getSelectionModel().select(selection);
    }

    private void selectInitialDate() {
        LocalDate date = selectedAppointment.getStart().toLocalDate();
        appDate.setValue(date);
    }

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
