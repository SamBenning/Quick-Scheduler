module C195 {
    requires javafx.fxml;
    requires javafx.controls;

    requires java.sql;

    opens sample to javafx.graphics;

    exports sample;
    exports sample.controller;
    exports sample.controller.customerControllers;
    exports sample.controller.appointmentControllers;
    exports sample.model;
    opens sample.controller to javafx.graphics;
    opens sample.model to javafx.base;
}