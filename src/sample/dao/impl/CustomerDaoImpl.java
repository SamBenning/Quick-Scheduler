package sample.dao.impl;

import javafx.collections.ObservableList;
import sample.dao.CustomerDao;
import sample.model.Customer;

import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        return null;
    }

    @Override
    public ObservableList<Customer> getCustomer(String title) throws SQLException {
        return null;
    }

    @Override
    public boolean addCustomer(Customer customer) throws SQLException {
        return false;
    }

    @Override
    public boolean updateCustomer(int existingID, Customer newCustomer) {
        return false;
    }

    @Override
    public boolean deleteCustomer(Customer customer) throws SQLException {
        return false;
    }
}
