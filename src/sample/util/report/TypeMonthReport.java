package sample.util.report;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sample.dao.AppointmentDao;
import sample.model.AppointmentType;

import java.time.Month;

public class TypeMonthReport extends Report{

    private static AppointmentType type;
    private static Month month;
    private static ComboBox<AppointmentType> types;
    private static ComboBox<Month> months;
    private static Label totalLabel;
    private static Label totalCount;



    public TypeMonthReport(String name) {
        super(name);
    }

    public static void askType() {
        types = new ComboBox<>();
        types.setPromptText("Select appointment type");
        Insets insets = new Insets(0,0,0,10);
        HBox.setMargin(types, insets);
        types.getItems().addAll(AppointmentType.getAllTypes());
        types.addEventHandler(ActionEvent.ACTION,
                actionEvent -> setType(actionEvent));
        dynamicComboArea.getChildren().add(types);
    }

    public static void askMonth() {
        months = new ComboBox<>();
        months.setPromptText("Select month");
        Insets insets = new Insets(0,0,0,10);
        HBox.setMargin(months, insets);
        months.getItems().addAll(Month.values());
        months.addEventHandler(ActionEvent.ACTION,
                actionEvent -> setMonth(actionEvent));
        dynamicComboArea.getChildren().add(months);
    }

    public static void setType(ActionEvent actionEvent) {
        type = ((ComboBox<AppointmentType>) actionEvent.getSource()).getSelectionModel().getSelectedItem();
        if (month != null) {
            countTotal();
        }
    }

    public static void setMonth(ActionEvent actionEvent) {
        month = ((ComboBox<Month>) actionEvent.getSource()).getSelectionModel().getSelectedItem();
        if (type != null) {
            countTotal();
        }
    }

    public static void countTotal() {
        Integer count = AppointmentDao.getTypeMonthCount(type, month);
        if (totalLabel == null) {
            totalLabel = new Label("COUNT: ");
            totalLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12.0));
            Insets insets = new Insets(0,0,0,10);
            HBox.setMargin(totalLabel, insets);
            dynamicComboArea.getChildren().add(totalLabel);
        }
        if (totalCount == null) {
            totalCount = new Label(count.toString());
            totalCount.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12.0));
            dynamicComboArea.getChildren().add(totalCount);
        } else {
            totalCount.setText(count.toString());
        }

    }

    public static void setupCombos() {
        dynamicComboArea.getChildren().clear();
        dynamicTableArea.getChildren().clear();
        askType();
        askMonth();
    }

    @Override
    public ReportListener getOnSelection() {
        return () -> setupCombos();
    }

}
