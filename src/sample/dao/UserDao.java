package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.User;
import sample.util.QueryUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles sending queries and updates to the users table in the database.*/
public final class UserDao {

    /**
     * Queries the database and returns an observable list representing all of the user records.
     * @return An observable list of User objects.*/
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = QueryUtil.getAllUsersPreparedStatement();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User newUser = new User(
                        resultSet.getString("User_Name"),
                        "dummy"
                );
                newUser.setUserId(resultSet.getInt("User_ID"));
                users.add(newUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Given a username and password, queries the database and checks for a record where both fields match.
     * @param username The username typed in the username field on the log-in screen.
     * @param password The password typed in the password field on the log-in screen.
     * @return A boolean representing whether a user with matching credentials exists in the database.*/
    public static boolean checkCredentials(String username, String password) {
        try {
            String query = "select 1 from users where User_Name = ? and Password = ?;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e){
            System.out.println("Login failed");
            return false;
        }
    }
}
