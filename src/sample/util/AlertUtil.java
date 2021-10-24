package sample.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class AlertUtil {

    public static void showErrorAlert(String alertString) {
        Alert alert = new Alert(Alert.AlertType.ERROR, alertString, ButtonType.OK);
        alert.setHeaderText("Encountered error(s) performing selected action.");
        alert.showAndWait();
    }

    public static boolean warnDeleteAssociatedAppointments() {
        String alertString = "Are you sure you wish to permanently delete all appointments associated with this customer?";
        Alert alert = new Alert(Alert.AlertType.WARNING,alertString, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("This action will cause appointments to be deleted.");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    public static boolean confirmDeletion() {
        String alertString = "Are you sure you wish to delete the selected item?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertString, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirm deletion.");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

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

}
