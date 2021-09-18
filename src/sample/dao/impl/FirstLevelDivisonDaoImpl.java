package sample.dao.impl;

import javafx.collections.ObservableList;
import sample.dao.FirstLevelDivisionDao;
import sample.model.FirstLevelDivision;

import java.sql.SQLException;

public class FirstLevelDivisonDaoImpl implements FirstLevelDivisionDao {
    @Override
    public ObservableList<FirstLevelDivision> getAllDivisions() throws SQLException {
        return null;
    }

    @Override
    public ObservableList<FirstLevelDivision> getDivision(String title) throws SQLException {
        return null;
    }

    @Override
    public boolean addDivision(FirstLevelDivision division) throws SQLException {
        return false;
    }

    @Override
    public boolean updateDivision(int existingID, FirstLevelDivision newDivision) {
        return false;
    }

    @Override
    public boolean deleteDivision(FirstLevelDivision division) throws SQLException {
        return false;
    }
}
