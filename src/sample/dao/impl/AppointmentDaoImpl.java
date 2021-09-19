package sample.dao.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.dao.AppointmentDao;
import sample.dao.JDBC;
import sample.model.Appointment;
import sample.model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppointmentDaoImpl implements AppointmentDao {

    @Override
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String query = "SELECT * FROM appointments;";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Appointment newAppointment = new Appointment(
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getTimestamp("Create_Date"),
                        resultSet.getString("Created_By"),
                        resultSet.getTimestamp("Last_Update"),
                        resultSet.getString("Last_Updated_By"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID")
                );
                appointments.add(newAppointment);
            }
            JDBC.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("null ptr exception");
        }
        return appointments;
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
