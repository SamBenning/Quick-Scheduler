package sample.util.report;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dao.AppointmentDao;
import sample.dao.ContactDao;
import sample.model.Appointment;
import sample.model.Contact;

import java.time.LocalDateTime;

/**
 *Defines the Contact Schedule report type. */
public class ContactScheduleReport extends Report {

    private static Contact contact;
    private static ComboBox<Contact> contacts;

    public ContactScheduleReport(String name) {
        super(name);
    }

    /**
     * Generates a ComboBox containing a list of contacts queried from the database and displays it in dynamicComboArea.
     * Also adds an event handler to the combo box so that when a selection is made, the setContact method is called.
     * Both the dynamicComboArea and dynamicTableArea are cleared to get rid of anything displayed by previously selected
     * reports.*/
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

    /**
     * Saves the contact that was selected in the combo box to the contact member variable. Calls setTable, which
     * will generate a tableview based on the contact.*/
    public static void setContact() {
        contact = contacts.getSelectionModel().getSelectedItem();
        setTable();
    }

    /**
     * Generates a table view based on the contact member variable. The table displays a schedule for the selected contact
     * in the dynamicTableArea.
     * */
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
        return () -> askContact();
    }
}
