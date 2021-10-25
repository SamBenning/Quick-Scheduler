package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Customer;
import sample.util.QueryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles sending queries and updates to the customers table in the database.*/
public final class CustomerDao {

    /**
     * Queries the database and builds an observable list of all customers.
     * @return An observable list containing Customer objects representing all customer records in the database.*/
    public static ObservableList<Customer> getAllCustomers () {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            String query = "select * from customers";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Customer newCustomer = generateCustomer(resultSet);
                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Executes an update on the database to add a customer record to the customers table.
     * @param customer A Customer object representing the data to be added to the customers table.
     * @return A boolean representing whether the operation was successful.*/
    public static boolean addCustomer(Customer customer) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getAddCustomerPreparedStatement(customer);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Executes an update to the database to update the data in an existing customer record.
     * @param customer A customer object representing the data to be written to the customer record.
     * @return A boolean representing whether the operation was successful.*/
    public static boolean updateCustomer(Customer customer) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getUpdateCustomerPreparedStatement(customer);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Queries the database and gets a customer based on a customerId.
     * @param customerId The Customer_ID of the record in the database to be retrieved.
     * @return A Customer object representing the data of the customer record in the database.*/
    public static Customer getCustomer(int customerId) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getCustomerByIdPreparedStatement(customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return generateCustomer(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Given a customer object, deletes the appropriate customer record from the database.
     * @param customer Customer object representing the record to be deleted from the database.
     * @return A boolean representing whether the operation was successful.*/
    public static boolean deleteCustomer(Customer customer) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getDeleteCustomerPreparedStatement(customer);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Given a ResultSet from a database query, generates a new Customer object for use in the application.
     * @param resultSet A ResultSet obtained from calling executeQuery on a PreparedStatement from util.QueryUtil.
     * @return A Customer object generated based on the data in the resultSet parameter.*/
    private static Customer generateCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
            resultSet.getInt("Customer_ID"),
            resultSet.getString("Customer_Name"),
            resultSet.getString("Address"),
            resultSet.getString("Postal_Code"),
            resultSet.getString("Phone"),
            resultSet.getInt("Division_ID"));
    }
}

