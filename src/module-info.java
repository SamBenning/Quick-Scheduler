module C195 {
    requires javafx.fxml;
    requires javafx.controls;

    requires java.sql;

    opens sample to javafx.graphics;

    exports sample;
    exports sample.controller;
    opens sample.controller to javafx.graphics;
}