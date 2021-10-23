package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Customer;
import sample.model.User;
import sample.util.QueryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class CustomerDao {

    public static ObservableList<Customer> getAllCustomers () {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            String query = "select * from customers";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Customer newCustomer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getInt("Division_ID")
                );
                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

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

    public static boolean updateCustomer(Customer customer) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getUpdateCustomerPreparedStatement(customer);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Customer getCustomer(int customerId) {
        try {
            PreparedStatement preparedStatement = QueryUtil.getCustomerByIdPreparedStatement(customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getInt("Division_ID")
                );
                return customer;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

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

