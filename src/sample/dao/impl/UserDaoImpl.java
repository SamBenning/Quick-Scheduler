package sample.dao.impl;

import javafx.collections.ObservableList;
import sample.dao.UserDao;
import sample.model.User;

import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public ObservableList<User> getAllUsers() throws SQLException {
        return null;
    }

    @Override
    public ObservableList<User> getUser(String title) throws SQLException {
        return null;
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        return false;
    }

    @Override
    public boolean updateUser(int existingID, User newUser) {
        return false;
    }

    @Override
    public boolean deleteUser(User user) throws SQLException {
        return false;
    }
}
