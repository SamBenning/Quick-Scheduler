package sample.controller.appointmentControllers;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.dao.ContactDao;
import sample.dao.CustomerDao;
import sample.dao.UserDao;
import sample.model.*;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class AppointmentController {
    public TextField appTitleField;
    public TextField appDescriptionField;
    public TextField appLocationField;
    public TextField appIdField;
    public DatePicker appDate;
    public ComboBox appStartTime;
    public ComboBox appEndTime;
    public ComboBox<String> appStartHourCombo;
    public ComboBox<String> appStartMinuteCombo;
    public ComboBox<String> appStartAmPmCombo;
    public ComboBox<String> appEndHourCombo;
    public ComboBox<String> appEndMinuteCombo;
    public ComboBox<String> appEndAmPmCombo;
    public ComboBox<String> appCustomerCombo;
    public ComboBox<String> appUserCombo;
    public ComboBox<String> appContactCombo;
    public Button cancelButton;
    public Button saveButton;
    public ComboBox appTypeCombo;
    protected ObservableList<Appointment> appointments;
    protected ObservableList<Customer> customers;
    protected HashMap<String, Integer> customerToIdMap;
    protected ObservableList<User> users;
    protected HashMap<String, Integer> userToIdMap;
    protected ObservableList<Contact> contacts;
    protected HashMap<String, Integer> contactToIdMap;
    protected LocalDate date;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    protected boolean hasEmptyDateTimeCombo;

    public AppointmentController(ObservableList<Appointment> appointments) {

        customerToIdMap = new HashMap<>();
        userToIdMap = new HashMap<>();
        contactToIdMap = new HashMap<>();
        this.appointments = appointments;
        this.customers = CustomerDao.getAllCustomers();
        this.users = UserDao.getAllUsers();
        this.contacts = ContactDao.getAllContacts();
    }

    protected void initCustomers(ObservableList<Customer> customers) {
        for (Customer customer: customers) {
            int id = customer.getCustomerId();
            String idString = "";
            if (id < 100) {
                idString += "0";
            }
            if (id < 10) {
                idString += "0";
            }
            idString += id;
            String comboString = "(" + idString + ") - " + customer.getCustomerName();
            customerToIdMap.put(comboString, id);
        }
        ArrayList<String> keys = new ArrayList<>(customerToIdMap.keySet());
        Collections.sort(keys);
        appCustomerCombo.getItems().addAll(keys);
    }

    protected void initUsers(ObservableList<User> users) {
        for (User user: users) {
            String comboString = "(" + user.getUserId() + ") - " + user.getUserName();
            Integer id = user.getUserId();
            userToIdMap.put(comboString, id);
        }
        ArrayList<String> keys = new ArrayList<>(userToIdMap.keySet());
        Collections.sort(keys);
        appUserCombo.getItems().addAll(keys);
    }

    protected void initContacts(ObservableList<Contact> contacts) {
        for (Contact contact: contacts) {
            String comboString = "(" + contact.getContactId() + ") - " + contact.getContactName();
            Integer id = contact.getContactId();
            contactToIdMap.put(comboString, id);
        }
        ArrayList<String> keys = new ArrayList<>(contactToIdMap.keySet());
        Collections.sort(keys);
        appContactCombo.getItems().addAll(keys);
    }

    protected void initTypes() {
        appTypeCombo.getItems().setAll(AppointmentType.getAllTypes());
    }

    protected void initTimeCombos() {
        System.out.println("yoyo");
        ArrayList<String> hours = new ArrayList<>();
        ArrayList<String> minutes = new ArrayList<>();
        ArrayList<String> amPm = new ArrayList<>();
        minutes.add("00");
        for(int i = 5; i < 60; i+=5) {
            int hour = i/5;
            int minute = i;
            if (hour < 10) {
                hours.add("0" + hour);
            } else {
                hours.add(Integer.toString(hour));
            }
            if (minute == 5) {
                minutes.add("05");
            } else {
                minutes.add(Integer.toString(minute));
            }
        }
        hours.add("12");
        amPm.add("AM");
        amPm.add("PM");
        appStartHourCombo.getItems().addAll(hours);
        appStartMinuteCombo.getItems().addAll(minutes);
        appStartAmPmCombo.getItems().addAll(amPm);
        appEndHourCombo.getItems().addAll(hours);
        appEndMinuteCombo.getItems().addAll(minutes);
        appEndAmPmCombo.getItems().addAll(amPm);

    }

    protected Appointment instantiateAppointment () {

        int customerId = customerToIdMap.get(appCustomerCombo.getSelectionModel().getSelectedItem());
        int userId = userToIdMap.get(appUserCombo.getSelectionModel().getSelectedItem());
        int contactId = contactToIdMap.get(appContactCombo.getSelectionModel().getSelectedItem());

        return new Appointment(
                appTitleField.getText(),
                appDescriptionField.getText(),
                appLocationField.getText(),
                appTypeCombo.getSelectionModel().getSelectedItem().toString(),
                startDateTime,
                endDateTime,
                customerId,
                userId,
                contactId
        );
    }

    protected int getLocalOffsetFromEst() {
        ZoneId systemZone = ZoneId.systemDefault();
        ZoneId estZone = ZoneId.of("America/New_York");
        Instant now = Instant.now();
        ZoneOffset offsetForSystemZone = systemZone.getRules().getOffset(now);
        ZoneOffset estOffset = estZone.getRules().getOffset(now);
        return estOffset.compareTo(offsetForSystemZone);
    }

    private LocalDateTime getLocalDateTime(ComboBox<String> hourCombo, ComboBox<String>minuteCombo,ComboBox<String> amPmCombo) {
        String timeString = hourCombo.getSelectionModel().getSelectedItem() + ":" + minuteCombo.getSelectionModel().getSelectedItem();
        LocalTime time = LocalTime.parse(timeString, timeFormat);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if(amPmCombo.getSelectionModel().getSelectedItem().equals("PM")) {
            return dateTime.plusHours(12);
        }
        return dateTime;
    }

    protected void setDate() {
        date = appDate.getValue();
    }

    protected void setStartDateTime() {
        startDateTime = getLocalDateTime(appStartHourCombo, appStartMinuteCombo, appStartAmPmCombo);
    }

    protected void setEndDateTime() {
        endDateTime = getLocalDateTime(appEndHourCombo, appEndMinuteCombo, appEndAmPmCombo);
    }

    public boolean hasEmptyDateTimeCombo() {
        return hasEmptyDateTimeCombo;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public int getSelectedCustomerId() {
        return customerToIdMap.get(appCustomerCombo.getSelectionModel().getSelectedItem());
    }

}
