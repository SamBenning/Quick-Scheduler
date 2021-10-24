package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import sample.dao.UserDao;
import sample.model.AppointmentType;
import sample.util.AlertUtil;
import sample.util.JavaFXUtil;
import sample.util.report.ContactScheduleReport;
import sample.util.report.Report;
import sample.util.report.TypeMonthReport;
import sample.util.report.UserActivityReport;

import java.io.*;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public VBox loginMainContainer;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label localIdLabel;

    private ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try{
            rb = ResourceBundle.getBundle("sample/util/Login", Locale.getDefault());
            System.out.println("French Locale detected. Translating.");
            translate(loginMainContainer.getChildren());
            usernameField.setPromptText(rb.getString(usernameField.getPromptText()));
            passwordField.setPromptText(rb.getString(passwordField.getPromptText()));
            localIdLabel.setText(localIdLabel.getText() + " " + ZoneId.systemDefault());
            System.out.println(ZoneId.systemDefault());
        } catch (MissingResourceException e) {
            localIdLabel.setText(localIdLabel.getText() + " " + ZoneId.systemDefault());
        }

        
        

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
            reports.add(new UserActivityReport("User database entries"));
            Report.setReports(reports);

        };
        Thread initAppTypesThread = new Thread(runnable);
        initAppTypesThread.start();
    }

    public void attemptLoginHandler(ActionEvent actionEvent) {
        boolean credentialsValid = UserDao.checkCredentials(usernameField.getText(), passwordField.getText());
        passwordField.clear();
        if (credentialsValid) {
            JavaFXUtil.changeWindow(actionEvent,
                    "/sample/view/mainForm.fxml",900, 500);
        } else {
            AlertUtil.showInvalidCredentialsAlert(Locale.getDefault(), rb);
        }
        try {
            writeLoginAttempt(credentialsValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void translate(ObservableList<Node> nodes) {

        for (Node node : nodes) {
            try {
                VBox vbox = (VBox) node;
                ObservableList<Node> childrenNodes = vbox.getChildren();
                translate(childrenNodes);
            } catch (ClassCastException e) {
                try {
                    tryLabel(node);
                } catch (ClassCastException cc1) {
                    try {
                        tryTextField(node);
                    } catch (ClassCastException cc2) {
                        try {
                            tryButton(node);
                        } catch (ClassCastException ignored) {
                        }
                    }
                }
            }
        }
    }

    private void tryLabel(Node node) throws ClassCastException {
        Label label = (Label) node;
        label.setText(rb.getString(label.getText()));
    }

    private void tryTextField (Node node) throws ClassCastException {
        TextField textField = (TextField) node;
        textField.setPromptText(rb.getString(textField.getPromptText()));
    }

    private void tryButton (Node node) throws ClassCastException {
        Button button = (Button) node;
        button.setText(rb.getString(button.getText()));
    }

    private void writeLoginAttempt(boolean success) throws IOException {
        File file = new File("Login_Summary.txt");
        ZonedDateTime time = ZonedDateTime.now();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
        printWriter.append("\n" + time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        printWriter.append( "\n \t" + "User Entered: " + usernameField.getText());
        printWriter.append( "\n \t" + "Successful: " + success);
        printWriter.close();
    }

}
