package sample.util.report;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dao.AppointmentDao;
import sample.dao.CustomerDao;
import sample.dao.UserDao;
import sample.model.Appointment;
import sample.model.Customer;
import sample.model.User;

public class UserActivityReport extends Report{

    private static User user;
    private static ComboBox<User> users;

    public UserActivityReport(String name) {
        super(name);
    }

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

    private static void setTables() {
        dynamicTableArea.getChildren().clear();
        user = users.getSelectionModel().getSelectedItem();
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

    @Override
    public ReportListener getOnSelection() {
        return () -> askUser();
    }
}
