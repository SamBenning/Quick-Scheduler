package sample.util;

import sample.dao.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


    private String query;

    public static String getFormattedCustomerString(int customerId) {
        String query = "select Customer_Name from customers where Customer_ID = ?";
        return "";
    }
}
