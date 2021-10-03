package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Customer;
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
   /* public ObservableList<Customer> getCustomer (String title);
    public boolean addCustomer (Customer customer);
    public boolean updateCustomer(int existingID, Customer newCustomer);
    public boolean deleteCustomer (Customer customer);*/
}
