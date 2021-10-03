package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDao {

    public static ObservableList<FirstLevelDivision> getDivisionsByCountry (int countryId) {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String query = "select Division_ID, Division from first_level_divisions where Country_ID = ?";
        try {
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1,countryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                FirstLevelDivision division = new FirstLevelDivision(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        countryId
                );
                divisions.add(division);
            }
            return divisions;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisions;
    }

    public static int getDivisionId(String divisionName) {
        String query = "select Division_ID from first_level_divisions where Division = ?";
        try {
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setString(1, divisionName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Division_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
}
