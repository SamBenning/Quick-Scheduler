package sample.dao;

import javafx.collections.ObservableList;
import sample.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDao {


    public static boolean checkCredentials(String username, String password) {
        try {
            String query = "select 1 from users where User_Name = ? and Password = ?;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }
}
