package sample.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class AlertUtil {

    public static void showCustomerErrorAlert(String alertString) {
        Alert alert = new Alert(Alert.AlertType.ERROR, alertString, ButtonType.OK);
        alert.setHeaderText("Encountered error(s) performing selected action.");
        alert.showAndWait();
    }

}
