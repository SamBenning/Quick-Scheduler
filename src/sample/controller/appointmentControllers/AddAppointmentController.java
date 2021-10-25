package sample.controller.appointmentControllers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import sample.dao.AppointmentDao;
import sample.model.Appointment;
import sample.util.JavaFXUtil;
import sample.util.ValidationUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Add Appointment form. Most functionality is contained in superclass AppointmentController.
 * This class mostly just handles button presses, passes data into the ValidationUtil, and initializes the combo boxes.*/
public class AddAppointmentController extends AppointmentController implements Initializable {

    /**
     * @param appointments Appointment list is passed in from the mainForm so that this can add the new appointment to it.*/
    public AddAppointmentController(ObservableList<Appointment> appointments) {
        super(appointments);
        System.out.println((Integer)getLocalOffsetFromEst());
    }

    /**
     * Calls init functions to set up combo boxes.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCustomers(customers);
        initUsers(users);
        initContacts(contacts);
        initTypes();
        initTimeCombos();
    }

    /**
     * Closes the window.*/
    public void cancelButtonHandler(ActionEvent actionEvent) {
        JavaFXUtil.closeWindow(actionEvent);
    }

    /**
     * Grabs values from form fields and uses them to generate an Appointment object.
     * Appointment is added to the database, the appointments ObservableList is refreshed,
     * and the window is closed.*/
    public void saveButtonHandler(ActionEvent actionEvent) {
        try {
            setDate();
            setStartDateTime();
            setEndDateTime();
            hasEmptyDateTimeCombo = false;
        } catch (Exception e) {
            hasEmptyDateTimeCombo = true;
        }

        if (ValidationUtil.validateAppointment(this)) {
            addAppToDb();
            JavaFXUtil.closeWindow(actionEvent);
        }
    }

    /**
     * Calls addAppointment from AppointmentDao to actually add appointment to the database.*/
    private void addAppToDb() {
        if(AppointmentDao.addAppointment(instantiateAppointment())) {
            appointments.setAll(AppointmentDao.getAllAppointments());
        }
    }
}
