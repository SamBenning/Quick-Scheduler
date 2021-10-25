package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents the a selectable appointment type. This is used to populate a combo box. The database
 * does not have an appointment type entity--this is simply used to control user input.*/
public class AppointmentType {
    private String name;
    private static ObservableList<AppointmentType>allTypes;

    static {
        allTypes = FXCollections.observableArrayList();
    }

    public AppointmentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ObservableList<AppointmentType> getAllTypes() {
        return allTypes;
    }

    public static void setAllTypes(ObservableList<AppointmentType> allTypes) {
        AppointmentType.allTypes = allTypes;
    }

    @Override
    public String toString() {
        return name;
    }
}
