package sample.dao;

import javafx.collections.ObservableList;
import sample.model.Customer;

import java.sql.SQLException;

public interface CustomerDao {
    public ObservableList<Customer> getAllCustomers () throws SQLException;
    public ObservableList<Customer> getCustomer (String title) throws SQLException;
    public boolean addCustomer (Customer customer) throws SQLException;
    public boolean updateCustomer(int existingID, Customer newCustomer);
    public boolean deleteCustomer (Customer customer) throws SQLException;
}
