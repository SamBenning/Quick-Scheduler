package sample.util;

import sample.dao.JDBC;
import sample.model.Appointment;
import sample.model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class QueryUtil {
    
    private static PreparedStatement addCustomerPreparedStatement;
    private static PreparedStatement updateCustomerPreparedStatement;
    private static PreparedStatement getAllUsersPreparedStatement;
    private static PreparedStatement getCustomerByIdPreparedStatement;
    private static PreparedStatement updateAppointmentPreparedStatement;

    static {
        String addCustomerQ = "insert into customers(Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                " values(?,?,?,?,?)";
        String updateCustomerQ = "update customers set Customer_Name = ?, Address = ?, Postal_Code = ?," +
                " Phone = ?,Division_ID = ? where Customer_ID = ?;";
        String getAllUsersQ = "select * from users;";
        String getCustomerByIdQ = "select * from customers where Customer_ID = ?;";
        String updateAppointmentQ = "update appointments set Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? where Appointment_ID = ?;";
        try {
            addCustomerPreparedStatement = JDBC.connection.prepareStatement(addCustomerQ);
            updateCustomerPreparedStatement = JDBC.connection.prepareStatement(updateCustomerQ);
            getAllUsersPreparedStatement = JDBC.connection.prepareStatement(getAllUsersQ);
            getCustomerByIdPreparedStatement = JDBC.connection.prepareStatement(getCustomerByIdQ);
            updateAppointmentPreparedStatement = JDBC.connection.prepareStatement(updateAppointmentQ);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getCustomerString(int customerId) throws SQLException {
        String query = "select Customer_Name from customers where Customer_ID = ?;";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setInt(1, customerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1) + " (ID: " + customerId + ")";
    }

    public static String getUserString(int userId) throws SQLException {
        String query = "select User_Name from users where User_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1) + " (ID: " + userId + ")";
    }

    public static String getContactString(int contactId) throws SQLException {
        String query = "select Contact_Name from contacts where Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setInt(1, contactId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1) + " (ID: " + contactId + ")";
    }

    public static PreparedStatement getAddAppointmentPreparedStatement (Appointment appointment) throws SQLException {
        String query = "insert into appointments(Title, Description, Location," +
                "Type, Start, End, Customer_ID, User_ID, Contact_ID) values(?,?,?,?,?,?,?,?,?)";


        LocalDateTime startLocalDateTime = appointment.getStart();
        LocalDateTime endLocalDateTime = appointment.getEnd();
        // Found this solution on stackoverflow:
        // https://stackoverflow.com/questions/34626382/convert-localdatetime-to-localdatetime-in-utc
        startLocalDateTime = appointment.getStart(); //startLocalDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        endLocalDateTime = appointment.getEnd(); //endLocalDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();


        DateTimeFormatter sqlFormatter = DateTimeFormatter.ISO_DATE_TIME;
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setString(1, appointment.getTitle());
        preparedStatement.setString(2, appointment.getDescription());
        preparedStatement.setString(3, appointment.getLocation());
        preparedStatement.setString(4, appointment.getType());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(startLocalDateTime));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(endLocalDateTime));
        preparedStatement.setInt(7, appointment.getCustomerId());
        preparedStatement.setInt(8, appointment.getUserId());
        preparedStatement.setInt(9, appointment.getContactId());

        return preparedStatement;
    }

    public static PreparedStatement getAddCustomerPreparedStatement (Customer customer) throws SQLException {
        addCustomerPreparedStatement.setString(1, customer.getCustomerName());
        addCustomerPreparedStatement.setString(2, customer.getAddress());
        addCustomerPreparedStatement.setString(3, customer.getPostalCode());
        addCustomerPreparedStatement.setString(4, customer.getPhone());
        addCustomerPreparedStatement.setInt(5, customer.getDivisionId());
        return addCustomerPreparedStatement;
    }

    public static PreparedStatement getUpdateCustomerPreparedStatement (Customer customer) throws SQLException {
        updateCustomerPreparedStatement.setString(1, customer.getCustomerName());
        updateCustomerPreparedStatement.setString(2, customer.getAddress());
        updateCustomerPreparedStatement.setString(3, customer.getPostalCode());
        updateCustomerPreparedStatement.setString(4, customer.getPhone());
        updateCustomerPreparedStatement.setInt(5, customer.getDivisionId());
        updateCustomerPreparedStatement.setInt(6, customer.getCustomerId());
        return updateCustomerPreparedStatement;
    }

    public static PreparedStatement getAllUsersPreparedStatement () {
        return getAllUsersPreparedStatement;
    }

    public static PreparedStatement getCustomerByIdPreparedStatement (int customerId) throws SQLException {
        getCustomerByIdPreparedStatement.setInt(1, customerId);
        return getCustomerByIdPreparedStatement;
    }

    public static PreparedStatement getUpdateAppointmentPreparedStatement (int id, Appointment updatedApp) throws SQLException {
        updateAppointmentPreparedStatement.setString(1, updatedApp.getTitle());
        updateAppointmentPreparedStatement.setString(2, updatedApp.getDescription());
        updateAppointmentPreparedStatement.setString(3, updatedApp.getLocation());
        updateAppointmentPreparedStatement.setString(4, updatedApp.getType());
        updateAppointmentPreparedStatement.setTimestamp(5, Timestamp.valueOf(updatedApp.getStart()));
        updateAppointmentPreparedStatement.setTimestamp(6, Timestamp.valueOf(updatedApp.getEnd()));
        updateAppointmentPreparedStatement.setInt(7, updatedApp.getCustomerId());
        updateAppointmentPreparedStatement.setInt(8, updatedApp.getUserId());
        updateAppointmentPreparedStatement.setInt(9, updatedApp.getContactId());
        updateAppointmentPreparedStatement.setInt(10, id);
        return  updateAppointmentPreparedStatement;
    }

}
