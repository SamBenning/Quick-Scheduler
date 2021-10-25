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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Abstract AppointmentController class which holds the bulk of the methods and member variables for the other appointment controllers.*/
public abstract class AppointmentController {
    public TextField appTitleField;
    public TextField appDescriptionField;
    public TextField appLocationField;
    public TextField appIdField;
    public DatePicker appDate;
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
    public ComboBox<AppointmentType> appTypeCombo;
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

    /**
     * Initializes hashmaps for combo box functionality and calls DAOs grab all customers, users, and contacts
     * from the database.
     * @param appointments The list of appointments from the main TableView.*/
    public AppointmentController(ObservableList<Appointment> appointments) {

        customerToIdMap = new HashMap<>();
        userToIdMap = new HashMap<>();
        contactToIdMap = new HashMap<>();
        this.appointments = appointments;
        this.customers = CustomerDao.getAllCustomers();
        this.users = UserDao.getAllUsers();
        this.contacts = ContactDao.getAllContacts();
    }

    /**
     * Initializes the customers combo box.
     * @param customers List of all customer model objects built from database.*/
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

    /**
     * Initializes the users combo box.
     * @param users List of all user model objects built from database.*/
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

    /**
     * Initializes the contacts combo box.
     * @param contacts List of all contacts model objects built from database.*/
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

    /**
     * Initializes the types combo box.
     * <br>
     * Note that this init function doesn't use HashMaps to populate the combo box unlike the other init functions.
     * It simply uses a static observable list within the AppointmentType class, which stores all the appointment types.
     * This is mostly because I wrote the other methods much earlier in the project and didn't realize that combo boxes
     * use the toString method to display text.*/
    protected void initTypes() {
        appTypeCombo.getItems().setAll(AppointmentType.getAllTypes());
    }

    /**
     * Initializes all of the time selection combo boxes.*/
    protected void initTimeCombos() {
        ArrayList<String> hours = new ArrayList<>();
        ArrayList<String> minutes = new ArrayList<>();
        ArrayList<String> amPm = new ArrayList<>();
        minutes.add("00");
        for(int i = 5; i < 60; i+=5) {
            int hour = i/5;
            if (hour < 10) {
                hours.add("0" + hour);
            } else {
                hours.add(Integer.toString(hour));
            }
            if (i == 5) {
                minutes.add("05");
            } else {
                minutes.add(Integer.toString(i));
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

    /**
     * Generates an appointment object based on the input from the text fields and combo boxes.
     * @return Appointment object to be used to add to or update the database.*/
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

    /**
     * Gets the offset between system timezone and EST for the purpose of checking inputs against business hours.
     * @return Integer offset between system time and EST.*/
    protected int getLocalOffsetFromEst() {
        ZoneId systemZone = ZoneId.systemDefault();
        ZoneId estZone = ZoneId.of("America/New_York");
        Instant now = Instant.now();
        ZoneOffset offsetForSystemZone = systemZone.getRules().getOffset(now);
        ZoneOffset estOffset = estZone.getRules().getOffset(now);
        return estOffset.compareTo(offsetForSystemZone);
    }

    /**
     * Builds a LocalDateTime based on the inputs in the time selection combo boxes.
     * @return A LocalDateTime object representing the user inputted time.*/
    private LocalDateTime getLocalDateTime(ComboBox<String> hourCombo, ComboBox<String>minuteCombo,ComboBox<String> amPmCombo) {
        String timeString = hourCombo.getSelectionModel().getSelectedItem() + ":" + minuteCombo.getSelectionModel().getSelectedItem();
        LocalTime time = LocalTime.parse(timeString, timeFormat);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if(amPmCombo.getSelectionModel().getSelectedItem().equals("PM")) {
            return dateTime.plusHours(12);
        }
        return dateTime;
    }

    /**
     * Grabs the date value from the date picker.*/
    protected void setDate() {
        date = appDate.getValue();
    }

    /**
     * Passes values from combo box into the getLocalDateTime function and saves to startDateTime for use in generating
     * appointment object.*/
    protected void setStartDateTime() {
        startDateTime = getLocalDateTime(appStartHourCombo, appStartMinuteCombo, appStartAmPmCombo);
    }

    /**
     * Passes values from combo box into the getLocalDateTime function and saves to endDateTime for use in generating
     * appointment object.*/
    protected void setEndDateTime() {
        endDateTime = getLocalDateTime(appEndHourCombo, appEndMinuteCombo, appEndAmPmCombo);
    }

    /**
     * Used externally to tell whether there is an empty combo box for time selection. Used by ValidationUtil to generate
     * error message.*/
    public boolean hasEmptyDateTimeCombo() {
        return hasEmptyDateTimeCombo;
    }

    /**
     * Used by ValidationUtil to perform checks.
     * @return returns startDateTime generated from user input.*/
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Used by ValidationUtil to perform checks.
     * @return returns endDateTime generated from user input.*/
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Used by ValidationUtil to check for overlapping appointments for customer.
     * @return Returns ID of customer selected in combo box.*/
    public int getSelectedCustomerId() {
        return customerToIdMap.get(appCustomerCombo.getSelectionModel().getSelectedItem());
    }

}
