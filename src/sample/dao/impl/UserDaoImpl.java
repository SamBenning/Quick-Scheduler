package sample.dao.impl;

import javafx.collections.ObservableList;
import sample.dao.JDBC;
import sample.dao.UserDao;
import sample.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImpl {

    public ObservableList<User> getAllUsers() {
        return null;
    }




    public boolean addUser(User user) throws SQLException {
        return false;
    }


    public boolean updateUser(int existingID, User newUser) {
        return false;
    }


    public boolean deleteUser(User user) throws SQLException {
        return false;
    }
}
