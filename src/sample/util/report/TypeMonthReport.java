package sample.util.report;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sample.dao.AppointmentDao;
import sample.model.AppointmentType;

import java.time.Month;

/**
 * Defines the appointment by type and month report type.*/
public class TypeMonthReport extends Report{

    private static AppointmentType type;
    private static Month month;
    private static Label totalLabel;
    private static Label totalCount;



    public TypeMonthReport(String name) {
        super(name);
    }

    /**
     * ~~LAMBDA EXPRESSION~~
     * <br>
     * Creates a combo box populated with AppointmentType options. These appointment types are the same
     * options available when creating and modifying appointments, as all appointment types in the program
     * are obtained from the static observable list within the AppointmentType class. Adds the combo box to
     * the dynamicComboArea. Adds an event handler to the combo box which calls the setType method when a
     * selection is made.
     * <br>
     * A lambda expression is used to define the event handler. A lambda expression is convenient here, as it
     * can simply be passed in as a parameter to the addEventHandler method.*/
    public static void askType() {
        ComboBox<AppointmentType> types = new ComboBox<>();
        types.setPromptText("Select appointment type");
        setMargin(types);
        types.getItems().addAll(AppointmentType.getAllTypes());
        types.addEventHandler(ActionEvent.ACTION,
                actionEvent -> setType(actionEvent));
        dynamicComboArea.getChildren().add(types);
    }

    /**
     * ~~LAMBDA EXPRESSION~~
     * <br>
     * Creates a combo box populated with all of the months within the java.time.Month package. Adds the combo
     * box the the dynamicComboArea. Adds an event handler to the combo box which calls the setMonth method
     * when a selection is made.
     * <br>
     * A lambda expression is used to define the event handler. A lambda expression is convenient here, as it
     * can simply be passed in as a parameter to the addEventHandler method.*/
    public static void askMonth() {
        ComboBox<Month> months = new ComboBox<>();
        months.setPromptText("Select month");
        setMargin(months);
        months.getItems().addAll(Month.values());
        months.addEventHandler(ActionEvent.ACTION,
                actionEvent -> setMonth(actionEvent));
        dynamicComboArea.getChildren().add(months);
    }

    /**
     * Sets the type member variable to a reference to whichever type is selected in the combo box
     * from which the actionEvent originated. If month field is not null, then it means both combo boxes
     * have a selection, and the countTotal method can be called.*/
    public static void setType(ActionEvent actionEvent) {
        type = ((ComboBox<AppointmentType>) actionEvent.getSource()).getSelectionModel().getSelectedItem();
        if (month != null) {
            countTotal();
        }
    }

    /**
     * Sets the month member variable toa  reference to whichever month is selected in the combo box from
     * which the actionEvent originated. If the type field is not null, then it means both combo boxes
     * have a selection, and the countTotal method can be called.*/
    public static void setMonth(ActionEvent actionEvent) {
        month = ((ComboBox<Month>) actionEvent.getSource()).getSelectionModel().getSelectedItem();
        if (type != null) {
            countTotal();
        }
    }

    /**
     * Based on selected type and month, performs a query of the database and displays a a count of the number
     * of appointments matching both selections.*/
    public static void countTotal() {
        Integer count = AppointmentDao.getTypeMonthCount(type, month);
        if (totalLabel == null) {
            totalLabel = new Label("COUNT: ");
            totalLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12.0));
            setMargin(totalLabel);
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

    /**
     * Performs initial setup of the combo boxes. Clears dynamicComboArea and dynamicTableArea to get rid of
     * any elements displayed by prior selected reports.*/
    public static void setupCombos() {
        dynamicComboArea.getChildren().clear();
        dynamicTableArea.getChildren().clear();
        askType();
        askMonth();
    }

    /**
     * ~~LAMBDA EXPRESSION~~
     * <br>
     * Provides a lambda expression returning the function. The lambda expression is useful in this case because it allows
     * any of the report subclass on selection functionality to be called from the MainMenu. The Main Controller simply
     * gets the selected report class form a combo box and calls selection.getOnSelection.onSelectReport. Each Report
     * subclass simply has to define its required functionality in the getOnSelection method and return it as a callback,
     * which the lambda expression does in a concise manner.*/
    @Override
    public ReportListener getOnSelection() {
        return () -> setupCombos();
    }

}
