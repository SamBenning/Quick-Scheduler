package sample.util.report;

/**
 * This is a functional interface that is used to return callbacks from report subclasses so that customer functionality
 * cal be called for each report type in the main menu.*/
public interface ReportListener {
    void onSelectReport();
}
