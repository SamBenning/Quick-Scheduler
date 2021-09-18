package sample.dao.impl;

import javafx.collections.ObservableList;
import sample.dao.CountryDao;
import sample.model.Country;

import java.sql.SQLException;

public class CountryDaoImpl implements CountryDao {
    @Override
    public ObservableList<Country> getAllCountries() throws SQLException {
        return null;
    }

    @Override
    public ObservableList<Country> getCountry(String title) throws SQLException {
        return null;
    }

    @Override
    public boolean addCountry(Country country) throws SQLException {
        return false;
    }

    @Override
    public boolean updateCountry(int existingID, Country newCountry) {
        return false;
    }

    @Override
    public boolean deleteCountry(Country country) throws SQLException {
        return false;
    }
}
