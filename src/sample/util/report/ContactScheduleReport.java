package sample.util.report;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.dao.AppointmentDao;
import sample.dao.ContactDao;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;

import java.time.LocalDateTime;

public class ContactScheduleReport extends Report {

    private static Contact contact;
    private static ComboBox<Contact> contacts;

    public ContactScheduleReport(String name) {
        super(name);
    }

    public static void askContact() {
        dynamicComboArea.getChildren().clear();
        dynamicTableArea.getChildren().clear();
        contacts = new ComboBox<>();
        setMargin(contacts);
        contacts.setPromptText("Select contact");
        contacts.getItems().setAll(ContactDao.getAllContacts());
        contacts.addEventHandler(ActionEvent.ACTION,
                actionEvent -> setContact());
        dynamicComboArea.getChildren().add(contacts);
    }

    public static void setContact() {
        contact = contacts.getSelectionModel().getSelectedItem();
        setTable();
    }

    private static void setTable() {
        dynamicTableArea.getChildren().clear();
        TableView<Appointment> appointmentTableView = new TableView<>();

        TableColumn<Appointment, Integer> id = new TableColumn<>("Appointment_ID");
        TableColumn<Appointment, String> title = new TableColumn<>("Title");
        TableColumn<Appointment, String> description = new TableColumn<>("Description");
        TableColumn<Appointment, String> location = new TableColumn<>("Location");
        TableColumn<Appointment, String> type = new TableColumn<>("Type");
        TableColumn<Appointment, LocalDateTime> start = new TableColumn<>("Start");
        TableColumn<Appointment, LocalDateTime> end = new TableColumn<>("End");
        TableColumn<Appointment, Integer> customer = new TableColumn<>("Customer_ID");

        id.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        appointmentTableView.getColumns().add(id);
        appointmentTableView.getColumns().add(title);
        appointmentTableView.getColumns().add(description);
        appointmentTableView.getColumns().add(location);
        appointmentTableView.getColumns().add(type);
        appointmentTableView.getColumns().add(start);
        appointmentTableView.getColumns().add(end);
        appointmentTableView.getColumns().add(customer);
        appointmentTableView.setItems(AppointmentDao.getAppointmentsByContact(contact));
        dynamicTableArea.getChildren().add(appointmentTableView);
    }

    @Override
    public ReportListener getOnSelection() {
        return () -> askContact();
    }
}
