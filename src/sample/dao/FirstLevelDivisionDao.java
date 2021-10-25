package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles sending queries and updates to the firstleveldivision table in the database*/
public class FirstLevelDivisionDao {

    /**
     * Given a countryId, builds an observable of matching divisions.
     * @param countryId The value that will be compared against the Country_ID field to find matches.
     * @return An observable list of FirstLevelDivision objects.*/
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

    /**
     * Given a division name, returns the division id of the first record matching that name.
     * @param divisionName A String that will be checked against the division name field in the database.
     * @return An int representing the id of the first matching record for the division name in the table.
     * <br>
     * If no match is found, returns -1.*/
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

    /**
     * Given a divisionId, finds the countryId for that division. This works because there is a 1:n relationship
     * between countries and divisions.
     * @param divisionId The division id to be checked.
     * @return An int representing the country id of the country that maps to the passed in division id.
     * <br>
     * returns -1 if no match is found.*/
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

    /**
     * Given a division id, returns a String representing that division name.
     * @param divisionId The division id to be checked. Because this is the primary key, at most 1 match will be found.
     * @return A String representing the division name. If no match is found, returns null;*/
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
