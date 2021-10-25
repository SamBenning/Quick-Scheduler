package sample.model;

/**
 * Model object class representing firstleveldivisions from the database.*/
public class FirstLevelDivision {

    private int divisionId;
    private String divisionName;
    private int countryId;

    public FirstLevelDivision(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    public String getDivisionName() {
        return divisionName;
    }

}
