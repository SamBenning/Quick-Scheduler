package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CountryDao {

    public static ObservableList<Country> getAllCountries () {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        try {
            String query = "select * from countries;";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Country country = new Country (
                        resultSet.getInt("Country_ID"),
                        resultSet.getString("Country")
                );
                countries.add(country);
            }
            return countries;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }
}
