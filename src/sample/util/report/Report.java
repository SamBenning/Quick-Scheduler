package sample.util.report;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public abstract class Report {
    private String name;
    private static ObservableList<Report> reports;
    protected static HBox dynamicComboArea;
    protected static HBox dynamicTableArea;
    public static ReportListener onSelection;

    public Report(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract ReportListener getOnSelection();

    public static ObservableList<Report> getReports() {
        return reports;
    }

    public static void setReports(ObservableList<Report> reports) {
        Report.reports = reports;
    }

    public static HBox getDynamicComboArea() {
        return dynamicComboArea;
    }

    public static void setDynamicComboArea(HBox dynamicComboArea) {
        Report.dynamicComboArea = dynamicComboArea;
    }

    public static void setDynamicTableArea(HBox dynamicTableArea) {
        Report.dynamicTableArea = dynamicTableArea;
    }

    protected static void setMargin(ComboBox<?> element) {
        Insets insets = new Insets(0,0,0,10);
        HBox.setMargin(element, insets);
    }

    protected static void setMargin(Label element) {
        Insets insets = new Insets(0,0,0,10);
        HBox.setMargin(element, insets);
    }
}
