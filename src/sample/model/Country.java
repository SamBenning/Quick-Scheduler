package sample.model;

/**
 * Model object class to represent country records from the database.*/
public class Country {

    private final int countryId;
    private final String countryName;

    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

}
