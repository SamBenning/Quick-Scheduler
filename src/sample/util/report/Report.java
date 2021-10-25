package sample.util.report;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Class from which each report type class inherits. Stores static observable list of all Report objects used in the
 * application. Contains references to HBoxes from the main menu as member variables so that the children report classes
 * can dynamically add elements to them to display data.*/
public abstract class Report {
    private String name;
    private static ObservableList<Report> reports;
    protected static HBox dynamicComboArea;
    protected static HBox dynamicTableArea;

    public Report(String name) {
        this.name = name;
    }

    /**
     * Each child report class defines their on implementation of this method. ReportListener is a functional interface,
     * so it's essentially returning a callback method specifying what needs to be done when a user selects the report
     * type from the combo box in the main menu.*/
    public abstract ReportListener getOnSelection();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is so that the report name will be displayed in the main menu combo box.*/
    @Override
    public String toString() {
        return name;
    }



    public static ObservableList<Report> getReports() {
        return reports;
    }

    public static void setReports(ObservableList<Report> reports) {
        Report.reports = reports;
    }

    /**
     * Simply takes in an HBox and sets it to the dynamicComboArea member variable. dynamicComboArea is used in children
     * report classes to add combo boxes as necessary.*/
    public static void setDynamicComboArea(HBox dynamicComboArea) {
        Report.dynamicComboArea = dynamicComboArea;
    }

    /**
     * Simply takes an Hbox and sets it to the dynamicTableArea member variable. dynamicTableArea is used in children report
     * classes to add TableViews as necessary.*/
    public static void setDynamicTableArea(HBox dynamicTableArea) {
        Report.dynamicTableArea = dynamicTableArea;
    }

    /**
     * Helper function which sets the margins of the passed in ComboBox so that elements have some spacing in between.*/
    protected static void setMargin(ComboBox<?> element) {
        Insets insets = new Insets(0,0,0,10);
        HBox.setMargin(element, insets);
    }

    /**
     * Helper function which sets the margins of the passed in Label so that elements have some spacing in between.*/
    protected static void setMargin(Label element) {
        Insets insets = new Insets(0,0,0,10);
        HBox.setMargin(element, insets);
    }
}
