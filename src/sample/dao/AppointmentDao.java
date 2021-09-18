package sample.dao;



import javafx.collections.ObservableList;
import sample.model.Appointment;

import java.sql.SQLException;

public interface AppointmentDao {

    public ObservableList<Appointment> getAllAppointments () throws SQLException;
    public ObservableList<Appointment> getAppointment (String title) throws SQLException;
    public boolean addAppointment (Appointment appointment) throws SQLException;
    public boolean updateAppointment(int existingID, Appointment newAppointment);
    public boolean deleteAppointment (Appointment appointment) throws SQLException;

}
