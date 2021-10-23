package sample.dao;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Appointment;
import sample.model.AppointmentType;
import sample.util.QueryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public final class AppointmentDao {

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments;";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Appointment newAppointment = generateAppointment(resultSet);
                appointments.add(newAppointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("null ptr exception");
        }
        return appointments;
    }

    public static ArrayList<Appointment> getAppointmentsForCustomer(int customerId) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = QueryUtil.getAppointmentForCustomerPreparedStatement(customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Appointment newAppointment = generateAppointment(resultSet);
                appointments.add(newAppointment);
            }

        } catch (SQLException e) {
                e.printStackTrace();
        }
        return  appointments;
    }

    public static ObservableList<Appointment> getAppointmentsInDateRange(LocalDate firstDate, LocalDate lastDate) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = QueryUtil.getAppointmentInDateRange(firstDate, lastDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = generateAppointment(resultSet);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static Integer getTypeMonthCount (AppointmentType type, Month month) {
        int count = 0;
        String typeString = type.getName();
        int startCountYear = 2000;
        int endCountYear = 2100;

        try {
            for (int i = startCountYear; i <= endCountYear; i++) {

                LocalDateTime firstDayOfMonth = LocalDate.of(i, month.getValue(), 1).atStartOfDay();
                LocalDateTime lastDayOfMonth = LocalDate.of(i, month.getValue(), month.minLength()).atTime(23, 59, 59);
                PreparedStatement preparedStatement = QueryUtil.getTypeMonthCountPreparedStatement(typeString, firstDayOfMonth, lastDayOfMonth);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count += resultSet.getInt("Count");
                }
            }
        } catch (SQLException e) {
                return -1;
            }
        return count;
        }


    public ObservableList<Appointment> getAppointment(String title) throws SQLException {
        return null;
    }

    public static Appointment getLastAddedAppointment() {
        try {
            String query = "select * from appointments order by Appointment_ID DESC limit 1;";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return generateAppointment(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean updateAppointment(int existingId, Appointment newAppointment) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getUpdateAppointmentPreparedStatement(existingId, newAppointment);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static boolean addAppointment(Appointment appointment) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getAddAppointmentPreparedStatement(appointment);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean deleteAppointment (Appointment appointment) throws SQLException {
        return true;
    }

    private static Appointment generateAppointment (ResultSet resultSet) throws SQLException {
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
        return newAppointment;
    }

}
