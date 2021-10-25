package sample.util;

import sample.dao.JDBC;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;
import sample.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utility class that generates and stores all of the PreparedStatements used in the DAO classes to query.
 * Provides a number of methods that take in either a model object instance, or an id that it is used to query
 * or update the database. PreparedStatements are saved to member variables appropriated named, and each
 * member method will set the parameters of the PreparedStatement before returning it.
 * These methods are all very straightforward, so no specific comments are provided for them.
 * <br>
 * The static block is used to ensure all of the PreparedStatements have been build and saved to the appropriate
 * member variable.*/
public final class QueryUtil {
    
    private static PreparedStatement addCustomerPreparedStatement;
    private static PreparedStatement updateCustomerPreparedStatement;
    private static PreparedStatement getAllUsersPreparedStatement;
    private static PreparedStatement getCustomerByIdPreparedStatement;
    private static PreparedStatement updateAppointmentPreparedStatement;
    private static PreparedStatement getAppointmentForCustomerPreparedStatement;
    private static PreparedStatement getAppointmentInDateRange;
    private static PreparedStatement getTypeMonthCountPreparedStatement;
    private static PreparedStatement getAppointmentsByContactPreparedStatement;
    private static PreparedStatement getAppointmentsByUserPreparedStatement;
    private static PreparedStatement getAppointmentsByCustomerPreparedStatement;
    private static PreparedStatement getDeleteAppointmentPreparedStatement;
    private static PreparedStatement getDeleteCustomerPreparedStatement;

    static {
        String addCustomerQ = "insert into customers(Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                " values(?,?,?,?,?)";
        String updateCustomerQ = "update customers set Customer_Name = ?, Address = ?, Postal_Code = ?," +
                " Phone = ?,Division_ID = ? where Customer_ID = ?;";
        String getAllUsersQ = "select * from users;";
        String getCustomerByIdQ = "select * from customers where Customer_ID = ?;";
        String updateAppointmentQ = "update appointments set Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? where Appointment_ID = ?;";
        String getAppointmentForCustomerQ = "select * from appointments where Customer_ID = ?";
        String getAppointInDateRangeQ = "select * from appointments where Start between ? and ?;";
        String getTypeMonthCountQ = "select count(Appointment_ID) as \"Count\" from appointments where Type = ? and" +
                " (Start between ? and ?);";
        String getAppointmentsByContactQ = "select * from appointments where Contact_ID = ?;";
        String getAppointmentsByUserQ = "select * from appointments where User_ID = ?;";
        String getAppointmentsByCustomerQ = "select * from appointments where Customer_ID = ?;";
        String getDeleteAppointmentQ = "delete from appointments where Appointment_ID = ?;";
        String getDeleteCustomerQ = "delete from customers where Customer_ID = ?;";
        try {
            addCustomerPreparedStatement = JDBC.connection.prepareStatement(addCustomerQ);
            updateCustomerPreparedStatement = JDBC.connection.prepareStatement(updateCustomerQ);
            getAllUsersPreparedStatement = JDBC.connection.prepareStatement(getAllUsersQ);
            getCustomerByIdPreparedStatement = JDBC.connection.prepareStatement(getCustomerByIdQ);
            updateAppointmentPreparedStatement = JDBC.connection.prepareStatement(updateAppointmentQ);
            getAppointmentForCustomerPreparedStatement = JDBC.connection.prepareStatement(getAppointmentForCustomerQ);
            getAppointmentInDateRange = JDBC.connection.prepareStatement(getAppointInDateRangeQ);
            getTypeMonthCountPreparedStatement = JDBC.connection.prepareStatement(getTypeMonthCountQ);
            getAppointmentsByContactPreparedStatement = JDBC.connection.prepareStatement(getAppointmentsByContactQ);
            getAppointmentsByUserPreparedStatement = JDBC.connection.prepareStatement(getAppointmentsByUserQ);
            getDeleteAppointmentPreparedStatement = JDBC.connection.prepareStatement(getDeleteAppointmentQ);
            getAppointmentsByCustomerPreparedStatement = JDBC.connection.prepareStatement(getAppointmentsByCustomerQ);
            getDeleteCustomerPreparedStatement = JDBC.connection.prepareStatement(getDeleteCustomerQ);

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


        LocalDateTime startLocalDateTime;
        LocalDateTime endLocalDateTime;
        // Found this solution on stackoverflow:
        // https://stackoverflow.com/questions/34626382/convert-localdatetime-to-localdatetime-in-utc
        startLocalDateTime = appointment.getStart(); //startLocalDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        endLocalDateTime = appointment.getEnd(); //endLocalDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
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

    public static PreparedStatement getAppointmentForCustomerPreparedStatement(int customerId) throws SQLException {
        getAppointmentForCustomerPreparedStatement.setInt(1, customerId);
        return getAppointmentForCustomerPreparedStatement;
    }

    public static PreparedStatement getAppointmentInDateRange(LocalDate firstDate, LocalDate secondDate) throws SQLException{
        getAppointmentInDateRange.setTimestamp(1, Timestamp.valueOf(firstDate.atStartOfDay()));
        getAppointmentInDateRange.setTimestamp(2, Timestamp.valueOf(secondDate.atTime(23, 59, 59, 999)));
        return getAppointmentInDateRange;
    }

    public static PreparedStatement getTypeMonthCountPreparedStatement(String type, LocalDateTime monthStart, LocalDateTime monthEnd) throws SQLException {
        getTypeMonthCountPreparedStatement.setString(1, type);
        getTypeMonthCountPreparedStatement.setTimestamp(2, Timestamp.valueOf(monthStart));
        getTypeMonthCountPreparedStatement.setTimestamp(3, Timestamp.valueOf(monthEnd));
        return getTypeMonthCountPreparedStatement;
    }

    public static PreparedStatement getAppointmentsByContactPreparedStatement(Contact contact) throws SQLException{
        getAppointmentsByContactPreparedStatement.setInt(1, contact.getContactId());
        return  getAppointmentsByContactPreparedStatement;
    }

    public static PreparedStatement getAppointmentsByUserPreparedStatement(User user) throws SQLException {
        getAppointmentsByUserPreparedStatement.setInt(1, user.getUserId());
        return getAppointmentsByUserPreparedStatement;
    }

    public static PreparedStatement getAppointmentsByCustomerPreparedStatement(Customer customer) throws SQLException {
        getAppointmentsByCustomerPreparedStatement.setInt(1, customer.getCustomerId());
        return getAppointmentsByCustomerPreparedStatement;
    }

    public static PreparedStatement getDeleteAppointmentPreparedStatement(Appointment appointment) throws SQLException {
        getDeleteAppointmentPreparedStatement.setInt(1, appointment.getAppointmentId());
        return getDeleteAppointmentPreparedStatement;
    }

    public static PreparedStatement getDeleteCustomerPreparedStatement(Customer customer) throws SQLException {
        getDeleteCustomerPreparedStatement.setInt(1, customer.getCustomerId());
        return getDeleteCustomerPreparedStatement;
    }

}
