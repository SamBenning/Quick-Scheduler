package sample.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

}
