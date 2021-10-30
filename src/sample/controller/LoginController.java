package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.dao.UserDao;
import sample.model.AppointmentType;
import sample.util.AlertUtil;
import sample.util.JavaFXUtil;
import sample.util.report.ContactScheduleReport;
import sample.util.report.Report;
import sample.util.report.TypeMonthReport;
import sample.util.report.UserActivityReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * First, attempts to get the resource bundle based on system locale. Because the only properties file is for french,
     * this will fail if the user's system is set to English(or any other language), and thus, everything will remain
     * in English. If getBundle succeeds, translation to french is performed.
     *<br>
     * ~~LAMBDA EXPRESSION~~
     * <br>
     * Implements the runnable interface via lambda expression for threading. Initializes an ObservableList
     * of appointment types and stores it in the static member variable of AppointmentType class for populating
     * combo boxes in the main program. Also initializes reports ObservableList.*/
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

    /**
     * Grabs values from username and password fields and queries the database via UserDao.checkCredentials.
     * If the credentials are valid, the main menu is rendered. If not, an error alert is displayed. The locale is passed
     * into the AlertUtil.showInvaidCredentialsAlert so translation can be performed on the alert if necessary.*/
    public void attemptLoginHandler(ActionEvent actionEvent) {
        boolean credentialsValid = UserDao.checkCredentials(usernameField.getText(), passwordField.getText());
        passwordField.clear();
        if (credentialsValid) {
            JavaFXUtil.changeWindow(actionEvent,
                    "/sample/view/mainForm.fxml",1000, 550);
        } else {
            AlertUtil.showInvalidCredentialsAlert(Locale.getDefault(), rb);
        }
        try {
            writeLoginAttempt(credentialsValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles translating JavaFX nodes to French. Admittedly, this method is a bit silly(I could
     * have just manually set all elements), but it was fun to write!*/
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

    /**
     * Helper method for translate.*/
    private void tryLabel(Node node) throws ClassCastException {
        Label label = (Label) node;
        label.setText(rb.getString(label.getText()));
    }

    /**
     * Helper method for translate.*/
    private void tryTextField (Node node) throws ClassCastException {
        TextField textField = (TextField) node;
        textField.setPromptText(rb.getString(textField.getPromptText()));
    }

    /**
     * Helper method for translate.*/
    private void tryButton (Node node) throws ClassCastException {
        Button button = (Button) node;
        button.setText(rb.getString(button.getText()));
    }

    /**
     * Writes to Login_Summary.txt to keep track of all login attempts. Logs system time, time zone, the username
     * entered, and whether the attempt was successful.*/
    private void writeLoginAttempt(boolean success) throws IOException {
        File file = new File("login_activity.txt");
        ZonedDateTime time = ZonedDateTime.now();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
        printWriter.append("\n" + time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        printWriter.append( "\n \t" + "User Entered: " + usernameField.getText());
        printWriter.append( "\n \t" + "Successful: " + success);
        printWriter.close();
    }
}
