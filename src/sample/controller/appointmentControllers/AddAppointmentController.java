package sample.controller.appointmentControllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.dao.AppointmentDao;
import sample.model.Appointment;
import sample.util.JavaFXUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddAppointmentController {
    public TextField appTitleField;
    public TextField appDescriptionField;
    public TextField appLocationField;
    public TextField appTypeField;
    public DatePicker appStartDate;
    public TextField appStartTime;
    public DatePicker appEndDate;
    public TextField appEndTime;
    public ComboBox appCustomerCombo;
    public ComboBox appUserCombo;
    public ComboBox appContactCombo;
    public Button cancelButton;
    public Button saveButton;
    private ObservableList<Appointment> appointments;

    public AddAppointmentController(ObservableList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    /**
     * Grabs values from form fields and uses them to generate an Appointment object.
     * Appointment is added to the database, the appointments ObservableList is refreshed,
     * and the window is closed.*/
    public void saveButtonHandler(ActionEvent actionEvent) {

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate startDate = appStartDate.getValue();
        String startTimeString = appStartTime.getText();
        LocalTime startTime = LocalTime.parse(startTimeString, timeFormat);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        LocalDate endDate = appEndDate.getValue();
        String endTimeString = appEndTime.getText();
        LocalTime endTime = LocalTime.parse(endTimeString, timeFormat);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        Appointment newAppointment = new Appointment(
                appTitleField.getText(),
                appDescriptionField.getText(),
                appLocationField.getText(),
                appTypeField.getText(),
                startDateTime,
                endDateTime,
                0,
                0,
                0
        );

        if (AppointmentDao.addAppointment(newAppointment)) {
            appointments.setAll(AppointmentDao.getAllAppointments());
            JavaFXUtil.closeWindow(actionEvent);
        }
    }
}
