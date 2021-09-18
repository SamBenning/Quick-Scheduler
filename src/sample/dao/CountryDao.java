package sample.dao;

import javafx.collections.ObservableList;
import sample.model.Country;

import java.sql.SQLException;

public interface CountryDao {
    public ObservableList<Country> getAllCountries () throws SQLException;
    public ObservableList<Country> getCountry (String title) throws SQLException;
    public boolean addCountry (Country country) throws SQLException;
    public boolean updateCountry(int existingID, Country newCountry);
    public boolean deleteCountry (Country country) throws SQLException;
}
