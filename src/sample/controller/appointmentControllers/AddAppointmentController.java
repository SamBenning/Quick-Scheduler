package sample.controller.appointmentControllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.util.JavaFXUtil;

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

    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    public void saveButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }
}
