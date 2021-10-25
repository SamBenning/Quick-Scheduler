package sample.util.report;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dao.AppointmentDao;
import sample.dao.UserDao;
import sample.model.Appointment;
import sample.model.User;

/**
 * Defines the appointment by user report type.*/
public class UserActivityReport extends Report{

    private static ComboBox<User> users;

    public UserActivityReport(String name) {
        super(name);
    }

    /**
     * ~~LAMBDA EXPRESSION~~
     * <br>
     * Creates a combo box populated with users queried from the database. Adds the combo combo box to dynamicComboArea,
     * and adds an event handler to call the setTables method when a selection is made.
     * <br>
     * A lambda expression is used to define the event handler. A lambda expression is convenient here, as it
     * can simply be passed in as a parameter to the addEventHandler method.*/
    public static void askUser() {
        dynamicComboArea.getChildren().clear();
        dynamicTableArea.getChildren().clear();
        users = new ComboBox<>();
        users.setPromptText("Select user");
        setMargin(users);
        users.getItems().setAll(UserDao.getAllUsers());
        users.addEventHandler(ActionEvent.ACTION,
                actionEvent -> setTables());
        dynamicComboArea.getChildren().add(users);
    }

    /**
     * Queries the database for appointments matching the user selected and generates a table based on the result.*/
    private static void setTables() {
        dynamicTableArea.getChildren().clear();
        User user = users.getSelectionModel().getSelectedItem();
        TableView<Appointment> appointmentTableView = new TableView<>();
        TableColumn<Appointment, Integer> appointmentId = new TableColumn<>("Appointment_ID");
        TableColumn<Appointment, String> appointmentTitle = new TableColumn<>("Title");
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        appointmentTableView.getColumns().add(appointmentId);
        appointmentTableView.getColumns().add(appointmentTitle);

        appointmentTableView.setItems(AppointmentDao.getAppointmentsByUser(user));

        dynamicTableArea.getChildren().add(appointmentTableView);


    }

    /**
     * ~~LAMBDA EXPRESSION~~
     * <br>
     * Provides a lambda expression returning the function. The lambda expression is useful in this case because it allows
     * any of the report subclass on selection functionality to be called from the MainMenu. The Main Controller simply
     * gets the selected report class form a combo box and calls selection.getOnSelection.onSelectReport. Each Report
     * subclass simply has to define its required functionality in the getOnSelection method and return it as a callback,
     * which the lambda expression does in a concise manner.*/
    @Override
    public ReportListener getOnSelection() {
        return () -> askUser();
    }
}
