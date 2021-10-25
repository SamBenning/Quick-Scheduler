package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.*;
import sample.util.QueryUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

/**
 * Handles sending queries and updates to the appointments table in the database.*/
public final class AppointmentDao {

    /**
     * Queries the database and returns an observable list representing all of the appointment records.
     * @return An observable list of Appointment objects*/
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

    /**
     * Queries the database and builds an arraylist based on customer id.
     * @param customerId The query looks for appointments with a Customer_ID matching this value
     * @return An arraylist of Appointment model objects built from appointment records in the database with matching
     * customer id.*/
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

    /**
     * Queries the database and builds an observable list based on date range.
     * @param firstDate The lower bound of the date range to search.
     * @param lastDate The upper bound of the date range to search.
     * @return An Observable List of appointments that lie within the range between the firstDate and the lastDate.*/
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

    /**
     * Queries the database and builds an observable list based on a contact.
     * @param contact A Contact object, from which the contactId is used to look for matching appointments.
     * @return An observable list of appointments matching the contact parameter*/
    public static ObservableList<Appointment> getAppointmentsByContact(Contact contact) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = QueryUtil.getAppointmentsByContactPreparedStatement(contact);
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

    /**Queries the database and builds an observable list based on a user.
     * @param user A User object, from which the userId is used to look for matching appointments.
     * @return An observable list of appointments matching the user parameter.*/
    public static ObservableList<Appointment> getAppointmentsByUser(User user) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = QueryUtil.getAppointmentsByUserPreparedStatement(user);
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

    /**
     * Queries the database and builds an observable list based on a customer.
     * @param customer A Customer object, from which the customerId is used to look for matching appointments.
     * @return An observable list of appointments matching the customer parameter.*/
    public static ObservableList<Appointment> getAppointmentsByCustomer(Customer customer) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = QueryUtil.getAppointmentsByCustomerPreparedStatement(customer);
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

    /**
     * Queries the database for a count of appointments of a given type occurring in a given month. In order to obtain a
     * count not limited to the current year, this method loops from 2000 to 2100 and inputs the iteration value as the year
     * into the query, effectively obtaining a count including every year within that range.
     * @param type The type name is used from this object to compare against the type field in the database.
     * @param month The month for which the count
     * @return An Integer representing the number of occurrences of a given appointment type within a given month (between
     * the years of 2000 and 2100).*/
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

    /**
    *Given an appointment id, and an appointment object, updates an appointment record in the database.
     * @param existingId The Appointment_ID of the record to be updated.
     * @param newAppointment The Appointment object containing the data to be written to the existing appointment.
     * @return A boolean representing whether the operation was successful.*/
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

    /**
     * Uses an Appointment object to write a new record to the appointment table in the database.
     * @param appointment The Appointment object containing the data which will be written to the database.
     * @return A boolean representing whether the operation was successful.*/
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

    /**
     * Given an Appointment object, deletes the appropriate appointment record from the database.
     * @param appointment Appointment object representing the record to be deleted from the database.
     * @return A boolean representing whether the operation was successful.*/
    public static boolean deleteAppointment (Appointment appointment) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getDeleteAppointmentPreparedStatement(appointment);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Given a ResultSet from a database query, generates a new Appointment object for use in the application.
     * @param resultSet A ResultSet obtained from calling executeQuery on a PreparedStatement from util.QueryUtil.
     * @return An Appointment object generated based on the data in the resultSet parameter.*/
    private static Appointment generateAppointment (ResultSet resultSet) throws SQLException {
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
