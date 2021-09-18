package sample.dao;

import javafx.collections.ObservableList;
import sample.model.FirstLevelDivision;

import java.sql.SQLException;

public interface FirstLevelDivisionDao {
    public ObservableList<FirstLevelDivision> getAllDivisions () throws SQLException;
    public ObservableList<FirstLevelDivision> getDivision (String title) throws SQLException;
    public boolean addDivision (FirstLevelDivision division) throws SQLException;
    public boolean updateDivision(int existingID, FirstLevelDivision newDivision);
    public boolean deleteDivision (FirstLevelDivision division) throws SQLException;
}
