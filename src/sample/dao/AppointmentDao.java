package sample.dao;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Appointment;
import sample.util.QueryUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class AppointmentDao {

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments;";
            Statement statement = JDBC.connection.createStatement();
            String tableName = "appointments";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Appointment newAppointment = new Appointment(
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID")
                );
                newAppointment.setAppointmentId(resultSet.getInt("Appointment_ID"));
                newAppointment.setCustomer(QueryUtil.getCustomerString(newAppointment.getCustomerId()));
                newAppointment.setUser(QueryUtil.getUserString(newAppointment.getUserId()));
                newAppointment.setContact(QueryUtil.getContactString(newAppointment.getContactId()));
                appointments.add(newAppointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("null ptr exception");
        }
        return appointments;
    }


    public ObservableList<Appointment> getAppointment(String title) throws SQLException {
        return null;
    }


    public boolean updateAppointment(int existingId, Appointment newAppointment) {
        return true;
    }

    public boolean addAppointment(Appointment appointment) throws SQLException {
        return true;
    }

    public boolean deleteAppointment(Appointment appointment) throws SQLException {
        return true;
    }
}
