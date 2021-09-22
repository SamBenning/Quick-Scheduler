package sample.util;

import sample.dao.JDBC;
import sample.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class QueryUtil {

    private Statement selectAllStatement;
    private static Statement selectAll;
    private static String selectQuery = "select ? from ? where ? = ?";

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
        preparedStatement.setInt(7, 1);
        preparedStatement.setInt(8, 1);
        preparedStatement.setInt(9, 1);

        System.out.println("SQL query:");
        System.out.println(query);

        return preparedStatement;

    }


    private String query;

    public static String getFormattedCustomerString(int customerId) {
        String query = "select Customer_Name from customers where Customer_ID = ?";
        return "";
    }
}
