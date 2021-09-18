package sample.dao;

import javafx.collections.ObservableList;
import sample.model.User;

import java.sql.SQLException;

public interface UserDao {

    public ObservableList<User> getAllUsers () throws SQLException;
    public ObservableList<User> getUser (String title) throws SQLException;
    public boolean addUser (User user) throws SQLException;
    public boolean updateUser(int existingID, User newUser);
    public boolean deleteUser (User user) throws SQLException;

}
