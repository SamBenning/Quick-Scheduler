package sample.dao.impl;

import javafx.collections.ObservableList;
import sample.dao.AppointmentDao;
import sample.model.Appointment;

import java.sql.SQLException;

public class AppointmentDaoImpl implements AppointmentDao {

    @Override
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        return null;
    }

    @Override
    public ObservableList<Appointment> getAppointment(String title) throws SQLException {
        return null;
    }

    @Override
    public boolean updateAppointment(int existingId, Appointment newAppointment) {
        return true;
    }

    @Override
    public boolean addAppointment(Appointment appointment) throws SQLException {
        return true;
    }

    @Override
    public boolean deleteAppointment(Appointment appointment) throws SQLException {
        return true;
    }
}
