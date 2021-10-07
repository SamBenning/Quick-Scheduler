package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Country;
import sample.model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static int getCountryIdByDivisionId(int divisionId) {
        try {
            String query = "select Country_ID from first_level_divisions where Division_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1, divisionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("Country_ID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public static ObservableList<FirstLevelDivision> getAllDivisions() {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        try {
            String query = "select * from first_level_divisions";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                FirstLevelDivision division = new FirstLevelDivision(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("Country_ID")
                );
                divisions.add(division);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisions;
    }

    public static String getDivisionName(int divisionId) {
        String query = "select Division from first_level_divisions where Division_ID = ?";
        try {
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1, divisionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("Division");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

}
