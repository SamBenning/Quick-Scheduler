package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.dao.UserDao;
import sample.model.AppointmentType;
import sample.util.JavaFXUtil;
import sample.util.report.ContactScheduleReport;
import sample.util.report.Report;
import sample.util.report.TypeMonthReport;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField usernameField;
    public TextField passwordField;
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * ~~LAMBDA EXPRESSION~~
         * <br>
         * Implements the runnable interface via lambda expression for threading. Initializes an ObservableList
         * of appointment types and stores it in the static member variable of AppointmentType class for populating
         * combo boxes in the main program.*/
        Runnable runnable = () -> {
            System.out.println("I'm in a thread!");
            ObservableList<AppointmentType> appTypes = FXCollections.observableArrayList();
            appTypes.add(new AppointmentType("Planning Session"));
            appTypes.add(new AppointmentType("De-Briefing"));
            appTypes.add(new AppointmentType("Contract Review"));
            appTypes.add(new AppointmentType("Coffee Break"));
            appTypes.add(new AppointmentType("Video Conference"));
            appTypes.add(new AppointmentType("Product Training"));
            AppointmentType.setAllTypes(appTypes);

            ObservableList<Report> reports = FXCollections.observableArrayList();
            reports.add(new TypeMonthReport("Appointment count by Type/Month"));
            reports.add(new ContactScheduleReport("Contact schedule"));
            Report.setReports(reports);

        };
        Thread initAppTypesThread = new Thread(runnable);
        initAppTypesThread.start();
    }

    public void attemptLoginHandler(ActionEvent actionEvent) {
        boolean credentialsValid = UserDao.checkCredentials(usernameField.getText(), passwordField.getText());
        if (credentialsValid) {
            JavaFXUtil.changeWindow(actionEvent,
                    "/sample/view/mainForm.fxml",900, 500);
        }
    }
}
