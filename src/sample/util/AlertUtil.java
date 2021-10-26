package sample.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sample.model.Appointment;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class that provides several methods for throwing alerts.*/
public abstract class AlertUtil {

    /**
     * Shows an error alert with custom text.
     * @param alertString The text to be displayed in the alert.*/
    public static void showErrorAlert(String alertString) {
        Alert alert = new Alert(Alert.AlertType.ERROR, alertString, ButtonType.OK);
        alert.setHeaderText("Encountered error(s) performing selected action.");
        alert.showAndWait();
    }

    /**
     * Shows a specific warning to be shown when a user attempts to delete a customer that has associated appointments.
     * @return Returns true if the user clicks yes to confirm, false otherwise.*/
    public static boolean warnDeleteAssociatedAppointments() {
        String alertString = "Are you sure you wish to permanently delete all appointments associated with this customer?";
        Alert alert = new Alert(Alert.AlertType.WARNING,alertString, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("This action will cause appointments to be deleted.");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Shows a confirmation alert prompting user to confirm a deletion operation.
     * @return. Returns true if user selects yes, false otherwise.*/
    public static boolean confirmDeletion() {
        String alertString = "Are you sure you wish to delete the selected item?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertString, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirm deletion.");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Shows an alert when the user enters an incorrect user or password on the log-in screen. Can display in French or
     * English.
     * @param locale Used for translation purposes. If a French locale is passed in, the alert is translated to French.
     * Otherwise, the default English text will appear.
     * @param rb A resource bundle containing the translation key-value pairs so that text can be translated.*/
    public static void showInvalidCredentialsAlert(Locale locale, ResourceBundle rb) {
        String title = "Error";
        String header = "Invalid username or password";
        String alertString = "Please make sure you entered your log-in credentials correctly.";
        if (locale.equals(Locale.FRENCH)) {
            title = rb.getString(title);
            header = rb.getString(header);
            alertString = rb.getString(alertString);

        }
        Alert alert = new Alert(Alert.AlertType.ERROR, alertString, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    /**
     * Displays a customer information alert based on an appointment instance.
     * @param appointment An appointment instance used to generate customer message.*/
    public static void showUpcomingAppointment(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String alertString = "ID: " + appointment.getAppointmentId() +
                "\nTitle: " + appointment.getTitle() +
                "\nDescription: " + appointment.getDescription() +
                "\nStart Time: " + appointment.getStart().format(formatter) +
                "\nEnd Time: " + appointment.getEnd().format(formatter);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, alertString, ButtonType.OK);
        alert.setTitle("Welcome!");
        alert.setHeaderText("A scheduled appointment will begin soon!");
        alert.showAndWait();
    }

    /**
     * Overloaded method to handle showing an alert when no upcoming appointments are found.*/
    public static void showUpcomingAppointment() {
        String alertString = "Scheduled appointments can be viewed in the Appointments tab.";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, alertString, ButtonType.OK);
        alert.setTitle("Welcome!");
        alert.setHeaderText("You do not have any appointments within the next 15 minutes.");
        alert.showAndWait();
    }
}
