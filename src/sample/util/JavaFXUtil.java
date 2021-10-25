package sample.util;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controller.appointmentControllers.AddAppointmentController;
import sample.controller.appointmentControllers.ModifyAppointmentController;
import sample.controller.customerControllers.AddCustomerController;
import sample.controller.customerControllers.ModifyCustomerController;
import sample.model.Appointment;
import sample.model.Customer;

import java.io.IOException;

/**
 * Utility class that provides a number of functions for showing and changing windows and scenes.*/
public final class JavaFXUtil {

    /**
     * Takes an action event and closes the stage of the source of the action event. Used to close
     * the add/modify windows for customers and appointments in the application.
     * @param actionEvent Used to obtain a stage*/
    public static void closeWindow(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the Add Appointment menu in a new window. Sets modality so that main application cannot be interacted
     * with while this window is open.*/
    public static void showAddAppWindow(ActionEvent actionEvent, String resourceLocation, ObservableList<Appointment> list) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            loader.setControllerFactory(AddAppointmentController -> new AddAppointmentController(list));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the Modify Appointment menu in a new window. Sets modality so that main application cannot be interacted
     * with while this window is open.*/
    public static void showModifyAppWindow(ActionEvent actionEvent, String resourceLocation,
                                           ObservableList<Appointment> list, Appointment appointment) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            loader.setControllerFactory(ModifyAppointmentController -> new ModifyAppointmentController(appointment, list));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the Add Customer menu in a new window. Sets modality so that main application cannot be interacted
     * with while this window is open.*/
    public static void showAddCustomerWindow(ActionEvent actionEvent, String resourceLocation, ObservableList<Customer> list) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            loader.setControllerFactory(AddCustomerController -> new AddCustomerController(list));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the Modify Customer menu in a new window. Sets modality so that main application cannot be interacted
     * with while this window is open.*/
    public static void showModifyCustomerWindow(ActionEvent actionEvent, String resourceLocation,
                                                ObservableList<Customer> list, Customer customer) {
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(JavaFXUtil.class.getResource(resourceLocation));
            loader.setControllerFactory(AddCustomerController -> new ModifyCustomerController(customer, list));
            newWindow.setScene(new Scene(loader.load()));
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the scene. Gets the stage from the passed in actionEvent and sets the scene to a new scene
     * which is generated from the resourceLocation passed in. Also takes integers width and height to set
     * the window size.
     * @param actionEvent The window calling this must pass in an action event so that a Stage can be obtained.
     * @param resourceLocation A string representing the file location of the fxml file for the new scene.
     * @param width The width in pixels of the new window.
     * @param height The height in pixels of the new window.*/
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
