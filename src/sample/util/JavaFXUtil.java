package sample.util;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controller.Controller;
import sample.controller.appointmentControllers.AddAppointmentController;
import sample.controller.customerControllers.AddCustomerController;
import sample.model.Appointment;
import sample.model.Customer;

import java.io.IOException;

public final class JavaFXUtil {

    public static void closeWindow(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNewWindow(ActionEvent actionEvent, String resourceLocation) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAddAppWindow(ActionEvent actionEvent, String resourceLocation, ObservableList<Appointment> list) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            //loader.setController(controller);
            loader.setControllerFactory(AddAppointmentController -> new AddAppointmentController((ObservableList<Appointment>) list));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAddCustomerWindow(ActionEvent actionEvent, String resourceLocation, ObservableList<Customer> list) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            //loader.setController(controller);
            loader.setControllerFactory(AddCustomerController -> new AddCustomerController(list));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeWindow(ActionEvent actionEvent, String resourceLocation, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
